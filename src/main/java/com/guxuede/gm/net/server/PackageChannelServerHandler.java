/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.guxuede.gm.net.server;

import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.client.registry.pack.PlayerDisconnectedPack;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.system.component.MessageComponent;
import entityEdit.E;
import entityEdit.Mappers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;


/**
 * Handles a server-side channel.
 */
@Slf4j
public class PackageChannelServerHandler extends SimpleChannelInboundHandler<NetPack> {
    private static final AttributeKey<Entity> GAME_ENTITY = AttributeKey.valueOf("GAME_ENTITY");

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws UnknownHostException {
        log.info("channelActive");
        Channel channel = ctx.channel();
        Entity entity = E.create().with(ChannelComponent.class, e->{
            e.channel = channel;
        }).buildToWorld();
        channel.attr(GAME_ENTITY).set(entity);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, NetPack msg) throws Exception {
        log.info("received message:"  + msg);
        Entity entity = ctx.attr(GAME_ENTITY).get();
        Mappers.channelCM.get(entity).inBoundPack(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("channelInactive = [" + ctx + "]");
        Entity entity = ctx.attr(GAME_ENTITY).get();
        ctx.attr(GAME_ENTITY).set(null);

        ChannelComponent channelComponent = Mappers.channelCM.get(entity);
        Channel channel = channelComponent.channel;
        if(channel.isOpen()){
            channel.close();
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.error("handlerRemoved = [" + ctx + "]");
        ctx.attr(GAME_ENTITY).set(null);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE)
                log.info("read idle");
            else if (event.state() == IdleState.WRITER_IDLE)
                log.info("write idle");
            else if (event.state() == IdleState.ALL_IDLE)
                log.info("all idle");
        }
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("exceptionCaught = [" + cause + "]");
        cause.printStackTrace();
        ctx.close();
    }
}
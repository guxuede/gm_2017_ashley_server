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
import com.guxuede.gm.net.system.component.NetClientComponent;
import entityEdit.E;
import entityEdit.Mappers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

import java.net.UnknownHostException;


/**
 * Handles a server-side channel.
 */
public class PackageChannelServerHandler extends SimpleChannelInboundHandler<NetPack> {
    private static final AttributeKey<Entity> GAME_ENTITY = AttributeKey.valueOf("GAME_ENTITY");

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws UnknownHostException {
        System.out.println("链接进入");
        Channel channel = ctx.channel();
        Entity entity = E.create().connectedToServer(channel).buildToWorld();
        channel.attr(GAME_ENTITY).set(entity);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, NetPack msg) throws Exception {
        Entity entity = ctx.attr(GAME_ENTITY).get();
        entity.getComponent(NetClientComponent.class).inBoundPack(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("channelInactive = [" + ctx + "]");
        Entity entity = ctx.attr(GAME_ENTITY).get();
        ChannelComponent channelComponent = Mappers.channelCM.get(entity);
        channelComponent.reset();
        entity.remove(ChannelComponent.class);
        NetClientComponent netClientComponent = Mappers.netPackCM.get(entity);
        PlayerDisconnectedPack playerDisconnectedPack = new PlayerDisconnectedPack(netClientComponent.getId());
        netClientComponent.inBoundPack(playerDisconnectedPack);
        ctx.attr(GAME_ENTITY).set(null);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved = [" + ctx + "]");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE)
                System.out.println("read idle");
            else if (event.state() == IdleState.WRITER_IDLE)
                System.out.println("write idle");
            else if (event.state() == IdleState.ALL_IDLE)
                System.out.println("all idle");
        }
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
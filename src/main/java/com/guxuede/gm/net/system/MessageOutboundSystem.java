package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.MessageComponent;
import entityEdit.Mappers;
import io.netty.channel.Channel;


/**
 * Created by guxuede on 2017/6/3 .
 */
public class MessageOutboundSystem extends IteratingSystem {

    private static final Family family = Family.all(MessageComponent.class, ChannelComponent.class).get();

    public MessageOutboundSystem(){
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //process outbound message
        MessageComponent messageComponent = Mappers.messageCM.get(entity);
        ChannelComponent channelComponent = Mappers.channelCM.get(entity);
        if(channelComponent!=null){
            Channel channel = channelComponent.channel;
            messageComponent.outboundNetPacks.consumerAll(channel::write);
            channel.flush();
        }else{
            messageComponent.outboundNetPacks.clear();
        }
    }

    public void broadCaseMessage(NetPack netPack){
        getEngine().getEntitiesFor(family).forEach(e->{
            e.getComponent(MessageComponent.class).outboundPack(netPack);
        });
    }

    public void broadCaseMessageExcept(NetPack netPack, Entity except){
        getEngine().getEntitiesFor(family).forEach(e->{
            if(e!=except){
                e.getComponent(MessageComponent.class).outboundPack(netPack);
            }
        });
    }


}

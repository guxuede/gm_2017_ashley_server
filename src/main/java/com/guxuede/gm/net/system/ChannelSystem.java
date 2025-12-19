package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.client.registry.pack.PlayerDisconnectedPack;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.MessageComponent;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import entityEdit.Mappers;
import io.netty.channel.Channel;

public class ChannelSystem extends IteratingSystem {

    private static final Family family = Family.all(ChannelComponent.class).get();
    private static final Family family1 = Family.all(PlayerDataComponent.class, MessageComponent.class).get();
    private static final Family family2 = Family.all(MessageComponent.class).get();

    public ChannelSystem() {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float v) {
        ChannelComponent channelComponent = Mappers.channelCM.get(entity);
        Channel channel = channelComponent.channel;

        if(channel.isOpen()){
            //send message
            for (Entity next : getEngine().getEntitiesFor(family2)) {
                MessageComponent messageComponent = Mappers.messageCM.get(next);
                if (messageComponent.channelId.equals(channel.id())) {
                    messageComponent.outboundNetPacks.consumerAll(channel::write);
                    channel.flush();
                }
            }
        }else{
            //process channel close
            processChannelClose(channel);
            getEngine().removeEntity(entity);

        }

        //process inbound message
        channelComponent.inboundNetPacks.consumerAll(p->{
            if(p.getId() == -1){
                p.action(getEngine(),entity);
            }else {
                processPlayerMessage(p, channel);
            }
        });



    }

    private void processPlayerMessage(NetPack p, Channel channel) {
        for (Entity next : getEngine().getEntitiesFor(family1)) {
            MessageComponent messageComponent = Mappers.messageCM.get(next);
            PlayerDataComponent playerDataComponent = Mappers.playerCM.get(next);
            if (messageComponent.channelId.equals(channel.id()) && playerDataComponent.id == p.getId()) {
                messageComponent.inBoundPack(p);
                break;
            }
        }
    }


    private void processChannelClose(Channel channel) {
        for (Entity next : getEngine().getEntitiesFor(family1)) {
            MessageComponent messageComponent = Mappers.messageCM.get(next);
            PlayerDataComponent playerDataComponent = Mappers.playerCM.get(next);
            if (messageComponent.channelId.equals(channel.id())) {
                PlayerDisconnectedPack playerDisconnectedPack = new PlayerDisconnectedPack(playerDataComponent.getId());
                messageComponent.inBoundPack(playerDisconnectedPack);
            }
        }
    }
}

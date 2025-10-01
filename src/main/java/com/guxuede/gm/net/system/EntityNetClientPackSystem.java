package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.NetClientComponent;
import com.guxuede.gm.net.utils.PackQueue;
import entityEdit.Mappers;
import io.netty.channel.Channel;

import java.util.Map;


/**
 * Created by guxuede on 2017/6/3 .
 */
public class EntityNetClientPackSystem extends IteratingSystem {

    private static final Family family = Family.all(NetClientComponent.class).get();

    public EntityNetClientPackSystem(){
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //process inbound entity message
        NetClientComponent netClientComponent = Mappers.netPackCM.get(entity);
        netClientComponent.inboundNetPacks.consumerAll(p-> processNetPack(getEngine(), entity, p));

        //process outbound message
        ChannelComponent channelComponent = Mappers.channelCM.get(entity);
        if(channelComponent!=null){
            Channel channel = channelComponent.channel;
            netClientComponent.outboundNetPacks.consumerAll(p->{
                channel.write(p);
            });
            channel.flush();
        }
    }

    private void processNetPack(Engine engine, Entity entity, NetPack pack){
        pack.action(engine, entity);
    }

}

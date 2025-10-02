package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.component.MessageComponent;
import entityEdit.Mappers;


/**
 * Created by guxuede on 2017/6/3 .
 */
public class MessageInboundSystem extends IteratingSystem {

    private static final Family family = Family.all(MessageComponent.class).get();

    public MessageInboundSystem(){
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //process inbound entity message
        MessageComponent messageComponent = Mappers.messageCM.get(entity);
        messageComponent.inboundNetPacks.consumerAll(p-> processNetPack(getEngine(), entity, p));
    }

    private void processNetPack(Engine engine, Entity entity, NetPack pack){
        pack.action(engine, entity);
    }

}

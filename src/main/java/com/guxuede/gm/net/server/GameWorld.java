package com.guxuede.gm.net.server;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.CommandSystem;
import com.guxuede.gm.net.system.EntityNetClientPackSystem;
import com.guxuede.gm.net.system.GlobalNetPackSystem;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.NetClientComponent;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Created by guxuede on 2017/5/23 .
 */
public class GameWorld implements Runnable {

    public static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static final PooledEngine ENTITY_ENGINE = new PooledEngine();

    private long last = System.currentTimeMillis();

    public GameWorld(){
        ENTITY_ENGINE.addSystem(new CommandSystem(this));
        ENTITY_ENGINE.addSystem(new EntityNetClientPackSystem());
        ENTITY_ENGINE.addSystem(new GlobalNetPackSystem(ENTITY_ENGINE, this));//please keep this system at last position
    }

    @Override
    public synchronized void run() {
        ENTITY_ENGINE.update(System.currentTimeMillis() - last);
        last = System.currentTimeMillis();
    }

    private static final Family family = Family.all(ChannelComponent.class).get();

    public void broadCaseMessage(NetPack netPack){
        ENTITY_ENGINE.getEntitiesFor(family).forEach(e->{
            e.getComponent(NetClientComponent.class).outboundPack(netPack);
        });
    }
}

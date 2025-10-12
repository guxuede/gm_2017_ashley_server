package com.guxuede.gm.net.server;

import com.badlogic.ashley.core.PooledEngine;
import com.guxuede.gm.net.system.*;
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
        ENTITY_ENGINE.addSystem(new MessageInboundSystem());
        ENTITY_ENGINE.addSystem(new MessageOutboundSystem());
        ENTITY_ENGINE.addSystem(new CommandSystem(ENTITY_ENGINE));
        ENTITY_ENGINE.addSystem(new UserManagerSystem(10000));
        ENTITY_ENGINE.addSystem(new MapGateSystem());
        ENTITY_ENGINE.addSystem(new GlobalNetPackSystem(ENTITY_ENGINE, this));//please keep this system at last position
    }

    @Override
    public synchronized void run() {
        try{
            ENTITY_ENGINE.update(System.currentTimeMillis() - last);
            last = System.currentTimeMillis();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

}

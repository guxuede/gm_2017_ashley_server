package com.guxuede.gm.net.system.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.utils.PackQueue;
import io.netty.channel.ChannelId;

public class MessageComponent implements Component, Pool.Poolable{

    public ChannelComponent channelComponent;
    public PackQueue<NetPack> inboundNetPacks = new PackQueue<>();

    public synchronized void inBoundPack(NetPack netPack){
        inboundNetPacks.add(netPack);
    }

    @Override
    public void reset() {
        inboundNetPacks.clear();
    }
}

package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.utils.PackageUtils;
import entityEdit.Mappers;
import io.netty.buffer.ByteBuf;

public class PlayerDisconnectedPack extends NetPack {
    private int id;

    public PlayerDisconnectedPack(int id) {
        this.id = id;
    }

    public PlayerDisconnectedPack(ByteBuf data) {
        super(data);
        this.id = data.readInt();
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(this.id);
    }

    private static final Family family = Family.all(ChannelComponent.class).get();

    @Override
    public void action(Engine engine, Entity entity) {
        engine.getEntitiesFor(family).forEach(e->{
            ChannelComponent channelComponent = Mappers.channelCM.get(e);
            channelComponent.channel.write(PlayerDisconnectedPack.this);
        });
        engine.removeEntity(entity);
    }
}

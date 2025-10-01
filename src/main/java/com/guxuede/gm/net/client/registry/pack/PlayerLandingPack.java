package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.utils.PackageUtils;
import entityEdit.Mappers;
import io.netty.buffer.ByteBuf;

public class PlayerLandingPack extends NetPack {
    private int id, x, y;
    private String character;

    public PlayerLandingPack(int id, int x, int y, String character) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.character = character;
    }

    public PlayerLandingPack(ByteBuf data) {
        super(data);
        this.id = data.readInt();
        this.character = PackageUtils.readString(data);
        this.x = data.readInt();
        this.y = data.readInt();
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(this.id);
        PackageUtils.writeString(character, data);
        data.writeInt(this.x);
        data.writeInt(this.y);
    }

    private static final Family family = Family.all(ChannelComponent.class).get();

    @Override
    public void action(Engine engine, Entity entity) {
        engine.getEntitiesFor(family).forEach(e->{
            ChannelComponent channelComponent = Mappers.channelCM.get(e);
            channelComponent.channel.write(PlayerLandingPack.this);
        });
    }
}

package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.NetClientComponent;
import entityEdit.Mappers;
import io.netty.buffer.ByteBuf;

public class PlayerMovePack extends NetPack {

    private int id;
    private Vector2 acceleration = new Vector2();

    public PlayerMovePack(int id, Vector2 acceleration, Vector2 position) {
        this.id = id;
        this.acceleration.set(acceleration);
    }

    public PlayerMovePack(int id, Vector2 acceleration) {
        this.id = id;
        this.acceleration.set(acceleration);
    }

    public PlayerMovePack(ByteBuf data) {
        id = data.readInt();
        acceleration.set(data.readFloat(), data.readFloat());
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(id);
        data.writeFloat(acceleration.x);
        data.writeFloat(acceleration.y);
    }
    private static final Family family = Family.all(ChannelComponent.class).get();

    @Override
    public void action(Engine engine , Entity entity) {
        NetClientComponent netClientComponent = Mappers.netPackCM.get(entity);
        netClientComponent.acceleration.set(acceleration);

        engine.getEntitiesFor(family).forEach(e->{
            ChannelComponent channelComponent = Mappers.channelCM.get(e);
            channelComponent.channel.write(PlayerMovePack.this);
        });

    }
}

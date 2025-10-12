package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.client.registry.NetPack;
import io.netty.buffer.ByteBuf;

public class ActorUnLandingPack extends NetPack {
    private int id;

    public ActorUnLandingPack(int id) {
        this.id = id;
    }

    public ActorUnLandingPack(ByteBuf data) {
        super(data);
        this.id = data.readInt();
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(this.id);
    }


    @Override
    public void action(Engine engine, Entity entity) {

    }
}

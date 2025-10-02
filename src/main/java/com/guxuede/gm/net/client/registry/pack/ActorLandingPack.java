package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.utils.PackageUtils;
import io.netty.buffer.ByteBuf;

public class ActorLandingPack extends NetPack {
    private String userName;
    private String character;
    private int id;
    private float x, y;
    private int direction;

    public ActorLandingPack(String userName, String character, int id, float x, float y, int direction) {
        this.userName = userName;
        this.character = character;
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public ActorLandingPack(ByteBuf data) {
        super(data);
        this.id = data.readInt();
        this.userName = PackageUtils.readString(data);
        this.character = PackageUtils.readString(data);
        this.x = data.readFloat();
        this.y = data.readFloat();
        this.direction = data.readInt();
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(this.id);
        PackageUtils.writeString(userName, data);
        PackageUtils.writeString(character, data);
        data.writeFloat(this.x);
        data.writeFloat(this.y);
        data.writeInt(this.direction);
    }


    @Override
    public void action(Engine engine, Entity entity) {

    }
}

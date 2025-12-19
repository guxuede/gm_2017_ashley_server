package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.utils.PackageUtils;
import io.netty.buffer.ByteBuf;

public class ActorLandingPack extends NetPack {
    private String mapName;
    private String userName;
    private int id;
    private float x, y;
    private String character;
    private float directionInDegrees;
    private String client;

    public ActorLandingPack(String mapName, String userName, String character, int id, float x, float y, float directionInDegrees, String client) {
        this.mapName = mapName;
        this.userName = userName;
        this.character = character;
        this.id = id;
        this.x = x;
        this.y = y;
        this.directionInDegrees = directionInDegrees;
        this.client = client;
    }

    public ActorLandingPack(ByteBuf data) {
        super(data);
        this.mapName = PackageUtils.readString(data);
        this.id = data.readInt();
        this.userName = PackageUtils.readString(data);
        this.character = PackageUtils.readString(data);
        this.x = data.readFloat();
        this.y = data.readFloat();
        this.directionInDegrees = data.readFloat();
        this.client = PackageUtils.readString(data);
    }


    @Override
    public void write(ByteBuf data) {
        PackageUtils.writeString(mapName, data);
        data.writeInt(this.id);
        PackageUtils.writeString(userName, data);
        PackageUtils.writeString(character, data);
        data.writeFloat(this.x);
        data.writeFloat(this.y);
        data.writeFloat(this.directionInDegrees);
        PackageUtils.writeString(client, data);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void action(Engine engine, Entity entity) {

    }
}

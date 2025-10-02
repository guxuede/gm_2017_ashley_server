package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.MessageOutboundSystem;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.system.component.PositionComponent;
import com.guxuede.gm.net.utils.PackageUtils;
import entityEdit.Mappers;
import io.netty.buffer.ByteBuf;

public class PlayerLandingPack extends NetPack {
    private String userName;
    private String character;
    private int id;
    private int x, y;
    private int direction;

    public PlayerLandingPack(String userName, String character, int id, int x, int y, int direction) {
        this.userName = userName;
        this.character = character;
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public PlayerLandingPack(ByteBuf data) {
        super(data);
        this.id = data.readInt();
        this.userName = PackageUtils.readString(data);
        this.character = PackageUtils.readString(data);
        this.x = data.readInt();
        this.y = data.readInt();
        this.direction = data.readInt();
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(this.id);
        PackageUtils.writeString(userName, data);
        PackageUtils.writeString(character, data);
        data.writeInt(this.x);
        data.writeInt(this.y);
        data.writeInt(this.direction);
    }


    @Override
    public void action(Engine engine, Entity entity) {

    }
}

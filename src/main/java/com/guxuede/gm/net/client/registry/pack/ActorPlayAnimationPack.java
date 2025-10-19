package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import com.guxuede.gm.net.client.registry.NetPack;

import com.guxuede.gm.net.system.MessageOutboundSystem;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.utils.PackageUtils;
import entityEdit.Mappers;
import io.netty.buffer.ByteBuf;

public class ActorPlayAnimationPack extends NetPack{
    private int id;
    public String animationName;
    public int duration;


    public ActorPlayAnimationPack(int id, String animationName, int duration) {
        this.id = id;
        this.animationName = animationName;
        this.duration = duration;
    }

    public ActorPlayAnimationPack(ByteBuf data) {
        id = data.readInt();
        animationName = PackageUtils.readString(data);
        duration = data.readInt();
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(id);
        PackageUtils.writeString(animationName,data);
        data.writeInt(duration);
    }

    @Override
    public void action(Engine engine, Entity entity) {
        PlayerDataComponent playerDataComponent = Mappers.playerCM.get(entity);
        engine.getSystem(MessageOutboundSystem.class).broadCaseMessageInSameMap(this, playerDataComponent.mapName);
    }

}

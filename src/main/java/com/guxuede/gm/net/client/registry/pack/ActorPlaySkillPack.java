package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.MessageOutboundSystem;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.utils.PackageUtils;
import entityEdit.Mappers;
import io.netty.buffer.ByteBuf;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

public class ActorPlaySkillPack extends NetPack {
    private int id;
    public String skillId;
    public float targetX;
    public float targetY;


    public ActorPlaySkillPack(int id, String skillId, float targetX, float targetY) {
        this.id = id;
        this.skillId = skillId;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public ActorPlaySkillPack(ByteBuf data) {
        id = data.readInt();
        skillId = PackageUtils.readString(data);
        targetX = data.readFloat();
        targetY = data.readFloat();
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(id);
        PackageUtils.writeString(skillId,data);
        data.writeFloat(targetX);
        data.writeFloat(targetY);
    }


    @Override
    public void action(Engine engine, Entity entity) {
        PlayerDataComponent playerDataComponent = Mappers.playerCM.get(entity);
        engine.getSystem(MessageOutboundSystem.class).broadCaseMessageInSameMap(this, playerDataComponent.mapName);
    }

}

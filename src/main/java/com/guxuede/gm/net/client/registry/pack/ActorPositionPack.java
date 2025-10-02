package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.MessageOutboundSystem;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import entityEdit.Mappers;
import io.netty.buffer.ByteBuf;

public class ActorPositionPack extends NetPack {

    private final int id;

    private final int direction;
    private final Vector2 position = new Vector2();

    public ActorPositionPack(int id, int direction, Vector2 position) {
        this.id = id;
        this.direction = direction;
        this.position.set(position);
    }

    //[ 0, 0, 0, 3, -96, -8, 32, 12, 0, 0, 0, 4, 68, 37, 91, 50, 67, -111, 6, 9, 9, 9, 9, 9 ]
    public ActorPositionPack(ByteBuf data) {  // 谁保证一定跟写的一样?
        id = data.readInt();
        direction = data.readInt();
        float x = data.readFloat();
        float y = data.readFloat();
        position.set(x, y);
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(id);
        data.writeInt(direction);
        data.writeFloat(position.x); // 谁保证一定跟写的一样?
        data.writeFloat(position.y);
    }

    @Override
    public void action(Engine engine, Entity entity) {
        PlayerDataComponent playerDataComponent = Mappers.playerCM.get(entity);
        playerDataComponent.position.set(position.x, position.y);
        playerDataComponent.direction = direction;

        engine.getSystem(MessageOutboundSystem.class).broadCaseMessageExcept(this, entity);
    }
}

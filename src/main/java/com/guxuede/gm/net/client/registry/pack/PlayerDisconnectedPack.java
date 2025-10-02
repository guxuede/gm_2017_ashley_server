package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.MessageOutboundSystem;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.userdata.UserDto;
import com.guxuede.gm.net.userdata.UserManager;
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

    @Override
    public void action(Engine engine, Entity entity) {
        PlayerDataComponent playerDataComponent = Mappers.playerCM.get(entity);
        UserDto userDto = UserManager.loadUser(playerDataComponent.userName);
        userDto.setCharacter(playerDataComponent.getCharacter());
        userDto.setX(playerDataComponent.position.x);
        userDto.setY(playerDataComponent.position.y);

        engine.removeEntity(entity);

        engine.getSystem(MessageOutboundSystem.class).broadCaseMessage(this);
    }
}

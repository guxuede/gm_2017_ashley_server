package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.MessageOutboundSystem;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.utils.PackageUtils;
import entityEdit.Mappers;
import io.netty.buffer.ByteBuf;

public class PlayerChatPack extends NetPack {

    private int id;
    private String userName;
    private String chat;

    public PlayerChatPack(int id, String userName, String chat) {
        this.id = id;
        this.userName = userName;
        this.chat = chat;
    }

    public PlayerChatPack(ByteBuf data) {
        super(data);
        this.id = data.readInt();
        this.userName = PackageUtils.readString(data);
        this.chat = PackageUtils.readString(data);
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(id);
        PackageUtils.writeString(userName,data);
        PackageUtils.writeString(chat,data);
    }

    @Override
    public void action(Engine engine, Entity entity) {
        PlayerDataComponent playerDataComponent = Mappers.playerCM.get(entity);
        userName = playerDataComponent.userName;
        engine.getSystem(MessageOutboundSystem.class).broadCaseMessage(this);
    }

}

package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.CommandSystem;
import com.guxuede.gm.net.utils.PackageUtils;
import io.netty.buffer.ByteBuf;

public class PlayerCommandPack extends NetPack {

    private final int id;
    private final String command;

    public PlayerCommandPack(int id, String command) {
        this.id = id;
        this.command = command;
    }

    public PlayerCommandPack(ByteBuf data) {
        super(data);
        this.id = data.readInt();
        this.command = PackageUtils.readString(data);
    }

    @Override
    public void write(ByteBuf data) {
        data.writeInt(id);
        PackageUtils.writeString(command,data);
    }

    @Override
    public void action(Engine engine, Entity entity) {
        engine.getSystem(CommandSystem.class).executeCommand(command);
    }
}

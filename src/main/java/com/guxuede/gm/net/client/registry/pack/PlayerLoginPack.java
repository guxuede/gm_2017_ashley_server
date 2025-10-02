package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.MessageOutboundSystem;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.system.component.PositionComponent;
import com.guxuede.gm.net.userdata.UserDto;
import com.guxuede.gm.net.userdata.UserManager;
import com.guxuede.gm.net.utils.PackageUtils;
import entityEdit.E;
import io.netty.buffer.ByteBuf;


public class PlayerLoginPack extends NetPack {
    private String userName;
    private String password;

    public PlayerLoginPack() {

    }

    public PlayerLoginPack(ByteBuf data) {
        super(data);
        this.userName = PackageUtils.readString(data);
        this.password = PackageUtils.readString(data);
    }

    @Override
    public void write(ByteBuf data) {
        PackageUtils.writeString(this.userName, data);
        PackageUtils.writeString(this.password, data);
    }

    @Override
    public void action(Engine engine, Entity entity) {
        UserDto userDto = UserManager.loadUser(this.userName);
        E.edit(entity).with(PlayerDataComponent.class, e->{
            e.setCharacter(userDto.getCharacter());
            e.setId(userDto.getId());
            e.userName = userName;
            e.direction = 1;
        }).with(PositionComponent.class, e->{
            e.position.set(userDto.getX(), userDto.getY());
        });
        PlayerLandingPack pack = new PlayerLandingPack(userDto.getId(), userDto.getX(),userDto.getY(),userDto.getCharacter());
        engine.getSystem(MessageOutboundSystem.class).broadCaseMessage(pack);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

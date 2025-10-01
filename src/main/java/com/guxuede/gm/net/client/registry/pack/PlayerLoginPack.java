package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.utils.PackageUtils;
import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;


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
        if(StringUtils.equals(this.userName, "greg") && StringUtils.equals(this.password, "123")){

        }
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

package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.MessageOutboundSystem;
import com.guxuede.gm.net.system.component.MessageComponent;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.userdata.UserDto;
import com.guxuede.gm.net.userdata.UserManager;
import com.guxuede.gm.net.utils.PackageUtils;
import entityEdit.E;
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
        UserDto userDto = UserManager.loadUser(this.userName);

        //send all existing player to player
        engine.getEntitiesFor(Family.all(PlayerDataComponent.class).get()).forEach(e->{
            PlayerDataComponent p1 = e.getComponent(PlayerDataComponent.class);
            if(StringUtils.equals(p1.mapName, userDto.getMapName())){
                ActorLandingPack p = new ActorLandingPack(p1.mapName, p1.userName,p1.character, p1.id, p1.position.x, p1.position.y,p1.direction);
                entity.getComponent(MessageComponent.class).outboundPack(p);
            }
        });

        //landing current play to others
        E.edit(entity).with(PlayerDataComponent.class, e->{
            e.setCharacter(userDto.getCharacter());
            e.setId(userDto.getId());
            e.mapName = userDto.getMapName();
            e.userName = userName;
            e.direction = 1;
            e.position.set(userDto.getX(), userDto.getY());
        });

        //send current player to others(include )
        ActorLandingPack pack = new ActorLandingPack(userDto.getMapName(), userDto.getUserName(),userDto.getCharacter(), userDto.getId(), userDto.getX(),userDto.getY(),userDto.getDirection());
        engine.getSystem(MessageOutboundSystem.class).broadCaseMessageInSameMap(pack, userDto.getMapName());
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

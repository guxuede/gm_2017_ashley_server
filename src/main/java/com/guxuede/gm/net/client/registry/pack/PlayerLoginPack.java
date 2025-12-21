package com.guxuede.gm.net.client.registry.pack;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.MessageOutboundSystem;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.MessageComponent;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.userdata.UserDto;
import com.guxuede.gm.net.userdata.UserManager;
import com.guxuede.gm.net.utils.PackageUtils;
import entityEdit.E;
import entityEdit.Mappers;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PlayerLoginPack extends NetPack {
    private String userName;
    private String password;
    private String client;

    public PlayerLoginPack() {

    }

    public PlayerLoginPack(ByteBuf data) {
        super(data);
        this.userName = PackageUtils.readString(data);
        this.password = PackageUtils.readString(data);
        this.client = PackageUtils.readString(data);
    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public void write(ByteBuf data) {
        PackageUtils.writeString(this.userName, data);
        PackageUtils.writeString(this.password, data);
        PackageUtils.writeString(this.client, data);
    }

    @Override
    public void action(Engine engine, Entity channelEntity) {
        ChannelComponent channelComponent = Mappers.channelCM.get(channelEntity);

        if(isAlreadyLogin(engine)){
            log.error("已经登录了,不能再登录");
            return;
        }

        Entity entity = E.create().with(MessageComponent.class, e->{
            e.channelComponent = channelComponent;
        }).buildToWorld();


        UserDto userDto = UserManager.loadUser(this.userName);

        //send all existing actor to player
        engine.getEntitiesFor(Family.all(PlayerDataComponent.class).get()).forEach(e->{
            PlayerDataComponent p1 = e.getComponent(PlayerDataComponent.class);
            MessageComponent p2 = e.getComponent(MessageComponent.class);
            if(p2.channelComponent != channelComponent && StringUtils.equals(p1.mapName, userDto.getMapName())){
                ActorLandingPack p = new ActorLandingPack(p1.mapName, p1.userName,p1.character, p1.id, p1.position.x, p1.position.y,p1.directionInDegrees, p1.client);
                channelComponent.outboundPack(p);
            }
        });

        E.edit(entity).with(PlayerDataComponent.class, e->{
            e.setCharacter(userDto.getCharacter());
            e.setId(userDto.getId());
            e.mapName = userDto.getMapName();
            e.userName = userName;
            e.directionInDegrees = userDto.getDirectionInDegrees();
            e.position.set(userDto.getX(), userDto.getY());
            e.client = client;
        });

        //send current player to others(include current player)
        ActorLandingPack pack = new ActorLandingPack(userDto.getMapName(), userDto.getUserName(),userDto.getCharacter(), userDto.getId(), userDto.getX(),userDto.getY(),userDto.getDirectionInDegrees(), client);
        engine.getSystem(MessageOutboundSystem.class).broadCaseMessageInSameMapExcept(pack, channelComponent, userDto.getMapName());

        //send current player current player
        channelComponent.outboundPack(pack);
    }

    private static final Family playerDataComponentFamily = Family.all(PlayerDataComponent.class).get();

    private boolean isAlreadyLogin(Engine engine){
        for (Entity next : engine.getEntitiesFor(playerDataComponentFamily)) {
            PlayerDataComponent messageComponent = Mappers.playerCM.get(next);
            if (StringUtils.equals(messageComponent.userName,userName)) {
                return true;
            }
        }
        return false;
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

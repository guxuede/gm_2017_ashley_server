package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.guxuede.gm.net.client.registry.pack.ActorLandingPack;
import com.guxuede.gm.net.client.registry.pack.ActorUnLandingPack;
import com.guxuede.gm.net.system.component.MessageComponent;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import entityEdit.Mappers;
import org.apache.commons.lang3.StringUtils;

public class MapGateSystem  extends IteratingSystem {

    private static final Family family = Family.all(PlayerDataComponent.class).get();

    public MapGateSystem(){
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //process inbound entity message
        PlayerDataComponent playerDataComponent = Mappers.playerCM.get(entity);
        if(StringUtils.equals("data/desert1.tmx", playerDataComponent.mapName)){
            if(playerDataComponent.position.dst(787f,451f) <  20){
                actorMapChange(entity, playerDataComponent,  "data/map2.tmx", 110, 83);
            }
        }
        if(StringUtils.equals("data/map2.tmx", playerDataComponent.mapName)){
            if(playerDataComponent.position.dst(110f,50f) <  20){
                actorMapChange(entity, playerDataComponent,  "data/desert1.tmx", 787, 420);
            }
        }
    }


    public void actorMapChange(Entity entity, PlayerDataComponent playerDataComponent, String toMap, float x, float y){
        String originalMap = playerDataComponent.mapName;
        playerDataComponent.mapName =toMap;
        playerDataComponent.position.set(x, y);

        //notify other player in same map: un-landing player
        ActorUnLandingPack actorUnLandingPack = new ActorUnLandingPack(playerDataComponent.id);
        getEngine().getSystem(MessageOutboundSystem.class).broadCaseMessageInSameMapExcept(actorUnLandingPack, entity, originalMap);

        //notify other player(include current player) in new map: landing player
        ActorLandingPack pack = new ActorLandingPack(playerDataComponent.mapName, playerDataComponent.userName,playerDataComponent.getCharacter(), playerDataComponent.id, playerDataComponent.position.x,playerDataComponent.position.y,playerDataComponent.direction);
        getEngine().getSystem(MessageOutboundSystem.class).broadCaseMessageInSameMap(pack, playerDataComponent.mapName);

        //send existing player in new map to current player
        getEngine().getEntitiesFor(Family.all(PlayerDataComponent.class).get()).forEach(e->{
            PlayerDataComponent p1 = e.getComponent(PlayerDataComponent.class);
            if(p1.id!=playerDataComponent.id && StringUtils.equals(p1.mapName, playerDataComponent.mapName)){
                ActorLandingPack p = new ActorLandingPack(p1.mapName, p1.userName,p1.character, p1.id, p1.position.x, p1.position.y,p1.direction);
                entity.getComponent(MessageComponent.class).outboundPack(p);
            }
        });
    }
}

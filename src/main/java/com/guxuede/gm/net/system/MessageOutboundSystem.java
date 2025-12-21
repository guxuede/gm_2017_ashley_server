package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.MessageComponent;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.stream.StreamSupport;


/**
 * Created by guxuede on 2017/6/3 .
 */
public class MessageOutboundSystem extends EntitySystem {

    private static final Family ChannelComponentFamily = Family.all(ChannelComponent.class).get();
    private static final Family PlayerDataComponentFamily = Family.all(PlayerDataComponent.class).get();


    public void broadCaseMessage(NetPack netPack){
        getEngine().getEntitiesFor(ChannelComponentFamily).forEach(e->{
            e.getComponent(ChannelComponent.class).outboundPack(netPack);
        });
    }

    public void broadCaseMessageInSameMapExcept(NetPack netPack, ChannelComponent channelComponent1, String mapName){
        StreamSupport.stream(getEngine().getEntitiesFor(PlayerDataComponentFamily).spliterator(),false).filter(e->{
            PlayerDataComponent p1 = e.getComponent(PlayerDataComponent.class);
            return  StringUtils.equals(p1.mapName, mapName);
        }).map(e->e.getComponent(MessageComponent.class)).
                map(e->e.channelComponent).
                filter(Objects::nonNull)
                .filter(e-> !e.equals(channelComponent1))
                .distinct()
                .forEach(e->{
                    e.outboundPack(netPack);
                });
    }

    public void broadCaseMessageInSameMap(NetPack netPack, String mapName){
        broadCaseMessageInSameMapExcept(netPack, null, mapName);
    }

}

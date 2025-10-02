package com.guxuede.gm.net.system.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import io.netty.channel.Channel;

public class ChannelComponent implements Component, Pool.Poolable{

    public Channel channel;

    @Override
    public void reset() {
        if(channel!=null){
            try{
                if(!channel.isOpen()){
                    channel.close();
                }
            }catch (RuntimeException e){
                e.printStackTrace();
            }
            channel= null;
        }
    }
}

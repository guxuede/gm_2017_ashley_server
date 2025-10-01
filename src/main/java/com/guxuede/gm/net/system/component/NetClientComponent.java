package com.guxuede.gm.net.system.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.guxuede.gm.net.client.registry.NetPack;
import com.guxuede.gm.net.utils.PackQueue;

public class NetClientComponent implements Component, Pool.Poolable{

    private int id;
    private String character;
    public Vector2 acceleration = new Vector2();
    public Vector2 position = new Vector2();

    public PackQueue<NetPack> inboundNetPacks = new PackQueue<>();
    public PackQueue<NetPack> outboundNetPacks = new PackQueue<>();

    public synchronized void inBoundPack(NetPack netPack){
        inboundNetPacks.add(netPack);
    }

    public synchronized void outboundPack(NetPack netPack){
        outboundNetPacks.add(netPack);
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void reset() {
        inboundNetPacks.clear();
        outboundNetPacks.clear();
    }
}

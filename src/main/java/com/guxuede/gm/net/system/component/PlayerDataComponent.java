package com.guxuede.gm.net.system.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import static com.guxuede.gm.net.system.MovementSystem.DOWN;

public class PlayerDataComponent implements Component, Pool.Poolable{

    public String userName;

    public int id;
    public String character;
    public Vector2 acceleration = new Vector2();
    public final Vector2 velocity = new Vector2();//速度
    public int direction= DOWN;
    public boolean isMoving;


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

    }
}

package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.systems.IntervalSystem;
import com.guxuede.gm.net.userdata.UserManager;

public class UserManagerSystem extends IntervalSystem {


    public UserManagerSystem(float interval) {
        super(interval);
    }

    @Override
    protected void updateInterval() {
        System.out.println("save user");
        UserManager.save();
    }
}

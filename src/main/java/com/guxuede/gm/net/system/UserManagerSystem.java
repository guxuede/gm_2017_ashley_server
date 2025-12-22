package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import com.guxuede.gm.net.userdata.UserManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserManagerSystem extends IntervalSystem {
    private static final Family family = Family.all(PlayerDataComponent.class).get();


    public UserManagerSystem(float interval) {
        super(interval);
    }

    @Override
    protected void updateInterval() {
        log.info("save user");
        getEngine().getEntitiesFor(family).forEach(e->{
            PlayerDataComponent component = e.getComponent(PlayerDataComponent.class);
            UserManager.updateUser(component);
        });
        UserManager.save();
    }
}

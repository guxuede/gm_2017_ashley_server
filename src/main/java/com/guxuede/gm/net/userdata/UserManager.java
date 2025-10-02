package com.guxuede.gm.net.userdata;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class UserManager {

    private static final ConcurrentHashMap<String, UserDto> user = new ConcurrentHashMap<>();

    private static final String[] RANDOM_CHARACTER = new String[]{"RPGMarkPig", "Aquatic","Undead"};

    private static final Random random = new Random();

    public static UserDto loadUser(String userName){
       return user.computeIfAbsent(userName, new Function<String, UserDto>() {
            @Override
            public UserDto apply(String s) {
                UserDto userDto = new UserDto();
                userDto.setId((int) System.currentTimeMillis());
                userDto.setUserName(userName);
                userDto.setX(100);
                userDto.setY(100);
                userDto.setCharacter(RANDOM_CHARACTER[random.nextInt(0, 3)]);
                return userDto;
            }
        });
    }


}

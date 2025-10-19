package com.guxuede.gm.net.userdata;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.guxuede.gm.net.system.component.PlayerDataComponent;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Random;

public class UserManager {
    private static File userFile = new File("C:\\Users\\latiao\\user.json");

    private static final ObjectMap<String, UserDto> user = new ObjectMap<String, UserDto>();
    static{
        try{
            Json json = new Json();
            BufferedReader reader = IOUtils.buffer(new BufferedReader(new InputStreamReader(new FileInputStream(userFile))));
            user.putAll(json.fromJson(user.getClass(), reader));
        }catch (RuntimeException e){
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final String[] RANDOM_CHARACTER = new String[]{"RPGMarkPig", "Aquatic","Undead"};

    private static final Random random = new Random();

    public static UserDto loadUser(String userName){
        if(user.containsKey(userName)){
            return user.get(userName);
        }else{
            UserDto userDto = new UserDto();
            userDto.setId((int) System.currentTimeMillis());
            userDto.setUserName(userName);
            userDto.setX(100);
            userDto.setY(100);
            userDto.setMapName("data/desert1.tmx");
            userDto.setCharacter(RANDOM_CHARACTER[random.nextInt(0, 3)]);
            user.put(userName, userDto);
            return userDto;
        }
    }


    public static void save(){
        Json json = new Json();
        try {
            BufferedWriter buffer = IOUtils.buffer(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userFile))));
            json.toJson(user, buffer);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUser(PlayerDataComponent component) {
        UserDto userDto = user.get(component.userName);
        userDto.setId(component.getId());
        userDto.setCharacter(component.getCharacter());
        userDto.setMapName(component.mapName);
        userDto.setX(component.position.x);
        userDto.setY(component.position.y);
        userDto.setDirection(component.direction);
    }
}

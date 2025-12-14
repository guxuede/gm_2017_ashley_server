package com.guxuede.gm.net.userdata;

public class UserDto {
    private String mapName;
    private String userName;
    private int id;
    private String character;
    private float x,y;
    private float directionInDegrees;

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDirectionInDegrees() {
        return directionInDegrees;
    }

    public void setDirectionInDegrees(float directionInDegrees) {
        this.directionInDegrees = directionInDegrees;
    }
}

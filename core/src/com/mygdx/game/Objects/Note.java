package com.mygdx.game.Objects;

import com.badlogic.gdx.math.Rectangle;

public class Note {
    private int id;
    private float x;
    private float y;
    private int position;
    private String type;
    private boolean clicked;
    public Rectangle rectangle;
    public Note(int id, float x, float y, float width, float height, int position, String type){
        this.id = id;
        this.position = position;
        this.x = x;
        this.y = y;
        this.type = type;
        this.clicked = false;
        this.rectangle = new Rectangle(x, y, width, height);
    }
    public int getPosition(){
        return position;
    }
    public int getId(){
        return id;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public String getType(){
        return type;
    }
    public boolean isClicked(){
        return clicked;
    }
    public void setY(float y){
        this.y = y;
        rectangle.setY(this.y);
    }
    public void setClicked(boolean clicked){
        this.clicked = clicked;
    }
    public boolean contains(float x, float y){
        return rectangle.contains(x, y);
    }
}

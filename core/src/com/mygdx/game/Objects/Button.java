package com.mygdx.game.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class Button {
    private float x;
    private float y;
    private float width;
    private float height;
    private String text;
    private float songBPM;
    private float songLength;
    public Button(float x, float y, float width, float height, String text){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }
    public void draw(ShapeRenderer sr, Color color){
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(color);
        sr.rect(x, y, width, height);
        sr.circle(x, y+height/2, height/2);
        sr.circle(x+width, y+height/2, height/2);
        sr.end();
    }
    public boolean contains(float touchedX, float touchedY){
        Rectangle rec = new Rectangle(x, y, width, height);
        if(rec.contains(touchedX, touchedY))
            return true;
        Circle c1 = new Circle(x, y+height/2, height/2);
        Circle c2 = new Circle(x+width, y+height/2, height/2);
        return c1.contains(touchedX, touchedY) || c2.contains(touchedX, touchedY);
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }
    public String getText() {
        return text;
    }
    public void setText(String text){
        this.text = text;
    }
    public void setSongData(float songBPM, float songLength) {
        this.songBPM = songBPM;
        this.songLength = songLength;
    }
    public float getSongLength() {
        return songLength;
    }
    public float getSongBPM() {
        return songBPM;
    }
}

package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.Objects.Note;

public class PlayScreen extends InputAdapter implements Screen {
    private MyGame game;
    public static float NOTE_WIDTH;
    public static float NOTE_HEIGHT = 500;
    public static float START_DISTANCE = 0;
    private ShapeRenderer sr;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Array<Note> notes;
    private float scrollSpeed;
    private Vector3 pointerIn3D; //Location of our finger when we touch screen
    private Vector2 pointer;
    private boolean gameStarted;
    private SpriteBatch batch;
    private BitmapFont font;

    public PlayScreen(MyGame game, SpriteBatch batch){
        this.game = game;
        this.batch = batch;
    }

    @Override
    public void show() {
        sr = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1080, 2160);
        viewport = new StretchViewport(1080, 2160, camera);
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        font.setColor(0, 0, 0, 1);
        //how many notes are at once in array
        int noteCount = 10;
        notes = new Array<>();
        scrollSpeed = 1000;
        NOTE_WIDTH = viewport.getWorldWidth()/4;
        Gdx.input.setInputProcessor(this);
        pointerIn3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(pointerIn3D);
        pointer = new Vector2(pointerIn3D.x, pointerIn3D.y);
        gameStarted = false;

        for(int i = 0; i< noteCount; i++){
            int notePosition;
            if(i>0){
                do{
                    notePosition = (int)(Math.random()*4);
                }
                while(notePosition == notes.get(i-1).getPosition());
                notes.add(new Note(i, viewport.getWorldWidth()/4*notePosition, NOTE_HEIGHT*i+START_DISTANCE, NOTE_WIDTH, NOTE_HEIGHT, notePosition, "normal"));
            }
            else{
                notePosition = (int)(Math.random()*4);
                notes.add(new Note(i, viewport.getWorldWidth()/4*notePosition, NOTE_HEIGHT*i+START_DISTANCE, NOTE_WIDTH, NOTE_HEIGHT, notePosition, "start"));
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sr.setProjectionMatrix(camera.combined);

        drawBackground(sr);

        sr.setColor(Color.BLACK);
        for (Note n : notes) {
            if(n.getType().equals("normal")) {
                sr.begin(ShapeRenderer.ShapeType.Filled);
                if(n.isClicked())
                    sr.setColor(Color.GRAY);
                else
                    sr.setColor(Color.BLACK);
                sr.rect(n.getX(), n.getY(), viewport.getWorldWidth()/4, NOTE_HEIGHT);
                sr.end();
            }
            else if(n.getType().equals("start")){
                sr.begin(ShapeRenderer.ShapeType.Filled);
                if(n.isClicked())
                    sr.setColor(new Color(0xef9b0fFF));
                else
                    sr.setColor(new Color(0xffdf00FF));
                sr.rect(n.getX(), n.getY(), viewport.getWorldWidth()/4, NOTE_HEIGHT);
                sr.end();
            }
        }
        if(!gameStarted) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            font.getData().setScale(0.7f);
            font.draw(batch, "START", notes.get(0).getX() + 15, notes.get(0).getY() + NOTE_HEIGHT / 2f + 32);
            batch.end();
        }
        else
            update(delta);
    }

    public void update(float dt){
        for(int i=0; i<notes.size; i++) {
            Note n = notes.get(i);
            scrollSpeed += dt;
            if(n.getY()<-NOTE_HEIGHT){
                if(n.isClicked()) {
                    notes.removeIndex(i);
                    int notePosition;
                    do{
                        notePosition = (int)(Math.random()*4);
                    }
                    while(notePosition == notes.get(notes.size-1).getPosition());
                    notes.add(new Note(notes.get(notes.size-1).getId()+1, viewport.getWorldWidth()/4*notePosition, notes.get(notes.size-1).getY()+NOTE_HEIGHT, NOTE_WIDTH, NOTE_HEIGHT, notePosition, "normal"));
                }
                else{
                    dispose();
                    game.dispose();
                }
            }
             n.setY(n.getY()-scrollSpeed*dt);
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        sr.dispose();
    }

    private void drawBackground(ShapeRenderer sr){
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);
        sr.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        sr.setColor(Color.LIGHT_GRAY);
        sr.rect(viewport.getWorldWidth()/4-4, 0, 4, viewport.getWorldHeight());
        sr.rect(viewport.getWorldWidth()/2-2, 0, 4, viewport.getWorldHeight());
        sr.rect(viewport.getWorldWidth()/2+viewport.getWorldWidth()/4, 0, 4, viewport.getWorldHeight());
        sr.end();
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        pointerIn3D.x = screenX;
        pointerIn3D.y = screenY;
        pointerIn3D.z = 0;
        viewport.unproject(pointerIn3D); //Translate the screen coordinates to world coordinates
        this.pointer.x = pointerIn3D.x;
        this.pointer.y = pointerIn3D.y;
        boolean isAnyClicked = false;
        if(gameStarted) {
            for (Note n : notes) {
                if (n.contains(this.pointer.x, this.pointer.y)) {
                    n.setClicked(true);
                    isAnyClicked = true;
                }
            }
            if (!isAnyClicked) {
                dispose();
                game.dispose();
            }
        }
        else if (notes.get(0).contains(this.pointer.x, this.pointer.y)) {
            gameStarted = true;
            notes.get(0).setClicked(true);
        }
        return false;
    }
}

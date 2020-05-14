package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.Objects.Note;

public class SongPlayScreen extends InputAdapter implements Screen {
    private MyGame game;
    public static float NOTE_WIDTH;
    public static float NOTE_HEIGHT = 600;
    public static float START_DISTANCE = 500;
    private ShapeRenderer sr;
    private OrthographicCamera camera;
    private StretchViewport viewport;
    private Array<Note> notes;
    private float scrollSpeed;
    private Vector3 pointerIn3D; //Location of our finger when we touch screen
    private boolean gameStarted;
    private SpriteBatch batch;
    private BitmapFont font;
    private int score;
    private Music music;
    private GlyphLayout layout;
    private String songName;
    private float bpm;

    public SongPlayScreen(MyGame game, SpriteBatch batch, ShapeRenderer sr, BitmapFont font, String songName, float bpm){
        this.game = game;
        this.batch = batch;
        this.sr = sr;
        this.font = font;
        this.songName = songName;
        this.bpm = bpm;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MyGame.WORLD_WIDTH, MyGame.WORLD_HEIGHT);
        viewport = new StretchViewport(MyGame.WORLD_WIDTH, MyGame.WORLD_HEIGHT, camera);
        score = 0;
        //how many notes are at once in array
        int noteCount = 10;
        notes = new Array<>();
        //scrollSpeed = 1000;
        NOTE_WIDTH = viewport.getWorldWidth()/4;
        Gdx.input.setInputProcessor(this);
        pointerIn3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(pointerIn3D);
        gameStarted = false;
        layout = new GlyphLayout();
        music = Gdx.audio.newMusic(Gdx.files.internal("music/"+songName+".mp3"));
        music.setLooping(true);
        if(songName.equals("Sunny")){
            music.setVolume(0.3f);
        }
        else music.setVolume(0.15f);
        scrollSpeed = NOTE_HEIGHT * (bpm/60); //duration of a beat - bpm/minute

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
                    sr.setColor(Color.GRAY);
                else
                    sr.setColor(Color.ROYAL);
                sr.rect(n.getX(), n.getY(), viewport.getWorldWidth()/4, NOTE_HEIGHT);
                sr.end();
            }
        }
        batch.setProjectionMatrix(camera.combined);
        drawCenteredText(score+"", viewport.getWorldHeight()-300, 158, Color.ROYAL);
        if(!gameStarted)
            drawText("START", notes.get(0).getX() + 15, notes.get(0).getY() + NOTE_HEIGHT / 2f + 32, 90, Color.BLACK);
        else
            update(delta);
    }

    public void update(float dt){
        for(int i=0; i<notes.size; i++) {
            Note n = notes.get(i);
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
                    game.setScreen(new GameOverScreen(game, batch, sr, font, songName, bpm));
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

    private void drawText(String text, float x, float y, float size, Color color){
        batch.begin();
        font.getData().setScale(size/128);
        font.setColor(color);
        font.draw(batch, text, x, y);
        batch.end();
    }
    public void drawCenteredText(String s, float y, float size, Color color){
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        font.setColor(color);
        font.getData().setScale(size/128);
        layout.setText(font, s);
        font.draw(batch, s, (viewport.getWorldWidth()-layout.width)/2, y);
        batch.end();
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        pointerIn3D.x = screenX;
        pointerIn3D.y = screenY;
        pointerIn3D.z = 0;
        viewport.unproject(pointerIn3D); //Translate the screen coordinates to world coordinates
        boolean isAnyClicked = false;
        if(gameStarted) {
            for(int i=0; i<notes.size; i++) {
                Note n = notes.get(i);
                if (n.contains(pointerIn3D.x, pointerIn3D.y)) {
                    if(!n.isClicked()){
                        if(i == 0)
                            n.setClicked(true);
                        else if(notes.get(i-1).isClicked())
                            n.setClicked(true);
                        score++;
                    }
                    isAnyClicked = true;
                }
            }
            if (!isAnyClicked) {
                dispose();
                game.setScreen(new GameOverScreen(game, batch, sr, font, songName, bpm));
            }
        }
        else if (notes.get(0).contains(pointerIn3D.x, pointerIn3D.y)) {
            music.play();
            gameStarted = true;
            score++;
            notes.get(0).setClicked(true);
        }
        return false;
    }

    @Override
    public void dispose() {
        music.stop();
        music.dispose();
    }
}

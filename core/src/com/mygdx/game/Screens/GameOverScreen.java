package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.Objects.Button;

public class GameOverScreen extends InputAdapter implements Screen {
    private MyGame game;
    private ShapeRenderer sr;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private Vector3 pointerIn3D;
    private GlyphLayout layout;
    private Button restartButton;
    private Button homeButton;
    private String songName;
    private float bpm;
    private String gameType;

    public GameOverScreen(MyGame game, SpriteBatch batch, ShapeRenderer sr, BitmapFont font, String songName, float bpm){
        this.game = game;
        this.batch = batch;
        this.sr = sr;
        this.font = font;
        this.songName = songName;
        this.bpm = bpm;
        gameType = "Song";
    }
    public GameOverScreen(MyGame game, SpriteBatch batch, ShapeRenderer sr, BitmapFont font){
        this.game = game;
        this.batch = batch;
        this.sr = sr;
        this.font = font;
        gameType = "Infinity";
    }
    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MyGame.WORLD_WIDTH, MyGame.WORLD_HEIGHT);
        viewport = new FitViewport(MyGame.WORLD_WIDTH, MyGame.WORLD_HEIGHT, camera);
        Gdx.input.setInputProcessor(this);
        pointerIn3D = new Vector3(0, 0, 0);
        viewport.unproject(pointerIn3D);
        layout = new GlyphLayout();
        restartButton = new Button(viewport.getWorldWidth()/2-350, 650, 700, 180, "Restart");
        homeButton = new Button(viewport.getWorldWidth()/2-350, 350, 700, 180, "Back to menu");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sr.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);
        restartButton.draw(sr, Color.BLACK);
        homeButton.draw(sr, Color.BLACK);
        drawCenteredText("GAME", viewport.getWorldHeight()-400, 264, Color.BLACK);
        drawCenteredText("OVER", viewport.getWorldHeight()-650, 264, Color.BLACK);
        drawCenteredText(restartButton.getText(), restartButton.getY()+140, 128, Color.WHITE);
        drawCenteredText(homeButton.getText(), homeButton.getY()+140, 128, Color.WHITE);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        pointerIn3D.x = screenX;
        pointerIn3D.y = screenY;
        pointerIn3D.z = 0;
        viewport.unproject(pointerIn3D); //Translate the screen coordinates to world coordinates
        if(restartButton.contains(pointerIn3D.x, pointerIn3D.y)){
            MyGame.tapSound.play();
            if(gameType.equals("Song"))
                game.setScreen(new SongPlayScreen(game, batch, sr, font, songName, bpm));
            else
                game.setScreen(new InfinityPlayScreen(game, batch, sr, font));
        }
        if(homeButton.contains(pointerIn3D.x, pointerIn3D.y)){
            MyGame.tapSound.play();
            game.setScreen(new MenuScreen(game, batch, sr, font));
        }
        return false;
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

    }
}

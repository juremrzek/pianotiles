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

public class MenuScreen extends InputAdapter implements Screen {
    private MyGame game;
    private SpriteBatch batch;
    private ShapeRenderer sr;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private BitmapFont font;
    private GlyphLayout layout;
    private Vector3 pointerIn3D;
    private Button button1;
    private Button button2;

    public MenuScreen(MyGame game, SpriteBatch batch, ShapeRenderer sr, BitmapFont font){
        this.game = game;
        this.batch = batch;
        this.sr = sr;
        this.font = font;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, MyGame.WORLD_WIDTH, MyGame.WORLD_HEIGHT);
        viewport = new FitViewport(MyGame.WORLD_WIDTH, MyGame.WORLD_HEIGHT, camera);
        layout = new GlyphLayout();
        Gdx.input.setInputProcessor(this);
        pointerIn3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(pointerIn3D);
        button1 = new Button(viewport.getWorldWidth()/2-350, 1000, 700, 300, "Choose");
        button2 = new Button(viewport.getWorldWidth()/2-350, 430, 700, 300, "Increasing");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sr.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        button1.draw(sr, Color.BLACK);
        button2.draw(sr, Color.BLACK);
        font.getData().setScale(1.5f);
        drawCenteredText("PIANO", viewport.getWorldHeight()/2+800, 210, Color.BLACK);
        drawCenteredText("TILES", viewport.getWorldHeight()/2+600, 220, Color.BLACK);
        drawCenteredText(button1.getText(), 1270, 128, Color.WHITE);
        drawCenteredText("difficulty", 1140, 128, Color.WHITE);
        drawCenteredText(button2.getText(), 700, 128, Color.WHITE);
        drawCenteredText("difficulty", 570, 128, Color.WHITE);
        drawCenteredText("Made by Jure Mrzek", 80, 64, Color.GRAY);
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
    private void drawCenteredText(String s, float y, float size, Color color){
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
        if(button1.contains(pointerIn3D.x, pointerIn3D.y)){
            MyGame.tapSound.play();
            dispose();
            game.setScreen(new SongSelectScreen(game, batch, sr, font));
        }
        if(button2.contains(pointerIn3D.x, pointerIn3D.y)){
            MyGame.tapSound.play();
            dispose();
            game.setScreen(new InfinityPlayScreen(game, batch, sr, font));
        }
        return false;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

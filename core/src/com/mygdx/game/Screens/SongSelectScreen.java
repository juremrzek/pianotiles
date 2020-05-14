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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.MyGame;
import com.mygdx.game.Objects.Button;

public class SongSelectScreen extends InputAdapter implements Screen {
    private MyGame game;
    private SpriteBatch batch;
    private ShapeRenderer sr;
    private BitmapFont font;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Vector3 pointerIn3D;
    private GlyphLayout layout;
    private Button[] buttons;
    private Button backButton;

    public SongSelectScreen(MyGame game, SpriteBatch batch, ShapeRenderer sr, BitmapFont font){
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
        Gdx.input.setInputProcessor(this);
        pointerIn3D = new Vector3(0, 0, 0);
        viewport.unproject(pointerIn3D);
        layout = new GlyphLayout();
        backButton = new Button(50, 100, 300, 100, "Back");
        buttons = new Button[4];
        for(int i=0; i<buttons.length; i++){
            buttons[i] = new Button(viewport.getWorldWidth()/2-300, viewport.getWorldHeight()-300*i-800, 600, 200, "");
        }
        buttons[0].setText("Easy");
        buttons[1].setText("Medium");
        buttons[2].setText("Hard");
        buttons[3].setText("Speed of light");
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sr.setProjectionMatrix(camera.combined);
        drawCenteredText("Difficulty", viewport.getWorldHeight()-120, 192, Color.BLACK);
        drawCenteredText("select", viewport.getWorldHeight()-250, 192, Color.BLACK);
        for(Button b:buttons){
            b.draw(sr, Color.BLACK);
            drawCenteredText(b.getText(), b.getY()+b.getHeight()-50, 128, Color.WHITE);
        }

        backButton.draw(sr, Color.BLACK);
        drawText(backButton.getText(), backButton.getX()+75,backButton.getY()+backButton.getHeight()-25, 64, Color.WHITE);
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
    private void drawText(String text, float x, float y, float size, Color color){
        batch.begin();
        font.getData().setScale(size/128);
        font.setColor(color);
        font.draw(batch, text, x, y);
        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        pointerIn3D.x = screenX;
        pointerIn3D.y = screenY;
        pointerIn3D.z = 0;
        viewport.unproject(pointerIn3D); //Translate the screen coordinates to world coordinates
        int touchIndex = -1;
        for(int i=0; i<buttons.length; i++){
            if(buttons[i].contains(pointerIn3D.x, pointerIn3D.y)){
                MyGame.tapSound.play();
                touchIndex = i;
            }
        }
        if(backButton.contains(pointerIn3D.x, pointerIn3D.y)){
            MyGame.tapSound.play();
            dispose();
            game.setScreen(new MenuScreen(game, batch, sr, font));
        }
        switch(touchIndex){
            case 0: game.setScreen(new SongPlayScreen(game, batch, sr, font, "Sunny", 119));
            break;
            case 1: game.setScreen(new SongPlayScreen(game, batch, sr, font, "InTime", 222));
            break;
            case 2: game.setScreen(new SongPlayScreen(game, batch, sr, font, "DieByTheBlade", 282));
            break;
            case 3: game.setScreen(new SongPlayScreen(game, batch, sr, font, "AtTheSpeedOfLight", 350));
        }
        return false;
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

    }
}

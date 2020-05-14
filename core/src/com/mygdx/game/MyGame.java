package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Screens.*;

public class MyGame extends Game {
	public static final float WORLD_WIDTH = 1080;
	public static final float WORLD_HEIGHT = 2160;
	public static Sound tapSound;
	private SpriteBatch batch;
	private ShapeRenderer sr;
	private BitmapFont font;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		font.setColor(0, 0, 0, 1);
		ShapeRenderer sr = new ShapeRenderer();
		tapSound = Gdx.audio.newSound(Gdx.files.internal("soundclips/furElise-12.wav"));
		setScreen(new MenuScreen(this, batch, sr, font));
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		sr.dispose();
		font.dispose();
		tapSound.dispose();
		System.exit(0);
	}
}

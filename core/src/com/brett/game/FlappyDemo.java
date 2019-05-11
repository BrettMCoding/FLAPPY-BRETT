package com.brett.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brett.game.states.GameStateManager;
import com.brett.game.states.MenuState;
import com.brett.game.states.PlayState;

public class FlappyDemo extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	public static final String TITLE = "Flappy Brett";
	private GameStateManager gsm;
	private SpriteBatch batch;

	private Preferences prefs;
	private Music music;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		prefs = Gdx.app.getPreferences("fbrprefs");
		prefs.putBoolean("music", true);
		prefs.flush();

		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		if (prefs.getBoolean("music")) {
			music.setVolume(0.1f);
		} else {
			music.setVolume(0.0f);
		}

		if (gsm.getCurrentState() instanceof PlayState && gsm.getGamePaused()) {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			gsm.render(batch);
		} else {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			gsm.update(Gdx.graphics.getDeltaTime());
			gsm.render(batch);
		}
	}

	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		music.dispose();
	}
}

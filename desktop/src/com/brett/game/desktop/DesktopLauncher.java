package com.brett.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.brett.game.FlappyBrett;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = FlappyBrett.WIDTH;
		config.height = FlappyBrett.HEIGHT;
		config.title = FlappyBrett.TITLE;
		new LwjglApplication(new FlappyBrett(), config);



	}
}

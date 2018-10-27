package com.mygdx.game.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Settings;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = (int) Settings.WINDOW_HEIGHT;
		config.width = (int) Settings.WINDOW_WIDTH;
		config.resizable = true;
		MyGdxGame game = new MyGdxGame();
		new LwjglApplication(game, config);
	}
}

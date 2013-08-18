package com.newrog.colorcopter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class ColorCopterGame extends Game{

	public MainGame gamescreen;
	public MenuScreen menuscreen;
	
	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		Art.loadArt();
		MyAudio.loadSounds();
		gamescreen = new MainGame(this);
		menuscreen = new MenuScreen(this);
		
		setScreen(gamescreen);
		
		
	}

}

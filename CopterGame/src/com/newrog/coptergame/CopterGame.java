package com.newrog.coptergame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class CopterGame extends Game {
	
	public MainGame gamescreen;
	public MenuScreen menuscreen;
	
	@Override
	public void create() {
		Texture.setEnforcePotImages(false);
		//Art art = new Art();
		Art.loadArt();
		
		//Art.loadArt();
		MyAudio.loadSounds();
		gamescreen = new MainGame(this);
		menuscreen = new MenuScreen(this);
		
		setScreen(menuscreen);
		
		
	}
}

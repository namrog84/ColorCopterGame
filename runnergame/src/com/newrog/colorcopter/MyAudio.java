package com.newrog.colorcopter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Sound;

public class MyAudio {
	private MyAudio(){}
	
	
	public static Sound helicopterProp;
	public static Sound coinSound;
	
	public static void loadSounds(){
		
		helicopterProp = Gdx.audio.newSound(Gdx.files.internal("data/helicopter.wav"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("data/Pickup_Coin.wav"));
		
		
		
		
		
		
	}
	
	
	
	
}

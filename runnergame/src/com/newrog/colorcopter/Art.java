package com.newrog.colorcopter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Art {
	
	private Art(){}
	
	public static Texture texture;
	public static Texture texture2;
	public static Texture startTexture;
	public static Texture coinT;
	public static Texture coinPlain;
	public static Animation coinAnimation;
	
	
	public static Texture pBackground;
	public static Texture pBackground2;
	public static Texture heliTexture;
	public static Animation heliAnimation;
	public static Texture heliT;
	
	
	public static void loadArt(){
		heliTexture = new Texture(Gdx.files.internal("data/helicopter3.png"));
		heliTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		
		texture = new Texture(Gdx.files.internal("data/block4_2.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		pBackground = new Texture(Gdx.files.internal("data/blockbackground.png"));
		pBackground.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		pBackground2 = new Texture(Gdx.files.internal("data/blockbackground25.png"));
		pBackground2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		
		
		startTexture = new Texture(Gdx.files.internal("data/block1.png"));
		startTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		texture2 = new Texture(Gdx.files.internal("data/hitblock.png"));
		texture2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
		
		
		coinPlain = new Texture(Gdx.files.internal("data/coin1.png"));
		coinPlain.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		coinT = new Texture(Gdx.files.internal("data/coinTR.png"));
		coinT.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		//TextureRegion tr = new TextureRegion(coinT);
		TextureRegion[][] tmp = TextureRegion.split(coinT, 16, 32);
		TextureRegion[] keyFrames = new TextureRegion[4];
		keyFrames[0] = tmp[0][0];
		keyFrames[1] = tmp[0][1];
		keyFrames[2] = tmp[0][2];
		keyFrames[3] = tmp[0][3];
		coinAnimation = new Animation(.1f, keyFrames);
		
		
		
		
		

		heliT = new Texture(Gdx.files.internal("data/helicopteranim.png"));
		heliT.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		//TextureRegion htr = new TextureRegion(heliT);
		TextureRegion[][] htmp = TextureRegion.split(heliT, 192, 64);
		TextureRegion[] heliFrames = new TextureRegion[4];
		
		heliFrames[0] = htmp[0][0];
		heliFrames[1] = htmp[0][1];
		heliFrames[2] = htmp[0][2];
		heliFrames[3] = htmp[0][1];
		heliAnimation = new Animation(.05f, heliFrames);
		
	}
	
	
	
	

}

package com.newrog.colorcopter;

import javax.security.auth.callback.Callback;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Cubic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.newrog.colorcopter.entities.Entity;
import com.newrog.colorcopter.entities.EntityAccessor;
import com.newrog.colorcopter.entities.Junk;
import com.newrog.colorcopter.resources.Art;

public class MenuScreen implements Screen {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	//private Texture texture;
	private Sprite sprite;

	ColorCopterGame game;

	//Vector2 position;
	TweenManager tm;
	Junk junk;
	Junk junkgaming;
	private TweenCallback callback;
	private TweenCallback callbackFinish;
	public MenuScreen(ColorCopterGame g) {
		game = g;
		junk = new Junk();
		junkgaming = new Junk();
	
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		camera.update();
		batch = new SpriteBatch();

		
		//texture = new Texture(Gdx.files.internal("data/menu.png"));
		//texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		//TextureRegion region = new TextureRegion(texture, 0, 0, 1280, 720);

		//sprite = new Sprite(region);
		//sprite.setSize(1.0f, 1f * sprite.getHeight() / sprite.getWidth());
		//sprite.setOrigin(sprite.getWidth(), sprite.getHeight() / 2);
		//sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
		
		Tween.registerAccessor(Entity.class, new EntityAccessor());
		tm = new TweenManager();
		
		//Timeline timeline = Timeline.createSequence().
		//		beginSequence().push(Tween.to(junk,EntityAccessor.POSITION_XY, 10)).end().start();
		junk.position.x = Gdx.graphics.getWidth()/2-junk.sprite.getWidth()/2;
		junk.position.y = Gdx.graphics.getHeight()+10;
		
		junkgaming.sprite = new Sprite(Art.gamingTexture);
		junkgaming.sprite.setScale(5);
		junkgaming.position.x = Gdx.graphics.getWidth()/2-junkgaming.sprite.getWidth()/2;
		junkgaming.position.y = -10;
		
		
		callback = new TweenCallback() {

			@Override
			public void onEvent(int type, BaseTween<?> source) {
				transitionDownScreen();
				
			}
		};
		
		callbackFinish = new TweenCallback() {

			@Override
			public void onEvent(int type, BaseTween<?> source) {
				//System.out.println("BEEP");
				game.setScreen(game.gamescreen);
				
			}
		};
		
		
		
		Tween.to(junk, EntityAccessor.POSITION_XY, 1.0f).target(Gdx.graphics.getWidth()/2-junk.sprite.getWidth()/2,
				Gdx.graphics.getHeight()/2+2.1f*junk.sprite.getHeight()+1)
				.ease(Bounce.OUT).setCallback(callback).start(tm);
		Tween.to(junkgaming, EntityAccessor.POSITION_XY, 1.0f).target(Gdx.graphics.getWidth()/2-junkgaming.sprite.getWidth()/2,
				Gdx.graphics.getHeight()/2-2.1f*junkgaming.sprite.getHeight()-1)
				.ease(Bounce.OUT).start(tm);
		
		
		junkyBlack = new Junk();
		junkyBlack.sprite = new Sprite(Art.blackBackground);
		junkyBlack.sprite.setOrigin(0, 0);
		
		junkyBlack.sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		junkyBlack.position.x=0;
		junkyBlack.position.y=Gdx.graphics.getHeight();
		
	}
	Junk junkyBlack;
	protected void transitionDownScreen() {
		Tween.to(junkyBlack, EntityAccessor.POSITION_XY, 1.0f).target(0,0).ease(Cubic.INOUT).delay(.3f).setCallback(callbackFinish).start(tm);
	}
	
	protected void transitionUpScreen() {
		Tween.to(junkyBlack, EntityAccessor.POSITION_XY, 1.0f).target(0,Gdx.graphics.getHeight()).ease(Cubic.INOUT).start(tm);
	}

	public void logic(float delta) {

		
		if (Gdx.input.isKeyPressed(Keys.G))
		//		|| Gdx.input.isButtonPressed(Buttons.LEFT)) {
			game.setScreen(game.gamescreen);
		//}
		junk.update(delta);
		tm.update(delta);
		// System.out.println();
		junkyBlack.update(delta);
		junkgaming.update(delta);

	}

	@Override
	public void render(float delta) {
		logic(delta);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//sprite.draw(batch);
		junk.render(batch);
		junkgaming.render(batch);
		junkyBlack.render(batch);
		
		batch.end();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batch.dispose();
	//	texture.dispose();
	}

}

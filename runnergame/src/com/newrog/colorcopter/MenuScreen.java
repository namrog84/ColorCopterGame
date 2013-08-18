package com.newrog.colorcopter;

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

public class MenuScreen implements Screen {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;

	ColorCopterGame game;

	public MenuScreen(ColorCopterGame g) {
		game = g;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(1, h / w);
		batch = new SpriteBatch();

		texture = new Texture(Gdx.files.internal("data/menu.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion region = new TextureRegion(texture, 0, 0, 1280, 720);

		sprite = new Sprite(region);
		sprite.setSize(1.0f, 1f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth(), sprite.getHeight() / 2);
		sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);

	}

	public void logic(float delta) {

		if (Gdx.input.isKeyPressed(Keys.G)
				|| Gdx.input.isButtonPressed(Buttons.LEFT)) {
			game.setScreen(game.gamescreen);
		}

		// System.out.println();

	}

	@Override
	public void render(float delta) {
		logic(delta);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
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
		texture.dispose();
	}

}

package com.newrog.colorcopter.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.newrog.coptergame.Art;
import com.newrog.coptergame.MainGame;

public class Block extends Entity {

	// private Texture texture;
	public Sprite sprite;

	public BodyDef groundBodyDef;
	public Body groundBody;
	MainGame game;
	float x, y;
	float up;
	boolean top;

	public Block(float x, float y, MainGame g, float upper, boolean bottom) {
		game = g;
		type = 3;
		this.x = x;
		this.y = y;
		up = upper;
		top = bottom;
		sprite = new Sprite(Art.texture);
		sprite.setSize(Art.texture.getWidth(), upper - y + 16);

		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		// sprite.setPosition(+sprite.getWidth() / 2, +sprite.getHeight() / 2);

		if (bottom) {
			sprite.setPosition(x - sprite.getWidth() / 2, y - 16);
		} else {

			sprite.setPosition(x - sprite.getWidth() / 2, y - 32);
		}
		g.blockList.add(this);
		addedBox = false;
	}

	public boolean addedBox;

	public void addBox() {
		addedBox = true;
		// Create our body definition
		groundBodyDef =  new BodyDef();
		// Set its world position
		if (top)
			groundBodyDef.position.set(new Vector2(x * MainGame.WORLD_TO_BOX, (up - 128) * MainGame.WORLD_TO_BOX));
		else {
			groundBodyDef.position.set(new Vector2(x * MainGame.WORLD_TO_BOX, (32 + y) * MainGame.WORLD_TO_BOX));
		}

		// Create a body from the defintion and add it to the world
		groundBody = game.world.createBody(groundBodyDef);
		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();
		// (setAsBox takes half-width and half-height as arguments)
		if (top) {
			groundBox.setAsBox(32 * MainGame.WORLD_TO_BOX / 2, 128 * MainGame.WORLD_TO_BOX);
		} else {
			groundBox.setAsBox(32 * MainGame.WORLD_TO_BOX / 2, 128 * MainGame.WORLD_TO_BOX / 2);

		}

		groundBody.setUserData(this);
		// Create a fixture from our polygon shape and add it to our ground body
		Fixture f = groundBody.createFixture(groundBox, 0.0f);
		Filter filt = new Filter();
		filt.categoryBits = 0x0001;
		f.setFilterData(filt);
		// Clean up after ourselves
		groundBox.dispose();
	}

	public void update(float delta) {
		// sprite.setRotation(rotation);

	}

	public void render(SpriteBatch batch) {
		batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(),
				sprite.getScaleY(), sprite.getRotation());
		// sprite.draw(batch);

	}

	public void dispose() {
		//texture.dispose();
	}
}

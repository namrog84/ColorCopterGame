package com.newrog.colorcopter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Block extends Entity {

	//private Texture texture;
	public Sprite sprite;
	
	public BodyDef groundBodyDef;
	public Body groundBody;
	MainGame game;
	
	public Block(float x, float y, MainGame g) {
		game = g;
		type = 3;
		//g.texture = new Texture(Gdx.files.internal("data/block4.png"));
		//g.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sprite = new Sprite(Art.texture);
		sprite.setSize(Art.texture.getWidth(), Art.texture.getHeight());

		
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		//sprite.setPosition(+sprite.getWidth() / 2, +sprite.getHeight() / 2);
		
		sprite.setPosition(x-sprite.getWidth() / 2,y-sprite.getHeight() / 2);
		
		
		

		// Create our body definition
		groundBodyDef =new BodyDef();  
		// Set its world position
		groundBodyDef.position.set(new Vector2(x*MainGame.WORLD_TO_BOX, y*MainGame.WORLD_TO_BOX));  

		// Create a body from the defintion and add it to the world
		groundBody = game.world.createBody(groundBodyDef);  

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();  
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(32*MainGame.WORLD_TO_BOX/2, 32*MainGame.WORLD_TO_BOX/2);
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(groundBox, 0.0f); 
		// Clean up after ourselves
		groundBox.dispose();
		
		
		
		
		g.blockList.add(this);
	}

	public void update(float delta) {

		
		sprite.setRotation(rotation);

	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void dispose() {
	//	texture.dispose();
	}
}

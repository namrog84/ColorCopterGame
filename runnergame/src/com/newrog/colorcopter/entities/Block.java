package com.newrog.colorcopter.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.newrog.colorcopter.MainGame;
import com.newrog.colorcopter.resources.Art;

public class Block extends Entity {

	//private Texture texture;
	public Sprite sprite;
	
	public BodyDef groundBodyDef;
	public Body groundBody;
	MainGame game;
	float x,y;
	public Block(float x, float y, MainGame g) {
		game = g;
		type = 3;
		this.x = x;
		this.y = y;
		//g.texture = new Texture(Gdx.files.internal("data/block4.png"));
		//g.texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sprite = new Sprite(Art.texture);
		sprite.setSize(Art.texture.getWidth(), Art.texture.getHeight());

		
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		//sprite.setPosition(+sprite.getWidth() / 2, +sprite.getHeight() / 2);
		
		sprite.setPosition(x-sprite.getWidth() / 2,y-sprite.getHeight() / 2);
		
		
		
		g.blockList.add(this);
		addedBox = false;
	}
	public boolean addedBox;
	
	public void addBox(){
		addedBox = true;
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
	}

	public void update(float delta) {

		
		sprite.setRotation(rotation);

	}

	public void render(SpriteBatch batch) {
		batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
		//sprite.draw(batch);

		
	}

	public void dispose() {
	//	texture.dispose();
	}
}

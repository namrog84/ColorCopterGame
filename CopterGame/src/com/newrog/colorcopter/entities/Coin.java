package com.newrog.colorcopter.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.newrog.coptergame.Art;
import com.newrog.coptergame.Chunk;
import com.newrog.coptergame.MainGame;

public class Coin extends Entity {


	public float stateTime;
	
	TextureRegion currentFrame;

	private BodyDef groundBodyDef;

	public  Body groundBody;
	public Chunk coinChunk;
	public Coin(Chunk c, MainGame game, int x, int y) {
		position.x = x; 
		position.y = y;
		coinChunk = c;
		
		type = 5;

		// Create our body definition
		groundBodyDef =new BodyDef();  
		groundBodyDef.type = BodyType.DynamicBody;
		groundBodyDef.gravityScale = 0;
		// Set its world position
		groundBodyDef.position.set(new Vector2(x*MainGame.WORLD_TO_BOX, y*MainGame.WORLD_TO_BOX));  
		
		
		// Create a body from the defintion and add it to the world
		groundBody = game.world.createBody(groundBodyDef);  
		
		
		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();  
		// (setAsBox takes half-width and half-height as arguments)
		groundBox.setAsBox(8*MainGame.WORLD_TO_BOX/2, 24*MainGame.WORLD_TO_BOX/2, new Vector2(0,0), 0);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = groundBox;
		fixtureDef.density = 0f;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0;
		fixtureDef.isSensor = true;
		
		
		//fixtureDef.isSensor = true;
		
		
		// Create a fixture from our polygon shape and add it to our ground body  
		groundBody.createFixture(fixtureDef);
		
		
		groundBody.setUserData(this);
		
		// Clean up after ourselves
		groundBox.dispose();
		
		
		
	}

	//public boolean visible = true;
	@Override
	public void update(float delta) {
		
		stateTime += delta;
		currentFrame = Art.coinAnimation.getKeyFrame(stateTime, true);
		position.x = 100*groundBody.getPosition().x;
		position.y = 100*groundBody.getPosition().y;
	}

	@Override
	public void render(SpriteBatch batch) {
		//if(visible){
		batch.draw(currentFrame, position.x - Art.coinPlain.getWidth()/2, position.y- Art.coinPlain.getHeight()/2);
		//}

	}

}

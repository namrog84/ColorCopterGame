package com.newrog.coptergame;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Particle {
	public Body body;
	public Sprite s;
	MainGame game;
	public Particle(MainGame g, float x, float y, float angle){
		game = g;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		// bodyDef.angularDamping = 0f;
		bodyDef.fixedRotation = false;

		// Create our body in the world using our body definition
		body = g.world.createBody(bodyDef);
		
		//PolygonShape groundBox = new PolygonShape();  
		// (setAsBox takes half-width and half-height as arguments)
		//groundBox.setAsBox(1, 1);
		// Create a fixture from our polygon shape and add it to our ground body  
		PolygonShape squ = new PolygonShape();
		squ.setAsBox(.03f,.03f);
		bodyDef.fixedRotation = true;
		//circle.setRadius(.03f);
		
		// Clean up after ourselves

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = squ;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.6f;
		fixtureDef.restitution = .4f; // Make it bounce a little bit
		fixtureDef.filter.categoryBits = 0x0008;
		fixtureDef.filter.maskBits = 0x0001;
		
		//body.createFixture(groundBox, 1.0f); 
		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);
		
		body.applyForceToCenter((float)(g.r.nextFloat()*1.4f*Math.cos(angle)), (float)(g.r.nextFloat()*1.4f*Math.sin(angle)), true);
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		squ.dispose();
		switch (g.r.nextInt(3)) {
		case 0:
			s = new Sprite(Art.px_1Black);
			break;
		case 1:
			s = new Sprite(Art.px_1Red);
			break;
		default:
			s = new Sprite(Art.px_1White);

		}
		
		s.setScale(10);
	}
	
	int timer = 200;
	
	public void update(float delta){
		s.setPosition(100*body.getPosition().x, 100*body.getPosition().y);
		s.setRotation(body.getAngle());
		timer--;
		if(timer < 0){
			game.sparks.removeValue(this, true);
			game.world.destroyBody(body);
		}
		//s.setScale(s.getScaleX()*timer/200 );
		//s.setColor(1, 1, 1, 0);
	}
	
	
	public void render(SpriteBatch batch){
		//s.setColor(1, 1, 1, timer/255);
		batch.setColor(1, 1, 1, (timer)/200f);
		batch.draw(s, s.getX(), s.getY(), s.getOriginX(), s.getOriginY(), s.getWidth(), s.getHeight(), s.getScaleX(), s.getScaleY(), s.getRotation());
		batch.setColor(1, 1, 1, 1);
	}
	
	
}

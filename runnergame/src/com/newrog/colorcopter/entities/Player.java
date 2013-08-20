package com.newrog.colorcopter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.newrog.colorcopter.MainGame;
import com.newrog.colorcopter.resources.Art;
import com.newrog.colorcopter.resources.MyAudio;

public class Player extends Entity {

	public static final float MAXSPEED = 4;
	// private Texture texture;
	public Sprite sprite;

	public Body body;
	MainGame game;

	public float stateTime;
	TextureRegion currentFrame;
	ParticleEffect pWindFromProp = new ParticleEffect();
	boolean alive = true;

	public Player(MainGame g) {
		super();

		game = g;

		type = 99;
		pWindFromProp.load(Gdx.files.internal("data/wind"), Gdx.files.internal("effects"));

		sprite = new Sprite(Art.heliTexture);
		sprite.setSize(Art.heliTexture.getWidth(), Art.heliTexture.getHeight());

		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(2, 6);
		// bodyDef.angularDamping = 0f;
		bodyDef.fixedRotation = true;

		// Create our body in the world using our body definition
		body = g.world.createBody(bodyDef);
		body.setUserData(this);
		
		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(.20f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 1.5625f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.3f; // Make it bounce a little bit
		
		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);

		
		// fixture.notify();
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();

		MyAudio.helicopterProp.loop();

	}

	public boolean isAlive() {
		return alive;
	}

	private boolean showWind = false;
	boolean started = false;
	public int timeHit = 60;
	float lastVelocity = 0;

	public void update(float delta) {

		
		
		
		pWindFromProp.setPosition(position.x, position.y + 20);
		for (int i = 0; i < pWindFromProp.getEmitters().size; i++) { // get the list of emitters - things that emit particles
			pWindFromProp.getEmitters().get(i).getTransparency().setHigh(.15f);
			pWindFromProp.getEmitters().get(i).getAngle().setLow(rotation - 90); // low is the minimum rotation
			pWindFromProp.getEmitters().get(i).getAngle().setHigh(rotation - 90); // high is the max rotation
		}
		pWindFromProp.update(delta);

		if (!alive)
			return;

		if (started && timeHit > 0)
			timeHit--;

		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.SPACE)) {
			body.applyForceToCenter(0, 3.5f, true);
			showWind = true;
			if (!started)
				game.gameOverLabel.setText("");
			started = true;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			// y -= 4f;

		}

		/*
		 * if (Gdx.input.isKeyPressed(Keys.LEFT)) { x -= 3f; z += 3f; //player.body.applyLinearImpulse(-0.01f, 0, 0, 0, true);
		 * player.body.applyForceToCenter(-1f, 0, true); } if (Gdx.input.isKeyPressed(Keys.RIGHT)) { x += 3f; z -= 3f;
		 * 
		 * player.body.applyForceToCenter(1f, 0, true);
		 * 
		 * }
		 */

		if (body.getLinearVelocity().x > 0) {
			int modifier = 5;
			if (rotation > 0)
				modifier = 15;

			rotation -= modifier * (body.getLinearVelocity().x - lastVelocity);
			lastVelocity = body.getLinearVelocity().x;
		}
		if (body.getLinearVelocity().x < 0) {

			if (body.getLinearVelocity().x < 3) {
				rotation += 2;
			}
		}

		if (rotation > 15) {
			rotation = 15;
		} else if (rotation < -15) {
			rotation = -15;
		}

		if (Gdx.input.isKeyPressed(Keys.R)) {
			rotation += 1f;
		} else if (Gdx.input.isKeyPressed(Keys.E)) {
			rotation -= 1f;

		}

		body.applyForceToCenter(0.5f, 0, true);
		if (body.getLinearVelocity().x > Player.MAXSPEED) {
			body.applyForceToCenter(-1f, 0, true);
		}

		sprite.setPosition(100 * body.getPosition().x - 100, 100 * body.getPosition().y - 30);
		sprite.setRotation(rotation);
		position.set(100 * body.getPosition().x - 100, 100 * body.getPosition().y - 30);
		// if(body.getLocalCenter())
		// System.out.println(body.getPosition().x);

		if (position.x > 16000) {
			game.gameOverLabel.setText("YOU WIN!");
			// gameFinished = true;
		}

		stateTime += Gdx.graphics.getDeltaTime();
	}

	public void render(SpriteBatch batch) {
		if (showWind) {
			pWindFromProp.draw(batch);
		}

		
		if(alive){
			// sprite.setTexture(Art.heliAnimation.getKeyFrame(stateTime, true));
			currentFrame = Art.heliAnimation.getKeyFrame(stateTime, true);
			batch.draw(currentFrame, position.x, position.y, 192 / 2f, 64 / 2f, 192, 64, 1, 1, (int) rotation);
			// sprite.draw(batch);
		}
	}

	public void dispose() {

	}

	public void kill() {
		alive = false;
		MyAudio.helicopterProp.stop();
		showWind = false;
		// game.world.destroyBody(body);

	}

	public boolean removed = false;

	public void removed() {
		removed = true;
	}

	public boolean isRemoved() {
		return removed;
	}

}

package com.newrog.colorcopter.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

	public Vector2 position;

	public int type = 0;
	protected float width;
	protected float height;
	protected float rotation; 

	public Entity() {
		position = new Vector2(0,0);
	}

	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	
	
}

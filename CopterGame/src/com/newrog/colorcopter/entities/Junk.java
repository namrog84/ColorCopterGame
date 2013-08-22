package com.newrog.colorcopter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Junk extends Entity{
	public Sprite sprite;
	
	public Junk(){
		Texture texture;
		
		texture = new Texture(Gdx.files.internal("data/84bit_3xpixel.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		sprite = new Sprite(texture);
		sprite.setScale(5);
		//sprite.setPosition(00, 00);
		position.x = 0;
		position.y = 0;
		
	}
	@Override
	public void update(float delta) {
		sprite.setPosition(position.x, position.y);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
		
		
	}

}

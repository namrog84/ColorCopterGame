package com.newrog.coptergame;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.newrog.colorcopter.entities.Coin;
import com.newrog.colorcopter.entities.Entity;

public class Contacter implements ContactListener{

	MainGame g;
	public Contacter(MainGame game){
		g = game;
	}
	
	@Override
	public void beginContact(Contact contact) {
		
		
		//Entity eA = ((Entity)(contact.getFixtureA().getBody().getUserData()));
		//Entity eB = ((Entity)(contact.getFixtureB().getBody().getUserData()));
		//checkEntity(eA);
		//checkEntity(eB);
	}

	//private void checkEntity(Entity e) {
		//if(e != null){
	//		System.out.println(e + "  " + e.type);
			//System.out.println(e.type);
		//	if(e.type == 5 ){
				
			//	Coin c = (Coin)e;
			/*	if(c.hit = false){
					c.coinChunk.addCoinToBeRemoved(c);
					g.partEffects.setPosition(c.position.x, c.position.y);
					g.partEffects.start();
					c.hit = true;
				}*/
		//	}
		//}
	//}

	@Override
	public void endContact(Contact contact) {
		
		//if(().
		
				
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	

}

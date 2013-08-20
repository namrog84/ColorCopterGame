package com.newrog.colorcopter;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.newrog.colorcopter.entities.Block;
import com.newrog.colorcopter.entities.Coin;
import com.newrog.colorcopter.entities.EmptyBlock;
import com.newrog.colorcopter.entities.Entity;

public class Chunk {

	public static final int chunkWidth = 50;
	private int chunkHeight = 60;
	public Entity[][] blocks = new Entity[chunkWidth][chunkHeight];
	int ticks = 0;

	MainGame game;
	Random r;
	private int roughness = 35;
	private int windyness = 50;
	private int[] roll = { -2, -1, 1, 2 };
	public static int currentWidth = 25;
	private float offR;
	private float offG;
	private float offB;

	public int chunkPosition = 0;
	int chunkOffset = 0;
	public static int lastChunkY;
	public Array<Coin> coins = new Array<Coin>();
	static int chunkDiff = 0;

	public Chunk(MainGame game, int cp) {
		this.game = game;
		r = game.r;
		offR = 2 * r.nextFloat() - 1f;
		offG = 2 * r.nextFloat() - 1f;
		offB = 2 * r.nextFloat() - 1f;
		chunkPosition = cp;

		chunkOffset = chunkPosition * chunkWidth * 32;
		if ( chunkDiff < 8) { //chunkPosition % 2 == 0 &&
			chunkDiff++;
		}
		//System.out.println("diff: " + chunkDiff);
	}

	public void render(SpriteBatch batch) {

		//float Rfluc = (float) (Math.cos((ticks * offR) / 100f) * .1f);
		//float Gfluc = (float) (Math.cos((ticks * offG) / 100f) * .1f);
		//float Bfluc = (float) (Math.cos((ticks * offB) / 100f) * .1f);

		//batch.setColor(.9f + Rfluc, .9f + Gfluc, .9f + Bfluc, 1f);
		
		for (int i = 0; i < chunkWidth; i++) {
			for (int j = 0; j < chunkHeight; j++) {

				blocks[i][j].render(batch);
			}
		}
		//batch.setColor(Color.WHITE);
		
		if (coins.size > 0) {
			for (Coin c : coins) {
				// if(c!=null)
				c.render(batch);
			}
		}
	}
	int changeY1= 0;
	int changeY2= 0;
	
	int lastY1= 0;
	int lastY2= 0;
	
	
	
	private void buildLevel(Entity[][] bb, int sX, int sY) {
		lastY1 = sY;
		
		fillBlocks(bb, 0);

		for (int i = 0; i < chunkWidth - sX; i++) {
			if (r.nextInt(100) <= roughness) {
				int ro = r.nextInt(4);
				changeY2 = roll[ro];
				currentWidth += changeY2;

				if (currentWidth < 15 - chunkDiff)
					currentWidth = 15 - chunkDiff;
				if (currentWidth > 25 - (1.5) * chunkDiff)
					currentWidth = (int) (25 - (1.5) * chunkDiff);
			}

			if (r.nextInt(100) <= windyness) {
				changeY1 = roll[r.nextInt(4)]; 
				sY += changeY1;
				if (sY < 3)
					sY = 3;

			}
			if (sY + currentWidth > 57) {
				sY = sY - (sY + currentWidth - 55);
			}

			for (int j = 0; j < currentWidth; j++) {
				if (bb[sX + i][sY + j].type == 3) {
					if (((Block) bb[sX + i][sY + j]).addedBox) {
						game.world.destroyBody(((Block) bb[sX + i][sY + j]).groundBody);
					}
				}
				bb[sX + i][sY + j] = new EmptyBlock();
			}
			
			
			
			/*lastY1 = lastY1 - sY + changeY2;
			for(int z = 0; z < lastY1; z++){//if(lastY1 > 0)
				((Block) bb[sX + i][sY + currentWidth + z]).addBox(); // add top
			}
			if(lastY1 == 0)
				((Block) bb[sX + i][sY + currentWidth]).addBox(); // add top
			//for(int z = 0; z > lastY1; z--){
//				((Block) bb[sX + i-1][sY + currentWidth - z] ).addBox(); // add top
			//}
			lastY1 = sY ;
			*/
			
			/*
			for(int z = 0; z < changeY1+changeY2; z++){
				((Block) bb[sX + i][sY + currentWidth + z]).addBox(); // add top
				((Block) bb[sX + i][sY - z-1]).addBox(); // add bottom?
			}
			if( 0 == changeY1+changeY2){
				((Block) bb[sX + i][sY + currentWidth]).addBox(); // add top
				((Block) bb[sX + i][sY - 1]).addBox(); // add bottom?
			}
			for(int z = 0; z > changeY1+changeY2; z--){
				((Block) bb[sX + i][sY + currentWidth - z]).addBox(); // add top
				((Block) bb[sX + i][sY + z - 1]).addBox(); // add bottom?
			}*/
			
			
			((Block) bb[sX + i][sY + currentWidth + 0]).addBox(); // add top
			((Block) bb[sX + i][sY + currentWidth + 1]).addBox(); // add top
			((Block) bb[sX + i][sY + currentWidth + 2]).addBox(); // add top
			
			((Block) bb[sX + i][sY - 1]).addBox(); // add bottom?
			((Block) bb[sX + i][sY - 2]).addBox(); // add bottom?
			((Block) bb[sX + i][sY - 3]).addBox(); // add bottom?

			if (r.nextInt(100) < 8) {
				coins.add(new Coin(this, game, chunkOffset + (sX + i) * 32, (sY * 32) + (currentWidth * 16)));
			}
		}

		lastChunkY = sY;
	}

	void fillBlocks(Entity[][] b, int x) {
		for (int i = 0; i < chunkWidth; i++) {
			for (int j = 0; j < chunkHeight; j++) {
				b[i][j] = new Block(chunkOffset + x * 32 + i * 32, j * 32, game);

			}
		}
	}

	Array<Coin> removingCoins = new Array<Coin>();

	public void update(float delta) {
		ticks++;
		for (Coin c : coins) {
			c.update(delta);
		}

		coins.removeAll(removingCoins, true);

		for (Coin rc : removingCoins) {
			game.world.destroyBody(rc.groundBody);
		}
		removingCoins.clear();

	}
	
	public void reset() {
		//chunkDiff = 0;
		removingCoins.clear();
		coins.clear();
		if (chunkPosition == 0) {
			buildLevel(blocks, 8, 20);

			// starting area only
			for (int i = 2; i < 10; i++) {
				for (int j = 20; j < 20 + 10; j++) {
					if (blocks[i][j].type == 3) {
						if (((Block) blocks[i][j]).addedBox) {
							game.world.destroyBody(((Block) blocks[i][j]).groundBody);
						}
						blocks[i][j] = new EmptyBlock();
					}
				}
			}

			for (int i = 2; i < 10; i++) {
				if (blocks[i][19].type == 3) {
					((Block) blocks[i][19]).addBox();

				}
			}
			for (int i = 2; i < 10; i++) {
				if (blocks[i][30].type == 3) {
					((Block) blocks[i][30]).addBox();

				}
			}


		} else {
			buildLevel(blocks, 0, lastChunkY);
		}
		// clear out starting area

	}

	public Array<Coin> getCoins() {
		return coins;
	}

	public void addCoinToBeRemoved(Coin co) {
		removingCoins.add(co);
	}

	public void cleanUp() {
		for (Coin c : coins) {
			game.world.destroyBody(c.groundBody);
		}
		
		for (int i = 0; i < chunkWidth; i++) {
			for (int j = 0; j < chunkHeight; j++) {
				if (blocks[i][j].type == 3 && ((Block) blocks[i][j]).groundBody != null) {
					game.world.destroyBody(((Block) blocks[i][j]).groundBody);
				}
			}
		}

	}

}

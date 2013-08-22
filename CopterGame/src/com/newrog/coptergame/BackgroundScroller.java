package com.newrog.coptergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.newrog.colorcopter.utils.SimplexNoise;

public class BackgroundScroller {
	
	private static final int parallelChunkWidth = 75;
	private static final int parallelChunkHeight = 80;
	
	private boolean[][] blockBG2 = new boolean[parallelChunkWidth][parallelChunkHeight];
	private boolean[][] blockBG2_swap = new boolean[parallelChunkWidth][parallelChunkHeight];
	private float offset1 = parallelChunkWidth*18;
	private float distSinceLast = 0;
	private float distanc = 0;
	private int chunkCount1 = 0;
	private int chunkCount2 = 1;
	private int countt = 1;

	
	MainGame game;
	public BackgroundScroller(MainGame game){
		this.game = game;
		
	}
	
	

	public void init() {
		countt = 1;
		distSinceLast = 0;
		distanc = 0;
		chunkCount1 = 0;
		chunkCount2 = 1;

		generateBackground(chunkCount1);
		generateBackground2(chunkCount2);
	}
	
	
	double fillRate = .3;

	public void generateBackground(int off) {
		double factor = 10;
		for(int i = 0; i < parallelChunkWidth; i++){
			for(int j = 0; j < parallelChunkHeight; j++){
				
				if(SimplexNoise.noise((i+(parallelChunkWidth*off))/factor, (j+(parallelChunkHeight*off))/factor)>fillRate){
					blockBG2[i][j] = true;
				}else{
					blockBG2[i][j] = false;
				}
			}
		}
	}

	public void generateBackground2(int off) {
		double factor = 10;
		for(int i = 0; i < parallelChunkWidth; i++){
			for(int j = 0; j < parallelChunkHeight; j++){
				if(SimplexNoise.noise((i+(parallelChunkWidth*off))/factor, (j+(parallelChunkHeight*off))/factor)>fillRate){
					blockBG2_swap[i][j] = true;
				}else{
					blockBG2_swap[i][j] = false;
				}
			}
		}
	}


	public void render(SpriteBatch batch) {
		batch.draw(Art.blackBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		 
		drawBackground(batch, blockBG2, chunkCount1);
		drawBackground(batch, blockBG2_swap, chunkCount2);
		
		if(distanc > offset1*countt+(parallelChunkWidth*24) ){
			if(countt%2 == 1){
				chunkCount1+=2;				
				generateBackground(chunkCount1);
			}else{
				chunkCount2+=2;				
				generateBackground2(chunkCount2);
			}
			countt++;
			//System.out.println("DD " + extra);
			
			distanc -= offset1;
		}
		distanc += game.tmpCam.x - distSinceLast;
		distSinceLast = game.tmpCam.x;
		
	}
	
	private void drawBackground(SpriteBatch sb, boolean[][] bb, int cc) {
		for(int i = 0; i < parallelChunkWidth; i++){
			for(int j = 0; j < parallelChunkHeight; j++){
				if(bb[i][j]){
					sb.draw(Art.pBackground2, i*18+(cc*offset1)+(game.tmpCam.x)*.50f-360, j*18+game.tmpCam.y/3-0, 18, 18);
				}
			}
		}
	}


	
}

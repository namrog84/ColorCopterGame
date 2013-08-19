package com.newrog.colorcopter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackgroundScroller {
	
	private int parallelChunkWidth = 200;
	private boolean[][] blockBG2 = new boolean[parallelChunkWidth][200];
	private boolean[][] blockBG2_swap = new boolean[parallelChunkWidth][200];
	private float offset1 = parallelChunkWidth*16;
	private float distSinceLast = 0;
	private float distanc = 0;
	private int chunkCount1 = 0;
	private int chunkCount2 = 1;
	private int countt = 1;

	
	MainGame game;
	public BackgroundScroller(MainGame game){
		this.game = game;
		
	}
	

	public void generateBackground(int off) {
		double factor = 10;
		for(int i = 0; i < parallelChunkWidth; i++){
			for(int j = 0; j < 200; j++){
				
				if(SimplexNoise.noise((i+(parallelChunkWidth*off))/factor, (j+(200*off))/factor)>.4){
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
			for(int j = 0; j < 200; j++){
				if(SimplexNoise.noise((i+(parallelChunkWidth*off))/factor, (j+(200*off))/factor)>.4){
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
		distanc += -game.tmpCam.x - distSinceLast;
		distSinceLast = -game.tmpCam.x;
		
	}
	
	private void drawBackground(SpriteBatch sb, boolean[][] bb, int cc) {
		for(int i = 0; i < parallelChunkWidth; i++){
			for(int j = 0; j < 200; j++){
				if(bb[i][j]){
					sb.draw(Art.pBackground2, i*16+(cc*offset1)+(-game.tmpCam.x)*.50f-640, j*16+-game.tmpCam.y/3-480, 16, 16);
				}
			}
		}
	}


	public void init() {
		generateBackground(chunkCount1);
		generateBackground2(chunkCount2);
	}
	
}

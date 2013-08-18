package com.newrog.colorcopter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;

public class MainGame implements Screen {

	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;
	public OrthographicCamera camera;
	private Random r = new Random();
	private SpriteBatch batch;
	private int ticks = 0;

	World world = new World(new Vector2(0, -10), true);
	int[] roll = { -2, -1, 1, 2 };

	int length = 450;
	int roughness = 25;
	int windyness = 40;

	//int StartX = 8;
	//int StartY = 20;

	ColorCopterGame game;
	Player player;

	int chunkWidth = 100;
	int chunkHeight = 60;

	
	private Entity[][] blocks = new Entity[chunkWidth][chunkHeight];
	
	public ArrayList<Entity> blockList = new ArrayList<Entity>();
	public Array<Coin> coins = new Array<Coin>();

	
	int offy = 50;

	
	int bgx = 0;
	int bgx2= 640;
	boolean gameFinished = false;

	
	BackgroundScroller bgScroll;
	//private boolean[][] blockBG = new boolean[20][50];


	//private float offset2 = 800;

	
	private float extra = 0;
	
	
	ParticleEffect partEffects = new ParticleEffect();

	
	Stage stage = new Stage();
	TextureAtlas ta;
	int score = 0;
	Label l;

	int currentWidth = 25;
	
	Label gameOverLabel;
	public MainGame(ColorCopterGame g) {
		game = g;
		
		partEffects.load(Gdx.files.internal("data/coinBurst"), Gdx.files.internal("effects"));
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(w, h);
		
		
		bgScroll = new BackgroundScroller(this);
		
		//ta = new TextureAtlas(Gdx.files.internal("font/hel32"));
		
		GUIBuilder();
		
		


		batch = new SpriteBatch();
		player = new Player(this);
		
		buildLevel(blocks, 8, 20);
		
		for (int i = 2; i < 10; i++) {
			for (int j = 20; j < 20 + 10; j++) {
				if(blocks[i][j].type == 3){
					world.destroyBody(((Block) blocks[i][j]).groundBody);
					blocks[i][j] = new EmptyBlock();
				}
			}
		}
		player.body.setTransform(2, 20 * 32 * WORLD_TO_BOX, 0);

		
		bgScroll.init();


	}

	private void GUIBuilder() {

		BitmapFont font = new BitmapFont(Gdx.files.internal("data/font2.fnt"), Gdx.files.internal("data/font2_0.png"), false);
		LabelStyle ls = new LabelStyle(font, Color.WHITE);
		l = new Label("Score", ls);
		l.setPosition(0, 50);
		l.setWidth(Gdx.graphics.getWidth());
		l.setAlignment(Align.center);
		
		//l.setColor(Color.GREEN);
		l.setFontScale(6);
		stage.addActor(l);
		
		gameOverLabel = new Label("Good luck!", ls);
		gameOverLabel.setPosition(0, 450);
		gameOverLabel.setWidth(Gdx.graphics.getWidth());
		gameOverLabel.setAlignment(Align.center);
		gameOverLabel.setFontScale(5);
		stage.addActor(gameOverLabel);
	}

	int hitCounter = 0;
	

	private void generateLevel(int off) {
		double factor = 10;
		for(int i = 0; i < chunkWidth; i++){
			for(int j = 0; j < chunkHeight; j++){
				
				//if(SimplexNoise.noise((i+(chunkWidth*off))/factor, (j+(200*off))/factor)>.4){
				//	blockBG2[i][j] = true;
				//}else{
				//	blockBG2[i][j] = false;
				//}
			}
		}
	}

	private void generateLevel2(int off) {
		double factor = 10;
		for(int i = 0; i < chunkWidth; i++){
			for(int j = 0; j < chunkHeight; j++){
				//if(SimplexNoise.noise((i+(chunkWidth*off))/factor, (j+(200*off))/factor)>.4){
				//	blockBG2_swap[i][j] = true;
				//}else{
				//	blockBG2_swap[i][j] = false;
				//}
			}
		}
	}
	
	public void logic(float delta) {
		
		if (Gdx.input.isKeyPressed(Keys.M) || Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			game.setScreen(game.menuscreen);
		}

		player.update(delta);
		for(Coin c: coins){
			c.update(delta);
		}
		Array<Coin> removingCoins = new Array<Coin>();
		if (world.getContactCount() > 0) {

			List<Contact> contacts = world.getContactList();
			for (Contact c : contacts) {
				if (c.isTouching())
					for (Entity e : blockList) {

						if (e.type == 3) {
							Block b = (Block) e;

							if (c.getFixtureB().getBody().equals(b.groundBody) || c.getFixtureA().getBody().equals(b.groundBody)) {
								b.sprite.setTexture(Art.texture2);
								if(player.timeHit<=0){
									hitCounter++;
									gameOverLabel.setText("You Hit the Wall! " + hitCounter);
								}
							}
						}

					}
				
					for(Entity e: coins){
						if (e.type == 5) {
							Coin co = (Coin)e;
							if (c.getFixtureB().getBody().equals(co.groundBody) || c.getFixtureA().getBody().equals(co.groundBody)) {
								System.out.println("beep");
								MyAudio.coinSound.play();
								partEffects.setPosition(co.position.x, co.position.y);
								partEffects.start();
								removingCoins.add(co);
								score++;
								l.setText(""+score);
								
								//co.visible = false;
								
								//world.destroyBody(co.groundBody);
							}
							
						}
					}
					
			}

		}
		partEffects.update(delta);
		


		for(Coin rc: removingCoins){
			world.destroyBody(rc.groundBody);
		}
		coins.removeAll(removingCoins, true);
		
		
		if (ticks % 60 == 0) {
			System.out.println(Gdx.graphics.getFramesPerSecond());
			System.out.println("World: " + world.getBodyCount());
		}
		ticks++;
		
		if(player.position.x > 16000){
			gameOverLabel.setText("YOU WIN!");
			//gameFinished = true;
		}
	}
	
	public void draw(SpriteBatch batch, float parentAlpha){
		partEffects.draw(batch);
	}
	

	//private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	@Override
	public void render(float delta) {
		logic(delta);
		
		float lerp = .3f;
		camera.position.x += Math.round((player.sprite.getX() - camera.position.x) * lerp);
		camera.position.y += Math.round((player.sprite.getY() - camera.position.y) * lerp);

		camera.position.y = MathUtils.clamp(camera.position.y, 340, 1568);
		if (camera.position.x < 620)
			camera.position.x = 620;
		camera.update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		bgScroll.render(batch);
		

		partEffects.draw(batch);
		
		for(Coin c: coins){
			c.render(batch);	
		}
		
		player.render(batch);

		
		for (int i = 0; i < chunkWidth; i++) {
			for (int j = 0; j < chunkHeight; j++) {
				blocks[i][j].render(batch);
			}
		}
		
		/*for (int i = 0; i < chunkWidth; i++) {
			for (int j = 0; j < chunkHeight; j++) {
				blocks2[i][j].render(batch);
			}
		}*/
		
		
		batch.end();
		
		// Matrix4 debugCam = camera.combined.cpy();
		// debugRenderer.render(world, debugCam.scl(BOX_TO_WORLD));
		
		stage.act();
		stage.draw();
		
		//if(!gameFinished)
			world.step(1 / 60f, 6, 2);

	}
	


	private void buildLevel(Entity[][] bb, int sX, int sY) {

		fillBlocks(bb, 0);

		for (int i = 0; i < chunkWidth - sX; i++) {
			if (r.nextInt(100) <= roughness) {
				int ro = r.nextInt(4);
				currentWidth += roll[ro];
				
				if (currentWidth < 15)
					currentWidth = 15;
				if (currentWidth > 25)
					currentWidth = 25;
			}

			if (r.nextInt(100) <= windyness) {
				sY += roll[r.nextInt(4)];
				if (sY < 0)
					sY = 0;

			}
			if (sY + currentWidth > 58) {
				sY = sY - (sY + currentWidth - 55);
			}

			for (int j = 0; j < currentWidth; j++) {
				if (bb[sX + i][sY + j].type == 3) {
					world.destroyBody(((Block) bb[sX+ i][sY + j]).groundBody);
				}
				bb[sX + i][sY + j] = new EmptyBlock();
			}
			
			if(r.nextInt(100) < 20){
				coins.add(new Coin(this, (sX+i)*32, (sY*32)+(currentWidth*16)));
			}
		}
	}
	
	void fillBlocks(Entity[][] b, int x) {
		for (int i = 0; i < chunkWidth; i++) {
			for (int j = 0; j < 60; j++) {
				b[i][j] = new Block(x * 32 + i * 32, j * 32, this);

			}
		}
	}
	
	

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
	}

}

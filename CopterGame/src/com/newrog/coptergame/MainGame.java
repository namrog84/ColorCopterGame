package com.newrog.coptergame;

import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Cubic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.tablelayout.BaseTableLayout.Debug;
import com.newrog.colorcopter.entities.Block;
import com.newrog.colorcopter.entities.Coin;
import com.newrog.colorcopter.entities.EmptyBlock;
import com.newrog.colorcopter.entities.Entity;
import com.newrog.colorcopter.entities.EntityAccessor;
import com.newrog.colorcopter.entities.Junk;
import com.newrog.colorcopter.entities.Player;

/* Todo
 * done				Background blur?
 * done proper reset (upon death?)
 * partial:   color changer maybe?
 * done: infinite level
 * 
 * 
 * main menu?
 * explosion death
 *   
 * 
 */

public class MainGame implements Screen {

	public static final float WORLD_TO_BOX = 0.01f;
	public static final float BOX_TO_WORLD = 100f;

	public ArrayList<Entity> blockList = new ArrayList<Entity>();


	public World world;
	public OrthographicCamera camera;
	public OrthographicCamera cameraGUI;
	public CopterGame game;
	public Player player;

	public Label gameOverLabel;
	public Stage stage = new Stage();
	public TextureAtlas ta;

	private BackgroundScroller bgScroll;
	public Random r = new Random();
	private SpriteBatch batch;
	
	private ParticleEffect partEffects = new ParticleEffect();

	int distanceScore;
	// private boolean gameFinished = false;

	int hitCounter = 0;

	
	int bestScore = 0;
	int bestDistance = 0;
	
	// private int offy = 50;
	// private int bgx = 0;
	// private int bgx2 = 640;
	private int score = 0;
	private Label coinScoreLabel,distanceScoreLabel;
	private Label fpsWorld;

	private TextButton resetButton;

	// private int length = 450;

	//Array<Chunk> chunks;
	LinkedList<Chunk> chunkList;
	
	int chunkCounter;
	
	//Chunk chunk;
	//Chunk chunk2;
	public Array<Particle> sparks;
	// int StartX = 8;
	// int StartY = 20;

	// private boolean[][] blockBG = new boolean[20][50];

	//ShaderProgram blurShader;
	//FrameBuffer blurTargetA, blurTargetB;
	/*
	TextureRegion fboRegion;
	public static final int FBO_SIZE = 2048;
	public static final float MAX_BLUR = 2f;

	final String VERT = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

	"uniform mat4 u_projTrans;\n" + " \n" + "varying vec4 vColor;\n" + "varying vec2 vTexCoord;\n" +

	"void main() {\n" + "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" + "	vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" + "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" + "}";

	final String FRAGCHEAP =
	// GL ES specific stuff
	"#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" + //
			"varying LOWP vec4 vColor;\n" + "varying vec2 vTexCoord;\n" + "uniform sampler2D u_texture;\n" + "uniform float bias;\n" + "void main() {\n" + "	vec4 texColor = texture2D(u_texture, vTexCoord, bias);\n" + "	\n" + "	gl_FragColor = texColor * vColor;\n" + "}";

	final String FRAG = "#ifdef GL_ES\n" + "#define LOWP lowp\n" + "precision mediump float;\n" + "#else\n" + "#define LOWP \n" + "#endif\n" + "varying LOWP vec4 vColor;\n" + "varying vec2 vTexCoord;\n" + "\n" + "uniform sampler2D u_texture;\n" + "uniform float resolution;\n" + "uniform float radius;\n" + "uniform vec2 dir;\n" + "\n" + "void main() {\n" + "	vec4 sum = vec4(0.0);\n" + "	vec2 tc = vTexCoord;\n" + "	float blur = radius/resolution; \n" + "    \n" + "    float hstep = dir.x;\n" + "    float vstep = dir.y;\n" + "    \n" + "	sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.05;\n" + "	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.09;\n" + "	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.12;\n" + "	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.15;\n" + "	\n" + "	sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.16;\n" + "	\n" + "	sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.15;\n" + "	sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.12;\n" + "	sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.09;\n" + "	sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.05;\n" + "\n" + "	gl_FragColor = vColor * vec4(sum.rgb, 1.0);\n" + "}";
*/
	
	float w, h;
	private boolean zoomOut = false;
	private int tick;
	private Junk junkyBlack;
	private TweenManager tm;;

	public MainGame(CopterGame g) {
	
		ShaderProgram.pedantic = false;
		/*blurShader = new ShaderProgram(VERT, FRAG);
		if (!blurShader.isCompiled()) {
			System.err.println(blurShader.getLog());
			System.exit(0);
		}
		if (blurShader.getLog().length() != 0)
			System.out.println(blurShader.getLog());

		// setup uniforms for our shader
		blurShader.begin();
		blurShader.setUniformf("dir", 0f, 0f);
		blurShader.setUniformf("resolution", FBO_SIZE);
		blurShader.setUniformf("radius", 1f);
		blurShader.end();

		blurTargetA = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
		blurTargetB = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
		fboRegion = new TextureRegion(blurTargetA.getColorBufferTexture());
		fboRegion.flip(false, true);
*/
		
		tm = new TweenManager();
		
		game = g;
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(w, h);
		cameraGUI =new OrthographicCamera(w, h);
		cameraGUI.translate(w/2, h/2);
		cameraGUI.update();
		bgScroll = new BackgroundScroller(this);
		
		
		System.out.println("load");
		partEffects.load(Gdx.files.internal("data/coinBurst"), Gdx.files.internal("effects"));
		System.out.println("done");
		
		junkyBlack = new Junk();
		junkyBlack.sprite = new Sprite(Art.blackBackground);
		junkyBlack.sprite.setOrigin(0, 0);
		
		junkyBlack.sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		junkyBlack.position.x=0;
		junkyBlack.position.y=0;
		
		Tween.registerAccessor(Entity.class, new EntityAccessor());
		
		chunkCounter = 0;
		//chunks = new Array<Chunk>();
		chunkList = new LinkedList<Chunk>();
		chunkList.add(new Chunk(this,chunkCounter++));
		//chunks.add(new Chunk(this,chunkCounter++));
		//chunks.add(new Chunk(this,chunkCounter++));
		
		sparks = new Array<Particle>();
		
		GUIBuilder();

		
		
		/*callbackStart = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				
			}
		};*/
		resetCallback = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				//System.out.println("beep");
				transitionUpScreen();
				//Timeline.createSequence().beginSequence().delay(1).setCallback(callbackStart).start();
				
			}
		};


	}
	//private TweenCallback callbackStart;

	/*
	 * private void generateLevel(int off) { double factor = 10; for(int i = 0;
	 * i < chunkWidth; i++){ for(int j = 0; j < chunkHeight; j++){
	 * 
	 * //if(SimplexNoise.noise((i+(chunkWidth*off))/factor,
	 * (j+(200*off))/factor)>.4){ // blockBG2[i][j] = true; //}else{ //
	 * blockBG2[i][j] = false; //} } } }
	 * 
	 * private void generateLevel2(int off) { double factor = 10; for(int i = 0;
	 * i < chunkWidth; i++){ for(int j = 0; j < chunkHeight; j++){
	 * //if(SimplexNoise.noise((i+(chunkWidth*off))/factor,
	 * (j+(200*off))/factor)>.4){ // blockBG2_swap[i][j] = true; //}else{ //
	 * blockBG2_swap[i][j] = false; //} } } }
	 */
	
	
	public void logic(float delta) {

		
	
		
		//if (Gdx.input.isKeyPressed(Keys.M) || Gdx.input.isButtonPressed(Buttons.RIGHT)) {
		//	game.setScreen(game.menuscreen);
		//}

		//if (Gdx.input.isKeyPressed(Keys.Q)) {
		//	player.kill();
		//}
		
		
		player.update(delta);
		
		distanceScore = (int) player.position.x/10;
		if(distanceScore > bestDistance)
			bestDistance = distanceScore;
		
		
		if(distanceScore>15){
			distanceScoreLabel.setText("Dist:" + distanceScore + " ~ Best: " + bestDistance + "  " );
		}
		
		if (world.getContactCount() > 0) {

			List<Contact> contacts = world.getContactList();
			for (Contact c : contacts) {
				if (c.isTouching())
					if (player.isAlive()) {

						
						 Entity be = (Entity) c.getFixtureA().getBody().getUserData();
						 if(be.type == 3){
							 Block bl = (Block) c.getFixtureA().getBody().getUserData();
								if(bl != null){
									bl.sprite.setTexture(Art.texture2);
									if (player.timeHit <= 0) {
										// hitCounter++;
										gameOverLabel.setText("You Hit the Wall and Died! :(");

										player.kill();
										resetButton.setVisible(true);
									}
								}
	 
						 }
						 						
						/*for (Entity e : blockList) {
							if (e.type == 3) {
								Block b = (Block) e;

								if ((c.getFixtureB().getBody().equals(b.groundBody) && c.getFixtureA().getBody().equals(player.body))
										|| (c.getFixtureA().getBody().equals(b.groundBody)) && c.getFixtureB().getBody().equals(player.body)) {
									b.sprite.setTexture(Art.texture2);
									if (player.timeHit <= 0) {
										// hitCounter++;
										gameOverLabel.setText("You Hit the Wall and Died! :(");

										player.kill();
										resetButton.setVisible(true);

									}
								}
							}
						}*/
						
						
					
					for(Chunk chunky: chunkList){
						Array<Coin> coins = chunky.getCoins();
						for (Entity e : coins) {
							if (e.type == 5) {
								Coin co = (Coin) e;
								if (c.getFixtureB().getBody().equals(co.groundBody) || c.getFixtureA().getBody().equals(co.groundBody)) {

									
									partEffects.setPosition(co.position.x, co.position.y);
									partEffects.start();
									chunky.addCoinToBeRemoved(co);
									score++;
									if(score > bestScore)
										bestScore = score;
									MyAudio.coinSound.play();
									coinScoreLabel.setText("  Score: " + score  + " ~ Best: " + bestScore);
								}
							}
						}
					}
				}
			}
		}

		partEffects.update(delta);

	
		if(!player.isAlive() && !player.isRemoved()) {
			player.removed();
			for(int pi = 0; pi < 200; pi++){
				sparks.add(new Particle(this, player.body.getPosition().x+.3f-r.nextFloat()*.75f, player.body.getPosition().y-.1f+r.nextFloat()*.75f, r.nextFloat()*360));
			}
			world.destroyBody(player.body);
			//camera.setToOrtho(false, camera.viewportWidth, viewportHeight);
			zoomOut  = true;

			MyAudio.helicopterProp.stop();
		}
		if(zoomOut){
			
			w+=2;
			h+=2;
			
			if(w > Gdx.graphics.getWidth()*1.25f){
				zoomOut = false;
				
			}
		}

		// if (ticks % 60 == 0) {
		fpsWorld.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + "      WorldObjects: " + world.getBodyCount() + "  x: "+player.position.x + " cs " + chunkList.size());
		// System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond() +
		// "\tWorldObjects: " + world.getBodyCount());
		// }
		if(player.position.x > chunkCounter*Chunk.chunkWidth*32-50*32){
			
			chunkList.add(new Chunk(this, chunkCounter++));
			chunkList.get(chunkList.size()-1).reset();
		}
		for(Particle spa: sparks){
			spa.update(delta);
		}
		
		
		for(Chunk chunky: chunkList){
			chunky.update(delta);
		}
		if(chunkList.size() > 3){
			chunkList.remove(0).cleanUp();
			//chunkList.pop().cleanUp();
			//chunkList.remove(0);
		}
		tm.update(delta);
		
		
		junkyBlack.update(delta);
		
		tick++;
	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		partEffects.draw(batch);
	}

	//private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public Vector3 tmpCam = new Vector3(0,0,0);
	private TweenCallback resetCallback;

	float timeToReset = 1;
	@Override
	public void render(float delta) {
		if(resetting)
		{
			//System.out.println(timeToReset);
			timeToReset -= delta;
			
			if(timeToReset < 0){
				resetting = false;
				resetGame();
				timeToReset = 1;
			}
		}
		
		
		logic(delta);

		float lerp = .3f;
		camera.position.x += Math.round((player.sprite.getX()+250 - camera.position.x) * lerp);
		camera.position.y += Math.round((player.sprite.getY() - camera.position.y) * lerp);

		camera.position.y = MathUtils.clamp(camera.position.y, 340, 1568);
		if (camera.position.x < 620)
			camera.position.x = 620;
		camera.update();

		tmpCam = camera.position.cpy();

		
		batch.setProjectionMatrix(camera.combined);
		//tmpCam = camera.position.cpy();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// ///////////////////////////////////////////
		/*//blurTargetA.begin();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setShader(null);

		// resizeBatch
		camera.setToOrtho(false, FBO_SIZE, FBO_SIZE);
		batch.setProjectionMatrix(camera.combined);

		// batch.setProjectionMatrix(camera.combined);

		batch.begin();

		
		batch.flush();
		blurTargetA.end();
		batch.setShader(blurShader);
		blurShader.setUniformf("dir", 2f, 0f);
		blurTargetB.begin();

		// we want to render FBO target A into target B
		fboRegion.setTexture(blurTargetA.getColorBufferTexture());

		// draw the scene to target B with a horizontal blur effect
		batch.draw(fboRegion, 0, 0);

		// flush the batch before ending the FBO
		batch.flush();

		// finish rendering target B
		blurTargetB.end();
		// update our projection matrix with the screen size
		

		batch.setProjectionMatrix(camera.combined);

		// update the blur only along Y-axis
		blurShader.setUniformf("dir", 0f, 2f);
		fboRegion.setTexture(blurTargetB.getColorBufferTexture());
		batch.draw(fboRegion, 0, 0);

		// reset to default shader without blurs
		batch.setShader(null);

		batch.end();*/

		// camera.project(TempCamera); = TempCamera.cpy();
		camera.setToOrtho(false, w, h);
		camera.position.x = tmpCam.x;
		camera.position.y = tmpCam.y;
		camera.position.z = tmpCam.z;
		camera.update();
		// Gdx.gl.glClearColor(0, 0, 0, 1);
		// Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		bgScroll.render(batch);
		partEffects.draw(batch);

		for(Particle spa: sparks){
			spa.render(batch);
		}

		player.render(batch);

		for(Chunk chunky: chunkList){
			chunky.render(batch);
		}
		
		/*
		 * for (int i = 0; i < chunkWidth; i++) { for (int j = 0; j <
		 * chunkHeight; j++) { blocks2[i][j].render(batch); } }
		 */
	
		batch.end();

		// Matrix4 debugCam = camera.combined.cpy();
		// debugRenderer.render(world, debugCam.scl(BOX_TO_WORLD));

		stage.act();
		stage.draw();
		
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		junkyBlack.render(batch);
		batch.end();
		//System.out.println();
		world.step(1 / 60f, 5, 2);
	}


	private void GUIBuilder() {
		BitmapFont font = new BitmapFont(Gdx.files.internal("data/font2.fnt"), Gdx.files.internal("data/font2_0.png"), false);
		LabelStyle ls = new LabelStyle(font, Color.WHITE);
		coinScoreLabel = new Label("  Score", ls);
		coinScoreLabel.setPosition(5, 50);
		//l.setWidth(Gdx.graphics.getWidth());
		coinScoreLabel.setAlignment(Align.left);

		// l.setColor(Color.GREEN);
		coinScoreLabel.setFontScale(3);
		stage.addActor(coinScoreLabel);
		
		distanceScoreLabel = new Label("Distance  ", ls);
		distanceScoreLabel.setPosition(5, 50);
		distanceScoreLabel.setWidth(Gdx.graphics.getWidth());
		distanceScoreLabel.setAlignment(Align.right);

		// l.setColor(Color.GREEN);
		distanceScoreLabel.setFontScale(3);
		stage.addActor(distanceScoreLabel);
		
		
		
		
		

		gameOverLabel = new Label("Good luck!", ls);
		gameOverLabel.setPosition(0, Gdx.graphics.getHeight()/2);
		gameOverLabel.setWidth(Gdx.graphics.getWidth());
		gameOverLabel.setAlignment(Align.center);
		gameOverLabel.setFontScale(4);
		stage.addActor(gameOverLabel);

		fpsWorld = new Label("FPS WORLD", ls);
		fpsWorld.setPosition(10, 680);

		fpsWorld.setFontScale(1);
		//stage.addActor(fpsWorld);

		TextButtonStyle tbs = new TextButtonStyle();
		tbs.up = Art.buttonSkin.newDrawable("ninebutton");
		tbs.down = Art.buttonSkin.newDrawable("ninebutton");
		BitmapFont otherFont = new BitmapFont(Gdx.files.internal("data/font2.fnt"), Gdx.files.internal("data/font2_0.png"), false);
		tbs.font = otherFont;
		tbs.font.scale(3f);
		resetButton = new TextButton("Play Again?", tbs);
		// resetButton.scale(5f);

		resetButton.addListener(new ClickListener() {
			

			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				transitionDownScreen();
				resetting = true;
			}

		});
		resetButton.setSize(350, 120);
		resetButton.setPosition(Gdx.graphics.getWidth() / 2 - resetButton.getWidth() / 2, Gdx.graphics.getHeight() - 1.5f*resetButton.getHeight());
		// resetButton.setPosition(50, 50);
		resetButton.setVisible(false);
		stage.addActor(resetButton);
		Gdx.input.setInputProcessor(stage);
	}
private boolean resetting = false;
	public void resetGame() {
		
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		if(w < h){
			h = Gdx.graphics.getWidth();
			w = Gdx.graphics.getHeight();
		}
		sparks.clear();
		zoomOut = false;
		world = new World(new Vector2(0, -10), true);
		//Contacter listener = new Contacter(this);
		//world.setContactListener(listener);
		hitCounter = 0;
		
		//chunk.ticks = 0;
		score = 0;
		gameOverLabel.setText("");
		
		
		
		resetButton.setVisible(false);
		batch = new SpriteBatch();
		player = new Player(this);
		
		chunkList.clear();
		chunkCounter = 0;
		Chunk.chunkDiff = 0;
		Chunk.currentWidth = 25;
		chunkList.add(new Chunk(this,chunkCounter++));
		for(Chunk chunky: chunkList){
			chunky.reset();
			
		}
		//
		coinScoreLabel.setText("  Score: " + score  + " ~ Best: " + bestScore);
		distanceScoreLabel.setText("Distance  ");
		MyAudio.helicopterProp.loop();
		transitionUpScreen();
		player.body.setTransform(2, 20 * 32 * WORLD_TO_BOX, 0);
		camera = new OrthographicCamera(w, h);
		tmpCam = camera.position.cpy();
		//System.out.println(tmpCam.x);
		bgScroll.init();
		
	}
	protected void transitionDownScreen() {
		Tween.to(junkyBlack, EntityAccessor.POSITION_XY, 1.0f).target(0,0).ease(Cubic.INOUT).setCallback(resetCallback).start(tm);
	}
	
	protected void transitionUpScreen() {
		Tween.to(junkyBlack, EntityAccessor.POSITION_XY, 1.0f).target(0,Gdx.graphics.getHeight()).ease(Cubic.INOUT).start(tm);
	}
	
	
	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		MyAudio.helicopterProp.loop();
		resetGame();
	}

	@Override
	public void hide() {
		MyAudio.helicopterProp.stop();
	}

	@Override
	public void pause() {
		MyAudio.helicopterProp.stop();
	}

	@Override
	public void resume() {
		MyAudio.helicopterProp.loop();
	}

	@Override
	public void dispose() {
		batch.dispose();
		player.dispose();
	}

}

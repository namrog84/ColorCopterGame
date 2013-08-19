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
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;

/* Todo
 * done				Background blur?
 * infinite level
 * main menu?
 * partial:   color changer maybe?
 * explosion death
 * proper reset (upon death?)
 * 
 */


public class MainGame implements Screen {

	static final float WORLD_TO_BOX = 0.01f;
	static final float BOX_TO_WORLD = 100f;

	public ArrayList<Entity> blockList = new ArrayList<Entity>();
	public Array<Coin> coins = new Array<Coin>();

	public World world = new World(new Vector2(0, -10), true);
	public OrthographicCamera camera;
	public ColorCopterGame game;
	public Player player;

	public Label gameOverLabel;
	public Stage stage = new Stage();
	public TextureAtlas ta;

	private BackgroundScroller bgScroll;
	private Random r = new Random();
	private SpriteBatch batch;
	private int ticks = 0;
	private ParticleEffect partEffects = new ParticleEffect();

	private int[] roll = { -2, -1, 1, 2 };

	// private boolean gameFinished = false;

	int hitCounter = 0;
	private int currentWidth = 25;
	// private int offy = 50;
	// private int bgx = 0;
	// private int bgx2 = 640;
	private int score = 0;
	private Label l;
	private Label fpsWorld;

	// private int length = 450;
	private int roughness = 25;
	private int windyness = 40;

	private int chunkWidth = 50;
	private int chunkHeight = 60;
	
	private Entity[][] blocks = new Entity[chunkWidth][chunkHeight];

	// int StartX = 8;
	// int StartY = 20;

	// private boolean[][] blockBG = new boolean[20][50];

	
	ShaderProgram blurShader;
	FrameBuffer blurTargetA, blurTargetB;
	TextureRegion fboRegion;
	public static final int FBO_SIZE = 2048; 
	public static final float MAX_BLUR = 2f;
	
	final String VERT =  
			"attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
			"attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
			"attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			
			"uniform mat4 u_projTrans;\n" + 
			" \n" + 
			"varying vec4 vColor;\n" +
			"varying vec2 vTexCoord;\n" +
			
			"void main() {\n" +  
			"	vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
			"	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			"	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
			"}";
	
	final String FRAGCHEAP = 
			//GL ES specific stuff
			  "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" + //
			"varying LOWP vec4 vColor;\n" +
			"varying vec2 vTexCoord;\n" + 
			"uniform sampler2D u_texture;\n" +	
			"uniform float bias;\n" +
			"void main() {\n" +  
			"	vec4 texColor = texture2D(u_texture, vTexCoord, bias);\n" +
			"	\n" + 
			"	gl_FragColor = texColor * vColor;\n" + 
			"}";
	
	final String FRAG =
			"#ifdef GL_ES\n" + 
			"#define LOWP lowp\n" + 
			"precision mediump float;\n" + 
			"#else\n" + 
			"#define LOWP \n" + 
			"#endif\n" + 
			"varying LOWP vec4 vColor;\n" + 
			"varying vec2 vTexCoord;\n" + 
			"\n" + 
			"uniform sampler2D u_texture;\n" + 
			"uniform float resolution;\n" + 
			"uniform float radius;\n" + 
			"uniform vec2 dir;\n" + 
			"\n" + 
			"void main() {\n" + 
			"	vec4 sum = vec4(0.0);\n" + 
			"	vec2 tc = vTexCoord;\n" + 
			"	float blur = radius/resolution; \n" + 
			"    \n" + 
			"    float hstep = dir.x;\n" + 
			"    float vstep = dir.y;\n" + 
			"    \n" + 
			"	sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.05;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.09;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.12;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.15;\n" + 
			"	\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.16;\n" + 
			"	\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.15;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.12;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.09;\n" + 
			"	sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.05;\n" + 
			"\n" + 
			"	gl_FragColor = vColor * vec4(sum.rgb, 1.0);\n" + 
			"}";
	
	private float offG;
	private float offB;
	public MainGame(ColorCopterGame g) {
		offG = 2*r.nextFloat()-1f;
		offB = 2*r.nextFloat()-1f;
		
		ShaderProgram.pedantic = false;
		blurShader = new ShaderProgram(VERT, FRAG);
		if (!blurShader.isCompiled()) {
			System.err.println(blurShader.getLog());
			System.exit(0);
		}
		if (blurShader.getLog().length()!=0)
			System.out.println(blurShader.getLog());
		
		//setup uniforms for our shader
		blurShader.begin();
		blurShader.setUniformf("dir", 0f, 0f);
		blurShader.setUniformf("resolution", FBO_SIZE);
		blurShader.setUniformf("radius", 1f);
		blurShader.end();
		
		blurTargetA = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
		blurTargetB = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
		fboRegion = new TextureRegion(blurTargetA.getColorBufferTexture());
		fboRegion.flip(false, true);
		
		
		
		
		game = g;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(w, h);
		bgScroll = new BackgroundScroller(this);
		partEffects.load(Gdx.files.internal("data/coinBurst"), Gdx.files.internal("effects"));

		GUIBuilder();

		batch = new SpriteBatch();
		player = new Player(this);

		buildLevel(blocks, 8, 20);

		// clear out starting area
		for (int i = 2; i < 10; i++) {
			for (int j = 20; j < 20 + 10; j++) {
				if (blocks[i][j].type == 3) {
					world.destroyBody(((Block) blocks[i][j]).groundBody);
					blocks[i][j] = new EmptyBlock();
				}
			}
		}

		player.body.setTransform(2, 20 * 32 * WORLD_TO_BOX, 0);

		bgScroll.init();
	}

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

		if (Gdx.input.isKeyPressed(Keys.M) || Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			game.setScreen(game.menuscreen);
		}

		player.update(delta);
		for (Coin c : coins) {
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
								if (player.timeHit <= 0) {
									hitCounter++;
									gameOverLabel.setText("You Hit the Wall! " + hitCounter);
								}
							}
						}
					}
				for (Entity e : coins) {
					if (e.type == 5) {
						Coin co = (Coin) e;
						if (c.getFixtureB().getBody().equals(co.groundBody) || c.getFixtureA().getBody().equals(co.groundBody)) {
							System.out.println("beep");
							MyAudio.coinSound.play();
							partEffects.setPosition(co.position.x, co.position.y);
							partEffects.start();
							removingCoins.add(co);
							score++;
							l.setText("" + score);

						}
					}
				}
			}
		}

		partEffects.update(delta);

		for (Coin rc : removingCoins) {
			world.destroyBody(rc.groundBody);
		}
		coins.removeAll(removingCoins, true);

		//if (ticks % 60 == 0) {
			fpsWorld.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + "      WorldObjects: " + world.getBodyCount());
			//System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond() + "\tWorldObjects: " + world.getBodyCount());
		//}
		ticks++;

	}

	public void draw(SpriteBatch batch, float parentAlpha) {
		partEffects.draw(batch);
	}

	// private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public Vector3 tmpCam;
	
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
		
		
		tmpCam = camera.position.cpy();
		
		
		/////////////////////////////////////////////
		blurTargetA.begin();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		batch.setShader(null);
		
		//resizeBatch
		camera.setToOrtho(false, FBO_SIZE, FBO_SIZE);
		batch.setProjectionMatrix(camera.combined);
		
		
		
		//batch.setProjectionMatrix(camera.combined);

		
		
		batch.begin();
		
		bgScroll.render(batch);
		batch.flush();
		blurTargetA.end();
		batch.setShader(blurShader);
		blurShader.setUniformf("dir", 1.5f, 0f);
		blurTargetB.begin();
		
		
		//we want to render FBO target A into target B
		fboRegion.setTexture(blurTargetA.getColorBufferTexture());
		
		//draw the scene to target B with a horizontal blur effect
		batch.draw(fboRegion, 0, 0);
		
		//flush the batch before ending the FBO
		batch.flush();
		
		//finish rendering target B
		blurTargetB.end();
		//update our projection matrix with the screen size
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		batch.setProjectionMatrix(camera.combined);
		
		//update the blur only along Y-axis
		blurShader.setUniformf("dir", 0f, 1.5f);
		fboRegion.setTexture(blurTargetB.getColorBufferTexture());
		batch.draw(fboRegion, 0, 0);
		
		//reset to default shader without blurs 
		batch.setShader(null);
			
		batch.end();
		
		
		
		//camera.project(TempCamera); = TempCamera.cpy();
		camera.position.x = tmpCam.x;
		camera.position.y = tmpCam.y;
		camera.position.z = tmpCam.z;
		camera.update();
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		//Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
	
		
		
		batch.begin();
		
		
		partEffects.draw(batch);

		for (Coin c : coins) {
			c.render(batch);
		}

		player.render(batch);
		
		
		float Rfluc = (float) (Math.cos(player.position.x/100) / 10f);
		float Gfluc = (float) (Math.cos((player.position.x*offG)/100) / 10f);
		float Bfluc = (float) (Math.cos((player.position.x*offB)/100) / 10f);
				
				
		batch.setColor( .9f + Rfluc, .9f+ Gfluc, .9f+ Bfluc, 1f);
		for (int i = 0; i < chunkWidth; i++) {
			for (int j = 0; j < chunkHeight; j++) {
				
				blocks[i][j].render(batch);
			}
		}
		batch.setColor(Color.WHITE);
		
		/*
		 * for (int i = 0; i < chunkWidth; i++) { for (int j = 0; j <
		 * chunkHeight; j++) { blocks2[i][j].render(batch); } }
		 */

		batch.end();

		// Matrix4 debugCam = camera.combined.cpy();
		// debugRenderer.render(world, debugCam.scl(BOX_TO_WORLD));

		stage.act();
		stage.draw();

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
					world.destroyBody(((Block) bb[sX + i][sY + j]).groundBody);
				}
				bb[sX + i][sY + j] = new EmptyBlock();
			}

			if (r.nextInt(100) < 20) {
				coins.add(new Coin(this, (sX + i) * 32, (sY * 32) + (currentWidth * 16)));
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

	private void GUIBuilder() {
		BitmapFont font = new BitmapFont(Gdx.files.internal("data/font2.fnt"), Gdx.files.internal("data/font2_0.png"), false);
		LabelStyle ls = new LabelStyle(font, Color.WHITE);
		l = new Label("Score", ls);
		l.setPosition(0, 50);
		l.setWidth(Gdx.graphics.getWidth());
		l.setAlignment(Align.center);

		// l.setColor(Color.GREEN);
		l.setFontScale(6);
		stage.addActor(l);

		gameOverLabel = new Label("Good luck!", ls);
		gameOverLabel.setPosition(0, 450);
		gameOverLabel.setWidth(Gdx.graphics.getWidth());
		gameOverLabel.setAlignment(Align.center);
		gameOverLabel.setFontScale(5);
		stage.addActor(gameOverLabel);
		
		
		
		
		
		
		fpsWorld = new Label("FPS WORLD", ls);
		fpsWorld.setPosition(10, 680);
		//fpsWorld.setWidth(Gdx.graphics.getWidth());
		//fpsWorld.setAlignment(Align.center);

		fpsWorld.setFontScale(2);
		stage.addActor(fpsWorld);
		
		
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

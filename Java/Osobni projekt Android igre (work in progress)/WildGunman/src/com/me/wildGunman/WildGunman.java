package com.me.wildGunman;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

//TODO repackat img (Billy img je zastario - malkice visi u zraku, uocljivo na openingu 2)

public class WildGunman implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture saloonImage;
	private Texture saloonWall;
	private Texture saloonBrownParts;
	private Texture saloonFence;
	private Texture badguyJoe;
	private Texture badguyMel;
	private Texture badguyBilly;
	private TextureRegion region;
	private TextureRegion region2;
	private TextureRegion region3;
	private Texture score;
	private Texture bullet;
	private int cur_numberof_bullets;
//	private Texture badguys;
	private int floor1y = 255;
	private int floor1doorx = 430;
	private int floor1window1x = 115;
	private int floor1window2x = 750;
	private int floor2y = 640;
	private int floor2window1x = 145;
	private int floor2window2x = 720;
	private int windowlength = 160;
	private int windowheight = 330;
	boolean shotJoe = false;
	private float originalWidth = 1024;
	private float originalHeight = 1024;	
	private float widthRatio;
	private float heightRatio;
	private Texture saloonclean;
	static TextureAtlas atlas;
	AtlasRegion regionatl;
	BadGuy badguy;
	BadGuy badguy2;
	BadGuy badguy3;
	BadGuy badguy4;
	BadGuy badguy5;
	Texture redgirl;
	Texture redgirl2;
	ArrayList<BadGuy> badguys = new ArrayList<BadGuy>(5);
	static Stack<Integer> freeOpenings;
	long lastSpawnTime;
	boolean gameOver = false;
	
	
	public static TextureAtlas getAtlas(){
		return atlas;
	}
	
	public static Stack<Integer> getFreeOpenings(){
		return freeOpenings;
	}
	

	@Override
	public void create() {		
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		widthRatio = w / originalWidth;
		heightRatio = h / originalHeight;
		
		Game.setWidthRatio(widthRatio);
		Game.setHeightRatio(heightRatio);			
		
//		System.out.println("width: " + w + "\nheight: " + h);
//		System.out.println("widthratio: " + widthratio + "\nheightratio: " + heightratio);
		
//		
//		camera = new OrthographicCamera(1, h/w);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, originalWidth, originalHeight);

		batch = new SpriteBatch();
		
//		saloonImage = new Texture(Gdx.files.internal("Saloon2.png"));
//		saloonImage.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
		saloonWall = new Texture(Gdx.files.internal("Saloonwalls.png"));
		saloonBrownParts = new Texture(Gdx.files.internal("Saloonbrown.png"));
		saloonFence = new Texture(Gdx.files.internal("Saloonfence.png"));
//		badguyJoe = new Texture(Gdx.files.internal("badguyJoeHDpoweroftwo.png"));
//		badguyMel = new Texture(Gdx.files.internal("badguyMelHDpoweroftwo.png"));
//		badguyBilly = new Texture(Gdx.files.internal("billy.png"));
//		redgirl = new Texture(Gdx.files.internal("red.png"));
//		redgirl2 = new Texture(Gdx.files.internal("red2.png"));
		
		
		atlas = new TextureAtlas(Gdx.files.internal("packedimages/testingbang.pack"));
		
		freeOpenings = new Stack<Integer>();
		

		for (int i=1; i<=5; i++)
			freeOpenings.push(i);
		
				
//		freeOpenings.remove((Object) 1);
//		freeOpenings.add(1);
//		
//		for (int i=0; i < freeOpenings.size(); i++)
//			System.out.println("ovaj je free: " + freeOpenings.get(i));
		
		
		lastSpawnTime = TimeUtils.nanoTime(); 
//		for (int i=0; i<5; i++){
//			Collections.shuffle(freeOpenings);			
//			badguys.add(Game.spawnBaddie()); 
//		}
		
//		regionatl = atlas.findRegion("Billy");
//		
//		badguy = new Billy(1);
//		badguy2 = new Billy(2);
//		badguy3 = new Billy(3);
//		badguy4 = new Billy(4);
//		badguy5 = new Billy(5);
		
//		regionatl = atlas.findRegion("Esteban");
//		
//		badguy = new Esteban(1);
//		badguy2 = new Esteban(2);
//		badguy3 = new Esteban(3);
//		badguy4 = new Esteban(4);
//		badguy5 = new Esteban(5);
		
//		regionatl = atlas.findRegion("Joe");
//		
//		badguy = new Joe(1);
//		badguy2 = new Joe(2);
//		badguy3 = new Joe(3);
//		badguy4 = new Joe(4);
//		badguy5 = new Joe(5);
		
//		regionatl = atlas.findRegion("Mel");
//		
//		badguy = new Mel(1);
//		badguy2 = new Mel(2);
//		badguy3 = new Mel(3);
//		badguy4 = new Mel(4);
//		badguy5 = new Mel(5);
				
//		badguy = new Sanchez(1);
//		badguy2 = new Sanchez(2);
//		badguy3 = new Sanchez(3);
//		badguy4 = new Sanchez(4);
//		badguy5 = new Sanchez(5);
			

//		badguy = new Joe(1);
//
//		badguy5 = new Billy(5);

		
//		region = new TextureRegion(badguyJoe, 0, 0, 160, 329);
//		region = new TextureRegion(badguyBilly, 0, 0, 188, 304);
//		region = new TextureRegion(redgirl, 0, 0, 183, 295);
		
		saloonclean = new Texture(Gdx.files.internal("Saloonvclean.png"));
		region2 = new TextureRegion(saloonclean, 0, 840, 1024, 184);
//		region3 = new TextureRegion(redgirl2, 0, 0, 144, 300);
		
//		region3 = new TextureRegion(badguyMel, 0, 0, 170, 331);
//		
//		region = new TextureRegion(saloonImage, 0, 0, 1024, 840);
//		region2 = new TextureRegion(saloonImage, 0, 840, 1024, 184);
//		
//		badguys = new Texture (Gdx.files.internal("badguy.png"));
//		region3 = new TextureRegion(badguys, 0, 0, 110, 195);
//		
//		

		bullet = new Texture(Gdx.files.internal("bulletHD.png"));
				
		cur_numberof_bullets = 15;
	}

	@Override
	public void dispose() {
		batch.dispose();
		atlas.dispose();
//		saloonImage.dispose();
	}

	public void bang() {
		gameOver = true;
	}
	
	@Override
	public void render() {	
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		
		
		// input
		if (!gameOver && Gdx.input.justTouched()) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();
			
			System.out.println("x:" + x + "\ny:" + y);
			
			if (cur_numberof_bullets > 0) {		
				
				cur_numberof_bullets--;
				
				Iterator<BadGuy> iter = badguys.iterator();
				while(iter.hasNext()) {
					BadGuy badguy = iter.next();
					
					if (badguy.checkIfHit(x, y) == true) {
						iter.remove();
						freeOpenings.add(badguy.getOpening());
						break;
					}
				}
				
//				if ( x >= floor1doorx*widthratio && x <= (floor1doorx + 160)*widthratio  &&
//						y >= (1024-255-330)*heightratio && y <= (1024-255)*heightratio ) 
//					shotJoe = true;
			}
		}
		
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();	
		batch.setColor(1,1,1,1);
//		batch.setBlendFunction(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		batch.enableBlending();
		
		// draw saloon
//		batch.draw(saloonWall, 0, 0);		
//		batch.draw(saloonFence, 0, 0);				
//		batch.draw(saloonBrownParts, 0, 0);
	
		
		// draw bad guy
//		if (!shotJoe) 
//			batch.draw(region, floor1doorx, floor1y);

		
//		batch.draw(region3, floor1doorx, floor1y);		
//		batch.draw(region, floor1window1x, floor1y);
//		batch.draw(region, floor1window2x, floor1y);
//		batch.draw(region3, floor2window1x, floor2y);
//		batch.draw(region3, floor2window2x, floor2y);
		
		// draw bad guys
		Iterator<BadGuy> iter = badguys.iterator();
		while (iter.hasNext()) {
			BadGuy badguy = iter.next();
			if (badguy.isHit() == false)
				batch.draw(badguy.getRegion(), badguy.getX(), badguy.getY());			
		}
		

		
//		batch.draw(badguy.getRegion(), badguy.getX(), badguy.getY());
//		batch.draw(badguy2.getRegion(), badguy2.getX(), badguy2.getY());
//		batch.draw(badguy3.getRegion(), badguy3.getX(), badguy3.getY());
//		batch.draw(badguy4.getRegion(), badguy4.getX(), badguy4.getY());
//		batch.draw(badguy5.getRegion(), badguy5.getX(), badguy5.getY());
		
//		batch.draw(saloonBrownParts, 0, 0);
		batch.draw(region2, 0, 0);

		// draw saloon
		batch.draw(saloonWall, 0, 0);		
		batch.draw(saloonFence, 0, 0);				
//		batch.draw(saloonBrownParts, 0, 0);
	
		
		// nacrtaj 15 metaka
		for (int i=0; i<cur_numberof_bullets; i++)
			batch.draw(bullet, 50 + i*32, 114);
		
		//tinting color
//		batch.setColor(0.6f,0,0,1);
		
		// pokusaj spawnat bad guya svake sekunde		
		if (!gameOver && (TimeUtils.nanoTime() - lastSpawnTime > TimeUtils.millisToNanos(2000))) {
			Collections.shuffle(freeOpenings);
			BadGuy newBaddie = Game.spawnBaddie(TimeUtils.nanoTime());
			if (newBaddie != null)
				badguys.add(newBaddie);
			lastSpawnTime = TimeUtils.nanoTime();
		}
		
		// check to see if a bad guy fired (if too much time passed since he spawned)
		if (!gameOver) {
			iter = badguys.iterator();
			while (iter.hasNext()) {
				badguy = iter.next();
				// 	if more than 2 seconds passed since he spawned
				if (TimeUtils.nanoTime() - badguy.getSpawnTime() > TimeUtils.millisToNanos(1500)) {
					bang();
					badguy.bang();
					System.out.println("Bang!");
				}
			}
		}
		
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		widthRatio = width / originalWidth;
		heightRatio = height / originalHeight;
		
		Game.setWidthRatio(widthRatio);
		Game.setHeightRatio(heightRatio);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

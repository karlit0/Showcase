package com.me.wildGunman;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public abstract class BadGuy {

	/* prebacit ove varijable u Game */
	
	private int floor1doorx = 430;
	private int floor1window1x = 115;
	private int floor1window2x = 750;
	private int floor1y = 255;

	private int floor2window1x = 145;
	private int floor2window2x = 720;
	private int floor2y = 640;
	
	private float widthRatio;
	private float heightRatio;
	
	private AtlasRegion region;
	
	private int width;
	private int height;
	
	private int opening;
	private boolean shot = false;
	
	private int Xpos;
	private int Ypos;
	private int Xoffset;
	private int Yoffset;
	private int X;
	private int Y;
	private int openingBottom;	
	private long spawnTime;
	
//	public abstract int getX();
//	public abstract int getY();	
			
	public BadGuy(AtlasRegion _region, int _opening, int _Xoffset, int _Yoffset) {
		region = _region;
		width = region.getRegionWidth();
		height = region.getRegionHeight();
		
		opening = _opening;
		
		if (opening == 2)
			openingBottom = 767;
		else if (opening == 1 || opening == 3)
			openingBottom = 720; 
		else
			openingBottom = 330;
		
		System.out.println("Filling opening: " + opening);
		
		switch (opening) {
		
			case 1:
				Xpos = floor1window1x;
				Ypos = floor1y;
				break;
			case 2:
				Xpos = floor1doorx;
				Ypos = floor1y;
				break;
			case 3:
				Xpos = floor1window2x;
				Ypos = floor1y;
				break;
			case 4:
				Xpos = floor2window1x;
				Ypos = floor2y;
				break;
			case 5:
				Xpos = floor2window2x;
				Ypos = floor2y;
				break;
		}
					
		Xoffset = _Xoffset;
		Yoffset = _Yoffset;
		
		X = Xpos + Xoffset;
		if (opening == 4 || opening == 5) /* gornji kat */
			Y = Ypos + Yoffset;
		else
			Y = Ypos;
		
	}
	
	public int getX(){
		return X;
	}
	public int getY(){
		return Y;
	}
	
	public int getOpening(){
		return opening;
	}
	public void setSpawnTime(long spawnTime) {
		this.spawnTime = spawnTime;
	}
	public long getSpawnTime() {
		return spawnTime;
	}
		
//	int getXpos(){
//		return Xpos;
//	}
//	
//	int getYpos(){
//		return Ypos;
//	}
	
	public AtlasRegion getRegion(){
		return region;
	}

	public void hit(){
		shot = true;
	}
	
	public boolean isHit(){
		return shot;
	}
	public void setRegion(AtlasRegion region) {
		this.region = region;
	}
	public abstract void bang();	
	
	/* ovo zasad provjerava jesi pogodio vrata (kao original), ako bih htio da provjeri jesi bas pogodio lika (cetverokut),
	 * trebao bi ubaciti u podklasama super naredbe koje vrate povratnu informaciju o offsetu(x,y) - razmisliti o ovome
	 * ako bi to primjenio u ovakvom obliku onda mozes hittat ljude udarajuci kroz zid na prozor openinzima  */
		
//	int getXpos(int opening){
//		switch (opening) {
//		
//		case 1:
//			return floor1window1x;					
//		case 2:
//			return floor1doorx;
//		case 3:
//			return floor1window2x;
//		case 4:
//			return floor2window1x;
//		case 5:
//			return floor2window2x;
//
//			/* izbacit iznimku mb*/
//		default:
//			return 0;	
//		}
//	};
//	
//	int getYpos(int opening){
//		if (opening == 1 || opening == 2 || opening == 3)
//			return floor1y;
//		else if (opening == 4 || opening == 5)
//			return floor2y;
//		else /* mb iznimka */
//			return 0;
//	}
	
	boolean checkIfHit(int x, int y){
		
		widthRatio = Game.getWidthRatio();
		heightRatio = Game.getHeightRatio();
//		System.out.println("Height ratio: " + Game.getHeightRatio() );
//		System.out.println("Width ratio: " + Game.getWidthRatio() );
//		
//		System.out.println("Xpos: " + Xpos*widthRatio);
//		System.out.println("Ypos: " + Ypos*heightRatio);
//		
//		System.out.println("Bad Guy at: " + opening + " checking if hit");
		
		if ( x >= X*widthRatio && x <= (X + width)*widthRatio &&
				y >= (1024-Y-height)*heightRatio && y <= (openingBottom)*heightRatio ) {
			hit();
			return true;
		}
		else
			return false;
	}
}

package com.me.wildGunman;

import java.util.Random;

public class Game {

	static float widthRatio;
	static float heightRatio;
	
	public static void setWidthRatio(float w){
		widthRatio = w;
	}
	
	public static void setHeightRatio(float h){
		heightRatio = h;
	}
	
	public static float getWidthRatio(){
		return widthRatio;
	}
	
	public static float getHeightRatio(){
		return heightRatio;
	}
	
	public static BadGuy spawnBaddie(long spawnTime){
		Random r = new Random();
		int randomer = r.nextInt(5) + 1; // random broj u intervalu [1, 5] 
		BadGuy badGuy;
		int opening;
		
		if (WildGunman.getFreeOpenings().isEmpty() == true)
			return null; /* mb throw exception, ovisi o tome gdje ce se provjeravat jel 0 ili ne (hoce li "main" provjeravat) */
		
		opening = (Integer) WildGunman.getFreeOpenings().pop();
		
		
		/* mozda zamijenit ovaj randomer sa listom freeBadGuys (kao freeOpenings) gdje se dohvati koji badguy
		 * vec nije u igri (ima taman 5 badguyeva i 5 openinga pa ako se zeli, ne moraju nikad bit 2 ista badguya ziva odjednom) */
		
		switch (randomer) {
		case 1:
			badGuy = new Billy(opening);
			break;
			
		case 2:
			badGuy = new Esteban(opening);
			break;
			
		case 3:
			badGuy = new Joe(opening);
			break;
			
		case 4:
			badGuy = new Mel(opening);
			break;
			
		case 5:
			badGuy = new Sanchez(opening);
			break;

		default:
			badGuy = null;
			break;
		}
		
		//TODO
		badGuy = new Joe(opening);
		
		badGuy.setSpawnTime(spawnTime);
		return badGuy;
	}


}

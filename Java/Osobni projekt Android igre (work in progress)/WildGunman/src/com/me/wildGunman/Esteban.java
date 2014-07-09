package com.me.wildGunman;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Esteban extends BadGuy{
			
	private static int Xoffset = 0;
	private static int Yoffset = 0; 	
	private int opening;
	
	public Esteban(int _opening /* broj koji odredjuje gdje se nalazi: 1-5, 
	 							1 - donji lijevi prozor
	 							2 - vrata
	 							3 - donji desni prozor
	 							4 - gornji lijevi prozor
	 							5 - gornji desni prozor */ ) {
		super(WildGunman.getAtlas().findRegion("Esteban"), _opening, Xoffset, Yoffset);
		opening = _opening;
	}

	public void bang() {
		
	}
//	public int getX() {
//		return super.getXpos(opening) + offsetx;
//	}
//
//	public int getY() {
//		if (opening == 4 || opening == 5)
//			return super.getYpos(opening) + offsety2;
//		else if (opening == 1 || opening == 2 || opening == 3)
//			return super.getYpos(opening);
//		else /* iznimka mb */
//			return 0;
//			
//	}

	
}

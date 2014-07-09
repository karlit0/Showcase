package com.me.wildGunman;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Billy extends BadGuy{
		
	private static int Xoffset = -15;
	private static int Yoffset = -10; 	
	private int opening;

	/* dodat u konstruktor argumente koji definiraju na kojem mjestu se spawna
	 * ovaj enemy (to se u igri radi "random" - valjda), na temelju toga dodat neku logiku
	 * koja radi offset ako treba (za x uvijek treba a za y treba ak je na gornjem katu)
	 * vjerojatno bolje umjesto reference na atlasregion prenijet referencu na cijeli textureatlas
	 * jer je lakse tak pristupit onda ostalim teksturama koje ce bit potrebne (fire/death) */
	
	public Billy(int _opening /* broj koji odredjuje gdje se nalazi: 1-5, 
	 							1 - donji lijevi prozor
	 							2 - vrata
	 							3 - donji desni prozor
	 							4 - gornji lijevi prozor
	 							5 - gornji desni prozor */ ) {
		super(WildGunman.getAtlas().findRegion("Billy"), _opening, Xoffset, Yoffset);
		opening = _opening;
	}

	public void bang() {
		
	}
//	public int getX() {
//		return super.getXpos() + offsetx;
//	}

//	public int getY() {
//		if (opening == 4 || opening == 5)
//			return super.getYpos() + offsety2;
//		else if (opening == 1 || opening == 2 || opening == 3)
//			return super.getYpos();
//		else /* iznimka mb */
//			return 0;
//			
//	}

	
}

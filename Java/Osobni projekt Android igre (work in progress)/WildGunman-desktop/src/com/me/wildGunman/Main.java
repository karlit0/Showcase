package com.me.wildGunman;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;

public class Main {
	public static void main(String[] args) {		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "WildGunman";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 768;
				
	
		System.setProperty("org.lwjgl.opengl.Display.allowSoftwareOpenGL", "true");
		new LwjglApplication(new WildGunman(), cfg);
	}
}

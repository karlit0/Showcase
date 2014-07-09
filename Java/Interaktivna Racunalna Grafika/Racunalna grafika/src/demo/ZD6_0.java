package demo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class ZD6_0 {
	static {
	GLProfile.initSingleton();
	}

	static void kocka (float w, GL2 gl2) {
		float wp = w / 2.0f ;
		// gornja stranica
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3f(-wp, -wp, wp);
		gl2.glVertex3f(wp, -wp, wp);
		gl2.glVertex3f(wp, wp, wp);
		gl2.glVertex3f(-wp, wp, wp);
		gl2.glEnd();
		// donja stranica
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3f(-wp, wp, -wp);
		gl2.glVertex3f(wp, wp, -wp);
		gl2.glVertex3f(wp, -wp, -wp);
		gl2.glVertex3f(-wp, -wp, -wp);
		gl2.glEnd();
		// desna stranica
		gl2.glBegin(GL.GL_LINE_LOOP) ;
		gl2.glVertex3f(wp, wp, -wp);
		gl2.glVertex3f(-wp, wp, -wp);
		gl2.glVertex3f(-wp, wp, wp);
		gl2.glVertex3f(wp, wp, wp);
		gl2.glEnd();
		// lijeva stranica
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3f(wp, -wp, wp);
		gl2.glVertex3f(-wp, -wp, wp);
		gl2.glVertex3f(-wp, -wp, -wp);
		gl2.glVertex3f(wp, -wp, -wp);
		gl2.glEnd();
		// prednja stranica
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3f(wp, -wp, -wp);
		gl2.glVertex3f(wp, wp, -wp);
		gl2.glVertex3f(wp, wp, wp);
		gl2.glVertex3f(wp, -wp, wp);
		gl2.glEnd();
		// straznja stranica
		gl2.glBegin(GL.GL_LINE_LOOP);
		gl2.glVertex3f(-wp, -wp, wp);
		gl2.glVertex3f(-wp, wp, wp);
		gl2.glVertex3f(-wp, wp, -wp);
		gl2.glVertex3f(-wp, -wp, -wp);
		gl2.glEnd();
		};
		
	static void renderScene(GL2 gl2) {
		gl2.glColor3f(1.0f, 0.2f, 0.2f);
		gl2.glPushMatrix();
//		gl2.glScalef(10.0f,  10.0f, 10.0f);
		kocka(2, gl2);
		gl2.glPopMatrix();
		
//		gl2.glColor3f(0.0f, 0.2f, 1.0f);
//		gl2.glPushMatrix();
//		gl2.glTranslatef(10.0f, 0.0f, 0.0f);
//		gl2.glRotatef(30.0f, 0.0f, 0.0f, 1.0f);
//		gl2.glScalef(5.0f, 5.0f, 5.0f);
//		kocka(1, gl2);
//		gl2.glPopMatrix();
	}
	
	public static void main (String [] args) {

		SwingUtilities.invokeLater(new Runnable () {
			@Override
			public void run () {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapabilities);
				
				// Reagiranje na promjenu velicine platna, na zahtjev za
				// crtanjem i slicno ...
				glcanvas.addGLEventListener (new GLEventListener() {
					@Override
					public void reshape(GLAutoDrawable glautodrawable, int x,
						int y, int width, int height) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();
						
//						gl2.glFrustumf(-1.2f, 1.2f, -1.2f, 1.2f, 1.5f, 30.0f);
						gl2.glFrustumf(-0.5f, 0.5f, -0.5f, 0.5f, 1.0f, 100.0f);
//						gl2.glOrthof(-10f, 15f, -10f, 10f, 1.5f, 30.0f);
															
//						gl2.glViewport(0, 0, width/2, height/2); // slika u donjoj lijevoj cetvrtini prozora
//						gl2.glViewport(0, height/2, width/2, height/2); // slika u gornje lijevoj cetvrtini prozora
//						gl2.glViewport(width/2, 0, width/2, height/2); // slika u donje desnoj cetvrtini prozora
//						gl2.glViewport(width/2, height/2, width/2, height/2); // slika u gornje desnoj cetvrtini prozora
//						gl2.glViewport(width/4, height/4, width/2, height/2); // slika u centru
						
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glViewport(0, 0, width, height); // slika preko cijelog prozora
					}

					@Override
					public void init(GLAutoDrawable glautodrawable) {
					}
	
					@Override
					public void dispose(GLAutoDrawable glautodrawable) {
					}
					
					@Override
					public void display(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						GLU glu = GLU.createGLU(gl2);
						
						gl2.glClearColor(0, 1, 0, 0);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();
						
//						gl2.glMatrixMode(GL2.GL_MODELVIEW);
//						gl2.glLoadIdentity();
						
//						gl2.glTranslatef(0.0f, 0.0f, -20.0f);
//						glu.gluLookAt(0.0f, 0.0f, 20.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
						glu.gluLookAt(3.0f, 4.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
						renderScene(gl2);
						
//						gl2.glColor3f(1.0f, 0.2f, 0.2f);
//						gl2.glScalef(10.0f, 10.0f, 10.0f);
					
						// Crtam vodoravnu liniju na blizoj ravnini, pri dnu...
//						gl2.glColor3f(1, 0, 0);
//						gl2.glBegin(GL.GL_LINE_STRIP);
//						gl2.glVertex3f(-0.9f, -0.9f, -0.9f);
//						gl2.glVertex3f(0.9f, -0.9f, -0.9f);
//						gl2.glEnd();
//						
//						// Crtam vodoravnu liniju koja se udaljava, iznad prethodne...
//						gl2.glColor3f(1, 0, 0);
//						gl2.glBegin(GL.GL_LINE_STRIP);
//						gl2.glVertex3f(-0.9f, -0.7f, -0.9f);
//						gl2.glVertex3f(0.9f, -0.7f, 3.1f);;
//						gl2.glEnd();
					}
				});

				final JFrame jframe = new JFrame (
					"Primjer 6");
				jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jframe.addWindowListener(new WindowAdapter() {
					public void windowClosing (WindowEvent windowevent) {
						jframe.dispose();
					System.exit(0) ;
					}
				});	
				jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
				jframe.setSize(640, 480);
				jframe.setVisible(true);
				glcanvas.requestFocusInWindow();
			}
		});
	}
}
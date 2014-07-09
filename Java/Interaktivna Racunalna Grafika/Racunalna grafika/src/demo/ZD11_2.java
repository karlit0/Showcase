package demo;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

public class ZD11_2 {
		
	static int pointsNumber;
	static int limit;
	static int eta1, eta2, eta3, eta4;
	static ArrayList<Float> a = new ArrayList<Float>();
	static ArrayList<Float> b = new ArrayList<Float>();
	static ArrayList<Float> c = new ArrayList<Float>();
	static ArrayList<Float> d = new ArrayList<Float>();
	static ArrayList<Float> e = new ArrayList<Float>();
	static ArrayList<Float> f = new ArrayList<Float>();
	static ArrayList<Integer> p = new ArrayList<Integer>();
	static int lines;
	
	static {
	GLProfile.initSingleton();
	}
	
	public static void renderScene(GL2 gl2) {
		gl2.glPointSize(1);
		gl2.glColor3f(0.0f, 0.7f, 0.3f);
		gl2.glBegin(GL.GL_POINTS);
		double x0, y0;
		for (int brojac = 0; brojac < pointsNumber; brojac++) {
			// pocetna tocka:
			x0 = 0;
			y0 = 0;
			// iterativna primjena:
			for (int iter = 0; iter < limit; iter++) {
				double x, y;
				x = y = 0;
				int random = (int) (Math.random()*100);
				int temp = p.get(0);
				for (int i=0; i < lines; i++) {
					if (random < temp) {
						x = a.get(i) * x0 + b.get(i) * y0 + e.get(i);
						y = c.get(i) * x0 + d.get(i) * y0 + f.get(i);
						break;
					}
					if (i != lines-1)
						temp += p.get(i+1);
				}
				x0 = x;
				y0 = y;
			}
			gl2.glVertex2i(zaokruzi(x0 * eta1 + eta2), zaokruzi(y0 * eta3 + eta4));
		}
		gl2.glEnd();
	}
	
	public static int zaokruzi(double d) {
		if (d >= 0)
			return (int) (d + 0.5);
		else
			return (int) (d - 0.5);
	}
	
	public static void main (String[] args) {
		
//		File file = new File("paprat.txt");
//		File file = new File("trokutsierpinski.txt");
		File file = new File("tepihsierpinski.txt");
		
		BufferedReader br = null;
		String line = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String delims = "[ ]+";
		String[] tokens; 
		int count = 0;
		
		try {
			while ((line = br.readLine()) != null) {
				tokens = line.split(delims);
				if (tokens[0].equals("#"))
					continue;
				count++;
				switch (count) {
				case 1:
					pointsNumber = Integer.parseInt(tokens[0]);
					break;
				case 2:
					limit = Integer.parseInt(tokens[0]);
					break;
				case 3:
					eta1 = Integer.parseInt(tokens[0]);
					eta2 = Integer.parseInt(tokens[1]);
					break;
				case 4:
					eta3 = Integer.parseInt(tokens[0]);
					eta4 = Integer.parseInt(tokens[1]);
					break;

				default:
					a.add(Float.parseFloat(tokens[0]));
					b.add(Float.parseFloat(tokens[1]));
					c.add(Float.parseFloat(tokens[2]));
					d.add(Float.parseFloat(tokens[3]));
					e.add(Float.parseFloat(tokens[4]));
					f.add(Float.parseFloat(tokens[5]));
					p.add((int) (Float.parseFloat(tokens[6]) * 100));
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		lines = a.size();
		
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

						// coordinate system origin at lower left with width and
						// height same as the window
						GLU glu = new GLU();
						glu.gluOrtho2D(0.0f, width-1, 0.0f, height-1);

						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glLoadIdentity();

						gl2.glViewport(0, 0, width, height);
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
	
						gl2.glClearColor(1f, 1f, 1f, 0f);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();
						
						renderScene(gl2);
					}
				});

				final JFrame jframe = new JFrame (
					"Vježba 11.2 - IFS Fraktal");
				jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jframe.addWindowListener(new WindowAdapter() {
					public void windowClosing (WindowEvent windowevent) {
						jframe.dispose();
					System.exit(0) ;
					}
				});	
				jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
				jframe.setSize(600, 600);
				jframe.setVisible(true);
				glcanvas.requestFocusInWindow();
			}
			
		});			
	}
}
package demo;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

import lab01.pomlib.IVector;
import lab01.pomlib.Vector;

public class ZD9_1 {
	static {
	GLProfile.initSingleton();
	}
	
	static ObjectModel obj;
	static double angle = 18.4349488;
	static double increment = 1;
	static double r = 3.16227766;
	static GLCanvas glCanvas = null;
	static IVector lightVector;
	static boolean usingZBuffer = true;
	static boolean usingConstantShading = true;
	
	static void renderScene(GL2 gl2) {
		ArrayList<Face3D> faces = obj.getFaces();
		ArrayList<Vertex3D> vertices = obj.getVertices();		
		for (int i=0; i < faces.size(); i++) { 
			gl2.glBegin(GL2.GL_POLYGON);
			Vertex3D Vi = vertices.get(faces.get(i).getIndexes()[0] - 1); // faces pamti indexe od 1 pa nadalje
			Vertex3D Vi1 = vertices.get(faces.get(i).getIndexes()[1] - 1);
			Vertex3D Vi2 = vertices.get(faces.get(i).getIndexes()[2] - 1);
			
			if (usingConstantShading)
				gl2.glNormal3f((float) faces.get(i).getA(), (float) faces.get(i).getB(), (float) faces.get(i).getC());
			else 
				gl2.glNormal3f((float) Vi.getNormal().get(0), (float) Vi.getNormal().get(1), (float) Vi.getNormal().get(2)); 
			gl2.glVertex3f(Vi.getX(), Vi.getY(), Vi.getZ());
			
			if (usingConstantShading)
				gl2.glNormal3f((float) faces.get(i).getA(), (float) faces.get(i).getB(), (float) faces.get(i).getC());
			else
				gl2.glNormal3f((float) Vi1.getNormal().get(0), (float) Vi1.getNormal().get(1), (float) Vi1.getNormal().get(2));
			gl2.glVertex3f(Vi1.getX(), Vi1.getY(), Vi1.getZ());
			
			if (usingConstantShading)
				gl2.glNormal3f((float) faces.get(i).getA(), (float) faces.get(i).getB(), (float) faces.get(i).getC());
			else
				gl2.glNormal3f((float) Vi2.getNormal().get(0), (float) Vi2.getNormal().get(1), (float) Vi2.getNormal().get(2));
			gl2.glVertex3f(Vi2.getX(), Vi2.getY(), Vi2.getZ());
			gl2.glEnd();
		}
	}
	
	public static void main (String [] args) {

//		File file = new File("kocka.obj");
		File file = new File("teddy.obj");
//		File file = new File("tetrahedron.obj");
//		File file = new File("tsd00.obj");
		
		BufferedReader br = null;
		String line = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		obj = new ObjectModel();

		String delims = "[ ]+";
		String[] tokens; 
		
		try {
			while ((line = br.readLine()) != null) {
				tokens = line.split(delims);
				if (tokens.length < 1)
					continue;
				if (tokens[0].equals("v")) {
					if (tokens.length != 4) {
						System.out.println("Invalid line: " + line);
						continue;
					}
					float x = Float.parseFloat(tokens[1]);
					float y = Float.parseFloat(tokens[2]);
					float z = Float.parseFloat(tokens[3]);
					obj.addVertex(x, y, z);
				} else if (tokens[0].equals("f")) {
					if (tokens.length != 4) {
						System.out.println("Invalid line: " + line);
						continue;
					}
					int index0 = Integer.parseInt(tokens[1]);
					int index1 = Integer.parseInt(tokens[2]);
					int index2 = Integer.parseInt(tokens[3]);
					obj.addFace(index0, index1, index2);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		obj.normalize();
		obj.normalAverages();
		
		SwingUtilities.invokeLater(new Runnable () {
			@Override
			public void run () {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
				glcapabilities.setDepthBits(16);
				final GLCanvas glcanvas = new GLCanvas(glcapabilities);
				glCanvas = glcanvas;
								
				
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_L) {		
							angle -= increment;
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_R) {
							angle += increment;
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							angle = 18.4349488;
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_LEFT) {
							angle += increment;
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
							angle -= increment;
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_Z){							
							if (usingZBuffer)
								usingZBuffer = false;
							else
								usingZBuffer = true;
							System.out.println("Uporaba Z-spremnika: " + usingZBuffer);
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_K){
							usingConstantShading = true;
							System.out.println("Uporaba konstantnog sjenèanja");
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_G){
							usingConstantShading = false;
							System.out.println("Uporaba Gourandovog sjenèanja");
							e.consume();
							glCanvas.display();
						}
					}
				});
				
				// Reagiranje na promjenu velicine platna, na zahtjev za
				// crtanjem i slicno ...
				glcanvas.addGLEventListener (new GLEventListener() {
					@Override
					public void reshape(GLAutoDrawable glautodrawable, int x,
						int y, int width, int height) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();
						
						gl2.glFrustumf(-0.5f, 0.5f, -0.5f, 0.5f, 1.0f, 100.0f);
						
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glViewport(0, 0, width, height); // slika preko cijelog prozora
						
	
					}

					private GLU glu;
					
					@Override
					public void init(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						glu = new GLU();
						gl2.glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
						gl2.glClearDepth(1.0f);
						
						gl2.glDepthFunc(GL.GL_LEQUAL);
						gl2.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL);
						gl2.glEnable(GL.GL_CULL_FACE);
						gl2.glCullFace(GL.GL_BACK);
						gl2.glEnable(GL2.GL_LIGHTING);
						gl2.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] {0.0f, 0.0f, 0.0f, 1f}, 0);
												
						gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[] {1f, 1f, 1f, 1f}, 0);
						gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {1f, 1f, 1f, 1f}, 0);
						gl2.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[] {0.01f, 0.01f, 0.01f, 1f}, 0);
						gl2.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 96f);
						gl2.glShadeModel(GL2.GL_FLAT);
					}
	
					@Override
					public void dispose(GLAutoDrawable glautodrawable) {
					}
					
					@Override
					public void display(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						
						if (usingZBuffer)
							gl2.glEnable(GL.GL_DEPTH_TEST);
						else
							gl2.glDisable(GL.GL_DEPTH_TEST);
						if (usingConstantShading)
							gl2.glShadeModel(GL2.GL_FLAT);
						else
							gl2.glShadeModel(GL2.GL_SMOOTH);
						
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
						gl2.glLoadIdentity();
						
						double x = r*Math.cos(Math.toRadians(angle));
						double y = 4;
						double z = r*Math.sin(Math.toRadians(angle));
						
						glu.gluLookAt(x, y, z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
						lightVector = new Vector(4, 5, 3);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float [] {4f, 5f, 3f, 1f}, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float [] {0.2f, 0.2f, 0.2f, 1f}, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float [] {0.8f, 0.8f, 0f, 1f}, 0);
						gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float [] {0f, 0f, 0f, 1f}, 0);
						gl2.glEnable(GL2.GL_LIGHT0);
						renderScene(gl2);
					}
				});

				final JFrame jframe = new JFrame (
					"Zadatak 9.1");
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
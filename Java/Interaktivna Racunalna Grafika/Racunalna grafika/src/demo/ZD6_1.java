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

public class ZD6_1 {
	static {
	GLProfile.initSingleton();
	}
	
	static ObjectModel obj;
	static double angle = 18.4349488;
	static double increment = 1;
	static double r = 3.16227766;
	static GLCanvas glCanvas = null;
		
	static void renderScene(GL2 gl2) {
		gl2.glColor3f(1.0f, 0.2f, 0.2f);
		ArrayList<Face3D> faces = obj.getFaces();
		ArrayList<Vertex3D> vertices = obj.getVertices();
		for (int i=0; i < faces.size(); i++) {
			gl2.glBegin(GL.GL_LINE_LOOP);
			Vertex3D Vi = vertices.get(faces.get(i).getIndexes()[0] - 1); // faces pamti indexe od 1 pa nadalje
			Vertex3D Vi1 = vertices.get(faces.get(i).getIndexes()[1] - 1);
			Vertex3D Vi2 = vertices.get(faces.get(i).getIndexes()[2] - 1);
			gl2.glVertex3f(Vi.getX(), Vi.getY(), Vi.getZ());
			gl2.glVertex3f(Vi1.getX(), Vi1.getY(), Vi1.getZ());
			gl2.glVertex3f(Vi2.getX(), Vi2.getY(), Vi2.getZ());
			gl2.glEnd();
		}
	}
	
	public static void main (String [] args) {

		File file = new File("kocka.obj");
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
		
		SwingUtilities.invokeLater(new Runnable () {
			@Override
			public void run () {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
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
						
						double x = r*Math.cos(Math.toRadians(angle));
						double y = 4;
						double z = r*Math.sin(Math.toRadians(angle));
						
						glu.gluLookAt(x, y, z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
						renderScene(gl2);
					}
				});

				final JFrame jframe = new JFrame (
					"Zadatak 6.1");
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
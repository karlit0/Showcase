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
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import lab01.pomlib.IMatrix;
import lab01.pomlib.IVector;
import lab01.pomlib.Vector;

public class ZD7_2 {
	static {
	GLProfile.initSingleton();
	}
	
	static ObjectModel obj;
	static IMatrix m;
	static double angle = 18.4349488;
	static double increment = 1;
	static double r = 3.16227766;
	static GLCanvas glCanvas = null;
	static int option = 1;
		
	static void renderScene(GL2 gl2) {
		gl2.glColor3f(1.0f, 0.2f, 0.2f);
		ArrayList<Face3D> faces = obj.getFaces();
		ArrayList<Vertex3D> vertices = obj.getVertices();
		for (int i=0; i < faces.size(); i++) {
			if ((option == 2 || option == 3) && !faces.get(i).isVisible())
				continue;
			
			IVector tvs[] = new IVector[3];
			
			gl2.glBegin(GL.GL_LINE_LOOP);
			for (int j=0; j < faces.get(i).getIndexes().length; j++) {
				Vertex3D Vi = vertices.get(faces.get(i).getIndexes()[j] - 1); // faces pamti indexe od 1 pa nadalje
				IVector v = new Vector(Vi.getX(), Vi.getY(), Vi.getZ(), 1.0f);
				IVector tv = null;
				try {
					tv = v.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogeneus();
				} catch (Exception e) {
					e.printStackTrace();
				}
//				System.out.println(v + " -> " + tv);
				if (option == 4)
					tvs[j] = tv;
				else 
					gl2.glVertex2f((float) tv.get(0), (float) tv.get(1));
			}
			if (option == 4 && IRG.isAntiClockwise(tvs)) {
				gl2.glVertex2f((float) tvs[0].get(0), (float) tvs[0].get(1));
				gl2.glVertex2f((float) tvs[1].get(0), (float) tvs[1].get(1));
				gl2.glVertex2f((float) tvs[2].get(0), (float) tvs[2].get(1));
			}
			gl2.glEnd();
		}
	}
	
	public static void main (String [] args) {

		File file = new File("kocka.obj");
//		File file = new File("frog.obj");
//		File file = new File("teddy.obj");
		
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
						if (e.getKeyCode() == KeyEvent.VK_1) {
							option = 1;
							System.out.println("Odabran na�in 1. : Bez odbacivanja");
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_2) {
							option = 2;
							System.out.println("Odabran na�in 2. : Odbacivanja algoritmom 1");
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_3) {
							option = 3;
							System.out.println("Odabran na�in 3. : Odbacivanja algoritmom 2");
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_4) {
							option = 4;
							System.out.println("Odabran na�in 4. : Odbacivanja algoritmom 3");
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
						
						gl2.glClearColor(0, 1, 0, 0);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();
						
						IMatrix tp = null;
						IMatrix pr = null;
						double x = r*Math.cos(Math.toRadians(angle));
						double y = 4;
						double z = r*Math.sin(Math.toRadians(angle));
						IVector eye = new Vector(x, y, z);
						
						try {
							tp = IRG.lookAtMatrix(eye , new Vector(0, 0, 0), new Vector(0, 1, 0));
							pr = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);
							m = tp.nMultiply(pr);
						} catch (Exception e) {
							e.printStackTrace();
						}
					
						switch (option) {
						case 1:
							break;
						case 2:
							try {
								obj.determineFaceVisibilities1(eye);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case 3:
							try {
								obj.determineFaceVisibilities2(eye);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case 4:
							break;
						default:
							break;
						}
						renderScene(gl2);
					}
				});

				final JFrame jframe = new JFrame (
					"Zadatak 7.2");
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
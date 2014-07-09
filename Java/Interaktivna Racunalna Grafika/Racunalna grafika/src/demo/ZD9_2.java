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

public class ZD9_2 {
	static {
	GLProfile.initSingleton();
	}
	
	static ObjectModel obj;
	static IMatrix m;
	static double angle = 18.4349488;
	static double increment = 1;
	static double r = 3.16227766;
	static GLCanvas glCanvas = null;
	static boolean usingZBuffer = true;
	static boolean usingFlatShading = true;
	static float Iar, Iag, Iab, Idr, Idg, Idb, Irr, Irg, Irb;
	static float kar, kag, kab, kdr, kdg, kdb, krr, krg, krb, n;
	static float Ir, Ig, Ib;
	static IVector lightVector;
	static IVector eye;
		
	static void renderScene(GL2 gl2) {
		ArrayList<Face3D> faces = obj.getFaces();
		ArrayList<Vertex3D> vertices = obj.getVertices();
		for (int i=0; i < faces.size(); i++) {
			if (!faces.get(i).isVisible())
				continue;

			if (usingFlatShading) {
				Vertex3D V1 = vertices.get(faces.get(i).getIndexes()[0] - 1);
				Vertex3D V2 = vertices.get(faces.get(i).getIndexes()[1] - 1);
				Vertex3D V3 = vertices.get(faces.get(i).getIndexes()[2] - 1);
				Vertex3D P = calculateMiddlePoint(V1, V2, V3);
				calculateIntensity(P, faces.get(i).getNormal());
				gl2.glColor3f(Ir, Ig, Ib);
			}
			gl2.glBegin(GL2.GL_POLYGON);		
			for (int j=0; j < faces.get(i).getIndexes().length; j++) {
				Vertex3D Vi = vertices.get(faces.get(i).getIndexes()[j] - 1); // faces pamti indexe od 1 pa nadalje
				
				if (!usingFlatShading) {
					calculateIntensity(Vi, Vi.getNormal());
					gl2.glColor3f(Ir, Ig, Ib);
				}
				
				IVector v = new Vector(Vi.getX(), Vi.getY(), Vi.getZ(), 1.0f);
				IVector tv = null;
				try {
					tv = v.toRowMatrix(false).nMultiply(m).toVector(false).nFromHomogeneus();
				} catch (Exception e) {
					e.printStackTrace();
				}								
				gl2.glVertex3f((float) tv.get(0), (float) tv.get(1), (float) tv.get(2));
			}
			gl2.glEnd();
		}
	}
	
	public static void main (String [] args) {

//		File file = new File("kocka.obj");
		File file = new File("teddy.obj");
//		File file = new File("frog.obj");
		
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
							usingFlatShading = true;
							System.out.println("Uporaba konstantnog sjenèanja");
							e.consume();
							glCanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_G){
							usingFlatShading = false;
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
						
						gl2.glMatrixMode(GL2.GL_MODELVIEW);
						gl2.glViewport(0, 0, width, height); // slika preko cijelog prozora
					}

					@Override
					public void init(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
						gl2.glClearDepth(1.0f);
						
						gl2.glDepthFunc(GL.GL_LEQUAL);
						gl2.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL);
						gl2.glShadeModel(GL2.GL_SMOOTH);
						Iar = Iag = Iab = 0.2f;
						Idr = Idg = 0.8f;
						Idb = 0f;
						Irr = Irg = Irb = 0f;
						kar = kag = kab = 1f;
						kdr = kdg = kdb = 1f;
						krr = krg = krb = 0.01f;
						n = 96f;
						lightVector = new Vector(4f, 5f, 3f);
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
						
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
						gl2.glLoadIdentity();
						
						IMatrix tp = null;
						IMatrix pr = null;
						double x = r*Math.cos(Math.toRadians(angle));
						double y = 4;
						double z = r*Math.sin(Math.toRadians(angle));
						eye = new Vector(x, y, z);
						
						try {
							tp = IRG.lookAtMatrix(new Vector(x, y, z), new Vector(0, 0, 0), new Vector(0, 1, 0));
							pr = IRG.buildFrustumMatrix(-0.5, 0.5, -0.5, 0.5, 1, 100);
							m = tp.nMultiply(pr);
							obj.determineFaceVisibilities2(eye);
						} catch (Exception e) {
							e.printStackTrace();
						}
					
					
						
						renderScene(gl2);
					}
				});

				final JFrame jframe = new JFrame (
					"Zadatak 9.2");
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
	
	static void calculateIntensity(Vertex3D point, IVector normal) {
		float Igr, Igg, Igb, _Idr, _Idg, _Idb, Isr, Isg, Isb;
		
		Igr = kar * Iar;
		Igg = kag * Iag;
		Igb = kab * Iab;
		
		IVector l = new Vector (lightVector.get(0) - point.getX(), lightVector.get(1) - point.getY(), lightVector.get(2) - point.getZ());
		float cosphi = 0;
		try {
			cosphi = (float) l.cosine(normal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cosphi < 0)
			cosphi = 0;
		_Idr = Idr * kdr * cosphi;
		_Idg = Idg * kdg * cosphi;
		_Idb = Idb * kdb * cosphi;
		
		IVector r = reflectedVector(l, normal);
		IVector v = new Vector(eye.get(0) - point.getX(), eye.get(1) - point.getY(), eye.get(2) - point.getZ());
		float cosalpha = 0;
		try {
			cosalpha = (float) r.cosine(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Isr = Irr * krr * (float) Math.pow(cosalpha, n);
		Isg = Irg * krg * (float) Math.pow(cosalpha, n);
		Isb = Irb * krb * (float) Math.pow(cosalpha, n);
		
		Ir = Igr + _Idr + Isr;
		Ig = Igg + _Idg + Isg;
		Ib = Igb + _Idb + Isb;
	}
	
	static IVector reflectedVector(IVector m, IVector n) {
		IVector r = null;
		try {
			r = n.nScalarMultiply(2).nScalarMultiply(1f / n.norm());
			r = r.nScalarMultiply(m.scalarProduct(n));
			r = r.nScalarMultiply(1f / n.norm());
			r = r.nSub(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}
	
	static Vertex3D calculateMiddlePoint(Vertex3D V1, Vertex3D V2, Vertex3D V3) {
		float Cx, Cy, Cz;
		
		Cx = (V1.getX() + V2.getX() + V3.getX()) / 3;
		Cy = (V1.getY() + V2.getY() + V3.getY()) / 3;
		Cz = (V1.getZ() + V2.getZ() + V3.getZ()) / 3;
		return new Vertex3D(Cx, Cy, Cz);
	}
}
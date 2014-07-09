package demo;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

import lab01.pomlib.IMatrix;
import lab01.pomlib.Matrix;

public class ZD8 {
	static {
	GLProfile.initSingleton();
	}
	
	static int cheight;
	static ArrayList<iTocka2D> points; 
	static iTocka2D pointSelected;
	static int divs = 50;
	static double bernstein[];
	static double bezier[];
		
	static void drawControlPolygon(GL2 gl2) {
		gl2.glColor3f(1.0f, 0.0f, 0.0f);
		gl2.glBegin(GL.GL_LINE_STRIP);
		for (int i=0; i < points.size(); i++) {
			gl2.glVertex2f((float) points.get(i).x, (float) points.get(i).y); 
		}
		gl2.glEnd();
		gl2.glColor3f(1.0f, 1.0f, 0.0f);
		for (int i=0; i < points.size(); i++) {
			gl2.glBegin(GL.GL_LINE_LOOP);
			gl2.glVertex2f((float) points.get(i).x - 10, (float) points.get(i).y - 10);
			gl2.glVertex2f((float) points.get(i).x - 10, (float) points.get(i).y + 10);
			gl2.glVertex2f((float) points.get(i).x + 10, (float) points.get(i).y + 10);
			gl2.glVertex2f((float) points.get(i).x + 10, (float) points.get(i).y - 10);
			gl2.glEnd();
		}
	}
	
	static int[] computeFactors(int n) {
		int a = 1;
		int factors[] = new int[n+1];
		
		for (int i=1; i <= n+1; i++) {
			factors[i-1] = a;
			a = a * (n-i+1) / i;
		}
		return factors;
	}
	
	static void drawApproxBezier(GL2 gl2) {
		int x, y;
		int n = points.size()-1;
		int factors[] = computeFactors(n);
		double t, b;
		
		gl2.glColor3f(0.0f, 0.0f, 1.0f);
		gl2.glBegin(GL.GL_LINE_STRIP); 
		for (int i=0; i <= divs; i++) {
			t = 1.0 / divs * i;
			x = 0; y = 0;
			for (int j=0; j <= n; j++) {
				b = factors[j]*Math.pow(t, j)*Math.pow(1-t, n-j);
				x += b * points.get(j).x;
				y += b * points.get(j).y;
			}
			gl2.glVertex2f(x, y);
		}		
		gl2.glEnd();
	}
	
	static double bernsteinToBezier(int i, double t) {
		int n = points.size()-1;
		int factors[] = computeFactors(n);
		double b[] = new double[n+1];
		double f = 0;
		for (int j=0; j <= n; j++) {
			b[j] = factors[j]*Math.pow(t, j)*Math.pow(1-t, n-j);
		}
		for (int j=i; j <= n; j++)
			f += b[j];
		return f;
	}
	
	static void drawInterpolBezier(GL2 gl2) {
		if (points.size() < 2)
			return;
		
		double F[][] = new double[points.size()][points.size()];
		
		for (int i=0; i <= points.size()-1; i++) {
			double t = (float) i/(points.size()-1);
			for (int j=0; j <= points.size()-1; j++) {
				F[i][j] = bernsteinToBezier(j, t);
			}
		}
		IMatrix mat = null;
		IMatrix px = null;
		IMatrix py = null;
		IMatrix ax = null;
		IMatrix ay = null;
		double pxarray[][] = new double[points.size()][1];
		double pyarray[][] = new double[points.size()][1];
		for (int i=0; i < points.size(); i++) {
			pxarray[i][0] = points.get(i).x;
			pyarray[i][0] = points.get(i).y;
		}
		
		try {
			mat = new Matrix(points.size(), points.size(), F, true);
			mat = mat.nInvert();
			px = new Matrix(points.size(), 1, pxarray, true);
			py = new Matrix(points.size(), 1, pyarray, true);
			ax = mat.nMultiply(px);
			ay = mat.nMultiply(py);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		double t;
		double x, y;
		gl2.glColor3f(0.0f, 0.0f, 0.0f);
		gl2.glBegin(GL.GL_LINE_STRIP);
		for (int i=0; i <= divs; i++) {
			t = 1.0 / divs * i;
			x = 0; y = 0;
			for (int j=0; j <= points.size()-1; j++) {
				x += ax.get(j, 0) * bernsteinToBezier(j, t);
				y += ay.get(j, 0) * bernsteinToBezier(j, t);
			}
			gl2.glVertex2f((float) x, (float) y);
		}
		gl2.glEnd();
	}
	
	public static void main (String [] args) {

		SwingUtilities.invokeLater(new Runnable () {
			@Override
			public void run () {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapabilities);
				
				points = new ArrayList<iTocka2D>();
				pointSelected = null;
				
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							points = new ArrayList<iTocka2D>();
							e.consume();
							glcanvas.display();
						}
					}
				});
				
				glcanvas.addMouseListener (new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1) { // Left Click
							points.add(new iTocka2D(e.getX(), cheight - e.getY()));
						}
						glcanvas.display();
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON3){ // Right Click
							// provjeri je li u radijusu (pravokutniku) od 10 px oko kliknute tocke definiran vrh kontrolnog poligona
							int x = e.getX();
							int y = cheight - e.getY();
							for (int i=0; i < points.size(); i++) {
								iTocka2D cpoint = points.get(i);
								if (Math.abs(x - cpoint.x) <= 10 && 
										Math.abs(y - cpoint.y) <= 10 ) {
									pointSelected = cpoint;
									break;
								}
							}
						}
					}
					
					@Override
					public void mouseReleased(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON3)
							pointSelected = null;
					}
				});
				
				glcanvas.addMouseMotionListener(new MouseMotionAdapter() {
					@Override
					public void mouseDragged(MouseEvent e) {
						if (pointSelected != null) {
							int index = points.indexOf(pointSelected);
							iTocka2D dpoint = points.get(index);
							dpoint.x = e.getX();
							dpoint.y = cheight - e.getY();
							super.mouseDragged(e);
							glcanvas.display();
						}
					}
				});

				
				// Reagiranje na promjenu velicine platna, na zahtjev za
				// crtanjem i slicno ...
				glcanvas.addGLEventListener (new GLEventListener() {
					@Override
					public void reshape(GLAutoDrawable glautodrawable, int x,
						int y, int width, int height) {
						cheight = height;
						
						GL2 gl2 = glautodrawable.getGL().getGL2();
						GLU glu = GLU.createGLU(gl2);
												
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();
						
						glu.gluOrtho2D(0.0f, width, 0.0f, height);
						
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
						
						drawControlPolygon(gl2);
						drawApproxBezier(gl2);
						drawInterpolBezier(gl2);
					}
				});

				final JFrame jframe = new JFrame (
					"Zadatak 8");
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
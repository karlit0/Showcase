package demo;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EmptyStackException;
import java.util.Stack;

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

import lab01.pomlib.Complex;

public class ZD11_1 {
		
	static double xmin = 0;
	static double xmax = 599;
	static double ymin = 0;
	static double ymax = 599;
	static double umin = -2;
	static double umax = 1;
	static double vmin = -1.2;
	static double vmax = 1.2;
	static int maxLimit = 128;
	static boolean usingBlackWhite = true;
	static boolean cubedFunc = true;
	static Stack<Params> stack = new Stack<Params>();
	
	static {
	GLProfile.initSingleton();
	}
	
	public static void renderScene(GL2 gl2) {
		gl2.glPointSize(1);
		gl2.glBegin(GL.GL_POINTS);
		for (int x=0; x < xmax; x++)
			for(int y=0; y < ymax; y++) {
				double c_re = (x - xmin) / (double) (xmax - xmin) * (umax - umin) + umin;
				double c_im = (y - ymin) / (double) (ymax - ymin) * (vmax - vmin) + vmin;
				Complex c = new Complex(c_re, c_im);
				int n;
				if (cubedFunc)
					n = divergenceTest(c, maxLimit);
				else
					n = divergenceTest2(c, maxLimit);
				if (usingBlackWhite)
					colorScheme1(n, gl2);
				else
					colorScheme2(n, gl2);
				gl2.glVertex2i(x, y);
			}
		gl2.glEnd();
	}
	
	public static int divergenceTest(Complex c, int maxLimit) {
		Complex z = new Complex(0, 0);
		for (int i=1; i <= maxLimit; i++) {
			z = z.squared().add(c);
			if (z.module() > 2)
				return i;
		}
		return -1;
	}
	public static int divergenceTest2(Complex c, int maxLimit) {
		Complex z = new Complex(0, 0);
		for (int i=1; i <= maxLimit; i++) {
			z = z.cubed().add(c);
			if (z.module() > 2)
				return i;
		}
		return -1;
	}
	
	public static void colorScheme1(int n, GL2 gl2) {
		if (n == -1)
			gl2.glColor3f(0f, 0f, 0f);
		else
			gl2.glColor3f(1f, 1f, 1f);
	}
	public static void colorScheme2(int n, GL2 gl2) {
		if (n == -1) {
			gl2.glColor3f(0f, 0f, 0f);
		} else if (maxLimit < 16) {
			int r = (int) ((n-1) / (double) (maxLimit - 1) * 255 + 0.5);
			int g = 255 - r;
			int b = ((n-1) % (maxLimit / 2)) * 255 / (maxLimit / 2);
			gl2.glColor3f((float) (r / 255f), (float) (g / 255f), (float) (b / 255f));
		} else {
			int lim = maxLimit < 32 ? maxLimit : 32;
			int r = (n-1) * 255 / lim;
			int g = ((n-1) % (lim / 4)) * 255 / (lim / 4);
			int b = ((n-1) % (lim / 8)) * 255 / (lim / 8);
			gl2.glColor3f((float) (r / 255f), (float) (g / 255f), (float) (b / 255f));
		}
	}
	
	public static void main (String[] args) {
		
		SwingUtilities.invokeLater(new Runnable () {

			@Override
			public void run () {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapabilities);
				
				// Reagiranje na pritiske tipki na misu ...
				glcanvas.addMouseListener (new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
												
//						System.out.println("Mis je kliknut na: x=" + e.getX()
//								+ ", y=" + (ymax - e.getY()));
						double x = e.getX();
						double y = ymax - e.getY();						
						
						double c_re = (x - xmin) / (double) (xmax - xmin) * (umax - umin) + umin;
						double c_im = (y - ymin) / (double) (ymax - ymin) * (vmax - vmin) + vmin;
						double w = umax - umin;
						double h = vmax - vmin;
						
						Params p = new Params(umin, umax, vmin, vmax);
						stack.push(p);
						
						umax = c_re + (w / 32);
						umin = c_re - (w / 32);
						vmax = c_im + (h / 32);
						vmin = c_im - (h / 32);						
						
						glcanvas.display();
					}
				});

				// Reagiranje na pritiske tipaka na tipkovnici ...
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_B) {
							usingBlackWhite = true;
							e.consume();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_C) {
							usingBlackWhite = false;
							e.consume();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_1) {
							cubedFunc = true;
							e.consume();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_2) {
							cubedFunc = false;
							e.consume();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_X) {							
							try {
								Params p = stack.pop();
								umin = p.umin;
								umax = p.umax;
								vmin = p.vmin;
								vmax = p.vmax;
							} catch(EmptyStackException ese) {
								e.consume();
								return;
							}
							e.consume();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
							while (true) {
								try {
									Params p = stack.pop();
									umin = p.umin;
									umax = p.umax;
									vmin = p.vmin;
									vmax = p.vmax;
								} catch(EmptyStackException ese) {
									break;
								}
							}
							e.consume();
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
						GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glMatrixMode(GL2.GL_PROJECTION);
						gl2.glLoadIdentity();

						xmax = width-1;
						ymax = height-1;
						
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
					"Vježba 11.1 - Mandelbrotov Fraktal");
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

class Params {
	double umin;
	double umax;
	double vmin;
	double vmax;
	
	public Params(double _umin, double _umax, double _vmin, double _vmax) {
		umin = _umin;
		umax = _umax;
		vmin = _vmin;
		vmax = _vmax;
	}
}
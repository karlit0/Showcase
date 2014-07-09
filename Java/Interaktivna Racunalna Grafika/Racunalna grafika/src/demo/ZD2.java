package demo;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import lab01.pomlib.IVector;
import lab01.pomlib.Vector;

public class ZD2 {
	
	static {
	GLProfile.initSingleton();
	}
	

	
	public static void main (String[] args) {
		
		
		
		SwingUtilities.invokeLater(new Runnable () {
						
			private int counter = 0;
			private ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
			private int tempX;
			private int tempY;
			private int curHeight;
			private boolean kontrola = false;
			private boolean odsijecanje = false;
			private float boxX1;
			private float boxY1;
			private float boxX2;
			private float boxY2;
			private float boxX3;
			private float boxY3;
			private float boxX4;
			private float boxY4;
			
			
			@Override
			public void run () {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapabilities);
				
				// Reagiranje na pritiske tipki na misu ...
				glcanvas.addMouseListener (new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
												
						System.out.println("Mis je kliknut na: x=" + e.getX()
								+ ", y=" + e.getY());

						counter++;						
						if (counter == 1) {
							tempX = e.getX();
							tempY = curHeight - e.getY();
						} else if (counter == 2) {
							lines.add(new LineSegment(tempX, tempY, e.getX(), curHeight - e.getY()));
							counter = 0;
						}
														
						// Napravi nesto
						// ...
						// Posalji zahtjev za ponovnim crtanjem ...
						glcanvas.display();
					}
				});

				// Reagiranje na pritiske tipaka na tipkovnici ...
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_O) {
							if (odsijecanje == false)
								odsijecanje = true;
							else
								odsijecanje = false;
							e.consume();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_K) {
							if (kontrola == false)
								kontrola = true;
							else
								kontrola = false;
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
						curHeight = height;
						
						GL2 gl2 = glautodrawable.getGL().getGL2();
//						GL gl = glautodrawable.getGL();					
						gl2.glMatrixMode(GL2.GL_PROJECTION);
//						gl.
						gl2.glLoadIdentity();

						// coordinate system origin at lower left with width and
						// height same as the window
						GLU glu = new GLU();
						glu.gluOrtho2D(0.0f, width, 0.0f, height);

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
					
					boolean pointInBox(int x, int y) {
						if (odsijecanje == false)
							return true;
						if (x >= boxX1 && x <= boxX2 && y >= boxY1 && y <= boxY3)
							return true;
						else
							return false;
					}
					
					void bresenhamPos(GL2 gl2, int xs, int ys, int xe, int ye) {
						int x, yc, correction;
						int a, yf;						
						
						if (ye-ys <= xe-xs) {
							a = 2*(ye-ys);
							yc = ys; yf = -(xe-xs); correction = -2*(xe-xs);
							for (x = xs; x <= xe; x++) {
								if (pointInBox(x, yc) == true)
									gl2.glVertex2f((float) x, (float) yc);
								yf = yf + a;
								if (yf >= 0) {
									yf = yf + correction;
									yc = yc + 1;
								}
							}
						} else {
							// swap x, y coordinates
							x = xe; xe = ye; ye = x;
							x = xs; xs = ys; ys = x;
							a = 2*(ye-ys);
							yc = ys; yf = -(xe-xs); correction = -2*(xe-xs);
							for (x = xs; x <= xe; x++) {
								if (pointInBox(yc, x) == true)
									gl2.glVertex2f((float) yc, (float) x);
								yf = yf + a;
								if (yf >= 0) {
									yf = yf + correction;
									yc = yc + 1;
								}
							}
						}
					}
					
					void bresenhamNeg(GL2 gl2, int xs, int ys, int xe, int ye) {
						int x, yc, correction;
						int a, yf;
						
						if (-(ye-ys) <= xe-xs) {
							a = 2*(ye-ys);
							yc = ys; yf = (xe-xs); correction = 2*(xe-xs);
							for (x = xs; x <= xe; x++) {
								if (pointInBox(x, yc) == true)
									gl2.glVertex2f((float) x, (float) yc);
								yf = yf + a;
								if (yf <= 0) {
									yf = yf + correction;
									yc = yc - 1;
								}
							}
						} else {
							x = xe; xe = ys; ys = x;
							x = xs; xs = ye; ye = x;
							a = 2*(ye-ys);
							yc = ys; yf = (xe-xs); correction = 2*(xe-xs);
							for (x = xs; x <= xe; x++) {
								if (pointInBox(yc, x) == true)
									gl2.glVertex2f((float) yc, (float) x);
								yf = yf + a;
								if (yf <= 0) {
									yf = yf + correction;
									yc = yc - 1;
								}
							}
						}
					} 
					
					void bresenham(GL2 gl2, int xs, int ys, int xe, int ye) {
						if (xs <= xe) {
							if (ys <= ye) {
								bresenhamPos(gl2, xs, ys, xe, ye);
							} else {
								bresenhamNeg(gl2, xs, ys, xe, ye);
							}
						} else {
							if (ys >= ye) {
								bresenhamPos(gl2, xe, ye, xs, ys);
							} else {
								bresenhamNeg(gl2, xe, ye, xs, ys);								
							}
						}
					}
	
					@Override
					public void display(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						int width = glautodrawable.getWidth();
						int height = glautodrawable.getHeight();
						boxX1 = width/4.f;
						boxY1 = height/4.f;
						boxX2 = 3*width/4.f;
						boxY2 = height/4.f;
						boxX3 = 3*width/4.f;
						boxY3 = 3*height/4.f;
						boxX4 = width/4.f;
						boxY4 = 3*height/4.f;

						gl2.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();

						gl2.glColor3f(0.0f, 0.0f, 0.0f);

						for (int i = 0; i < lines.size(); i++) {							
							LineSegment cur_line = lines.get(i);
							int xs = cur_line.getX1();
							int ys = cur_line.getY1();
							int xe = cur_line.getX2();
							int ye = cur_line.getY2();
							gl2.glBegin(GL.GL_POINTS);
							bresenham(gl2, xs, ys, xe, ye);
							gl2.glEnd();
						}

						if (kontrola == true) {
							gl2.glColor3f(1.0f, 0.0f, 0.0f);
							gl2.glBegin(GL.GL_LINES);
							for (int i = 0; i < lines.size(); i++) {
								LineSegment cur_line = lines.get(i);
								int xs = cur_line.getX1();
								int ys = cur_line.getY1();
								int xe = cur_line.getX2();
								int ye = cur_line.getY2();
								IVector v = new Vector(xs-xe, ys-ye);
//								Vector v = new Vector(xs-xe, ys-ye);
								
							
								System.out.println(v);
//								float dx = xs-xe;
//								float dy = ys-ye;
//								float dist = (float) Math.sqrt(dx*dx + dy*dy);
								try {
									v.normalize();
//									System.out.println("NORMALIZED: " + v);
								} catch (Exception e) {
									e.printStackTrace();
								}
//								dx /= dist;
//								dy /= dist;
//								float x3 = xs - (4)*dy;
								float x3 = (xs - (4)*(float) v.get(1));
//								float y3 = ys + (4)*dx;
								float y3 = (ys + (4)*(float) v.get(0));
//								float x4 = xe - (4)*dy;
								float x4 = (xe - (4)*(float) v.get(1));
//								float y4 = ye + (4)*dx;
								float y4 = (ye + (4)*(float) v.get(0));
								
								gl2.glVertex2f(x3, y3);
								gl2.glVertex2f(x4, y4);														
							}
							gl2.glEnd();
						}
						if (odsijecanje == true) {
							gl2.glColor3f(0.0f, 1.0f, 0.0f);
							gl2.glBegin(GL.GL_LINES);
							gl2.glVertex2f(boxX1, boxY1);
							gl2.glVertex2f(boxX2, boxY2);
							gl2.glVertex2f(boxX2, boxY2);
							gl2.glVertex2f(boxX3, boxY3);
							gl2.glVertex2f(boxX3, boxY3);
							gl2.glVertex2f(boxX4, boxY4);
							gl2.glVertex2f(boxX4, boxY4);
							gl2.glVertex2f(boxX1, boxY1);
							gl2.glEnd();
						}
//												
					}
				});

				final JFrame jframe = new JFrame (
					"Vježba 2 - Crtanje linija na rasterskim prikaznim jedinicama");
				jframe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				jframe.addWindowListener(new WindowAdapter() {
					public void windowClosing (WindowEvent windowevent) {
						jframe.dispose();
					System.exit(0) ;
					}
				});	
				jframe.getContentPane().add(glcanvas, BorderLayout.CENTER);
//				jframe.setSize(640, 480);
//				jframe.setSize(800, 600);
				jframe.setSize(600, 600);
				jframe.setVisible(true);
				glcanvas.requestFocusInWindow();
			}
			

		});			
	}
}

class LineSegment {
	
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	public LineSegment(int _x1, int _y1, int _x2, int _y2){
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;				
	}
	
	public int getX1(){
		return x1;
	}
	public int getY1(){
		return y1;
	}
	public int getX2(){
		return x2;
	}
	public int getY2(){
		return y2;
	}	
}
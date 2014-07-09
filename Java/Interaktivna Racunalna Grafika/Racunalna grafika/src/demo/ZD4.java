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

public class ZD4 {
	
	static {
	GLProfile.initSingleton();
	}
	
	public static void main (String[] args) {
		
		SwingUtilities.invokeLater(new Runnable () {

			private ArrayList<iPolyElem> polel = new ArrayList<iPolyElem>();
			private float mouseX;
			private float mouseY;	
			private int Height;
			private boolean popunjavanje = false;
			private boolean konveksnost = false;
			private int state = 1;
			private iPolyElem dynamiclast;
			private iPolyElem dynamicmouse;
			private ArrayList<iPolyElem> dynamicpolel = new ArrayList<iPolyElem>();
			private boolean isConvex = true;
			private boolean clockwiseOrientation = false;
			
			/*
			 * RED (255, 0, 0) = 1, 0, 0
			 * GREEN (0, 128, 0) = 0, 0.5, 0
			 * BLUE (0, 0, 255) = 0, 0, 1
			 * CYAN (0, 255, 255) = 0, 1, 1 
			 * YELLOW (255, 255, 0) = 1, 1, 0
			 * MAGENTA (255, 0, 255) = 1, 0, 1
			 * 
			 * (non-Javadoc)
			 * @see java.lang.Runnable#run()
			 */						
			
			public void dynamicCalcCoef() {
				if (polel.size() < 1)
					return;
				
				checkPolygonConv();
				
				iPolyElem first = polel.get(0);
				dynamicpolel.clear();
				for (int i=0; i < polel.size(); i++)
					dynamicpolel.add(polel.get(i).copy());
				dynamicmouse = new iPolyElem(new iTocka2D((int) mouseX, (int) mouseY));
				dynamiclast = dynamicpolel.get(dynamicpolel.size()-1);
				dynamicpolel.add(dynamicmouse);
				
				
				dynamiclast.brid.a = dynamiclast.vrh.y - dynamicmouse.vrh.y;
				dynamiclast.brid.b = -(dynamiclast.vrh.x - dynamicmouse.vrh.x);
				dynamiclast.brid.c = dynamiclast.vrh.x * dynamicmouse.vrh.y
								- dynamiclast.vrh.y * dynamicmouse.vrh.x;
				if (clockwiseOrientation)
					dynamiclast.lijevi = dynamiclast.vrh.y < dynamicmouse.vrh.y;
				else
					dynamiclast.lijevi = dynamiclast.vrh.y > dynamicmouse.vrh.y;
					
				dynamicmouse.brid.a = dynamicmouse.vrh.y - first.vrh.y;
				dynamicmouse.brid.b = -(dynamicmouse.vrh.x - first.vrh.x);
				dynamicmouse.brid.c = dynamicmouse.vrh.x * first.vrh.y
								- dynamicmouse.vrh.y * first.vrh.x;
				if (clockwiseOrientation)
					dynamicmouse.lijevi = dynamicmouse.vrh.y < first.vrh.y;
				else
					dynamicmouse.lijevi = dynamicmouse.vrh.y > first.vrh.y;
				
//				System.out.println("Dynamic:");
//				for (int i=0; i < dynamicpolel.size(); i++)
//					System.out.println(dynamicpolel.get(i));
			}
			
			public void calcCoefPolygonKonv() {
				int i, i0;
				i0 = polel.size() - 1; 
				
				for (i=0; i < polel.size(); i++) {
					iPolyElem poleli0 = polel.get(i0);
					iPolyElem poleli = polel.get(i);
					poleli0.brid.a = poleli0.vrh.y - poleli.vrh.y;
					poleli0.brid.b = -(poleli0.vrh.x - poleli.vrh.x);
					poleli0.brid.c = poleli0.vrh.x * poleli.vrh.y
									- poleli0.vrh.y * poleli.vrh.x;
					if (clockwiseOrientation)
						poleli0.lijevi = poleli0.vrh.y < poleli.vrh.y;
					else
						poleli0.lijevi = poleli0.vrh.y > poleli.vrh.y;
					i0 = i;
				}
							
			}
			
			public void checkPolygonConv() {
				int ispod, iznad;
				double r;
				ispod = iznad = 0;
				iPolyElem poleli, poleli0;
				int i0 = dynamicpolel.size()-2;
				for (int i=0; i < dynamicpolel.size(); i++, i0++) {
					if (i0 >= dynamicpolel.size())
						i0 = 0;
					poleli0 = dynamicpolel.get(i0);
					poleli = dynamicpolel.get(i);
					r = poleli0.brid.a * poleli.vrh.x + poleli0.brid.b * poleli.vrh.y + poleli0.brid.c;
					if (r > 0) iznad++;
					else if (r < 0) ispod++;
				}
				isConvex = false;
				if (ispod == 0) {
					isConvex = true;
					clockwiseOrientation = false;
				}
				else if (iznad == 0) {
					isConvex = true;
					clockwiseOrientation = true;
				}
				
				correctOrientation();
				
//				System.out.println("is convex: " + isConvex);
//				System.out.println("clockwise orientation: " + clockwiseOrientation);
			}
			
			public void correctOrientation() {
				if (polel.size() < 2)
					return;
				int i0 = polel.size() - 1; 
				for (int i=0; i < polel.size(); i++) {
					iPolyElem poleli0 = polel.get(i0);
					iPolyElem poleli = polel.get(i);
					if (clockwiseOrientation)
						poleli0.lijevi = poleli0.vrh.y < poleli.vrh.y;
					else
						poleli0.lijevi = poleli0.vrh.y > poleli.vrh.y;
					i0 = i;
				}
			
			}
			
			public void fillPolygonConv(GL2 gl2) {
				int xmin, xmax, ymin, ymax;
				
				if (dynamicpolel.size() < 3) /* barem 3 toèke definirane (2 fiksne + miš) */ {
					if (dynamicpolel.size() == 2) {
						gl2.glVertex2f((float) dynamicpolel.get(0).vrh.x, (float) dynamicpolel.get(0).vrh.y);
						gl2.glVertex2f((float) dynamicpolel.get(1).vrh.x, (float) dynamicpolel.get(1).vrh.y);
					}
					return;
				}
				
				xmin = xmax = (int) dynamicpolel.get(0).vrh.x;
				ymin = ymax = (int) dynamicpolel.get(0).vrh.y;
				for (int i = 1; i < dynamicpolel.size(); i++) {
					if (xmin > (int) dynamicpolel.get(i).vrh.x) xmin = (int) dynamicpolel.get(i).vrh.x;
					if (xmax < (int) dynamicpolel.get(i).vrh.x) xmax = (int) dynamicpolel.get(i).vrh.x;
					if (ymin > (int) dynamicpolel.get(i).vrh.y) ymin = (int) dynamicpolel.get(i).vrh.y;
					if (ymax < (int) dynamicpolel.get(i).vrh.y) ymax = (int) dynamicpolel.get(i).vrh.y;
				}
				
				int i0;				
				double L, D, x;
				iPolyElem poleli, poleli0;
				/* Bojanje poligona */
				for (int y = ymin; y <= ymax; y++) {
					/* Pronadi najvece lijevo i najmanje desno sjeciste */
					L = xmin; D = xmax;
					i0 = dynamicpolel.size()-1;
					/* i0 je pocetak brida, i je kraj brida */
					for (int i=0; i < dynamicpolel.size(); i0=i++) {
						/* ako je brid vodoravn */
						poleli = dynamicpolel.get(i);
						poleli0 = dynamicpolel.get(i0);
						if (poleli0.brid.a == 0) {
							if (poleli0.vrh.y == y) {
								if (poleli0.vrh.x < poleli.vrh.x){
									L = poleli0.vrh.x;
									D = poleli.vrh.x;
								} else {
									L = poleli.vrh.x;
									D = poleli0.vrh.x;
								}
								break;
							}
						} else { /* inace je regularan brid, nadi sjeciste*/
							x = (- poleli0.brid.b * y - poleli0.brid.c) /
									(double) poleli0.brid.a;
							if (poleli0.lijevi) {
								if (L < x) L = x;					
							} else {
								if (D > x) D = x;
							}							
						}
					}
//					if (L > D) //TODO
//						continue;
					gl2.glVertex2f((float) L, y);
					gl2.glVertex2f((float) D, y);
				}
			}
			
			public boolean checkIsMouseIn(int x, int y) throws Exception {
				double r;
				boolean mightBeOn = false;
				for (int i=0; i < polel.size(); i++) {
					iPolyElem poleli = polel.get(i);
					r = poleli.brid.a * x + poleli.brid.b * y + poleli.brid.c;
//					System.out.println("r = " + r);
					if (r == 0) /* tocka na bridu poligona */
						mightBeOn = true;
					if (clockwiseOrientation) {
						if (r > 0)
							return false;
					} else {
						if (r < 0)
							return false;
					}
				}
				if (mightBeOn)
					throw new Exception();
				return true;
			}
			
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
//								+ ", y=" + e.getY());

						if (state == 2) {
							boolean in;
							try {
								in = checkIsMouseIn(e.getX(), Height - e.getY());
							} catch (Exception e1) {
								System.out.println("Toèka (" + e.getX() + ", " + (Height - e.getY()) + ") je na bridu poligona");
								return;
							}
							if (in)
								System.out.println("Toèka (" + e.getX() + ", " + (Height - e.getY()) + ") je unutar poligona");
							else
								System.out.println("Toèka (" + e.getX() + ", " + (Height - e.getY()) + ") je izvan poligona");
							return;
						}
						
						if (konveksnost)
							if (!isConvex) {
								System.out.println("Toèka (" + e.getX() + ", " + (Height - e.getY()) + ") se ne prihvaæa (koveksnost = true)");
								return;
							}
						
						polel.add(new iPolyElem(new iTocka2D(e.getX(), Height - e.getY())));
						calcCoefPolygonKonv();
//						for (int i=0; i < polel.size(); i++)
//							System.out.println(polel.get(i));
													
//						System.out.println("counter: " + counter);
						glcanvas.display();
					}
				});

				// Reagiranje na pomicanje pokazivaca misa ...
				glcanvas.addMouseMotionListener (new MouseMotionAdapter() {
					@Override
					public void mouseMoved(MouseEvent e) {
//					System.out.println("Mis pomaknut na: x=" + e.getX()
//							+ ", y=" + e.getY());
						
						mouseX = e.getX();
						mouseY = Height - e.getY();						
							
						if (state == 1)
							dynamicCalcCoef();

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
						if (e.getKeyCode() == KeyEvent.VK_Q){
							for (int i=0; i < polel.size(); i++)
								System.out.println(polel.get(i));
						}
						
						if (e.getKeyCode() == KeyEvent.VK_N) {
							e.consume();
							if (state == 1) {
								System.out.println("Prelazak u stanje 2");
								calcCoefPolygonKonv();
								state++;
								popunjavanje = false;
								konveksnost = false;
							}
							else if (state == 2) {
								System.out.println("Prelazak u stanje 1");
								state--;
								polel.clear();
								dynamicpolel.clear();
								isConvex = true;
							}
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_K) {
							e.consume();
							if (state == 2)
								return;
							
							if (konveksnost == true) {
								System.out.println("Postavljanje zastavice konveksnost na false");
								konveksnost = false;
							}
							else {
								if (isConvex) {
									System.out.println("Postavljanje zastavice konveksnost na true");
									konveksnost = true;
								}
								else
									System.out.println("Promjena zastavice konveksnost na false nije moguæa");
							}
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_P) {
							e.consume();
							if (state == 2)
								return;						
							
							if (popunjavanje == false) {
								System.out.println("Postavljanje zastavice popunjavanje na true");
								popunjavanje = true;
							}
							else {
								System.out.println("Postavljanje zastavice popunjavanje na false");
								popunjavanje = false;
							}
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
						Height = height;

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
	
					@Override
					public void display(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
	
						/*
						 * RED (255, 0, 0) = 1, 0, 0
						 * GREEN (0, 128, 0) = 0, 0.5, 0
						 * BLUE (0, 0, 255) = 0, 0, 1
						 * CYAN (0, 255, 255) = 0, 1, 1 
						 * YELLOW (255, 255, 0) = 1, 1, 0
						 * MAGENTA (255, 0, 255) = 1, 0, 1
						 * 
						 * (non-Javadoc)
						 * @see java.lang.Runnable#run()
						 */
						
						if (konveksnost)
							gl2.glClearColor(0.0f, 0.5f, 0.f, 0.f);
						else
							gl2.glClearColor(1f, 1f, 1f, 0f);
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();
						
						gl2.glColor3f(0f, 0f, 0f);
						
						if (popunjavanje == false) {
							if (polel.size() == 1) {
								gl2.glBegin(GL2.GL_LINES); 
								gl2.glVertex2f((float) polel.get(0).vrh.x, (float) polel.get(0).vrh.y);
								if (state == 1)
									gl2.glVertex2f(mouseX, mouseY);
								gl2.glEnd();
							} else if (polel.size() > 1) {
								gl2.glBegin(GL2.GL_LINE_LOOP);
								for (int i=0; i < polel.size(); i++)
									gl2.glVertex2f((float) polel.get(i).vrh.x, (float) polel.get(i).vrh.y);
								if (state == 1)
									gl2.glVertex2f(mouseX, mouseY);
								gl2.glEnd();
							}					
						}
						else {
							gl2.glBegin(GL2.GL_LINES);
							fillPolygonConv(gl2);
							gl2.glEnd();
						}									
					}
				});

				final JFrame jframe = new JFrame (
					"Vježba 3 - Crtanje i popunjavanje poligona");
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

class iTocka2D {
	double x;
	double y;
	
	public iTocka2D(double _x, double _y) {
		x = _x;
		y = _y;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}

class iBrid2D {
	double a;
	double b;
	double c;
	
	public iBrid2D() {
	}
	
	public iBrid2D(double _a, double _b, double _c) {
		a = _a;
		b = _b;
		c = _c;
	}
	
	@Override
	public String toString() {
		return "a="+a+", b="+b+", c="+c;
	}
}

class iPolyElem {
	iTocka2D vrh;
	iBrid2D brid;
	boolean lijevi;
	
	public iPolyElem(iTocka2D _vrh) {
		vrh = _vrh;
		brid = new iBrid2D();
	}
	
	public iPolyElem(iTocka2D _vrh, iBrid2D _brid, boolean _lijevi) {
		vrh = _vrh;
		brid = _brid;
		lijevi = _lijevi;
	}
	
	public iPolyElem copy() {
		iTocka2D _vrh = new iTocka2D(vrh.x, vrh.y);
		iBrid2D _brid = new iBrid2D(brid.a, brid.b, brid.c);
		return new iPolyElem(_vrh, _brid, lijevi);
	}
	
	@Override
	public String toString() {
		return "" + vrh + " " + brid + " " + lijevi;
	}
}

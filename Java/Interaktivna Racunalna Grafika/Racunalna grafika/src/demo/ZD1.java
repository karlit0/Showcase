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

public class ZD1 {
	
	static {
	GLProfile.initSingleton();
	}
	
	public static void main (String[] args) {
		
		SwingUtilities.invokeLater(new Runnable () {
			
			private	int counter = 0;
			private float[] x = new float[3];
			private float[] y = new float[3];
			private ArrayList<Triangle> triangles = new ArrayList<Triangle>();
			private float mouseX;
			private float mouseY;
			private float tempX1;
			private float tempY1;
			private float tempX2;
			private float tempY2;
			private int current_color = 0;
			private int curWidth;
			private int curHeight;			
			
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
						
						x[counter] = e.getX();
						y[counter] = e.getY();
						counter++;
						
						if (counter == 1) {
							tempX1 = e.getX();
							tempY1 = e.getY();
						}
						if (counter == 2) {
							tempX2 = e.getX();
							tempY2 = e.getY();
						}						
						if (counter > 2) {
							System.out.println("TROKUT!");
							switch (current_color) {
							case 0:
								triangles.add(new Triangle(x, y, 1f, 0f, 0f));
								break;
							case 1:
								triangles.add(new Triangle(x, y, 0f, 0.5f, 0f));								
								break;
							case 2:
								triangles.add(new Triangle(x, y, 0f, 0f, 1f));								
								break;
							case 3:
								triangles.add(new Triangle(x, y, 0f, 1f, 1f));								
								break;
							case 4:
								triangles.add(new Triangle(x, y, 1f, 1f, 0f));								
								break;
							case 5:
								triangles.add(new Triangle(x, y, 1f, 0f, 1f));								
								break;
							default:
								break;
							}						
							counter = 0;
						}
							
						
						System.out.println("counter: " + counter);
								
						// Napravi nesto
						// ...
						// Posalji zahtjev za ponovnim crtanjem ...
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
						mouseY = e.getY();
							
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
						if (e.getKeyCode() == KeyEvent.VK_N) {
							current_color++;
							if (current_color > 5)
								current_color = 0;
							e.consume();
							glcanvas.display();
						}
						if (e.getKeyCode() == KeyEvent.VK_P) {
							current_color--;
							if (current_color < 0)
								current_color = 5;
							e.consume();
							glcanvas.display();
						}
						//TODO
						/* dodana funkcionalnost cancelanja crtanja trenutnog trokuta */
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE) 
							counter = 0;
					}
				});

				// Reagiranje na promjenu velicine platna, na zahtjev za
				// crtanjem i slicno ...
				glcanvas.addGLEventListener (new GLEventListener() {
					@Override
					public void reshape(GLAutoDrawable glautodrawable, int x,
						int y, int width, int height) {
						if (width != 0 && height != 0) {
							float widthRatio = (float) width / (float) curWidth;
							float heightRatio = (float) height / (float) curHeight;
							scaleTriangles(widthRatio, heightRatio);
						}
						GL2 gl2 = glautodrawable.getGL().getGL2();
						gl2.glMatrixMode(GL2.GL_PROJECTION);
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
	
					@Override
					public void display(GLAutoDrawable glautodrawable) {
						GL2 gl2 = glautodrawable.getGL().getGL2();
						int width = glautodrawable.getWidth();
						int height = glautodrawable.getHeight();
						if (width != 0) curWidth = width;
						if (height != 0) curHeight = height;
	
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
						

						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
						gl2.glLoadIdentity();

						/* draw saved triangles*/
						if (triangles.size() != 0) {
							gl2.glBegin(GL.GL_TRIANGLES);
							for (int i = 0; i < triangles.size(); i++) {
								Triangle cur_triangle  = triangles.get(i);
								gl2.glColor3f(cur_triangle.getR(), cur_triangle.getG(), cur_triangle.getB());
								for (int j = 0; j < 3; j++)
									gl2.glVertex2f(cur_triangle.getX()[j], height-cur_triangle.getY()[j]);
							}
							gl2.glEnd();
						}
						
						switch (current_color) {
						case 0:
							gl2.glColor3f(1f, 0f, 0f);
							break;
						case 1:
							gl2.glColor3f(0f, 0.5f, 0f);
							break;
						case 2:
							gl2.glColor3f(0f, 0f, 1f);
							break;
						case 3:
							gl2.glColor3f(0f, 1f, 1f);
							break;
						case 4:
							gl2.glColor3f(1f, 1f, 0f);
							break;
						case 5:
							gl2.glColor3f(1f, 0f, 1f);
							break;
						default:
							break;
						}
						
						/* draw quad in top-right corner signaling current color */
						gl2.glBegin(GL.GL_TRIANGLES);
						gl2.glVertex2f(width-20, height-20);
						gl2.glVertex2f(width-25, height-20);
						gl2.glVertex2f(width-20, height-25);
						gl2.glVertex2f(width-25, height-20);
						gl2.glVertex2f(width-25, height-25);
						gl2.glVertex2f(width-20, height-25);
						gl2.glEnd();
						
						
						/* if only 1 point input */
						if (counter == 1) {
							gl2.glBegin(GL.GL_LINES);
							gl2.glVertex2f(tempX1, height - tempY1);
							gl2.glVertex2f(mouseX, height - mouseY);;
							gl2.glEnd();
						}
						
						/* if only 2 points input */
						if (counter == 2) {
							gl2.glBegin(GL.GL_TRIANGLES);
							gl2.glVertex2f(tempX1, height - tempY1);
							gl2.glVertex2f(tempX2, height - tempY2);
							gl2.glVertex2f(mouseX, height - mouseY);
							gl2.glEnd();
						}						
					}
				});

				final JFrame jframe = new JFrame (
					"Vježba 1 - Prvi program u OpenGL-u");
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
			
			//TODO
			/* dodana funkcija scaleanja trokuta prikladno resizanju prozora */
			public void scaleTriangles(float widthRatio, float heightRatio) {
				for (int i = 0; i < triangles.size(); i++) {
					Triangle cur_triangle = triangles.get(i);
					for (int j = 0; j < 3; j++) {
						cur_triangle.setX(j, cur_triangle.getX()[j]*widthRatio);
						cur_triangle.setY(j, cur_triangle.getY()[j]*heightRatio);
					}
				}
			}
		});			
	}
	

}

class Triangle {
	
	private float[] x;
	private float[] y;
	private float R;
	private float G;
	private float B;
	
	public Triangle(float[] _x, float[] _y, float R, float G, float B) {
		x = new float[3];
		y = new float[3];
		for(int i = 0; i < 3; i++) {
			x[i] = _x[i];
			y[i] = _y[i];
		}				
		this.R = R;
		this.G = G;
		this.B = B;
	}
	
	public float[] getX() {
		return x;
	}	
	public float[] getY() {
		return y;
	}		
	public void setX(int i, float val) {
		x[i] = val;
	}
	public void setY(int i, float val) {
		y[i] = val;
	}
	public float getR() {
		return R;
	}	
	public float getG() {
		return G;
	}
	public float getB() {
		return B;
	}
}
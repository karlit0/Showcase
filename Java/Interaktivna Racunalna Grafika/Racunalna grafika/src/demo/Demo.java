package demo;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

public class Demo {
	static {
	GLProfile.initSingleton();
	}

	public static void main (String [] args) {

		SwingUtilities.invokeLater(new Runnable () {
			@Override
			public void run () {
				GLProfile glprofile = GLProfile.getDefault();
				GLCapabilities glcapabilities = new GLCapabilities(glprofile);
				final GLCanvas glcanvas = new GLCanvas(glcapabilities);
				
				// Reagiranje na pritiske tipki na misu ...
				glcanvas.addMouseListener (new MouseAdapter() {
					@Override
					public void mouseClicked (MouseEvent e) {
					System.out.println("Mis je kliknut na: x=" + e.getX()
						+ ", y=" + e.getY());
					// Napravi nes to
					// . . .
					// Posalji zahtjev za ponovnim crtanjem ...
					glcanvas.display();
					}
				});

				// Reagiranje na pomicanje pokazivaca misa ...
				glcanvas.addMouseMotionListener (new MouseMotionAdapter() {
					@Override
					public void mouseMoved (MouseEvent e) {
					System.out.println("Mis pomaknut na: x=" + e.getX()
						+ ", y=" + e.getY());
						// Napravi nes to
						// ...
						// Posalji zahtjev za ponovnim crtanjem ...
					glcanvas.display();
					}
				});

				// Reagiranje na pritiske tipaka na tipkovnici ...
				glcanvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_R) {
						e.consume();
						// Napravi nesto
						// ...
						// Posalji zahtjev za ponovnim crtanjem ...
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
	
						gl2.glClear(GL.GL_COLOR_BUFFER_BIT);
	
						// draw a triangle filling the window
						gl2.glLoadIdentity();
						gl2.glBegin(GL.GL_TRIANGLES);
						gl2.glColor3f(1, 0, 0);
						gl2.glVertex2f(0, 0);
						gl2.glColor3f(0, 1, 0);
						gl2.glVertex2f(width, 0);
						gl2.glColor3f(0, 0, 1);
						gl2.glVertex2f(width / 2, height);
						gl2.glEnd();
					}
				});

				final JFrame jframe = new JFrame (
					"Primjer prikaza obojanog trokuta");
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
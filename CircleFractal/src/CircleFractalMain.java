import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class CircleFractalMain extends JPanel
		implements ActionListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;

	int screenWidth = 1000;
	int screenHeight = 1000;
	boolean[] keys = new boolean[300];
	boolean[] keysToggled = new boolean[300];
	boolean[] mouse = new boolean[200];
	Circle c;
	ArrayList<Circle> cs;
	boolean init = true;
	Graphics2D g2;

	// ============== end of settings ==================

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.black);
			g2.fillRect(0, 0, screenWidth, screenHeight);
			g2.setColor(Color.white);
		for(Circle c : cs) {
			c.draw(g2);
		}
	}

	public void update() throws InterruptedException {
		for(Circle c : cs) {
			c.update(cs);
		}
	}

	private void init() {
		cs = new ArrayList<Circle>();
		cs.add(new Circle(500,500));
		cs.add(new Circle(700,700));
	}

	// ==================code above ===========================

	@Override
	public void actionPerformed(ActionEvent arg0) {

		try {
			update();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repaint();
	}

	public static void main(String[] arg) {
		@SuppressWarnings("unused")
		CircleFractalMain d = new CircleFractalMain();
	}

	public CircleFractalMain() {
		JFrame f = new JFrame();
		f.setTitle("Circle Fractal");
		f.setSize(screenWidth, screenHeight);
		f.setBackground(Color.BLACK);
		f.setResizable(false);
		f.addKeyListener(this);
		f.addMouseMotionListener(this);
		f.addMouseWheelListener(this);
		f.addMouseListener(this);

		f.add(this);

		t = new Timer(15, this);
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		init();

	}

	Timer t;

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;

	}

	@Override
	public void keyReleased(KeyEvent e) {

		keys[e.getKeyCode()] = false;

		if (keysToggled[e.getKeyCode()]) {
			keysToggled[e.getKeyCode()] = false;
		} else {
			keysToggled[e.getKeyCode()] = true;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouse[e.getButton()] = true;

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouse[e.getButton()] = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouse[e.getButton()] = true;

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

}
class Circle {
	double x,y,r=Double.MIN_VALUE;
	boolean growing = true;
	int thresh = 1;
	double stroke = r/20;

	public Circle(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	void grow() {
		r++;
	}
	void draw(Graphics2D g2) {
		g2.setStroke(new BasicStroke((float) stroke));
		g2.drawOval((int)(x-r), (int)(y-r), (int)(r*2), (int)(r*2));
	}
	boolean intersects(Circle c) {
		return((x-c.x) * (x-c.x) + (y-c.y)*(y-c.y) < (c.r + r + thresh + stroke) * (c.r + r + thresh + c.stroke));
		//return(distance(c.x,c.y,x,y) <= c.r + r + thresh);
	}
	void update(ArrayList<Circle> cs) {
		if(growing) {
		for(Circle c : cs) {
			if(c != this) {
				if(this.intersects(c)) {
					growing = false;
					c.growing = false;
					break;
				}
			}
		}
		grow();
		stroke = r/20;
		}
		
	}
	double distance(double x1,double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
}
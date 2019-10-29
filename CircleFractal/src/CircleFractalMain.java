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
	Camera cam = new Camera(0, 0, 1, screenWidth, screenHeight);
	boolean buffering = true;
	int size = 1000;

	// ============== end of settings ==================

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		g2.fillRect(0, 0, screenWidth, screenHeight);
		g2.setColor(Color.white);
		for (Circle c : cs) {
			c.draw(g2, cam);
		}
	}

	public void update() throws InterruptedException {
		// while(buffering) {
		for (Circle c : cs) {
			c.update(cs);
		}
		//for(int i = 0; i < 1; i++) {
		addCircle(screenWidth, screenHeight);
		//}
		if (cs.size() == size) {
			buffering = false;
		}
		// cam.changeScale(.005f);
		if (keys[87]) {
			cam.yOff += 10 / cam.scale;
		}
		if (keys[83]) {
			cam.yOff -= 10 / cam.scale;
		}
		if (keys[68]) {
			cam.xOff -= 10 / cam.scale;
		}
		if (keys[65]) {
			cam.xOff += 10 / cam.scale;
		}
	}

	// }

	private void init() {
		cs = new ArrayList<Circle>();
		cs.add(new Circle(Math.random() * screenWidth + 1, Math.random() * screenHeight + 1));
		cs.add(new Circle(Math.random() * screenWidth + 1, Math.random() * screenHeight + 1));
	}

	public void addCircle(int screenWidth, int screenHeight) {
		int attempts = 0;
		while (attempts < 1000) {
			double x = cam.toXMap(Math.random() * screenWidth + 1);
			double y = cam.toYMap(Math.random() * screenHeight + 1);
			// if !in a circle
			boolean valid = true;
			for (Circle c : cs) {
				if (new Circle(x, y).intersects(c, c.growRate)) {
					valid = false;
				}
			}
			if (valid) {
				cs.add(new Circle(x, y));
				break;
			}
			attempts++;
		}
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
		
		init();

		t = new Timer(15, this);
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

		

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

		if (e.getWheelRotation() < 0) {
			cam.changeScale(.1f);
		} else {
			cam.changeScale(-.1f);
		}
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
	double x, y, r = Double.MIN_VALUE;
	boolean growing = true;
	double thresh = 0;
	double stroke = 1;// r / 20;
	double growRate = 1f;

	public Circle(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}

	void grow() {
		r += growRate;

	}

	void draw(Graphics2D g2, Camera cam) {
		// g2.setStroke(new BasicStroke((float) (stroke * cam.scale)));
		g2.drawOval(cam.toXScreen((int) (x - r)), cam.toYScreen((int) (y - r)), (int) (r * 2 * cam.scale),
				(int) (r * 2 * cam.scale));
	}

	boolean intersects(Circle c, double threshHold) {
		return ((x - c.x) * (x - c.x) + (y - c.y) * (y - c.y) <= (c.r + r + threshHold + stroke)
				* (c.r + r + threshHold + stroke));
		// return(distance(c.x,c.y,x,y) <= c.r + r + thresh);
	}

	boolean intersects(Circle c) {
		return ((x - c.x) * (x - c.x) + (y - c.y) * (y - c.y) <= (c.r + r + thresh + stroke)
				* (c.r + r + thresh + stroke));
		// return(distance(c.x,c.y,x,y) <= c.r + r + thresh);
	}

	void update(ArrayList<Circle> cs) {
		double distS = 1;
		if (growing) {
			for (Circle c : cs) {
				if (c != this) {
					distS = Math.sqrt((x - c.x) * (x - c.x) + (y - c.y) * (y - c.y)) - (r + c.r);
					break;
				}
			}

			for (Circle c : cs) {
				if (c != this) {

					if (this.intersects(c)) {
						growing = false;
						// c.growing = false;
						// break;
					}

					double dist = Math.sqrt((x - c.x) * (x - c.x) + (y - c.y) * (y - c.y)) - (r + c.r);
					double tempS = dist - c.r;
					if (dist < distS) {
						distS = dist;
					}

				}
			}
			growRate = (distS / 20);
			grow();
			// stroke = r / 20;
		}

	}

	double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

}

class Camera {
	double xOff, yOff, screenW, screenH;
	double scale;
	Point center;
	float scaleNotches = 0;
	float targetScale;

	public Camera(int xOff, int yOff, double scale, int screenW, int screenH) {
		super();
		this.xOff = xOff;
		this.yOff = yOff;
		this.scale = scale;
		this.screenW = screenW;
		this.screenH = screenH;
		center = new Point(screenW / 2, screenH / 2);
	}

	public void focus(Point p) {
		xOff = screenW / 2 - p.x;
		yOff = screenH / 2 - p.y;
	}

	public void changeScale(float notches) {
		scaleNotches += notches;
		scale = Math.pow(2, scaleNotches);
	}

	public int toXScreen(int x) {
		int dx = (int) ((x + xOff - center.x) * scale);
		return (center.x + dx);
	}

	public int toYScreen(int y) {
		int dy = (int) ((y + yOff - center.y) * scale);
		return (center.y + dy);
	}

	public double toXMap(double x) {
		return ((x - center.x) / scale) + center.x - xOff;
	}

	public double toYMap(double y) {
		return ((y - center.y) / scale) + center.y - yOff;
	}
}

class Point {
	int x, y;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public double distanceTo(Point p2) {
		return Math.sqrt((this.x - p2.x) * (this.x - p2.x) + (this.y - p2.y) * (this.y - p2.y));
	}

	public double angleTo(Point p2) {
		try {
			return Math.atan2(this.y - p2.y, this.x - p2.x);
		} catch (Exception e) {

		}
		return 0;
	}
}
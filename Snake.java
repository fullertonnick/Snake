
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;

public class Snake implements ActionListener, KeyListener {

	// Data
	public static Snake snake;
	public JFrame jframe;
	public RenderPanel renderPanel;
	public CopyOnWriteArrayList<Point> snakeParts = new CopyOnWriteArrayList<Point>();
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3, SCALE = 10;
	public int direction = DOWN, score, tailLength = 10;
	public Point head, cherry;
	public Random random;
	public boolean over = false, paused;
	public Dimension dim;
	int w = 600, h = 650;

	//constructor
	public Snake() {
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		jframe = new JFrame("Snake");
		jframe.setVisible(true);
		jframe.setSize(w, h);
		jframe.setResizable(false);
		jframe.setLocation(dim.width / 2 - jframe.getWidth() / 2, dim.height / 2 - jframe.getHeight() / 2);
		jframe.add(renderPanel = new RenderPanel());
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.addKeyListener(this);
		startGame();
	}

	// more data
	int fps = 12;
	int skip = 1000 / fps;
	long next = System.currentTimeMillis();
	int sleep = 0;
	boolean run = true;

	// FPS Method
	private void fixedFPS() {
		update();
		renderPanel.repaint();

		next += skip;
		sleep = (int) (next - System.currentTimeMillis());
		if (sleep >= 0) {
			try {
				Thread.sleep(sleep);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Start Game Method
	public void startGame() {
		over = false;
		paused = false;
		score = 0;
		tailLength = 14;
		direction = DOWN;
		head = new Point(0, -1);
		random = new Random();
		snakeParts.clear();
		cherry = new Point(random.nextInt(h / SCALE - 20), random.nextInt(w / SCALE - 20));
		System.out.println("x" + cherry.x + " | y" + cherry.y);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true)
					fixedFPS();
			}
		}).start();
	}

	//updates position and movements
	//Accepts key presses
	void update() {
		if (head != null && !over && !paused) {
			snakeParts.add(new Point(head.x, head.y));

			if (direction == UP) {
				if (head.y - 1 >= 0 && noTailAt(head.x, head.y - 1)) {
					head = new Point(head.x, head.y - 1);
				} else {
					over = true;
				}
			}

			if (direction == DOWN) {
				if (head.y + 1 < h / SCALE && noTailAt(head.x, head.y + 1)) {
					head = new Point(head.x, head.y + 1);
				} else {
					over = true;
				}
			}

			if (direction == LEFT) {
				if (head.x - 1 >= 0 && noTailAt(head.x - 1, head.y)) {
					head = new Point(head.x - 1, head.y);
				} else {
					over = true;
				}
			}

			if (direction == RIGHT) {
				if (head.x + 1 < w / SCALE && noTailAt(head.x + 1, head.y)) {
					head = new Point(head.x + 1, head.y);
				} else {
					over = true;
				}
			}

			if (snakeParts.size() > tailLength) {
				snakeParts.remove(0);
			}

			if (cherry != null) {
				if (head.equals(cherry)) {
					score += 10;
					tailLength++;
					cherry.setLocation(random.nextInt(h / SCALE - 20), random.nextInt(w / SCALE - 20));
					System.out.println("new location x" + cherry.x + " | y" + cherry.y);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {}

	public boolean noTailAt(int x, int y) {
		for (Point point : snakeParts) {
			if (point.equals(new Point(x, y))) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		snake = new Snake();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int i = e.getKeyCode();

		if ((i == KeyEvent.VK_A || i == KeyEvent.VK_LEFT) && direction != RIGHT) {
			direction = LEFT;
		}

		if ((i == KeyEvent.VK_D || i == KeyEvent.VK_RIGHT) && direction != LEFT) {
			direction = RIGHT;
		}

		if ((i == KeyEvent.VK_W || i == KeyEvent.VK_UP) && direction != DOWN) {
			direction = UP;
		}

		if ((i == KeyEvent.VK_S || i == KeyEvent.VK_DOWN) && direction != UP) {
			direction = DOWN;
		}

		if (i == KeyEvent.VK_SPACE) {
			if (over) {
				startGame();
			} else {
				paused = !paused;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}

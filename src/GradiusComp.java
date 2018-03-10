import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

import javax.swing.JComponent;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class GradiusComp extends JComponent {

	// useful constants
	private final static int GAME_TICK = 1000 / 60;
	private final static int ASTEROID_MAKE_TICK = 1000 / 4;

	private final static int SHIP_INIT_X = 10;
	private final static int SHIP_INIT_Y = Gradius.HEIGHT / 3;
	private final static int SHIP_VEL_BASE = 2;
	private final static int SHIP_VEL_FAST = 4;
	private ArrayList<Timer> gameTick;

	// collection of asteroid
	private Collection<Asteroid> roids;
	private Collection<Asteroid> stars;

	private Ship ship;

	public GradiusComp() {
		// Array List of timer
		gameTick = new ArrayList<Timer>();
		gameTick.add(new Timer(GAME_TICK, (a) -> {
			update();
		}));

		gameTick.add(new Timer(ASTEROID_MAKE_TICK, (a) -> {
			makeAsteroid();
		}));

		gameTick.add(new Timer(ASTEROID_MAKE_TICK, (a) -> {
			makeStars();
		}));

		// Event Listener
		addKeyListener(new ShipKeyListener());
		roids = new HashSet<Asteroid>();
		stars = new HashSet<Asteroid>();

	}

	public void makeStars() {
		stars.add(AsteroidFactory.makeStars());
	}

	/** Adds asteroid to the collection **/
	public void makeAsteroid() {
		roids.add(AsteroidFactory.makeAsteroid());
	}

	/** Void method that Updates and repaints **/
	public void update() {
		ship.move();
		stars.stream().sequential().forEach(Asteroid::move);
		stars.removeIf(a -> a.isOutOfBounds());
		roids.stream().sequential().forEach(Asteroid::move);
		roids.removeIf(a -> a.isOutOfBounds());
		// if any of the asteroid hit the ship
		if (roids.stream().parallel().anyMatch(a -> a.intersects(ship))) {
			gameTick.stream().parallel().forEach((a) -> {
				a.stop(); // stop the game

				});
		}
		;
		repaint();
	}

	/** Paints the component */
	public void paintComponent(Graphics g) {
		requestFocusInWindow();
		int x, y, r;

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g2.fillRect(0, 0, 900, 700);

		for (int i = 0; i < 70; i++) {
			// generate a random point to be the center of the circle
			// restrict it to the size of the applet window

			x = (int) (Math.random() * 900);
			y = (int) (Math.random() * 1200);

			// select a random size of the radius (make it in the range 10..40)
			r = (int) (Math.random() * 2);

			g.setColor(Color.white);
			// draw the circle
			g.fillOval(x - r, y - r, 2 * r, 2 * r);
		}
		paintComponent(g2);
		paintComponentText(g2);

	}

	private void paintComponent(Graphics2D g2) {
		g2.setBackground(Color.BLACK);

		ship.draw(g2);

		stars.stream().sequential().forEachOrdered(a -> a.drawStars(g2));
		roids.stream().sequential().forEachOrdered(a -> a.draw(g2));

	}

	private void paintComponentText(Graphics2D g2) {
		// GAME OVER TEXT
		for (Timer t : gameTick) {
			if (!t.isRunning()) {
				g2.setColor(Color.RED);
				Font myFont = new Font("Courier New", 1, 25);
				g2.setFont(myFont);
				g2.drawString("GAME OVER", 360, 330);
			}
		}
	}

	/** This method starts the clock **/
	public void start() {
		// initializing ship
		ship = new ShipImpl(SHIP_INIT_X, SHIP_INIT_Y, new Rectangle2D.Double(0,
				0, getWidth(), getHeight()));

		// setting startbounds
		AsteroidFactory.setStartBounds(new Rectangle(0, 0, Gradius.HEIGHT,
				Gradius.HEIGHT + 250));
		// setting MoveBounds
		AsteroidFactory.setMoveBounds(new Rectangle(0, 0, Gradius.HEIGHT + 200,
				Gradius.HEIGHT));

		gameTick.stream().forEach((a) -> a.start());

	}

	/**
	 * Some of the following parts has been taken from a in-class exercise /
	 * slides
	 */
	private class ShipKeyListener extends KeyAdapter {

		private boolean up;
		private boolean down;
		private boolean left;
		private boolean right;
		private boolean state;

		private void setVelocity(KeyEvent e) {
			setDirection(e);
			final int dp = e.isShiftDown() ? SHIP_VEL_FAST : SHIP_VEL_BASE;

			int dx = 0;
			int dy = 0;

			if (up && !down) {
				dy = -dp;
			} else if (down && !up) {
				dy = dp;
			}
			if (left && !right) {
				dx = -dp;
			} else if (right && !left) {
				dx = dp;
			}
			ship.setVelocity(dx, dy);

		}

		/**
		 * Sets the direction of th e8 cardinal direction according to key
		 * pressed
		 * 
		 * @param KeyEvent
		 *            e
		 * **/
		private void setDirection(KeyEvent e) {

			switch (e.getID()) {
			case KeyEvent.KEY_PRESSED:
				state = true;
				break;
			case KeyEvent.KEY_RELEASED:
				state = false;
				break;
			default:
				return;
			}
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_KP_UP:
				up = state;
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_KP_DOWN:
				down = state;
				break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_KP_LEFT:
				left = state;
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_KP_RIGHT:
				right = state;
				break;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {

			setVelocity(e);

		}

		@Override
		public void keyReleased(KeyEvent e) {

			setVelocity(e);
		}

	}

}

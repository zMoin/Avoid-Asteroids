import java.awt.Rectangle;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class AsteroidFactory {

	private final static int ASTEROID_SIZE_MIN = 10;
	private final static int ASTEROID_SIZE_MAX = 40;
	private final static int ASTEROID_VEL_MIN = 1;
	private final static int ASTEROID_VEL_MAX = 4;

	private static Rectangle startBounds;
	private static Rectangle moveBounds;

	private AsteroidFactory() {
	}

	public static void setStartBounds(Rectangle r) {
		startBounds = r;
	}

	public static void setMoveBounds(Rectangle r) {
		moveBounds = r.union(startBounds);
		moveBounds.width += 1;
	}

	/** Makes a new star (as an asteroid for now)**/
	public static Asteroid makeStars() {
		Asteroid str = new StarImpl((int) startBounds.getHeight(), random(0,
				(int) startBounds.getWidth()), random(1, 5), random(1, 5),
				random(ASTEROID_VEL_MIN, ASTEROID_VEL_MAX));
		return str;
	}

	/** Makes a new asteroid **/
	public static Asteroid makeAsteroid() {
		Asteroid as = new AsteroidImpl((int) startBounds.getHeight(), random(0,
				(int) startBounds.getWidth()), random(ASTEROID_SIZE_MIN,
				ASTEROID_SIZE_MAX),
				random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX), random(
						ASTEROID_VEL_MIN, ASTEROID_VEL_MAX));
		return as;
	}

	/**
	 * Random number generator
	 * 
	 * @param min
	 *            : smaller of two numbers
	 * @param max
	 *            : bigger of two numbers
	 * @return int : a random value
	 * 
	 * **/
	private static int random(int min, int max) {
		if (max - min == 0) {
			return min;
		}
		Random rand = java.util.concurrent.ThreadLocalRandom.current();
		return min + rand.nextInt(max + 1);
	}

	/** A class that implements AsteroidImpl */
	private static class StarImpl extends SpriteImpl implements Asteroid {
		private final static Color COLOR = Color.WHITE;

		/**
		 * AsteroidImp Implementation
		 * 
		 * @param x
		 *            x coordinate
		 * @param y
		 *            y coordinate
		 * @param w
		 *            width
		 * @param h
		 *            height
		 * @param v
		 *            velocity
		 * 
		 * ***/
		public StarImpl(int x, int y, int w, int h, float v) {
			super(new Ellipse2D.Double(x, y, w, h), moveBounds, false, COLOR);
			// set the velocity in accordance to v
			setVelocity(-(random((int) (v * 0.5), (int) (v * 0.5))), 0);

		}

	}

	/** A class that implements AsteroidImpl */
	private static class AsteroidImpl extends SpriteImpl implements Asteroid {
		private final static Color COLOR = Color.BLACK;

		/**
		 * AsteroidImp Implementation
		 * 
		 * @param x
		 *            x coordinate
		 * @param y
		 *            y coordinate
		 * @param w
		 *            width
		 * @param h
		 *            height
		 * @param v
		 *            velocity
		 * 
		 * ***/
		public AsteroidImpl(int x, int y, int w, int h, float v) {
			super(new Ellipse2D.Double(x, y, w, h),
					moveBounds, false, COLOR);
			// set the velocity in accordance to v
			setVelocity(-v, 0);

		}

	}
}

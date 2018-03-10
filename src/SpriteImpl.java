import java.awt.*;
import java.awt.geom.*;

public abstract class SpriteImpl implements Sprite {

	// drawing
	private Shape shape;
	private final Color border;
	private final Color fill;

	// movement
	private float dx, dy;
	private final Rectangle2D bounds;
	private final boolean isBoundsEnforced;

	protected SpriteImpl(Shape shape, Rectangle2D bounds,
			boolean boundsEnforced, Color border, Color fill) {
		this.shape = shape;
		this.bounds = bounds;
		this.isBoundsEnforced = boundsEnforced;
		this.border = border;
		this.fill = fill;
	}

	protected SpriteImpl(Shape shape, Rectangle2D bounds,
			boolean boundsEnforced, Color fill) {
		this(shape, bounds, boundsEnforced, null, fill);
	}

	// shape getter
	public Shape getShape() {
		return shape;
	}

	/**
	 * Sets the velocity of the entity
	 * 
	 * @param dx
	 *            :
	 * */
	public void setVelocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void move() {
		// move the shapes in accordance to the bounds
		Shape oldShape = getShape();
		shape = AffineTransform.getTranslateInstance(dx, dy)
				.createTransformedShape(shape);
		if (this.isBoundsEnforced && !isInBounds()) {
			shape = oldShape;
		}
	}

	/**
	 * This method determines if the shape is out of bounds
	 * 
	 * @return boolean
	 * 
	 * */

	public boolean isOutOfBounds() {
		return (!isInBounds() && shape.intersects(AsteroidFactory
				.makeAsteroid().getShape().getBounds2D()));

	}

	/**
	 * These methods determine if the shape is within the bounds
	 * 
	 * @param bound
	 *            : the bound of the shape
	 * @param shape
	 *            : the shape of the bound
	 * @return boolean
	 * 
	 * **/
	public boolean isInBounds() {
		return isInBounds(bounds, shape);
	}

	private static boolean isInBounds(Rectangle2D bounds, Shape s) {
		// return if the bound is completely within the frame
		return (bounds.contains(bounds.getX(), bounds.getY(),
				bounds.getWidth(), bounds.getHeight()) && bounds.contains(s
				.getBounds()));

	}

	/** Draws the shape **/
	public void draw(Graphics2D g2) {
		
		g2.setColor(this.fill);
		g2.fill(this.shape);
		g2.setColor(Color.GREEN);
		g2.draw(this.shape);
		
	}
	
	public void drawStars(Graphics2D g2){
		
		
		g2.setColor(this.fill);
		g2.fill(this.shape);
		g2.setColor(this.border);
		g2.draw(this.shape);
	}
	/**
	 * Determines the intersection
	 * 
	 * @param other
	 *            (Sprite)
	 * @param area
	 *            a
	 * @param Area
	 *            b
	 * @param boolean
	 * **/
	public boolean intersects(Sprite other) {
		return intersects(other.getShape());
	}

	private boolean intersects(Shape other) {
		// returns true iff the shapes intersect
		return intersects(new Area(shape.getBounds()),
				new Area(other.getBounds()));
	}

	private static boolean intersects(Area a, Area b) {
		// returns true iff area a intersects area b
		a.intersect(b);
		return !a.isEmpty();

	}
}

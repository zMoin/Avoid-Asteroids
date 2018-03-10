import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

public class ShipImpl extends SpriteImpl implements Ship {

	private final static Color FILL = Color.GREEN;
	private final static Color BORDER = Color.BLACK;

	private final static int HEIGHT = 20;
	private final static int WIDTH = HEIGHT;

	public ShipImpl(int x, int y, Rectangle2D moveBounds) {
		//passing the parameters to super
		super(new Polygon(new int[] { x, x, x + WIDTH }, new int[] { y,
				y + HEIGHT, y + HEIGHT / 2 }, 3), moveBounds, true, BORDER,
				FILL);

	}
}

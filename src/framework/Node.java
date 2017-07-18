package framework;

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Interface for component creation
 */
public interface Node extends Cloneable, Serializable {

	/**
    Draw the node.
    @param g2 the graphics context
	*/
	void draw(Graphics2D g2);
	
	/**
	 * Moves a node to a new position (x+dx,y+dy)
	 * @param dx how far a node will be moved on a graph in x
	 * @param dy the new y-position in a graph for a node
	 */
	void translate(double dx, double dy);
	/**
    Gets the smallest rectangle that bounds this edge.
    The bounding rectangle contains all labels.
    @return the bounding rectangle
	 */
	Rectangle2D getBounds();
	/**
    @return copy of node
	 */
	Object clone();
	/**
	 * Takes a point(aPoint) and checks for best point on the
	 * node in aPoints direction.
	 * @param point that determines what direction 
	 * return point is placed
     * @return point on node in aPoints direction
	 */
	Point2D getConnectionPoint(Point2D aPoint);
	/**
	 * Gets the child nodes if there are any.
    @return children of node or null
	 */
	List<Node> getNodes();
	/**
	 * String information about node
    @return info important to the user
	 */
	String toString();
	/**
	 * get a button that is used to represent the node
	 * @param size of the image icon (x,y)
     * @return ImageIcon
	 */
	ImageIcon getButtonIcon(int size);
	/**
	 * Checks to see if node wants to be added in the cart.
	 * @param size of image(both sides)
     * @return true/false
	 */
	boolean cartable();
}

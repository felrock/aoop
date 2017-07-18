
package app;

import java.awt.*;
import java.awt.geom.*;
import java.util.List;

import javax.swing.ImageIcon;

import framework.*;

/**
 * A node that is used by other parent nodes and for pins/holes. 
 */
public class ComponentNode implements Node {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1688448327690657182L;
	public ComponentNode(Color aColor) {
		x = 0;
		y = 0;
		color = aColor;
		body = new Ellipse2D.Double(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException exception) {
			return null;
		}
	}

	public void draw(Graphics2D g2) {
		g2.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(color);
		g2.fill(body);
		g2.setColor(border_color);
		g2.draw(body);
	}

	public void translate(double dx, double dy) {
		x += dx;
		y += dy;
		body = new Ellipse2D.Double(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
	}

	public Rectangle2D getBounds() {
		return new Rectangle2D.Double(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
	}
	
	@Override
	public Point2D getConnectionPoint(Point2D aPoint) {
		double centerX = x + DEFAULT_SIZE / 2;
		double centerY = y + DEFAULT_SIZE / 2;
		double dx = aPoint.getX() - centerX;
		double dy = aPoint.getY() - centerY;
		double distance = Math.sqrt(dx * dx + dy * dy);
		if (distance == 0) return aPoint;
		else {
			return new Point2D.Double(
				centerX + dx * (DEFAULT_SIZE / 2) / distance,
				centerY + dy * (DEFAULT_SIZE / 2) / distance);
		}
	}
	/**
	 * gets the size (diameter) of the node
	 * @return component diameter
	 * */
	public double getSize(){
		return DEFAULT_SIZE;
	}
	/**
	 * gets node coordinate in x plane
	 * @return coordinate x
	 * */
	public double getX(){
		return x;
	}
	/**
	 * gets node coordinate in y plane
	 * @return coordinate y
	 * */
	public double getY(){
		return y;
	}
	@Override
	public List<Node> getNodes() {
		return null;
	}
	@Override
	public ImageIcon getButtonIcon(int size) {
		return null;
	}
	@Override
	public boolean cartable() {
		return false;
	}
	/**
	 * gets the shape which is a Ellipse2D
	 * @return componentNode sphere
	 * */
	public Shape getBody() {
		return body;
	}
	private double x;
	private double y;
	private Color color;
	private Shape body;
	private final static Color border_color = Color.GREEN;
	private static final int DEFAULT_SIZE = 10;

}
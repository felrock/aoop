package app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import framework.*;
/**
 * Capacitor component, see Node interface for documentation for some 
 * methods. Parent node
 * 
 * DEFAULT_SIZE: size of the gate
 * TEXT_ALIGN: padding for text which indicates what gate it is
 * 
 * */
@SuppressWarnings("serial")
public class Capacitor implements Node {
	public Capacitor(int farad,String image){
		x = 0;
		y = 0;
		capacitance = farad;
		this.image = image;
		legs = new ArrayList<Node>();
		ComponentNode output = new ComponentNode(Color.BLACK);
		output.translate(0, 0);
		ComponentNode input1 = new ComponentNode(Color.BLACK);
		input1.translate(0,0);
		legs.add(output);
		legs.add(input1);
		legs.get(0).translate(DEFAULT_SIZE/2,-DEFAULT_SIZE/4);
		legs.get(1).translate(-DEFAULT_SIZE, -DEFAULT_SIZE/4);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		body = new Line2D.Double(x-DEFAULT_SIZE/3,y-DEFAULT_SIZE/2, x-DEFAULT_SIZE/3, y+DEFAULT_SIZE/2);
		body2 = new Line2D.Double(x+DEFAULT_SIZE/3,y-DEFAULT_SIZE/2, x+DEFAULT_SIZE/3, y+DEFAULT_SIZE/2);
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(degrees), x, y);
		body = at.createTransformedShape(body);
		body2 = at.createTransformedShape(body2);
		
		g2.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.GRAY);
		g2.drawString(MetricPrefix.toString(capacitance)+" F", (int)(x-TEXT_ALIGN), (int)(y-TEXT_ALIGN));
		g2.setColor(color);
		g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g2.draw(body);
		g2.draw(body2);
		g2.setStroke(new BasicStroke());
		for(Node n : legs){
			n.draw(g2);
		}
	}
	public Object clone() {
		Capacitor g = new Capacitor(20,image);
		g.translate(x, y);
		return g;
	}
	@Override
	public void translate(double dx, double dy) {
		x += dx;
		y += dy;
		for(Node n : legs){
			n.translate(dx, dy);
		}
	}

	@Override
	public Rectangle2D getBounds() {
		Rectangle2D size = new Rectangle2D.Double(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
		for(Node n : legs){
			size = size.createUnion(n.getBounds());
		}
		return size;
	}

	@Override
	public Point2D getConnectionPoint(Point2D aPoint) {
		return legs.get(0).getConnectionPoint(aPoint);
	}
	@Override
	public List<Node> getNodes() {
		return legs;
	}
	@Override
	public ImageIcon getButtonIcon(int size) {
		return new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
	}
	@Override
	public String toString(){
		return "Capacitor: "+MetricPrefix.toString(capacitance)+" F";
	}
	/**
	 * sets the capacitance to farad value
	 * @param new value for capacitance
	 * @precondition farad > 0 ( you can't have negative capacitance)
	 * */
	public void setCapacitance(double farad){
		capacitance = farad;
	}
	/**
	 * gets the capacitance value
	 * @return capacitor strength
	 * */
	public double getCapacitance(){
		return capacitance;
	}
	/**
	 * Rotates the capacitor 90 degrees, using the AffineTransom library. 
	 * */
	public void rotate(){
		degrees = (90+degrees) % 180;
		AffineTransform at = new AffineTransform();
		at.rotate(Math.PI/2, x, y);
		body = at.createTransformedShape(body);
		body2 = at.createTransformedShape(body2);
		for(Node n: legs){
			Point2D t = new Point2D.Double(((ComponentNode)n).getX(),((ComponentNode)n).getY());
			at.transform(t, t);
			ComponentNode c = new ComponentNode(Color.BLACK);
			c.translate(t.getX() - DEFAULT_SIZE/2, t.getY());
			legs.set(legs.indexOf(n), c);
		}
	}
	@Override
	public boolean cartable() {
		return true;
	}
	private List<Node> legs;
	private double capacitance;
	private double x;
	private double y;
	private double degrees;
	private Shape body;
	private Shape body2;
	private String image;
	private final static Color color = Color.BLACK;
	private static final double DEFAULT_SIZE = 20;
	private static final double TEXT_ALIGN = 25;
}

package app;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import framework.*;
/**
 * Resistor component, see Node interface for documentation for some 
 * methods. Parent node
 * 
 * DEFAULT_SIZE: size of the gate
 * TEXT_ALIGN: padding for text which indicates what gate it is
 * 
 * */
@SuppressWarnings("serial")
public class Resistor implements Node{
	public Resistor(double ohms, String image){
		x = 0;
		y = 0;
		resistance = ohms;
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
		g2.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.GRAY);
		g2.drawString(MetricPrefix.toString(resistance)+" \u2126", (int)(x-TEXT_ALIGN), (int)(y-TEXT_ALIGN));
		g2.setColor(color);
		g2.fill(body);
		g2.setColor(border_color);
		g2.draw(body);
		for(Node n : legs){
			n.draw(g2);
		}
	}
	
	public Object clone() {
		Resistor r = new Resistor(resistance,image);
		r.translate(x, y);
		return r;
	}
	
	@Override
	public void translate(double dx, double dy) {
		x += dx;
		y += dy;
		for(Node n : legs){
			n.translate(dx, dy);
		}
		body = new Rectangle2D.Double(x - DEFAULT_SIZE/2, y- DEFAULT_SIZE/2, DEFAULT_SIZE, DEFAULT_SIZE);
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
	public String toString(){
		return "Resistor: "+MetricPrefix.toString(resistance)+" \u2126";
	}
	/**
	 * sets the capacitance to ohms value
	 * @param new value for capacitance
	 * @precondition ohms > 0 ( you can't have negative capacitance)
	 * */
	public void setResistance(double ohms){
		resistance = ohms;
	}
	/**
	 * gets the capacitance value
	 * @return capacitor strength
	 * */
	public double getResistance(){
		return resistance;
	}
	@Override
	public ImageIcon getButtonIcon(int size) {
		return new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
	}
	/**
	 * Rotates the capacitor 90 degrees, using the AffineTransom library. 
	 * */
	public void rotate(){
		AffineTransform at = new AffineTransform();
		at.rotate(Math.PI/2, x, y);
		body = at.createTransformedShape(body);
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
	private double resistance;
	private double x;
	private double y;
	private Shape body;
	private String image;
	private final static Color color = Color.BLACK;
	private final static Color border_color = Color.GREEN;
	private static final double DEFAULT_SIZE = 20;
	private static final double TEXT_ALIGN = 25;

}

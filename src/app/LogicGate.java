package app;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import framework.*;
/**
 * LogicGate component, see Node interface for documentation for some 
 * methods. Parent node
 * 
 * DEFAULT_SIZE: size of the gate
 * ANCHOR_SIZE: size of the childNodes
 * TEXT_ALIGN: padding for text which indicates what gate it is
 * 
 * */
@SuppressWarnings("serial")
public class LogicGate implements Node {
	public LogicGate(Color c, int id,String image){
		x = 0;
		y = 0;
		main_color = c;
		this.image = image;
		gateType = Gates.AND;
		legs = new ArrayList<Node>();
		for(int i=0; i < 3; i++){
			legs.add(new ComponentNode(Color.BLACK));
		}
		legs.get(0).translate((DEFAULT_SIZE/2),(-DEFAULT_SIZE/2)+ANCHOR_SIZE);
		legs.get(1).translate(-DEFAULT_SIZE + ANCHOR_SIZE/2, -DEFAULT_SIZE/3 -ANCHOR_SIZE/2);
		legs.get(2).translate(-DEFAULT_SIZE + ANCHOR_SIZE/2, DEFAULT_SIZE/3 -ANCHOR_SIZE/2);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.GRAY);
		g2.drawString(gateType.toString(), (int)(x-TEXT_ALIGN), (int)(y-TEXT_ALIGN));
		
		g2.setColor(main_color);
		g2.fill(body);
		g2.setColor(border_color);
		g2.draw(body);
		for(Node n : legs){
			n.draw(g2);
		}
	}
	@Override
	public List<Node> getNodes(){
		return legs;
	}
	
	@Override
	public void translate(double dx, double dy) {
		x += dx;
		y += dy;
		for(Node n : legs){
			n.translate(dx, dy);
		}
		body = new Rectangle2D.Double(x-DEFAULT_SIZE/2,y-DEFAULT_SIZE/2,DEFAULT_SIZE,DEFAULT_SIZE);
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
	public Object clone() {
		LogicGate g = new LogicGate(main_color, id,image);
		g.translate(x, y);
		return g;
	}
	
	@Override
	public Point2D getConnectionPoint(Point2D aPoint) {
		return legs.get(0).getConnectionPoint(aPoint);
	}
	@Override
	public ImageIcon getButtonIcon(int size) {
		return new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
	}
	@Override
	public String toString(){
		return gateType+" gate";
	}
	/**
	 * set the gate to fixed enum Gates
	 * @precondition gate != null
	 * @param new gate value
	 * */
	public void setGate(Gates gate){
		gateType = gate;
	}
	/**
	 * returns gate value
	 * @precondition gate != null
	 * @return gate value
	 * */
	public Gates getGate(){
		return gateType;
	}
	/**
	 * Rotates the logicgate 90 degrees, using the AffineTransom library. 
	 * */
	public void rotate(){
		AffineTransform at = new AffineTransform();
		at.rotate(Math.PI/2, x, y);
		for(Node n: legs){
			Point2D t = new Point2D.Double(((ComponentNode)n).getX(),((ComponentNode)n).getY());
			at.transform(t, t);
			ComponentNode c = new ComponentNode(Color.BLACK);
			c.translate(t.getX() - DEFAULT_SIZE/2 + ANCHOR_SIZE/2, t.getY());
			legs.set(legs.indexOf(n), c);
		}
	}
	@Override
	public boolean cartable() {
		return true;
	}
	private double x;
	private double y;
	private Gates gateType;
	private Color main_color;
	private int id;
	private List<Node> legs;
	private String image;
	private Shape body;
	private static final double DEFAULT_SIZE = 30;
	private static final double ANCHOR_SIZE = 10;
	private static final double TEXT_ALIGN = 35;
	private final static Color border_color = Color.GREEN;

}

package app;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;

/**
 * Hole , see Node interface and ComponentNode class for documentation for some 
 * methods. Multilayer pin, no child nodes.
 * */
@SuppressWarnings("serial")
public class Hole extends ComponentNode{
	public Hole(Color c, String image){
		super(c);
		this.c = c;
		this.image = image;
		label = "";
	}
	@Override
	public ImageIcon getButtonIcon(int size) {
		return new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
	}

	public void draw(Graphics2D g2) {
		g2.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.GRAY);
		g2.drawString(label, (int)(getX()-25), (int)(getY()-5));
		g2.setColor(c);
		g2.fill(this.getBody());
		g2.setColor(border_color);
		g2.draw(this.getBody());
	}
	@Override
	public String toString(){
		return label;
	}
	/**
	 * set new label, used in toString
	 * @precondition labe != null
	 * @param new label
	 * */
	public void setLabel(String label){
		this.label = label;
	}
	/**
	 * get label
	 * @precondition label != null
	 * @return label
	 * */
	public String getLabel(){
		return label;
	}
	private Color c;
	private String label;
	private String image;
	private final static Color border_color = Color.GREEN;
	
}
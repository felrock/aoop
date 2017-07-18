package app;

import java.awt.*;
import java.awt.geom.*;

/**
   An edge as a line
*/
@SuppressWarnings("serial")
public class LineEdge extends AbstractEdge{
   /**
    * draw the line 
    * @precondition g2 != null
    * */
   public void draw(Graphics2D g2){
		g2.setRenderingHint(
			    RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
	   g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
	   g2.setPaint(Color.RED);
	   g2.draw(getConnectionPoints());
   }
   /**
	* Check if a point is in the vicinity of the edge line. With MAX_DIST
	* as padding.
	* @precondition aPoint != null
	* @param point to check
	* @return if point is close enoguh or not(true/false)
	* */
   public boolean contains(Point2D aPoint){
	   final double MAX_DIST = 4;
	   return getConnectionPoints().ptSegDist(aPoint) < MAX_DIST;
   }
}
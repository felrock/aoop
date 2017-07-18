package app;

import java.awt.geom.*;

import framework.Edge;
import framework.Node;

/**
   A class that supplies convenience implementations for
   a number of methods in the Edge interface type.
*/
@SuppressWarnings("serial")
public abstract class AbstractEdge implements Edge
{
   public Object clone()
   {
      try
      {
         return super.clone();
      }
      catch (CloneNotSupportedException exception)
      {
         return null;
      }
   }
   /**
    * connect two nodes
    * @param two points that create the edge line
    * @precondition s != null && e != null
    * */
   public void connect(Node s, Node e)
   {
      start = s;
      end = e;
   }
   /**
    * Returns start node
    * @precondition start !=null
    * */
   public Node getStart()
   {
      return start;
   }
   /**
    * Returns end node
    * @precondition end !=null
    * */
   public Node getEnd()
   {
      return end;
   }
   /**
    * return boundary around the edge line.
    * @precondition start != null && end != null
    * @return edge as a rectangle2D
    * */
   public Rectangle2D getBounds()
   {
      Line2D conn = getConnectionPoints();
      Rectangle2D r = new Rectangle2D.Double();
      r.setFrameFromDiagonal(conn.getX1(), conn.getY1(),
         conn.getX2(), conn.getY2());
      return r;
   }
   /**
    * gets a line2d between start and end
    * @precondition start != null && end != null
    * @return edge as line2D
    * */
   public Line2D getConnectionPoints()
   {
      Rectangle2D startBounds = start.getBounds();
      Rectangle2D endBounds = end.getBounds();
      Point2D startCenter = new Point2D.Double(
         startBounds.getCenterX(), startBounds.getCenterY());
      Point2D endCenter = new Point2D.Double(
         endBounds.getCenterX(), endBounds.getCenterY());
      return new Line2D.Double(
         start.getConnectionPoint(endCenter),
         end.getConnectionPoint(startCenter));
   }

   private Node start;
   private Node end;
}
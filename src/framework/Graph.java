package framework;

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;
/**
 * Graph is a template for what the functionality will look like for the editor.
 * 
 * RADUIS: padding used for finding nodes
 * */
@SuppressWarnings("serial")
public abstract class Graph implements Serializable {
	/**
	 * creates list needed
	 */
	public Graph() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}
	/**
	 * connect two nodes at location p1,p2 if nodes are found
	 * @precondition e != null && p1 != null && p2 != null
	 * @param edge between nodes location for start and end of edge
	 * @return true if edge was possible, false it not
	 */
	public boolean connect(Edge e, Point2D p1, Point2D p2){
	      Node n1 = achorNode(p1);
	      Node n2 = achorNode(p2);
	      if (n1 != null && n2 != null)
	      {
	         e.connect(n1, n2);
	         edges.add(e);
	         return true;
	      }
	      return false;
	 }
	/**
	 * Add to node list at location p
	 * @precondition n != null && p != null
	 * @param object to be added and location p
	 */
	 public void add(Node n, Point2D p) {
		Rectangle2D bounds = n.getBounds();
		n.translate(p.getX() - bounds.getX(), p.getY() - bounds.getY());
		nodes.add(n);
	}
	/**
	 * Add to node list
	 * @precondition n != null
	 * @param object to be added
	 * @postcondition nodes size increased by one
	 */
	public void add(Node n) {
		nodes.add(n);
	}
	/**
	 * Add to edge list
	 * @precondition e != null
	 * @param object to be added
	 * @postcondition edges size increased by one
	 */
	public void add(Edge e) {
		edges.add(e);
	}
	/**
	 * Draw all components
	 * @precondition g2 != null
	 * @param graphics
	 */
	public void draw(Graphics2D g2) {
		for (Node n : nodes)
			n.draw(g2);
		for(Edge e : edges)
			e.draw(g2);
	}
	/**
	 * Finds nodes to anchor edge with, child or parent
	 * @precondition point != null
	 * @param location p
	 * @return child node within range or null if nothing was found
	 */
	public Node achorNode(Point2D point){
		for(Node n : nodes){
			if(n.getNodes() != null){
				for(Node m : n.getNodes()){
					Rectangle2D temp = m.getBounds();
					temp.setRect(temp.getX()-(RADIUS), temp.getY()-(RADIUS), temp.getWidth()+RADIUS, temp.getHeight()+RADIUS);
					if(temp.contains(point))
						return m;
				}
			}else{
				Rectangle2D temp = n.getBounds();
				temp.setRect(temp.getX()-(RADIUS), temp.getY()-(RADIUS), temp.getWidth()+RADIUS, temp.getHeight()+RADIUS);
				if(temp.contains(point))
					return n;
			}
		}
		return null;
	}
	/**
	 * Finds only parent nodes
	 * @precondition point != null
	 * @param location p
	 * @return parent node within range or null if nothing was found
	 */
	public Node findNode(Point2D point){
		for(Node n : nodes){
				Rectangle2D temp = n.getBounds();
				temp.setRect(temp.getX()-(RADIUS/2), temp.getY()-(RADIUS/2), temp.getWidth()+RADIUS, temp.getHeight()+RADIUS);
				if(temp.contains(point))
					return n;
		}
		return null;
	}
	/**
	 * Return node at point or null
	 * @precondition point != null
	 * @param location p
	 * @return edge if exists at point else null
	 */
	public Edge findEdge(Point2D point){
		for(Edge e : edges)
			if(e.contains(point))
					return e;
		return null;
	}
	/**
	 * Remove a node and all edges attached to it
	 * @precondition n != null
	 * @param n to be removed
	 */
	public void deleteNode(Node n){
		removeConnections(n);
		nodes.remove(n);
	}
	/**
	 * Check if rectangle intersect with components and edges on
	 * board
	 * @precondition r2d != null
	 * @param n to be removed
	 * @return true if it intersects, false it not
	 */
	public boolean noIntersect(Rectangle2D r2d){
		for(Edge e : edges){
			if(e.getConnectionPoints().intersects(r2d)){
				return false;
			}
		}
		for(Node n : nodes){
			if(n.getBounds().contains(r2d)){
				return false;
			}
		}
		return true;
	}
	/**
	 * Check if line intersect with current edges
	 * @precondition l2d != null
	 * @param line that might intersect
	 * @return true if it intersects, false it not
	 */
	public boolean noIntersect(Line2D l2d){
		for(Edge e : edges){
			if(e.getConnectionPoints().intersectsLine(l2d)){
				return false;
			}
		}
		return true;
	}
	/**
	 * Removes all connections(Edges) associated with n, by using a visitor pattern
	 * child and parent connections will be removed.
	 * @precondition n != null
	 * @param node to remove edges connected to it
	 * @postcondition Edges connected with n are removed
	 * */
	public void removeConnections(Node n){
		if(n.getNodes() != null){
			for(Node m : n.getNodes()){
				removeConnections(m);
			}
		}else{
			for(Edge e : (ArrayList<Edge>) edges.clone()){
				if (e.getStart().equals(n) || e.getEnd().equals(n)){
					edges.remove(e);
				}
			}
		}
	}
	/**
	 * Remove an edge
	 * @param e to be removed
	 */
	public void deleteEdge(Edge e){
		edges.remove(e);
	}
	/**
	 * gets all the Node bounds and return them in a list
	 * @return list of bounds
	 * */
	public ArrayList<Rectangle2D> getNodeBounds(){
		ArrayList<Rectangle2D> r = new ArrayList<Rectangle2D>();
		for (Node n : nodes){
			r.add(n.getBounds());
		}
		return r;
   }
	/**
	 * gets all the Edge bounds and return them in a list
	 * @return list of bounds
	 * */
	public ArrayList<Rectangle2D> getEdgeBounds(){
		ArrayList<Rectangle2D> r = new ArrayList<Rectangle2D>();
		for (Edge e : edges){
		   r.add(e.getBounds());
		}
		return r;
	}
	
	public abstract Node[] 	getNodePrototypes();
	public abstract Edge[] 	getEdgePrototypes();
	
	public List<Node> getNodes() {
		return Collections.unmodifiableList(nodes);
	}
	
	public List<Edge> getEdges(){
		return Collections.unmodifiableList(edges);
	}
	
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	private final double RADIUS = 10;
}
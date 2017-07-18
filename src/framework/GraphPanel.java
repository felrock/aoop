package framework;

import java.awt.*;
import java.awt.geom.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * SUBDIVISIONS : amount of squares on the grid
 * SUBDIVISION_SIZE : size of the squares
 * */
@SuppressWarnings("serial")
public class GraphPanel extends JComponent {
	/**
	 * Create necessary structures, add listeners for mouse actions
	 * 
	 * */
	public GraphPanel(ToolBar aToolBar, Graph aGraph, Cart cart) {
		toolBar = aToolBar;
		graph = aGraph;
		setBackground(Color.WHITE);
		this.cart = cart;
		addMouseListener(new MouseAdapter() {
			/**
			 * @param event, current mouse event
			 * mousePressed handles selected component and if mouse event is a right click.
			 * If edge is selected, mousePressed saves location as lineStart for edge object
			 * in release method
			 * 
			 * */
			public void mousePressed(MouseEvent event) {
				Point2D mousePoint = event.getPoint();
				Node n = graph.findNode(mousePoint);
				Object tool = toolBar.getSelectedTool();
				if (tool != null && !isRightClicked(event)){
					 /* If tool is a line and an object was pressed,
					  * then set start for line*/
					if(tool instanceof Edge){
						if(n != null){ 
							lineStart = mousePoint; 
							selected = graph.findEdge(mousePoint);
						}
					}
					else{
						selected = graph.findNode(mousePoint);
					}
				}
				else if(isRightClicked(event)){
					selected = graph.findNode(mousePoint);
					if(selected == null)
						selected = graph.findEdge(mousePoint);
					if(selected != null)
						popUpMenu(event);
				}
				origin = mousePoint;
				repaint();
			}
			/**
			 * @param event, current mouse event
			 * mousePressed handles creation of components and creating edges between lines
			 * adds component to cart if it has set cartable() as true
			 * */
			public void mouseReleased(MouseEvent event){
				Object tool = toolBar.getSelectedTool();
				Point2D mousePoint = event.getPoint();
				if (tool != null && !isRightClicked(event)){
					/* If there already is an object,
					 * dont add the new one.*/
					if (tool instanceof Node && selected == null) {
						Node prototype = (Node) tool;
						Node temp = (Node) prototype.clone();
						Rectangle2D bounds = temp.getBounds();
						temp.translate(mousePoint.getX() - bounds.getX(), mousePoint.getY() - bounds.getY());
						if(graph.noIntersect(temp.getBounds())){
							graph.add(temp);
							selected = temp;
							if(temp.cartable())
								cart.addItem(new Item(temp,temp.toString()));
						}
					}
					else if(tool instanceof Edge && lineStart != null){
						if(graph.noIntersect(new Line2D.Double(lineStart,mousePoint))){
							Edge prototype = (Edge) tool;
							Edge newEdge = (Edge) prototype.clone();
							if (graph.connect(newEdge, lineStart, mousePoint)){
								selected = newEdge;
							}
						}
					}
				}
				revalidate();
				repaint();
				lineStart = null;
				currentLine = null;
			}
		});
		/**
		 * @param event, current mouse event
		 * mouseDragged handles movement of selected components. If edge is selected
		 * it creates a temporary line to mousePoint
		 * */
		addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent event){
				Point2D mousePoint = event.getPoint();
				Object tool = toolBar.getSelectedTool();
				if (tool != null && !isRightClicked(event)) {
					if(tool instanceof Edge && lineStart != null){
						currentLine = new Line2D.Double(lineStart,mousePoint);
					}
					else if(tool instanceof Node && selected != null){
						((Node)selected).translate(mousePoint.getX()-origin.getX(), mousePoint.getY()-origin.getY());
					}
				}
				origin = mousePoint;
				repaint();
			}
		});
	}
	/**
	 * Draw the grid, and call graph draw method.
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.lightGray);
		for (int i = 1; i < SUBDIVISIONS; i++) {
			int x = i * SUBDIVISION_SIZE;
			for(int j=0; j < getSize().height;j+=10)
				g2.drawLine(x, j, x, j+5);
			
		}
		for (int i = 1; i < SUBDIVISIONS; i++) {
			int y = i * SUBDIVISION_SIZE;
			for(int j=0; j < getSize().width;j+=10)
				g2.drawLine(j, y, j+5, y);
		}
		if(currentLine != null){
			g2.setPaint(Color.BLUE);
			g2.draw(currentLine);
		}
		graph.draw(g2);
	}
	/**
	 * remove the selected object. check instance and invoke
	 * appropriate method
	 * */
	public void removeSelected(){
		if(selected instanceof Node){
			graph.deleteNode((Node)selected);
			cart.remove(selected);
		}else if(selected instanceof Edge){
			graph.deleteEdge((Edge)selected);
		}
		repaint();
	}
	/**
	 * create a property sheet for the selected object, if anything changes 
	 * repaint. Also update cart.
	 * */
	public void editSelected(){
		PropertySheet sheet = new PropertySheet(selected);
	      sheet.addChangeListener(new ChangeListener(){
				@Override
				public void stateChanged(ChangeEvent e) {
					repaint();
				}
	         });
	      JOptionPane.showMessageDialog(null, 
	         sheet, 
	         "Properties", 
	         JOptionPane.QUESTION_MESSAGE);
	      cart.updateCart(graph);
	}

	/**
	 * @param event, current mouse event
	 * @return true if right click was made.
	 */
	public boolean isRightClicked(MouseEvent event){
		return SwingUtilities.isRightMouseButton(event) || event.isControlDown();
	}
	/**
	 * @param event, current mouse event
	 * creates a drop down menu at the mouse position 
	 */
	public void popUpMenu(MouseEvent e){
		popMenu = new JPopupMenu();
		popMenu.setBorder(BorderFactory.createRaisedBevelBorder());
		ActionListener menuListener = new ActionListener() {
		public void actionPerformed(ActionEvent event) {
			  if(event.getActionCommand() == "Delete" && selected != null){
				  removeSelected();
			  }
			  else if(event.getActionCommand() == "Edit"){
				  editSelected();
			  }
			  else if(event.getActionCommand() == "Rotate"){
        		graph.removeConnections((Node)selected);
        		try {
					rotateMet.invoke(selected);
				} catch (Exception ex) {}
			  }
	      repaint();
		  }
		};
          
        JMenuItem anItem = new JMenuItem("Delete", (new ImageIcon
				(new ImageIcon("src/pictures/trashbin.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH))));
        anItem.addActionListener(menuListener);
        popMenu.add(anItem);
        anItem = new JMenuItem("Edit", (new ImageIcon
				(new ImageIcon("src/pictures/edit.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH))));
        anItem.addActionListener(menuListener);
        popMenu.add(anItem);
        /*add rotate to menu if it exists within selected's class*/
        try{
        	rotateMet = selected.getClass().getMethod("rotate");
        	if(rotateMet != null){
	        	anItem = new JMenuItem("Rotate", (new ImageIcon
	    				(new ImageIcon("src/pictures/rotate.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH))));
	        	anItem.addActionListener(menuListener);
	        	popMenu.add(anItem);
        	}
        }
        catch(Exception MethodNotFound){
        	// do nothing
        }
        popMenu.show(this, e.getX(), e.getY());
	}
	
	
	
	private static final int SUBDIVISIONS = 100;
	private static final int SUBDIVISION_SIZE = 2400 / SUBDIVISIONS;
	private Method rotateMet;
	private Point2D origin;
	private Point2D lineStart;
	private Object selected;
	private Graph graph;
	private ToolBar toolBar;
	private JPopupMenu popMenu;
	private Line2D currentLine;
	private Cart cart;
}

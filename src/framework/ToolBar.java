package framework;

import java.awt.*;
import java.util.*;
import javax.swing.*;
/**
 * Tool bar for components
 * 
 * BUTTON_SIZE : size of the JButtons in the tool bar
 * */
@SuppressWarnings("serial")
public class ToolBar extends JPanel {
	/**
	 * create necessary data structures and add prototypes
	 * */
	public ToolBar(Graph graph) {
		group = new ButtonGroup();
		tools = new ArrayList<Object>();
		Node[] nodeTypes = graph.getNodePrototypes();
		for (Node n : nodeTypes)
			add(n);
		Edge[] edgeTypes = graph.getEdgePrototypes();
		for(Edge e : edgeTypes)
			add(e);
	}
	/**
	 * gets the selected tool in the tool bar else return null
	 * @return selected tool
	 * */
	public Object getSelectedTool() {
		int i = 0;
		for (Object o : tools) {
			JToggleButton button = (JToggleButton) getComponent(i++);
			if (button.isSelected())
				return o;
		}
		return null;
	}
	/**
	 * add prototype that implements Node to tool bar
	 * @precondition n != null
	 * @param n to be added
	 * */
	public void add(final Node n) {
			JToggleButton button = new JToggleButton(n.getButtonIcon(BUTTON_SIZE));
			group.add(button);
			add(button);
			tools.add(n);
	}
	/**
	 * add prototype that implements Edge to tool bar, static image for now
	 * @precondition e != null
	 * @param e to be added
	 * */	
	public void add(final Edge e) {
		JToggleButton button = new JToggleButton(new ImageIcon(new ImageIcon("src/pictures/drawing.png").getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH)));
		group.add(button);
		add(button);
		tools.add(e);
	}
	private ButtonGroup group;
	private ArrayList<Object> tools;

	private static final int BUTTON_SIZE = 20;
}
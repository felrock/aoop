package app;

import java.awt.*;

import framework.*;
/**
 * Extends abstract class Graph, implements prototypes
 * */
@SuppressWarnings("serial")
public class SimpleGraph extends Graph {
	/**
	 * Tools for the toolbox, placeable components that implements Node
	 * */
	public Node[] getNodePrototypes() {
		Node[] nodeTypes = {
				new LogicGate(Color.BLACK, 0, "src/pictures/test.png"), 
				new Resistor(20,"src/pictures/resistor.png"), 
				new Capacitor(10,"src/pictures/cap.png"), 
				new Hole(Color.RED,"src/pictures/dotdot.png"), 
				new Pin(Color.BLACK,"src/pictures/dot.png")
				};
		return nodeTypes;
	}
	/**
	 * Tools for the toolbox, placeable components that implements Edge
	 * */
	public Edge[] getEdgePrototypes(){
		Edge[] edgeTypes = {new LineEdge()};
		return edgeTypes;
	}
}
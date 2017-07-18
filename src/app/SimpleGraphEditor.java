package app;

import javax.swing.*;
import framework.*;
/**
 * Deployment class, creates the SimpleGraph object.
 * @author Felix S
 * @author Jonatan J
 * 
 * */
public class SimpleGraphEditor {
	public static void main(String[] args) {
		JFrame frame = new GraphFrame(new SimpleGraph());
		frame.setTitle("ECeditor");
		frame.setVisible(true);
	}
}

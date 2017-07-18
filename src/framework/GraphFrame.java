package framework;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import app.SimpleGraph;

/**
 * This frame shows the toolbar, shopping cart and the graph. 
 * 
 * FRAME_HEIGHT : window height
 * FRAME_WIDTH  : window width
 */
@SuppressWarnings("serial")
public class GraphFrame extends JFrame {
	/**
	 * create necessary structures  
	 * */
	public GraphFrame(final Graph graph) {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.graph = graph;
		cart = new Cart();
		toolBar = new ToolBar(graph);
		panel = new GraphPanel(toolBar, graph,cart);
		scrollPane = new JScrollPane(panel);
		
		createMenu();

		this.setJMenuBar(menuBar);
		this.add(toolBar, BorderLayout.NORTH);
		this.add(cart, BorderLayout.EAST);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	/**
	 * create the top menu for file handling, create listener that 
	 * handles save/load/new
	 * */
	public void createMenu(){
		ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
              if(event.getActionCommand() == "Save"){
            	  try {
					saveToFile();
				} catch (IOException e) {
					//user did something wrong
				}
              }
              if(event.getActionCommand() == "Load"){
            	  try {
					loadFile();
				} catch (ClassNotFoundException | IOException e) {
					//user did something wrong
				}
              }
              if(event.getActionCommand() == "New"){
            	  graph = new SimpleGraph();
            	  newSetup();
              }
            }};
		menuBar = new JMenuBar();	
		menuBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		JMenu menu = new JMenu("File");
		JMenuItem menuItem = new JMenuItem("New",(new ImageIcon
				(new ImageIcon("src/pictures/edit.png").getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH))));
		menuItem.addActionListener(menuListener);
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Save",(new ImageIcon
				(new ImageIcon("src/pictures/save.png").getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH))));
		menuItem.addActionListener(menuListener);
		menu.add(menuItem);
		menuItem = new JMenuItem("Load",(new ImageIcon
				(new ImageIcon("src/pictures/load.png").getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH))));
		menuItem.addActionListener(menuListener);
		menu.add(menuItem);
		menuBar.add(menu);
	}
	/**
	 * saves graph data structure to a file, either to new or existing file
	 * @throws file not found or i/o complications
	 * */
	public void saveToFile() throws FileNotFoundException, IOException{
		JFileChooser c = new JFileChooser();
		if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
			try{
				File file = c.getSelectedFile();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(graph);
				out.close();
			}
			catch (IOException exception)
			{
				JOptionPane.showMessageDialog(null,
						exception);
			}
		}
	}
	/**
	 * load graph data structure to a current graph. 
	 * @throws file not found, i/o complications, wrong serial or class not found
	 * */
	public void loadFile() throws FileNotFoundException, IOException, ClassNotFoundException{
		JFileChooser c = new JFileChooser();
		int r = c.showOpenDialog(this);
		if (r == JFileChooser.APPROVE_OPTION){
			try{
				File file = c.getSelectedFile();
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
				graph = (Graph) in.readObject();
				in.close();
				newSetup();
			}
			catch (IOException exception)
			{
				JOptionPane.showMessageDialog(null,
						exception);
			}
			catch (ClassNotFoundException exception)
			{
				JOptionPane.showMessageDialog(null,
						exception);
			}
		}
	}
	/**
	 * create new editor project
	 * */
	public void newSetup(){
		this.remove(scrollPane);
		this.remove(toolBar);
		this.remove(cart);
		
		cart = new Cart();
		toolBar = new ToolBar(graph);
		panel = new GraphPanel(toolBar, graph,cart);
		scrollPane = new JScrollPane(panel);
		
		this.setJMenuBar(menuBar);
		this.add(toolBar, BorderLayout.NORTH);
		this.add(cart, BorderLayout.EAST);
		this.add(scrollPane, BorderLayout.CENTER);
		
		cart.updateCart(graph);
		
		revalidate();
		repaint();
	}

	private Graph graph;
	private GraphPanel panel;
	private JScrollPane scrollPane;
	private Cart cart;
	private ToolBar toolBar;
	private JMenuBar menuBar;

	public static final int FRAME_WIDTH = 800;
	public static final int FRAME_HEIGHT = 600;
}
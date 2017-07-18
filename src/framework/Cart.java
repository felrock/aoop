package framework;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;


public class Cart extends JTextArea{

	/**
	 * Class that holds all cartable() items, updates upon editing for visuals. 
	 * Cart extends JTextArea for easy use of setText() method
	 * 
	 * CART_WIDTH: width of cart window
	 * CART_HEIGHT: height of cart window
	 */
	private static final long serialVersionUID = -4286740683633062140L;
	public Cart() {
		items = new ArrayList<Item>();
		setPreferredSize(new Dimension(CART_WIDTH,CART_HEIGHT));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setEditable(false);
	}
	/**
	 * Print out the items to the JTextArea.
	 */
	public void printCart(){
		String r = "";
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> amounts = new ArrayList<Integer>();
		
		for(int i=0; i < items.size(); i++){
			String name = items.get(i).getName();
			if(names.contains(name)){
				amounts.add(names.indexOf(name), amounts.get(names.indexOf(name))+1); // it exist add get amount and add one to it
			}else{
				names.add(name); // name of item
				amounts.add(1); // add one item.
			}
		}
		for(int i=0; i < names.size();i++){
			r +=names.get(i) + " - " + "x" + amounts.get(i) + "\n";
		}
		this.setText(r);
	}
	/**
	 * add item to cart and update the JTextArea
	 * @precondition i != null
	 * @param Item to be added
	 */
	public void addItem(Item i){
		items.add(i);
		printCart();
	}
	/**
	 * removes Object o from list
	 * @param Object to be removed
	 * @postcondition if o doesn't exit nothing happens
	 */
	public void remove(Object o){
		for(Item t : items){
			if(t.getOrigin().equals(o)){
				items.remove(t);
				break;
			}
		}
		printCart();
	}
	/**
	 * remove old items and add the new from graph, graph is final because
	 * it should not be edited.
	 * @param graph to get components from 
	 */
	public void updateCart(final Graph graph){
		items.removeAll(items);
		for(Node n : graph.getNodes())
			if(n.cartable())
				addItem(new Item(n,n.toString()));
	}
	
	public static final int CART_WIDTH = 200;
	public static final int CART_HEIGHT = 600;
	public static ArrayList<Item> items;
}

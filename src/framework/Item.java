package framework;
/**
 * Class for cart items
 * */
public class Item{
	private String name;
	private Object origin;
	/**
	 * origin is the object which is used when removing items
	 * name is what will be printed on the JTextArea(cart)
	 * */
	public Item(Object origin, String name){
		this.setOrigin(origin);
		this.setName(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString(){
		return name;
	}
	public Object getOrigin() {
		return origin;
	}
	public void setOrigin(Object origin) {
		this.origin = origin;
	}
}

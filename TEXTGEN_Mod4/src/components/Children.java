package components;

import java.util.ArrayList;

public class Children {
	ArrayList<Component> children;
	
	public Children(ArrayList<Component> children){
		this.children = children;
		if(children == null)
			this.children = new ArrayList<Component>();
	}
	
	public void addChild(Component child){
		if(child!=null)
			children.add(child);
	}
	
	public void addChild(int index, Component child){
		if(child!=null)
			children.add(index, child);
	}
	
	public ArrayList<Component> getChildren(){
		return children;
	}
	
	public void removeChild(Component child){
		if(child != null)
			children.remove(child);
	}
}
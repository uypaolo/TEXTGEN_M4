package components;

public class ComponentInfo {

	private static final String TYPE_PHRASE = "phrase";
	private static final String TYPE_LEAF = "leaf";
	
	private String name;
	private String description;
	private String type;
	
	public ComponentInfo(String name, String description, String type){
		this.name = name;
		this.description = description;
		this.type = type;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public boolean isLeaf(){
		return type.equalsIgnoreCase(TYPE_LEAF);
	}
	
	public ComponentInfo getCopy(){
		return new ComponentInfo(name, description, type);
	}
}

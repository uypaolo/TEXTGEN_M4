package components;

import java.io.File;
import java.util.ArrayList;

import org.jdom2.Element;

public class InputXMLDocument {

	private String category;
	private String name;
	private String comments;
	private File xmlFile;
	
	private ArrayList<Component> sentences;
	
	public InputXMLDocument(File xmlFile, String category, String name, String comments, ArrayList<Component> sentences){
		this.xmlFile = xmlFile;
		
		if(category == null)
			category = "";
		if(name == null)
			name = "";
		if(comments == null)
			comments = "";
		if(sentences == null)
			sentences = new ArrayList<Component>();
		
		this.category = category;
		this.name = name;
		this.sentences = sentences;
		this.comments = comments;
	}
	
	//Getters
	public String getName() {
		return name;
	}
	
	public String getCategory(){
		return category;
	}
	
	public String getComments(){
		return comments;
	}
	
	public File getXmlFile(){
		return xmlFile;
	}
	
	public void setXMLFile(File file){
		xmlFile = file;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	public void setOtherInfo(String otherInfo){
		this.comments = otherInfo;
	}
	
	public ArrayList<Component> getClauses(){
		return (ArrayList<Component>)sentences.clone();
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder(category+" / "+name);
		if(comments != null)
			sb.append(" / "+comments);
		return sb.toString();
	}
	
	public InputXMLDocument getCopyWithoutFeatures(){
		
		ArrayList<Component> sentenceCopies = new ArrayList<Component>();
		for(Component sentence: sentences)
			sentenceCopies.add(sentence.getCopyWithoutFeatures());
		
		File xmlFileCopy;
		if(xmlFile != null)
			xmlFileCopy = new File(xmlFile.getName());
		else
			xmlFileCopy = null;
		return new InputXMLDocument(xmlFileCopy, category, name, comments, sentenceCopies);
	}
	
	public int setTagForStructuralRule(int startTag){
		int currTag = startTag;
		
		for(Component component: sentences)
			currTag = component.setTag(currTag);
		
		return currTag;
	}
	
	public int getBiggestTag(){
		int maxTag = -1;
		
		for(Component component: sentences){
			int currTag = component.getMaxTag();
			if(currTag > maxTag)
				maxTag = currTag;
		}
		
		return maxTag;
	}
	
	//Setters
	public void addClauseAt(int index, Component clause){
		sentences.add(index, clause);
	}
	
	public void removeSentence(Component sentence){
		if(sentence != null)
			sentences.remove(sentence);
	}
	
	//Generation of XML Copy

	
	//Complex getters
	public Element generateXMLCopy(){
		
		Element rootElement = new Element("document");
		
		setElementAttribute(rootElement, "name", name);
		setElementAttribute(rootElement, "category", category);
		
		if(!comments.isEmpty()){
			Element commentsElement = new Element("comments");
			commentsElement.setText(comments);
			rootElement.addContent(commentsElement);
		}
		
		
		for(Component clause: sentences)
			rootElement.addContent(clause.generateXMLElement());
		
		return rootElement;
	}

	private void setElementAttribute(Element element, String attributeName, String attributeValue){
		if(attributeValue != null && !attributeValue.isEmpty() && attributeName != null && !attributeName.isEmpty())
			element.setAttribute(attributeName, attributeValue);
	}
}
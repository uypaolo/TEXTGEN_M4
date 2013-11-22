package components;

import java.util.ArrayList;

import managers.FeatureManager;

import org.jdom2.Element;

import features.Feature;

public class Phrase extends Component{

	private Children children;
	
	public Phrase(Element componentElement, boolean autoAddDefaultFeatures) {
		super(componentElement, autoAddDefaultFeatures);
		children = new Children(new ArrayList<Component>());
	}
	
	public Phrase(String componentName, boolean autoAddDefaultFeatures){
		super(componentName, autoAddDefaultFeatures);
		this.children = new Children(new ArrayList<Component>());
	}
	
	public void addChild(Component child){
		children.addChild(child);
	}

	public String getStringForPrinting() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(name+"(");
		for(Component child: children.getChildren())
			stringBuilder.append(child.toString());
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
	
	protected String getFeatures(boolean includeDefaults, String nextLineToken){
		StringBuilder featureString = new StringBuilder();
		featureString.append("*****Features");
		if(!includeDefaults)
			featureString.append("(Non-default)");
		
		featureString.append("*****");
		featureString.append(nextLineToken);
		
		for(Feature feature: featureList.getFeatureList()){	
			if(includeDefaults || !FeatureManager.isFeatureDefault(name, feature.getName(), feature.getValue())){
				featureString.append(feature.getName()+" = "+feature.getValue());
				if(!feature.isStandard())
					featureString.append(" [Custom Feature]");
				featureString.append(nextLineToken);
			}
		}
		featureString.append(nextLineToken);
		return featureString.toString();
	}
	
	public boolean isLeaf() {
		return false;
	}

	public Children getChildren() {
		return children;
	}

	public String toString() {
		return name;
	}

	@Override
	public String toGeneratedString() {
		return toString();
	}
	
	@Override
	protected void addAdditionalXMLContent(Element parentElement) {
		for(Component child: children.getChildren())
			parentElement.addContent(child.generateXMLElement());
	}

	@Override
	public String toLexiconSentence() {
		StringBuilder sb = new StringBuilder();
		int numChildren = children.getChildren().size();
		ArrayList<Component> childList = children.getChildren();
		
		for(int i=0; i < numChildren; i++){
			Component child = children.getChildren().get(i);
			String childSentence = child.toLexiconSentence();
			if(!childSentence.isEmpty()){
				sb.append(child.toLexiconSentence());
				if(i < numChildren - 1 && !childList.get(i+1).toLexiconSentence().equals("."))
					sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	@Override
	public String toConceptSentence() {
		StringBuilder sb = new StringBuilder();
		int numChildren = children.getChildren().size();
		ArrayList<Component> childList = children.getChildren();
		
		for(int i=0; i < numChildren; i++){
			Component child = children.getChildren().get(i);
			String childSentence = child.toConceptSentence();
			if(!childSentence.isEmpty()){
				sb.append(childSentence);
				if(i < numChildren - 1 && !childList.get(i+1).toConceptSentence().equals("."))
					sb.append(" ");
			}
		}
		return sb.toString();
	}

	public void insertChildAt(int index, Component child){
		children.addChild(index, child);
	}

	public void removeChild(Component child){
		children.removeChild(child);
	}
	
	public int setTag(int nextTag) {
		this.tag = nextTag;
		nextTag++;
		
		for(Component child: children.getChildren())
			nextTag = child.setTag(nextTag);
		
		return nextTag;
	}

	
	
	public Component getComponentByTag(int tag) {
		if(this.tag == tag)
			return this;
		
		for(Component child: children.getChildren()){
			Component potentialMatch = child.getComponentByTag(tag);
			if(potentialMatch != null)
				return potentialMatch;
		}
		
		return null;
	}

	@Override
	public int getMaxTag() {
		int maxTag = -1;
		
		for(Component component: children.getChildren()){
			int currTag = component.getMaxTag();
			if(currTag > maxTag)
				maxTag = currTag;
		}
		
		return maxTag;
	}

}
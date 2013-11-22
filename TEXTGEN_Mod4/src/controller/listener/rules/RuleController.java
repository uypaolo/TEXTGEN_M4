package controller.listener.rules;

import components.Phrase;

public interface RuleController {
	
	public void updateFeature();
	
	public void setPhraseComponent(Phrase phrase);
	
	public void connectComponents();
}

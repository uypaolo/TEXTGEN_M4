package controller;

import view.ontology.OntologyPanel;

public class OntologyController {

	private OntologyPanel ontologyPanel;
	
	public OntologyController(){
		ontologyPanel = new OntologyPanel();
	}
	
	//Refresh
	public void refreshOntologyPanel(){
		ontologyPanel.initTable();
	}
	
	//Getters
	public OntologyPanel getPanel(){
		return ontologyPanel;
	}
	
}

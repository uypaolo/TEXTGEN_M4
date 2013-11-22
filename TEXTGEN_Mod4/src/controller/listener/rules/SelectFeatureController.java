package controller.listener.rules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.rules.SelectFeatures;

import components.Component;
import components.Leaf;
import components.Phrase;

import features.Feature;

public class SelectFeatureController {
	SelectFeatures view;
	Leaf leafComponent;
	Phrase phraseComponent;
	Phrase clauseComponent;
	String phrasePOSCode;
	RuleController controller;
	
	//initialize rulegroup
	
	public SelectFeatureController(Phrase clauseComponent, RuleController controller)
	{
		this.controller = controller;
		this.clauseComponent = clauseComponent;
		Component temp = clauseComponent.getChildren().getChildren().get(0);
		
		if(!temp.isLeaf())
		{
			this.phraseComponent = (Phrase)temp;
			this.phrasePOSCode = phraseComponent.getPOSCode();
			leafComponent = (Leaf)phraseComponent.getChildren().getChildren().get(0);
		}
		
		else
		{
			leafComponent = (Leaf)temp;
			phraseComponent = null;
		}
		
		
		view = new SelectFeatures(leafComponent.getPOSCode(), leafComponent.getDescription(), phrasePOSCode);
		
		setSelectedFeature();
		view.addOKBtnListener(new OKActionListener());
		view.addCancelBtnListener(new CancelActionListener());
	}
	
	public void setSelectedFeature()
	{
		setFeatureTable(view.getFeatureTable(), leafComponent.getFeatures());
		if(view.getPhraseTable() != null)
			setFeatureTable(view.getPhraseTable(), phraseComponent.getFeatures());
		setFeatureTable(view.getClauseTable(), clauseComponent.getFeatures());
	}
	
	public void setFeatureTable(JTable table, ArrayList<Feature> featureList)
	{
		for(int i = 0; i < table.getColumnCount(); i+=2)
		{
			for(int j = 0; j < table.getRowCount(); j++)
			{
				for(int k = 0; k < featureList.size(); k++)	
					if(table.getValueAt(j, i)!=null)
						if(table.getValueAt(j, i).equals(featureList.get(k).getValue()) 
								&& table.getColumnName(i).equals(featureList.get(k).getName()))
							table.setValueAt(Boolean.TRUE, j, i+1);
			}
		}
	}
	
	class OKActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			 leafComponent.getFeatures().clear();
			 clauseComponent.getFeatures().clear();

			 //insert basic feature
			 addFeatureToComponent(leafComponent, view.getFeatureTable());
			 //insert clause features
			 addFeatureToComponent(clauseComponent, view.getClauseTable());
			 if(phraseComponent != null)
			 {
				 phraseComponent.getFeatures().clear();
				//insert phrase feature
				 addFeatureToComponent(phraseComponent, view.getPhraseTable());
			 }
			 controller.updateFeature();
			 view.dispose();
		}
		
		public void addFeatureToComponent(Component component, JTable table)
		{
			for(int i = 0 ; i < table.getColumnCount(); i+=2)
			{
			  String featureName = table.getColumnName(i);
			  //DBFeatureValues dbFeatValList = FeatureManager.getFeatureValues(featureElement);
			  for(int j = 0; j < table.getRowCount(); j++)
			  {
				  if(table.getValueAt(j, i)!=null && table.getValueAt(j, i+1) != Boolean.FALSE)
				  {
					  component.addFeature(new Feature(featureName, table.getValueAt(j, i).toString(), true));
				  }  
			  }
		  	}
		}
	}
	
	
	class CancelActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			view.dispose();
		}
		
	}
	
	class tabPaneChangeListener implements ChangeListener
	{
	      public void stateChanged(ChangeEvent changeEvent) 
	      {
	    	view.getPhraseTable().repaint();
	    	view.getPhraseTable().revalidate();
	        //view.repaint();
	        //view.revalidate();
	      }
	}
}
package view.lexicon;

import java.util.ArrayList;

import javax.swing.JTable;

import features.Feature;

public class FeatureTable extends JTable{
	
	ArrayList<Feature> featList;
	
	public FeatureTable(){
		//set display properties
	}
	
	public void setFeatList(ArrayList<Feature> featList){
		this.featList = featList;
	}

}

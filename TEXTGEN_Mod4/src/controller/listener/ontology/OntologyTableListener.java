package controller.listener.ontology;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import managers.OntologyManager;
import ontology.ConceptList;
import view.ontology.OntologyTable;

public class OntologyTableListener implements TableModelListener{
	
	OntologyTable ontoTable;
	private ConceptList conceptList; //Use this to modify the concepts. it automatically updates XML if you use conceptList to update concepts.

	
	public OntologyTableListener(OntologyTable ontoTable, String posName){
		this.ontoTable = ontoTable;
		conceptList = OntologyManager.getInstance().getConceptList(posName);
	}
	
	public void tableChanged(TableModelEvent tme) {
		int row = tme.getFirstRow();
		int column = tme.getColumn();
	
		String colName = ontoTable.getColumnName(column);
		
		String conceptName = ontoTable.getStringAt(row, OntologyTable.COLUMN_STEM_NAME);
		String conceptSense = ontoTable.getStringAt(row, OntologyTable.COLUMN_SENSE);
		String editedString = (String)ontoTable.getValueAt(row, column);
		
		if(colName.equals(OntologyTable.COLUMN_COMMENTS))
			conceptList.updateConceptComment(conceptName, conceptSense, editedString);
		else if(colName.equals(OntologyTable.COLUMN_DEFINITION))
			conceptList.updateConceptDefinition(conceptName, conceptSense, editedString);
		else if(colName.equals(OntologyTable.COLUMN_SAMPLE_SENTENCE))
			conceptList.updateConceptSampleSentence(conceptName, conceptSense, editedString);
	}
}

package view.ontology;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import lexicon.Lexicon;
import managers.LexiconManager;
import ontology.Concept;
import ontology.ConceptList;

public class OntologyTable extends JTable{

	private ConceptList conceptList;
	private TableModelListener listener;
	private String[] columnNames;
	
	
	public static final String COLUMN_STEM_NAME = "Concept Stems";
	public static final String COLUMN_SENSE = "Sense";
	public static final String COLUMN_DEFINITION = "Definition";
	public static final String COLUMN_MAPPINGS = "Mappings";
	public static final String COLUMN_COMMENTS = "Comments";
	public static final String COLUMN_SAMPLE_SENTENCE = "Sample Sentence";
	
	public OntologyTable(){
		columnNames = new String[]{COLUMN_STEM_NAME, COLUMN_SENSE, COLUMN_DEFINITION, COLUMN_MAPPINGS, COLUMN_SAMPLE_SENTENCE, COLUMN_COMMENTS};
	}
	
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
	        Component c = super.prepareRenderer(renderer, row, column);
	        if (c instanceof JComponent) {
	           if(column == 0){
	            JComponent jc = (JComponent) c;
	            if(conceptList!= null){
	            	Concept currConcept = conceptList.getConceptList().get(row);
	            	//Place desired tooltip information here
	            	jc.setToolTipText("More Info for "+currConcept.getName()+"-"+currConcept.getSense());
	            }
	           }
	        }
	        return c;
	    }
	
	public void setListener(TableModelListener listener){
		this.listener = listener;
		this.getModel().addTableModelListener(listener);
	}
	
	public void setList(ConceptList list){
		if(list != null){
			this.conceptList = list; 
			refreshTableModel();
			
			DefaultTableModel model = (DefaultTableModel) this.getModel();
			
			for (int i = 0; i<model.getRowCount(); i++) {
			  for(int j=0;j<model.getColumnCount(); j++)
					prepareRenderer(this.getCellRenderer(i,j), i, j );
            }
		}
	}
	
	private void refreshTableModel(){
		DefaultTableModel model = new DefaultTableModel();	
		model.setColumnIdentifiers(columnNames);
		
		ArrayList<Concept> concepts = conceptList.getConceptList();
		for(Concept concept: concepts){
			model.addRow(generateRowData(concept));
		}
		this.setModel(model);
		model.addTableModelListener(listener);
	}
	
	private String[] generateRowData(Concept concept){
		String mappedLexicon = "";
		String pos = conceptList.getPOS();
		for(Lexicon lex : LexiconManager.getInstance().getMappedLexicons(pos, concept.getName(), concept.getSense())){
			mappedLexicon += lex.getName();
			mappedLexicon += ", ";
		}
		if(!mappedLexicon.equals(""))
			mappedLexicon = mappedLexicon.substring(0, mappedLexicon.length()-2);
		
		String[] data = {concept.getName(),concept.getSense(),concept.getDefinition(),mappedLexicon, concept.getSampleSentence(), concept.getComments()}; // should get values as displayed above.
		return data;
	}
	
	public boolean isCellEditable(int row, int column) {
	     	if(column == 2 || column > 3)
	     		return true;
	       return false;
	    }
	
	public String getStringAt(int row, String columnName){
		DefaultTableModel model = (DefaultTableModel)getModel();
		
		if(row < 0 ||row >= model.getRowCount())
			return null;
		
		int index = indexOf(columnName);
		if(index == -1)
			return null;
		
		return (String)model.getValueAt(row, index);
	}
	
	private int indexOf(String columnName){
		for(int i=0;i<columnNames.length; i++)
			if(columnNames[i].equalsIgnoreCase(columnName))
				return i;
		
		return -1;
	}
	
}

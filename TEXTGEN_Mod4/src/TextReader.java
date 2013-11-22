

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class TextReader {
	
	private static final String TEXTFILE_PATH_ONTOLOGY = "InputXMLandFiles\\TextFiles\\relations.txt";
	
	public void readText(){
		
		HashMap<String, Integer> stringCount = new HashMap<String, Integer>();
		
	    try {

			BufferedReader br = new BufferedReader(new FileReader(TEXTFILE_PATH_ONTOLOGY));
	    	Element posElement = new Element("pos");
	    	posElement.setAttribute("name", "Rel");
	    	
	        String line = br.readLine();

	        while (line != null) {
	            //System.out.print(line);
	        	if(!line.equals("\n") && !line.trim().isEmpty()){
	        		String[] tokens = line.split("'");
	        		
	        		for(String token: tokens)
	        			System.out.print(token+", ");
	        		System.out.println();
	        		
	        		Element stemElement = new Element("stem");
	        		
	        		if(stringCount.get(tokens[0].trim()) == null)
	        			stringCount.put(tokens[0].trim(), 1);
	        		else
	        		{
	        			int count = stringCount.get(tokens[0].trim());
	        			stringCount.put(tokens[0].trim(), count+1);
	        		}
	        		
	        		stemElement.setAttribute("name", tokens[0].trim());
	        		stemElement.setAttribute("sense", convertToLetter(stringCount.get(tokens[0].trim())));
	        		
	        		Element definitionElement = new Element("definition");
	        	
	        		if(tokens.length == 2)
	        			definitionElement.setText(tokens[1].trim());
	        		
	        		stemElement.addContent(definitionElement);
	        		posElement.addContent(stemElement);
	        	}
	        	
	            line = br.readLine();
	            
	        }
	        System.out.println("Exit");
	        
	        XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			try{
				xmlOutput.output(posElement, new FileWriter("Databases\\Ontology\\rel.xml"));
			}
			catch(Exception e){
				e.printStackTrace();
			}
	        
			br.close();
	        
	    }
	    catch(Exception e){e.printStackTrace();}
	   
	}
	
	private String convertToLetter(int n){
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return alphabet.charAt(n-1)+"";
	}
	
	
}

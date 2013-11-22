package managers;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XMLManager {
	//This class handles actual reading/writing of XML files.
	
	//Singleton
	private static XMLManager instance;
	public static XMLManager getInstance(){
		if(instance == null)
			instance = new XMLManager();
		return instance;
	}
	
	
	
	//Other attributes/methods
	
	private static final String BACKUP_PATH = "backups\\";
	
	public boolean writeToXML(String filePath, Element rootElement){
		File targetFile = new File(filePath);
		if(targetFile.exists()) //back up mode
			return writeToXMLWithBackup(filePath, rootElement);
		
		return writeToXMLWithoutBackup(filePath, rootElement);
	}
	
	private boolean writeToXMLWithoutBackup(String filePath, Element rootElement){
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try{
			xmlOutput.output(rootElement, new FileWriter(filePath));
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
		
	private boolean writeToXMLWithBackup(String filePath, Element rootElement){
		//backup the file first
		Path targetPath = Paths.get(filePath);
		Path backupPath = Paths.get(BACKUP_PATH+targetPath.getFileName());

		//Create the folder if it does not exist
		File backUpFolder = new File(BACKUP_PATH);
		if(!backUpFolder.exists())
			backUpFolder.mkdir();

		try{
			Files.copy(targetPath, backupPath, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		//If a backup cannot be made, do not proceed!

		//file writing successful
		if(writeToXMLWithoutBackup(filePath, rootElement)){
			return true;
		}
		
		//failed, Try restoring
		try{
			Files.copy(backupPath, targetPath, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
		}
		catch(Exception restoreException){
			restoreException.printStackTrace();
			JOptionPane.showMessageDialog(null, "Back-up restoration failed.\n Please search for "+backupPath.getFileName()+" in the 'backups' folder and\n copy it to "+targetPath.getFileName()+".");
		}

		return false;
	}
		
}

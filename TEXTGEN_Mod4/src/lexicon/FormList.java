package lexicon;

import java.util.ArrayList;

import org.jdom2.Element;

public class FormList {

	private ArrayList<Form> formList;
	
	public FormList(ArrayList<Form> formList){
		this.formList = formList;
	}
	
	public void setForm(Form newForm){
		Form toEdit = getForm(newForm.getName());
		if(toEdit != null)
			toEdit.setValue(newForm.getValue());
		else
			formList.add(newForm);
	}
	
	public Form getForm(String formName){
		for(Form form: formList)
			if(form.getName().equals(formName))
				return form;
		
		return null;
	}
	
	public void removeForm(String formName){
		Form toRemove = getForm(formName);
		if(toRemove != null)
			formList.remove(toRemove);
	}
	
	public void renameForm(String formName, String newFormName){
		Form toRename = getForm(formName);
		if(toRename != null){
			toRename.setName(newFormName);
		}
	}
	
	public Element generateXMLElement(){
		Element formsElement = new Element("forms");
		
		for(Form form: formList)
			formsElement.addContent(form.generateXMLElement());
			
		return formsElement;
	}
	
	public ArrayList<Form> getFormList(){
		return formList;
	}
	
}

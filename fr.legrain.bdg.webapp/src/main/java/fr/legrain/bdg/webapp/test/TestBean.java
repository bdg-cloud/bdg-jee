package fr.legrain.bdg.webapp.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
 
@Named
@ViewScoped
public class TestBean implements Serializable {

	private static final long serialVersionUID = 7752061447417566324L;

	private A selectedA;
	private B selectedB;
	
	private BService bService;
	
	private List<A> valuesA;
	private List<B> valuesB;

	@PostConstruct
    public void init() {
		bService = new BService();
		
		selectedA = new A();
		selectedA.setVal1("xxx");
		selectedA.setVal2("bb2");
		
		valuesA = new ArrayList<>();
		valuesA.add(selectedA);
    }
	
	public List<B> testAutoComplete(String query) {
		List<B> allValues = bService.getAllValues();
        List<B> filteredValues = new ArrayList<B>();
        
        if(query==null || query.equals("")) {
        	return allValues;
        } else {
	        for (int i = 0; i < allValues.size(); i++) {
	        	B b = allValues.get(i);
	            if(b.getVal10().toLowerCase().contains(query.toLowerCase())) {
	                filteredValues.add(b);
	            }
	        }
	        return filteredValues;
        }
    }
	
	public void autocompleteSelection(SelectEvent event) {
		Object value = event.getObject();

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("autocompleteSelection() : "+nomChamp);
		
		selectedA = new A(selectedB.getVal10(), selectedB.getVal11());

	}
	
	public void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
	}

	public A getSelectedA() {
		return selectedA;
	}

	public void setSelectedA(A selectedA) {
		this.selectedA = selectedA;
	}

	public List<A> getValuesA() {
		return valuesA;
	}

	public void setValuesA(List<A> valuesA) {
		this.valuesA = valuesA;
	}

	public B getSelectedB() {
		return selectedB;
	}

	public void setSelectedB(B selectedB) {
		this.selectedB = selectedB;
	}

	public List<B> getValuesB() {
		return valuesB;
	}

	public void setValuesB(List<B> valuesB) {
		this.valuesB = valuesB;
	}
    
}

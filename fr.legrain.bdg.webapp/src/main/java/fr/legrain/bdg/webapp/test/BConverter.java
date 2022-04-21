package fr.legrain.bdg.webapp.test;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@FacesConverter(forClass=B.class)
public class BConverter implements Converter, Serializable {
	
	private BService bService = new BService();

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object valueToRender) {
    	if (valueToRender == null || valueToRender.equals("")) {  
            return "";  
        } else {  
        	return String.valueOf(((B) valueToRender).getVal10());
        }  
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
    	if (submittedValue.trim().equals("")) {  
    		return null;  
    	} else {  
    		try {  
    			//int number = Integer.parseInt(submittedValue);  
    			return bService.getAllValues().get(bService.getAllValues().indexOf(submittedValue));
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
		return submittedValue;
    }

}
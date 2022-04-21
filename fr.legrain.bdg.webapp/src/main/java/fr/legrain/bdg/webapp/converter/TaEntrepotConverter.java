package fr.legrain.bdg.webapp.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

import fr.legrain.article.model.TaEntrepot;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.lib.ejb.EJBLookup;


@FacesConverter(forClass=TaEntrepot.class)
//@FacesConverter(value="taTTiersConverter")
//http://stackoverflow.com/questions/13156671/how-can-i-inject-in-facesconverter
//@Named
//@SessionScoped
public class TaEntrepotConverter implements Converter, Serializable {
	
	@EJB ITaEntrepotServiceRemote  taEntrepotService;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object valueToRender) {
    	if (valueToRender == null || valueToRender.equals("")) {  
            return "";  
        } else {  
        	return String.valueOf(((TaEntrepot) valueToRender).getCodeEntrepot());
        }  
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        // Convert String to MyObj.
    	if (submittedValue!=null && submittedValue.trim().equals("")) {  
    		return null;  
    	} else {  
    		try {  

    			//pas d'injection dans les converter avant jsf 2.3
    			taEntrepotService = new EJBLookup<ITaEntrepotServiceRemote>().doLookup(new InitialContext(),EJBLookup.EJB_NAME_TA_ENTREPOT_SERVICE, ITaEntrepotServiceRemote.class.getName());

    			return taEntrepotService.findByCode(submittedValue);

    		} catch (FinderException e) {
    			e.printStackTrace();
    		} catch (/*Naming*/Exception e) {
    			e.printStackTrace();
    		}
    	}
		return submittedValue;
    }

}
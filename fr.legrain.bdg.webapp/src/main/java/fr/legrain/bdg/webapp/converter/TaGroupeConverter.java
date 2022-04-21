package fr.legrain.bdg.webapp.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

import fr.legrain.bdg.conformite.service.remote.ITaGroupeServiceRemote;
import fr.legrain.conformite.model.TaGroupe;
import fr.legrain.lib.ejb.EJBLookup;


@FacesConverter(forClass=TaGroupe.class)
//@FacesConverter(value="taTTiersConverter")
//http://stackoverflow.com/questions/13156671/how-can-i-inject-in-facesconverter
//@Named
//@SessionScoped
public class TaGroupeConverter implements Converter, Serializable {
	
	@EJB ITaGroupeServiceRemote  taGroupeService;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object valueToRender) {
    	if (valueToRender == null || valueToRender.equals("")) {  
            return "";  
        } else {  
            //return String.valueOf(((User) valueToRender).getNom())+" "+String.valueOf(((User) valueToRender).getPrenom());  
        	return String.valueOf(((TaGroupe) valueToRender).getCodeGroupe());
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
    			//taTTiersService = (ITaTTiersServiceRemote) new InitialContext().lookup("java:global/fr.legrain.bdg.ear/fr.legrain.bdg.ejb/TaTTiersService!fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote");
    			taGroupeService = new EJBLookup<ITaGroupeServiceRemote>().doLookup(new InitialContext(),EJBLookup.EJB_NAME_TA_GROUPE_SERVICE, ITaGroupeServiceRemote.class.getName());

    			return taGroupeService.findByCode(submittedValue);

    		} catch (FinderException e) {
    			e.printStackTrace();
    		} catch (/*Naming*/Exception e) {
    			e.printStackTrace();
    		}
    	}
		return submittedValue;
    }

}
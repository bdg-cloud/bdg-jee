package fr.legrain.bdg.webapp.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

import fr.legrain.bdg.conformite.service.remote.ITaModeleGroupeServiceRemote;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.lib.ejb.EJBLookup;


@FacesConverter(forClass=TaModeleGroupe.class)
//@FacesConverter(value="taTTiersConverter")
//http://stackoverflow.com/questions/13156671/how-can-i-inject-in-facesconverter
//@Named
//@SessionScoped
public class TaModeleGroupeConverter implements Converter, Serializable {
	
	@EJB ITaModeleGroupeServiceRemote  taModeleGroupeService;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object valueToRender) {
    	if (valueToRender == null || valueToRender.equals("")) {  
            return "";  
        } else {  
            //return String.valueOf(((User) valueToRender).getNom())+" "+String.valueOf(((User) valueToRender).getPrenom());  
        	return String.valueOf(((TaModeleGroupe) valueToRender).getCodeGroupe());
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
    			taModeleGroupeService = new EJBLookup<ITaModeleGroupeServiceRemote>().doLookup(new InitialContext(),EJBLookup.EJB_NAME_TA_MODELE_GROUPE_SERVICE, ITaModeleGroupeServiceRemote.class.getName());

    			return taModeleGroupeService.findByCode(submittedValue);

    		} catch (FinderException e) {
    			e.printStackTrace();
    		} catch (/*Naming*/Exception e) {
    			e.printStackTrace();
    		}
    	}
		return submittedValue;
    }

}
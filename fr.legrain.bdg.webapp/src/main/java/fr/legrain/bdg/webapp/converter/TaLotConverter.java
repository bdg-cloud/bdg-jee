package fr.legrain.bdg.webapp.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.lib.ejb.EJBLookup;


@FacesConverter(forClass=TaLot.class)
//@FacesConverter(value="taTTiersConverter")
//http://stackoverflow.com/questions/13156671/how-can-i-inject-in-facesconverter
//@Named
//@SessionScoped
public class TaLotConverter implements Converter, Serializable {
	
	@EJB ITaLotServiceRemote  taLotService;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object valueToRender) {
    	if (valueToRender == null || valueToRender.equals("")) {  
            return "";  
        } else {  
            //return String.valueOf(((User) valueToRender).getNom())+" "+String.valueOf(((User) valueToRender).getPrenom());  
        	return String.valueOf(((TaLot) valueToRender).getNumLot());
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
    			taLotService = new EJBLookup<ITaLotServiceRemote>().doLookup(new InitialContext(),EJBLookup.EJB_NAME_TA_LOT_SERVICE, ITaLotServiceRemote.class.getName(),true);

    			return taLotService.findByCode(submittedValue);

    		} catch (FinderException e) {
    			e.printStackTrace();
    		} catch (/*Naming*/Exception e) {
    			e.printStackTrace();
    		}
    	}
		return submittedValue;
    }

}
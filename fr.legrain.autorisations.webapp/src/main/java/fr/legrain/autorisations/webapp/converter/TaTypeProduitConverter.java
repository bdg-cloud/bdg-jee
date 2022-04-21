package fr.legrain.autorisations.webapp.converter;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

import fr.legrain.autorisations.autorisation.model.TaTypeProduit;
import fr.legrain.autorisations.ejb.lib.osgi.EJBLookup;
import fr.legrain.bdg.autorisations.service.remote.ITaTypeProduitServiceRemote;


@FacesConverter(forClass=TaTypeProduit.class)
//http://stackoverflow.com/questions/13156671/how-can-i-inject-in-facesconverter
//@ManagedBean
//@SessionScoped
public class TaTypeProduitConverter implements Converter {
	
	@EJB ITaTypeProduitServiceRemote  taService;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object valueToRender) {
    	if (valueToRender == null || valueToRender.equals("")) {  
            return "";  
        } else {  
            //return String.valueOf(((User) valueToRender).getNom())+" "+String.valueOf(((User) valueToRender).getPrenom());  
        	return String.valueOf(((TaTypeProduit) valueToRender).getCode());
        }  
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String submittedValue) {
        // Convert String to MyObj.
    	if (submittedValue.trim().equals("")) {  
    		return null;  
    	} else {  
    		try {  
//    			int number = Integer.parseInt(submittedValue);  

    			//pas d'injection dans les converter avant jsf 2.3
//    			taTCiviliteService = (ITaTCiviliteServiceRemote) new InitialContext().lookup("java:global/fr.careco.ear/fr.careco.ejb.mvn/UsersService!fr.legrain.bdg.tiers.service.remote.IUserServiceRemote");
    			taService = new EJBLookup<ITaTypeProduitServiceRemote>().doLookup(new InitialContext(),EJBLookup.EJB_NAME_TA_TYPE_PRODUIT_SERVICE, ITaTypeProduitServiceRemote.class.getName());

    			return taService.findByCode(submittedValue);
//    			return taUniteService.findById(number);

    		} catch (FinderException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (/*Naming*/Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
		return submittedValue;
    }

}
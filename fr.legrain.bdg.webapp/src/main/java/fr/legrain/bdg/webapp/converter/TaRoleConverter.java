package fr.legrain.bdg.webapp.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

import fr.legrain.bdg.droits.service.remote.ITaRoleServiceRemote;
import fr.legrain.droits.model.TaRole;
import fr.legrain.lib.ejb.EJBLookup;


@FacesConverter(forClass=TaRole.class)
//http://stackoverflow.com/questions/13156671/how-can-i-inject-in-facesconverter
//@Named
//@SessionScoped
public class TaRoleConverter implements Converter, Serializable {
	
	@EJB ITaRoleServiceRemote  taRoleService;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object valueToRender) {
    	if (valueToRender == null || valueToRender.equals("")) {  
            return "";  
        } else {  
            //return String.valueOf(((User) valueToRender).getNom())+" "+String.valueOf(((User) valueToRender).getPrenom());  
        	return String.valueOf(((TaRole) valueToRender).getRole());
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
    			taRoleService = new EJBLookup<ITaRoleServiceRemote>().doLookup(new InitialContext(),EJBLookup.EJB_NAME_TA_ROLE_SERVICE, ITaRoleServiceRemote.class.getName());

    			return taRoleService.findByCode(submittedValue);
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
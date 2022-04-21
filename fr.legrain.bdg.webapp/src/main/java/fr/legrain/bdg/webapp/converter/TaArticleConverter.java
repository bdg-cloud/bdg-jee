package fr.legrain.bdg.webapp.converter;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.lib.ejb.EJBLookup;


@FacesConverter(forClass=TaArticle.class)
//@FacesConverter(value="taTTiersConverter")
//http://stackoverflow.com/questions/13156671/how-can-i-inject-in-facesconverter
//@Named
//@SessionScoped
public class TaArticleConverter implements Converter, Serializable {
	
	@EJB ITaArticleServiceRemote  taArticleService;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object valueToRender) {
    	if (valueToRender == null || valueToRender.equals("")) {  
            return "";  
        } else {  
            //return String.valueOf(((User) valueToRender).getNom())+" "+String.valueOf(((User) valueToRender).getPrenom());  
        	return String.valueOf(((TaArticle) valueToRender).getCodeArticle());
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
    			taArticleService = new EJBLookup<ITaArticleServiceRemote>().doLookup(new InitialContext(),EJBLookup.EJB_NAME_TA_ARTICLE_SERVICE, ITaArticleServiceRemote.class.getName(),false);

    			return taArticleService.findByCode(submittedValue);

    		} catch (FinderException e) {
    			e.printStackTrace();
    		} catch (/*Naming*/Exception e) {
    			e.printStackTrace();
    		}
    	}
		return submittedValue;
    }

}
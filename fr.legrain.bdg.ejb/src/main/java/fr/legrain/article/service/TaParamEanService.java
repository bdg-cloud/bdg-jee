package fr.legrain.article.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import fr.legrain.bdg.article.service.remote.ITaParamEan128ServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEanServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

/**
 * Session Bean implementation class TaParamEan128Bean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaParamEanService  implements ITaParamEanServiceRemote {

	static Logger logger = Logger.getLogger(TaParamEanService.class);
	private @EJB ITaParamEan128ServiceRemote taParamEan128Service;
	/**
	 * Default constructor. 
	 */
	public TaParamEanService() {
	}
	
	public Map<String, String>  decodeEan(String codeBarre) throws Exception {
		if(codeBarre!=null){
			if(codeBarre.trim().length()==13) {
				Map<String, String> listeRetour = new LinkedHashMap<String, String>();
				listeRetour.put("EAN13", codeBarre);
				return listeRetour;
			}else
				return taParamEan128Service.decodeEan128(codeBarre);
		}else return null;
	}
	
	public String encodeEan(String ai,String valeur) throws Exception {
		if(ai.equals("EAN13")) return valeur;
		return taParamEan128Service.encodeEan128(ai,valeur);
	}
	
	public String encodeEan(String ai,Date valeur) throws Exception {
		return taParamEan128Service.encodeEan128(ai,valeur);
	}
	
	public String encodeEan(String ai,BigDecimal valeur) throws Exception {
		return taParamEan128Service.encodeEan128(ai,valeur);
	}
	
	 public String encodeEan(Map<String, Object> listeAiValeur) throws Exception {
		 String retour="";
			if(listeAiValeur!=null){
				if(!listeAiValeur.isEmpty()) {
					for (String key : listeAiValeur.keySet()) {
						if(key.equals("EAN13"))
							retour=(String) listeAiValeur.get(key);
						else {
							Object o=listeAiValeur.get(key);
							if(o instanceof String)	retour+= taParamEan128Service.encodeEan128(key,(String)o);
							if(o instanceof Date)	retour+= taParamEan128Service.encodeEan128(key,(Date)o);
							if(o instanceof BigDecimal)	retour+= taParamEan128Service.encodeEan128(key,(BigDecimal)o);
						}
					}
					return retour;
				}
					
			}
			return retour;
	}
	
}

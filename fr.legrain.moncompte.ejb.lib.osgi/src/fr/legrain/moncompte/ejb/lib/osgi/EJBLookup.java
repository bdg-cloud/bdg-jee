package fr.legrain.moncompte.ejb.lib.osgi;

import javax.naming.Context;
import javax.naming.NamingException;

public class EJBLookup<I> extends EJBLookupName {
	
	public static final String EJB_NAME_TA_TIERS_SERVICE      = "TaTiersService";
	public static final String EJB_NAME_TA_TIERS_SERVICE_STATELESS  = "TaTiersServiceStateless";
	public static final String EJB_NAME_TA_TIERS_SERVICE_STATEFUL  = "TaTiersServiceStatefull";	
	
	public static final String EJB_NAME_TA_TYPE_PRODUIT_SERVICE      = "TaTypeProduitService";
	public static final String EJB_NAME_TA_PRODUIT_SERVICE      = "TaProduitService";
	
	public I doLookup(Context context, String beanName, final String interfaceName) {
		return doLookup(context, beanName, interfaceName, false);
	}


	/**
	 * I - interface de l'EJB rechercher : ex : (fr.legrain.bdg.tiers.service.remote.)ITaTiersServiceRemote
	 * beanName - nom du bean : ex : TaTiersService
	 * @return
	 */
	public I doLookup(Context context, String beanName, final String interfaceName, boolean statefull) {
		//Context context = null;
		I bean = null;
		try {
			// 1. Obtaining Context
			//context = JNDILookupClass.getInitialContext();
			// 2. Generate JNDI Lookup name
			//String beanName = "TaTiersService";
			//final String interfaceName = I.class.getName();
			String lookupName = getLookupName(beanName,interfaceName);
			
			if(statefull) {
				lookupName += "?stateful";
			}
			
			System.err.println(lookupName);
			
			// 3. Lookup and cast
			bean = (I) context.lookup(lookupName);

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return bean;
	}

}

package fr.legrain.bdg.webapp.edition.birt;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;

import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.document.model.TaFacture;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.ejb.EJBLookup;

//https://wiki.eclipse.org/BIRT/FAQ/Data_Access#Q:_How_do_I_get_data_from_an_EJB.3F
public class BirtEJB {
	
//	private InitialContext initContext = null;
//    private TaFacture myGreeterBean;
    private ITaFactureServiceRemote taFactureService;
    private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
//    private TaFacture myGreeterRemote;
    
    private TransactionSynchronizationRegistry reg;
    
    private void initTenant(String tenant) {
		reg.putResource(ServerTenantInterceptor.TENANT_TOKEN_KEY,tenant);
	}
	
	private void initTenantRegistry() {
		try {
			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
    
	
	// Example Methods
	public void setupEJB() throws Exception {
		
		
		taFactureService = new EJBLookup<ITaFactureServiceRemote>().doLookup(new InitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaFactureServiceRemote.class.getName(),false);
		taInfoEntrepriseService = new EJBLookup<ITaInfoEntrepriseServiceRemote>().doLookup(new InitialContext(),EJBLookup.EJB_NAME_TA_FACTURE_SERVICE, ITaInfoEntrepriseServiceRemote.class.getName(),false);

		
//	     Properties p = new Properties();
//	     p.put("java.naming.factory.initial", contextString );
//	     p.put("java.naming.provider.url", providerString );
//	     initContext = new javax.naming.InitialContext(p);
//	     Object objref = initContext.lookup(JNDIName);
//	     ITaFactureServiceRemote myGreeterHome = (ITaFactureServiceRemote)PortableRemoteObject.narrow(objref,ITaFactureServiceRemote.class);
	}
	
	public TaInfoEntreprise findInfos(String codeDossierFournisseur) throws Exception {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		return taInfoEntrepriseService.findInstance();
	}
	
	public TaFacture makeCall(String codeDossierFournisseur, String codeFacture) throws Exception {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		
		return taFactureService.findByCode(codeFacture);
		
//	     myGreeterRemote = myGreeterHome.create();
//	     String theMessage = myGreeterRemote.getGreeting();
//	     return (theMessage);
	}
}

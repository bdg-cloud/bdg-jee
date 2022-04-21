package fr.legrain.bdg.synchronisation.dossier.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.synchronisation.dossier.service.remote.ISynchronisationDossierServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;
import fr.legrain.tiers.model.IConstWS;

@Stateless
public class SynchronisationDossierService implements ISynchronisationDossierServiceRemote {
	
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private SynchronisationDossierMultitenantProxy multitenantProxy;
	
	private MonCompteWebServiceClientCXF wsMonCompte;
	private TransactionSynchronizationRegistry reg;
	
	@PostConstruct
	public void init() {
		try {
			wsMonCompte = new MonCompteWebServiceClientCXF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initTenant(String tenant) {
//		try {
//			wsMonCompte = new MonCompteWebServiceClientCXF();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
	
	public List<TaArticle> listeArticle(String codeDossierSource) throws EJBException{
		initTenantRegistry();
		initTenant(codeDossierSource);
		try {
			return multitenantProxy.listeArticle(codeDossierSource);
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}
	
	public List<TaArticle> synchronisationArticle(String codeDossierSource) throws EJBException {
		return null;
	}


}

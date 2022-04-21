package fr.legrain.bdg.synchronisation.dossier.service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.compteclientfinal.remote.ICompteClientFinalServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.paiement.service.MyQualifierLiteral;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.model.TiersDossier;

/**
 * http://stackoverflow.com/questions/28480480/propagation-requires-new-does-not-create-a-new-transaction-in-spring-with-jpa
 * 
 * Le tenant est vérifié une seule fois au début de la transaction. La transaction est par défaut lié à l'appel d'une méthode de l'EJB.
 * Dans une cas ou une méthode d'EJB doit accéder à plusieurs tenant, il faut donc pouvoir démarrer une nouvelle transaction pour chaque requette à la base de données.
 * On peut utiliser la valeur REQUIRES_NEW de l'anotation @Transactional sur la méthode concerné, mais cette méthode doit obligatoirement se trouvé dans un autre bean
 * pour que JEE puisse l'injecter et démarrer une nouvelle transaction. Si la méthode est dans le même bean, il n'y a pas d'inject et pas d'interprétation de l'annotation
 * @Transactional et donc pas de nouvelle transaction.
 * 
 * @author nicolas
 *
 */
@Stateless
public class SynchronisationDossierMultitenantProxy {

	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaDevisServiceRemote taDevisService;

//	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
//	@Inject @Any private Instance<IPaiementEnLigneDossierService> iServicePaiementEnLigneDossierInstance;

	private String tenant = null;

	private TransactionSynchronizationRegistry reg;

	@PostConstruct
	public void init() {

	}

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

	@Transactional(value=TxType.REQUIRES_NEW)
	public TaInfoEntreprise infosMultiTenant(String tenant) {
		initTenantRegistry();
		initTenant(tenant);
		TaInfoEntreprise infosEts = taInfoEntrepriseService.findInstance();
		return infosEts;
	}

	@Transactional(value=TxType.REQUIRES_NEW)
	public List<TaArticle> listeArticle(String codeDossierSource) throws EJBException{
		initTenantRegistry();
		initTenant(codeDossierSource);

//		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		List<TaArticle> l = taArticleService.selectAll();

		if(l!=null) {

			return l;
		}

		return null;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

}

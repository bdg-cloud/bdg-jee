package fr.legrain.bdg.rest.proxy.multitenant;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.bdg.compteclientfinal.service.IEtatPaiementCourant;
import fr.legrain.bdg.compteclientfinal.service.LgrEmail;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.rest.model.ParamCompteEspaceClient;
import fr.legrain.bdg.rest.model.ParamEmailConfirmationCommandeBoutique;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaLAvisEcheanceDTO;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.email.service.EmailProgrammePredefiniService;
import fr.legrain.general.model.TaAliasEspaceClient;
import fr.legrain.general.service.remote.ITaAliasEspaceClientServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.paiement.service.MyQualifierLiteral;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaParamEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.service.TaEmailService;

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
public class PanierRestMultitenantProxy extends AbstractRestMultitenantProxy implements IPanierRestMultitenantProxy {

	@EJB private ITaPanierServiceRemote taPanierService;

	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	@Inject @Any private Instance<IPaiementEnLigneDossierService> iServicePaiementEnLigneDossierInstance;
	@Inject private	TenantInfo tenantInfo;

	private String tenant = null;
	private BdgProperties bdgProperties;
	@EJB private LgrEmail lgrEmail;
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaBoncdeServiceRemote taBoncdeService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
	
	@EJB private EmailProgrammePredefiniService emailProgrammePredefiniService;

	private TransactionSynchronizationRegistry reg;

	
	
	@Transactional(value=TxType.REQUIRES_NEW)
	public TaPanier merge(String codeDossierFournisseur, TaPanier detachedInstance) {
		try {
			initTenantRegistry();
			initTenant(codeDossierFournisseur);
			return taPanierService.merge(detachedInstance);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
//	@SuppressWarnings("unused")
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public void emailConfirmationCommandeBoutique(String codeDossierFournisseur,int id, ParamEmailConfirmationCommandeBoutique paramEmailConfirmationCommandeBoutique) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//
//		try {
//	    	TaPanier t = new TaPanier();
//	    	TaPanierDTO dto = new TaPanierDTO();
//	    	TaBoncde cmd = null;
//	    	TaEspaceClient compteEspaceClient = null;
//    	
//	   		
//				//t = taPanierService.findById(id);
//    		t = taPanierService.findByIDFetch(id);
//    		
//    		//if(paramEmailConfirmationCommandeBoutique.getCodeCommande()!=null) {
//    			//cmd = taBoncdeService.findByCodeFetch(paramEmailConfirmationCommandeBoutique.getCodeCommande());
//    			cmd = taPanierService.findCommandePanier(t.getCodeDocument());
//    		//}
//    		compteEspaceClient = taEspaceClientService.findById(paramEmailConfirmationCommandeBoutique.getIdcompteEspaceClient());
//    		TaReglement reg = taPanierService.findReglementPourPanier(t.getCodeDocument());
//    		TaStripePaymentIntent pi = taPanierService.findReglementStripePanier(t.getCodeDocument());
//    		TaTiers tiers = taTiersService.findByCode(paramEmailConfirmationCommandeBoutique.getCodeTiers());
//    		//if(t) //verifier que le panier appartient bien a ce client et peut être revérifier qu'il y a bien une commande
//			emailProgrammePredefiniService.emailConfirmationCommandeBoutique(t,cmd,compteEspaceClient,tiers,reg,pi);
//				
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	    	
//	}    

}

package fr.legrain.bdg.compteclientfinal.service;

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
import fr.legrain.paiement.service.PaiementGeneralDossierService;
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
public class CompteClientSoapMultitenantProxy implements ICompteClientSoapMultitenantProxy{

	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private PaiementGeneralDossierService paiementGeneralDossierService;

	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	@Inject @Any private Instance<IPaiementEnLigneDossierService> iServicePaiementEnLigneDossierInstance;

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
	@Deprecated
	public RetourPaiementCarteBancaire payerFactureCB(String codeDossierFournisseur, String codeClientChezCeFournisseur, PaiementCarteBancaire cb, TaFacture facture) throws EJBException{
		initTenantRegistry();
		initTenant(codeDossierFournisseur);

		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);
		if(service!=null) {
			return service.payer(compte, cb, facture, null);
		}
//		if(compte!=null) {
//
//			String implementation = "";
//
//			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
//			case UtilServiceWebExterne.CODE_SWE_STRIPE:
//				implementation = PaiementLgr.TYPE_STRIPE;
//				break;
//			case UtilServiceWebExterne.CODE_SWE_PAYPAL:
//				implementation = PaiementLgr.TYPE_PAYPAL;
//				break;
//			default:
//				break;
//			}
//			
//			IPaiementEnLigneDossierService service = iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
//			
////			try {
//				return service.payer(compte, cb, facture, null);
////			} catch(EJBException e) {
////				throw new EJBException(e);
////			}
//		}

		return null;
	}
	
	public boolean fournisseurPermetPaiementParCB(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);

		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT); 
		
		if(compte!=null) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional(value=TxType.REQUIRES_NEW)
	public boolean clientExisteChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur, String cleLiaisonCompteClient) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);

		if(cleLiaisonCompteClient!=null && !cleLiaisonCompteClient.equals("")) {
			cleLiaisonCompteClient = cleLiaisonCompteClient.trim();
			try {
				TaTiers t = taTiersService.findByCode(codeClientChezCeFournisseur);

				if(t!=null) {
					if(t.getCleLiaisonCompteClient()!=null && t.getCleLiaisonCompteClient().trim().equals(cleLiaisonCompteClient)) {
						t.setUtiliseCompteClient(true);
						t.setDateDerniereConnexionCompteClient(new Date());
						t = taTiersService.merge(t);
						return true;
					} else {
						System.out.println("Le client ("+codeClientChezCeFournisseur+") existe bien chez ce fournisseur ("+codeDossierFournisseur+") mais la clé de liaison ("+cleLiaisonCompteClient+") est incorrecte.");
					}
				} else {
					return false;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false; 
	}

	@Transactional(value=TxType.REQUIRES_NEW)
	public TaTiers infosClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);

		try {
			TaTiers t = taTiersService.findByCode(codeClientChezCeFournisseur);
			return t;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(value=TxType.REQUIRES_NEW)
	public byte[] pdfClient(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);

		try {
			TaTiers t = taTiersService.findByCode(codeClientChezCeFournisseur);
			String pdfPath = taTiersService.generePDF(t.getIdTiers());

			Path path = Paths.get(pdfPath);
			byte[] b = Files.readAllBytes(path);
			return b;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(value=TxType.REQUIRES_NEW)
	public byte[] pdfFacture(String codeDossierFournisseur, String codeFactureChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);

		try {
			TaFacture t = taFactureService.findByCode(codeFactureChezCeFournisseur);
			String pdfPath = taFactureService.generePDF(t.getIdDocument(), null, null, null);

			Path path = Paths.get(pdfPath);
			byte[] b = Files.readAllBytes(path);
			return b;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Transactional(value=TxType.REQUIRES_NEW)
	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);

		//List<TaFacture> listeDoc = taFactureService.findByCodeTiersAndDate(codeClientChezCeFournisseur, debut, fin);

		//return listeDoc;
		return null;
	}

	@Transactional(value=TxType.REQUIRES_NEW)
	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur,
			String codeClientChezCeForunisseur, Date debut, Date fin) {

		initTenantRegistry();
		initTenant(codeDossierFournisseur);
		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
		t.setUtiliseCompteClient(true);
		t.setDateDerniereConnexionCompteClient(new Date());
		t = taTiersService.merge(t);
		if(t.getUtiliseCompteClient()!=null && t.getUtiliseCompteClient()==true) {
			List<TaFacture> listeDoc = taFactureService.findByCodeTiersAndDateCompteClient(codeClientChezCeForunisseur, debut, fin);
//			for (TaFacture taFacture : listeDoc) {
//				taFacture.calculSommeAvoirIntegres();
//				System.out.println("Facture : "+taFacture.getAvoirsComplet());
//			}
			return listeDoc;
		} else {
			System.out.println("Le service compte client n'est pas actif pour le client "+codeClientChezCeForunisseur+" chez le fournisseur "+codeDossierFournisseur);
			return new ArrayList<TaFacture>();
		}


	}


	public List<TaDevis> devisClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeForunisseur) {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional(value=TxType.REQUIRES_NEW)
	public List<TaDevis> devisClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeForunisseur,
			Date debut, Date fin) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

}

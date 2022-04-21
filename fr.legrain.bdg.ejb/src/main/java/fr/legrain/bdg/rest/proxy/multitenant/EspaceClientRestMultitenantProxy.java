package fr.legrain.bdg.rest.proxy.multitenant;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
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

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.article.model.TaTTva;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeAccountServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTTvaServiceRemote;
import fr.legrain.bdg.compteclientfinal.service.IEtatPaiementCourant;
import fr.legrain.bdg.compteclientfinal.service.LgrEmail;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
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
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurWebServiceServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.rest.filters.request.JWTTokenGlobalFilter;
import fr.legrain.bdg.rest.model.ParamCompteEspaceClient;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTvaDocServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaLAvisEcheanceDTO;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaPanier;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.model.TaUtilisateurWebService;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.general.model.TaAliasEspaceClient;
import fr.legrain.general.service.remote.ITaAliasEspaceClientServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.paiement.service.LgrStripe;
import fr.legrain.paiement.service.MyQualifierLiteral;
import fr.legrain.paiement.service.PaiementGeneralDossierService;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.dto.TaParamEspaceClientDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTTvaDoc;
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
public class EspaceClientRestMultitenantProxy extends AbstractRestMultitenantProxy implements IEspaceClientRestMultitenantProxy {

	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaParamEspaceClientServiceRemote taParamEspaceClientService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
//	@EJB private ITaLFactureServiceRemote taLFactureService;
//	@EJB private ITaDevisServiceRemote taDevisService;
//	@EJB private ITaBoncdeServiceRemote taBoncdeService;
	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaPanierServiceRemote taPanierService;   
//	@EJB private ITaLAvisEcheanceServiceRemote taLAvisEcheanceService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
//	@EJB private ITaActionEditionServiceRemote taActionEditionService;
	
//	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
//	private @EJB ITaTTiersServiceRemote taTTiersService;
//	private @EJB ITaTTvaDocServiceRemote taTTvaDocService;
	
	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService paiementStripeDossierService;
	
	@EJB private ITaAbonnementServiceRemote taAbonnementService;
	
	@EJB protected ITaUtilisateurServiceRemote taUtilisateurService;
	@EJB protected ITaUtilisateurWebServiceServiceRemote taUtilisateurWebServiceService;
	@EJB protected ITaAliasEspaceClientServiceRemote taAliasEspaceClientService;

	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	@EJB private ITaStripeAccountServiceRemote taStripeAccountService;
	@EJB private PaiementGeneralDossierService paiementGeneralDossierService;
	@Inject @Any private Instance<IPaiementEnLigneDossierService> iServicePaiementEnLigneDossierInstance;
	@Inject private	TenantInfo tenantInfo;
//	private @Inject @PaiementLgr(PaiementLgr.TYPE_STRIPE) IPaiementEnLigneDossierService iServicePaiementEnLigneDossierInstance;

	private Integer idTiers = null; //TODO A supprimer vite et utilisation d'un table de relation pour gerer le multi compte tiers

	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public TaInfoEntreprise infosMultiTenant(String tenant) {
		initTenantRegistry();
		initTenant(tenant);
		TaInfoEntreprise infosEts = taInfoEntrepriseService.findInstance();
		return infosEts;
	}
	
	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public TaTiersDTO infosTiersMultiTenantDTO(String tenant) {
		initTenantRegistry();
		initTenant(tenant);
		TaInfoEntreprise infosEts = taInfoEntrepriseService.findInstance();
		TaTiersDTO t = null;
		try {
			t = taTiersService.findByIdDTO(infosEts.getTaTiers().getIdTiers());
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public TaEspaceClientDTO creerCompteEspaceClient(ParamCompteEspaceClient p) throws Exception {
//		
//		ParamCompteEspaceClient paramCompteEspaceClient = new ParamCompteEspaceClient();
//		p.decrypte();
//		paramCompteEspaceClient = p;
//		
//		Calendar cal = Calendar.getInstance(); // creates calendar
//		cal.setTime(p.getDateLimiteLien());               // sets calendar time/date
//		cal.add(Calendar.DAY_OF_WEEK, 1);      // adds one hour
//		Date dateValidite = cal.getTime();  
//		
//		if(dateValidite.before(new Date())) {
//			throw new Exception("Ce liens n'est plus valide.");
//		}
//		
//		//ParamCompteEspaceClient
//		initTenantRegistry();
//		initTenant(paramCompteEspaceClient.getCodeDossierFournisseur());
//		TaEspaceClientDTO retour = null;
////		try {
//		TaParamEspaceClient param = taParamEspaceClientService.findInstance();
//		
//			TaEspaceClient taEspaceClient = taEspaceClientService.findByCode(paramCompteEspaceClient.getEmail());
//			if(taEspaceClient!=null) {
//				throw new Exception("Un compte avec cette adresse email existe déjà.");
//			}
//		
//			TaTiers taTiers = null;
//			if(param.getLiaisonNouveauCompteEmailAuto()!=null && param.getLiaisonNouveauCompteEmailAuto()) {
//				List<TaTiers>  listeTiers = taTiersService.findByEmailParDefaut(paramCompteEspaceClient.getEmail());
//				if(listeTiers!=null && !listeTiers.isEmpty()) {
//					taTiers = listeTiers.get(0);
//				}
//				if(taTiers==null) {
//					//pas de tiers correspondant, lever une exception ?
//				}
//			}
//			
////			if(paramCompteEspaceClient.getCodeTiers()!=null) {
////				//taTiers = taTiersService.findByCode(p.getCodeClientChezCeFournisseur());
////				//taTiers = taTiersService.findByCode(paramCompteEspaceClient.getCodeTiers());
////				if(taTiers==null) {
////					//pas de tiers correspondant, lever une exception ?
////				}
////			}
//			taEspaceClient = new TaEspaceClient();
//			taEspaceClient.setTaTiers(taTiers);
//			taEspaceClient.setEmail(paramCompteEspaceClient.getEmail());
//			taEspaceClient.setPassword(paramCompteEspaceClient.getPassword());
//			taEspaceClient.setNom(paramCompteEspaceClient.getNom());
//			taEspaceClient.setPrenom(paramCompteEspaceClient.getPrenom());
//			taEspaceClient.setActif(true);
//			taEspaceClient = taEspaceClientService.merge(taEspaceClient);
//			retour = taEspaceClientService.findByIdDTO(taEspaceClient.getIdEspaceClient());
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		return retour;
//		
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public TaEspaceClientDTO modifierCompteEspaceClient(String codeDossierFournisseur, ParamCompteEspaceClient p) throws Exception {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClientDTO retour = null;
////		try {
//			TaTiers taTiers = null;
//			if(p.getCodeTiers()!=null) {
//				taTiers = taTiersService.findByCode(p.getCodeTiers());
//			}
//			TaEspaceClient taEspaceClient = taEspaceClientService.findById(p.getId());
//			taEspaceClient.setTaTiers(taTiers);
////			taEspaceClient.setEmail(p.getEmail());
////			taEspaceClient.setPassword(p.getPassword());
////			taEspaceClient.setActif(true);
//			taEspaceClientService.merge(taEspaceClient);
//			retour = taEspaceClientService.findByIdDTO(taEspaceClient.getIdEspaceClient());
////		} catch (FinderException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		return retour;
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public TaEspaceClientDTO liaisonNouveauCompteEspaceClient(String codeDossierFournisseur, ParamCompteEspaceClient p) throws Exception {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClientDTO retour = null;
////		try {
//			TaTiers taTiers = null;
//			TaEspaceClient taEspaceClient = null;
//			if(p.getCodeTiers()!=null) {
//				taEspaceClient = taEspaceClientService.findByCodeTiers(p.getCodeTiers());
//				if(taEspaceClient!=null && !taEspaceClient.getEmail().equals(p.getEmail())) {
//					throw new Exception("Ce code tiers est déjà lié à une autre adresse email");
//				}
//				
//				taTiers = taTiersService.findByCode(p.getCodeTiers());
//				if(taTiers==null) {
//					throw new Exception("Ce code tiers introuvable");
//				}
//			} else {
//				taEspaceClient = taEspaceClientService.findById(p.getId());
//				String codeTypeTiersDefaut = "VISIT";
//				TaTTiers taTTiers;
//				taTTiers = taTTiersService.findByCode(codeTypeTiersDefaut);
//				
//				String codeTypeTvaDefaut = "F";
//				TaTTvaDoc taTTvaDoc;
//				taTTvaDoc = taTTvaDocService.findByCode(codeTypeTvaDefaut);
//				
//				taTiers = new TaTiers();
////				taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
//				taTiers.setCodeTiers(taTiersService.genereCode(null));
//				taTiers.setCodeCompta(taTiers.getCodeTiers());
//				taTiers.setTaTTiers(taTTiers);
//				taTiers.setCompte("411");
//				taTiers.setNomTiers(taEspaceClient.getNom());
//				taTiers.setPrenomTiers(taEspaceClient.getPrenom());
//				
//				TaEmail taEmail = new TaEmail();
//				taEmail.setTaTiers(taTiers);
//				taEmail.setAdresseEmail(taEspaceClient.getEmail());
//				taTiers.setTaEmail(taEmail);
//				taTiers.addEmail(taEmail);
//
//				if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
//					taTiers.setCompte(taTTiers.getCompteTTiers());
//				} 
//				taTiers.setActifTiers(1);
//				taTiers.setTtcTiers(0);
//				taTiers.setTaTTvaDoc(taTTvaDoc);
//				taTiers = taTiersService.merge(taTiers);
//			}
//			taEspaceClient = taEspaceClientService.findById(p.getId());
//			taEspaceClient.setTaTiers(taTiers);
////			taEspaceClient.setEmail(p.getEmail());
////			taEspaceClient.setPassword(p.getPassword());
////			taEspaceClient.setActif(true);
//			taEspaceClientService.merge(taEspaceClient);
//			retour = taEspaceClientService.findByIdDTO(taEspaceClient.getIdEspaceClient());
////		} catch (FinderException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		return retour;
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public TaEspaceClientDTO demandeMotDePasse(ParamCompteEspaceClient p) {
//		initTenantRegistry();
//		initTenant(p.getCodeDossierFournisseur());
//		TaEspaceClientDTO retour = null;
//		try {
//			TaEspaceClient taEspaceClient = null;
//			if(p.getEmail()!=null) {
//				taEspaceClient = taEspaceClientService.findByCode(p.getEmail());
//			}
//			if(taEspaceClient != null) {
//					System.out.println("MdpOublieBean.demandeMotDePasse() => adresseEmail : "+p.getEmail());
//
////					String a = p.getCodeDossierFournisseur()+separateurChaineCryptee+p.getEmail();
////					String b = LibCrypto.encrypt(a);
//					
//					p.setDateLimiteLien(null);
//					p.crypte();
//
//					//		String subject = "mon sujet ..";
//					//		String msgTxt = "mon email ...";
//					//		String fromEmail = "nicolas@legrain.fr";
//					//		String fromName = "Nicolas";
//					//		String[][] toEmailandName = new String[][]{ {"nico290382@gmail.com","NP"}};
//
//					//String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Demande de changement de mot de passe";
//					String subject = p.getHostname()+" - Demande de changement de mot de passe";
//
//					String utilisateur = p.getEmail();
//					String lienCnx = "";
//					//		String lienCnx = "http://"+bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"")+tenantDossier+"."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+":"+bdgProperties.getProperty(BdgProperties.KEY_PORT_HTTP,"")+"/login/mdp_validation.xhtml?p="+URLEncoder.encode(b, "UTF-8")+"";
////					lienCnx = bdgProperties.url("espaceclient")+"/login/mdp_validation.xhtml?p="+URLEncoder.encode(p.infoCompteCryptees, "UTF-8")+"";
//					String port = p.getPort()!=null?(!p.getPort().equals("80")?":"+p.getPort():""):"";
//					lienCnx = "https://"+p.getHostname()+port+"/#/validation-modif-mdp?p="+URLEncoder.encode(p.infoCompteCryptees, "UTF-8")+"";
//					
//					String lienCnxTxt = "";
//					String msgTxt = "Bonjour "+utilisateur+",\n"
//							+"\n"
//							+"nous avons reçu une demande de modification de mot passe pour votre dossier Bureau de Gestion.\n"
//							+"\n"
//							+"Afin de finaliser ce changement de mot de passe, merci de cliquer sur le lien suivant :\n"
//							+"\n"
//							+""+lienCnx+"\n"
//							+"\n"
//							+"Ce lien sera valide pendant 24 heures seulement.\n"
//							+"\n"
//							//+"Si vous ne pouvez pas cliquer sur le lien, copiez le entièrement et collez le dans la barre d'adresse de votre navigateur puis validez.\n"
//							//+"\n"
//							//+""+lienCnxTxt+"\n"
//							//+"\n"
//							+"Si vous n'êtes pas à l'origine de cette demande, ignorez ce message. Vous conserverez vos identifiants habituels pour vous connecter sur le Bureau de Gestion.\n"
//							+"\n"
//							+"Cordialement,\n"
//							+"\n"
//							+"Service Gestion.\n"
//							+"\n"
//							+"Le Bureau de Gestion est un service en ligne dédié à la gestion des entreprises proposé par Legrain SAS. \n"
//							;
//					String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
//					String fromName = "Bureau de Gestion";
//					String[][] toEmailandName = new String[][]{ {p.getEmail(),""}};
//
//					lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//
//					//envoie d'une copie à legrain
//					toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};//Zee6Zasa
//					lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//					//TODO faire avec BCC
//
////					FacesContext context = FacesContext.getCurrentInstance();  
////					context.addMessage(null, new FacesMessage("", 
////							"Vous allez recevoir un email pour changer votre mot de passe à l'adresse suivante : "+p.getEmail()
////							)); 
////
////					actionOk = true;
//			} else {
//				//ERREUR
//				//Aucun utilisateur avec cet identifiant n'existe dans le dossier : "+tenantDossier, 
//			}
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return retour;
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public TaEspaceClientDTO validationMotDePasse(ParamCompteEspaceClient p) throws Exception {
//		initTenantRegistry();
//		initTenant(p.getCodeDossierFournisseur());
//		
//		Calendar cal = Calendar.getInstance(); // creates calendar
//		cal.setTime(p.getDateLimiteLien());               // sets calendar time/date
//		cal.add(Calendar.DAY_OF_WEEK, 1);      // adds one hour
//		Date dateValidite = cal.getTime();  
//		
//		if(dateValidite.before(new Date())) {
//			throw new Exception("Ce liens n'est plus valide.");
//		}
//		
//		try {
//			TaEspaceClient taEspaceClient = null;
//			if(p.getEmail()!=null) {
//				taEspaceClient = taEspaceClientService.findByCode(p.getEmail());
//			}
//			if(taEspaceClient != null) {
//	
//				if(p.getPassword()!=null && !p.getPassword().equals("") 
//						&& p.getPasswordConf()!=null && !p.getPasswordConf().equals("") 
//						&& p.getPassword().equals(p.getPasswordConf())) {
//					//le nouveau mot de passe saisie est correct
//					TaUtilisateur taUtilisateur = new TaUtilisateur();
//					p.setPassword(taUtilisateur.passwordHashSHA256_Base64(p.getPassword()));
//	
//					if(true /*verifComplexiteMotDePasse(p.getPassword())*/) {
//						taEspaceClient.setPassword(p.getPassword());
//	//					taEspaceClient.setPasswd(taEspaceClient.passwordHashSHA256_Base64(p.getPassword()));
//						taEspaceClientService.enregistrerMerge(taEspaceClient);
//						
//	
//						//		String subject = "mon sujet ..";
//						//		String msgTxt = "mon email ...";
//						//		String fromEmail = "nicolas@legrain.fr";
//						//		String fromName = "Nicolas";
//						//		String[][] toEmailandName = new String[][]{ {"nico290382@gmail.com","NP"}};
//	
//						String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de modification de votre mot de passe";
//	
//						String utilisateur = p.getEmail();
//						String msgTxt = "Bonjour "+utilisateur+",\n"
//								+"\n"
//								+"nous vous confirmons que la modification de votre mot de passe a bien été prise en compte.\n"
//								+"\n"
//								+"Notez bien que seul votre mot de passe a été changé et que le nom d'utilisateur est toujours le même.\n"
//								+"\n"
//								+"Important !\n"
//								+"\n"
//								+"Si vous n'êtes pas l'origine de cette modification, veuillez contacter le plus vite possible notre support soit :\n"
//								+"\n"
//								+"Par téléphone : 05.63.30.31.44\n"
//								+"\n"
//								+"Par Email : support@legrain.fr\n"
//								+"\n"
//								+"En cas de communication par email, n'oubliez pas de rappeler votre nom d'utilisateur et votre code client.\n"
//								+"\n"
//								+"Cordialement,\n"
//								+"\n"
//								+"Service Gestion.\n"
//								+"\n"
//								+"Le Bureau de Gestion est un service en ligne dédié à la gestion des entreprises proposé par Legrain SAS. \n"
//								;
//						String fromEmail = "legrain@legrain.fr";
//						String fromName = "Legrain";
//						String[][] toEmailandName = new String[][]{ {p.getEmail(),""}};
//	
//						lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//	
//						//envoie d'une copie à legrain
//						//toEmailandName = new String[][]{ {"legrain@legrain.fr",null}};
//						//lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//						//TODO faire avec BCC
//	
////						FacesContext context = FacesContext.getCurrentInstance();  
////						context.addMessage(null, new FacesMessage("", 
////								//"Votre nouveau mot de passe viens de vous être envoyé à l'adresse suivante : "+adresseEmail
////								"Votre mot de passe a bien été changé. Un mail de confirmation a été envoyé à l'adresse suivante : "+p.getEmail()+" **** "+p.getPassword()
////								)); 
//	
//	//					actionOk = true;
//	
//	
//					} else {
////						FacesContext context = FacesContext.getCurrentInstance();  
////						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Votre nouveau mot de passe est trop simple", 
////								""
////								)); 
//					}
//				} else {
//	
////					FacesContext context = FacesContext.getCurrentInstance();  
////					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Les valeurs saisie doivent être identique", 
////							""
////							)); 
//				}
//			} else {
////				FacesContext context = FacesContext.getCurrentInstance();  
////				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Aucun utilisateur avec cet identifiant n'existe dans le dossier : "+p.getCodeDossierFournisseur(), 
////						""
////						)); 
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public TaEspaceClientDTO login(ParamCompteEspaceClient p) {
		TaEspaceClientDTO retour = null;
		retour = taEspaceClientService.loginDTO(p.getEmail(), p.getPassword());
		return retour;
	}
	
//	//@Transactional(value=TxType.REQUIRES_NEW)
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public String creerPaymentIntent(String codeDossierFournisseur, String codeClientChezCeFournisseur, BigDecimal montant, 
//			String codeDocument,
//			String typeDocument) throws EJBException{
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//
//		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
//		
//		TaFacture facture = null;
//		TaAvisEcheance avisEcheance = null;
//		TaPanier panier = null;
//		if(typeDocument.equals(TaFacture.TYPE_DOC)) {//"Facture"
//			try {
//				facture = taFactureService.findByCode(codeDocument);
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else if(typeDocument.equals(TaAvisEcheance.TYPE_DOC)) {//"AvisEcheance"
//			try {
//				avisEcheance = taAvisEcheanceService.findByCode(codeDocument);
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else if(typeDocument.equals(TaPanier.TYPE_DOC)) {//"Panier"
//			try {
//				panier = taPanierService.findByCode(codeDocument);
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		PaiementCarteBancaire cb  = new PaiementCarteBancaire();
//		cb.setCompteClient(true);
//		cb.setOriginePaiement("Espace client");
//		cb.setMontantPaiement(montant);
//
//		IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);
//	
////		if(compte!=null) {
////
////			String implementation = "";
////
////			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
////			case UtilServiceWebExterne.CODE_SWE_STRIPE:
////				implementation = PaiementLgr.TYPE_STRIPE;
////				break;
////			case UtilServiceWebExterne.CODE_SWE_PAYPAL:
////				implementation = PaiementLgr.TYPE_PAYPAL;
////				break;
////			default:
////				break;
////			}
////			
////			service = iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
////		} else {
////			//pas de compte paramétré, on force l'appel sur l'implémentation Stripe, qui utilisera le compte Stripe Connect du dossier en priorité s'il existe
////			service = paiementStripeDossierService;
////		}	
////			try {
//		if(service!=null) {
//			if(typeDocument.equals(TaFacture.TYPE_DOC)) {//"Facture"
//				return service.creerPaymentIntent(compte, cb, facture, null);
//			} else if(typeDocument.equals(TaAvisEcheance.TYPE_DOC)) {//"AvisEcheance"
//				return service.creerPaymentIntent(compte, cb, avisEcheance, null); //TODO à modifier/améliorer quand on pourra relier un reglement à tout type de document y compris les "non payable"
//			} else if(typeDocument.equals(TaPanier.TYPE_DOC)) {//"Panier"
//				return service.creerPaymentIntent(compte, cb, panier, null); //TODO à modifier/améliorer quand on pourra relier un reglement à tout type de document y compris les "non payable"
//			}
////				return iServicePaiementEnLigneDossierInstance.creerPaymentIntent(compte, cb, facture, null);
////			} catch(EJBException e) {
////				throw new EJBException(e);
////			}
//		}
//		
//
//		return null;
//	}

	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public RetourPaiementCarteBancaire payerFactureCB(String codeDossierFournisseur, String codeClientChezCeFournisseur, PaiementCarteBancaire cb, TaFacture facture) throws EJBException{
		initTenantRegistry();
		initTenant(codeDossierFournisseur);

		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
		IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);
//		IPaiementEnLigneDossierService service = null;
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
//			 iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
//
//		}else {
//			//pas de compte paramétré, on force l'appel sur l'implémentation Stripe, qui utilisera le compte Stripe Connect du dossier en priorité s'il existe
//			service = paiementStripeDossierService;
//		}	
		if(service!=null) {
			return service.payer(compte, cb, facture, null);
		}

		return null;
	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public int etatPaiementCourant(String codeDossierFournisseur, String idStripePaymentIntent) throws EJBException{
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		
//		int retour = 0;
//
//		TaStripePaymentIntent paymentIntent = null;
//		
//		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT);
//		IPaiementEnLigneDossierService service = paiementGeneralDossierService.findImplementation(compte);
//		
////		interfaceStripeElementResultatOk = false;
////		interfaceStripeElementResultatErreur = false;
////		interfaceStripeElementResultatNonTermine = false;
//
////		IPaiementEnLigneDossierService service = null;
////		if(compte!=null) {
////
////			String implementation = "";
////
////			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
////			case UtilServiceWebExterne.CODE_SWE_STRIPE:
////				implementation = PaiementLgr.TYPE_STRIPE;
////				break;
////			case UtilServiceWebExterne.CODE_SWE_PAYPAL:
////				implementation = PaiementLgr.TYPE_PAYPAL;
////				break;
////			default:
////				break;
////			}
////			
////			service = iServicePaiementEnLigneDossierInstance.select(new MyQualifierLiteral(implementation)).get();
////
////			
////	
////		}else {
////			//pas de compte paramétré, on force l'appel sur l'implémentation Stripe, qui utilisera le compte Stripe Connect du dossier en priorité s'il existe
////			service = paiementStripeDossierService;
////		}	
//
//		if(service!=null) {
//			
//			paymentIntent = service.updatePaymentIntentFromStripe(compte, idStripePaymentIntent); 
//	
//			if(paymentIntent!=null && paymentIntent.getStatus().equals("succeeded")) {
//	//			interfaceStripeElementResultatOk = true;
//				retour = IEtatPaiementCourant.PAIEMENT_OK;
//	//			FacesContext context = FacesContext.getCurrentInstance();  
//	//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO , "Paiement CB",
//	//					"Le paiement a été validé. Le règlement correspondant doit être visible dans la liste des règlement"));
//			} else if(paymentIntent!=null && paymentIntent.getStatus().equals("processing")) {
//	//			interfaceStripeElementResultatNonTermine = true;
//				retour = IEtatPaiementCourant.PAIEMENT_EN_COURS;
//	//			FacesContext context = FacesContext.getCurrentInstance();  
//	//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN , "Paiement CB", 
//	//					"Le paiement est encore encore cours de validation. "
//	//							+ "Quand il sera validé, le règlement correspondant apparetra dans la liste des règlements."
//	//							+ "S'il n'apparait pas, veuillez verifier dans votre service de paiement (Stripe)"));
//			} else  {
//	//			interfaceStripeElementResultatErreur = true;
//				retour = IEtatPaiementCourant.PAIEMENT_ERREUR;
//	//			FacesContext context = FacesContext.getCurrentInstance();  
//	//			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR , "Paiement CB", 
//	//					"Une erreur est survenue pendant la validation du paiement. Il n'a pas été pris en compte"));
//			}
//		}
//		
//		return retour;
//	}
	
//	@Override
//	public boolean fournisseurPermetPaiementParCB(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//
////		TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT); 
////		
////		if(compte!=null) {
////			return true;
////		} else {
////			return false;
////		}
//		
//		return paiementGeneralDossierService.dossierPermetPaiementCB();
//	}

	@Override
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

	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public TaTiersDTO infosClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
		initTenantRegistry();
		initTenant(codeDossierFournisseur);

		try {
			TaTiersDTO t = taTiersService.findByCodeDTO(codeClientChezCeFournisseur);
			return t;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
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

	@Override
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
	
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public String clePubliqueStripe(String dossier) {
//		initTenantRegistry();
//		initTenant(dossier);
//
//		try {
//			TaCompteServiceWebExterne compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_PAIEMENT); 
//			UtilServiceWebExterne utilServiceWebExterne = new UtilServiceWebExterne();
//			
//			boolean modeTest = false;
//			boolean modeStripeConnect = false;
//			modeTest = lgrEnvironnementServeur.isModeTestPaiementDossier();
//			TaStripeAccount taStripeAccount =  taStripeAccountService.findInstance(); 
//			if(taStripeAccount!=null && taStripeAccount.getIdExterne()!=null) {
//				modeStripeConnect = true;
//			}
//			if(modeStripeConnect) {
//				//connect, donc utilisation des clés programmes
//				if(modeTest) {
//					return LgrStripe.TEST_PUBLIC_API_KEY_PROGRAMME;
//				} else {
//					return LgrStripe.LIVE_PUBLIC_API_KEY_PROGRAMME;
//				}
//			} else {
//				//utilisation des clé API du dossier s'il y en a, sinon erreur
//				if(compte!=null && compte.getApiKey2()!=null) {
//					String API_KEY_DOSSIER_PK_LIVE = utilServiceWebExterne.decrypter(compte).getApiKey2();
//					return API_KEY_DOSSIER_PK_LIVE;
//				} else {
//					//TODO exception, pas de compte connect et aucun cle dans le dossier
//				}
//			}
////			if(compte!=null) {
////				return utilServiceWebExterne.decrypter(compte).getApiKey2();
////			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public String cleConnectStripe(String dossier) {
//		initTenantRegistry();
//		initTenant(dossier);
//
//		try {
//			TaStripeAccount taStripeAccount =  taStripeAccountService.findInstance(); 
////			UtilServiceWebExterne utilServiceWebExterne = new UtilServiceWebExterne();
////			if(compte!=null) {
////				return utilServiceWebExterne.decrypter(compte).getApiKey2();
////			}
//			if(taStripeAccount!=null && taStripeAccount.getIdExterne()!=null) {
//				return taStripeAccount.getIdExterne();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}

//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public byte[] pdfFacture(String codeDossierFournisseur, String codeFactureChezCeFournisseur) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//
//		try {
//			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
//			TaFacture t = taFactureService.findByCode(codeFactureChezCeFournisseur);
//			String pdfPath = taFactureService.generePDF(t.getIdDocument(), new HashMap<String,Object>(), null, null,taActionEdition);
//
//			Path path = Paths.get(pdfPath);
//			byte[] b = Files.readAllBytes(path);
//			return b;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public String pdfFactureFile(String codeDossierFournisseur, String codeFactureChezCeFournisseur) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//
//		try {
//			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
//			TaFacture t = taFactureService.findByCode(codeFactureChezCeFournisseur);
//			String pdfPath = taFactureService.generePDF(t.getIdDocument(), new HashMap<String,Object>(), null, null,taActionEdition);
//
////			Path path = Paths.get(pdfPath);
////			byte[] b = Files.readAllBytes(path);
//			return pdfPath;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public String pdfDevisFile(String codeDossierFournisseur, String codeFactureChezCeFournisseur) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//
//		try {
//			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
//			TaDevis t = taDevisService.findByCode(codeFactureChezCeFournisseur);
//			String pdfPath = taDevisService.generePDF(t.getIdDocument(), null, null, null);
//
////			Path path = Paths.get(pdfPath);
////			byte[] b = Files.readAllBytes(path);
//			return pdfPath;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public String pdfCommandeFile(String codeDossierFournisseur, String codeFactureChezCeFournisseur) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//
//		try {
//			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
//			TaBoncde t = taBoncdeService.findByCode(codeFactureChezCeFournisseur);
//			String pdfPath = taBoncdeService.generePDF(t.getIdDocument(), null, null, null);
//
////			Path path = Paths.get(pdfPath);
////			byte[] b = Files.readAllBytes(path);
//			return pdfPath;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public String pdfAvisEcheanceFile(String codeDossierFournisseur, String codeFactureChezCeFournisseur) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//
//		try {
//			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
//			TaAvisEcheance t = taAvisEcheanceService.findByCode(codeFactureChezCeFournisseur);
//			String pdfPath = taAvisEcheanceService.generePDF(t.getIdDocument(), null, null, null);
//
////			Path path = Paths.get(pdfPath);
////			byte[] b = Files.readAllBytes(path);
//			return pdfPath;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}

//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//
//		//List<TaFacture> listeDoc = taFactureService.findByCodeTiersAndDate(codeClientChezCeFournisseur, debut, fin);
//
//		//return listeDoc;
//		return null;
//	}

//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur,
//			String codeClientChezCeForunisseur, Date debut, Date fin) {
//
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeClientChezCeForunisseur);
////		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
////		t.setUtiliseCompteClient(true);
////		t.setDateDerniereConnexionCompteClient(new Date());
////		t = taTiersService.merge(t);
//		
//		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
//			t = rechercheEspaceClientMultiTiersSecondaire(codeClientChezCeForunisseur);
//		}
////		t.setDateDerniereConnexion(new Date());
////		taEspaceClientService.merge(t);
//		if(t.getActif()!=null && t.getActif()==true) {
//			List<TaFacture> listeDoc = taFactureService.findByCodeTiersAndDateCompteClient(codeClientChezCeForunisseur, debut, fin);
////			for (TaFacture taFacture : listeDoc) {
////				taFacture.calculSommeAvoirIntegres();
////				System.out.println("Facture : "+taFacture.getAvoirsComplet());
////			}
//			return listeDoc;
//		} else {
//			System.out.println("Le service compte client n'est pas actif pour le client "+codeClientChezCeForunisseur+" chez le fournisseur "+codeDossierFournisseur);
//			return new ArrayList<TaFacture>();
//		}
//
//
//	}
	
	public TaEspaceClient rechercheEspaceClientMultiTiersSecondaire(String codeTiers) {
		//TODO A supprimer vite et utilisation d'un table de relation pour gerer le multi compte tiers
		TaEspaceClient t = null;
			try {
				TaTiers ti = taTiersService.findByCode(codeTiers); //recuperation du tiers correspondant au code
				if(ti!=null) {
					for (TaEmail email : ti.getTaEmails()) {
						List<TaTiers> listeTiers = taTiersService.findByEmail(email.getAdresseEmail());
						for (TaTiers taTiers : listeTiers) {
							if(!taTiers.getCodeTiers().equals(codeTiers)) {
								t = taEspaceClientService.findByCodeTiers(taTiers.getCodeTiers());
								if(t!=null) {
									idTiers = ti.getIdTiers();
								}
							}
						}
					} 
				}
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return t;
	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public List<TaFactureDTO> facturesClientChezFournisseurDTO(String codeDossierFournisseur,
//			String codeClientChezCeForunisseur, Date debut, Date fin) {
//
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeClientChezCeForunisseur);
////		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
////		t.setUtiliseCompteClient(true);
////		t.setDateDerniereConnexionCompteClient(new Date());
////		t = taTiersService.merge(t);
//		
//		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
//			t = rechercheEspaceClientMultiTiersSecondaire(codeClientChezCeForunisseur);
//		}
////		t.setDateDerniereConnexion(new Date());
////		taEspaceClientService.merge(t);
//		if(t.getActif()!=null && t.getActif()==true) {
//			List<TaFacture> listeDoc = taFactureService.findByCodeTiersAndDateCompteClient(codeClientChezCeForunisseur, debut, fin);
////			for (TaFacture taFacture : listeDoc) {
////				taFacture.calculSommeAvoirIntegres();
////				System.out.println("Facture : "+taFacture.getAvoirsComplet());
////			}
//			//TaFactureMapper mapper = new TaFactureMapper();
//			LgrDozerMapper<TaFacture, TaFactureDTO> mapper = new LgrDozerMapper<>();
//			List<TaFactureDTO> listeDocDTO = new ArrayList<>();
//			TaFactureDTO dto = null;
//			for (TaFacture taFacture : listeDoc) {
//				taFacture.calculeTvaEtTotaux();
//				taFacture.calculSommeAvoirIntegres();
//				//listeDocDTO.add(mapper.mapEntityToDto(taFacture, null));
//				dto = new TaFactureDTO();
//				mapper.map(taFacture, dto);
//				listeDocDTO.add(dto);
//			}
//			return listeDocDTO;
//		} else {
//			System.out.println("Le service compte client n'est pas actif pour le client "+codeClientChezCeForunisseur+" chez le fournisseur "+codeDossierFournisseur);
//			return new ArrayList<TaFactureDTO>();
//		}
//
//
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public TaAvisEcheanceDTO getAvisEcheanceById(String codeDossierFournisseur, String codeClientChezCeFournisseur, Integer idAvisEcheance) {
//
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeClientChezCeFournisseur);
//
//		
//		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
//			t = rechercheEspaceClientMultiTiersSecondaire(codeClientChezCeFournisseur);
//		}
////		t.setDateDerniereConnexion(new Date());
////		taEspaceClientService.merge(t);
//		if(t.getActif()!=null && t.getActif()==true) {
//			TaAvisEcheanceDTO f = null;
//	    	
//			TaAvisEcheance entity = null;
//		    	
//		    	try {
//		    		
//		    		entity = taAvisEcheanceService.findById(idAvisEcheance);
//		    		f = taAvisEcheanceService.findByIdDTO(idAvisEcheance);
//		    		
//		    		f.setLignesDTO(new ArrayList<TaLAvisEcheanceDTO>());
//		    		TaLAvisEcheanceDTO ldto = null;
//		    		for (TaLAvisEcheance l : entity.getLignes()) {
//		    			ldto = taLAvisEcheanceService.findByIdDTO(l.getIdLDocument());
//		    			f.getLignesDTO().add(ldto);
//					}
//					System.out.println("FlashRestService.getBonlivById() : "/*+t.getCodeTiers()*/);
//				} catch (FinderException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		    	return f;
//		
//		} else {
//			System.out.println("Le service compte client n'est pas actif pour le client "+codeClientChezCeFournisseur+" chez le fournisseur "+codeDossierFournisseur);
//			return new TaAvisEcheanceDTO();
//		}
//
//
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public TaFactureDTO getFactureById(String codeDossierFournisseur, String codeClientChezCeFournisseur, Integer idFacture) {
//
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeClientChezCeFournisseur);
//		
//		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
//			t = rechercheEspaceClientMultiTiersSecondaire(codeClientChezCeFournisseur);
//		}
////		t.setDateDerniereConnexion(new Date());
////		taEspaceClientService.merge(t);
//		if(t.getActif()!=null && t.getActif()==true) {
//			TaFactureDTO f = null;
//	    	
//	    		TaFacture entity = null;
//		    	
//		    	try {
//		    		
//		    		entity = taFactureService.findById(idFacture);
//		    		f = taFactureService.findByIdDTO(idFacture);
//		    		
//		    		f.setLignesDTO(new ArrayList<TaLFactureDTO>());
//		    		TaLFactureDTO ldto = null;
//		    		for (TaLFacture l : entity.getLignes()) {
//		    			ldto = taLFactureService.findByIdDTO(l.getIdLDocument());
//		    			f.getLignesDTO().add(ldto);
//					}
//					System.out.println("FlashRestService.getBonlivById() : "/*+t.getCodeTiers()*/);
//				} catch (FinderException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		    	return f;
//		
//		} else {
//			System.out.println("Le service compte client n'est pas actif pour le client "+codeClientChezCeFournisseur+" chez le fournisseur "+codeDossierFournisseur);
//			return new TaFactureDTO();
//		}
//
//
//	}
	
//	public TaFactureDTO facturePourAvisEcheance(String codeDossierFournisseur, String codeAvisEcheance) {
//
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		
//		TaFactureDTO taFactureDTO = null;
//		try {
//			taFactureDTO = taFactureService.findByCodeDTO(codeAvisEcheance);
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return taFactureDTO;
//	}

//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public List<TaDevisDTO> devisClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeForunisseur,
//			Date debut, Date fin) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeClientChezCeForunisseur);
////		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
////		t.setUtiliseCompteClient(true);
////		t.setDateDerniereConnexionCompteClient(new Date());
////		t = taTiersService.merge(t);
//		
//		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
//			t = rechercheEspaceClientMultiTiersSecondaire(codeClientChezCeForunisseur);
//		}
////		t.setDateDerniereConnexion(new Date());
////		taEspaceClientService.merge(t);
//		if(t.getActif()!=null && t.getActif()==true) {
//			List<TaDevis> listeDoc = taDevisService.findByCodeTiersAndDateCompteClient(codeClientChezCeForunisseur, debut, fin);
////			for (TaFacture taFacture : listeDoc) {
////				taFacture.calculSommeAvoirIntegres();
////				System.out.println("Facture : "+taFacture.getAvoirsComplet());
////			}
//			//TaFactureMapper mapper = new TaFactureMapper();
//			LgrDozerMapper<TaDevis, TaDevisDTO> mapper = new LgrDozerMapper<>();
//			List<TaDevisDTO> listeDocDTO = new ArrayList<>();
//			TaDevisDTO dto = null;
//			for (TaDevis taDevis : listeDoc) {
//				taDevis.calculeTvaEtTotaux();
//				//taDevis.calculSommeAvoirIntegres();
//				//listeDocDTO.add(mapper.mapEntityToDto(taFacture, null));
//				dto = new TaDevisDTO();
//				mapper.map(taDevis, dto);
//				listeDocDTO.add(dto);
//			}
//			return listeDocDTO;
//		} else {
//			System.out.println("Le service compte client n'est pas actif pour le client "+codeClientChezCeForunisseur+" chez le fournisseur "+codeDossierFournisseur);
//			return new ArrayList<TaDevisDTO>();
//		}
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public List<TaAbonnementFullDTO> abonnementClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeForunisseur,
//			Date debut, Date fin) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeClientChezCeForunisseur);
////		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
////		t.setUtiliseCompteClient(true);
////		t.setDateDerniereConnexionCompteClient(new Date());
////		t = taTiersService.merge(t);
//		
//		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
//			idTiers = null;
//			t = rechercheEspaceClientMultiTiersSecondaire(codeClientChezCeForunisseur);
//		} else {
//			idTiers = t.getTaTiers().getIdTiers();
//		}
////		t.setDateDerniereConnexion(new Date());
////		taEspaceClientService.merge(t);
//		if(t.getActif()!=null && t.getActif()==true) {
//			//List<TaAbonnementDTO> listeDocDTO = new ArrayList<>();
//			
//			//List<TaAbonnementFullDTO> listeDocDTO = taStripeSubscriptionService.findAllFullDTOByIdTiers(t.getTaTiers().getIdTiers());
//			List<TaAbonnementFullDTO> listeDocDTO = taAbonnementService.findAllFullDTOByIdTiers(idTiers);
//
//			LgrDozerMapper<TaAbonnement, TaAbonnementDTO> mapper = new LgrDozerMapper<>();
//		
////			TaAbonnementDTO dto = null;
////			for (TaAbonnement taAbonnement : listeDoc) {
////				taAbonnement.calculeTvaEtTotaux();
////				//taDevis.calculSommeAvoirIntegres();
////				//listeDocDTO.add(mapper.mapEntityToDto(taFacture, null));
////				dto = new TaAbonnementDTO();
////				mapper.map(taAbonnement, dto);
////				listeDocDTO.add(dto);
////			}
//			return listeDocDTO;
//		} else {
//			System.out.println("Le service compte client n'est pas actif pour le client "+codeClientChezCeForunisseur+" chez le fournisseur "+codeDossierFournisseur);
//			return new ArrayList<TaAbonnementFullDTO>();
//		}
//	}
	
//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public List<TaAvisEcheanceDTO> avisEcheanceClientChezFournisseurDTO(String codeDossierFournisseur, String codeClientChezCeForunisseur,
//			Date debut, Date fin) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeClientChezCeForunisseur);
////		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
////		t.setUtiliseCompteClient(true);
////		t.setDateDerniereConnexionCompteClient(new Date());
////		t = taTiersService.merge(t);
//		
//		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
//			t = rechercheEspaceClientMultiTiersSecondaire(codeClientChezCeForunisseur);
//		}
////		t.setDateDerniereConnexion(new Date());
////		taEspaceClientService.merge(t);
//		if(t.getActif()!=null && t.getActif()==true) {
//			List<TaAvisEcheance> listeDoc = taAvisEcheanceService.findByCodeTiersAndDateCompteClient(codeClientChezCeForunisseur, debut, fin);
////			for (TaFacture taFacture : listeDoc) {
////				taFacture.calculSommeAvoirIntegres();
////				System.out.println("Facture : "+taFacture.getAvoirsComplet());
////			}
//			//TaFactureMapper mapper = new TaFactureMapper();
//			LgrDozerMapper<TaAvisEcheance, TaAvisEcheanceDTO> mapper = new LgrDozerMapper<>();
//			List<TaAvisEcheanceDTO> listeDocDTO = new ArrayList<>();
//			TaAvisEcheanceDTO dto = null;
//			for (TaAvisEcheance taAvisEcheance : listeDoc) {
//				taAvisEcheance.calculeTvaEtTotaux();
//				//taDevis.calculSommeAvoirIntegres();
//				//listeDocDTO.add(mapper.mapEntityToDto(taFacture, null));
//				dto = new TaAvisEcheanceDTO();
//				mapper.map(taAvisEcheance, dto);
//				listeDocDTO.add(dto);
//			}
//			return listeDocDTO;
//		} else {
//			System.out.println("Le service compte client n'est pas actif pour le client "+codeClientChezCeForunisseur+" chez le fournisseur "+codeDossierFournisseur);
//			return new ArrayList<TaAvisEcheanceDTO>();
//		}
//	}

	

//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public TaParamEspaceClientDTO parametreEspaceClient(String dossier) {
//		initTenantRegistry();
//		initTenant(dossier);
//
//		try {
//			TaParamEspaceClientDTO t = taParamEspaceClientService.findInstanceDTO();
//			return t;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}

//	@Override
//	public byte[] logo(String dossier) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public List<TaBonlivDTO> livraisonsClientChezFournisseurDTO(String codeDossierFournisseur,
//			String codeClientChezCeForunisseur, Date debut, Date fin) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public List<TaBoncdeDTO> commandesClientChezFournisseurDTO(String codeDossierFournisseur,
//			String codeClientChezCeForunisseur, Date debut, Date fin) {
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
//		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeClientChezCeForunisseur);
////		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
////		t.setUtiliseCompteClient(true);
////		t.setDateDerniereConnexionCompteClient(new Date());
////		t = taTiersService.merge(t);
//		
//		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
//			t = rechercheEspaceClientMultiTiersSecondaire(codeClientChezCeForunisseur);
//		}
////		t.setDateDerniereConnexion(new Date());
////		taEspaceClientService.merge(t);
//		if(t.getActif()!=null && t.getActif()==true) {
//			List<TaBoncde> listeDoc = taBoncdeService.findByCodeTiersAndDateCompteClient(codeClientChezCeForunisseur, debut, fin);
////			for (TaFacture taFacture : listeDoc) {
////				taFacture.calculSommeAvoirIntegres();
////				System.out.println("Facture : "+taFacture.getAvoirsComplet());
////			}
//			//TaFactureMapper mapper = new TaFactureMapper();
//			LgrDozerMapper<TaBoncde, TaBoncdeDTO> mapper = new LgrDozerMapper<>();
//			List<TaBoncdeDTO> listeDocDTO = new ArrayList<>();
//			TaBoncdeDTO dto = null;
//			for (TaBoncde taBoncde : listeDoc) {
//				taBoncde.calculeTvaEtTotaux();
//				//taDevis.calculSommeAvoirIntegres();
//				//listeDocDTO.add(mapper.mapEntityToDto(taFacture, null));
//				dto = new TaBoncdeDTO();
//				mapper.map(taBoncde, dto);
//				listeDocDTO.add(dto);
//			}
//			return listeDocDTO;
//		} else {
//			System.out.println("Le service compte client n'est pas actif pour le client "+codeClientChezCeForunisseur+" chez le fournisseur "+codeDossierFournisseur);
//			return new ArrayList<TaBoncdeDTO>();
//		}
//	}
	
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public String replaceTenantAlias(String tenantAlias) {
//		System.out.println(tenantAlias);
//		String tmp = "";
//		if(tenantAlias!=null && tenantAlias.startsWith("alias:")) {
//			tenantAlias = tenantAlias.replace("alias:", "");
//			System.out.println("Recherche d'un dossier pour l'alias : "+tenantAlias); tmp = tenantAlias;
//			TaAliasEspaceClient taAliasEspaceClient = null;
//			try {
//				taAliasEspaceClient = taAliasEspaceClientService.findByCode(tenantAlias);
//				if(taAliasEspaceClient!=null) {
//					tenantAlias = taAliasEspaceClient.getTenant();
//					System.out.println("Tenant de l'alias '"+tmp+"' : "+tenantAlias);
//				}
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return tenantAlias;
//	}
	
	
//	@Transactional(value=TxType.REQUIRES_NEW)
//	public boolean authentification(String dossier, String login, String password) {
//		try {
//			initTenantRegistry();
//			initTenant(dossier);
//			//TaUtilisateur u = taUtilisateurService.findByCode(login);
//			TaUtilisateurWebService u = taUtilisateurWebServiceService.findByCode(login);
//			if(u!=null && u.getPasswd()!=null && u.getPasswd().equals(u.passwordHashSHA256_Base64(password))) {
//				return true;
//			} else {
//				//System.out.println("Utilisateur webservice ("+login+") rest invalide pour le dossier : "+reg.getResource(ServerTenantInterceptor.TENANT_TOKEN_KEY));
//				System.out.println("Utilisateur webservice ("+login+") rest invalide pour le dossier : "+reg.getResource(JWTTokenGlobalFilter.TENANT_REST_TOKEN_KEY));
//				System.out.println("Vérifier le mot de passe dans le schéma correspondant au dossier - hash : "+u.passwordHashSHA256_Base64(password));
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		} 
//	}

}

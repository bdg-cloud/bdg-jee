package fr.legrain.abonnement.stripe.service;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang.SerializationUtils;
import org.jboss.ejb3.annotation.TransactionTimeout;

import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.abonnement.stripe.model.TaStripeInvoiceItem;
import fr.legrain.abonnement.stripe.model.TaStripePaiementPrevu;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaiementPrevuServiceRemote;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.cron.service.remote.ITaCronServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.cron.model.TaCron;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.dto.TaLAvisEcheanceDTO;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosAvisEcheance;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.dossier.service.TaInfoEntrepriseService;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.email.service.EnvoiEmailService;
import fr.legrain.generation.service.CreationDocumentLigneEcheanceAbonnementMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.paiement.service.LgrEnvironnementServeur;

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
public class MultitenantProxyTimerAbonnement {

	private String tenant = null;
	private TransactionSynchronizationRegistry reg;
	
	public static final int NB_JOUR_AVANT_ECHEANCE = 30;
	public static final int NB_JOUR_PERIODE_REGROUPEMENT = 20;
	public static final int NB_JOUR_PREL_AVANT_ECHEANCE = 7;
			
	@EJB private ITaStripePaiementPrevuServiceRemote taStripePaiementPrevuService;
//	@EJB private ITaStripeSubscriptionServiceRemote taStripeSubscriptionService;
	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	private @EJB ITaStripeSourceServiceRemote taStripeSourceService;
	private @EJB ITaStripeCustomerServiceRemote taStripeCustomerService;
	private @EJB TimerDeclenchePaiementPrevuService timerDeclenchePaiementPrevuService;
	@EJB private ITaAbonnementServiceRemote taAbonnementService;
	@EJB private ITaLAbonnementServiceRemote taLAbonnementService;

	@EJB private ITaEtatServiceRemote taEtatService;
	
	private @EJB ITaTiersServiceRemote taTiersService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTLigneServiceRemote taTLigneService;
	
	private @EJB ITaCronServiceRemote taCronService;
	private @EJB EnvoiEmailService emailServiceFinder;
	private @Inject SessionInfo sessionInfo;
	@Inject protected TenantInfo tenantInfo;
	
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private @EJB ITaFactureServiceRemote taFactureService;
	
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	@EJB private CreationDocumentLigneEcheanceAbonnementMultiple creation;
	
	
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

//	@Transactional(value=TxType.REQUIRES_NEW)
//	/**
//	 * Création des lignes d'échéance (ta_l_echeance) si nécessaire
//	 * @param tenant
//	 * @param n
//	 */
//	public void doWork(String tenant, TaStripeSubscription n) {
//				
//		initTenantRegistry();
//		initTenant(tenant);
//		try {
////			//rechercher les abonnements actifs
////			taStripeSubscriptionService.selectAll();
//			
//			Date now = new Date();
//				//si un des abonnements actif a une échéance à : date du jour + NB_JOUR_AVANT_ECHEANCE
//			if(n.getDateDebut().before(now) && n.getDateFin().before(now)) {
//				//l'abonnement est bien actif en ce moment
//				
//				Date date = n.getQuandCree();
//				
//				LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//				LocalDateTime localDateTimeEcheance = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//				LocalDateTime localDateTimeNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//				
//				if(!n.getItems().isEmpty()) {
//					String interval = n.getItems().iterator().next().getTaPlan().getInterval();
//					switch (interval) {
////					case "day":
////						break;
////					case "week":
////						break;
//					case "month":
//						
//						boolean finDuMois = false;
//						if(localDateTime.getMonthValue() == 2) {
//							if(localDateTime.getDayOfMonth()>=28) {
//								finDuMois = true;
//							}
//						} else if(localDateTime.getDayOfMonth() >= 30) {
//							finDuMois = true;
//						}
//						//prendre le jour du mois de la date création => gérer 28/29, 30, 31, fin de mois
//						// => touver la prochaine date d'échéance ce mois ci ou cette année
//						if(finDuMois) {
//							localDateTimeEcheance = localDateTimeEcheance.withDayOfMonth(localDateTimeEcheance.getMonth().length(localDateTimeEcheance.toLocalDate().isLeapYear()));
//						} else {
//							localDateTimeEcheance = localDateTimeEcheance.withDayOfMonth(localDateTime.getDayOfMonth());
//						}
//						
//						break;
//					case "year":
//						localDateTimeEcheance = localDateTimeEcheance.withYear(localDateTime.getYear());
//						break;
//					default:
//						break;
//					}
//					
//					// enlever le nombre de jour de "marge"
//					LocalDateTime localDateTimeGenerationEcheance = localDateTimeEcheance.minusDays(NB_JOUR_AVANT_ECHEANCE);
//					//comparer cette derniere date avec la date du jour
//					if(localDateTimeGenerationEcheance.isEqual(localDateTimeNow)) {
//						//si on est ce jour là, verifier que non deja traiter/creer et creer
//						Date dateTimeEcheance = Date.from(localDateTimeEcheance.atZone(ZoneId.systemDefault()).toInstant());
//						List<TaStripePaiementPrevu> p = taStripePaiementPrevuService.findByAbonnementAndDate(n,dateTimeEcheance);
//						//ajouter une liaison entre l'avis d'échéance et l'abonnement (nouvelle clé étrangère) ??
//						// -OU- (si regroupement possible)
//						//ajouter une liaison entre une ligne d'avis d'échéance et l'abonnement (nouvelle clé étrangère) ??
////						List<TaAvisEcheance> ae = taAvisEcheanceService.findByAbonnementAndDate(n,dateTimeEcheance);
//						if(p == null || p.isEmpty()) {
//						    //créer aussi un "paiement prévu"
//						}
////						if(ae == null || ae.isEmpty()) {
////							//créer un avis d'échéance (et/ou un avis de prélèvement) en cas de source stripe
////						}
//						for (TaStripeSubscriptionItem item : n.getItems()) {
//							TaLEcheance ech = new TaLEcheance();
//							ech.setTaStripeSubscription(n);
//							ech.setTaArticle(item.getTaPlan().getTaArticle());
//							ech.setQteLDocument(new BigDecimal(item.getQuantity()));
//							ech.setPrixULDocument(item.getTaPlan().getAmount());
//							ech.setLibLDocument(item.getTaPlan().getTaArticle().getLibellecArticle());
//							ech = taLEcheanceService.merge(ech);
//						}
//						
//					}
//					//si avant ce jour => rien ???
//					//si après ce jour, vérifier qu'il existe bien ???
//				}
//	
//
//			}
//			
//				
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//			
//	}
	
	
	/**
	 * Création des avis d'échéance et des "paiement prévu" correspondant 
	 * et des éventuels paramétrages de regroupement.
	 * @param tenant
	 * @param n
	 */
	//@Transactional(value=TxType.REQUIRES_NEW) //mise en commentaire pour le passage de WildFly 17 à 24
	@TransactionTimeout(value=600,unit=TimeUnit.MINUTES) //mise en commentaire pour le passage de WildFly 17 à 24
	public void doWork(String tenant) {
		System.out.println("*******************************************************PASSAGE CRON POUR ABONNEMENTS*******************************************************");
		initTenantRegistry();
		initTenant(tenant);
		this.tenant = tenant;
		Date now = new Date();
		try {
			
			suspendEcheances();
			supprimeEcheancesSuspendueDelaiSurvie();

			//rechercher les abonnements (non annulés) dont la date de fin période active arrive à échéance dans X jours ou moins
			List<TaAbonnementDTO> listeAbonnementsDTO = taAbonnementService.findAllEnCoursDTOArrivantAEcheanceDansXJours(NB_JOUR_AVANT_ECHEANCE, null);
			Map<String,List<TaAbonnementDTO>> mapAboATraiteParTiers = new HashMap<>();
			List<TaLEcheance> listeProchainesEcheances = new ArrayList<TaLEcheance>();
			
			System.out.println("Nb abo qui arrivent à échéance (période active) dans "+NB_JOUR_AVANT_ECHEANCE+" jours : "+listeAbonnementsDTO.size());
			
			//on trie les abos par tiers, inutile ici pour l'instant mais servira peut etre a la génération d'avis d'échéance
			for (TaAbonnementDTO abo : listeAbonnementsDTO) {
				//controle sur l'annulation de l'abonnement
				if(abo.getDateAnnulation() == null ) {
					String cle = abo.getCodeTiers();
					if(!mapAboATraiteParTiers.keySet().contains(cle)) {
						mapAboATraiteParTiers.put(cle, new ArrayList<>());
						
					}
					mapAboATraiteParTiers.get(cle).add(abo);
					
				}
			}
			
			
			for (String codeTiers : mapAboATraiteParTiers.keySet()) {
				List<Integer> listeIdAbonnementDejaTraite = new ArrayList<>();
				
				if(!mapAboATraiteParTiers.get(codeTiers).isEmpty()) {
					//création des paiements prévus stripe
					//TaStripePaiementPrevu pp = creerPaiementPrevu(taAvisEcheance);
					//if(pp!= null ) {
						//pp = taStripePaiementPrevuService.merge(pp);
					//}
					
					for (TaAbonnementDTO abo : mapAboATraiteParTiers.get(codeTiers)) {
						//si on a pas deja traité cet abonnement lié a cette ligne d'échéance
						//if(!listeIdAbonnementDejaTraite.contains(e.getTaStripeSubscription().getIdStripeSubscription())) {
						if(!listeIdAbonnementDejaTraite.contains(abo.getId())) {
							//quand on passe l'abonnement en paramètre il va généré potentiellement toute les ligne d'échéance suivante (une par plan/ligne d'abonnement)
							//il ne faut donc pas s'"occuper" des autre échéances de ce même abonnement car elles auront deja été traitées
							listeProchainesEcheances.addAll(taAbonnementService.genereProchainesEcheances(abo));
							
							//une fois l'échéance traité on met son abo dans la  liste des abo déjà traités
							listeIdAbonnementDejaTraite.add(abo.getId());
						}
					}
					
					
				}
			}
			System.out.println("************** "+listeProchainesEcheances.size()+" lignes d'échéances ont été générées ! ********************");
			
			relance();
			
			TaCron cron = taCronService.findByCode(TaCron.CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT);
			if(cron != null) {
				cron.setDateDerniereExecution(now);
				taCronService.merge(cron);
				
				System.out.println("************** FIN PASSAGE CRON ABONNEMENTS ********************");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * Cette méthode récupère toutes les ligne d'échéances en cours et suspendu en fonction de leur article et de leur "TaJourRelance" associés
	 * Puis elle regroupe ces échéances par tiers et enfin envoi un mail a chaque tiers concernés
	 */
	private void relance(){
		//on, trouve les échéances en cours ou suspendu
		List<TaLEcheance> listeEcheance = taLEcheanceService.selectAllEcheanceARelancer(null);
		
		System.out.println("**************RELANCE MAILS :  "+listeEcheance.size()+" lignes d'échéances sont à relancer ! ********************");
		
		//on les regroupe par tiers
		Map<String,List<TaLEcheance>> mapEchParTiers = new HashMap<>();
		for (TaLEcheance ech : listeEcheance) {
				String cle = ech.getTaAbonnement().getTaTiers().getCodeTiers();
				if(!mapEchParTiers.keySet().contains(cle)) {
					mapEchParTiers.put(cle, new ArrayList<>());
					
				}
				mapEchParTiers.get(cle).add(ech);
				
		}
		//on créer un mail par tiers
		for (String codeTiers : mapEchParTiers.keySet()) {
			TaTiers tiers;
			try {
				tiers = taTiersService.findByCode(codeTiers);

					String message = "Cher Client (code tiers :"+tiers.getCodeTiers()+" ) <br><br>"
							+ "vous recevez ce message car vous êtes abonné aux services ci dessous qui arrivent bientôt à échéance. <br><br>";
					String objet = "Vos abonnements arrivent à échéances (code tiers :"+tiers.getCodeTiers()+" )";
					//String destinataire = tiers.getTaEmail().getAdresseEmail();
					String destinataire = null;
					BigDecimal montantTotalTTC = BigDecimal.ZERO;
					for (TaLEcheance ech : mapEchParTiers.get(codeTiers)) {
						montantTotalTTC = montantTotalTTC.add(ech.getMtTtcLDocument());
						message+=" - "+ech.getTaAbonnement().getCodeDocument()+" : "+ech.getLibLDocument()+", <b>date d'échéance le "+LibDate.customFormatDate(ech.getDateEcheance())+"</b> pour un montant de "+ech.getMtTtcLDocument()+
								" € TTC pour la période du "+LibDate.customFormatDate(ech.getDebutPeriode())+" au "+LibDate.customFormatDate(ech.getFinPeriode())+" <br>";
					}
					
					message+="<br><br><b>Pour un total de "+montantTotalTTC+" € TTC </b><br><br>";
					
					switch (tenant) {
					case "legrain82":
						message+="<br><br>Pour prolonger ces services, merci de nous adresser votre règlement suivant l'un des moyens ci-dessous : <br><br>" + 
								 
							"- par carte bancaire en nous appelant au 05.63.30.31.44 <br><br>" + 

							"- par carte bancaire en vous connectant dans l’espace client depuis legrain.fr <br><br>" + 

							"- ou en nous faisant parvenir un RIB, nous pouvons mettre en place un prélèvement automatique <br><br>" + 

							"- ou par virement sur le compte suivant (attention changement de Rib) <br><br>" + 

							"FR76 1780 7000 0465 5210 7351 978     BIC CCBPFRPPTLS     <br>" + 
							"BANQUE POPULAIRE OCCITANE<br><br>" + 
							"Merci de privilégier les règlements en ligne plutôt que les chèques qui ne seront traités qu’une fois par semaine.";
						break;
					case "demo":
						message+="<br><br>Pour prolonger ces services, merci de nos adresser votre règlement suivant l'un des moyens ci-dessous : <br><br>" + 
								 
							"- par carte bancaire en nous appelant au 05.63.30.31.44 <br><br>" + 

							"- par carte bancaire en vous connectant dans l’espace client depuis legrain.fr <br><br>" + 

							"- ou en nous faisant parvenir un RIB, nous pouvons mettre en place un prélèvement automatique <br><br>" + 

							"- ou par virement sur le compte suivant (attention changement de Rib) <br><br>" + 

							"FR76 1780 7000 0465 5210 7351 978     BIC CCBPFRPPTLS     <br>" + 
							"BANQUE POPULAIRE OCCITANE<br><br>" + 
							"Merci de privilégier les règlements en ligne plutôt que les chèques qui ne seront traités qu’une fois par semaine.";
						break;

					default:
						message+="<br><br>Pour prolonger ces services, merci de nos adresser votre règlement.  <br><br>";
						break;
					}

					
				//	message+="<br><br> Votre code tiers est :"+tiers.getCodeTiers()+" <br><br> ";

					if(tiers.getTaEmail() != null) {
						destinataire = tiers.getTaEmail().getAdresseEmail();
					}
					
					
					
					
					 // le tiers n'a pas de mail, on envoi un mail au dossier, avec un mot supplémentaire
					if(destinataire == null) {
						String info = " | CE TIERS N'A PAS D'ADRESSE EMAIL ! <br><br>";
						String infoObjet = " | TIERS SANS MAIL |";
						
						StringBuilder sb = new StringBuilder(message);
						message = sb.insert(0, info).toString();
						StringBuilder sb2 = new StringBuilder(objet);
						objet = sb2.insert(0, infoObjet).toString();
						
						destinataire = taInfoEntrepriseService.findInstance().getEmailInfoEntreprise();
					}
					 
					//si on est sur serveur de test, on envoi un mail à yann@legrain.fr 
					if(lgrEnvironnementServeur.isModeTestEmailDossier())	{
						 destinataire = "yann@legrain.fr";
					}	
					 
					
						List<String> adressesDestinataireLoc = new ArrayList<>();
						List<TaEmail> emailTiersDestinataire = new ArrayList<>();
						List<TaContactMessageEmail> contactMessageEmailDestinataire = new ArrayList<>();
						List<String> adressesCcLoc = new ArrayList<>();
						List<TaEmail> emailTiersCc = null;
						List<TaContactMessageEmail> contactMessageEmailCc = new ArrayList<>();
						List<String> adressesBccLoc = new ArrayList<>();
						List<TaEmail> emailTiersBcc = null;
						List<TaContactMessageEmail> contactMessageEmailBcc = new ArrayList<>();
						File[] attachment = new File[1];
						List<String> adressesCc= new ArrayList<String>();
						List<String> adressesBcc= new ArrayList<String>();
						List<String> adressesDestinataire = new ArrayList<String>();
						
						
						
		
						adressesDestinataire.add(destinataire);
						
						if(!lgrEnvironnementServeur.isModeTestEmailDossier())	{
							if(tiers.getTaEmail() != null) {
								emailTiersDestinataire.add(tiers.getTaEmail());
							}
						}
						
						
						//on envoi un mail par tiers	
						String adresseExpediteur = "";
						
						
						
						//création avis échéance et envoi en pj
						if(mapEchParTiers.get(codeTiers) != null && !mapEchParTiers.get(codeTiers).isEmpty()){
							
								ParamAfficheChoixGenerationDocument param =  new ParamAfficheChoixGenerationDocument();
									String finDeLigne = "\r\n";
				
									List<IDocumentTiers> listeDocCrees=new LinkedList<IDocumentTiers>();
				
				
									param.getListeLigneEcheanceSrc().clear();
									param.setDateLivraison(param.getDateDocument());

									for (TaLEcheance ligne : mapEchParTiers.get(codeTiers)) {
										TaLEcheance l = taLEcheanceService.findById(ligne.getIdLEcheance());
										if(l != null) {
											param.getListeLigneEcheanceSrc().add(ligne);
										}
										
									}
														

									RetourGenerationDoc retour=null;
									String idCommande = null;
									
									//creation document	
									attachment = null; //nicolas : attachment à null et création d'AE et du PDF en commentaire, car il y a du CDI avec SessionScoped qui plante à partir des timers
//									if(param.getListeLigneEcheanceSrc().size()!=0){
//										retour=null; 
//										param.setCodeTiers(codeTiers);
//										//param.setIdDocumentExterne(idCommande);
//										//param.setCodeServiceExterne(serviceWeb.getCodeServiceWebExterne());
//										//param.setTypeEntiteExterne(typeEntite);
//										param.setTypeDest(TaAvisEcheance.TYPE_DOC);
//										creation.setCodeTiers(codeTiers);
//										creation.setIdDocumentExterne(idCommande);
//										creation.setParam(param);
//										creation.setDateDocument(new Date());
//										creation.setRegleDoc(false);
//										retour=creation.creationDocument(false);
//										if(retour!=null && retour.isRetour()&&retour.getDoc()!=null){
//											
//											TaActionEdition actionImprimer = new TaActionEdition();
//											actionImprimer.setCodeAction(TaActionEdition.code_mail);
//											Map<String, Object> mapParametreEdition = null;
//											mapParametreEdition = new HashMap<>();
//											//on appelle une methode qui va aller chercher (et elle crée un fichier xml (writing)) elle même l'edition par defaut choisie en fonction de l'action si il y a 
//											String pdf = taAvisEcheanceService.generePDF(retour.getDoc().getIdDocument(),mapParametreEdition,null,null,actionImprimer);
//											if(pdf != null) {
//												File file  = new File(pdf);
//												
////												ArrayList<File> arrayList = new ArrayList<File>(Arrays.asList(attachment));  
////												arrayList.add(file);
//												attachment[0]= file;
//											}
//											
//																		
//										}
//									}
									
									
									
									
//					
				
															
					
							
						}
						
						
						
						
						//envoi au tiers
						emailServiceFinder.sendAndLogEmailDossier(
								null,
								adresseExpediteur,//null,
								"BDG",
								objet,//sujet
								message, // message en string
								message, //message html 
								adressesDestinataire, //destinataires
								emailTiersDestinataire,
								contactMessageEmailDestinataire,
								adressesCc, 
								emailTiersCc,
								contactMessageEmailCc,
								adressesBcc, 
								emailTiersBcc,
								contactMessageEmailBcc,
								attachment,
								null,//replyTo
								null//nicolas : normalement c'est sessionInfo.getUtilisateur() mais encore une fois, il y a un probleme de SessionScope pour ce code si il est initié par un Timer
								);
						
						
						//envoi au tenant du dossier
						 adressesDestinataire = new ArrayList<String>();
						
						//on ajoute un peu d'info pour le tenant du dossier
						String info = "COPIE DU MAIL DE RELANCE ENVOYE AU TIERS "+tiers.getCodeTiers()+" :  <br><br>";
						String infoObjet;
						if(lgrEnvironnementServeur.isModeTestEmailDossier())	{
							adressesDestinataire.add("yann@legrain.fr");
							 infoObjet  = "SERVER DE TEST : COPIE MAIL RELANCE TIERS "+tiers.getCodeTiers()+" : ";
						}else {
							
							
							 infoObjet  = "COPIE MAIL RELANCE TIERS "+tiers.getCodeTiers()+" : ";
						}
						//on envoi le mail au tenant du dossier 
						if(taInfoEntrepriseService.findInstance().getEmailInfoEntreprise() != null ) {
							adressesDestinataire.add(taInfoEntrepriseService.findInstance().getEmailInfoEntreprise());
						}
						
						StringBuilder sb = new StringBuilder(message);
						message = sb.insert(0, info).toString();
						StringBuilder sb2 = new StringBuilder(objet);
						objet = sb2.insert(0, infoObjet).toString();
						emailServiceFinder.sendAndLogEmailDossier(
								null,
								adresseExpediteur,//null,
								"BDG",
								objet,//sujet
								message, // message en string
								message, //message html 
								adressesDestinataire, //destinataires
								emailTiersDestinataire,
								contactMessageEmailDestinataire,
								adressesCc, 
								emailTiersCc,
								contactMessageEmailCc,
								adressesBcc, 
								emailTiersBcc,
								contactMessageEmailBcc,
								attachment,
								null,//replyTo
								null//nicolas : normalement c'est sessionInfo.getUtilisateur() mais encore une fois, il y a un probleme de SessionScope pour ce code si il est initié par un Timer
								);
						
					
				
				
				
				
				
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	/**
	 * Ne sert pas pour le moment car on laisser tomber la création automatique d'avis d'échéance
	 */
	private void creerAvisEcheanceParTiers() {
		//rechercher les abonnements (non annulés) dont la date de fin période active arrive à échéance dans X jours ou moins
		//List<TaAbonnementDTO> listeAbonnementsDTO = taAbonnementService.findAllEnCoursDTOArrivantAEcheanceDansXJours(NB_JOUR_AVANT_ECHEANCE, null);
		//Map<String,List<TaAbonnementDTO>> mapAboATraiteParTiers = new HashMap<>();
		
		//List<TaLEcheance> listeEcheance = taLEcheanceService.findAllArrivantAEcheanceDansXJours(NB_JOUR_AVANT_ECHEANCE, null);
		List<TaLEcheance> listeEcheance = null;
		Map<String,List<TaLEcheance>> mapEchATraiteParTiers = new HashMap<>();
		
		System.out.println("Nb d'écheance qui arrivent à échéance (période active) dans "+NB_JOUR_AVANT_ECHEANCE+" jours : "+listeEcheance.size());
		
		//on trie les abos par tiers
		for (TaLEcheance ech : listeEcheance) {
			//controle sur l'annulation de l'abonnement
			TaAbonnement abo = ech.getTaAbonnement();
			if(abo.getDateAnnulation() == null ) {
				String cle = abo.getTaTiers().getCodeTiers();
				if(!mapEchATraiteParTiers.keySet().contains(cle)) {
					mapEchATraiteParTiers.put(cle, new ArrayList<>());
					
				}
				mapEchATraiteParTiers.get(cle).add(ech);
				
			}
		}
		
		for (String codeTiers : mapEchATraiteParTiers.keySet()) {
			if(!mapEchATraiteParTiers.get(codeTiers).isEmpty()) {
				
				for (TaLEcheance abo : mapEchATraiteParTiers.get(codeTiers)) {
					
				}
			}
		}
		
		
		
	}
	
	/**
	 * Méthode qui suspend les lignes d'échéances dépassé mais aussi leur lignes d'abonnement associés
	 */
	private void suspendEcheances() {
		 taLEcheanceService.suspendEcheances();

	}
	
	/**
	 * Méthode qui supprime les lignes d'échéances suspendue avec délai de survie dépassé, et annule lignes d'abonnement associées
	 */
	private void supprimeEcheancesSuspendueDelaiSurvie() {
		taLEcheanceService.supprimeEcheanceDelaiSurvieDepasse();
	}
	
	//changer par yann le 30/09/2020
	public void doWorkOld(String tenant) {
				
		initTenantRegistry();
		initTenant(tenant);
		Date now = new Date();
		try {
//			//rechercher les lignes d'échéances non deja relié a des ligne d'avis d'échéance
			List<TaLEcheance> listeLEcheanceNonLie = taLEcheanceService.rechercheEcheanceNonLieAAvisEcheanceTiers();
			
			Map<String,List<TaLEcheance>> mapEcheanceATraiteParTiers = new HashMap<>();
			System.out.println("Nb échéances non liées : "+listeLEcheanceNonLie.size());
			for (TaLEcheance ech : listeLEcheanceNonLie) {
//				if(ech.getIdLEcheance() == 559) {
//					System.out.println("je suis sur l'écheance recherchée!! ");
//				}
				//trier par tiers pour regroupement
//				taStripeCustomerService.rechercherCustomer(ech.getTaStripeSubscription().)
				if(/*ech.getTaStripeSubscription().getDateAnnulation()==null &&*/ ech.getDateAnnulation()==null) { //l'échéance n'a pas été annuler donc on peut la traiter
				
					//String cle = ech.getTaStripeSubscription().getTaAbonnement().getTaTiers().getCodeTiers();
					String cle = ech.getTaAbonnement().getTaTiers().getCodeTiers();
					if(!mapEcheanceATraiteParTiers.keySet().contains(cle)) {
						mapEcheanceATraiteParTiers.put(cle, new ArrayList<>());
					}
					LocalDateTime localDateTimeEcheance = new Date(ech.getDateEcheance().getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					LocalDateTime localDateTimeNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
					
					/*
					 * filtrer les échéances à traiter ou pas en fonction des dates
					 * TODO en cas de regroupement pour un tiers on ne comparera pas avec NB_JOUR_AVANT_ECHEANCE mais par rapport à une période
					 */
					LocalDateTime localDateTimeGenerationEcheance = localDateTimeEcheance.minusDays(NB_JOUR_AVANT_ECHEANCE);
					if( localDateTimeGenerationEcheance.isBefore(localDateTimeNow)) {
						//si on est ce jour là, verifier que non deja traiter/creer et creer
						Date dateTimeEcheance = Date.from(localDateTimeGenerationEcheance.atZone(ZoneId.systemDefault()).toInstant());
						mapEcheanceATraiteParTiers.get(cle).add(ech);
					}
				}
				
			}
			
			//pour chaques tiers qui a des lignes d'échéances qui ne sont pas liés a des lignes d'avis d'échéances et dont la date d'échéances est dans moins de 10 jours :
			for (String codeTiers : mapEcheanceATraiteParTiers.keySet()) {
				List<Integer> listeIdAbonnementDejaTraite = new ArrayList<>();
				
				//Génération AvisEch et "paiement prévu"
				if(!mapEcheanceATraiteParTiers.get(codeTiers).isEmpty()) { //il y a bien des échéances à traiter pour ce tiers
					TaAvisEcheance taAvisEcheance = creerAvisEcheance(codeTiers, mapEcheanceATraiteParTiers.get(codeTiers));
					taAvisEcheance.calculeTvaEtTotaux();
					taAvisEcheance = taAvisEcheanceService.merge(taAvisEcheance);
					TaStripePaiementPrevu pp = creerPaiementPrevu(taAvisEcheance);
					if(pp!= null ) {
						pp = taStripePaiementPrevuService.merge(pp);
					}
					
					//pour chaques  lignes d'échéances du tiers qui ne sont pas liés a des lignes d'avis d'échéances et dont la date d'échéances est dans moins de 10 jours :
					//quand on "utilise" une ligne d'échéance pour la mettre dans un Avis d'échéance, 
					//on génère la ligne d'échéance suivante
					for (TaLEcheance e : mapEcheanceATraiteParTiers.get(codeTiers)) {
						//si on a pas deja traité cet abonnement lié a cette ligne d'échéance
						//if(!listeIdAbonnementDejaTraite.contains(e.getTaStripeSubscription().getIdStripeSubscription())) {
						if(!listeIdAbonnementDejaTraite.contains(e.getTaAbonnement().getIdDocument())) {
							//quand on passe l'abonnement en paramètre il va généré potentiellement toute les ligne d'échéance suivante (une par plan/ligne d'abonnement)
							//il ne faut donc pas s'"occuper" des autre échéances de ce même abonnement car elles auront deja été traitées
 							List<TaLEcheance> listeProchainesEcheances = taAbonnementService.genereProchainesEcheances(e.getTaAbonnement(),e);
							for (TaLEcheance taLEcheance : listeProchainesEcheances) {
								taLEcheance = taLEcheanceService.merge(taLEcheance);
							}
							//une fois l'échéance traité on met son abo dans la  liste des abo déjà traités
							//listeIdAbonnementDejaTraite.add(e.getTaStripeSubscription().getIdStripeSubscription());
							listeIdAbonnementDejaTraite.add(e.getTaAbonnement().getIdDocument());
						}
					}
				}
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	
	public TaStripePaiementPrevu creerPaiementPrevu(TaAvisEcheance taAvisEcheance) {
		TaStripePaiementPrevu taStripePaiementPrevu = null;
		
		//mapperUIToModelPaiementPrevu.map(selectedTaStripePaiementPrevuDTO, taStripePaiementPrevu);
		
		try {
			
//			if(taAvisEcheance.getLignes().get(0).getTaLEcheance().getTaStripeSubscription().getTaStripeCustomer() != null &&
//					taAvisEcheance.getLignes().get(0).getTaLEcheance().getTaStripeSubscription().getTaStripeSource() != null) {

			if(taAvisEcheance.getLignes().get(0).getTaLEcheance().getTaAbonnement().getTaStripeCustomer() != null &&
					taAvisEcheance.getLignes().get(0).getTaLEcheance().getTaAbonnement().getTaStripeSource() != null) {
				
				taStripePaiementPrevu = new TaStripePaiementPrevu();
				taStripePaiementPrevu.setTaStripeCustomer(taAvisEcheance.getLignes().get(0).getTaLEcheance().getTaAbonnement().getTaStripeCustomer());
				
//				taStripeSubscription.setStatus(null);
				String sourceStripeID = null;
				sourceStripeID = taAvisEcheance.getLignes().get(0).getTaLEcheance().getTaAbonnement().getTaStripeSource().getIdExterne();
				if(sourceStripeID != null) {
//						String sourceStripeID = taStripeCustomer.getTaSourceDefault().getIdExterne();
					Integer timpeStampBillingCycleAnchor = null;
					String billing = null;
					Integer daysUntilDue = null;
					
					taStripePaiementPrevu.setMontant(taAvisEcheance.getMtTtcCalc());
					taStripePaiementPrevu.setDateDeclenchement(taAvisEcheance.getDateEchDocument());
					//taStripePaiementPrevu.setTaStripeSubscription(taAvisEcheance.getLignes().get(0).getTaLEcheance().getTaStripeSubscription());
					taStripePaiementPrevu.setTaAbonnement(taAvisEcheance.getLignes().get(0).getTaLEcheance().getTaAbonnement());
					taStripePaiementPrevu.setTaAvisEcheance(taAvisEcheance);
					
					if(sourceStripeID.equals("Chèque") || sourceStripeID.equals("Virement")) {
//							//Mode manuel, pas de prélèvement automatique
//							billing = "send_invoice";
//							daysUntilDue = 30;
//							taStripeSubscription.setBilling(billing);
//							taStripeSubscription.setDaysUntilDue(daysUntilDue);
						sourceStripeID = null;
					} else {
						taStripePaiementPrevu.setTaStripeSource(taStripeSourceService.rechercherSource(sourceStripeID));
					}
					
//						//TODO mise en place ou mise à jour d'un timer ?
//						if(taStripePaiementPrevu.getTimerHandle()!=null) {
//							//un timer existe deja pour cette notification, on l'annule et on le recréer
//							TimerHandle h = (TimerHandle) SerializationUtils.deserialize(taStripePaiementPrevu.getTimerHandle());
//							try {
//								Timer t = h.getTimer();
//								t.cancel();
//							} catch (NoSuchObjectLocalException ex) {
//								System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
//							}
//						}
//						taStripePaiementPrevu.setTimerHandle(SerializationUtils.serialize((Serializable) timerDeclenchePaiementPrevuService.creerTimer(taStripePaiementPrevu)));
				}
			}
			

			

		} catch(Exception e) {
			e.printStackTrace();
		}
		return taStripePaiementPrevu;
	}
	
	public TaAvisEcheance creerAvisEcheance(TaStripeInvoice taStripeInvoice) {
		try {
			TaAvisEcheance taAvisEcheance = new TaAvisEcheance();
			TaAvisEcheanceDTO dto = new TaAvisEcheanceDTO();
			TaInfosAvisEcheance taInfosDocument = new TaInfosAvisEcheance();
			LgrDozerMapper<TaAvisEcheanceDTO,TaAvisEcheance> mapperUIToModel = new LgrDozerMapper<>();
			LgrDozerMapper<TaLAvisEcheanceDTO,TaLAvisEcheance> mapperUIToModelLigne = new LgrDozerMapper<>();
			
			if(taInfosDocument==null) {
					taInfosDocument = new TaInfosAvisEcheance();
			}
			if(taAvisEcheance!=null) {
				taInfosDocument.setTaDocumentGeneral(taAvisEcheance);
				taAvisEcheance.setTaInfosDocument(taInfosDocument);
			}
		
			//taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			dto.setCodeDocument(taAvisEcheanceService.genereCode(null)); //ejb
			//validateUIField(Const.C_CODE_TIERS,dto.getCodeTiers());//permet de verrouiller le code genere
		
			dto.setIdTiers(dto.getIdTiers());
			dto.setCodeTiers(dto.getCodeTiers());
			taAvisEcheance.setTaTiers(taTiersService.findById(taStripeInvoice.getTaCustomer().getTaTiers().getIdTiers()));
			dto.setCodeCompta(taAvisEcheance.getTaTiers().getCodeCompta());
			dto.setCompte(taAvisEcheance.getTaTiers().getCompte());
			taAvisEcheance.getTaInfosDocument().setCodeCompta(taAvisEcheance.getTaTiers().getCodeCompta());
			taAvisEcheance.getTaInfosDocument().setCompte(taAvisEcheance.getTaTiers().getCompte());
			
			dto.setLibelleDocument("Avis d'échéance");
			
			mapperUIToModel.map(dto, taAvisEcheance);
			
			TaLAvisEcheance l = null;
			TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
			for (TaStripeInvoiceItem ldto : taStripeInvoice.getItems()) {
				l = new TaLAvisEcheance();
	//			mapperUIToModelLigne.map(ldto, l);
				l.setTaArticle(taArticleService.findById(ldto.getTaPlan().getTaArticle().getIdArticle()));
	////			l.setTaLot(taLotService.findByCode(ldto.getNumLot()));
				l.setTaTLigne(taTLigne);
	//			//l.setQteLDocument(ldto.getQteLDocument());
				taAvisEcheance.addLigne(l);
				l.setTaDocument(taAvisEcheance);
			}
			
			//taAvisEcheance = taAvisEcheanceService.merge(taAvisEcheance);
			//taStripeInvoice.setTaAvisEcheance(taAvisEcheance);
			//taStripeInvoice = taStripeInvoiceService.merge(taStripeInvoice);
	
			return taAvisEcheance;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaAvisEcheance creerAvisEcheance(String codeTiers, List<TaLEcheance> listeLigneEcheance) {
		
		return taAvisEcheanceService.creerAvisEcheance(listeLigneEcheance);
//		try {
//			TaAvisEcheance taAvisEcheance = new TaAvisEcheance();
//			TaAvisEcheanceDTO dto = new TaAvisEcheanceDTO();
//			TaInfosAvisEcheance taInfosDocument = new TaInfosAvisEcheance();
//			LgrDozerMapper<TaAvisEcheanceDTO,TaAvisEcheance> mapperUIToModel = new LgrDozerMapper<>();
//			LgrDozerMapper<TaLAvisEcheanceDTO,TaLAvisEcheance> mapperUIToModelLigne = new LgrDozerMapper<>();
//			
//			if(taInfosDocument==null) {
//					taInfosDocument = new TaInfosAvisEcheance();
//			}
//			if(taAvisEcheance!=null) {
//				taInfosDocument.setTaDocumentGeneral(taAvisEcheance);
//				taAvisEcheance.setTaInfosDocument(taInfosDocument);
//			}
//			
//			if(codeTiers.equals("3141")) {
//				System.out.println("je suis sur LEVY en train de créer son avis d'échéance");
//			}
//		
//			//taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
//			dto.setCodeDocument(taAvisEcheanceService.genereCode(null)); //ejb
//			//validateUIField(Const.C_CODE_TIERS,dto.getCodeTiers());//permet de verrouiller le code genere
//		
//			dto.setIdTiers(dto.getIdTiers());
//			dto.setCodeTiers(dto.getCodeTiers());
//			taAvisEcheance.setTaTiers(taTiersService.findByCode(codeTiers));
//			dto.setCodeCompta(taAvisEcheance.getTaTiers().getCodeCompta());
//			dto.setCompte(taAvisEcheance.getTaTiers().getCompte());
//			taAvisEcheance.setNbDecimalesPrix(2);
//			taAvisEcheance.setNbDecimalesQte(2);
//			taAvisEcheance.getTaInfosDocument().setCodeCompta(taAvisEcheance.getTaTiers().getCodeCompta());
//			taAvisEcheance.getTaInfosDocument().setCompte(taAvisEcheance.getTaTiers().getCompte());
//			
//			//rajout yann
//			if(taAvisEcheance.getTaTiers().getTaAdresse() != null) {
//				taAvisEcheance.getTaInfosDocument().setAdresse1(taAvisEcheance.getTaTiers().getTaAdresse().getAdresse1Adresse());
//				taAvisEcheance.getTaInfosDocument().setAdresse2(taAvisEcheance.getTaTiers().getTaAdresse().getAdresse2Adresse());
//				taAvisEcheance.getTaInfosDocument().setAdresse3(taAvisEcheance.getTaTiers().getTaAdresse().getAdresse3Adresse());
//				taAvisEcheance.getTaInfosDocument().setAdresse1Liv(taAvisEcheance.getTaTiers().getTaAdresse().getAdresse1Adresse());
//				taAvisEcheance.getTaInfosDocument().setAdresse2Liv(taAvisEcheance.getTaTiers().getTaAdresse().getAdresse2Adresse());
//				taAvisEcheance.getTaInfosDocument().setAdresse3Liv(taAvisEcheance.getTaTiers().getTaAdresse().getAdresse3Adresse());
//				
//				taAvisEcheance.getTaInfosDocument().setCodepostal(taAvisEcheance.getTaTiers().getTaAdresse().getCodepostalAdresse());
//				taAvisEcheance.getTaInfosDocument().setCodepostalLiv(taAvisEcheance.getTaTiers().getTaAdresse().getCodepostalAdresse());
//				
//				taAvisEcheance.getTaInfosDocument().setVille(taAvisEcheance.getTaTiers().getTaAdresse().getVilleAdresse());
//				taAvisEcheance.getTaInfosDocument().setVilleLiv(taAvisEcheance.getTaTiers().getTaAdresse().getVilleAdresse());
//				taAvisEcheance.getTaInfosDocument().setPays(taAvisEcheance.getTaTiers().getTaAdresse().getPaysAdresse());
//				taAvisEcheance.getTaInfosDocument().setPaysLiv(taAvisEcheance.getTaTiers().getTaAdresse().getPaysAdresse());
//			}
//			
//			if(taAvisEcheance.getTaTiers().getTaTCivilite() != null) {
//				taAvisEcheance.getTaInfosDocument().setCodeTCivilite(taAvisEcheance.getTaTiers().getTaTCivilite().getCodeTCivilite());
//			}
//			
//			if(taAvisEcheance.getTaTiers().getTaTEntite() != null) {
//				taAvisEcheance.getTaInfosDocument().setCodeTEntite(taAvisEcheance.getTaTiers().getTaTEntite().getCodeTEntite());
//			}
//			
//			if(taAvisEcheance.getTaTiers().getTaTTvaDoc() != null) {
//				taAvisEcheance.getTaInfosDocument().setCodeTTvaDoc(taAvisEcheance.getTaTiers().getTaTTvaDoc().getCodeTTvaDoc());
//			}
//			
//			
//			
//			
////			taAvisEcheance.getTaInfosDocument().setFinMoisCPaiement(finMoisCPaiement);
////			taAvisEcheance.getTaInfosDocument().setCodeCPaiement(codeCPaiement);
////			taAvisEcheance.getTaInfosDocument().setReportCPaiement(reportCPaiement);
//			
//			
//			taAvisEcheance.getTaInfosDocument().setNomTiers(taAvisEcheance.getTaTiers().getNomTiers());
//			taAvisEcheance.getTaInfosDocument().setPrenomTiers(taAvisEcheance.getTaTiers().getPrenomTiers());
//			taAvisEcheance.getTaInfosDocument().setSurnomTiers(taAvisEcheance.getTaTiers().getSurnomTiers());
//			if(taAvisEcheance.getTaTiers().getTaEntreprise() != null) {
//				taAvisEcheance.getTaInfosDocument().setNomEntreprise(taAvisEcheance.getTaTiers().getTaEntreprise().getNomEntreprise());
//			}
//			
//			
//			
//			
//			dto.setLibelleDocument("Avis d'échéance");
//			
//			mapperUIToModel.map(dto, taAvisEcheance);
//			
//			TaLAvisEcheance l = null;
//			TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
//			for (TaLEcheance ldto : listeLigneEcheance) {
//				l = new TaLAvisEcheance();
//	//			mapperUIToModelLigne.map(ldto, l);
//				l.setTaArticle(taArticleService.findById(ldto.getTaArticle().getIdArticle()));
//	////			l.setTaLot(taLotService.findByCode(ldto.getNumLot()));
//				
//				if(codeTiers.equals("3141") && l.getTaArticle().getCodeArticle().equals("OPT-SAUVR")) {
//					System.out.println("Je suis sur l'article OPT-SAUVR qui va aussi créer une ligne de commentaire completement HS https://loyes.bdg.cloud");
//				}
//				l.setTaTLigne(taTLigne);
//				//l.setTaLEcheance(ldto);
//				l.setQteLDocument(ldto.getQteLDocument());
//				l.setLibLDocument(ldto.getLibLDocument());
//				l.setPrixULDocument(ldto.getPrixULDocument());
//				l.setMtHtLDocument(ldto.getMtHtLDocument());
//				l.setMtTtcLDocument(ldto.getMtTtcLDocument());
//				//rajout yann
////				l.setQte2LDocument();
////				l.setU2LDocument();
//				l.setCompteLDocument(l.getTaArticle().getNumcptArticle());
//				l.setU1LDocument(ldto.getU1LDocument());
//				//l.setCodeTTvaLDocument();
//				l.setRemHtLDocument(ldto.getRemHtLDocument());
//				l.setRemTxLDocument(ldto.getRemTxLDocument());
//				//l.setLibTvaLDocument();
//				
//				//l.setTauxTvaLDocument(ldto.getTauxTvaLDocument());
//				l.setCodeTvaLDocument(l.getTaArticle().getTaTva().getCodeTva());
//				l.setTauxTvaLDocument(l.getTaArticle().getTaTva().getTauxTva());
//				taAvisEcheance.addLigne(l);
//				l.setTaDocument(taAvisEcheance);
//				
//				//ligne de commentaire sur la période
//				l = new TaLAvisEcheance();
//				l.setTaTLigne(taTLigne);
//				l.setTaLEcheance(ldto);
//				l.setLibLDocument("Période du "+LibDate.dateToString(ldto.getDebutPeriode())+" au "+LibDate.dateToString(ldto.getFinPeriode()));
//				taAvisEcheance.addLigne(l);
//				l.setTaDocument(taAvisEcheance);
//				
//				//lignes de commentaires sur le produit (compléments)
////				if(ldto.getTaStripeSubscriptionItem()!=null) {
////					if(ldto.getTaStripeSubscriptionItem().getComplement1()!=null) {
//				if(ldto.getTaLAbonnement()!=null) {
//					if(ldto.getTaLAbonnement().getComplement1()!=null) {
//						l = new TaLAvisEcheance();
//						l.setTaTLigne(taTLigne);
//						//l.setTaLEcheance(ldto);
//						//l.setLibLDocument(ldto.getTaStripeSubscriptionItem().getComplement1());
//						l.setLibLDocument(ldto.getTaLAbonnement().getComplement1());
//						taAvisEcheance.addLigne(l);
//						l.setTaDocument(taAvisEcheance);
//					}
//					if(ldto.getTaLAbonnement().getComplement2()!=null) {
//						l = new TaLAvisEcheance();
//						l.setTaTLigne(taTLigne);
//						//l.setTaLEcheance(ldto);
//						l.setLibLDocument(ldto.getTaLAbonnement().getComplement2());
//						taAvisEcheance.addLigne(l);
//						l.setTaDocument(taAvisEcheance);
//					}
//					if(ldto.getTaLAbonnement().getComplement3()!=null) {
//						l = new TaLAvisEcheance();
//						l.setTaTLigne(taTLigne);
//						//l.setTaLEcheance(ldto);
//						l.setLibLDocument(ldto.getTaLAbonnement().getComplement3());
//						taAvisEcheance.addLigne(l);
//						l.setTaDocument(taAvisEcheance);
//					}
//				}
//			}
//			
//			//taAvisEcheance = taAvisEcheanceService.merge(taAvisEcheance);
//			//taStripeInvoice.setTaAvisEcheance(taAvisEcheance);
//			//taStripeInvoice = taStripeInvoiceService.merge(taStripeInvoice);
//	
//			return taAvisEcheance;
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		return null;
	}
	
	@Transactional(value=TxType.REQUIRES_NEW)
	/**
	 * Déclenchement d'un "paiement prévu" dont la date déclenchement est arrivé à terme.
	 * @param tenant
	 * @param paiementPrevu
	 */
	public void doWork(String tenant, TaStripePaiementPrevu paiementPrevu) {
				
		initTenantRegistry();
		initTenant(tenant);
		try {
//			//rechercher les paiement à déclencher
//			taStripeSubscriptionService.selectAll();
			
			taStripePaiementPrevuService.declencherPaiementStripe(paiementPrevu);

		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

}

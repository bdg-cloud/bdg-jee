package fr.legrain.tache.service;

import java.io.File;
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
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
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
import fr.legrain.bdg.email.service.remote.IEnvoiEmailViaServiceExterneDossierService;
import fr.legrain.bdg.paiement.service.remote.IPaiementEnLigneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaEvenementServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaNotificationServiceRemote;
import fr.legrain.bdg.tache.service.remote.ITaRecurrenceEvenementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaRDocument;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.email.service.EmailLgr;
import fr.legrain.email.service.EnvoiEmailService;
import fr.legrain.email.service.LgrEmailSMTPService;
import fr.legrain.email.service.LgrMailjetService;
import fr.legrain.general.dao.IDatabaseDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.mail.LgrMailjet;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.paiement.service.MyQualifierLiteral;
import fr.legrain.paiement.service.PaiementLgr;
import fr.legrain.push.service.BdgFirebaseService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tache.model.TaNotification;
import fr.legrain.tache.model.TaRDocumentEvenement;
import fr.legrain.tache.model.TaTypeNotification;
import fr.legrain.tiers.model.TaEmail;
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
public class MultitenantProxyTimerEvenement {

	private String tenant = null;
	private TransactionSynchronizationRegistry reg;
	
	@Inject private Event<TaNotification> events;
		
	@EJB private ITaEvenementServiceRemote taEvenementService;
	@EJB private ITaRecurrenceEvenementServiceRemote taRecurrenceEvenementService;
	@EJB private ITaNotificationServiceRemote taNotificationService;
//	@EJB private IEnvoiEmailViaServiceExterneDossierService lgrMailjetService;
	@EJB private EnvoiEmailService emailServiceFinder;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private BdgFirebaseService bdgFirebaseService;
	
	//@EJB private DatabaseService s;
	@Inject private IDatabaseDAO dao;

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
	public void doWork(String tenant,TaNotification n) {
				
		initTenantRegistry();
		initTenant(tenant);
		try {
			List<TaNotification> listeNotification = null;
					
			if(n!=null) {
				listeNotification = new ArrayList<>();
				n = taNotificationService.findById(n.getIdNotification());
				if(n.getTaEvenementUtilisateur()!=null) {
					n.getTaEvenementUtilisateur().getLibelleEvenement(); //Pour éviter le no session dans le message push, normalement pas besoin car la propriete est en eager, un peu bizzare
				} else {
					n.getTaEvenementTiers().getLibelleEvenement();
				}
				listeNotification.add(n);
			} else {
				listeNotification = taNotificationService.selectAll();
			}
			
			
	        System.out.println("Timer notifications des évènements : "+tenant);
	        
	//        TaNotification n = new TaNotification();
	//        n.setVersion(tenant); // a supprimer
	        
	        //events.fire(n);
	        
	        for (TaNotification notif : listeNotification) {
	        	String codeType = notif.getTaTypeNotification().getCodeTypeNotification();
	        	
	        	switch (codeType) {
				case TaTypeNotification.CODE_TYPE_NOTIFICATION_EMAIL:
//					if(notif.getTaEvenementTiers().getListeDocument()!=null && !notif.getTaEvenementTiers().getListeDocument().isEmpty()) {
//						//pour l'instant on ne gère qu'un seul document
					
//						//TODO voir si on peu faire une injection dynamique du service en fonction du type du document, système de qualifier comme pour le paiement du compte client

					//	TaRDocumentEvenement rdoc = notif.getTaEvenementTiers().getListeDocument().get(0);
//						SWTDocument swtDoc = rdoc.getSWTDocument();
//					}
					TaInfoEntreprise taInfoEntreprise =  taInfoEntrepriseService.findInstance();
					if(notif.getListeUtilisateur()!=null) {
						for (TaUtilisateur u : notif.getListeUtilisateur()) {
							if(u.getEmail()!=null) {
//								lgrMailjetService.send(
//										null, 
//										"BDG", 
//										"Notification : "+notif.getTaEvenementUtilisateur().getLibelleEvenement(), 
//										notif.getTaEvenementUtilisateur().getLibelleEvenement(), 
//										notif.getTaEvenementUtilisateur().getLibelleEvenement(), 
//										new String[]{u.getEmail()},
//										null);
								//TODO enregistrer l'email dans l'historique des emails
								//taMessageEmail = taMessageEmailService.merge(taMessageEmail);
								//cf : EnvoyerEmailController => déplacer le code dans un service pour lier l'envoi à l'enregistrement
								
								List<String> adressesDestinataire = new ArrayList<>();
								List<TaEmail> emailTiersDestinataire = null;
								List<TaContactMessageEmail> contactMessageEmailDestinataire  = null;
								List<String> adressesCc = null;
								List<TaEmail> emailTiersCc = null;
								List<TaContactMessageEmail> contactMessageEmailCc = null;
								List<String> adressesBcc = null;
								List<TaEmail> emailTiersBcc = null;
								List<TaContactMessageEmail> contactMessageEmailBcc = null;
								File[] attachment = null;
								
								adressesDestinataire.add(u.getEmail());
								
								emailServiceFinder.sendAndLogEmailProgramme(
										null,
										"BDG",
										taInfoEntreprise.getNomInfoEntreprise()+" : "+notif.getTaEvenementUtilisateur().getLibelleEvenement(),
										notif.getTaEvenementUtilisateur().getLibelleEvenement(), 
										notif.getTaEvenementUtilisateur().getLibelleEvenement(), 
										adressesDestinataire, 
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
										notif.getTaEvenementUtilisateur().getProprietaire()
										);
							}
						}
					}
					if(notif.getListeTiers()!=null) {
						for (TaTiers t : notif.getListeTiers()) {
							if(t.getTaEmail()!=null && t.getTaEmail().getAdresseEmail()!=null) {
//								lgrMailjetService.send(
//										null, 
//										"BDG", 
//										"Notification : "+notif.getTaEvenementTiers().getLibelleEvenement(), 
//										notif.getTaEvenementTiers().getLibelleEvenement(), 
//										notif.getTaEvenementTiers().getLibelleEvenement(), 
//										new String[]{t.getTaEmail().getAdresseEmail()},
//										null);
								
								List<String> adressesDestinataire = new ArrayList<>();
								List<TaEmail> emailTiersDestinataire = null;
								List<TaContactMessageEmail> contactMessageEmailDestinataire  = null;
								List<String> adressesCc = null;
								List<TaEmail> emailTiersCc = null;
								List<TaContactMessageEmail> contactMessageEmailCc = null;
								List<String> adressesBcc = null;
								List<TaEmail> emailTiersBcc = null;
								List<TaContactMessageEmail> contactMessageEmailBcc = null;
								File[] attachment = null;

								
								adressesDestinataire.add(t.getTaEmail().getAdresseEmail());
								
								emailServiceFinder.sendAndLogEmailProgramme(
										null,
										"BDG",
										taInfoEntreprise.getNomInfoEntreprise()+" : "+notif.getTaEvenementTiers().getLibelleEvenement(),
										notif.getTaEvenementTiers().getLibelleEvenement(), 
										notif.getTaEvenementTiers().getLibelleEvenement(), 
										adressesDestinataire, 
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
										notif.getTaEvenementTiers().getProprietaire()
										);
							}
						}
					}	
					
					break;
				case TaTypeNotification.CODE_TYPE_NOTIFICATION_WEBAPP:
					notif.setVersion(tenant);
					events.fire(notif);
					
					break;
				case TaTypeNotification.CODE_TYPE_NOTIFICATION_SMS:
					
					break;
				case TaTypeNotification.CODE_TYPE_NOTIFICATION_APPLI_MOBILE:
					//bdgFirebaseService.envoyerMessagePush(utilisateur);
					break;
	
				default:
					break;
				}
	        	
	        	notif.setNotificationEnvoyee(true);
				notif.setLu(false);
				taNotificationService.merge(notif);
			}
		} catch (FinderException e) {
			// TODO Auto-generated catch block
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

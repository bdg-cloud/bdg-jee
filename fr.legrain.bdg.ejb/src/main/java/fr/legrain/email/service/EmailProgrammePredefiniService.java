package fr.legrain.email.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.rest.model.ParamDemandeRenseignement;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibDate;
import fr.legrain.moncompte.ws.TaDossier;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@Interceptors(ServerTenantInterceptor.class)
public class EmailProgrammePredefiniService {
	
	private @EJB EnvoiEmailService emailServiceFinder;
	private @EJB ITaParamEspaceClientServiceRemote taParamEspaceClientService;
	private @EJB ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private BdgProperties bdgProperties;
	
	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	
	private @EJB ITaPanierServiceRemote taPanierService;
	
	@PostConstruct
	private void init() {
		bdgProperties = new BdgProperties();
	}
	
	/**
	 * A SUPPRIMER
	 * @param fournisseur
	 * @param taEspaceClient
	 * @param client
	 * @param tenant
	 * @param alias
	 * @param mdpClair
	 */
	public void emailEspaceClientCreerTous(TaTiers fournisseur, TaEspaceClient taEspaceClient, TaTiers client, String tenant, String alias, String mdpClair){ 
//		Sujet : Activation l'espace client auprès de la société [NomDeLaSociete]
//		Bonjour,
//		Nous avons le plaisir de vous informer que dès à présent vous pouvez récupérer vos documents, comme les factures, depuis l'espace client de notre programme de gestion Bureau de Gestion.
//		Si vous possédez déjà un compte, il vous suffira d'établir une liaison avec notre société en vous rendant sur votre espace client que vous trouverez à l'adresse suivante : https://espaceclient.bdg.cloud
//		Si vous n'avez pas de compte, vous pouvez en créer un en vous rendant à la même adresse : https://espaceclient.bdg.cloud
//		Et ensuite, il vous suffira d'établir une liaison avec notre société.
//		Rappel : pour établir une liaison avec notre société, il vous suffit de vous munir de votre compte client que avez auprès de la société [NomDeLaSociete] afin d'être identifié. Vous recevrez ensuite un email pour valider votre liaison.
//		Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.
//		Très cordialement,
//		[NomDeLaSociete] 
			String subject = "Activation de votre nouvel espace client auprès de la société Legrain Informatique";
			
			String url = "https://espace-client.legrain.fr";//OSS_REMOVE
//			TaParamEspaceClient param = taParamEspaceClientService.findInstance();
//			if(param.getUrlPerso()!=null && !param.getUrlPerso().equals("")) {
//				url = param.getUrlPerso();
//			}
			
			//OSS_REMOVE
			String msgTxt = "Bonjour,<br/>"
					+"<br/>"
					+"Nous avons le plaisir de vous informer de la migration de votre compte legrain vers un espace plus moderne et plus complet.<br/>"
					+"<br/>"
					+"Ce nouvel espace vous permettra de réaliser de nombreuses opérations comme le téléchargement et le règlement par carte bancaire de vos factures, le suivi de vos abonnements ainsi que le téléchargement de vos logiciels.<br/>"
					+"<br/>"
					+"Vous le trouverez à l'adresse suivante : <br/>"
					+url+"<br/>"
					+"ou en cliquant sur \"Mon compte\" puis \"Mon compte\" sur la page d'accueil de https://www.legrain.fr/<br/>"
					//OSS_REMOVE
					+"<br/>"
					+"Identifiant et mot de passe à utiliser pour vous y connecter :<br/>"
					+"<br/>"
					+"Identifiant : "+taEspaceClient.getEmail()+"<br/>"
					+"<br/>"
					+"Mot de passe : "+mdpClair+"<br/>"
					+"<br/>"
					+"Par ailleurs, nous conservons pour l'instant l'ancien espace qui se trouve à l'adresse http://moncompte.legrain.fr<br/>"  
					+"ou en cliquant sur \"Mon compte\" puis \"Ancien compte\" sur la page d'accueil de https://www.legrain.fr/<br/>"
					+"avec vos anciens identifiants.<br/>"
					+ "<br/>"
					+"Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter Legrain Informatique au 05.63.30.31.44.<br/>"
					//OSS_REMOVE
					+"<br/>"
					+"Très cordialement,<br/>"
					+"<br/>"
					+"Legrain Informatique"
					+"<br/>"
					;
			
//			String msgTxt = "Bonjour,<br/>"
//					+"<br/>"
//					+"Nous avons le plaisir de vous informer que dès à présent vous pouvez récupérer vos documents depuis votre espace client.<br/>"
//					+"<br/>"
//					+"Vous trouverez l'espace client à l'adresse suivante : "+url+".<br/>"
//					+"<br/>"
//					+"Identifiant : "+taEspaceClient.getEmail()+"<br/>"
//					+"<br/>"
//					+"Mot de passe : "+mdpClair+"<br/>"
//					+"<br/>"
//					+"Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.<br/>"
//					+"<br/>"
//					+"Très cordialement,<br/>"
//					+"<br/>"
//					+fournisseur.getNomTiers()
//					+"<br/>"
//					;
			
			String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
			String fromName = "Bureau de Gestion";
			String[][] toEmailandName = new String[][]{ {client.getTaEmail().getAdresseEmail(),client.getNomTiers()}};
			List<String> listeToEmail = new ArrayList<>();
			listeToEmail.add(client.getTaEmail().getAdresseEmail());
			listeToEmail.add("notifications@bdg.cloud");//OSS_REMOVE
			
			//sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//			emailServiceFinder.sendAndLogEmailDossier(
//					null,
//					fromEmail,//null,
//					fromName,
//					subject,
//					msgTxt, 
//					msgTxt, 
//					listeToEmail, 
//					null,//emailTiersDestinataire,
//					null,//contactMessageEmailDestinataire,
//					null,//adressesCc, 
//					null,//emailTiersCc,
//					null,//contactMessageEmailCc,
//					null,//adressesBcc, 
//					null,//emailTiersBcc,
//					null,//contactMessageEmailBcc,
//					null,//attachment,
//					null,//replyTo
//					null//sessionInfo.getUtilisateur()
//					);
			
			emailServiceFinder.sendAndLogEmailProgramme(
					null,//null,
					fromEmail,
					subject,
					msgTxt, 
					msgTxt, 
					listeToEmail,
					null,//emailTiersDestinataire,
					null,//contactMessageEmailDestinataire,
					null,//adressesCc, 
					null,//emailTiersCc,
					null,//contactMessageEmailCc,
					null,//adressesBcc, 
					null,//emailTiersBcc,
					null,//contactMessageEmailBcc,
					null,//attachment,
					null,//replyTo
					null//sessionInfo.getUtilisateur()
					);
			
			//envoie d'une copie à legrain
//			toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//			sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
			//TODO faire avec BCC
		}
	
	public void emailEspaceClientCreer(TaTiers fournisseur, TaEspaceClient taEspaceClient, TaTiers client, String tenant, String alias, String mdpClair){ 
//		Sujet : Activation l'espace client auprès de la société [NomDeLaSociete]
//		Bonjour,
//		Nous avons le plaisir de vous informer que dès à présent vous pouvez récupérer vos documents, comme les factures, depuis l'espace client de notre programme de gestion Bureau de Gestion.
//		Si vous possédez déjà un compte, il vous suffira d'établir une liaison avec notre société en vous rendant sur votre espace client que vous trouverez à l'adresse suivante : https://espaceclient.bdg.cloud
//		Si vous n'avez pas de compte, vous pouvez en créer un en vous rendant à la même adresse : https://espaceclient.bdg.cloud
//		Et ensuite, il vous suffira d'établir une liaison avec notre société.
//		Rappel : pour établir une liaison avec notre société, il vous suffit de vous munir de votre compte client que avez auprès de la société [NomDeLaSociete] afin d'être identifié. Vous recevrez ensuite un email pour valider votre liaison.
//		Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.
//		Très cordialement,
//		[NomDeLaSociete] 
			String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Activation de l'espace client auprès de la société "+fournisseur.getNomTiers();
			
			String url = "https://"+tenant+".espace-client."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE);
			TaParamEspaceClient param = taParamEspaceClientService.findInstance();
			if(param.getUrlPerso()!=null && !param.getUrlPerso().equals("")) {
				url = param.getUrlPerso();
			}
			
			String msgTxt = "Bonjour,<br/>"
					+"<br/>"
					+"Nous avons le plaisir de vous informer que dès à présent vous pouvez récupérer vos documents depuis votre espace client.<br/>"
					+"<br/>"
					+"Vous trouverez l'espace client à l'adresse suivante : "+url+".<br/>"
					+"<br/>"
					+"Identifiant : "+taEspaceClient.getEmail()+"<br/>"
					+"<br/>"
					+"Mot de passe : "+mdpClair+"<br/>"
					+"<br/>"
					+"Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.<br/>"
					+"<br/>"
					+"Très cordialement,<br/>"
					+"<br/>"
					+fournisseur.getNomTiers()
					+"<br/>"
					;
			
			String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
			String fromName = "Bureau de Gestion";
			String[][] toEmailandName = new String[][]{ {client.getTaEmail().getAdresseEmail(),client.getNomTiers()}};
			List<String> listeToEmail = new ArrayList<>();
			listeToEmail.add(client.getTaEmail().getAdresseEmail());
			listeToEmail.add("notifications@bdg.cloud");//OSS_REMOVE
			
			//sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//			emailServiceFinder.sendAndLogEmailDossier(
//					null,
//					fromEmail,//null,
//					fromName,
//					subject,
//					msgTxt, 
//					msgTxt, 
//					listeToEmail, 
//					null,//emailTiersDestinataire,
//					null,//contactMessageEmailDestinataire,
//					null,//adressesCc, 
//					null,//emailTiersCc,
//					null,//contactMessageEmailCc,
//					null,//adressesBcc, 
//					null,//emailTiersBcc,
//					null,//contactMessageEmailBcc,
//					null,//attachment,
//					null,//replyTo
//					null//sessionInfo.getUtilisateur()
//					);
			
			emailServiceFinder.sendAndLogEmailProgramme(
					null,//null,
					fromEmail,
					subject,
					msgTxt, 
					msgTxt, 
					listeToEmail,
					null,//emailTiersDestinataire,
					null,//contactMessageEmailDestinataire,
					null,//adressesCc, 
					null,//emailTiersCc,
					null,//contactMessageEmailCc,
					null,//adressesBcc, 
					null,//emailTiersBcc,
					null,//contactMessageEmailBcc,
					null,//attachment,
					null,//replyTo
					null//sessionInfo.getUtilisateur()
					);
			
			//envoie d'une copie à legrain
//			toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//			sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
			//TODO faire avec BCC
		}
	
	public void emailInformationCollectiveCompteClientDeLaPartDunFournisseur(TaTiers fournisseur, TaTiers client){ 
//		Sujet : Activation l'espace client auprès de la société [NomDeLaSociete]
//		Bonjour,
//		Nous avons le plaisir de vous informer que dès à présent vous pouvez récupérer vos documents, comme les factures, depuis l'espace client de notre programme de gestion Bureau de Gestion.
//		Si vous possédez déjà un compte, il vous suffira d'établir une liaison avec notre société en vous rendant sur votre espace client que vous trouverez à l'adresse suivante : https://espaceclient.bdg.cloud
//		Si vous n'avez pas de compte, vous pouvez en créer un en vous rendant à la même adresse : https://espaceclient.bdg.cloud
//		Et ensuite, il vous suffira d'établir une liaison avec notre société.
//		Rappel : pour établir une liaison avec notre société, il vous suffit de vous munir de votre compte client que avez auprès de la société [NomDeLaSociete] afin d'être identifié. Vous recevrez ensuite un email pour valider votre liaison.
//		Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.
//		Très cordialement,
//		[NomDeLaSociete] 
			String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Activation de l'espace client auprès de la société "+fournisseur.getNomTiers();
			
			String msgTxt = "Bonjour,<br/>"
					+"<br/>"
					+"Nous avons le plaisir de vous informer que dès à présent vous pouvez récupérer vos documents, comme les factures, depuis l'espace client de notre programme de gestion Bureau de Gestion.<br/>"
					+"<br/>"
					+"Si vous possédez déjà un compte, il vous suffira d'établir une liaison avec notre société en vous rendant sur votre espace client que vous trouverez à l'adresse suivante : https://espaceclient.bdg.cloud.<br/>"
					+"<br/>"
					+"Si vous n'avez pas de compte, vous pouvez en créer un en vous rendant à la même adresse : https://espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+".<br/>"
					+"<br/>"
					+"Et ensuite, il vous suffira d'établir une liaison avec notre société.<br/>"
					+"<br/>"
					+"Votre clé secrète à saisir lors de la liaison de votre espace client avec notre société est : "+client.getCleLiaisonCompteClient()+".<br/>"
					+"<br/>"
					+"Rappel : pour établir une liaison avec notre société, il vous suffit de vous munir de votre compte client que avez auprès de la société "+fournisseur.getNomTiers()+" afin d'être identifié. Vous recevrez ensuite un email pour valider votre liaison.<br/>"
					+"<br/>"
					+"Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.<br/>"
					//OSS_REMOVE
					+"<br/>"
					+"Très cordialement,<br/>"
					+"<br/>"
					+fournisseur.getNomTiers()
					+"<br/>"
					;
			
			String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
			String fromName = "Bureau de Gestion";
			String[][] toEmailandName = new String[][]{ {client.getTaEmail().getAdresseEmail(),client.getNomTiers()}};
			List<String> listeToEmail = new ArrayList<>();
			listeToEmail.add(client.getTaEmail().getAdresseEmail());
			listeToEmail.add("notifications@bdg.cloud");//OSS_REMOVE
			
			//sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//			emailServiceFinder.sendAndLogEmailDossier(
//					null,
//					fromEmail,//null,
//					fromName,
//					subject,
//					msgTxt, 
//					msgTxt, 
//					listeToEmail, 
//					null,//emailTiersDestinataire,
//					null,//contactMessageEmailDestinataire,
//					null,//adressesCc, 
//					null,//emailTiersCc,
//					null,//contactMessageEmailCc,
//					null,//adressesBcc, 
//					null,//emailTiersBcc,
//					null,//contactMessageEmailBcc,
//					null,//attachment,
//					null,//replyTo
//					null//sessionInfo.getUtilisateur()
//					);
			
			emailServiceFinder.sendAndLogEmailProgramme(
					null,//null,
					fromEmail,
					subject,
					msgTxt, 
					msgTxt, 
					listeToEmail,
					null,//emailTiersDestinataire,
					null,//contactMessageEmailDestinataire,
					null,//adressesCc, 
					null,//emailTiersCc,
					null,//contactMessageEmailCc,
					null,//adressesBcc, 
					null,//emailTiersBcc,
					null,//contactMessageEmailBcc,
					null,//attachment,
					null,//replyTo
					null//sessionInfo.getUtilisateur()
					);
			
			//envoie d'une copie à legrain
//			toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//			sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
			//TODO faire avec BCC
		}
				
		public void emailInformationClientMiseADispositionDeDocumentsDeLaPartDunFournisseur(TaTiers fournisseur, TaTiers client) { 
//		Sujet : [NomDeLaSociete] Vous avez reçu des documents dans votre espace client
//		Bonjour,
//		Nous vous informons que des nouveaux documents sont à votre diposition dans votre espace client.
//		Pour les consulter et les télécharger, il vous suffit de vous rendre l'adresse suivante muni de vos identifiants de connection : https://espaceclient.bdg.cloud
//		Pour toutes questions relatives au fonctionnement de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.
//		Très cordialement,
//		[NomDeLaSociete] 
			String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Vous avez reçu des documents dans votre espace client.";
			
			String msgTxt = "Bonjour,<br/>"
					+"<br/>"
					+"Nous vous informons que des nouveaux documents sont à votre diposition dans votre espace client.<br/>"
					+"<br/>"
					+"<br/>"
					+"Pour les consulter et les télécharger, il vous suffit de vous rendre l'adresse suivante muni de vos identifiants de connection : https://espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+".<br/>"
					+"<br/>"
					+"Pour toutes questions relatives au fonctionnement de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.<br/>"
					//OSS_REMOVE
					+"<br/>"
					+"Très cordialement,<br/>"
					+"<br/>"
					+fournisseur.getNomTiers()
					+"<br/>"
					;
			
			String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
			String fromName = "Bureau de Gestion";
			String[][] toEmailandName = new String[][]{ {client.getTaEmail().getAdresseEmail(),client.getNomTiers()}};
			List<String> listeToEmail = new ArrayList<>();
			listeToEmail.add(client.getTaEmail().getAdresseEmail());
			listeToEmail.add("notifications@bdg.cloud");//OSS_REMOVE
			
			//sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//			emailServiceFinder.sendAndLogEmailDossier(
//			null,
//			fromEmail,//null,
//			fromName,
//			subject,
//			msgTxt, 
//			msgTxt, 
//			listeToEmail, 
//			null,//emailTiersDestinataire,
//			null,//contactMessageEmailDestinataire,
//			null,//adressesCc, 
//			null,//emailTiersCc,
//			null,//contactMessageEmailCc,
//			null,//adressesBcc, 
//			null,//emailTiersBcc,
//			null,//contactMessageEmailBcc,
//			null,//attachment,
//			null,//replyTo
//			null//sessionInfo.getUtilisateur()
//			);
	
	emailServiceFinder.sendAndLogEmailProgramme(
			null,//null,
			fromEmail,
			subject,
			msgTxt, 
			msgTxt, 
			listeToEmail,
			null,//emailTiersDestinataire,
			null,//contactMessageEmailDestinataire,
			null,//adressesCc, 
			null,//emailTiersCc,
			null,//contactMessageEmailCc,
			null,//adressesBcc, 
			null,//emailTiersBcc,
			null,//contactMessageEmailBcc,
			null,//attachment,
			null,//replyTo
			null//sessionInfo.getUtilisateur()
			);
			
			//envoie d'une copie à legrain
//			toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//			sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
			//TODO faire avec BCC
		}
		
		public void emailConfirmationCreationDossier(TaDossier d, String nomEntreprise, String nomDossier, String emailEntreprise) {
//			String subject = "mon sujet ..";
//			String msgTxt = "mon email ...";
//			String fromEmail = "nicolas@legrain.fr";
//			String fromName = "Nicolas";
			
			String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de la création de votre dossier de gestion";
			
			String utilisateur = nomEntreprise;
			String lienCnx = "http://"+bdgProperties.getProperty(BdgProperties.KEY_PREFIXE_SOUS_DOMAINE,"")+nomDossier+"."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+":"+bdgProperties.getProperty(BdgProperties.KEY_PORT_HTTP,"")+"";
			
			String msgTxt = "Bonjour "+utilisateur+",<br/>"
				+"<br/>"
				+"Félicitation ! Votre dossier Bureau de Gestion a bien été créé.<br/>"
				+"<br/>"
				+"Dès maintenant, vous pouvez vous connecter sur votre Bureau de Gestion à l'adresse suivante :<br/>"
				+"<br/>"
				+""+lienCnx+"<br/>"
				+"<br/>"
				+"Nom d'utilisateur : "+emailEntreprise+"<br/>"
				+"<br/>"
				+"Mot de passe : celui défini lors de la création du dossier.<br/>"
				+"<br/>"
				+"Vous bénéficiez de 45 jours d'utilisation gratuite sans engagement avec tous les modules disponibles. A l'issue de cette période, vous pourrez  choisir les modules dont vous avez besoin, en ajouter ou en supprimer et cela à tout moment.<br/>"
				+"<br/>"
				+"Si vous avez besoin d'aide, contactez le support, soit par téléphone au 05.63.30.31.44, soit par email à support@legrain.fr<br/>"
				//OSS_REMOVE
				+"<br/>"
				+"Pour bénéficier d'une formation, il suffit de contacter notre service commercial au 05.63.30.31.44. Nous étudierons ensemble votre besoin et les prises en charge possible.<br/>"
				//OSS_REMOVE
				+"<br/>"
				+"nous vous souhaitons une bonne utilisation de votre Bureau de Gestion.<br/>"
				+"<br/>"
				+"Très cordialement,<br/>"
				+"<br/>"
				+"Service Gestion <br/>"
				;
			
			
			String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
			String fromName = "Bureau de Gestion";
			String[][] toEmailandName = new String[][]{ {d.getTaClient().getTaUtilisateur().getEmail(),d.getTaClient().getNom()}};
			List<String> listeToEmail = new ArrayList<>();
			listeToEmail.add(d.getTaClient().getTaUtilisateur().getEmail());
			listeToEmail.add("notifications@bdg.cloud");//OSS_REMOVE
			
			//sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//			emailServiceFinder.sendAndLogEmailDossier(
//			null,
//			fromEmail,//null,
//			fromName,
//			subject,
//			msgTxt, 
//			msgTxt, 
//			listeToEmail, 
//			null,//emailTiersDestinataire,
//			null,//contactMessageEmailDestinataire,
//			null,//adressesCc, 
//			null,//emailTiersCc,
//			null,//contactMessageEmailCc,
//			null,//adressesBcc, 
//			null,//emailTiersBcc,
//			null,//contactMessageEmailBcc,
//			null,//attachment,
//			null,//replyTo
//			null//sessionInfo.getUtilisateur()
//			);
	
	emailServiceFinder.sendAndLogEmailProgramme(
			null,//null,
			fromEmail,
			subject,
			msgTxt, 
			msgTxt, 
			listeToEmail,
			null,//emailTiersDestinataire,
			null,//contactMessageEmailDestinataire,
			null,//adressesCc, 
			null,//emailTiersCc,
			null,//contactMessageEmailCc,
			null,//adressesBcc, 
			null,//emailTiersBcc,
			null,//contactMessageEmailBcc,
			null,//attachment,
			null,//replyTo
			null//sessionInfo.getUtilisateur()
			);
			
			//envoie d'une copie à legrain
//			toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//			sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
			//TODO faire avec BCC
		}
	
		//TODO A amélioré (avec des modèles d'emails)
		public void emailDemandeRenseignementArticle(ParamDemandeRenseignement question, TaTiers t, TaArticle a){ 
			
			String subject = "Demande de renseignement sur le produit : "+a.getLibellecArticle();

			String msgTxt = "";
			msgTxt = ""
					//+"\n"
					+(question.getTexteDemande()!=null?question.getTexteDemande():"")+"\n"
					+"\n"
					+"******** Catalogue/Boutique ********"
					+"\n"
					+"Code tiers : "+(question.getCodeTiers()!=null?question.getCodeTiers():"")+"\n"
					+"Code article : "+(question.getCodeArticle()!=null?question.getCodeArticle():"")+"\n"
					+"libellé article : "+(question.getLibelleArticle()!=null?question.getLibelleArticle():"")+"\n"
					+"Email : "+(question.getEmail()!=null?question.getEmail():"")+"\n"
					+"\n"
					;
			
			if(t!=null) {
				msgTxt += "******** Fiche tiers ********"
				+"\n"
				+ "Nom : "+(t.getNomTiers()!=null?t.getNomTiers():"")
				+"\n"
				+"Prenom : "+(t.getPrenomTiers()!=null?t.getPrenomTiers():"")
				+"\n"
				+"Email : "+(t.getTaEmail()!=null?t.getTaEmail().getAdresseEmail():"")
				+"\n"
				+"Téléphone : "+(t.getTaTelephone()!=null?t.getTaTelephone().getNumeroTelephone():"")
				+"\n"
				;
			}

			String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
			String fromName = "Bureau de Gestion";
//			String[][] toEmailandName = new String[][]{ {"serviceclient@legrain.fr","Service client"}};
//			String[][] toEmailandName = new String[][]{ {"nicolas@legrain.fr","Service client"}};
			
			String[][] toEmailandName = new String[][]{ {taInfoEntrepriseService.findInstance().getEmailInfoEntreprise(),""}};
			List<String> listeToEmail = new ArrayList<>();
			listeToEmail.add(taInfoEntrepriseService.findInstance().getEmailInfoEntreprise());
//			listeToEmail.add("notifications@bdg.cloud");
			
			//sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//			emailServiceFinder.sendAndLogEmailDossier(
//					null,
//					fromEmail,//null,
//					fromName,
//					subject,
//					msgTxt, 
//					msgTxt, 
//					listeToEmail, 
//					null,//emailTiersDestinataire,
//					null,//contactMessageEmailDestinataire,
//					null,//adressesCc, 
//					null,//emailTiersCc,
//					null,//contactMessageEmailCc,
//					null,//adressesBcc, 
//					null,//emailTiersBcc,
//					null,//contactMessageEmailBcc,
//					null,//attachment,
//					null,//replyTo
//					null//sessionInfo.getUtilisateur()
//					);
			
			emailServiceFinder.sendAndLogEmailProgramme(
					null,//null,
					fromEmail,
					subject,
					msgTxt, 
					msgTxt, 
					listeToEmail,
					null,//emailTiersDestinataire,
					null,//contactMessageEmailDestinataire,
					null,//adressesCc, 
					null,//emailTiersCc,
					null,//contactMessageEmailCc,
					null,//adressesBcc, 
					null,//emailTiersBcc,
					null,//contactMessageEmailBcc,
					null,//attachment,
					null,//replyTo
					null//sessionInfo.getUtilisateur()
					);
			
			//envoie d'une copie à legrain
//			toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//			sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
			//TODO faire avec BCC
		}
	
		//TODO A amélioré (avec des modèles d'emails)
		public void emailConfirmationCommandeBoutique(TaPanier panier, TaBoncde commande, TaEspaceClient taEspaceClient, TaTiers taTiers, TaReglement taReglement, TaStripePaymentIntent intent){ 
			
			String nomEntreprise = taInfoEntrepriseService.findInstance().getNomInfoEntreprise();
			String subject = "["+nomEntreprise+"] Confirmation de commande : "+commande.getCodeDocument();

			String msgTxt = "";
			msgTxt = ""
					//+"\n"
					//+(question.getTexteDemande()!=null?question.getTexteDemande():"")+"\n"
					+"<br/>"
					+"Bonjour "+taTiers.getNomTiers()+"<br/>"
					+"Nous avons le plaisir de vous informer que votre commande a bien été enregristrée.<br/>"
					+"<br/>"
					+"Commande au Nom de : "+commande.getTaTiers().getNomTiers() +" effectuée le "+LibDate.dateToStringAA(commande.getDateDocument())+".<br/>"
					+"Votre numéro de panier est : "+panier.getCodeDocument() +".<br/>"
					+"Votre numéro de commande est : "+commande.getCodeDocument() +".<br/>"
					;
			
					if(intent!=null) {
						msgTxt += "Votre numéro de règlement internet est : "+intent.getIdExterne() +".<br/>";
					}
					if(taReglement!=null) {
						msgTxt += "Votre numéro de règlement est : "+taReglement.getCodeDocument() +".<br/>";
					}
			
					msgTxt += "Veuillez nous communiquer ce numéro pour toutes demandes."
					+"<br/>"
					+"<br/>"
					+"Legrain Informatique vous remercie de votre confiance. Votre commande sera traitée dans les plus brefs délais."
					+"<br/>"
					+"<br/>"
					+"Cordialement,"
					+"<br/>"
					+"<br/>"
					+"Service client.<br/>"
					;

			String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
			String fromName = "Bureau de Gestion";
//			String[][] toEmailandName = new String[][]{ {"serviceclient@legrain.fr","Service client"}};
//			String[][] toEmailandName = new String[][]{ {"nicolas@legrain.fr","Service client"}};
			
			List<String> listeToEmail = new ArrayList<>();
			List<String> listeBccEmail = new ArrayList<>();
			
			//j'ai mis yann@legrain.fr ici mais a la base c'est nicolas@legrain.fr
			if(lgrEnvironnementServeur.isModeTestEmailProgramme()) {
				String[][] toEmailandName = new String[][]{ {"yann@legrain.fr",""}};
				listeToEmail.add("yann@legrain.fr");
				listeBccEmail.add("yann@legrain.fr");
			} else {
				String[][] toEmailandName = new String[][]{ {taEspaceClient.getEmail(),""}};
				listeToEmail.add(taEspaceClient.getEmail());
				listeBccEmail.add(taInfoEntrepriseService.findInstance().getEmailInfoEntreprise());
			}
			
//			listeToEmail.add("notifications@bdg.cloud");
			
			//sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//			emailServiceFinder.sendAndLogEmailDossier(
//					null,
//					fromEmail,//null,
//					fromName,
//					subject,
//					msgTxt, 
//					msgTxt, 
//					listeToEmail, 
//					null,//emailTiersDestinataire,
//					null,//contactMessageEmailDestinataire,
//					null,//adressesCc, 
//					null,//emailTiersCc,
//					null,//contactMessageEmailCc,
//					null,//adressesBcc, 
//					null,//emailTiersBcc,
//					null,//contactMessageEmailBcc,
//					null,//attachment,
//					null,//replyTo
//					null//sessionInfo.getUtilisateur()
//					);
			
			emailServiceFinder.sendAndLogEmailProgramme(
					null,//null,
					fromEmail,
					subject,
					msgTxt, 
					msgTxt, 
					listeToEmail,
					null,//emailTiersDestinataire,
					null,//contactMessageEmailDestinataire,
					null,//adressesCc, 
					null,//emailTiersCc,
					null,//contactMessageEmailCc,
					listeBccEmail,//adressesBcc, 
					null,//emailTiersBcc,
					null,//contactMessageEmailBcc,
					null,//attachment,
					null,//replyTo
					null//sessionInfo.getUtilisateur()
					);
			
			//envoie d'une copie à legrain
			//toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
			//emailServiceFinder.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
			//TODO faire avec BCC
		}
	
}

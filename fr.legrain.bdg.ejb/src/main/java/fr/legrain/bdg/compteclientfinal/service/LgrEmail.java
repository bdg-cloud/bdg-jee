package fr.legrain.bdg.compteclientfinal.service;

import java.net.URLEncoder;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.rest.model.ParamCompteEspaceClient;
import fr.legrain.bdg.rest.model.ParamDemandeRenseignement;
import fr.legrain.bdg.rest.model.ParamPub;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.mail.LgrEmailUtil;
import fr.legrain.tiers.model.TaTiers;

@Stateless
public class LgrEmail {
	
	private LgrEmailUtil util;
	
	private BdgProperties bdgProperties;
	private String separateurChaineCryptee = ICompteClientSoapMultitenantProxy.separateurChaineCryptee;
	
	@PostConstruct
	private void init() {
		bdgProperties = new BdgProperties();
//		util = new LgrEmailUtil("smtp.legrain.fr",587,true,"nicolas@legrain.fr","pwd37las67"); //STARTTLS
//		util = new LgrEmailUtil("smtp.bdg.cloud",587,true,"noreply@bdg.cloud","aeHaegh2");
		util = new LgrEmailUtil(
				bdgProperties.getProperty(BdgProperties.KEY_SMTP_HOSTNAME),
				LibConversion.stringToInteger(bdgProperties.getProperty(BdgProperties.KEY_SMTP_PORT)),
				LibConversion.StringToBoolean(bdgProperties.getProperty(BdgProperties.KEY_SMTP_SSL)),
				bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN),
				bdgProperties.getProperty(BdgProperties.KEY_SMTP_PASSWORD)
				); //STARTTLS
	}
	
	public void sendEmail(String subject, String msgTxt, String fromEmail, String fromName, String[][] toEmailandName) {
		util.sendEmail(subject,msgTxt,fromEmail,fromName,toEmailandName);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//A faire plus tard		
	public void emailPourLaValidationDeCreationDunCompteClient(ParamCompteEspaceClient utilisateur){ 
//	Sujet : Confirmation de la création de votre compte sur espaceclient.bdg.cloud
//	Bonjour,
//	Nous avons bien reçu votre demande de création d'un compte sur espaceclient.bdg.cloud.
//	Afin de confirmer cette création, merci de bien vouloir cliquer sur le lien ci-dessous pour finaliser l'actvation de votre espace client.
//	Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.
//	Très cordialement,
//	[NomDeLaSociete] 
		
		//String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de la création de votre compte sur espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE);
		String subject = utilisateur.getHostname()+" - Confirmation de la création de votre compte sur "+utilisateur.getHostname();
		
		utilisateur.crypte();
		
		String lienCnx = "";
		String port = utilisateur.getPort()!=null?(!utilisateur.getPort().equals("80")?":"+utilisateur.getPort():""):"";
		try {
			//lienCnx = bdgProperties.url("espaceclient")+"/validation-creation-compte?p="+URLEncoder.encode(utilisateur.getInfoCompteCryptees(), "UTF-8")+"";
			lienCnx = "https://"+utilisateur.getHostname()+port+"/#/validation-creation-compte?p="+URLEncoder.encode(utilisateur.getInfoCompteCryptees(), "UTF-8")+"";
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		String msgTxt = "Bonjour,\n"
				+"\n"
				//+"Nous avons bien reçu votre demande de création d'un compte sur espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" .\n"
				+"Nous avons bien reçu votre demande de création d'un compte sur "+utilisateur.getHostname()+" .\n"
				+"\n"
				+"Afin de confirmer cette création, merci de bien vouloir cliquer sur le lien ci-dessous pour finaliser l'activation de votre espace client.\n"
				+"\n"
				+""+lienCnx+"\n"
				+"\n"
				+"Ce lien sera valide pendant 24 heures seulement.\n"
				+"\n"
				+"Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.\n"
				+"\n"
				+"Email généré automatiquement par le programme Bureau de Gestion.\n"
				+"\n"
				;
		
		String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
		String fromName = "Bureau de Gestion";
		String[][] toEmailandName = new String[][]{ {utilisateur.getEmail(),utilisateur.getNom()}};
		
		sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		
		//envoie d'une copie à legrain
		toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
		sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
	}
	
	//A faire plus tard		
	public void emailPublicite(ParamPub pub, TaTiers t){ 
		
//	      var a = new Publicite(1,"BDG Cloud Standard","assets/images/publicite_lgr/1-bdg-cloud-standard.png");
//	      var b = new Publicite(2,"BDG Cloud Viti","assets/images/publicite_lgr/2-bdg-cloud-viti.png");
//	      var c = new Publicite(3,"BDG Cloud Conserverie","assets/images/publicite_lgr/3-bdg-cloud-conserverie.png");
//	      var d = new Publicite(4,"Epicea","assets/images/publicite_lgr/4-epicea.png");
//	      var e = new Publicite(5,"Site Vitrine","assets/images/publicite_lgr/5-site-vitrine.png");
//	      var f = new Publicite(6,"Site e-commerce","assets/images/publicite_lgr/6-site-e-commerce.png");
//	      var g = new Publicite(7,"Sauvegarde","assets/images/publicite_lgr/7-sauvegarde.png");
//	      var h = new Publicite(8,"Anti virus","assets/images/publicite_lgr/8-anti-virus.png");
		
		String subject = "Demande de renseignement sur le produit : "+pub.getTitre();
		
//		String lienCnx = "";
//		String port = utilisateur.getPort()!=null?(!utilisateur.getPort().equals("80")?":"+utilisateur.getPort():""):"";
//		try {
//			//lienCnx = bdgProperties.url("espaceclient")+"/validation-creation-compte?p="+URLEncoder.encode(utilisateur.getInfoCompteCryptees(), "UTF-8")+"";
//			lienCnx = "https://"+utilisateur.getHostname()+port+"/#/validation-creation-compte?p="+URLEncoder.encode(utilisateur.getInfoCompteCryptees(), "UTF-8")+"";
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		String msgTxt = "";
		msgTxt = "Bonjour,\n"
				+"\n"
				+"Demande de renseignement sur le produit : "+(pub.getTitre()!=null?pub.getTitre():"")+"\n"
				+"\n"
				+"******** espace-client ********"
				+"\n"
				+"Code tiers : "+(pub.getCodeTiers()!=null?pub.getCodeTiers():"")+"\n"
				+"Email : "+(pub.getEmail()!=null?pub.getEmail():"")+"\n"
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
		String[][] toEmailandName = new String[][]{ {"serviceclient@legrain.fr","Service client"}};
//		String[][] toEmailandName = new String[][]{ {"nicolas@legrain.fr","Service client"}};
		
		sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		
		//envoie d'une copie à legrain
//		toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//		sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
	}
	
	
			
//	public void emailPourLaValidationDuneLiaisonAvecUnFournisseur(TaFournisseur fournisseur, TaTiers client){ 
////	Sujet : Confirmation de l'activation de la liaison entre votre espace client et votre compte sur espaceclient.bdg.cloud
////	Bonjour,
////	Vous avez demandé d'établir une liaison avec votre fournisseur [NomDeLaSociété] sur votre compte sur espaceclient.bdg.cloud.
////	Afin de confirmer cette création, merci de bien vouloir cliquer sur le lien ci-dessous pour finaliser cette liaison.
////	Une fois confirmée, vous pourrez récupérer des documents venant de votre fournisseur, comme par exemple des factures, depuis votre espace client disponible à l'adresse espaceclient.bdg.cloud.
////	Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.
////	Très cordialement,
////	[NomDeLaSociete] 
//		
//		String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de l'activation de la liaison entre votre espace client et votre compte sur espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE);
//		
//		String msgTxt = "Bonjour,\n"
//				+"\n"
//				+"Vous avez demandé d'établir une liaison avec votre fournisseur "+fournisseur.getRaisonSocialeFournisseur()+" sur votre compte sur espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" .\n"
//				+"\n"
//				+"Afin de confirmer cette création, merci de bien vouloir cliquer sur le lien ci-dessous pour finaliser cette liaison.\n"
//				+"\n"
//				+"\n"
//				+"\n"
//				+"Une fois confirmée, vous pourrez récupérer des documents venant de votre fournisseur, comme par exemple des factures, depuis votre espace client disponible à l'adresse espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" .\n"
//				+"\n"
//				+"Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.\n"
//				+"\n"
//				+"Très cordialement,\n"
//				+"\n"
//				+fournisseur.getRaisonSocialeFournisseur()
//				+"\n"
//				;
//		
//		String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
//		String fromName = "Bureau de Gestion";
//		String[][] toEmailandName = new String[][]{ {client.getTaEmail().getAdresseEmail(),client.getNomTiers()}};
//		
//		sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//		
//		//envoie d'une copie à legrain
//		toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
//		sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
//	}
}

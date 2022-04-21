package fr.legrain.bdg.compteclient.service;

import java.net.URLEncoder;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;
import fr.legrain.bdg.compteclient.ws.TaTiers;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.lib.mail.LgrEmailUtil;

@Stateless
public class LgrEmail {
	
	private LgrEmailUtil util;
	
	private BdgProperties bdgProperties;
	private String separateurChaineCryptee = "~";
	
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
	public void emailPourLaValidationDeCreationDunCompteClient(TaUtilisateur utilisateur){ 
//	Sujet : Confirmation de la création de votre compte sur espaceclient.bdg.cloud
//	Bonjour,
//	Nous avons bien reçu votre demande de création d'un compte sur espaceclient.bdg.cloud.
//	Afin de confirmer cette création, merci de bien vouloir cliquer sur le lien ci-dessous pour finaliser l'actvation de votre espace client.
//	Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.
//	Très cordialement,
//	[NomDeLaSociete] 
		
		String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de la création de votre compte sur espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE);
		
		String a = utilisateur.getNom()+separateurChaineCryptee+utilisateur.getPrenom()+separateurChaineCryptee+utilisateur.getEmail()+separateurChaineCryptee+utilisateur.getPasswd();
		String b = LibCrypto.encrypt(a);
		
		String lienCnx = "";
		try {
			lienCnx = bdgProperties.url("espaceclient")+"/validation_creation_compte.xhtml?p="+URLEncoder.encode(b, "UTF-8")+"";
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		String msgTxt = "Bonjour,\n"
				+"\n"
				+"Nous avons bien reçu votre demande de création d'un compte sur espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" .\n"
				+"\n"
				+"Afin de confirmer cette création, merci de bien vouloir cliquer sur le lien ci-dessous pour finaliser l'activation de votre espace client.\n"
				+"\n"
				+""+lienCnx+"\n"
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
			
	public void emailPourLaValidationDuneLiaisonAvecUnFournisseur(TaFournisseur fournisseur, TaTiers client){ 
//	Sujet : Confirmation de l'activation de la liaison entre votre espace client et votre compte sur espaceclient.bdg.cloud
//	Bonjour,
//	Vous avez demandé d'établir une liaison avec votre fournisseur [NomDeLaSociété] sur votre compte sur espaceclient.bdg.cloud.
//	Afin de confirmer cette création, merci de bien vouloir cliquer sur le lien ci-dessous pour finaliser cette liaison.
//	Une fois confirmée, vous pourrez récupérer des documents venant de votre fournisseur, comme par exemple des factures, depuis votre espace client disponible à l'adresse espaceclient.bdg.cloud.
//	Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.
//	Très cordialement,
//	[NomDeLaSociete] 
		
		String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de l'activation de la liaison entre votre espace client et votre compte sur espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE);
		
		String msgTxt = "Bonjour,\n"
				+"\n"
				+"Vous avez demandé d'établir une liaison avec votre fournisseur "+fournisseur.getRaisonSocialeFournisseur()+" sur votre compte sur espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" .\n"
				+"\n"
				+"Afin de confirmer cette création, merci de bien vouloir cliquer sur le lien ci-dessous pour finaliser cette liaison.\n"
				+"\n"
				+"\n"
				+"\n"
				+"Une fois confirmée, vous pourrez récupérer des documents venant de votre fournisseur, comme par exemple des factures, depuis votre espace client disponible à l'adresse espaceclient."+bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" .\n"
				+"\n"
				+"Pour toutes questions sur l'activation de votre espace client, vous pouvez contacter l'éditeur du logiciel Legrain Informatique au 05.63.30.31.44.\n"
				+"\n"
				+"Très cordialement,\n"
				+"\n"
				+fournisseur.getRaisonSocialeFournisseur()
				+"\n"
				;
		
		String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
		String fromName = "Bureau de Gestion";
		String[][] toEmailandName = new String[][]{ {client.getTaEmail().getAdresseEmail(),client.getNomTiers()}};
		
		sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		
		//envoie d'une copie à legrain
		toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
		sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
	}
}

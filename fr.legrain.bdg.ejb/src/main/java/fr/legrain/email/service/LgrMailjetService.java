package fr.legrain.email.service;

import java.io.File;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;

import fr.legrain.bdg.email.service.remote.IEnvoiEmailViaServiceExterneDossierService;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
//import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.lib.mail.LgrMailjet;
import fr.legrain.lib.mail.ReponseMailjetEmailInformationService;
import fr.legrain.paiement.service.LgrEnvironnementServeur;
import fr.legrain.lib.mail.ReponseEmailSendService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;

@Stateless
@EmailLgr(EmailLgr.TYPE_MAILJET_PROGRAMME_BDG)
public class LgrMailjetService extends EmailService implements IEnvoiEmailViaServiceExterneDossierService {
	
	private String MJ_APIKEY_PUBLIC = null;
	private String MJ_APIKEY_PRIVATE = null;
	
	private @EJB ITaParametreServiceRemote taParametreService;
	private @Inject ParamBdg paramBdg;
	
	//private LgrMailjet lgrMailjet = new LgrMailjet(MJ_APIKEY_PUBLIC,MJ_APIKEY_PRIVATE);
	private LgrMailjet lgrMailjet = null;
	private BdgProperties bdgProperties = null;

	@EJB private LgrEnvironnementServeur lgrEnvironnementServeur;
	
	public ReponseEmailSendService send(String fromEmail, String fromName, String subject, String textPart, String htmlPart, String[] destinataires, String[] cc, String[] bcc, File[] attachment, String replyToEmail) {

		bdgProperties = new BdgProperties();
		//SchemaResolver sr = new SchemaResolver();
		taParametreService.initParamBdg();
		
		MJ_APIKEY_PUBLIC = paramBdg.getTaParametre().getMailjetApikeyPublicDefautProgramme();
		MJ_APIKEY_PRIVATE = paramBdg.getTaParametre().getMailjetApikeyPrivateDefautProgramme();
		lgrMailjet = new LgrMailjet(MJ_APIKEY_PUBLIC,MJ_APIKEY_PRIVATE);
		try {
			//if(fromEmail==null) {
//				fromEmail = "legrain@legrain.biz";
				fromEmail = lgrEnvironnementServeur.emailDefautServeur();
				
//				String ip = bdgProperties.getProperty("ip");
//				if(ip==null || ip.equals("") ||  ip.equals("127.0.0.1")) {
//					fromEmail = "legrain@legrain.biz";
//				} else if(ip.equals("92.243.1.160")) {
//					//serveur de test DEV-internet
//					fromEmail = "noreply@devbdg.work";
//				} else if(ip.equals("46.226.106.155")) {
//					//serveur de test PPROD-internet
//					fromEmail = "noreply@pprodbdg.work";
//				} else if(ip.equals("46.226.106.155")) {
//					//serveur de test PPROD-internet
//				} else if(ip.equals("46.226.106.152")) {
//					//serveur de PROD internet
//					fromEmail = "noreply@bdg.cloud";
//				} else if(ip.equals("46.226.106.155")) {
//					//serveur de test PPROD-internet
//				}
				
				//String localPath = bdgProperties.osTempDirDossier(sr.resolveCurrentTenantIdentifier())+"/"+bdgProperties.tmpFileName("facture.pdf");
			//}
			return lgrMailjet.send_v3_1(fromEmail, fromName, subject, textPart, htmlPart, destinataires, cc, bcc, attachment, replyToEmail);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TaMessageEmail updateServiceInformation(TaMessageEmail taMessageEmail) {
		taMessageEmail.setTypeServiceExterne(EmailLgr.TYPE_MAILJET_PROGRAMME_BDG);
		return taMessageEmail;
		
	}

	@Override
	public ReponseEmailSendService send(TaCompteServiceWebExterne configService, String fromEmail, String fromName,
			String subject, String textPart, String htmlPart, String[] destinataires, String[] cc, String[] bcc,
			File[] attachment, String replyToEmail) throws EJBException {
		
		this.configService = configService;
		return send(fromEmail, fromName, subject, textPart, htmlPart, destinataires, cc, bcc, attachment, replyToEmail);
	}

	@Override
	public TaMessageEmail updateInfosMessageExterne(TaMessageEmail taMessageEmail, TaCompteServiceWebExterne configService) {
		if(taMessageEmail.getMessageID()!=null) {
			ReponseMailjetEmailInformationService r = lgrMailjet.findMessageInformation(Long.parseLong(taMessageEmail.getMessageID()));
			if(r!=null) {
				taMessageEmail.setStatusServiceExterne(r.getStatus());
				taMessageEmail.setEtatMessageServiceExterne(r.getJson());
				taMessageEmailService.merge(taMessageEmail);
			}
		}
		return taMessageEmail;
	}
	
	

}

package fr.legrain.email.service;

import java.io.File;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.email.service.remote.IEnvoiEmailViaServiceExterneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.lib.mail.LgrMailjet;
import fr.legrain.lib.mail.ReponseMailjetEmailInformationService;
import fr.legrain.lib.mail.ReponseEmailSendService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;

@Stateless
@EmailLgr(EmailLgr.TYPE_MAILJET_DOSSIER)
public class EnvoiEmailViaMailjetDossierService extends EmailService implements IEnvoiEmailViaServiceExterneDossierService {
	
	private LgrMailjet lgrMailjet = null;
	
	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	
	public ReponseEmailSendService send(TaCompteServiceWebExterne configService, 
			String fromEmail, String fromName, String subject, String textPart, String htmlPart, 
			String[] destinataires, String[] cc, String[] bcc, File[] attachment, String replyToEmail) throws EJBException {
		
		this.configService = configService;
		return send(fromEmail, fromName, subject, textPart, htmlPart, destinataires, cc, bcc, attachment, replyToEmail);
		
	}
	
	public ReponseEmailSendService send(String fromEmail, String fromName, String subject, String textPart, String htmlPart, 
			String[] destinataires, String[] cc, String[] bcc, File[] attachment, String replyToEmail) {
		
		try {
			if(configService==null) {
				configService =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_EMAIL);
			}
			if(configService!=null) { 
				UtilServiceWebExterne util = new UtilServiceWebExterne();
				configService = util.decrypter(configService);
				
				System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
				System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
				
				//boolean modeTest = true;
	
				//String API_KEY_DOSSIER = configService.getApiKey1();
				
				lgrMailjet = new LgrMailjet(configService.getApiKey1(), configService.getApiKey2());
					
				return lgrMailjet.send_v3_1(fromEmail, fromName, subject, textPart, htmlPart, destinataires, cc, bcc, attachment, replyToEmail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TaMessageEmail updateServiceInformation(TaMessageEmail taMessageEmail) {
		taMessageEmail.setTypeServiceExterne(EmailLgr.TYPE_MAILJET_DOSSIER);
		if(configService!=null) {
			taMessageEmail.setCodeCompteServiceWebExterne(configService.getCodeCompteServiceWebExterne());
		}
		return taMessageEmail;
	}
	
	@Override
	public TaMessageEmail updateInfosMessageExterne(TaMessageEmail taMessageEmail, TaCompteServiceWebExterne configService) {
		if(taMessageEmail.getMessageID()!=null) {
			
			if(configService!=null) { 
				UtilServiceWebExterne util = new UtilServiceWebExterne();
				configService = util.decrypter(configService);
				
				System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
				System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
	
				lgrMailjet = new LgrMailjet(configService.getApiKey1(), configService.getApiKey2());
			}
			ReponseMailjetEmailInformationService r = lgrMailjet.findMessageInformation(Long.parseLong(taMessageEmail.getMessageID()));
			if(r!=null) {
				taMessageEmail.setStatusServiceExterne(r.getStatus());
				taMessageEmail.setEtatMessageServiceExterne(r.getJson());
				taMessageEmail = taMessageEmailService.merge(taMessageEmail);
			}
		}
		return taMessageEmail;
	}
	
}

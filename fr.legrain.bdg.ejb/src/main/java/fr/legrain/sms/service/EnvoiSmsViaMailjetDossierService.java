package fr.legrain.sms.service;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.sms.service.remote.IEnvoiSMSViaServiceExterneDossierService;
import fr.legrain.lib.mail.LgrMailjet;
import fr.legrain.lib.mail.ReponseMailjetEmailInformationService;
import fr.legrain.lib.mail.ReponseMailjetSmsInformationService;
import fr.legrain.lib.mail.ReponseSmsSendService;
import fr.legrain.lib.mail.ReponseEmailSendService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.sms.model.TaMessageSMS;

@Stateless
@SmsLgr(SmsLgr.TYPE_SMS_MAILJET)
public class EnvoiSmsViaMailjetDossierService extends SmsService implements IEnvoiSMSViaServiceExterneDossierService {
	
	private LgrMailjet lgrMailjet = null;
	
	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	
	public ReponseSmsSendService send(TaCompteServiceWebExterne configService, 
			String fromNumber, String fromName, String textPart, String[] destinataires) throws EJBException {
		
		this.configService = configService;
		return send(fromNumber, fromName,  textPart, destinataires);
	}
	
	public ReponseSmsSendService send(String fromNumber, String fromName, String textPart,String[] destinataires) {
		
		try {
			if(configService==null) {
				configService =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_SMS);
			}
			if(configService!=null) { 
				UtilServiceWebExterne util = new UtilServiceWebExterne();
				configService = util.decrypter(configService);
				
				System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
				System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
				
				//boolean modeTest = true;
	
				//String API_KEY_DOSSIER = configService.getApiKey1();
				
				lgrMailjet = new LgrMailjet(configService.getApiKey1());
					
				return lgrMailjet.sendSMS(fromNumber, fromName, textPart, destinataires);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TaMessageSMS updateServiceInformation(TaMessageSMS taMessageSMS) {
		taMessageSMS.setTypeServiceExterne(SmsLgr.TYPE_SMS_MAILJET);
		if(configService!=null) {
			taMessageSMS.setCodeCompteServiceWebExterne(configService.getCodeCompteServiceWebExterne());
		}
		return taMessageSMS;
	}
	
	@Override
	public TaMessageSMS updateInfosMessageExterne(TaMessageSMS taMessageSMS, TaCompteServiceWebExterne configService) {
		if(taMessageSMS.getMessageID()!=null) {
			
			if(configService!=null) { 
				UtilServiceWebExterne util = new UtilServiceWebExterne();
				configService = util.decrypter(configService);
				
				System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
				System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
	
				lgrMailjet = new LgrMailjet(configService.getApiKey1());
			}
			ReponseMailjetSmsInformationService r = lgrMailjet.findMessageSMSInformation(taMessageSMS.getMessageID());
			if(r!=null) {
				taMessageSMS.setStatusServiceExterne(r.getStatus());
				taMessageSMS.setEtatMessageServiceExterne(r.getJson());
				taMessageSMS = taMessageSMSService.merge(taMessageSMS);
			}
		}
		return taMessageSMS;
	}
	
}

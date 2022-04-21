package fr.legrain.bdg.sms.service.remote;

import javax.ejb.EJBException;

import fr.legrain.lib.mail.ReponseEmailSendService;
import fr.legrain.lib.mail.ReponseSmsSendService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.sms.model.TaMessageSMS;


public interface IEnvoiSMSViaServiceExterneDossierService extends IEnvoiSMSService {
	
	public ReponseSmsSendService send(TaCompteServiceWebExterne configService, 
			String fromNumber, String fromName, String textPart, 
			String[] destinataires )
			throws EJBException;

	public TaMessageSMS updateInfosMessageExterne(TaMessageSMS taMessageSMS, TaCompteServiceWebExterne configService);

}
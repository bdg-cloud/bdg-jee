package fr.legrain.bdg.email.service.remote;

import java.io.File;

import javax.ejb.EJBException;

import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.lib.mail.ReponseEmailSendService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;


public interface IEnvoiEmailViaServiceExterneDossierService extends IEnvoiEmailService {
	
	public ReponseEmailSendService send(TaCompteServiceWebExterne configService, 
			String fromEmail, String fromName, String subject, String textPart, String htmlPart, 
			String[] destinataires, String[] cc, String[] bcc, File[] attachment, String replyToEmail)
			throws EJBException;

	public TaMessageEmail updateInfosMessageExterne(TaMessageEmail taMessageEmail, TaCompteServiceWebExterne configService);

}
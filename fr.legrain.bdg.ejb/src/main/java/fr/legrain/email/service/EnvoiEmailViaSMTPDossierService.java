package fr.legrain.email.service;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.email.service.remote.IEnvoiEmailViaServiceExterneDossierService;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.lib.mail.LgrEmailUtil;
import fr.legrain.lib.mail.ReponseEmailSendService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;

@Stateless
@EmailLgr(EmailLgr.TYPE_SMTP_DOSSIER)
public class EnvoiEmailViaSMTPDossierService extends EmailService implements IEnvoiEmailViaServiceExterneDossierService {
	
	private LgrEmailUtil util;
	
	@PostConstruct
	private void init() {
		//Boolean utiliseServiceWebExterne = taPreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.UTILISE_SERVICE_WEB_EXTERNE_POUR_EMAIL);
		String host = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.EMAIL_SMTP_HOST);
		String port = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.EMAIL_SMTP_PORT);
		Boolean ssl = taPreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.EMAIL_SMTP_SSL);
		String login = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.EMAIL_SMTP_LOGIN);
		String password = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.EMAIL_SMTP_PASSWORD);
		
		util = new LgrEmailUtil(
				host,
				LibConversion.stringToInteger(port),
				ssl,
				login,
				LibCrypto.decrypt(password)
			); //STARTTLS
	}
	
	public ReponseEmailSendService send(TaCompteServiceWebExterne configService,
			String fromEmail, String fromName, String subject, String textPart, String htmlPart, 
			String[] destinataires, String[] cc, String[] bcc, File[] attachment, String replyToEmail) throws EJBException {
		
		return util.send(fromEmail, fromName, subject, textPart, htmlPart, destinataires, cc, bcc, attachment, replyToEmail);
		//util.sendEmailAttachement(subject,textPart,fromEmail,fromName,destinataires,null,null,null,null);
		
	}

	@Override
	public ReponseEmailSendService send(String fromEmail, String fromName, String subject, String textPart, String htmlPart,
			String[] destinataires, String[] cc, String[] bcc, File[] attachment, String replyToEmail) {
		return util.send(fromEmail, fromName, subject, textPart, htmlPart, destinataires, cc, bcc, attachment, replyToEmail);
	}

	@Override
	public TaMessageEmail updateServiceInformation(TaMessageEmail taMessageEmail) {
		taMessageEmail.setTypeServiceExterne(EmailLgr.TYPE_SMTP_DOSSIER);
		return taMessageEmail;
		
	}

	@Override
	public TaMessageEmail updateInfosMessageExterne(TaMessageEmail taMessageEmail, TaCompteServiceWebExterne configService) {
		return taMessageEmail;
	}
	
	
}

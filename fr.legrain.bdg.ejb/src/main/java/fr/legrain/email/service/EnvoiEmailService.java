package fr.legrain.email.service;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.email.service.remote.IEnvoiEmailViaServiceExterneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.tiers.model.TaEmail;

@Stateless
public class EnvoiEmailService {
	
	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	@EJB private ITaPreferencesServiceRemote taPreferencesService;
	@Inject @Any private Instance<IEnvoiEmailViaServiceExterneDossierService> iEnvoiEmailViaServiceExterneDossierService;
	
//	public void sendAndLog(TaCompteServiceWebExterne compte,String fromEmail, String fromName, String subject, String textPart, String htmlPart, 
//			List<String> adressesDestinataire, List<TaEmail> emailTiersDestinataire, List<TaContactMessageEmail> contactMessageEmailDestinataire,
//			List<String> adressesCc, List<TaEmail> emailTiersCc, List<TaContactMessageEmail> contactMessageEmailCc,
//			List<String> adressesBcc, List<TaEmail> emailTiersBcc, List<TaContactMessageEmail> contactMessageEmailBcc,
//			File[] attachment, String replyToEmail,
//			TaUtilisateur taUtilisateur
//			) {
//		
//		IEnvoiEmailViaServiceExterneDossierService s = findEmailServiceDossier(compte, fromEmail, fromName, subject, textPart, htmlPart, adressesDestinataire, emailTiersDestinataire, contactMessageEmailDestinataire,
//				adressesCc, emailTiersCc, contactMessageEmailCc, adressesBcc, emailTiersBcc, contactMessageEmailBcc, attachment, replyToEmail, taUtilisateur);
//	
//		s.sendAndLog(compte, fromEmail, fromName, subject, textPart, htmlPart, adressesDestinataire, emailTiersDestinataire, contactMessageEmailDestinataire,
//				adressesCc, emailTiersCc, contactMessageEmailCc, adressesBcc, emailTiersBcc, contactMessageEmailBcc, attachment, replyToEmail, taUtilisateur);
//	}
	
	//public IEnvoiEmailViaServiceExterneDossierService findEmailServiceDossier(TaCompteServiceWebExterne compte) {
	public IEnvoiEmailViaServiceExterneDossierService sendAndLogEmailDossier(TaCompteServiceWebExterne compte,String fromEmail, String fromName, String subject, String textPart, String htmlPart, 
			List<String> adressesDestinataire, List<TaEmail> emailTiersDestinataire, List<TaContactMessageEmail> contactMessageEmailDestinataire,
			List<String> adressesCc, List<TaEmail> emailTiersCc, List<TaContactMessageEmail> contactMessageEmailCc,
			List<String> adressesBcc, List<TaEmail> emailTiersBcc, List<TaContactMessageEmail> contactMessageEmailBcc,
			File[] attachment, String replyToEmail,
			TaUtilisateur taUtilisateur) {
		if(compte==null) {
			compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_EMAIL);
		}
		Boolean utiliseServiceWebExterne = taPreferencesService.retourPreferenceBoolean(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.UTILISE_SERVICE_WEB_EXTERNE_POUR_EMAIL);
		String hostSMTP = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.EMAIL_SMTP_HOST);
		String implementation = "";
		
		String adresseExpediteurDefaut = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.EMAIL_ADRESSE_EXPEDITEUR_DEFAUT);
		String nomExpediteurDefaut = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.EMAIL_NOM_EXPEDITEUR_DEFAUT);
		String repondreADefaut = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_EMAIL, ITaPreferencesServiceRemote.EMAIL_REPONDRE_A_DEFAUT);

		replyToEmail = repondreADefaut;
		
		boolean compteExterneDossier = false;
		if(utiliseServiceWebExterne) {
			//l'utilisateur souhaite utilisé son propre service d'envoi d'email
			if(compte!=null) {
				switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
				case UtilServiceWebExterne.CODE_SWE_MAILJET:
					implementation = EmailLgr.TYPE_MAILJET_DOSSIER;
					compteExterneDossier = true;
					break;
//				case UtilServiceWebExterne.CODE_SWE_MAIL_CHIMP:
//					implementation = EmailLgr.TYPE_MAIL_CHIMP;
//					break;
				default:
					break;
				}
				fromEmail = adresseExpediteurDefaut;
				fromName = nomExpediteurDefaut;
			} else {
				implementation = EmailLgr.TYPE_MAILJET_PAR_DEFAUT_BDG;
			}
		} else if(hostSMTP!=null && !hostSMTP.equals("")) {
			//l'utilisateur souhaite de son propre serveur SMTP renseigné dans les préférences du dossier
			implementation = EmailLgr.TYPE_SMTP_DOSSIER;
			fromEmail = adresseExpediteurDefaut;
			fromName = nomExpediteurDefaut;
		} else {
			implementation = EmailLgr.TYPE_MAILJET_PAR_DEFAUT_BDG;
			//utilisation du service d'envoi de mail de BDG
			//clé mailjet par defaut pour tout les dossier tant que pas de compte mailjet propre au dossier ou de serveru SMTP propore au dossier
		}
		// else
		// utilisation d'un serveur SMTP "BDG"
		
		
		IEnvoiEmailViaServiceExterneDossierService service = iEnvoiEmailViaServiceExterneDossierService.select(new MyQualifierEmailLiteral(implementation)).get();
		
		if(compteExterneDossier) {
			service.sendAndLog(compte, fromEmail, fromName, subject, textPart, htmlPart, adressesDestinataire, emailTiersDestinataire, contactMessageEmailDestinataire,
					adressesCc, emailTiersCc, contactMessageEmailCc, adressesBcc, emailTiersBcc, contactMessageEmailBcc, attachment, replyToEmail, taUtilisateur);
		} else {
			service.sendAndLog(fromEmail, fromName, subject, textPart, htmlPart, adressesDestinataire, emailTiersDestinataire, contactMessageEmailDestinataire,
					adressesCc, emailTiersCc, contactMessageEmailCc, adressesBcc, emailTiersBcc, contactMessageEmailBcc, attachment, replyToEmail, taUtilisateur);
		}
		
		return service;
	}
	
	public IEnvoiEmailViaServiceExterneDossierService sendAndLogEmailProgramme(String fromEmail, String fromName, String subject, String textPart, String htmlPart, 
			List<String> adressesDestinataire, List<TaEmail> emailTiersDestinataire, List<TaContactMessageEmail> contactMessageEmailDestinataire,
			List<String> adressesCc, List<TaEmail> emailTiersCc, List<TaContactMessageEmail> contactMessageEmailCc,
			List<String> adressesBcc, List<TaEmail> emailTiersBcc, List<TaContactMessageEmail> contactMessageEmailBcc,
			File[] attachment, String replyToEmail,
			TaUtilisateur taUtilisateur) {
		
		Boolean modeSMTP = false;
		String implementation = "";
		
		boolean compteExterneDossier = false;
		if(modeSMTP) {
			implementation = EmailLgr.TYPE_SMTP_PROGRAMME_BDG;

		} else {
			implementation = EmailLgr.TYPE_MAILJET_PROGRAMME_BDG;
		}
		
		IEnvoiEmailViaServiceExterneDossierService service = iEnvoiEmailViaServiceExterneDossierService.select(new MyQualifierEmailLiteral(implementation)).get();
		
		service.sendAndLog(fromEmail, fromName, subject, textPart, htmlPart, adressesDestinataire, emailTiersDestinataire, contactMessageEmailDestinataire,
				adressesCc, emailTiersCc, contactMessageEmailCc, adressesBcc, emailTiersBcc, contactMessageEmailBcc, attachment, replyToEmail, taUtilisateur);
		
		
		return service;
	}
	
	public TaMessageEmail updateInfosMessageExterne(TaMessageEmail taMessageEmail) {
		try {
			if(taMessageEmail!=null && taMessageEmail.getTypeServiceExterne()!=null) {
				String implementation = null;
				TaCompteServiceWebExterne taCompteServiceWebExterne = null;
				if(taMessageEmail.getTypeServiceExterne().equals(EmailLgr.TYPE_MAILJET_PAR_DEFAUT_BDG)) {
					implementation = EmailLgr.TYPE_MAILJET_PAR_DEFAUT_BDG;
				} else if(taMessageEmail.getTypeServiceExterne().equals(EmailLgr.TYPE_MAILJET_DOSSIER)) {
					implementation = EmailLgr.TYPE_MAILJET_DOSSIER;
					if(taMessageEmail.getCodeCompteServiceWebExterne()!=null) {
						taCompteServiceWebExterne = taCompteServiceWebExterneService.findByCode(taMessageEmail.getCodeCompteServiceWebExterne());
					}
				} else if(taMessageEmail.getTypeServiceExterne().equals(EmailLgr.TYPE_MAILJET_PROGRAMME_BDG)) {
					implementation = EmailLgr.TYPE_MAILJET_PROGRAMME_BDG;
				}
				
				if(implementation!=null) {
					IEnvoiEmailViaServiceExterneDossierService service = iEnvoiEmailViaServiceExterneDossierService.select(new MyQualifierEmailLiteral(implementation)).get();
					return service.updateInfosMessageExterne(taMessageEmail, taCompteServiceWebExterne);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taMessageEmail;
	}

}

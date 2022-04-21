package fr.legrain.sms.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.sms.service.remote.IEnvoiSMSViaServiceExterneDossierService;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.sms.model.TaContactMessageSMS;
import fr.legrain.sms.model.TaMessageSMS;
import fr.legrain.tiers.model.TaTelephone;

@Stateless
public class EnvoiSmsService {
	
	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	@EJB private ITaPreferencesServiceRemote taPreferencesService;
	@Inject @Any private Instance<IEnvoiSMSViaServiceExterneDossierService> iEnvoiSMSViaServiceExterneDossierService;
	
	public IEnvoiSMSViaServiceExterneDossierService sendAndLogSMSDossier(TaCompteServiceWebExterne compte,String fromNumber, String fromName, 
			 String textPart, 
			List<String> numerosDestinataire, List<TaTelephone> numeroTiersDestinataire, List<TaContactMessageSMS> contactMessageSMSDestinataire,

			TaUtilisateur taUtilisateur) {
		if(compte==null) {
			compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_SMS);
		}
		String implementation = "";
		
		String nomExpediteurDefaut = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_SMS, ITaPreferencesServiceRemote.SMS_NOM_EXPEDITEUR_DEFAUT);
		String numeroExpediteurDefaut = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_SMS, ITaPreferencesServiceRemote.SMS_NUMERO_EXPEDITEUR_DEFAUT);

//		String replyToEmail = repondreADefaut;
		
		

		if(compte!=null) {
			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
			case UtilServiceWebExterne.CODE_SWE_MAILJET_SMS:
				implementation = SmsLgr.TYPE_SMS_MAILJET;
				break;
			case UtilServiceWebExterne.CODE_SWE_OVH_SMS:
				implementation = SmsLgr.TYPE_SMS_OVH;
				break;
			default:
				break;
			}
			fromNumber = numeroExpediteurDefaut;
			fromName = nomExpediteurDefaut;
		} 
		
		
		
		IEnvoiSMSViaServiceExterneDossierService service = iEnvoiSMSViaServiceExterneDossierService.select(new MyQualifierSmsLiteral(implementation)).get();
		
		
		service.sendAndLog(compte, fromNumber, fromName, textPart,  numerosDestinataire, numeroTiersDestinataire, contactMessageSMSDestinataire,
					 taUtilisateur);
		
		
		return service;
	}
	
	public IEnvoiSMSViaServiceExterneDossierService sendAndLogSMSProgramme(String fromNumber, String fromName, String textPart,  
			List<String> numerosDestinataire, List<TaTelephone> numeroTiersDestinataire, List<TaContactMessageSMS> contactMessageSMSDestinataire,

			TaUtilisateur taUtilisateur) {
		
		Boolean modeSMTP = false;
		String implementation = "";
		
		boolean compteExterneDossier = false;
		
		implementation = SmsLgr.TYPE_SMS_MAILJET_PROGRAMME;
		
		
		IEnvoiSMSViaServiceExterneDossierService service = iEnvoiSMSViaServiceExterneDossierService.select(new MyQualifierSmsLiteral(implementation)).get();
		
		service.sendAndLog(fromNumber, fromName,  textPart,  numerosDestinataire, numeroTiersDestinataire, contactMessageSMSDestinataire,
				 taUtilisateur);
		
		
		return service;
	}
	
	public TaMessageSMS updateInfosMessageExterne(TaMessageSMS taMessageSMS) {
		try {
			if(taMessageSMS!=null && taMessageSMS.getTypeServiceExterne()!=null) {
				String implementation = null;
				TaCompteServiceWebExterne taCompteServiceWebExterne = null;
				if(taMessageSMS.getTypeServiceExterne().equals(SmsLgr.TYPE_SMS_MAILJET_PROGRAMME)) {
					implementation = SmsLgr.TYPE_SMS_MAILJET_PROGRAMME;
				} else if(taMessageSMS.getTypeServiceExterne().equals(SmsLgr.TYPE_SMS_OVH_PROGRAMME)) {
						implementation = SmsLgr.TYPE_SMS_OVH_PROGRAMME;
				} else if(taMessageSMS.getTypeServiceExterne().equals(SmsLgr.TYPE_SMS_MAILJET)) {
					implementation = SmsLgr.TYPE_SMS_MAILJET;
					if(taMessageSMS.getCodeCompteServiceWebExterne()!=null) {
						taCompteServiceWebExterne = taCompteServiceWebExterneService.findByCode(taMessageSMS.getCodeCompteServiceWebExterne());
					}
				} else if(taMessageSMS.getTypeServiceExterne().equals(SmsLgr.TYPE_SMS_OVH)) {
					implementation = SmsLgr.TYPE_SMS_OVH;
					if(taMessageSMS.getCodeCompteServiceWebExterne()!=null) {
						taCompteServiceWebExterne = taCompteServiceWebExterneService.findByCode(taMessageSMS.getCodeCompteServiceWebExterne());
					}
				}
				
				if(implementation!=null) {
					IEnvoiSMSViaServiceExterneDossierService service = iEnvoiSMSViaServiceExterneDossierService.select(new MyQualifierSmsLiteral(implementation)).get();
					return service.updateInfosMessageExterne(taMessageSMS, taCompteServiceWebExterne);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taMessageSMS;
	}

}

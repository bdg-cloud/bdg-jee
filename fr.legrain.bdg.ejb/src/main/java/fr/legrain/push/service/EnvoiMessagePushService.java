package fr.legrain.push.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.push.service.remote.IEnvoiMessagePushViaServiceExterneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.push.model.TaContactMessagePush;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.sms.model.TaContactMessageSMS;
import fr.legrain.tiers.model.TaTelephone;

@Stateless
public class EnvoiMessagePushService {
	
	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	@EJB private ITaPreferencesServiceRemote taPreferencesService;
	@Inject @Any private Instance<IEnvoiMessagePushViaServiceExterneDossierService> iEnvoiPushViaServiceExterneDossierService;
	
	public IEnvoiMessagePushViaServiceExterneDossierService sendAndLogMessagePushDossier(TaCompteServiceWebExterne compte,String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaContactMessagePush> contactMessagePushDestinataire,

			TaUtilisateur taUtilisateur) {
		if(compte==null) {
			compte =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_SMS);
		}
		String implementation = MessagePushLgr.TYPE_MESSAGE_PUSH_FIREBASE; //une seule implémentation (par defaut) pour l'instant 
		
//		String nomExpediteurDefaut = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_SMS, ITaPreferencesServiceRemote.SMS_NOM_EXPEDITEUR_DEFAUT);
//		String numeroExpediteurDefaut = taPreferencesService.retourPreferenceString(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_ENVOI_SMS, ITaPreferencesServiceRemote.SMS_NUMERO_EXPEDITEUR_DEFAUT);

//		String replyToEmail = repondreADefaut;
		
		

		if(compte!=null) {
			switch (compte.getTaServiceWebExterne().getCodeServiceWebExterne()) {
			case UtilServiceWebExterne.CODE_SWE_MAILJET_SMS:
				implementation = MessagePushLgr.TYPE_MESSAGE_PUSH_FIREBASE;
				break;
//			case UtilServiceWebExterne.CODE_SWE_OVH_SMS:
//				implementation = MessagePushLgr.TYPE_SMS_OVH;
//				break;
			default:
				break;
			}
//			fromNumber = numeroExpediteurDefaut;
//			fromName = nomExpediteurDefaut;
		} 
		
		
		
		IEnvoiMessagePushViaServiceExterneDossierService service = iEnvoiPushViaServiceExterneDossierService.select(new MyQualifierMessagePushLiteral(implementation)).get();
		
		
		service.sendAndLog(compte, sujet, resume, contenu, url, 
				urlImage, style, contactMessagePushDestinataire,
					 taUtilisateur);
		
		
		return service;
	}
	
	public IEnvoiMessagePushViaServiceExterneDossierService sendAndLogMessagePushProgramme(String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaContactMessagePush> contactMessagePushDestinataire,

			TaUtilisateur taUtilisateur) {
		
		Boolean modeSMTP = false;
		String implementation = "";
		
		boolean compteExterneDossier = false;
		
		implementation = MessagePushLgr.TYPE_MESSAGE_PUSH_FIREBASE;
		
		
		IEnvoiMessagePushViaServiceExterneDossierService service = iEnvoiPushViaServiceExterneDossierService.select(new MyQualifierMessagePushLiteral(implementation)).get();
		
		service.sendAndLog(sujet, resume, contenu, url, 
			 urlImage, style, contactMessagePushDestinataire,
				 taUtilisateur);
		
		
		return service;
	}
	
	public TaMessagePush updateInfosMessageExterne(TaMessagePush taMessagePush) {
		try {
			if(taMessagePush!=null && taMessagePush.getTypeServiceExterne()!=null) {
				String implementation = null;
				TaCompteServiceWebExterne taCompteServiceWebExterne = null;
				if(taMessagePush.getTypeServiceExterne().equals(MessagePushLgr.TYPE_MESSAGE_PUSH_FIREBASE)) {
					implementation = MessagePushLgr.TYPE_MESSAGE_PUSH_FIREBASE;
//				} else if(taMessageSMS.getTypeServiceExterne().equals(MessagePushLgr.TYPE_SMS_OVH_PROGRAMME)) {
//						implementation = MessagePushLgr.TYPE_SMS_OVH_PROGRAMME;
//				} else if(taMessageSMS.getTypeServiceExterne().equals(MessagePushLgr.TYPE_SMS_MAILJET)) {
//					implementation = MessagePushLgr.TYPE_SMS_MAILJET;
//					if(taMessageSMS.getCodeCompteServiceWebExterne()!=null) {
//						taCompteServiceWebExterne = taCompteServiceWebExterneService.findByCode(taMessageSMS.getCodeCompteServiceWebExterne());
//					}
//				} else if(taMessageSMS.getTypeServiceExterne().equals(MessagePushLgr.TYPE_SMS_OVH)) {
//					implementation = MessagePushLgr.TYPE_SMS_OVH;
//					if(taMessageSMS.getCodeCompteServiceWebExterne()!=null) {
//						taCompteServiceWebExterne = taCompteServiceWebExterneService.findByCode(taMessageSMS.getCodeCompteServiceWebExterne());
//					}
				}
				
				if(implementation!=null) {
					IEnvoiMessagePushViaServiceExterneDossierService service = iEnvoiPushViaServiceExterneDossierService.select(new MyQualifierMessagePushLiteral(implementation)).get();
					return service.updateInfosMessageExterne(taMessagePush, taCompteServiceWebExterne);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taMessagePush;
	}

}

package fr.legrain.push.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import fr.legrain.bdg.push.service.remote.IEnvoiMessagePushViaServiceExterneDossierService;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.lib.mail.LgrMailjet;
import fr.legrain.lib.mail.ReponseMailjetSmsInformationService;
import fr.legrain.lib.mail.ReponseMessagePushFirebaseSendService;
import fr.legrain.lib.mail.ReponseSmsSendService;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;
import fr.legrain.sms.service.SmsLgr;
import fr.legrain.tiers.model.TaTiers;

@Stateless
@MessagePushLgr(MessagePushLgr.TYPE_MESSAGE_PUSH_FIREBASE)
public class EnvoiMessagePushViaFirebaseProgrammeService extends MessagePushService implements IEnvoiMessagePushViaServiceExterneDossierService {
	
//	private String MJ_BEARER_TOKEN = "6e23972a1a524fed85867f8c85a9abec"; //clé publique Mailjet Legrain - clé bdg-programme
//  private LgrMailjet lgrMailjet = new LgrMailjet(MJ_BEARER_TOKEN);
	@EJB private BdgFirebaseService bdgFirebaseService;
	
	@EJB private ITaCompteServiceWebExterneServiceRemote taCompteServiceWebExterneService;
	
	public ReponseMessagePushFirebaseSendService send(TaCompteServiceWebExterne configService, 
			String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaTiers> destinataires) throws EJBException {
		
		this.configService = configService;
		return send(sujet, resume, contenu, url, urlImage, style, destinataires);
	}
	
	public ReponseMessagePushFirebaseSendService send(String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaTiers> destinataires) {
		
		try {
			if(configService==null) {
				configService =  taCompteServiceWebExterneService.findCompteDefautPourAction(TaTServiceWebExterne.TYPE_SMS);
			}
			if(configService!=null) { 
				UtilServiceWebExterne util = new UtilServiceWebExterne();
				configService = util.decrypter(configService);
				
				System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
				System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
				
				
//				lgrMailjet = new LgrMailjet(configService.getApiKey1());
//				return lgrMailjet.sendSMS(fromNumber, fromName, textPart, destinataires);
				bdgFirebaseService.envoyerMessagePushTiersEspaceClient(destinataires.get(0).getTaEmail().getAdresseEmail(), sujet, resume, contenu, url, urlImage);
			}
			//SPECIAL pour les push pour l'instant il n'y a que Firebase et que l'implémentation du programme
			//donc pas besoin de compteServiceWebExterne pour l'instant mais cela risque de changer
			//donc pour l'instant on fait l'appel meme si : configService==null
			bdgFirebaseService.envoyerMessagePushTiersEspaceClient(destinataires.get(0).getTaEmail().getAdresseEmail(), sujet, resume, contenu, url, urlImage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TaMessagePush updateServiceInformation(TaMessagePush taMessageSMS) {
		taMessageSMS.setTypeServiceExterne(SmsLgr.TYPE_SMS_MAILJET);
		if(configService!=null) {
			taMessageSMS.setCodeCompteServiceWebExterne(configService.getCodeCompteServiceWebExterne());
		}
		return taMessageSMS;
	}
	
	@Override
	public TaMessagePush updateInfosMessageExterne(TaMessagePush taMessageSMS, TaCompteServiceWebExterne configService) {
//		if(taMessageSMS.getMessageID()!=null) {
//			
//			if(configService!=null) { 
//				UtilServiceWebExterne util = new UtilServiceWebExterne();
//				configService = util.decrypter(configService);
//				
//				System.out.println("MultitenantProxy.payerFactureCB() clé 1 "+ configService.getApiKey1());	
//				System.out.println("MultitenantProxy.payerFactureCB() clé 2 "+ configService.getApiKey2());
//	
//				lgrMailjet = new LgrMailjet(configService.getApiKey1(), configService.getApiKey2());
//			}
//			ReponseMailjetSmsInformationService r = lgrMailjet.findMessageSMSInformation(taMessageSMS.getMessageID());
//			if(r!=null) {
//				taMessageSMS.setStatusServiceExterne(r.getStatus());
//				taMessageSMS.setEtatMessageServiceExterne(r.getJson());
//				taMessageSMS = taMessagePushService.merge(taMessageSMS);
//			}
//		}
//		return taMessageSMS;
		return null;
	}
	
}

package fr.legrain.bdg.push.service.remote;

import java.util.List;

import javax.ejb.EJBException;

import fr.legrain.lib.mail.ReponseMessagePushFirebaseSendService;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.tiers.model.TaTiers;


public interface IEnvoiMessagePushViaServiceExterneDossierService extends IEnvoiMessagePushService {
	
	public ReponseMessagePushFirebaseSendService send(TaCompteServiceWebExterne configService, 
			String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaTiers> destinataires )
			throws EJBException;

	public TaMessagePush updateInfosMessageExterne(TaMessagePush taMessagePush, TaCompteServiceWebExterne configService);

}
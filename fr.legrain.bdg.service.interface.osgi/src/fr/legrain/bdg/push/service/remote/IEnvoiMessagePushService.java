package fr.legrain.bdg.push.service.remote;

import java.util.List;

import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.push.model.TaContactMessagePush;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.sms.model.TaContactMessageSMS;
import fr.legrain.sms.model.TaMessageSMS;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;


public interface IEnvoiMessagePushService {

	public TaMessagePush sendAndLog(String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaContactMessagePush> contactMessagePushDestinataire,

			TaUtilisateur taUtilisateur
			);
	
	public TaMessagePush sendAndLog(TaCompteServiceWebExterne taCompteServiceWebExterne, String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaContactMessagePush> contactMessagePushDestinataire,
			TaUtilisateur taUtilisateur
			);

}
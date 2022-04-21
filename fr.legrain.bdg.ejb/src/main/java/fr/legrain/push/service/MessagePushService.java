package fr.legrain.push.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.push.service.remote.ITaMessagePushServiceRemote;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.lib.mail.ReponseMessagePushFirebaseSendService;
import fr.legrain.lib.mail.ReponseSmsSendService;
import fr.legrain.push.model.TaContactMessagePush;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;

@Stateless
public abstract class MessagePushService {
	
	
	protected @EJB ITaMessagePushServiceRemote taMessagePushService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	protected TaCompteServiceWebExterne configService = null;
	
//	private String sujet;
//	private String resume;
//	private String contenu;
//	private String url;
//	private String urlImage;
//	private String style;
//	private List<TaTiers> destinataires;

	public abstract ReponseMessagePushFirebaseSendService send(String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaTiers> destinataires);

	public abstract TaMessagePush updateServiceInformation(TaMessagePush taMessagePush);
	
	public TaMessagePush sendAndLog(String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaContactMessagePush> contactMessagePushDestinataire,
			TaUtilisateur taUtilisateur
			) {
		return sendAndLog(null, sujet, resume,  contenu,  
				url, urlImage, style,  contactMessagePushDestinataire,
				taUtilisateur);
	}
	
	public TaMessagePush sendAndLog(TaCompteServiceWebExterne taCompteServiceWebExterne, String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaContactMessagePush> contactMessagePushDestinataire,
			TaUtilisateur taUtilisateur
			) {
		
		try {
			configService = taCompteServiceWebExterne;
			
			TaMessagePush taMessagePush = buildTaMessagePush(sujet, resume, contenu, url, 
					urlImage, style, contactMessagePushDestinataire,
					taUtilisateur);
			
			return sendAndLog(taMessagePush);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param taMessagePush
	 * @param attachment - dans un TaMessageEmail les pieces jointes en byte[] en mémoire, 
	 * 		pour éviter une nouvelle conversion on peu passer la liste de fichier directement 
	 * 		MAIS en cas de mauvaise manipulation la liste de fichier enregistrés dans le TaMessageEmail pourait différait de celle envoyé dans le mail
	 */
	private TaMessagePush sendAndLog(TaMessagePush taMessagePush) {
		try {
			/*
			 * destinatires,cc,bcc,contact et expéditeur
			 */
			//DESTINATAIRE
//			int nbDest = (taMessagePush.getDestinataires()!=null?taMessagePush.getDestinataires().size():0);
//			String[] dest = new String[nbDest];
//			int i = 0;
			List<TaTiers> l = new ArrayList<>();
			if(taMessagePush.getDestinataires()!=null) {
				for (TaContactMessagePush c : taMessagePush.getDestinataires()) {
					c.setTaMessagePush(taMessagePush);
					l.add(c.getTaTiers());
//					dest[i] = c.getNumeroTelephone();
//					i++;
				}
			}
				
			ReponseMessagePushFirebaseSendService rep = send(
				taMessagePush.getSujet(),//null, 
				taMessagePush.getResume(),//taInfoEntrepriseService.findInstance().getNomInfoEntreprise(), 
				taMessagePush.getContenu(), 
				taMessagePush.getUrl(), 
				taMessagePush.getUrlImage(),
				taMessagePush.getStyle(),
				//dest
				l
		
				);
		
			updateServiceInformation(taMessagePush);
			
			if(rep != null) {
				taMessagePush.setInfosService(rep.getReponseTxt());
				taMessagePush.setMessageID(rep.getMessageID());
				taMessagePush.setExpedie(!rep.isErreur());
			}

			taMessagePush = taMessagePushService.merge(taMessagePush);
			
			return taMessagePush;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return taMessagePush;
	}
	
	private TaMessagePush buildTaMessagePush(String sujet, String resume, String contenu, String url, 
			String urlImage, String style, List<TaContactMessagePush> contactMessagePushDestinataire,
			TaUtilisateur taUtilisateur) {
		
		try {
			/*
			 * Corps et option du mail
			 */		
			TaMessagePush taMessagePush = new TaMessagePush();
			
			Date now = new Date();
			taMessagePush.setDateCreation(now);
			taMessagePush.setDateEnvoi(now);
			
			
			taMessagePush.setSujet(sujet);
			taMessagePush.setResume(resume);
			taMessagePush.setContenu(contenu);
			taMessagePush.setUrl(url);
			taMessagePush.setUrlImage(urlImage);
			taMessagePush.setStyle(style);

			
			/*
			 * destinatires,cc,bcc,contact et expéditeur
			 */
			TaContactMessagePush taContactMessagePush = null;
			//DESTINATAIRE
//			int nbDest = (numeroTiersDestinataire!=null?numeroTiersDestinataire.size():0)+(numerosDestinataire!=null?numerosDestinataire.size():0);
//			String[] dest = new String[nbDest];
//			int i = 0;
			if(contactMessagePushDestinataire!=null) {
				for (TaContactMessagePush c : contactMessagePushDestinataire) {
					if(taMessagePush.getDestinataires()==null) {
						taMessagePush.setDestinataires(new HashSet<>());
					}
					taMessagePush.getDestinataires().add(c);
					c.setTaMessagePush(taMessagePush);
				}
			}
//			if(numeroTiersDestinataire!=null) {
//				for (TaTelephone c : numeroTiersDestinataire) {
//					
//					taContactMessagePush = new TaContactMessagePush();
////					taContactMessagePush.setNumeroTelephone(c.getNumeroTelephone());
//					taContactMessagePush.setTaTiers(c.getTaTiers());
//					taContactMessagePush.setTaMessagePush(taMessagePush);
//					if(taMessagePush.getDestinataires()==null) {
//						taMessagePush.setDestinataires(new HashSet<>());
//					}
//					taMessagePush.getDestinataires().add(taContactMessagePush);
//						
//					dest[i] = c.getNumeroTelephone();
//					i++;
//				}
//			}
//			if(numerosDestinataire!=null) {
//				
//				if(taMessagePush.getDestinataires()==null) {
//					taMessagePush.setDestinataires(new HashSet<>());
//				}
//				for (String c : numerosDestinataire) {
//					dest[i] = c;
//					
//					taContactMessagePush = new TaContactMessagePush();
////					taContactMessagePush.setNumeroTelephone(c);
//					taContactMessagePush.setTaMessagePush(taMessagePush);
//					taMessagePush.getDestinataires().add(taContactMessagePush);
//					
//					i++;
//				}
//			}
			
						
			/*
			 * Enregistrement du mail dans l'historique
			 */
			taMessagePush.setTaUtilisateur(taUtilisateur);
			
			
			
			return taMessagePush;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

package fr.legrain.sms.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.sms.service.remote.ITaMessageSMSServiceRemote;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.email.model.TaPieceJointeEmail;
import fr.legrain.lib.mail.ReponseEmailSendService;
import fr.legrain.lib.mail.ReponseSmsSendService;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.sms.model.TaContactMessageSMS;
import fr.legrain.sms.model.TaMessageSMS;
import fr.legrain.tiers.model.TaTelephone;

@Stateless
public abstract class SmsService {
	
	
	protected @EJB ITaMessageSMSServiceRemote taMessageSMSService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	protected TaCompteServiceWebExterne configService = null;
	
	

	public abstract ReponseSmsSendService send(String fromNumber, String fromName,  String textPart, String[] destinataires);

	public abstract TaMessageSMS updateServiceInformation(TaMessageSMS taMessageSMS);
	
	public TaMessageSMS sendAndLog( String fromNumber, String fromName,  String textPart,  
			List<String> numerosDestinataire, List<TaTelephone> numeroTiersDestinataire, List<TaContactMessageSMS> contactMessageSMSDestinataire,
			TaUtilisateur taUtilisateur
			) {
		return sendAndLog(null, fromNumber, fromName,  textPart,  
				numerosDestinataire, numeroTiersDestinataire, contactMessageSMSDestinataire,
				taUtilisateur);
	}
	
	public TaMessageSMS sendAndLog(TaCompteServiceWebExterne taCompteServiceWebExterne, String fromNumber, String fromName, 
			String textPart, 
			List<String> numerosDestinataire, List<TaTelephone> numeroTiersDestinataire, List<TaContactMessageSMS> contactMessageSMSDestinataire,
			TaUtilisateur taUtilisateur
			) {
		
		try {
			configService = taCompteServiceWebExterne;
			
			TaMessageSMS taMessageSMS = buildTaMessageSMS(fromNumber, fromName,  textPart,  
					numerosDestinataire, numeroTiersDestinataire, contactMessageSMSDestinataire,
					taUtilisateur);
			
			return sendAndLog(taMessageSMS);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param taMessageSMS
	 * @param attachment - dans un TaMessageEmail les pieces jointes en byte[] en mémoire, 
	 * 		pour éviter une nouvelle conversion on peu passer la liste de fichier directement 
	 * 		MAIS en cas de mauvaise manipulation la liste de fichier enregistrés dans le TaMessageEmail pourait différait de celle envoyé dans le mail
	 */
	private TaMessageSMS sendAndLog(TaMessageSMS taMessageSMS) {
		try {
			/*
			 * destinatires,cc,bcc,contact et expéditeur
			 */
			//DESTINATAIRE
			int nbDest = (taMessageSMS.getDestinataires()!=null?taMessageSMS.getDestinataires().size():0);
			String[] dest = new String[nbDest];
			int i = 0;
			if(taMessageSMS.getDestinataires()!=null) {
				for (TaContactMessageSMS c : taMessageSMS.getDestinataires()) {
					c.setTaMessageSms(taMessageSMS);
					dest[i] = c.getNumeroTelephone();
					i++;
				}
			}
				
			ReponseSmsSendService rep = send(
				taMessageSMS.getNumeroExpediteur(),//null, 
				taMessageSMS.getNomExpediteur(),//taInfoEntrepriseService.findInstance().getNomInfoEntreprise(), 
				//taMessageSMS.getSubject(), 
				taMessageSMS.getBodyPlain(), 
				//taMessageSMS.getBodyHtml(),
				dest
		
				);
		
			updateServiceInformation(taMessageSMS);
			
			if(rep != null) {
				taMessageSMS.setInfosService(rep.getReponseTxt());
				taMessageSMS.setMessageID(rep.getMessageID());
				taMessageSMS.setExpedie(!rep.isErreur());
			}

			taMessageSMS = taMessageSMSService.merge(taMessageSMS);
			
			return taMessageSMS;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return taMessageSMS;
	}
	
	private TaMessageSMS buildTaMessageSMS(String fromNumber, String fromName,  String textPart,
			List<String> numerosDestinataire, List<TaTelephone> numeroTiersDestinataire, List<TaContactMessageSMS> contactMessageSMSDestinataire,
			TaUtilisateur taUtilisateur) {
		
		try {
			/*
			 * Corps et option du mail
			 */		
			TaMessageSMS taMessageSMS = new TaMessageSMS();
			
			Date now = new Date();
			taMessageSMS.setDateCreation(now);
			taMessageSMS.setDateEnvoi(now);
			
			taMessageSMS.setNumeroExpediteur(fromNumber);
			taMessageSMS.setNomExpediteur(fromName);
			//taMessageSMS.setAdresseReplyTo(replyToEmail);
			
			//taMessageSMS.setSubject(subject);
			taMessageSMS.setBodyPlain(textPart);
			//taMessageSMS.setBodyHtml(htmlPart); //on ne gère pas le contenu html pour l'instant
			
			/*
			 * destinatires,cc,bcc,contact et expéditeur
			 */
			TaContactMessageSMS taContactMessageSMS = null;
			//DESTINATAIRE
			int nbDest = (numeroTiersDestinataire!=null?numeroTiersDestinataire.size():0)+(numerosDestinataire!=null?numerosDestinataire.size():0);
			String[] dest = new String[nbDest];
			int i = 0;
			if(contactMessageSMSDestinataire!=null) {
				for (TaContactMessageSMS c : contactMessageSMSDestinataire) {
					if(taMessageSMS.getDestinataires()==null) {
						taMessageSMS.setDestinataires(new HashSet<>());
					}
					taMessageSMS.getDestinataires().add(c);
					c.setTaMessageSms(taMessageSMS);
				}
			}
			if(numeroTiersDestinataire!=null) {
				for (TaTelephone c : numeroTiersDestinataire) {
					
					taContactMessageSMS = new TaContactMessageSMS();
					taContactMessageSMS.setNumeroTelephone(c.getNumeroTelephone());
					taContactMessageSMS.setTaTiers(c.getTaTiers());
					taContactMessageSMS.setTaMessageSms(taMessageSMS);
					if(taMessageSMS.getDestinataires()==null) {
						taMessageSMS.setDestinataires(new HashSet<>());
					}
					taMessageSMS.getDestinataires().add(taContactMessageSMS);
						
					dest[i] = c.getNumeroTelephone();
					i++;
				}
			}
			if(numerosDestinataire!=null) {
				
				if(taMessageSMS.getDestinataires()==null) {
					taMessageSMS.setDestinataires(new HashSet<>());
				}
				for (String c : numerosDestinataire) {
					dest[i] = c;
					
					taContactMessageSMS = new TaContactMessageSMS();
					taContactMessageSMS.setNumeroTelephone(c);
					taContactMessageSMS.setTaMessageSms(taMessageSMS);
					taMessageSMS.getDestinataires().add(taContactMessageSMS);
					
					i++;
				}
			}
			
						
			/*
			 * Enregistrement du mail dans l'historique
			 */
			taMessageSMS.setTaUtilisateur(taUtilisateur);
			
			
			
			return taMessageSMS;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

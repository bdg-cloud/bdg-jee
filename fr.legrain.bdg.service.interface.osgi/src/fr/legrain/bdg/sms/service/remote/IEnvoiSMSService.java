package fr.legrain.bdg.sms.service.remote;

import java.util.List;

import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.sms.model.TaContactMessageSMS;
import fr.legrain.sms.model.TaMessageSMS;
import fr.legrain.tiers.model.TaTelephone;


public interface IEnvoiSMSService {

	public TaMessageSMS sendAndLog(String fromNumber, String fromName,  String textPart, 
			List<String> adressesDestinataire, List<TaTelephone> emailTiersDestinataire, List<TaContactMessageSMS> contactMessageEmailDestinataire,

			TaUtilisateur taUtilisateur
			);
	
	public TaMessageSMS sendAndLog(TaCompteServiceWebExterne taCompteServiceWebExterne, String fromNumber, String fromName,  String textPart,  
			List<String> adressesDestinataire, List<TaTelephone> emailTiersDestinataire, List<TaContactMessageSMS> contactMessageEmailDestinataire,
			TaUtilisateur taUtilisateur
			);

}
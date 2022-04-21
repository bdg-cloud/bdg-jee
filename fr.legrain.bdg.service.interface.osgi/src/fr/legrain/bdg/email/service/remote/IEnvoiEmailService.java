package fr.legrain.bdg.email.service.remote;

import java.io.File;
import java.util.List;

import javax.ejb.EJBException;

import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.tiers.model.TaEmail;


public interface IEnvoiEmailService {

	public TaMessageEmail sendAndLog(String fromEmail, String fromName, String subject, String textPart, String htmlPart, 
			List<String> adressesDestinataire, List<TaEmail> emailTiersDestinataire, List<TaContactMessageEmail> contactMessageEmailDestinataire,
			List<String> adressesCc, List<TaEmail> emailTiersCc, List<TaContactMessageEmail> contactMessageEmailCc,
			List<String> adressesBcc, List<TaEmail> emailTiersBcc, List<TaContactMessageEmail> contactMessageEmailBcc,
			File[] attachment, String replyToEmail,
			TaUtilisateur taUtilisateur
			);
	
	public TaMessageEmail sendAndLog(TaCompteServiceWebExterne taCompteServiceWebExterne, String fromEmail, String fromName, String subject, String textPart, String htmlPart, 
			List<String> adressesDestinataire, List<TaEmail> emailTiersDestinataire, List<TaContactMessageEmail> contactMessageEmailDestinataire,
			List<String> adressesCc, List<TaEmail> emailTiersCc, List<TaContactMessageEmail> contactMessageEmailCc,
			List<String> adressesBcc, List<TaEmail> emailTiersBcc, List<TaContactMessageEmail> contactMessageEmailBcc,
			File[] attachment, String replyToEmail,
			TaUtilisateur taUtilisateur
			);

}
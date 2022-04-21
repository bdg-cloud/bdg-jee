package fr.legrain.bdg.compteclientfinal.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.paiement.model.PaiementCarteBancaire;
import fr.legrain.paiement.model.RetourPaiementCarteBancaire;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.tiers.model.TiersDossier;

@Remote
public interface ICompteClientFinalServiceRemote {
	
	/*
	 * Clé API fixe
	 */
	
	//Bdg booleen (comme imprimer) pour dire qu'un document est mis à disposition dans l'espace client, à ce moment génération d'un PDF stocké dans un blob dans une table qui ne sert que pour ça
	//Bdg booleen pour dire qu'un document a été récupéré par le client final (verrouillage)
	
	
	//liste des fournisseurs
	
	//liste des fournisseurs qui utilise les compte client
	public List<TiersDossier> listeFournisseur();
	
	//vérification d'un code client au près d'un fournisseur
	public boolean clientExisteChezFournisseur(String codeDossierFournisseur, String codeClientChezCeForunisseur, String cleLiaisonCompteClient);
		//nouveau compte => envoi de mail avec lien de confirmation, si pas d'email sur ce tiers message ?
		//validation d'un "lien de confirmation" (nouveau champ dans le tiers ?)
		//compte client deja existant => retour d'infos (ou pas)
		//code client invalide => retour d'infos (ou pas)
	public TaTiers infosClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur);
	
	public boolean fournisseurPermetPaiementParCB(String codeDossierFournisseur, String codeClientChezCeFournisseur);
	
	//liste des factures du tiers chez un fournisseur (facture complète ou light)
	//idem devis, bc, ... ?
	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur);
	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur, Date debut, Date fin);
	public List<TaDevis> devisClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur);
	public List<TaDevis> devisClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeFournisseur, Date debut, Date fin);
	
	//modification du document (règlement)
	
	//édition du document (fichier)
	public byte[] pdfClient(String codeDossierFournisseur, String codeClientChezCeFournisseur);
	public byte[] pdfFacture(String codeDossierFournisseur, String codeFactureChezCeFournisseur);
	
	
	//Dans BDG pour la création de compte, si adresse email invalide, écran dans le compte client pour demander un changement d'adresse email et donc gestion de ces demandes dans BDG ?
	
	//messages/ticket entre le client final et le fournisseur (nouvelle tables, quelle appli ?)
	
	//Système de paiement en ligne actif ou non
	
	//règlement d'un facture
	public RetourPaiementCarteBancaire payerFactureCB(String codeDossierFournisseur, String codeClientChezCeFournisseur, PaiementCarteBancaire cb, TaFacture facture) throws EJBException;
		//création de compte Stripe via API
		//Si compte "managed" récupération de toutes les infos de paiement pour Stripe
	
	//Récupération d'un taux de commission Legrain à appliquer au paiement (nouvelle table, et nouveau champ dans "moncompte" pour gérer un plafond
	
	//suivi du compte client coté fournisseur (bdg) : dernière consultation de document, suprression de liens, ...
}

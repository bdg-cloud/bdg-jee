package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.ejb.Remote;

import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTPaiement;


@Remote
public interface ITaPanierServiceRemote extends IGenericCRUDServiceDocumentRemote<TaPanier,TaPanierDTO>,
														IDocumentLigneALigneService<TaPanier>,
														IAbstractLgrDAOServer<TaPanier>,IAbstractLgrDAOServerDTO<TaPanierDTO>,
														IDocumentService<TaPanier>, IDocumentTiersStatistiquesService<TaPanier>,IDashboardDocumentServiceRemote{
	
	public static final String validationContext = "PANIER";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public List<TaPanierDTO> findAllLight();
	public List<TaPanierDTO> findByCodeLight(String code);
	public Date calculDateEcheanceAbstract(TaPanier doc, Integer report, Integer finDeMois);
	public Date calculDateEcheance(TaPanier doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) ;
	public Date calculDateEcheance(TaPanier doc, Integer report, Integer finDeMois) ;
	public List<IDocumentTiers> selectDocNonTransformeRequete(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin);
	
	public void calculeTvaEtTotaux(TaPanier doc);
	
	public TaPanier getPanierActifByCodeTier(String codeTiers);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaPanier doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaPanier doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaPanier doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaPanier doc);
	
	public TaPanier mergeAndFindById(TaPanier detachedInstance, String validationContext);
	
	public TaEtat rechercheEtatInitialDocument();
	public TaEtat etatLigneInsertion(TaPanier masterEntity);
	
	public TaPanier findByActif(String codeTiers);
	public TaPanier findByActifAvecLiaisonReglement(String codeTiers);
	
//	public TaPanier findDocByLDoc(ILigneDocumentTiers lDoc);
//	public TaPanier mergeEtat(TaPanier detachedInstance);
	
	public TaBoncde findCommandePanier(String codePanier);
	public TaReglement findReglementPourPanier(String codePanier);
	public TaStripePaymentIntent findReglementStripePanier(String codePanier);
	
	public TaPanierDTO recupereDerniereVersionPanierDTO(TaPanier entity) throws FinderException;
	public TaPanierDTO supprimerAjustementPrixVariable(int idPanier);
	public TaPanierDTO supprimerFdpFixe(int idPanier);
	public TaPanierDTO supprimerLigneCommentaireRetrait(int idPanier);
	public TaPanierDTO supprimerOptionsPanier(int idPanier);
	
	public TaPanierDTO ajouteLignePrixVariable(int idPanier);
	public TaPanierDTO supprimeCommentaireDateRetrait(int idPanier);
	public TaPanierDTO ajouteFdpFixePanier(int idPanier);
	public TaPanierDTO ajouteCommentaireDateRetrait(int idPanier, Date dateTimeMillis);
	public TaPanierDTO supprimeFdpFixePanier(int idPanier);
	
	public TaPanierDTO resumerPanier(int idPanier, String selectedTypeExpedition, Date dateRetrait);
}

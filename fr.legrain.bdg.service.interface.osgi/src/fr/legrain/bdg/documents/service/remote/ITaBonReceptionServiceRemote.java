package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.dto.TaLBonReceptionDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.tiers.dto.TaTiersDTO;

@Remote
public interface ITaBonReceptionServiceRemote extends IGenericCRUDServiceDocumentRemote<TaBonReception,TaBonReceptionDTO>,
IDocumentLigneALigneService<TaBonReception>,														
IAbstractLgrDAOServer<TaBonReception>,IAbstractLgrDAOServerDTO<TaBonReceptionDTO>,
														IDocumentService<TaBonReception>{
	
	public static final String validationContext = "BON_RECEPTION";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public List<TaBonReceptionDTO> findAllLight();
	public List<TaBonReceptionDTO> findByCodeLight(String code);
	
	
	public void calculeTvaEtTotaux(TaBonReception doc);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaBonReception doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaBonReception doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaBonReception doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaBonReception doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaBonReception doc);
	
	public Date calculDateEcheanceAbstract(TaBonReception doc, Integer report, Integer finDeMois);
	
	public Date calculDateEcheance(TaBonReception doc, Integer report, Integer finDeMois);
	
	public TaBonReception mergeAndFindById(TaBonReception detachedInstance, String validationContext);
	
	// Dima - Début
	public List<TaLBonReception> bonRecepFindByCodeArticle(String codeArticle);
	
	public List<TaLBonReception> bonRecepFindByLotParDate(String numLot , Date dateDeb, Date dateFin);
	public List<TaLBonReception> bonRecepFindByCodeArticleParDate(String codeArticle , Date dateDeb, Date dateFin);
	public List<TaLBonReceptionDTO> bonRecepFindByCodeFournisseurParDate(String codeFournisseur , Date dateDeb, Date dateFin);
	public List<TaTiersDTO> findTiersDTOByCodeArticleAndDate(String codeArticle, Date debut,Date fin);
	public TaBonReception findAvecResultatConformites(int idBr);
	public TaEtat rechercheEtatInitialDocument();
	public TaEtat etatLigneInsertion(TaBonReception masterEntity);
//	public TaBonReception findDocByLDoc(ILigneDocumentTiers lDoc);
//	public TaBonReception mergeEtat(IDocumentTiers detachedInstance);
}

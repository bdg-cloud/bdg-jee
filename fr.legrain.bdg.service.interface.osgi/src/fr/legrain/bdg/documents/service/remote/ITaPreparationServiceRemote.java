package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaPreparationDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaTPaiement;


@Remote
public interface ITaPreparationServiceRemote extends IGenericCRUDServiceDocumentRemote<TaPreparation,TaPreparationDTO>,
														IDocumentLigneALigneService<TaPreparation>,
														IAbstractLgrDAOServer<TaPreparation>,IAbstractLgrDAOServerDTO<TaPreparationDTO>,
														IDocumentService<TaPreparation>, IDocumentTiersStatistiquesService<TaPreparation>,IDashboardDocumentServiceRemote{
	
	public static final String validationContext = "BON_LIVRAISON";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public List<TaPreparationDTO> findAllLight();
	public List<TaPreparationDTO> findByCodeLight(String code);
	public Date calculDateEcheanceAbstract(TaPreparation doc, Integer report, Integer finDeMois);
	public Date calculDateEcheance(TaPreparation doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) ;
	public Date calculDateEcheance(TaPreparation doc, Integer report, Integer finDeMois) ;
	public List<IDocumentTiers> selectDocNonTransformeRequete(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin);
	
	public void calculeTvaEtTotaux(TaPreparation doc);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaPreparation doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaPreparation doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaPreparation doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaPreparation doc);
	
	public TaPreparation mergeAndFindById(TaPreparation detachedInstance, String validationContext);
	
	public TaEtat rechercheEtatInitialDocument();
	public TaEtat etatLigneInsertion(TaPreparation masterEntity);
	
//	public TaPreparation findDocByLDoc(ILigneDocumentTiers lDoc);
//	public TaPreparation mergeEtat(TaPreparation detachedInstance);

}

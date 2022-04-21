package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.ejb.Remote;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.edition.model.TaEdition;


@Remote
public interface ITaBonlivServiceRemote extends IGenericCRUDServiceDocumentRemote<TaBonliv,TaBonlivDTO>,
IDocumentLigneALigneService<TaBonliv>,														
IAbstractLgrDAOServer<TaBonliv>,IAbstractLgrDAOServerDTO<TaBonlivDTO>,
														IDocumentService<TaBonliv>, IDocumentTiersStatistiquesService<TaBonliv>,IDashboardDocumentServiceRemote{
	
	public static final String validationContext = "BON_LIVRAISON";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public List<TaBonlivDTO> findAllLight();
	public List<TaBonlivDTO> findByCodeLight(String code);
	public Date calculDateEcheanceAbstract(TaBonliv doc, Integer report, Integer finDeMois);
	public Date calculDateEcheance(TaBonliv doc, Integer report, Integer finDeMois,TaTPaiement taTPaiement) ;
	public Date calculDateEcheance(TaBonliv doc, Integer report, Integer finDeMois) ;
	public List<IDocumentTiers> selectDocNonTransformeRequete(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin);
	
	public void calculeTvaEtTotaux(TaBonliv doc);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaBonliv doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaBonliv doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaBonliv doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaBonliv doc);
	
	public TaBonliv mergeAndFindById(TaBonliv detachedInstance, String validationContext);
	
	public String writingFileEdition(TaEdition taEdition);
	TaBonliv mergeEtat(IDocumentTiers detachedInstance, boolean avecLien);
	public TaEtat etatLigneInsertion(TaBonliv masterEntity);
	public TaEtat rechercheEtatInitialDocument();
	
	public List<IDocumentTiersDTO> selectTourneeTransporteur(String codeTransporteur, Date debut, Date fin);
	
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin,
			String codeArticle);

	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin,
			String codeArticle, Boolean synthese);

	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin,
			String codeArticle, Boolean synthese, String orderBy);
	
	public List<IDocumentTiersDTO> selectBLNonTermineSansLotTransporteur(String codeTransporteur, Date debut, Date fin);
	
	
//	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);	

	//	public List<TaBonlivDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
//	
//	
//	public List<TaBonlivDTO> findBonlivNonTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaBonlivDTO> findBonlivTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaBonlivDTO> findBonlivNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);

//	public long countBonliv(Date debut, Date fin);	
//	public long countBonlivNonTransforme(Date debut, Date fin);
//	public long countBonlivNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countBonlivTransfos(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> caBonlivNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
////	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotal(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeARelancerJmaDTO(Date debut, Date fin, int precision);
//	public TaBonliv findDocByLDoc(ILigneDocumentTiers lDoc);
}

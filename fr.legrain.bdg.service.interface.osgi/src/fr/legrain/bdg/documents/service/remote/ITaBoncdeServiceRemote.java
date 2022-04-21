package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.document.model.TaPreparation;


@Remote
public interface ITaBoncdeServiceRemote extends IGenericCRUDServiceDocumentRemote<TaBoncde,TaBoncdeDTO>,
IDocumentLigneALigneService<TaBoncde>,														
IAbstractLgrDAOServer<TaBoncde>,IAbstractLgrDAOServerDTO<TaBoncdeDTO>,
														IDocumentService<TaBoncde>, IDocumentTiersStatistiquesService<TaBoncde>,IDashboardDocumentServiceRemote{
	public static final String validationContext = "BONCDE";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public TaBoncde mergeAndFindById(TaBoncde detachedInstance, String validationContext);
	
	public void calculeTvaEtTotaux(TaBoncde doc);
	
	public List<TaBoncde> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaBoncde doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaBoncde doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaBoncde doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaBoncde doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaBoncde doc);
	
	public Date calculDateEcheanceAbstract(TaBoncde doc, Integer report, Integer finDeMois);
	
	public Date calculDateEcheance(TaBoncde doc, Integer report, Integer finDeMois);
	
	public List<TaBoncdeDTO> findAllLight();
/////////Bloc de méthode pour notamment les onglets factures/devis/commandes/proforma dash par article et dash par tiers//////
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleNonTransforme(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleTransforme(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleARelancer(Date dateDebut,Date dateFin, int deltaNbJours,String codeArticle);
	
	public long countDocument(Date debut, Date fin, String codeTiers , String codeArticle);
	public long countDocumentNonTransforme(Date debut, Date fin, String codeTiers , String codeArticle);
	public long countDocumentTransforme(Date debut, Date fin, String codeTiers , String codeArticle);
	public long countDocumentNonTransformeARelancer(Date debut, Date fin,int deltaNbJours, String codeTiers , String codeArticle);

	public List<DocumentDTO> findAllLigneDTOPeriode(Date dateDebut, Date dateFin,String codeTiers, String codeArticle);
	public List<DocumentDTO> findDocumentTransfosLigneDTO(Date dateDebut, Date dateFin,String codeTiers, String codeArticle);
	public List<DocumentDTO> findDocumentNonTransfosLigneDTO(Date dateDebut, Date dateFin,String codeTiers, String codeArticle);
	public List<DocumentDTO> findDocumentNonTransfosARelancerLigneDTO(Date dateDebut, Date dateFin,int deltaNbJours, String codeTiers, String codeArticle);
	
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTransformeDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireNonTransformeDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireNonTransformeARelancerDTOArticleMois(Date dateDebut,Date dateFin, int deltaNbJours,String codeArticle,String codeTiers);
	
	public String writingFileEdition(TaEdition taEdition);
	public TaEtat rechercheEtatInitialDocument();
	public TaEtat etatLigneInsertion(TaBoncde masterEntity);
	
	public List<IDocumentTiersDTO> selectTourneeTransporteur(String codeTransporteur, Date debut, Date fin);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivDTOParFamille(Date dateDebut, Date dateFin, String codeArticle, Boolean synthese, String orderBy);
	
}

package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.dashboard.service.remote.IDashboardDocumentServiceRemote;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.edition.model.TaEdition;

@Remote
public interface ITaAvisEcheanceServiceRemote extends IGenericCRUDServiceDocumentRemote<TaAvisEcheance,TaAvisEcheanceDTO>,
IAbstractLgrDAOServer<TaAvisEcheance>,IAbstractLgrDAOServerDTO<TaAvisEcheanceDTO>,IDocumentLigneALigneService<TaAvisEcheance>,
IDocumentService<TaAvisEcheance>, IDocumentTiersStatistiquesService<TaAvisEcheance>, IDashboardDocumentServiceRemote,
IDocumentCodeGenere{
	public static final String validationContext = "AVIS_ECHEANCE";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public TaAvisEcheance mergeAndFindById(TaAvisEcheance detachedInstance, String validationContext);
	
	public String writingFileEdition(TaEdition taEdition);
	
	public List<TaAvisEcheance> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin);
	
	public void calculeTvaEtTotaux(TaAvisEcheance doc);
	
	/**
	 * Calcul des totaux de la facture
	 */
	public void calculTotaux(TaAvisEcheance doc);

	/**
	 * Reparti le total chaque code TVA sur l'ensemble des lignes concernées par ce code. 
	 */
	public void dispatcherTva(TaAvisEcheance doc);
	
	/**
	 * Lance la fonction de calcul du montant sur chacunes des lignes du document.
	 */
	public void calculMontantLigneDocument(TaAvisEcheance doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 * et mise à jour du montant de la TVA dans les lignes du document
	 */
	public void calculTvaTotal(TaAvisEcheance doc);
	
	/**
	 * Calcul de la grille de TVA en fonction de lignes du document et du taux de remise HT global.
	 * Mise à jour de la propriété <code>lignesTVA</code>
	 */
	public void calculLignesTva(TaAvisEcheance doc);
	
	public Date calculDateEcheanceAbstract(TaAvisEcheance doc, Integer report, Integer finDeMois);
	
	public Date calculDateEcheance(TaAvisEcheance doc, Integer report, Integer finDeMois);
	
	public List<TaAvisEcheanceDTO> findAllLight();
	
//	public List<TaAvisEcheance> findByAbonnementAndDate(TaStripeSubscription taStripeSubscription, Date dateEcheance);
	
	/////////Bloc de méthode pour notamment les onglets factures/devis/commandes/AvisEcheance dash par article et dash par tiers//////
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
	////////Fin bloc de méthode//////////////////////////////////////////////////////////////////////
	
	public List<TaAvisEcheance> rechercheDocumentSubscription(int idStripeSubscription);
	
	public List<TaAbonnement> rechercheAbonnementDansAvisEcheance(TaAvisEcheance taAvisEcheance);
	
	//DASHBOARD////
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersParTypeEtatDoc(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String typeEtatDoc, int deltaNbJours);
	//FIN DASH////
	
	//SPECIFIQUE ABONNEMENTS
	public List<DocumentDTO> findDocumentNonTransfosDTOAboEnCours(Date dateDebut, Date dateFin,String codeTiers);
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTOAboEnCours(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTOAboEnCours(Date dateDebut, Date dateFin, String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTOAboEnCours(Date dateDebut,Date dateFin, int deltaNbJours,String codeTiers);
	public long countDocumentNonTransformeAboEnCours(Date debut, Date fin, String codeTiers);
	public long countDocumentNonTransformeARelancerAboEnCours(Date debut, Date fin, int deltaNbJours,String codeTiers);
	public TaAvisEcheance creerAvisEcheance(List<TaLEcheance> listeLigneEcheance);
	
	public TaEtat rechercheEtatInitialDocument();
	public TaEtat etatLigneInsertion(TaAvisEcheance masterEntity);
	public TaAvisEcheance mergeEtat(IDocumentTiers detachedInstance, List<IDocumentTiers> avecLien);
	
	public List<TaAvisEcheance> rechercheByIdAbonnement(Integer idAbo);
	public List<TaAvisEcheance> rechercheAvisFaux();
	public void removeAllRDocumentEvenementAvisEcheance();
	
	public List<TaAvisEcheanceDTO> rechercheNonLieAFactureByIdTiers(Integer id);
	
	public Integer supprimeAvisVieuxXmois(Integer nbMois);

//	public List<TaAvisEcheanceDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
//	public List<TaAvisEcheanceDTO> findAvisEcheanceNonTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaAvisEcheanceDTO> findAvisEcheanceTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaAvisEcheanceDTO> findAvisEcheanceNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);

//	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
}



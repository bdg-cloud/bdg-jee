package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;

//@Remote
public interface IAvisEcheanceDAO extends IGenericDAO<TaAvisEcheance>, IDocumentDAO<TaAvisEcheance>,IDocumentTiersStatistiquesDAO<TaAvisEcheance>,IDocumentTiersEtatDAO<TaAvisEcheance>
,IDashboardDocumentDAO{
	
	public String genereCode();
	public List<TaAvisEcheanceDTO> findAllLight();
//	public List<TaProformaDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
//	public List<TaProformaDTO> findProformaNonTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaProformaDTO> findProformaNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<TaProformaDTO> findProformaTransfosDTO(Date dateDebut, Date dateFin);
	
	public List<TaAvisEcheance> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin);
	
//	public List<TaAvisEcheance> findByAbonnementAndDate(TaStripeSubscription taStripeSubscription, Date dateEcheance);
	
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
	////////Fin bloc de méthode//////////////////////////////////////////////////////////////////////
	
	public List<TaAvisEcheance> rechercheDocumentSubscription(int idStripeSubscription);
	
	public List<TaAbonnement> rechercheAbonnementDansAvisEcheance(TaAvisEcheance taAvisEcheance);
	
	///DASHBOARD///
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersParTypeEtatDoc(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String typeEtatDoc, int deltaNbJours);

	///FIN DASH///
	
	//SPECIFIQUE ABONNEMENTS
	public List<DocumentDTO> findDocumentNonTransfosDTOAboEnCours(Date dateDebut, Date dateFin,String codeTiers);
	public List<DocumentDTO> findDocumentNonTransfosARelancerDTOAboEnCours(Date dateDebut, Date dateFin, int deltaNbJours,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTOAboEnCours(Date dateDebut, Date dateFin, String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTOAboEnCours(Date dateDebut,Date dateFin, int deltaNbJours,String codeTiers);
	public long countDocumentNonTransformeAboEnCours(Date debut, Date fin, String codeTiers);
	public long countDocumentNonTransformeARelancerAboEnCours(Date debut, Date fin, int deltaNbJours,String codeTiers);
	
	public List<TaAvisEcheance> rechercheByIdAbonnement(Integer idAbo);
	public List<TaAvisEcheance> rechercheAvisFaux();
	
	public void removeAllRDocumentEvenementAvisEcheance();
	
	public List<TaAvisEcheanceDTO> rechercheNonLieAFactureByIdTiers(Integer id);
	

}

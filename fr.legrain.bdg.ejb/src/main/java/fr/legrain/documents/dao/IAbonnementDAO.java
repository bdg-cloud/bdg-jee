package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;
import fr.legrain.tiers.dto.TaDocumentTiersDTO;

//@Remote
public interface IAbonnementDAO extends IGenericDAO<TaAbonnement>, IDocumentDAO<TaAbonnement>,IDocumentTiersStatistiquesDAO<TaAbonnement>,IDocumentTiersEtatDAO<TaAbonnement>,IDashboardDocumentDAO {
	public String genereCode();
	public List<TaAbonnementDTO> findAllLight();
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleNonTransforme(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleTransforme(Date dateDebut, Date dateFin, String codeArticle);
	public List<DocumentChiffreAffaireDTO> sumChiffreAffaireTotalDTOArticleARelancer(Date dateDebut,Date dateFin, int deltaNbJours,String codeArticle);
	public TaAbonnement findByIdSubscription(Integer idStripeSubscription);
	
	public long countDocument(Date debut, Date fin, String codeTiers , String codeArticle);
	public long countDocumentNonTransforme(Date debut, Date fin, String codeTiers , String codeArticle);
	public long countDocumentTransforme(Date debut, Date fin, String codeTiers , String codeArticle);
	public long countDocumentNonTransformeARelancer(Date debut, Date fin,int deltaNbJours, String codeTiers , String codeArticle);
	
	public long countDocumentAnnule(Date dateDebut, Date dateFin,String codeTiers);
	
	public List<DocumentDTO> findAllLigneDTOPeriode(Date dateDebut, Date dateFin,String codeTiers, String codeArticle);
	public List<DocumentDTO> findDocumentTransfosLigneDTO(Date dateDebut, Date dateFin,String codeTiers, String codeArticle);
	public List<DocumentDTO> findDocumentNonTransfosLigneDTO(Date dateDebut, Date dateFin,String codeTiers, String codeArticle);
	public List<DocumentDTO> findDocumentNonTransfosARelancerLigneDTO(Date dateDebut, Date dateFin,int deltaNbJours, String codeTiers, String codeArticle);
	
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTotalDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireTransformeDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireNonTransformeDTOArticleMois(Date dateDebut, Date dateFin, String codeArticle, String codeTiers);
	public List<DocumentChiffreAffaireDTO> listeSumChiffreAffaireNonTransformeARelancerDTOArticleMois(Date dateDebut,Date dateFin, int deltaNbJours,String codeArticle,String codeTiers);
	
	
	public DocumentChiffreAffaireDTO chiffreAffaireTotalAnnuleDTO(Date dateDebut, Date dateFin,String codeTiers);
	public List<TaAbonnementDTO> findAllAnnuleDTOPeriode(Date dateDebut, Date dateFin,String codeTiers);
	public List<TaAbonnementDTO> findAllEnCoursDTOPeriode(Date dateDebut, Date dateFin,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireTotalEnCoursDTO(Date dateDebut, Date dateFin,String codeTiers);
	public long countDocumentEnCours(Date dateDebut, Date dateFin,String codeTiers);
	public List<TaAbonnementDTO> findAllSuspenduDTOPeriode(Date dateDebut, Date dateFin,String codeTiers);
	public DocumentChiffreAffaireDTO chiffreAffaireTotalSuspenduDTO(Date dateDebut, Date dateFin,String codeTiers);
	public long countDocumentSuspendu(Date dateDebut, Date dateFin,String codeTiers);
	
	public List<TaAbonnementFullDTO> findAllFullDTOByIdTiers(Integer idTiers);
	
	public List<TaAbonnementDTO> rechercherAbonnementNonStripeCustomerDTO(String idExterneCustomer);
	public List<TaAbonnementDTO> rechercherAbonnementCustomerDTO(String idExterneCustomer);
	public List<TaAbonnementDTO> rechercherAbonnementCustomerDTO(String idExterneCustomer, Boolean stripe);
	
	public List<TaAbonnementDTO> selectAllASuspendre();
	public TaAbonnement findByIdLEcheance(Integer idLEcheance);
	
	public List<TaAbonnementDTO> findAllEnCoursDTOArrivantAEcheanceDansXJours(Integer nbJours, String codeTiers);
	
	public List<TaAbonnement> findAllEnCours();
	public List<TaAbonnement>  findAllSansDatesPeriode();
	
	public List<TaAbonnement> selectAllAvecFetchLignesEtPlan();
	
	public List<TaAbonnementDTO> findAllAnnuleDTO(String codeTiers);
	
	public List<TaAbonnement> selectAllPourInsertionPeriodeV2();


}

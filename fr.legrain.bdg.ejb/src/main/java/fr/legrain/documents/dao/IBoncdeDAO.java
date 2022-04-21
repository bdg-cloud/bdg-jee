package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.DocumentDTO;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.tiers.dao.IDocumentDAO;

//@Remote
public interface IBoncdeDAO extends IGenericDAO<TaBoncde>,IDocumentDAO<TaBoncde>,IDashboardDocumentDAO {
	public String genereCode();
	public List<TaBoncdeDTO> findAllLight();
//	public List<TaBoncdeDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
//	public List<TaBoncdeDTO> findBoncdeNonTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaBoncdeDTO> findBoncdeNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<TaBoncdeDTO> findBoncdeTransfosDTO(Date dateDebut, Date dateFin);
	
	public List<TaBoncde> findByCodeTiersAndDateCompteClient(String codeTiers, Date debut, Date fin);
	
	
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date debut, Date fin);
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin);
//	
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin);
//	public long countDocument(Date debut, Date fin);
//	public long countDocumentTransforme(Date debut, Date fin);
//	public long countDocumentNonTransforme(Date debut, Date fin);
//	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//
//
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin);
//	
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	
	
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
	public List<IDocumentTiersDTO> selectTourneeTransporteur(String codeTransporteur, Date debut, Date fin);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivDTOParFamille(Date dateDebut, Date dateFin,String codeArticle);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivDTOParFamille(Date dateDebut, Date dateFin,	String codeArticle, Boolean synthese);
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivDTOParFamille(Date dateDebut, Date dateFin,	String codeArticle, Boolean synthese, String orderBy);
	
	
//
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date debut, Date fin, int precision, int deltaNbJours);
	
//	public long countBoncde(Date debut, Date fin);
//	public long countBoncdeNonTransforme(Date debut, Date fin);
//	public long countBoncdeNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countBoncdeTransfos(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> caBoncdeNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeARelancerJmaDTO(Date debut, Date fin, int precision);
}

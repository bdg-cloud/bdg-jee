package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dashboard.dto.TaArticlesParTiersDTO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.IDocumentTiersDTO;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.tiers.dao.IDocumentDAO;

//@Remote
public interface IBonlivDAO extends IGenericDAO<TaBonliv>,IDocumentDAO<TaBonliv>,IDashboardDocumentDAO {

	public List<IDocumentTiers> selectDocNonTransformeRequete(IDocumentTiers doc ,String codeTiers,String typeOrigine,String typeDest,Date debut,Date fin);
	
	public List<TaBonlivDTO> findAllLight();
	public List<TaBonlivDTO> findByCodeLight(String code);

	public List<IDocumentTiersDTO> selectTourneeTransporteur(String codeTransporteur, Date debut, Date fin);

	
	
	
	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin,
			String codeArticle);

	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin,
			String codeArticle, Boolean synthese);

	public List<DocumentChiffreAffaireDTO> listLigneArticlePeriodeBonlivTransporteurDTO(String codeTransporteur,Date dateDebut, Date dateFin,
			String codeArticle, Boolean synthese, String orderBy);

	public List<IDocumentTiersDTO> selectBLNonTermineSansLotTransporteur(String codeTransporteur, Date debut, Date fin);
	
//	public TaBonliv findDocByLDoc(ILigneDocumentTiers lDoc);
	
//	public List<TaArticlesParTiersDTO> findArticlesParTiersParMois(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransforme(Date debut, Date fin);
//	public List<TaArticlesParTiersDTO> findArticlesParTiersNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);	
//	public List<TaBonlivDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
//	public List<TaBonlivDTO> findBonlivNonTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaBonlivDTO> findBonlivNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<TaBonlivDTO> findBonlivTransfosDTO(Date dateDebut, Date dateFin);
	
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalDTO(Date debut, Date fin);
//	public DocumentChiffreAffaireDTO chiffreAffaireTotalDTO(Date dateDebut, Date dateFin);
//	
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeTotalDTO(Date dateDebut, Date dateFin);
//
//
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public DocumentChiffreAffaireDTO chiffreAffaireTransformeTotalDTO(Date dateDebut, Date dateFin);
//	
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public DocumentChiffreAffaireDTO chiffreAffaireNonTransformeARelancerTotalDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	
//	public long countDocument(Date debut, Date fin);
//	public long countDocumentTransforme(Date debut, Date fin);
//	public long countDocumentNonTransforme(Date debut, Date fin);
//	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTotalJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> listeChiffreAffaireNonTransformeARelancerJmaDTO(Date debut, Date fin, int precision, int deltaNbJours);
//	public long countBonliv(Date debut, Date fin);
//	public long countBonlivNonTransforme(Date debut, Date fin);
//	public long countBonlivNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countBonlivTransfos(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> caBonlivNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeARelancerJmaDTO(Date debut, Date fin, int precision);
}

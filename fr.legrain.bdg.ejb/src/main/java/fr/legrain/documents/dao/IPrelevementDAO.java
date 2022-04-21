package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaPrelevementDTO;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;

//@Remote
public interface IPrelevementDAO extends IGenericDAO<TaPrelevement>, IDocumentDAO<TaPrelevement>,
IDocumentTiersStatistiquesDAO<TaPrelevement>,IDocumentTiersEtatDAO<TaPrelevement>,IDashboardDocumentDAO {
	public String genereCode();
	public List<TaPrelevementDTO> findAllLight();
//	public List<TaPrelevementDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
//	public List<TaPrelevementDTO> findPrelevementNonTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaPrelevementDTO> findPrelevementNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<TaPrelevementDTO> findPrelevementTransfosDTO(Date dateDebut, Date dateFin);
	
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
	
//	public long countPrelevement(Date debut, Date fin);
//	public long countPrelevementNonTransforme(Date debut, Date fin);
//	public long countPrelevementNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countPrelevementTransfos(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> caPrelevementNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeARelancerJmaDTO(Date debut, Date fin, int precision);	
}

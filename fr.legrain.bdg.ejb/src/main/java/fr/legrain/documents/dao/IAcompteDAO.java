package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaFacture;
import fr.legrain.tiers.dao.IDocumentDAO;

//@Remote
public interface IAcompteDAO extends IGenericDAO<TaAcompte>,IDocumentDAO<TaAcompte>,IDashboardDocumentDAO {
	public List<TaAcompte> selectAllDisponible(TaFacture taFacture);
	public String genereCode();
	public List<TaAcompteDTO> findAllLight();
	public List<TaAcompte> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaAcompte> rechercheAcompteNonRemises(String codeTiers,Date dateDeb,Date DateFin,Boolean export,String codeTPaiement,String iban,boolean byDate);
	public List<TaAcompteDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) ;
	
//	public List<TaAcompteDTO> findAllDTOPeriode(Date dateDebut, Date dateFin);
//	public List<TaAcompteDTO> findAcompteNonTransfosDTO(Date dateDebut, Date dateFin);
//	public List<TaAcompteDTO> findAcompteNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<TaAcompteDTO> findAcompteTransfosDTO(Date dateDebut, Date dateFin);
	
	
	
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
//	public long countAcompte(Date debut, Date fin);
//	public long countAcompteNonTransforme(Date debut, Date fin);
//	public long countAcompteNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
//	public long countAcompteTransfos(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> caAcompteNonTransfosARelancerDTO(Date dateDebut, Date dateFin, int deltaNbJours);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeJmaDTO(Date debut, Date fin, int precision);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeTotalDTO(Date debut, Date fin);
//	public List<DocumentChiffreAffaireDTO> findChiffreAffaireNonTransformeARelancerJmaDTO(Date debut, Date fin, int precision);
}

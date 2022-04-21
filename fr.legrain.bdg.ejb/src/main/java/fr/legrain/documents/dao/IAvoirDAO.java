package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.DocumentChiffreAffaireDTO;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaFacture;
import fr.legrain.tiers.dao.IDocumentDAO;
import fr.legrain.tiers.dao.IDocumentTiersEtatDAO;
import fr.legrain.tiers.dao.IDocumentTiersStatistiquesDAO;
import fr.legrain.tiers.model.TaTiers;

//@Remote
public interface IAvoirDAO extends IGenericDAO<TaAvoir>,IDocumentDAO<TaAvoir>,IDocumentTiersStatistiquesDAO<TaAvoir>,IDocumentTiersEtatDAO<TaAvoir>,IDashboardDocumentDAO {
	public List<TaAvoir> selectAllDisponible(TaFacture taFacture);
	public List<TaAvoirDTO> findAllLight();
	public List<TaAvoir> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaAvoir> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	public List<TaAvoirDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) ;
	
//	public long countDocument(Date debut, Date fin);
//	public long countDocumentTransforme(Date debut, Date fin);
//	public long countDocumentNonTransforme(Date debut, Date fin);
//	public long countDocumentNonTransformeARelancer(Date debut, Date fin, int deltaNbJours);
	
	public List<DocumentChiffreAffaireDTO> findChiffreAffaireTotalDTO(Date dateDebut, Date dateFin);
	
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiers(Date dateDebut, Date dateFin, String codeArticle, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUnite(Date dateDebut, Date dateFin, String codeArticle, String codeUnite, String codeTiers, String orderBy);
	public List<DocumentChiffreAffaireDTO> listLigneArticleDTOTiersUniteFamille(Date dateDebut, Date dateFin, String codeArticle, String codeUnite,String codeFamille, String codeTiers, String orderBy);
	
	
	
	
	public List<TaAvoir> selectReglementNonLieAuDocument(TaFactureDTO taDocument,Date dateDeb,Date dateFin);
	public List<TaAvoir> selectAllLieAuDocument(TaFactureDTO taDocument,Date dateDeb,Date dateFin);
	public int selectCountDisponible(TaTiers taTiers);
	public List<TaAvoirDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin);
	public List<TaAvoirDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, String codeTiers, Boolean export);
	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin);
}

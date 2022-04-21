package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.dto.TaAvoirDTO;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.tiers.dao.IDocumentDAO;

//@Remote
public interface IApporteurDAO extends IGenericDAO<TaApporteur>,IDocumentDAO<TaApporteur>, IDashboardDocumentDAO {
	public List<TaApporteur> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaApporteur> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	public List<TaApporteurDTO> rechercheDocumentNonExporteLight(Date dateDeb, Date dateFin,boolean parDate) ;
	public List<TaApporteurDTO> findAllLight() ;
	public List<TaApporteurDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin);
	public List<TaApporteurDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, String codeTiers, Boolean export);
	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin);
}

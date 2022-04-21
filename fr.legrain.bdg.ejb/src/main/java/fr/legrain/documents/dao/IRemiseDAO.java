package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaRemise;
import fr.legrain.tiers.dao.IDocumentDAO;

//@Remote
public interface IRemiseDAO extends IGenericDAO<TaRemise>,IDocumentDAO<TaRemise> {

	public List<TaRemise> findSiReglementDansRemise(String code);
	public List<TaRemise> findSiAcompteDansRemise(String code);
	public List<TaRemise> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaRemise> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin);
	public List<TaRemiseDTO> RechercheDocumentDTO(String codeDeb,String codeFin);
	public TaRemiseDTO findByCodeDTO(String code);
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin);
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, String codeTiers, Boolean export);
	public List<TaRemiseDTO> rechercheDocumentNonExporteDTO(Date dateDeb, Date dateFin, boolean parDate);
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, boolean parDate);
	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin);
}

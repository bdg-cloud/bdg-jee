package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaRemise;

@Remote
public interface ITaRemiseServiceRemote extends IGenericCRUDServiceRemote<TaRemise,TaRemiseDTO>,
														IAbstractLgrDAOServer<TaRemise>,IAbstractLgrDAOServerDTO<TaRemiseDTO>,
														IDocumentService<TaRemise>{
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	public List<TaRemise> findSiReglementDansRemise(String code);
	public List<TaRemise> findSiAcompteDansRemise(String code);
	public List<TaRemise> rechercheDocumentNonExporte(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaRemise> rechercheDocumentOrderByDate(Date dateDeb, Date dateFin);
	
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin);
	public List<TaRemiseDTO> RechercheDocumentDTO(String codeDeb,String codeFin);
	public TaRemiseDTO findByCodeDTO(String code)throws FinderException;
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateExport, String codeTiers, Date dateDeb, Date dateFin);
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, String codeTiers, Boolean export);
	public List<TaRemiseDTO> rechercheDocumentNonExporteDTO(Date dateDeb, Date dateFin,boolean parDate);
	public List<TaRemiseDTO> rechercheDocumentDTO(Date dateDeb, Date dateFin, boolean parDate);
	public Date selectMinDateDocumentNonExporte(Date dateDeb, Date dateFin);
	
}

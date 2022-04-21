package fr.legrain.documents.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.dto.TaRReglementDTO;
import fr.legrain.document.model.TaRReglement;
import fr.legrain.tiers.model.TaTiers;

//@Remote
public interface IRReglementDAO extends IGenericDAO<TaRReglement> {

	List<TaRReglement> selectAll(TaTiers taTiers, Date dateDeb, Date dateFin);
	public List<TaRReglementDTO> rechercheDocumentDTO(String codeDoc, String codeTiers);
	List<Date> findDateExport(Date dateDeb,Date dateFin);
}

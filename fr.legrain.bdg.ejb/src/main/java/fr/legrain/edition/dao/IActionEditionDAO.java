package fr.legrain.edition.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.edition.dto.TaActionEditionDTO;
import fr.legrain.edition.model.TaActionEdition;

//@Remote
public interface IActionEditionDAO extends IGenericDAO<TaActionEdition> {
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight();
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight(Date debut, Date fin);
	
//	public List<TaMessageEmailDTO> findAllLight();
//	public List<TaMessageEmail> selectAll(int idTiers);
	public List<TaActionEditionDTO> findAllByIdEdtionDTO(int idEdition);
}

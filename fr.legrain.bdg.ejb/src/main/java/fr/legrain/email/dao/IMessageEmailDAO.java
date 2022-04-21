package fr.legrain.email.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.email.dto.TaMessageEmailDTO;
import fr.legrain.email.model.TaMessageEmail;

//@Remote
public interface IMessageEmailDAO extends IGenericDAO<TaMessageEmail> {
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight();
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight(Date debut, Date fin);
	
	public List<TaMessageEmail> selectAll(int idTiers);
	
	public List<TaMessageEmailDTO> findAllLight(int idTiers);
	public List<TaMessageEmailDTO> findAllLight();
}

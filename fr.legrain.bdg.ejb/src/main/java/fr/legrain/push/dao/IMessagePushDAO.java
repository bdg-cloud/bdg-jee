package fr.legrain.push.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.push.dto.TaMessagePushDTO;
import fr.legrain.push.model.TaMessagePush;

//@Remote
public interface IMessagePushDAO extends IGenericDAO<TaMessagePush> {
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight();
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight(Date debut, Date fin);
	
	public List<TaMessagePushDTO> findAllLight();
	public List<TaMessagePush> selectAll(int idTiers);
}

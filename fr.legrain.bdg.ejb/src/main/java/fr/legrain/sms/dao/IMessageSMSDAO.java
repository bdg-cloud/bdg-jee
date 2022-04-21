package fr.legrain.sms.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.sms.dto.TaMessageSMSDTO;
import fr.legrain.sms.model.TaMessageSMS;

//@Remote
public interface IMessageSMSDAO extends IGenericDAO<TaMessageSMS> {
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight();
//	public List<TaLogPaiementInternetDTO> selectAllDTOLight(Date debut, Date fin);
	
	public List<TaMessageSMSDTO> findAllLight();
	public List<TaMessageSMS> selectAll(int idTiers);
}

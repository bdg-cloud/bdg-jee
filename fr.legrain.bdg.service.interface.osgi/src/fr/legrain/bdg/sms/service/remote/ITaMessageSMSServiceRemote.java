package fr.legrain.bdg.sms.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.sms.dto.TaMessageSMSDTO;
import fr.legrain.sms.model.TaMessageSMS;

@Remote
public interface ITaMessageSMSServiceRemote extends IGenericCRUDServiceRemote<TaMessageSMS,TaMessageSMSDTO>,
														IAbstractLgrDAOServer<TaMessageSMS>,IAbstractLgrDAOServerDTO<TaMessageSMSDTO>{
	public static final String validationContext = "MESSAGE_SMS";
	
//	public List<TaMessageSMSDTO> selectAllDTOLight();
//	public List<TaMessageSMSDTO> selectAllDTOLight(Date debut, Date fin);
	
	public List<TaMessageSMSDTO> findAllLight();
	
	public List<TaMessageSMS> selectAll(int idTiers);
	public List<TaMessageSMSDTO> selectAllDTO(int idTiers);
	
}

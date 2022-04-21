package fr.legrain.bdg.push.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.push.dto.TaMessagePushDTO;
import fr.legrain.push.model.TaMessagePush;

@Remote
public interface ITaMessagePushServiceRemote extends IGenericCRUDServiceRemote<TaMessagePush,TaMessagePushDTO>,
														IAbstractLgrDAOServer<TaMessagePush>,IAbstractLgrDAOServerDTO<TaMessagePushDTO>{
	public static final String validationContext = "MESSAGE_PUSH";
	
//	public List<TaMessagePushDTO> selectAllDTOLight();
//	public List<TaMessagePushDTO> selectAllDTOLight(Date debut, Date fin);
	
	public List<TaMessagePushDTO> findAllLight();
	
	public List<TaMessagePush> selectAll(int idTiers);
	public List<TaMessagePushDTO> selectAllDTO(int idTiers);
	
}

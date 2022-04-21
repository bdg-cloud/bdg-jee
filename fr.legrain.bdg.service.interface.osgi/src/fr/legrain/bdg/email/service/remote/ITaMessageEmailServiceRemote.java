package fr.legrain.bdg.email.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.email.dto.TaMessageEmailDTO;
import fr.legrain.email.model.TaMessageEmail;

@Remote
public interface ITaMessageEmailServiceRemote extends IGenericCRUDServiceRemote<TaMessageEmail,TaMessageEmailDTO>,
														IAbstractLgrDAOServer<TaMessageEmail>,IAbstractLgrDAOServerDTO<TaMessageEmailDTO>{
	public static final String validationContext = "MESSAGE_EMAIL";
	
//	public List<TaMessageEmailDTO> selectAllDTOLight();
//	public List<TaMessageEmailDTO> selectAllDTOLight(Date debut, Date fin);
		
	public List<TaMessageEmail> selectAll(int idTiers);
	public List<TaMessageEmailDTO> selectAllDTO(int idTiers);
	
	public List<TaMessageEmailDTO> findAllLight(int idTiers);
	public List<TaMessageEmailDTO> findAllLight();
	
}

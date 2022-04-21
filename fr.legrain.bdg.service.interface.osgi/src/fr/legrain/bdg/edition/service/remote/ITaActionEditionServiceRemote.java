package fr.legrain.bdg.edition.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.edition.dto.TaActionEditionDTO;
import fr.legrain.edition.model.TaActionEdition;


@Remote
public interface ITaActionEditionServiceRemote extends IGenericCRUDServiceRemote<TaActionEdition,TaActionEditionDTO>,
														IAbstractLgrDAOServer<TaActionEdition>,IAbstractLgrDAOServerDTO<TaActionEditionDTO>{
	public static final String validationContext = "ACTION_EDITION";
	
//	public List<TaTServiceWebExterneDTO> findByCodeLight(String code);
	public List<TaActionEditionDTO> findAllByIdEdtionDTO(int idEdition);
}

package fr.legrain.bdg.edition.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.edition.dto.TaActionInterneDTO;
import fr.legrain.edition.model.TaActionInterne;


@Remote
public interface ITaActionInterneServiceRemote extends IGenericCRUDServiceRemote<TaActionInterne,TaActionInterneDTO>,
														IAbstractLgrDAOServer<TaActionInterne>,IAbstractLgrDAOServerDTO<TaActionInterneDTO>{
	public static final String validationContext = "ACTION_INTERNE";
	
//	public List<TaTServiceWebExterneDTO> findByCodeLight(String code);
}

package fr.legrain.bdg.edition.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.edition.dto.TaActionInterneDTO;
import fr.legrain.edition.dto.TaActionInterneProgDTO;
import fr.legrain.edition.model.TaActionInterne;
import fr.legrain.edition.model.TaActionInterneProg;


@Remote
public interface ITaActionInterneProgServiceRemote extends IGenericCRUDServiceRemote<TaActionInterneProg,TaActionInterneProgDTO>,
														IAbstractLgrDAOServer<TaActionInterneProg>,IAbstractLgrDAOServerDTO<TaActionInterneProgDTO>{
	public static final String validationContext = "ACTION_INTERNE_PROG";
	
//	public List<TaTServiceWebExterneDTO> findByCodeLight(String code);
}

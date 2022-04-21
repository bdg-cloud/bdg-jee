package fr.legrain.bdg.general.service.remote;

import javax.ejb.Remote;

import fr.legrain.controle.dto.TaVerrouModificationDTO;
import fr.legrain.controle.model.TaVerrouModification;

@Remote
public interface ITaVerrouModificationServiceRemote extends IGenericCRUDServiceRemote<TaVerrouModification,TaVerrouModificationDTO>,
														IAbstractLgrDAOServer<TaVerrouModification>,IAbstractLgrDAOServerDTO<TaVerrouModificationDTO>{
	public static final String validationContext = "VERROU_MODIFICATION";
}

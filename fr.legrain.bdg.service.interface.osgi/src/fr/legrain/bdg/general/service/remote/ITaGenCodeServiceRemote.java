package fr.legrain.bdg.general.service.remote;

import javax.ejb.Remote;

import fr.legrain.controle.dto.TaGenCodeDTO;
import fr.legrain.controle.model.TaGenCode;

@Remote
public interface ITaGenCodeServiceRemote extends IGenericCRUDServiceRemote<TaGenCode,TaGenCodeDTO>,
														IAbstractLgrDAOServer<TaGenCode>,IAbstractLgrDAOServerDTO<TaGenCodeDTO>{
	public static final String validationContext = "GEN_CODE";
}

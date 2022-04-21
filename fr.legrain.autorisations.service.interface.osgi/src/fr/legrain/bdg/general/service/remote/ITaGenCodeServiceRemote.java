package fr.legrain.bdg.general.service.remote;

import javax.ejb.Remote;

import fr.legrain.autorisations.controle.dto.TaGenCodeDTO;
import fr.legrain.autorisations.controle.model.TaGenCode;



@Remote
public interface ITaGenCodeServiceRemote extends IGenericCRUDServiceRemote<TaGenCode,TaGenCodeDTO>,
														IAbstractLgrDAOServer<TaGenCode>,IAbstractLgrDAOServerDTO<TaGenCodeDTO>{

}

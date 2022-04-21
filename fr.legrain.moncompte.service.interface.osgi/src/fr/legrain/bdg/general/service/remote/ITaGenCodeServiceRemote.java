package fr.legrain.bdg.general.service.remote;

import javax.ejb.Remote;

import fr.legrain.moncompte.controle.dto.TaGenCodeDTO;
import fr.legrain.moncompte.controle.model.TaGenCode;



@Remote
public interface ITaGenCodeServiceRemote extends IGenericCRUDServiceRemote<TaGenCode,TaGenCodeDTO>,
														IAbstractLgrDAOServer<TaGenCode>,IAbstractLgrDAOServerDTO<TaGenCodeDTO>{

}

package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTReceptionDTO;
import fr.legrain.article.model.TaTReception;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTiersDTO;

@Remote
public interface ITaTReceptionServiceRemote extends IGenericCRUDServiceRemote<TaTReception,TaTReceptionDTO>,
														IAbstractLgrDAOServer<TaTReception>,IAbstractLgrDAOServerDTO<TaTReceptionDTO>{
	public static final String validationContext = "T_RECEPTION";
	
	public List<TaTReceptionDTO> findByCodeLight(String code);
}

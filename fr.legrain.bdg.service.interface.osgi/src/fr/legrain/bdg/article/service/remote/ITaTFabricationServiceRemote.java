package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTFabricationDTO;
import fr.legrain.article.dto.TaTReceptionDTO;
import fr.legrain.article.model.TaTFabrication;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTFabricationServiceRemote extends IGenericCRUDServiceRemote<TaTFabrication,TaTFabricationDTO>,
														IAbstractLgrDAOServer<TaTFabrication>,IAbstractLgrDAOServerDTO<TaTFabricationDTO>{
	public static final String validationContext = "T_FABRICATION";
	
	public List<TaTFabricationDTO> findByCodeLight(String code);
}

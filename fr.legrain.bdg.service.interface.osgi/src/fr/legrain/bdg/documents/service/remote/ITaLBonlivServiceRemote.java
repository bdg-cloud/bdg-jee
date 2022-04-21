package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLBonlivDTO;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaLFacture;

@Remote
public interface ITaLBonlivServiceRemote extends IGenericCRUDServiceRemote<TaLBonliv,TaLBonlivDTO>,
														IAbstractLgrDAOServer<TaLBonliv>,IAbstractLgrDAOServerDTO<TaLBonlivDTO>{
	public static final String validationContext = "L_BONLIV";
}

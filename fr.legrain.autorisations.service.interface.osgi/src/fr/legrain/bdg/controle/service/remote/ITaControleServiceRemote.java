package fr.legrain.bdg.controle.service.remote;

import javax.ejb.Remote;

import fr.legrain.autorisations.controle.dto.TaControleDTO;
import fr.legrain.autorisations.controle.model.TaControle;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaControleServiceRemote extends IGenericCRUDServiceRemote<TaControle,TaControleDTO>,
														IAbstractLgrDAOServer<TaControle>,IAbstractLgrDAOServerDTO<TaControleDTO>{
	public static final String validationContext = "CONTROLE";
}

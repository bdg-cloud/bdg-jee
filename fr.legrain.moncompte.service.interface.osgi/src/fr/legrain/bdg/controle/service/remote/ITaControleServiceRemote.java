package fr.legrain.bdg.controle.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.controle.dto.TaControleDTO;
import fr.legrain.moncompte.controle.model.TaControle;

@Remote
public interface ITaControleServiceRemote extends IGenericCRUDServiceRemote<TaControle,TaControleDTO>,
														IAbstractLgrDAOServer<TaControle>,IAbstractLgrDAOServerDTO<TaControleDTO>{
	public static final String validationContext = "CONTROLE";
}

package fr.legrain.general.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.general.model.TaParametre;
import fr.legrain.general.model.TaParametre;
import fr.legrain.general.dto.TaParametreDTO;;

@Remote
public interface ITaParametreServiceRemote extends IGenericCRUDServiceRemote<TaParametre,TaParametreDTO>,
						IAbstractLgrDAOServer<TaParametre>,IAbstractLgrDAOServerDTO<TaParametreDTO>{
	public static final String validationContext = "TA_PARAMETRE";
	
	public TaParametre findInstance();
	
	public void initParamBdg();
}

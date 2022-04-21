package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaPrixPersoDTO;
import fr.legrain.moncompte.model.TaPrixPerso;


@Remote
public interface ITaPrixPersoServiceRemote extends IGenericCRUDServiceRemote<TaPrixPerso,TaPrixPersoDTO>,
														IAbstractLgrDAOServer<TaPrixPerso>,IAbstractLgrDAOServerDTO<TaPrixPersoDTO>{
	public static final String validationContext = "PRIX_PERSO";
}

package fr.legrain.general.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.general.model.TaFichier;
import fr.legrain.general.dto.TaFichierDTO;;

@Remote
public interface ITaFichierServiceRemote extends IGenericCRUDServiceRemote<TaFichier,TaFichierDTO>,
						IAbstractLgrDAOServer<TaFichier>,IAbstractLgrDAOServerDTO<TaFichierDTO>{
	public static final String validationContext = "TA_FICHIER";
}

package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaAdresseDTO;
import fr.legrain.moncompte.dto.TaCgvDTO;
import fr.legrain.moncompte.model.TaAdresse;
import fr.legrain.moncompte.model.TaCgv;


@Remote
public interface ITaCgvServiceRemote extends IGenericCRUDServiceRemote<TaCgv,TaCgvDTO>,
														IAbstractLgrDAOServer<TaCgv>,IAbstractLgrDAOServerDTO<TaCgvDTO>{
	public static final String validationContext = "CGV";
	
	public TaCgv findCgvCourant();

}

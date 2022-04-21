package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaAdresseDTO;
import fr.legrain.moncompte.dto.TaCategorieProDTO;
import fr.legrain.moncompte.model.TaAdresse;
import fr.legrain.moncompte.model.TaCategoriePro;


@Remote
public interface ITaCategorieProServiceRemote extends IGenericCRUDServiceRemote<TaCategoriePro,TaCategorieProDTO>,
														IAbstractLgrDAOServer<TaCategoriePro>,IAbstractLgrDAOServerDTO<TaCategorieProDTO>{
	public static final String validationContext = "CATEGORIE_PRO";

}

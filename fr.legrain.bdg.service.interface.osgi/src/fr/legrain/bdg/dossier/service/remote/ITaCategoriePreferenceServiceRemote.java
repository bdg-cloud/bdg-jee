package fr.legrain.bdg.dossier.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.dossier.dto.TaCategoriePreferenceDTO;
import fr.legrain.dossier.model.TaCategoriePreference;

@Remote
public interface ITaCategoriePreferenceServiceRemote extends IGenericCRUDServiceRemote<TaCategoriePreference,TaCategoriePreferenceDTO>,
														IAbstractLgrDAOServer<TaCategoriePreference>,IAbstractLgrDAOServerDTO<TaCategoriePreferenceDTO>{
	public static final String validationContext = "CATEGORIE_PREFERENCE";
	
	
}

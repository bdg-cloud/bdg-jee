package fr.legrain.bdg.dossier.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.dossier.dto.TaGroupePreferenceDTO;
import fr.legrain.dossier.dto.TaPreferencesDTO;
import fr.legrain.dossier.model.TaGroupePreference;
import fr.legrain.dossier.model.TaPreferences;

@Remote
public interface ITaGroupePreferenceServiceRemote extends IGenericCRUDServiceRemote<TaGroupePreference,TaGroupePreferenceDTO>,
														IAbstractLgrDAOServer<TaGroupePreference>,IAbstractLgrDAOServerDTO<TaGroupePreferenceDTO>{
	public static final String validationContext = "GROUPE_PREFERENCE";
	
	
}

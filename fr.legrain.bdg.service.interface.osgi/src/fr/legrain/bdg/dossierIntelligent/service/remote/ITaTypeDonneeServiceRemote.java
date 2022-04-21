package fr.legrain.bdg.dossierIntelligent.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.dossierIntelligent.dto.TaTypeDonneeDTO;
import fr.legrain.dossierIntelligent.model.TaTypeDonnee;

@Remote
public interface ITaTypeDonneeServiceRemote extends IGenericCRUDServiceRemote<TaTypeDonnee,TaTypeDonneeDTO>,
														IAbstractLgrDAOServer<TaTypeDonnee>,IAbstractLgrDAOServerDTO<TaTypeDonneeDTO>{
	public static final String validationContext = "TYPE_DONNEE";
	
	
}

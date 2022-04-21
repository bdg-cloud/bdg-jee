package fr.legrain.bdg.tache.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tache.dto.TaCategorieEvenementDTO;
import fr.legrain.tache.model.TaCategorieEvenement;

@Remote
public interface ITaCategorieEvenementServiceRemote extends IGenericCRUDServiceRemote<TaCategorieEvenement,TaCategorieEvenementDTO>,
														IAbstractLgrDAOServer<TaCategorieEvenement>,IAbstractLgrDAOServerDTO<TaCategorieEvenementDTO>{
	public static final String validationContext = "CATEGORIE_EVENEMENT";
}

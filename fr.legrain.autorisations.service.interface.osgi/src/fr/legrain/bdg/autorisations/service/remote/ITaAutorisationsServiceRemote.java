package fr.legrain.bdg.autorisations.service.remote;

import javax.ejb.Remote;

import fr.legrain.autorisation.xml.ListeModules;
import fr.legrain.autorisations.autorisation.dto.TaAutorisationsDTO;
import fr.legrain.autorisations.autorisation.model.TaAutorisations;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaAutorisationsServiceRemote extends IGenericCRUDServiceRemote<TaAutorisations,TaAutorisationsDTO>,
														IAbstractLgrDAOServer<TaAutorisations>,IAbstractLgrDAOServerDTO<TaAutorisationsDTO>{
	public static final String validationContext = "AUTORISATIONS";
	
	public ListeModules findByDossierTypeProduit(String codeDossier, Integer idTypeProduit);
}

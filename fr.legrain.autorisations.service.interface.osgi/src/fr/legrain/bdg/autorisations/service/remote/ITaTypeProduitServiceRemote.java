package fr.legrain.bdg.autorisations.service.remote;

import javax.ejb.Remote;

import fr.legrain.autorisations.autorisation.dto.TaTypeProduitDTO;
import fr.legrain.autorisations.autorisation.model.TaTypeProduit;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTypeProduitServiceRemote extends IGenericCRUDServiceRemote<TaTypeProduit,TaTypeProduitDTO>,
														IAbstractLgrDAOServer<TaTypeProduit>,IAbstractLgrDAOServerDTO<TaTypeProduitDTO>{
	public static final String validationContext = "TYPE_PRODUIT";
}

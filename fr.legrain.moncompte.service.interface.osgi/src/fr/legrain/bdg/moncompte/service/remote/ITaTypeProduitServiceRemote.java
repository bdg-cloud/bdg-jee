package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaTypeProduitDTO;
import fr.legrain.moncompte.model.TaTypeProduit;


@Remote
public interface ITaTypeProduitServiceRemote extends IGenericCRUDServiceRemote<TaTypeProduit,TaTypeProduitDTO>,
														IAbstractLgrDAOServer<TaTypeProduit>,IAbstractLgrDAOServerDTO<TaTypeProduitDTO>{
	public static final String validationContext = "TYPE_PRODUIT";

}

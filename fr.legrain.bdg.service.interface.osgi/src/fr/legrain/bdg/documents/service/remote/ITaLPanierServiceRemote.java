package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.model.TaLPanier;

@Remote
public interface ITaLPanierServiceRemote extends IGenericCRUDServiceRemote<TaLPanier,TaLPanierDTO>,
														IAbstractLgrDAOServer<TaLPanier>,IAbstractLgrDAOServerDTO<TaLPanierDTO>{
	public static final String validationContext = "L_PANIER";
}

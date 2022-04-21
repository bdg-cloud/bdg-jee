package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLDevisDTO;
import fr.legrain.document.model.TaLDevis;

@Remote
public interface ITaLDevisServiceRemote extends IGenericCRUDServiceRemote<TaLDevis,TaLDevisDTO>,
														IAbstractLgrDAOServer<TaLDevis>,IAbstractLgrDAOServerDTO<TaLDevisDTO>{
	public static final String validationContext = "L_DEVIS";
}

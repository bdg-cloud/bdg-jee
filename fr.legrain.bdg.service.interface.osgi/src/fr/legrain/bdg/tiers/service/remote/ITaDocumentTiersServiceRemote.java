package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaDocumentTiersDTO;
import fr.legrain.tiers.model.TaDocumentTiers;

@Remote
public interface ITaDocumentTiersServiceRemote extends IGenericCRUDServiceRemote<TaDocumentTiers,TaDocumentTiersDTO>,
														IAbstractLgrDAOServer<TaDocumentTiers>,IAbstractLgrDAOServerDTO<TaDocumentTiersDTO>{
	
	public static final String validationContext = "DOCUMENT_TIERS";
	
	public void RAZCheminVersion(String typeLogiciel);
}

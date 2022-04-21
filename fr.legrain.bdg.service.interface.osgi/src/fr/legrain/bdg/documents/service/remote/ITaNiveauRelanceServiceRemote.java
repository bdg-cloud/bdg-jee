package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaNiveauRelanceDTO;
import fr.legrain.document.model.TaTRelance;

@Remote
public interface ITaNiveauRelanceServiceRemote extends IGenericCRUDServiceRemote<TaTRelance,TaNiveauRelanceDTO>,
														IAbstractLgrDAOServer<TaTRelance>,IAbstractLgrDAOServerDTO<TaNiveauRelanceDTO>{
	public static final String validationContext = "NIVEAU_RELANCE";
}

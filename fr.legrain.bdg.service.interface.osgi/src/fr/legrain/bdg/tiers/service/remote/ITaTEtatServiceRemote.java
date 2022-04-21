package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaTEtatDTO;
import fr.legrain.document.model.TaTEtat;

@Remote
public interface ITaTEtatServiceRemote extends IGenericCRUDServiceRemote<TaTEtat,TaTEtatDTO>,
														IAbstractLgrDAOServer<TaTEtat>,IAbstractLgrDAOServerDTO<TaTEtatDTO>{
	
}

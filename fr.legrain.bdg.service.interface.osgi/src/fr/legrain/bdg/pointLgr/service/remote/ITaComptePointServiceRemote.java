package fr.legrain.bdg.pointLgr.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.pointLgr.dto.ComptePointDTO;
import fr.legrain.pointLgr.model.TaComptePoint;

@Remote
public interface ITaComptePointServiceRemote extends IGenericCRUDServiceRemote<TaComptePoint,ComptePointDTO>,
														IAbstractLgrDAOServer<TaComptePoint>,IAbstractLgrDAOServerDTO<ComptePointDTO>{
	
	public void removeTousLesPointsBonus(IDocumentTiers persistentInstance,boolean message,boolean bundleExist) throws Exception;
	
	
}

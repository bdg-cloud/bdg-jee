package fr.legrain.bdg.pointLgr.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.pointLgr.dto.PourcGroupeDTO;
import fr.legrain.pointLgr.model.TaPourcGroupe;

@Remote
public interface TaPourcGroupServiceRemote extends IGenericCRUDServiceRemote<TaPourcGroupe,PourcGroupeDTO>,
														IAbstractLgrDAOServer<TaPourcGroupe>,IAbstractLgrDAOServerDTO<PourcGroupeDTO>{
}

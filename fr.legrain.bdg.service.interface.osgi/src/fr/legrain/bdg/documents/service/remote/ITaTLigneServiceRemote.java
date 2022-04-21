package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.bdg.model.ObjectDTO;
import fr.legrain.document.model.TaTLigne;

@Remote
public interface ITaTLigneServiceRemote extends IGenericCRUDServiceRemote<TaTLigne,ObjectDTO>,
														IAbstractLgrDAOServer<TaTLigne>,IAbstractLgrDAOServerDTO<ObjectDTO>{
}

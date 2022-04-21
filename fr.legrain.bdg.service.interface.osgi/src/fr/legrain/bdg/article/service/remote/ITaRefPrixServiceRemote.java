package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaRefPrixDTO;
import fr.legrain.article.model.TaRefPrix;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaRefPrixServiceRemote extends IGenericCRUDServiceRemote<TaRefPrix,TaRefPrixDTO>,
														IAbstractLgrDAOServer<TaRefPrix>,IAbstractLgrDAOServerDTO<TaRefPrixDTO>{
}

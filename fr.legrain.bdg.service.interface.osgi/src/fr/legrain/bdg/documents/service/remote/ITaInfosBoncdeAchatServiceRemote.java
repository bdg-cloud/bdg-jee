package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaBoncdeAchatDTO;
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.model.TaInfosBoncde;
import fr.legrain.document.model.TaInfosBoncdeAchat;

@Remote
public interface ITaInfosBoncdeAchatServiceRemote extends IGenericCRUDServiceRemote<TaInfosBoncdeAchat,TaBoncdeAchatDTO>,
														IAbstractLgrDAOServer<TaInfosBoncdeAchat>,IAbstractLgrDAOServerDTO<TaBoncdeAchatDTO>{
	public TaInfosBoncdeAchat findByCodeBoncde(String code);

}

package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaCodeBarreDTO;
import fr.legrain.article.model.TaCodeBarre;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaCodeBarreServiceRemote extends IGenericCRUDServiceRemote<TaCodeBarre,TaCodeBarreDTO>,
														IAbstractLgrDAOServer<TaCodeBarre>,IAbstractLgrDAOServerDTO<TaCodeBarreDTO>{
	public static final String validationContext = "CODEBBARRE";
}

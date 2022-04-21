package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTypeCodeBarreDTO;
import fr.legrain.article.model.TaTypeCodeBarre;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITypeCodeBarreServiceRemote extends IGenericCRUDServiceRemote<TaTypeCodeBarre,TaTypeCodeBarreDTO>,
														IAbstractLgrDAOServer<TaTypeCodeBarre>,IAbstractLgrDAOServerDTO<TaTypeCodeBarreDTO>{
	public static final String validationContext = "TYPECODEBBARRE";
}

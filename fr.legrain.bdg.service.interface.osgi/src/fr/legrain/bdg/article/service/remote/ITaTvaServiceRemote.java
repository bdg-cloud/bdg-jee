package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.model.TaTva;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTvaServiceRemote extends IGenericCRUDServiceRemote<TaTva,TaTvaDTO>,
IAbstractLgrDAOServer<TaTva>,IAbstractLgrDAOServerDTO<TaTvaDTO>{
	public static final String validationContext = "TVA";
	
	public List<TaTvaDTO> findByCodeLight(String code);
}

package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.model.TaTTva;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTTvaServiceRemote extends IGenericCRUDServiceRemote<TaTTva,TaTTvaDTO>,
														IAbstractLgrDAOServer<TaTTva>,IAbstractLgrDAOServerDTO<TaTTvaDTO>{
	public static final String validationContext = "TTVA";
	
	public List<TaTTvaDTO> findByCodeLight(String code);
}

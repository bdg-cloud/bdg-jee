package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTConditionnementDTO;
import fr.legrain.article.model.TaTConditionnement;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTConditionnementServiceRemote extends IGenericCRUDServiceRemote<TaTConditionnement,TaTConditionnementDTO>,
														IAbstractLgrDAOServer<TaTConditionnement>,IAbstractLgrDAOServerDTO<TaTConditionnementDTO>{
	public static final String validationContext = "T_CONDITIONNEMENT";
}

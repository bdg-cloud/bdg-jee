package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaFamilleUniteDTO;
import fr.legrain.article.model.TaFamilleUnite;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaFamilleUniteServiceRemote extends IGenericCRUDServiceRemote<TaFamilleUnite,TaFamilleUniteDTO>,
														IAbstractLgrDAOServer<TaFamilleUnite>,IAbstractLgrDAOServerDTO<TaFamilleUniteDTO>{
	public static final String validationContext = "FAMILLE_UNITE";
}

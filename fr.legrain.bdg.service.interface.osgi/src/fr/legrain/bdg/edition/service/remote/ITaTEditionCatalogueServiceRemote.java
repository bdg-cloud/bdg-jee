package fr.legrain.bdg.edition.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.edition.dto.TaTEditionCatalogueDTO;
import fr.legrain.edition.model.TaTEditionCatalogue;


@Remote
public interface ITaTEditionCatalogueServiceRemote extends IGenericCRUDServiceRemote<TaTEditionCatalogue,TaTEditionCatalogueDTO>,
														IAbstractLgrDAOServer<TaTEditionCatalogue>,IAbstractLgrDAOServerDTO<TaTEditionCatalogueDTO>{
	public static final String validationContext = "T_EDITION_CATALOGUE";
	
//	public List<TaTServiceWebExterneDTO> findByCodeLight(String code);
}

package fr.legrain.bdg.edition.service.remote;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.edition.dto.TaEditionCatalogueDTO;
import fr.legrain.edition.model.TaEditionCatalogue;



@Remote
public interface ITaEditionCatalogueServiceRemote extends IGenericCRUDServiceRemote<TaEditionCatalogue,TaEditionCatalogueDTO>,
														IAbstractLgrDAOServer<TaEditionCatalogue>,IAbstractLgrDAOServerDTO<TaEditionCatalogueDTO>{
	public static final String validationContext = "EDITION_CATALOGUE";
	
	public List<TaEditionCatalogueDTO> findAllLight();
	public TaEditionCatalogueDTO findByCodeLight(String code);
	
	public List<TaEditionCatalogueDTO> rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImportees);
	public List<TaEditionCatalogueDTO> rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, Map<String,String> mapCodeEditionDejaImporteesVersion);
	
}

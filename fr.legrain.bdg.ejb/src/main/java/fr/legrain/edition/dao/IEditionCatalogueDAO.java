package fr.legrain.edition.dao;

import java.util.List;
import java.util.Map;

import fr.legrain.data.IGenericDAO;
import fr.legrain.edition.dto.TaEditionCatalogueDTO;
import fr.legrain.edition.model.TaEditionCatalogue;

//@Remote
public interface IEditionCatalogueDAO extends IGenericDAO<TaEditionCatalogue> {

	
	public List<TaEditionCatalogueDTO> findAllLight();
	public TaEditionCatalogueDTO findByCodeLight(String code);
	
	public List<TaEditionCatalogueDTO> rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImportees);
	public List<TaEditionCatalogueDTO> rechercheEditionDisponible(String codeDossier, String codeTypeEdition, String idAction, Map<String,String> mapCodeEditionDejaImporteesVersion);

}

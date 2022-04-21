package fr.legrain.edition.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaEdition;
import fr.legrain.email.dto.TaMessageEmailDTO;
import fr.legrain.email.model.TaMessageEmail;

//@Remote
public interface IEditionDAO extends IGenericDAO<TaEdition> {

	
	public List<TaEditionDTO> findAllLight();
	public TaEditionDTO findByCodeLight(String code);
	
	public List<TaEditionDTO> findByIdTypeDTO(int idTEdition);
	public List<TaEditionDTO> findByCodeTypeDTO(String codeTEdition);
	
	public List<TaEdition> findByIdType(int idTEdition);
	public List<TaEdition> findByCodeType(String codeTEdition);
	
	public List<TaEdition> rechercheEditionDisponible(String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImportees);
//	public List<TaEditionDTO> rechercheEditionDisponibleDTO(String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImportees);
//	
//	public List<String> rechercheCodeEditionDejaImporte(String codeTypeEdition, String idActions);
//	public List<TaEdition> rechercheEditionDejaImporte(String codeTypeEdition, String idActions);
//	public List<TaEditionDTO> rechercheEditionDejaImporteDTO(String codeTypeEdition, String idActions);
}

package fr.legrain.bdg.edition.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.edition.dto.TaEditionDTO;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.edition.model.TaEdition;



@Remote
public interface ITaEditionServiceRemote extends IGenericCRUDServiceRemote<TaEdition,TaEditionDTO>,
														IAbstractLgrDAOServer<TaEdition>,IAbstractLgrDAOServerDTO<TaEditionDTO>{
	public static final String validationContext = "EDITION";
	
	public List<TaEditionDTO> findAllLight();
	public TaEditionDTO findByCodeLight(String code);
	
	public List<TaEditionDTO> findByIdTypeDTO(int idTEdition);
	public List<TaEditionDTO> findByCodeTypeDTO(String codeTEdition);
	
	public List<TaEdition> findByIdType(int idTEdition);
	public List<TaEdition> findByCodeType(String codeTEdition);
	
	public List<TaEditionDTO> findByCodeTypeDTOAvecActionsEdition(String codeTEdition);
	
	public List<TaEdition> rechercheEditionDisponible(String codeTypeEdition, String idAction, List<String> listeCodeEditionDejaImportees);
	public List<TaEdition> rechercheEditionDisponibleProgrammeDefaut(String codeTypeEdition, String idAction);
	public TaEdition rechercheEditionActionDefaut(TaEdition edition, TaActionEdition action, String typeDoc);
	
}

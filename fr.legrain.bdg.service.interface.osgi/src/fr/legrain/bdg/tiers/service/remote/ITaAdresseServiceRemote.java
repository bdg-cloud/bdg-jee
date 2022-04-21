package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaAdresseDTO;
import fr.legrain.tiers.model.TaAdresse;

@Remote
public interface ITaAdresseServiceRemote extends IGenericCRUDServiceRemote<TaAdresse,TaAdresseDTO>,
														IAbstractLgrDAOServer<TaAdresse>,IAbstractLgrDAOServerDTO<TaAdresseDTO>{
	public static final String validationContext = "ADRESSE";
	
	public List<TaAdresse> rechercheParType(String codeType);
	public List<TaAdresse> rechercheParImport(String origineImport, String idImport);
	public int rechercheOdrePourType(String codeType,String codeTiers);
}

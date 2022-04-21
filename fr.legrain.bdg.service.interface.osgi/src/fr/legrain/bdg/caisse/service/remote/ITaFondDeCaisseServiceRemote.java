package fr.legrain.bdg.caisse.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.caisse.dto.TaFondDeCaisseDTO;
import fr.legrain.caisse.model.TaFondDeCaisse;

@Remote
public interface ITaFondDeCaisseServiceRemote extends IGenericCRUDServiceRemote<TaFondDeCaisse,TaFondDeCaisseDTO>,
														IAbstractLgrDAOServer<TaFondDeCaisse>,IAbstractLgrDAOServerDTO<TaFondDeCaisseDTO>{
	public static final String validationContext = "FOND_DE_CAISSE";
	
	public List<TaFondDeCaisseDTO> findByCodeLight(String code);
}

package fr.legrain.bdg.caisse.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.caisse.dto.TaTFondDeCaisseDTO;
import fr.legrain.caisse.model.TaTFondDeCaisse;

@Remote
public interface ITaTFondDeCaisseServiceRemote extends IGenericCRUDServiceRemote<TaTFondDeCaisse,TaTFondDeCaisseDTO>,
														IAbstractLgrDAOServer<TaTFondDeCaisse>,IAbstractLgrDAOServerDTO<TaTFondDeCaisseDTO>{
	public static final String validationContext = "T_FOND_DE_CAISSE";
	
	public List<TaTFondDeCaisseDTO> findByCodeLight(String code);
}

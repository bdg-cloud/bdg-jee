package fr.legrain.bdg.caisse.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.caisse.dto.TaDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaDepotRetraitCaisse;

@Remote
public interface ITaDepotRetraitCaisseServiceRemote extends IGenericCRUDServiceRemote<TaDepotRetraitCaisse,TaDepotRetraitCaisseDTO>,
														IAbstractLgrDAOServer<TaDepotRetraitCaisse>,IAbstractLgrDAOServerDTO<TaDepotRetraitCaisseDTO>{
	public static final String validationContext = "DEPOT_RETRAIT_CAISSE";
	
	public List<TaDepotRetraitCaisseDTO> findByCodeLight(String code);
}

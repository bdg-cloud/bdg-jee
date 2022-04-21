package fr.legrain.bdg.caisse.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.caisse.dto.TaTDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaTDepotRetraitCaisse;
import fr.legrain.caisse.model.TaTDepotRetraitCaisse;

@Remote
public interface ITaTDepotRetraitCaisseServiceRemote extends IGenericCRUDServiceRemote<TaTDepotRetraitCaisse,TaTDepotRetraitCaisseDTO>,
														IAbstractLgrDAOServer<TaTDepotRetraitCaisse>,IAbstractLgrDAOServerDTO<TaTDepotRetraitCaisseDTO>{
	public static final String validationContext = "T_DEPOT_RETRAIT_CAISSE";
	
	public List<TaTDepotRetraitCaisseDTO> findByCodeLight(String code);
}

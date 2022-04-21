package fr.legrain.bdg.caisse.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.caisse.dto.TaLSessionCaisseDTO;
import fr.legrain.caisse.model.TaLSessionCaisse;
import fr.legrain.caisse.model.TaLSessionCaisse;

@Remote
public interface ITaLSessionCaisseServiceRemote extends IGenericCRUDServiceRemote<TaLSessionCaisse,TaLSessionCaisseDTO>,
														IAbstractLgrDAOServer<TaLSessionCaisse>,IAbstractLgrDAOServerDTO<TaLSessionCaisseDTO>{
	public static final String validationContext = "L_SESSION_CAISSE";
	
	public List<TaLSessionCaisseDTO> findByCodeLight(String code);
}

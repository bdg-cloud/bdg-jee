package fr.legrain.bdg.caisse.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.caisse.dto.TaTLSessionCaisseDTO;
import fr.legrain.caisse.model.TaTLSessionCaisse;

@Remote
public interface ITaTLSessionCaisseServiceRemote extends IGenericCRUDServiceRemote<TaTLSessionCaisse,TaTLSessionCaisseDTO>,
														IAbstractLgrDAOServer<TaTLSessionCaisse>,IAbstractLgrDAOServerDTO<TaTLSessionCaisseDTO>{
	public static final String validationContext = "T_L_SESSION_CAISSE";
	
	public List<TaTLSessionCaisseDTO> findByCodeLight(String code);
}

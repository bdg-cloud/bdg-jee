package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.model.TaTEntite;

@Remote
public interface ITaTEntiteServiceRemote extends IGenericCRUDServiceRemote<TaTEntite,TaTEntiteDTO>,
														IAbstractLgrDAOServer<TaTEntite>,IAbstractLgrDAOServerDTO<TaTEntiteDTO>{
	public static final String validationContext = "T_ENTITE";
	
	//Dima - Début
	public List<TaTEntiteDTO> findByCodeLight(String code);
	//Dima -  Fin
}

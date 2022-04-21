package fr.legrain.abonnement.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.abonnement.dto.TaFrequenceDTO;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaFrequenceServiceRemote extends IGenericCRUDServiceRemote<TaFrequence,TaFrequenceDTO>,
														IAbstractLgrDAOServer<TaFrequence>,IAbstractLgrDAOServerDTO<TaFrequenceDTO>{
	public static final String validationContext = "FREQUENCE";
	
	public List<TaFrequenceDTO> findByCodeLight(String code);
	public List<TaFrequenceDTO> findAllLight();
	//public TaFrequence findByLibelle(String libelle);
}

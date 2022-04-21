package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTelephoneDTO;
import fr.legrain.tiers.model.TaTelephone;

@Remote
public interface ITaTelephoneServiceRemote extends IGenericCRUDServiceRemote<TaTelephone,TaTelephoneDTO>,
														IAbstractLgrDAOServer<TaTelephone>,IAbstractLgrDAOServerDTO<TaTelephoneDTO>{
	public static final String validationContext = "TELEPHONE";
	public List<TaTelephoneDTO> findAllLight();
}

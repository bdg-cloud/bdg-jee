package fr.legrain.bdg.servicewebexterne.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.servicewebexterne.dto.TaTAuthentificationDTO;
import fr.legrain.servicewebexterne.model.TaTAuthentification;


@Remote
public interface ITaTAuthentificationServiceRemote extends IGenericCRUDServiceRemote<TaTAuthentification,TaTAuthentificationDTO>,
														IAbstractLgrDAOServer<TaTAuthentification>,IAbstractLgrDAOServerDTO<TaTAuthentificationDTO>{
	public static final String validationContext = "T_AUTHENTIFICATION";
	
	public List<TaTAuthentificationDTO> findByCodeLight(String code);
}

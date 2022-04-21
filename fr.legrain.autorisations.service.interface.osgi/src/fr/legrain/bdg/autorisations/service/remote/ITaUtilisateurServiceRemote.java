package fr.legrain.bdg.autorisations.service.remote;

import javax.ejb.Remote;

import fr.legrain.autorisations.autorisation.dto.TaUtilisateurDTO;
import fr.legrain.autorisations.autorisation.model.TaUtilisateur;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;;


@Remote
public interface ITaUtilisateurServiceRemote extends IGenericCRUDServiceRemote<TaUtilisateur,TaUtilisateurDTO>,
														IAbstractLgrDAOServer<TaUtilisateur>,IAbstractLgrDAOServerDTO<TaUtilisateurDTO>{
	public static final String validationContext = "UTILISATEUR";
	public TaUtilisateur getSessionInfo();
	
	public String getTenantId();
}

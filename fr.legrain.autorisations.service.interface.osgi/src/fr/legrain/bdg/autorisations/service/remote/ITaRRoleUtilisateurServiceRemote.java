package fr.legrain.bdg.autorisations.service.remote;

import javax.ejb.Remote;

import fr.legrain.autorisations.autorisation.dto.TaRRoleUtilisateurDTO;
import fr.legrain.autorisations.autorisation.model.TaRRoleUtilisateur;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaRRoleUtilisateurServiceRemote extends IGenericCRUDServiceRemote<TaRRoleUtilisateur,TaRRoleUtilisateurDTO>,
														IAbstractLgrDAOServer<TaRRoleUtilisateur>,IAbstractLgrDAOServerDTO<TaRRoleUtilisateurDTO>{
	public static final String validationContext = "R_ROLE_UTILISATEUR";
}

package fr.legrain.bdg.droits.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaRRoleUtilisateurDTO;
import fr.legrain.droits.model.TaRRoleUtilisateur;

@Remote
public interface ITaRRoleUtilisateurServiceRemote extends IGenericCRUDServiceRemote<TaRRoleUtilisateur,TaRRoleUtilisateurDTO>,
														IAbstractLgrDAOServer<TaRRoleUtilisateur>,IAbstractLgrDAOServerDTO<TaRRoleUtilisateurDTO>{
	public static final String validationContext = "R_ROLE_UTILISATEUR";
}

package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaRRoleUtilisateurDTO;
import fr.legrain.moncompte.model.TaRRoleUtilisateur;

@Remote
public interface ITaRRoleUtilisateurServiceRemote extends IGenericCRUDServiceRemote<TaRRoleUtilisateur,TaRRoleUtilisateurDTO>,
														IAbstractLgrDAOServer<TaRRoleUtilisateur>,IAbstractLgrDAOServerDTO<TaRRoleUtilisateurDTO>{
	public static final String validationContext = "R_ROLE_UTILISATEUR";
}

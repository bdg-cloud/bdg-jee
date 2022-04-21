package fr.legrain.bdg.login.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaRRoleUtilisateurDTO;
import fr.legrain.droits.model.TaRRoleUtilisateur;
import fr.legrain.login.dto.TaRRoleUtilisateurLoginDTO;
import fr.legrain.login.model.TaRRoleUtilisateurLogin;

@Remote
public interface ITaRRoleUtilisateurLoginServiceRemote extends IGenericCRUDServiceRemote<TaRRoleUtilisateurLogin,TaRRoleUtilisateurLoginDTO>,
														IAbstractLgrDAOServer<TaRRoleUtilisateurLogin>,IAbstractLgrDAOServerDTO<TaRRoleUtilisateurLoginDTO>{
	public static final String validationContext = "R_ROLE_UTILISATEUR_LOGIN";
}

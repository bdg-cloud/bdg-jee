package fr.legrain.bdg.login.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.login.dto.TaUtilisateurLoginDTO;
import fr.legrain.login.model.TaUtilisateurLogin;


@Remote
public interface ITaUtilisateurLoginServiceRemote extends IGenericCRUDServiceRemote<TaUtilisateurLogin,TaUtilisateurLoginDTO>,
														IAbstractLgrDAOServer<TaUtilisateurLogin>,IAbstractLgrDAOServerDTO<TaUtilisateurLoginDTO>{
	public static final String validationContext = "UTILISATEUR_LOGIN";
//	public TaUtilisateur getSessionInfo();
//	
//	public String getTenantId();
}

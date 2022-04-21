package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaUtilisateurDTO;
import fr.legrain.moncompte.model.TaUtilisateur;


@Remote
public interface ITaUtilisateurServiceRemote extends IGenericCRUDServiceRemote<TaUtilisateur,TaUtilisateurDTO>,
														IAbstractLgrDAOServer<TaUtilisateur>,IAbstractLgrDAOServerDTO<TaUtilisateurDTO>{
	public static final String validationContext = "UTILISATEUR";
	public TaUtilisateur getSessionInfo();
	
	public TaUtilisateur findByEmail(String email);
	
	public String getTenantId();
}

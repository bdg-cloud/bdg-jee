package fr.legrain.bdg.compteclient.service.interfaces.remote.droits;

import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.droits.TaUtilisateurDTO;
import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;


@Remote
public interface ITaUtilisateurServiceRemote extends IGenericCRUDServiceRemote<TaUtilisateur,TaUtilisateurDTO>,
														IAbstractLgrDAOServer<TaUtilisateur>,IAbstractLgrDAOServerDTO<TaUtilisateurDTO>{
	public static final String validationContext = "UTILISATEUR";
	public TaUtilisateur getSessionInfo();
	public TaUtilisateur ctrlSaisieEmail(String email);
	public String getTenantId();
}

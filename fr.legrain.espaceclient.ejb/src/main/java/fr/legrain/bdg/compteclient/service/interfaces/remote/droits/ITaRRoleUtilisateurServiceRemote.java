package fr.legrain.bdg.compteclient.service.interfaces.remote.droits;

import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.droits.TaRRoleUtilisateurDTO;
import fr.legrain.bdg.compteclient.model.droits.TaRRoleUtilisateur;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;

@Remote
public interface ITaRRoleUtilisateurServiceRemote extends IGenericCRUDServiceRemote<TaRRoleUtilisateur,TaRRoleUtilisateurDTO>,
														IAbstractLgrDAOServer<TaRRoleUtilisateur>,IAbstractLgrDAOServerDTO<TaRRoleUtilisateurDTO>{
	public static final String validationContext = "R_ROLE_UTILISATEUR";
}

package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaPrixParUtilisateurDTO;
import fr.legrain.moncompte.model.TaPrixParUtilisateur;


@Remote
public interface ITaPrixParUtilisateurServiceRemote extends IGenericCRUDServiceRemote<TaPrixParUtilisateur,TaPrixParUtilisateurDTO>,
														IAbstractLgrDAOServer<TaPrixParUtilisateur>,IAbstractLgrDAOServerDTO<TaPrixParUtilisateurDTO>{
	public static final String validationContext = "PRIX_PAR_UTIL_PERSO";
}

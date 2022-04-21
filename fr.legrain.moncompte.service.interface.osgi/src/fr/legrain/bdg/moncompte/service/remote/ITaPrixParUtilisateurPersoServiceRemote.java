package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaPrixParUtilisateurPersoDTO;
import fr.legrain.moncompte.model.TaPrixParUtilisateurPerso;


@Remote
public interface ITaPrixParUtilisateurPersoServiceRemote extends IGenericCRUDServiceRemote<TaPrixParUtilisateurPerso,TaPrixParUtilisateurPersoDTO>,
														IAbstractLgrDAOServer<TaPrixParUtilisateurPerso>,IAbstractLgrDAOServerDTO<TaPrixParUtilisateurPersoDTO>{
	public static final String validationContext = "PRIX_PAR_UTIL_PERSO";
}

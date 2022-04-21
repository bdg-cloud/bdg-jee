package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaTNiveauDTO;
import fr.legrain.moncompte.dto.TaTypeProduitDTO;
import fr.legrain.moncompte.model.TaTNiveau;
import fr.legrain.moncompte.model.TaTypeProduit;


@Remote
public interface ITaTNiveauServiceRemote extends IGenericCRUDServiceRemote<TaTNiveau,TaTNiveauDTO>,
														IAbstractLgrDAOServer<TaTNiveau>,IAbstractLgrDAOServerDTO<TaTNiveauDTO>{
	public static final String validationContext = "T_NIVEAU";

}

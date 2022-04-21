package fr.legrain.bdg.conformite.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaResultatCorrectionDTO;
import fr.legrain.conformite.model.TaResultatCorrection;

@Remote
public interface ITaResultatCorrectionServiceRemote extends IGenericCRUDServiceRemote<TaResultatCorrection,TaResultatCorrectionDTO>,
														IAbstractLgrDAOServer<TaResultatCorrection>,IAbstractLgrDAOServerDTO<TaResultatCorrectionDTO>{
	public static final String validationContext = "RESULTAT_CORRECTION";
}

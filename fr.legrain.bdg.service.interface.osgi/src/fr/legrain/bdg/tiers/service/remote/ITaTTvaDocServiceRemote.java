package fr.legrain.bdg.tiers.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaTTvaDocDTO;
import fr.legrain.tiers.model.TaTTvaDoc;

@Remote
public interface ITaTTvaDocServiceRemote extends IGenericCRUDServiceRemote<TaTTvaDoc,TaTTvaDocDTO>,
														IAbstractLgrDAOServer<TaTTvaDoc>,IAbstractLgrDAOServerDTO<TaTTvaDocDTO>{
	public static final String validationContext = "T_TVA_DOC";
}

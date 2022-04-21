package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLAcompteDTO;
import fr.legrain.document.dto.TaLDevisDTO;
import fr.legrain.document.model.TaLAcompte;
import fr.legrain.document.model.TaLDevis;

@Remote
public interface ITaLAcompteServiceRemote extends IGenericCRUDServiceRemote<TaLAcompte,TaLAcompteDTO>,
														IAbstractLgrDAOServer<TaLAcompte>,IAbstractLgrDAOServerDTO<TaLAcompteDTO>{
	public static final String validationContext = "L_ACOMPTE";
}

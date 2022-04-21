package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLApporteurDTO;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.model.TaLApporteur;
import fr.legrain.document.model.TaLFacture;

@Remote
public interface ITaLApporteurServiceRemote extends IGenericCRUDServiceRemote<TaLApporteur,TaLApporteurDTO>,
														IAbstractLgrDAOServer<TaLApporteur>,IAbstractLgrDAOServerDTO<TaLApporteurDTO>{
	public static final String validationContext = "L_APPORTEUR";
}

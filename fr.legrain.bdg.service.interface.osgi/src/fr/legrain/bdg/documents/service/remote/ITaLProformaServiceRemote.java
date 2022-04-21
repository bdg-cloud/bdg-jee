package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLProformaDTO;
import fr.legrain.document.model.TaLProforma;

@Remote
public interface ITaLProformaServiceRemote extends IGenericCRUDServiceRemote<TaLProforma,TaLProformaDTO>,
														IAbstractLgrDAOServer<TaLProforma>,IAbstractLgrDAOServerDTO<TaLProformaDTO>{
	public static final String validationContext = "L_PROFORMA";
}

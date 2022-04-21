package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaProformaDTO;
import fr.legrain.document.model.TaInfosProforma;
import fr.legrain.edition.model.TaEdition;

@Remote
//public interface ITaInfosProformaServiceRemote extends IGenericCRUDServiceRemote<TaInfosProforma,TaInfosProformaDTO>,
//														IAbstractLgrDAOServer<TaInfosProforma>,IAbstractLgrDAOServerDTO<TaInfosProformaDTO>{
public interface ITaInfosProformaServiceRemote extends IGenericCRUDServiceRemote<TaInfosProforma,TaProformaDTO>,
														IAbstractLgrDAOServer<TaInfosProforma>,IAbstractLgrDAOServerDTO<TaProformaDTO>{
	public TaInfosProforma findByCodeProforma(String code);
	
}

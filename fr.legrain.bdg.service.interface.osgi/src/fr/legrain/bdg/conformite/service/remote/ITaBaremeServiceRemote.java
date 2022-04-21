package fr.legrain.bdg.conformite.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.documents.service.remote.IDocumentCodeGenere;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaBaremeDTO;
import fr.legrain.conformite.model.TaBareme;

@Remote
public interface ITaBaremeServiceRemote extends IGenericCRUDServiceRemote<TaBareme,TaBaremeDTO>,
														IAbstractLgrDAOServer<TaBareme>,IAbstractLgrDAOServerDTO<TaBaremeDTO>,
														IDocumentCodeGenere{
	public static final String validationContext = "BAREME";
	
	public void removeOID(TaBareme b);
}

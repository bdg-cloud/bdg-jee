package fr.legrain.bdg.conformite.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.documents.service.remote.IDocumentCodeGenere;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaModeleBaremeDTO;
import fr.legrain.conformite.model.TaModeleBareme;

@Remote
public interface ITaModeleBaremeServiceRemote extends IGenericCRUDServiceRemote<TaModeleBareme,TaModeleBaremeDTO>,
														IAbstractLgrDAOServer<TaModeleBareme>,IAbstractLgrDAOServerDTO<TaModeleBaremeDTO>,
														IDocumentCodeGenere{
	public static final String validationContext = "MODELE_BAREME";
}

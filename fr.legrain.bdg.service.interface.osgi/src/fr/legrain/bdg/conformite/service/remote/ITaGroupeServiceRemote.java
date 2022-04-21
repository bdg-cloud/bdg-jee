package fr.legrain.bdg.conformite.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.documents.service.remote.IDocumentCodeGenere;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaGroupeDTO;
import fr.legrain.conformite.model.TaGroupe;

@Remote
public interface ITaGroupeServiceRemote extends IGenericCRUDServiceRemote<TaGroupe,TaGroupeDTO>,
														IAbstractLgrDAOServer<TaGroupe>,IAbstractLgrDAOServerDTO<TaGroupeDTO>,
														IDocumentCodeGenere{
	public static final String validationContext = "GROUPE";
}

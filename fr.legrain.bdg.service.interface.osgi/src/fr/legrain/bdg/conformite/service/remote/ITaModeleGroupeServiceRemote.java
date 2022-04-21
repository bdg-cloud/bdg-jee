package fr.legrain.bdg.conformite.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.documents.service.remote.IDocumentCodeGenere;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaModeleGroupeDTO;
import fr.legrain.conformite.model.TaGroupe;
import fr.legrain.conformite.model.TaModeleGroupe;

@Remote
public interface ITaModeleGroupeServiceRemote extends IGenericCRUDServiceRemote<TaModeleGroupe,TaModeleGroupeDTO>,
														IAbstractLgrDAOServer<TaModeleGroupe>,IAbstractLgrDAOServerDTO<TaModeleGroupeDTO>,
														IDocumentCodeGenere{
	public static final String validationContext = "MODELE_GROUPE";
}

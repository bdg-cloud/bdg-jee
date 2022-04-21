package fr.legrain.bdg.conformite.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.documents.service.remote.IDocumentCodeGenere;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.dto.TaModeleConformiteDTO;
import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaModeleGroupe;

@Remote
public interface ITaModeleConformiteServiceRemote extends IGenericCRUDServiceRemote<TaModeleConformite,TaModeleConformiteDTO>,
														IAbstractLgrDAOServer<TaModeleConformite>,IAbstractLgrDAOServerDTO<TaModeleConformiteDTO>,
														IDocumentCodeGenere {
	public static final String validationContext = "MODELE_CONFORMITE";
	
	public List<TaModeleConformite> findByModeleGroupe(TaModeleGroupe taModeleGroupe);
}

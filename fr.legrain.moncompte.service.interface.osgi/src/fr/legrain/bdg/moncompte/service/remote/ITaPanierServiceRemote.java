package fr.legrain.bdg.moncompte.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaPanierDTO;
import fr.legrain.moncompte.model.TaPanier;


@Remote
public interface ITaPanierServiceRemote extends IGenericCRUDServiceRemote<TaPanier,TaPanierDTO>,
														IAbstractLgrDAOServer<TaPanier>,IAbstractLgrDAOServerDTO<TaPanierDTO>{
	public static final String validationContext = "PANIER";
	
	public List<TaPanier> findPanierDossier(String codeDossier);

	public List<TaPanier> findPanierClient(String codeClient);

	public List<TaPanier> findPanierDate(Date debutRecherche, Date finRecherche);
	
}

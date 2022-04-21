package fr.legrain.bdg.moncompte.service.remote;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.RemoveException;

import fr.legrain.autorisation.xml.ListeModules;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaDossierDTO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaDossier;
import fr.legrain.moncompte.model.TaPrixPerso;
import fr.legrain.moncompte.model.TaProduit;


@Remote
public interface ITaDossierServiceRemote extends IGenericCRUDServiceRemote<TaDossier,TaDossierDTO>,
														IAbstractLgrDAOServer<TaDossier>,IAbstractLgrDAOServerDTO<TaDossierDTO>{
	public static final String validationContext = "DOSSIER";
	
	public TaClient findClientDossier(String codeDossier);
	
	public List<TaDossier> findListeDossierClient(int idClient);
	public List<TaDossier> findListeDossierProduit(String idProduit);
	public void removeLigneAutorisation(TaDossier persistentInstance, TaAutorisation ligne)throws RemoveException;
	public void removeLignePrixPerso(TaDossier persistentInstance, TaPrixPerso ligne)throws RemoveException;
	
	public List<TaDossierDTO> selectAllLight();
	
	public void supprimeDossier(TaDossier dossier) throws RemoveException;
	public void supprimeDossier(TaDossier dossier, boolean supprimeClient, boolean supprimeUtilisateur) throws RemoveException;
	
	public void initDroitModules(TaDossier dossier);
	public ListeModules ajouteAutorisation(ListeModules l, TaProduit p);
}

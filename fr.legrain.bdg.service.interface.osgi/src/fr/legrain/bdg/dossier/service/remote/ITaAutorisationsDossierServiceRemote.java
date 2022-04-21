package fr.legrain.bdg.dossier.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.dossier.dto.TaAutorisationsDossierDTO;
import fr.legrain.dossier.model.TaAutorisationsDossier;
import fr.legrain.droits.model.TaUtilisateur;

@Remote
public interface ITaAutorisationsDossierServiceRemote extends IGenericCRUDServiceRemote<TaAutorisationsDossier,TaAutorisationsDossierDTO>,
														IAbstractLgrDAOServer<TaAutorisationsDossier>,IAbstractLgrDAOServerDTO<TaAutorisationsDossierDTO>{
	public static final String validationContext = "AUTORISATIONS_DOSSIER";
	
	public TaAutorisationsDossier findInstance();
	
	public void majDroitModuleDossierServiceWebExterne();
	
	public List<String> listeAutorisationDossier();
	public List<String> listeAutorisationUtilisateur(TaUtilisateur taUtilisateur);
	public Boolean autoriseMenu(String idMenu, TaUtilisateur taUtilisateur);
	
	public boolean isDevLgr();
	
	public boolean isDevLgr(String username);
	
}

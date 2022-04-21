package fr.legrain.bdg.droits.service.remote;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;


@Remote
public interface ITaUtilisateurServiceRemote extends IGenericCRUDServiceRemote<TaUtilisateur,TaUtilisateurDTO>,
														IAbstractLgrDAOServer<TaUtilisateur>,IAbstractLgrDAOServerDTO<TaUtilisateurDTO>{
	public static final String validationContext = "UTILISATEUR";
	public TaUtilisateur getSessionInfo();
	
	public List<TaUtilisateurDTO> findByCodeLight(String code);
	public List<TaUtilisateurDTO> findByNomLight(String nom);
	
	public TaUtilisateur login(String login, String password) throws EJBException;
	public TaUtilisateurDTO loginDTO(String login, String password);
	
	public List<TaUtilisateur> selectAll(Boolean systeme);
	
	public String getTenantId();
}

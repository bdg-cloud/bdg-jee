package fr.legrain.bdg.droits.service.remote;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaUtilisateurWebServiceDTO;
import fr.legrain.droits.model.TaUtilisateurWebService;


@Remote
public interface ITaUtilisateurWebServiceServiceRemote extends IGenericCRUDServiceRemote<TaUtilisateurWebService,TaUtilisateurWebServiceDTO>,
														IAbstractLgrDAOServer<TaUtilisateurWebService>,IAbstractLgrDAOServerDTO<TaUtilisateurWebServiceDTO>{
	public static final String validationContext = "UTILISATEUR_WEBSERVICE";
//	public TaUtilisateurWebService getSessionInfo();
	
	public void synchroniseCompteUtilisateurDossierEtWebService();
//	
	public List<TaUtilisateurWebServiceDTO> findByCodeLight(String code);
	public List<TaUtilisateurWebServiceDTO> findByNomLight(String nom);
	
	public TaUtilisateurWebService login(String login, String password) throws EJBException;
	public TaUtilisateurWebServiceDTO loginDTO(String login, String password);
//	
//	public String getTenantId();
}

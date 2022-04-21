package fr.legrain.bdg.general.service.remote;

import javax.ejb.Remote;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

import fr.legrain.general.dto.TaLogDossierDTO;
import fr.legrain.general.model.TaLogDossier;
 
@Remote
public interface ITaLogDossierServiceRemote extends IGenericCRUDServiceRemote<TaLogDossier,TaLogDossierDTO>,
														IAbstractLgrDAOServer<TaLogDossier>,IAbstractLgrDAOServerDTO<TaLogDossierDTO>{
	public static final String validationContext = "LOG_DOSSIER";
	
	public TaLogDossier findByUUID(String uuid);
//	public void logGeneralApi(ContainerRequestContext req, ContainerResponseContext res, String dossier,String utilisateur, String message);
//	public void logGeneralApi(String requestLogIdentifier, ContainerRequestContext req, ContainerResponseContext res, String dossier,String utilisateur, String message);
//	public void logGeneralApi(ContainerRequestContext requestContext,String dossier,String utilisateur, String message);
}

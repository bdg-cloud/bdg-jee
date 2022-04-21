package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaSetupDTO;
import fr.legrain.moncompte.model.TaSetup;


@Remote
//@Path("/setup")
public interface ITaSetupServiceRemote extends IGenericCRUDServiceRemote<TaSetup,TaSetupDTO>,
														IAbstractLgrDAOServer<TaSetup>,IAbstractLgrDAOServerDTO<TaSetupDTO>{
	public static final String validationContext = "SETUP";
	
	public String chargeDernierSetup(String codeClient, String codeProgramme, String versionClient);
	
	//@RolesAllowed("admin")
	@GET
	@Path("{id}")
	@Produces("application/json")
	public TaSetup getSetupId(@PathParam("id") String id);
	
	@POST
	@Path("/create")
	@Consumes("application/json")
	//http://stackoverflow.com/questions/630453/put-vs-post-in-rest
	public Response createSetup(TaSetup s);
	
	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	public Response uploadFile(MultipartFormDataInput input);

}

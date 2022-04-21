package fr.legrain.bdg.rest;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.tiers.dto.TaTiersDTO;
import io.swagger.v3.oas.annotations.Hidden;

@Path("test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
@Hidden
public class TestConnexionRestService extends AbstractRestService {
	
	//http://www.mkyong.com/tutorials/jax-rs-tutorials/
	
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB private ITaTTiersServiceRemote taTTiersService;
	
//	private LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModel = new LgrDozerMapper<>();
//	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUI = new LgrDozerMapper<>();

	/////////////////////////////////////////////////////////////////////////////////////////
	/*
	curl -k -X GET \
    -H "Accept: application/json" \
    -H "Dossier: demo" \
    -H "Lgr: aa" \ 
    -H "Bdg-login: aa" \
    -H "Bdg-password: aa" \
    https://dev.demo.promethee.biz:8443/rest/test/
    */
    @GET()
    @Path("/")
    public Response testCnx(@HeaderParam(X_HEADER_DOSSIER) String dossier,@HeaderParam(X_HEADER_LOGIN) String login,@HeaderParam(X_HEADER_PASSWORD) String password) {
    	
    	TaTiersDTO t = null;
    	try {
    		if(initTenantAndAuthenticate(dossier,login, password)) {
    			System.out.println("ok");
	    		int idTiersInfosEntrepriseDossier = 1;
	    		t = taTiersService.findByIdDTO(idTiersInfosEntrepriseDossier);
				System.out.println("TestConnexionRestService.testCnx() : "+t.getCodeTiers());
				return Response.status(Status.OK).entity(t).build();
    		} else {
    			return Response.status(Status.UNAUTHORIZED).entity(t).build();
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.NOT_ACCEPTABLE).entity(t).build();
		}
    }


}
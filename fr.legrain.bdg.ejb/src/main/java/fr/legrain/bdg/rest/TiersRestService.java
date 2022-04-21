package fr.legrain.bdg.rest;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTiers;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("tiers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class TiersRestService extends AbstractRestService {
	
	//http://www.mkyong.com/tutorials/jax-rs-tutorials/
	
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB private ITaTTiersServiceRemote taTTiersService;
	
	private LgrDozerMapper<TaTiersDTO,TaTiers> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaTiers,TaTiersDTO> mapperModelToUI = new LgrDozerMapper<>();

	/////////////////////////////////////////////////////////////////////////////////////////

	//https://dev.demo.promethee.biz:8443/rest/tiers/hello
    @GET()
    @Path("hello")
    @Hidden
    public String hello() {
        return "Hello";
    }

    @GET()
    @Path("{id}")
    @Operation(summary = "Find tiers by ID",
    tags = {MyApplication.TAG_TIERS},
    description = "Returns a tiers with ID",
    responses = {
//            @ApiResponse(description = "The pet", content = @Content(
//                    schema = @Schema(implementation = TaTiersDTO.class)
//            )),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
            @ApiResponse(responseCode = "404", description = "Tiers not found")
})
    @Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
    public Response getTiersById(
    		@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {
    	
    	TaTiersDTO t = null;
    	
	    	try {
	    		t = taTiersService.findByIdDTO(id);
				System.out.println("TiersRestService.getTiersById() : "+t.getCodeTiers());
			} catch (FinderException e) {
				e.printStackTrace();
			}
	    	return Response.status(200).entity(t).build();

	}
    
    @GET()
    @Path("/")
    @Operation(summary = "Retourne la liste des tiers",
    tags = {MyApplication.TAG_TIERS},
    description = "Retourne la liste des tiers",
    responses = {
	//            @ApiResponse(description = "The pet", content = @Content(
	//                    schema = @Schema(implementation = TaTiersDTO.class)
	//            )),
	//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
	//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
    @Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
    public Response getListeTiers() {
    	
    	List<TaTiersDTO> t = null;
    	
	    	try {
	    		t = taTiersService.findAllLightAdresseComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return Response.status(200).entity(t).build();
	}
    
    @POST()
    @Path("/")
    @Operation(summary = "Création d'un nouveau tiers",
    tags = {MyApplication.TAG_TIERS},
    description = "Création d'un nouveau tiers",
    responses = {
	//            @ApiResponse(description = "The pet", content = @Content(
	//                    schema = @Schema(implementation = TaTiersDTO.class)
	//            )),
	//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
	//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
    @Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
    public Response postCreateTiers(
    		@Parameter(required = true, allowEmptyValue = false) TaTiersDTO dto) {
    	
    	TaTiers t = new TaTiers();
    	
	    	try {
	    		String codeTiersDefaut = "C";
	
	//			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
				dto.setCodeTiers(taTiersService.genereCode(null)); //ejb
	//			validateUIField(Const.C_CODE_TIERS,dto.getCodeTiers());//permet de verrouiller le code genere
	
				dto.setCodeTTiers(codeTiersDefaut);
	
				dto.setCodeCompta(dto.getCodeTiers()); //ejb
	//			validateUIField(Const.C_CODE_COMPTA,dto.getCodeCompta()); //ejb
				dto.setCompte("411"); //ejb
	//			validateUIField(Const.C_COMPTE,dto.getCompte()); //ejb
	
				TaTTiers taTTiers;
				taTTiers = taTTiersService.findByCode(codeTiersDefaut);
	
				if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
					dto.setCompte(taTTiers.getCompteTTiers());
					//this.taTTiers = taTTiers;
				} else {
					//dto.setCompte(TiersPlugin.getDefault().getPreferenceStore().
					//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
				}
				t.setTaTTiers(taTTiers);
				dto.setActifTiers(true);
				//swtTiers.setTtcTiers(TiersPlugin.getDefault().getPreferenceStore().
				//getBoolean(PreferenceConstants.TIERS_SAISIE_TTC_DEFAUT));
				dto.setCodeTTvaDoc("F");
				
				mapperUIToModel.map(dto, t);
	    		
	    		t = taTiersService.merge(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return Response.status(200).entity(t).build();
	}

    @PUT()
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.WILDCARD)
    @Operation(summary = "Mise a jour d'un tiers existant",
    tags = {MyApplication.TAG_TIERS},
    description = "Mise a jour d'un tiers existant",
    responses = {
	//            @ApiResponse(description = "The pet", content = @Content(
	//                    schema = @Schema(implementation = TaTiersDTO.class)
	//            )),
	//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
	//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
    @Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
    public Response putUpdateTiers(
    		@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
    		@Parameter(required = true, allowEmptyValue = false) TaTiersDTO dto) {
    	
    	TaTiers t = new TaTiers();
    	
	    	try {  		
	    		t = taTiersService.findById(id);
				mapperUIToModel.map(dto, t);
	    		
	    		t = taTiersService.merge(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	return Response.status(200).entity(t).build();
	}
    
    @DELETE()
    @Path("{id}")
    @Operation(summary = "Suppression d'un tiers",
    tags = {MyApplication.TAG_TIERS},
    description = "Suppression d'un tiers",
    responses = {
	//            @ApiResponse(description = "The pet", content = @Content(
	//                    schema = @Schema(implementation = TaTiersDTO.class)
	//            )),
	//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
	//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
    @Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
    public Response deleteTiers(
    		@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {
    	
    	TaTiers t = new TaTiers();
    	
	    	try {
	    		t = taTiersService.findById(id);
	    		taTiersService.remove(t);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return Response.status(200).entity(t).build();
	}

}
package fr.legrain.bdg.rest;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("lots")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class LotRestService extends AbstractRestService {

	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;

	private LgrDozerMapper<TaLotDTO,TaLot> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaLot,TaLotDTO> mapperModelToUI = new LgrDozerMapper<>();

	@GET()
	@Path("{id}")
	@Operation(summary = "Retourne un lot", tags = {MyApplication.TAG_LOT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getLotById(@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaLotDTO t = null;

		try {
			t = taLotService.findByIdDTO(id);
			System.out.println("LotRestService.getLotById() : "+t.getNumLot());
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

	@GET()
	@Path("/")
	@Operation(summary = "Liste des lots", tags = {MyApplication.TAG_LOT})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getListeLot() {

		List<TaLotDTO> t = null;

		try {
			t = taLotService.findAllLight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}


	//    @POST()
	//    @Path("/")
	//    @Consumes(MediaType.APPLICATION_JSON)
	//    public Response postCreateLot(
	//    		@HeaderParam(X_HEADER_DOSSIER) String dossier,@HeaderParam(X_HEADER_LOGIN) String login,@HeaderParam(X_HEADER_PASSWORD) String password,
	//    		TaLotDTO dto) {
	//    	
	//    	TaLot t = new TaLot();
	//    	
	//    	if(initTenantAndAuthenticate(dossier,login, password)) {
	//	    	
	//	//    	try {
	//	//    		initTenantRegistry();
	//	//    		initTenant(dossier);
	//	//    		
	//	//    		///
	//	//    		String codeTiersDefaut = "C";
	//	//
	//	////			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
	//	//			dto.setCodeTiers(taBonlivService.genereCode(null)); //ejb
	//	////			validateUIField(Const.C_CODE_TIERS,dto.getCodeTiers());//permet de verrouiller le code genere
	//	//
	//	//			dto.setCodeTTiers(codeTiersDefaut);
	//	//
	//	//			dto.setCodeCompta(dto.getCodeTiers()); //ejb
	//	////			validateUIField(Const.C_CODE_COMPTA,dto.getCodeCompta()); //ejb
	//	//			dto.setCompte("411"); //ejb
	//	////			validateUIField(Const.C_COMPTE,dto.getCompte()); //ejb
	//	//
	//	//			//TaTTiersDAO daoTTiers = new TaTTiersDAO(getEm());
	//	//			TaTTiers taTTiers;
	//	//
	//	//			taTTiers = taTTiersService.findByCode(codeTiersDefaut);
	//	//
	//	//			if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
	//	//				dto.setCompte(taTTiers.getCompteTTiers());
	//	//				//this.taTTiers = taTTiers;
	//	//			} else {
	//	//				//dto.setCompte(TiersPlugin.getDefault().getPreferenceStore().
	//	//				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
	//	//			}
	//	//			t.setTaTTiers(taTTiers);
	//	//			dto.setActifTiers(true);
	//	//			//swtTiers.setTtcTiers(TiersPlugin.getDefault().getPreferenceStore().
	//	//			//getBoolean(PreferenceConstants.TIERS_SAISIE_TTC_DEFAUT));
	//	//			dto.setCodeTTvaDoc("F");
	//	//    		////
	//	//			
	//	//			mapperUIToModel.map(dto, t);
	//	//    		
	//	//    		t = taBonlivService.merge(t);
	//	//			//System.out.println("BonLivRestService.getTiersById() : "+t.getCodeTiers());
	//	//		} catch (Exception e) {
	//	//			// TODO Auto-generated catch block
	//	//			e.printStackTrace();
	//	//		}
	//	    	return Response.status(200).entity(t).build();
	//	////    	return Response.ok(t,MediaType.APPLICATION_XML).build();
	//	////		return Response.status(200).entity("getUserByName is called, name : " + id).build();
	//    	} else {
	//			return Response.status(Status.UNAUTHORIZED).entity(t).build();
	//		}
	//	}

}
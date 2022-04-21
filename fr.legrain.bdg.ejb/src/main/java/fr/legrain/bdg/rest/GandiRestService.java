package fr.legrain.bdg.rest;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEan128ServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEanServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.gandi.service.remote.IGandiService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Path("gandi")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class GandiRestService extends AbstractRestService {

	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaMouvementStockServiceRemote taMouvementStockService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;

	@EJB private ITaParamEan128ServiceRemote taParamEan128Service;
	@EJB private ITaParamEanServiceRemote taParamEanService;

	@EJB private IGandiService gandiService;

	private LgrDozerMapper<TaArticleDTO,TaArticle> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaArticle,TaArticleDTO> mapperModelToUI = new LgrDozerMapper<>();

	/*
	 curl -k  -H "Dossier:demo"  -H "X-Lgr:aa" -H "Bdg-login:demo"  -H "Bdg-password:demo" -H "cle_securite_gandi:xxxxxxxxxxx" -X POST https://dev.demo.promethee.biz:8443/rest/gandi/startserver/xxxxx

	 vm_id server dev bdg : xxxxx
	 clé sécurité : xxxxxx
	 */	
	@POST()
	@Path("/startserver/{id}")
	@Operation(summary = "Démarrer un serveur IaaS", tags = {MyApplication.TAG_GANDI})
	@Hidden
	public Response startServer(
			@Parameter(required = true, allowEmptyValue = false) @HeaderParam("cle_securite_gandi") String cle_securite_gandi,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		boolean success = false;
		success = gandiService.startServer(cle_securite_gandi, id);

		if(!success) {
			return Response.status(200).entity("Erreur dans le démarrage serveur").build();
		}

		return Response.status(200).entity("Serveur en cours de démarrage").build();

	}

	/*
	 curl -k  -H "Dossier:demo"  -H "X-Lgr:aa" -H "Bdg-login:demo"  -H "Bdg-password:demo" -H "cle_securite_gandi:Thai8Saisothoo3m"  -X POST https://dev.demo.promethee.biz:8443/rest/gandi/stopserver/333686
	 */	
	@POST()
	@Path("/stopserver/{id}")
	@Operation(summary = "Arreter un serveur IaaS", tags = {MyApplication.TAG_GANDI})
	@Hidden
	public Response stopServer(
			@Parameter(required = true, allowEmptyValue = false) @HeaderParam("cle_securite_gandi") String cle_securite_gandi,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		boolean success = false;
		success = gandiService.stopServer(cle_securite_gandi, id);

		if(!success) {
			return Response.status(200).entity("Erreur dans le démarrage serveur").build();
		}

		return Response.status(200).entity("Serveur en cours d'arrêt").build();

	}



}
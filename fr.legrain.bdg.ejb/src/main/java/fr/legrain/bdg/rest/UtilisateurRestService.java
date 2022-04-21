package fr.legrain.bdg.rest;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.dossier.dto.TaAutorisationsDossierDTO;
import fr.legrain.dossier.model.TaAutorisationsDossier;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("utilisateur")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class UtilisateurRestService extends AbstractRestService {

	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB private ITaAutorisationsDossierServiceRemote taAutorisationsDossierService;

	private LgrDozerMapper<TaUtilisateurDTO,TaUtilisateur> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaUtilisateur,TaUtilisateurDTO> mapperModelToUI = new LgrDozerMapper<>();

	@GET()
	@Path("{id}")
	@Operation(summary = "Retourne un utilisateur du dossier", tags = {MyApplication.TAG_UTILISATEUR_ET_DOSSIER})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getUtilisateurById(@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaUtilisateurDTO t = null;

		TaUtilisateur entity = null;

		try {
			entity = taUtilisateurService.findById(id);
			t = taUtilisateurService.findByIdDTO(id);

			System.out.println("UtilisateurRestService.getUtilisateurById() : "/*+t.getCodeTiers()*/);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

	@GET()
	@Path("autorisation-dossier")
	@Operation(summary = "Retourne la liste des autorisations du dossier, liste des modules", tags = {MyApplication.TAG_UTILISATEUR_ET_DOSSIER})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getAutorisationDossier(
			) {

		TaAutorisationsDossierDTO t = null;

		TaAutorisationsDossier entity = null;

		try {
			entity = taAutorisationsDossierService.findInstance();
			t = taAutorisationsDossierService.findByIdDTO(entity.getIdAutorisation());

			System.out.println("UtilisateurRestService.getAuthorisationDossier() : "/*+t.getCodeTiers()*/);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();

	}

	@GET()
	@Path("/")
	@Operation(summary = "Liste des utilisateurs du dossier", tags = {MyApplication.TAG_UTILISATEUR_ET_DOSSIER})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getListeUtilisateur() {

		List<TaUtilisateurDTO> t = null;

		try {
			t = taUtilisateurService.selectAllDTO();
			System.out.println("UtilisateurRestService.getListeUtilisateur() : ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

	//    @POST()
	//    @Path("/")
	//    @Consumes(MediaType.APPLICATION_JSON)
	//    @Operation(summary = "Création d'un utilisateur du dossier", tags = {MyApplication.TAG_UTILISATEUR_ET_DOSSIER})
	//    public Response postCreateUtilisateur(TaUtilisateurDTO dto) {
	//
	//		return Response.status(Status.NOT_IMPLEMENTED).entity(null).build();
	//	}

	@PUT()
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.WILDCARD)
	@Operation(summary =  "Association de l'identifiant unique Android au compte client du tiers correspondant",
	tags = {MyApplication.TAG_UTILISATEUR_ET_DOSSIER},
	description = "L'android registration token est notamment utilisé pour les push Firebase")
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response putUpdateUtilisateurAndroidRegistrationToken(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id,
			@Parameter(required = true, allowEmptyValue = false) TaUtilisateurDTO dto) {

		TaUtilisateur t = new TaUtilisateur();

		try {

			t = taUtilisateurService.findById(id);

			t.setAndroidRegistrationToken(dto.getAndroidRegistrationToken());
			t.setDernierAccesMobile(new Date());

			t = taUtilisateurService.merge(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();

	}

}
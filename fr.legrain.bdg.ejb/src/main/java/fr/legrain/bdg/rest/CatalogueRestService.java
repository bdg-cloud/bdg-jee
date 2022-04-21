package fr.legrain.bdg.rest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.dto.TaImageArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.article.service.TaCategorieArticleService;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCatalogueWebServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCategorieArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaImageArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEan128ServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEanServiceRemote;
import fr.legrain.bdg.compteclientfinal.service.LgrEmail;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.rest.filters.request.JWTTokenNeeded;
import fr.legrain.bdg.rest.model.ParamDemandeRenseignement;
import fr.legrain.bdg.rest.model.ParamPub;
import fr.legrain.bdg.rest.model.RechercheParCodeBarreResult;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.email.service.EmailProgrammePredefiniService;
import fr.legrain.tiers.dto.TaParamEspaceClientDTO;
import fr.legrain.tiers.model.TaTiers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("catalogue")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class CatalogueRestService extends AbstractRestService {

	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaCatalogueWebServiceRemote taCatalogueWebService;
	@EJB private ITaCategorieArticleServiceRemote taCategorieArticleService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB private ITaImageArticleServiceRemote taImageArticleService;

	@EJB private EmailProgrammePredefiniService emailProgrammePredefiniService;

	@EJB private ITaParamEan128ServiceRemote taParamEan128Service;
	@EJB private ITaParamEanServiceRemote taParamEanService;

	private LgrDozerMapper<TaArticleDTO,TaArticle> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaArticle,TaArticleDTO> mapperModelToUI = new LgrDozerMapper<>();

	@GET()
	@Path("/articles/{id}")
	@Operation(summary = "Cherche un article dans le catalogue",
	tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE},
	description = "Cherche un article dans le catalogue")
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getArticleById(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaArticleDTO t = null;

		try {
			t = taCatalogueWebService.findArticleCatalogue(id);
			System.out.println("ArticleRestService.getArticleById() : "+t.getCodeArticle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();

	}

	@GET()
	@Path("/categories")
	@Operation(summary = "Retourne la liste des catégories", tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getListeCategoriesArticle() {

		List<TaCategorieArticleDTO> t = null;

		try {
			t = taCategorieArticleService.findAllLight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

	@GET()
	@Path("/categories/{idCategorie}/articles")
	@Operation(summary = "Retourne la liste des articles d'une catégorie", tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getListeArticleInCategorie(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("idCategorie") int idCategorie) {

		List<TaArticleDTO> t = null;

		try {
			t = taCatalogueWebService.findListeArticleCatalogue(idCategorie);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();

	}

	@GET()
	@Path("/articles")
	@Operation(summary = "Retourne la liste de tous les articles dans le catalogue", tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getListeArticle() {

		List<TaArticleDTO> t = null;

		try {

			t = taCatalogueWebService.findListeArticleCatalogue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

	/**
	 * @param dossier
	 * @param idArticle
	 * @param idImage - si idImage est null, retourne l'image par défaut
	 * @param taille - si la taille est nulle, retourne la "grande" image
	 * @return
	 */
	public File findImageArticle(String dossier, int idArticle, Integer idImage, String taille) {

		try {
			List<TaImageArticleDTO> b = taImageArticleService.findByArticleLight(idArticle);

			String TAILLE_ORIGINE = "o";
			String TAILLE_PETIT = "s";
			String TAILLE_MOYEN = "m";
			String TAILLE_GRAND = "l";

			BdgProperties bdgProperties = new BdgProperties();
			String localPath = bdgProperties.osTempDirDossier(dossier)+"/"+bdgProperties.tmpFileNameInterne(""+idArticle);

			if(taille==null 
					|| (!taille.equals(TAILLE_ORIGINE)
							&& !taille.equals(TAILLE_PETIT)
							&& !taille.equals(TAILLE_MOYEN)
							&& !taille.equals(TAILLE_GRAND))
					) {
				taille = TAILLE_GRAND;
			}

			if(b!=null && !b.isEmpty()) {

				byte[] logo = null;

				int i = 0;
				for (TaImageArticleDTO taImageArticleDTO : b) {
					if(idImage==null || taImageArticleDTO.getId()==idImage) {
						if(taille.equals(TAILLE_GRAND)) {
							logo = taImageArticleDTO.getBlobImageGrand();
						} else if(taille.equals(TAILLE_MOYEN)) {
							logo = taImageArticleDTO.getBlobImageMoyen();
						} else if(taille.equals(TAILLE_PETIT)) {
							logo = taImageArticleDTO.getBlobImagePetit();
						} else if(taille.equals(TAILLE_ORIGINE)) {
							logo = taImageArticleDTO.getBlobImageOrigine();
						}
					}
				}

				if(logo!=null) {
					OutputStream  os = new FileOutputStream(localPath); 
					os.write(logo); 
					os.close(); 

					File file = new File(localPath);

					System.out.println(localPath);
					return file;
				}
			}
		} catch(EJBException e) {
			throw new EJBException(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GET()
	@Path("/articles/{id}/image-defaut/{taille}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "Retourne l'image par défaut de l'article sous forme de BLOB",
	tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE},
	description = "Retourne l'image si elle existe, sinon erreur 404",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			// @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Pas d'image disponible")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response imageDefautArticleTaille(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("taille") String taille) {
		try {

			File file = findImageArticle(dossier,id,null,taille);
			if(file!=null) {
				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
				return response.build();
			} else {
				ResponseBuilder response = Response.status(404);
				return response.build();
			}

		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@GET()
	@Path("/articles/{id}/image-defaut")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "Retourne l'image par défaut de l'article sous forme de BLOB",
	tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE},
	description = "Retourne l'image si elle existe, sinon erreur 404",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			// @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Pas d'image disponible")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response imageDefautArticle(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {
		try {

			File file = findImageArticle(dossier,id,null,null);
			if(file!=null) {
				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
				return response.build();
			} else {
				ResponseBuilder response = Response.status(404);
				return response.build();
			}

		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@GET()
	@Path("/articles/{id}/images/{idImage}/{taille}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "Retourne une des images d'un article donné sous forme de BLOB",
	tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE},
	description = "Retourne l'image si elle existe, sinon erreur 404",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			// @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Pas d'image disponible")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response imageArticleTaille(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("idImage") int idImage, 
			@Parameter(required = true, allowEmptyValue = false) @PathParam("taille") String taille) {
		try {

			File file = findImageArticle(dossier,id,idImage,taille);
			if(file!=null) {
				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
				return response.build();
			} else {
				ResponseBuilder response = Response.status(404);
				return response.build();
			}

		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@GET()
	@Path("/articles/{id}/images/{idImage}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "Retourne une des images d'un article donné sous forme de BLOB",
	tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE},
	description = "Retourne l'image si elle existe, sinon erreur 404",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			// @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Pas d'image disponible")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response imageArticle(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) @PathParam("idImage") int idImage) {
		try {

			File file = findImageArticle(dossier,id,idImage,null);
			if(file!=null) {
				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
				return response.build();
			} else {
				ResponseBuilder response = Response.status(404);
				return response.build();
			}

		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}


	@GET()
	@Path("/categories/{idCategorie}/image-defaut")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "Retourne l'image par défaut d'une catégorie sous forme de BLOB",
	tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE},
	description = "Retourne l'image si elle existe, sinon erreur 404",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			// @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "Pas d'image disponible")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response imageDefautCategorie(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("idCategorie") int idCategorie) {
		dossier = initTenant(dossier);
		try {
			TaCategorieArticle b = taCategorieArticleService.findById(idCategorie);

			BdgProperties bdgProperties = new BdgProperties();
			String localPath = bdgProperties.osTempDirDossier(dossier)+"/"+bdgProperties.tmpFileName(""/*taEdition.getFichierNom()*/);

			if(b!=null && b.getBlobImageOrigine()!=null) {


				OutputStream  os = new FileOutputStream(localPath); 
				os.write(b.getBlobImageOrigine()); 
				os.close(); 

				//File file = new File(multitenantProxy.logo(dossier));
				File file = new File(localPath);

				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
				return response.build();

			} else {
				ResponseBuilder response = Response.status(404);
				return response.build();
			}

		} catch(EJBException e) {
			throw new EJBException(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GET()
	@Path("/articles/{id}/images-ids")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Retourne la liste des ID image d'un article",
	tags = {MyApplication.TAG_CATALOGUE, MyApplication.TAG_ARTICLE},
	description = "Retourne la liste de ID, sinon erreur 404",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			// @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			@ApiResponse(responseCode = "404", description = "L'article n'existe pas")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response imageCategorie(
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {
		try {
			List<TaImageArticleDTO> b = taImageArticleService.findByArticleLight(id);

			BdgProperties bdgProperties = new BdgProperties();
			String localPath = bdgProperties.osTempDirDossier(dossier)+"/"+bdgProperties.tmpFileName(""/*taEdition.getFichierNom()*/);

			int[] r;
			if(b!=null && !b.isEmpty()) {
				r = new int[b.size()];

				int i = 0;
				for (TaImageArticleDTO taImageArticleDTO : b) {
					r[i] = taImageArticleDTO.getId();
					i++;
				}

				return Response.status(200).entity(r).build();

			} else {
				ResponseBuilder response = Response.status(404);
				return response.build();
			}

		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}

	@POST()
	@Path("/demande-renseignement")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Envoi d'un email  de demande de renseignement sur un article du catalogue",
	tags = {MyApplication.TAG_ESPACE_CLIENT, MyApplication.TAG_CATALOGUE ,MyApplication.TAG_DIVERS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response emailDemandeRenseignement(@Parameter(required = true, allowEmptyValue = false) ParamDemandeRenseignement p) throws EJBException{
		try {
			TaTiers t = taTiersService.findByCode(p.getCodeTiers());
			TaArticle a = taArticleService.findByCode(p.getCodeArticle());
			String messageEmailComplet = null;
			emailProgrammePredefiniService.emailDemandeRenseignementArticle(p,t,a);
			return Response.status(200)
					.build(); 
		} catch(EJBException | FinderException e) {
			throw new EJBException(e);
		}
	}


}
package fr.legrain.bdg.rest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaLBonlivDTO;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaLBonliv;
import fr.legrain.document.model.TaTLigne;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Path("bonliv")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class BonLivRestService extends AbstractRestService {

	@EJB private ITaBonlivServiceRemote taBonlivService;
	@EJB private ITaLBonlivServiceRemote taLBonlivService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaTLigneServiceRemote taTLigneService;

	private LgrDozerMapper<TaBonlivDTO,TaBonliv> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaBonliv,TaBonlivDTO> mapperModelToUI = new LgrDozerMapper<>();

	private LgrDozerMapper<TaLBonlivDTO,TaLBonliv> mapperUIToModelLigne = new LgrDozerMapper<>();
	private LgrDozerMapper<TaBonliv,TaInfosBonliv> mapperUIToModelDocumentVersInfosDoc = new LgrDozerMapper<>();

	@GET()
	@Path("{id}")
	@Operation(summary = "Retourne un bon de livraison", tags = {MyApplication.TAG_BON_LIVRAISON, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getBonlivById(@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaBonlivDTO t = null;

		TaBonliv entity = null;

		try {    		
			entity = taBonlivService.findById(id);
			t = taBonlivService.findByIdDTO(id);

			t.setLignesDTO(new ArrayList<TaLBonlivDTO>());
			TaLBonlivDTO ldto = null;
			for (TaLBonliv l : entity.getLignes()) {
				ldto = taLBonlivService.findByIdDTO(l.getIdLDocument());
				t.getLignesDTO().add(ldto);
			}
			System.out.println("BonLivRestService.getBonlivById() : "+t.getCodeTiers());
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

	@GET()
	@Path("/")
	@Operation(summary = "Liste des bon de livraison", tags = {MyApplication.TAG_BON_LIVRAISON, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getListeBonliv() {

		List<TaBonlivDTO> t = null;

		try {
			t = taBonlivService.findAllLight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

	@POST()
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Création d'un bon de livraison", tags = {MyApplication.TAG_BON_LIVRAISON, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postCreateBonliv(
			@RequestBody(description = "Nouveau BL", required = true, 
				content = @Content(mediaType = MediaType.APPLICATION_JSON,
				schema = @Schema(implementation = TaBonlivDTO.class))
			) 
			TaBonlivDTO dto) {

		TaBonliv masterEntity = new TaBonliv();
		TaInfosBonliv taInfosDocument = null;

		try {
			if(taInfosDocument==null) {
				taInfosDocument = new TaInfosBonliv();
			}
			if(masterEntity!=null) {
				taInfosDocument.setTaDocumentGeneral(masterEntity);
				masterEntity.setTaInfosDocument(taInfosDocument);
			}

			//taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			dto.setCodeDocument(taBonlivService.genereCode(null)); //ejb
			//validateUIField(Const.C_CODE_TIERS,dto.getCodeTiers());//permet de verrouiller le code genere

			dto.setIdTiers(dto.getIdTiers());
			dto.setCodeTiers(dto.getCodeTiers());
			masterEntity.setTaTiers(taTiersService.findById(dto.getIdTiers()));
			dto.setCodeCompta(masterEntity.getTaTiers().getCodeCompta());
			dto.setCompte(masterEntity.getTaTiers().getCompte());
			masterEntity.getTaInfosDocument().setCodeCompta(masterEntity.getTaTiers().getCodeCompta());
			masterEntity.getTaInfosDocument().setCompte(masterEntity.getTaTiers().getCompte());

			dto.setLibelleDocument("Bon de livraison");

			mapperUIToModel.map(dto, masterEntity);

			TaLBonliv l = null;
			TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
			for (TaLBonlivDTO ldto : dto.getLignesDTO()) {
				l = new TaLBonliv();
				mapperUIToModelLigne.map(ldto, l);
				l.setTaArticle(taArticleService.findById(ldto.getIdArticle()));
				l.setTaLot(taLotService.findByCode(ldto.getNumLot()));
				l.setTaTLigne(taTLigne);
				//l.setQteLDocument(ldto.getQteLDocument());
				masterEntity.addLigne(l);
				l.setTaDocument(masterEntity);
			}

			masterEntity = taBonlivService.merge(masterEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(masterEntity).build();

	}
	
	@PUT()
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Modification d'un bon de livraison", tags = {MyApplication.TAG_BON_LIVRAISON, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response putUpdateBonliv(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@RequestBody(description = "Nouveau BL", required = true, 
				content = @Content(mediaType = MediaType.APPLICATION_JSON,
				schema = @Schema(implementation = TaBonlivDTO.class))
			) 
			TaBonlivDTO dto) {

		TaBonliv masterEntity = new TaBonliv();
		TaInfosBonliv taInfosDocument = null;

		try {
			masterEntity = taBonlivService.findByIDFetch(id);

			mapperUIToModel.map(dto, masterEntity);
			mapperUIToModelDocumentVersInfosDoc.map(masterEntity, taInfosDocument);
			
			masterEntity.getTaInfosDocument().setAdresse1(dto.getInfos().getAdresse1());
			masterEntity.getTaInfosDocument().setAdresse2(dto.getInfos().getAdresse2());
			masterEntity.getTaInfosDocument().setAdresse3(dto.getInfos().getAdresse3());
			masterEntity.getTaInfosDocument().setCodepostal(dto.getInfos().getCodepostal());
			masterEntity.getTaInfosDocument().setVille(dto.getInfos().getVille());
			masterEntity.getTaInfosDocument().setPays(dto.getInfos().getPays());

			masterEntity.getTaInfosDocument().setAdresse1Liv(dto.getInfos().getAdresse1Liv());
			masterEntity.getTaInfosDocument().setAdresse2Liv(dto.getInfos().getAdresse2Liv());
			masterEntity.getTaInfosDocument().setAdresse3Liv(dto.getInfos().getAdresse3Liv());
			masterEntity.getTaInfosDocument().setCodepostalLiv(dto.getInfos().getCodepostalLiv());
			masterEntity.getTaInfosDocument().setVilleLiv(dto.getInfos().getVilleLiv());
			masterEntity.getTaInfosDocument().setPaysLiv(dto.getInfos().getPaysLiv());


			masterEntity = taBonlivService.merge(masterEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();

	}
	
	@POST()
	@Path("{id}/lignes")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Ajouter une ligne au bon de livraison",
	tags = {MyApplication.TAG_BON_LIVRAISON},
	description = "Ajouter une ligne au bon de livraison existant",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postAjouterLigne(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) TaLBonlivDTO ldto) {

		TaBonliv t = new TaBonliv();
		TaBonlivDTO dto = new TaBonlivDTO();

		try {  		
			t = taBonlivService.findByIDFetch(id);

			if(t.getNbDecimalesPrix()==null) {
				t.setNbDecimalesPrix(2);
			}

			boolean trouve = false;
			for (TaLBonliv ligne : t.getLignes()) {
				if(ligne.getTaArticle()!=null && ligne.getTaArticle().getCodeArticle().equals(ldto.getCodeArticle())) {
					trouve = true;
					ligne.setQteLDocument(ligne.getQteLDocument().add(BigDecimal.ONE));
					break;
				}
			}
			if(!trouve) {
				TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
				TaLBonliv nouvelleLigne = new TaLBonliv();
				//mapperUIToModelLigne.map(ldto, l);


				nouvelleLigne.setTaDocument(t);
				nouvelleLigne.setTaTLigne(taTLigne);
				nouvelleLigne.setTaArticle(taArticleService.findById(ldto.getIdArticle()));
				//nouvelleLigne.setQteLDocument(BigDecimal.ONE); //TODO est ce que la quantité est toujours 1 ? transporté la quantité dans le DTO ?
				nouvelleLigne.setQteLDocument(ldto.getQteLDocument());
				nouvelleLigne.setLibLDocument(ldto.getLibLDocument());
				//TODO recherche d'un prix spéficique ? se serrvir du DTO pour transporter le prix afficher dans la boutique (pas très sur) ?
				nouvelleLigne.setPrixULDocument(nouvelleLigne.getTaArticle().getTaCatalogueWeb().getTaPrixCatalogueDefaut().getPrixPrix());
				nouvelleLigne.setCodeTvaLDocument(nouvelleLigne.getTaArticle().getTaTva().getCodeTva());
				nouvelleLigne.setTauxTvaLDocument(nouvelleLigne.getTaArticle().getTaTva().getTauxTva());



				nouvelleLigne.calculMontant();

				t.addLigne(nouvelleLigne);

			}


			t.calculeTvaEtTotaux();
			t = taBonlivService.merge(t);

			dto = recupereDerniereVersionBonlivDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}

	@DELETE()
	@Path("{id}/lignes/{numLigneLDocument}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.WILDCARD)
	@Operation(summary = "Supprimer une ligne du bon de livraison",
	tags = {MyApplication.TAG_BON_LIVRAISON},
	description = "Supprimer une ligne du bon de livraison",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response putSupprimerLigne(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) @PathParam("numLigneLDocument") int numLigneLDocument) {

		TaBonliv t = new TaBonliv();
		TaBonlivDTO dto = null;
		try {  		
			t = taBonlivService.findByIDFetch(id);
			TaLBonliv ligneASupprimer = null; 
			for (TaLBonliv ligne : t.getLignes()) {
				if(ligne.getNumLigneLDocument()!=null && ligne.getNumLigneLDocument().equals(numLigneLDocument)) {
					ligneASupprimer = ligne;
					//ligne.setQteLDocument(ligne.getQteLDocument().add(BigDecimal.ONE));
					break;
				}

			}
			if(ligneASupprimer!=null) {
				t.removeLigne(ligneASupprimer);
				ligneASupprimer.setTaDocument(null);
			} else {
				// pas de ligne a supprimer => erreur
			}
			t.calculeTvaEtTotaux();
			t = taBonlivService.merge(t);

			dto = recupereDerniereVersionBonlivDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}

	@PUT()
	@Path("{id}/lignes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.WILDCARD)
	@Operation(summary = "Modifier une ligne du bon de livraison",
	tags = {MyApplication.TAG_BON_LIVRAISON},
	description = "Modifier une ligne du bon de livraison (quantité essentiellement)",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response putModifierLigne(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, @Parameter(required = true, allowEmptyValue = false) TaLBonlivDTO ldto) {

		TaBonliv t = new TaBonliv();
		TaBonlivDTO dto = null;

		try {  		
			t = taBonlivService.findByIDFetch(id);
			boolean trouve = false;
			for (TaLBonliv ligne : t.getLignes()) {
				if(ligne.getTaArticle()!=null && ligne.getTaArticle().getCodeArticle().equals(ldto.getCodeArticle())) {
					trouve = true;
					//mettre a jour la ligne
					ligne.setQteLDocument(ldto.getQteLDocument()/*.add(BigDecimal.ONE)*/);
					break;
				}
			}
			if(!trouve) {
				// 	erreur
			}
			t.calculeTvaEtTotaux();
			t = taBonlivService.merge(t);

			dto = recupereDerniereVersionBonlivDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}
	
	public TaBonlivDTO recupereDerniereVersionBonlivDTO(TaBonliv entity) throws FinderException {
		TaBonlivDTO dto = taBonlivService.findByIdDTO(entity.getIdDocument());
		dto.setInfos(entity.getTaInfosDocument());
		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		TaLBonlivDTO ldto = null;
		dto.setLignesDTO(new ArrayList<TaLBonlivDTO>());
		for (TaLBonliv l : entity.getLignes()) {
			ldto = taLBonlivService.findByIdDTO(l.getIdLDocument());
			dto.getLignesDTO().add(ldto);
		}
		return dto;
	}

}
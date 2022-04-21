package fr.legrain.bdg.rest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.compteclientfinal.service.LgrEmail;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.generation.service.remote.IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.rest.model.InfosPanierValideResult;
import fr.legrain.bdg.rest.model.ParamConfirmationCommandeBoutique;
import fr.legrain.bdg.rest.model.ParamEmailConfirmationCommandeBoutique;
import fr.legrain.bdg.rest.proxy.multitenant.IEspaceClientRestMultitenantProxy;
import fr.legrain.bdg.rest.proxy.multitenant.IPanierRestMultitenantProxy;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.dto.TaLPanierDTO;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.dto.TaTLigneDTO;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLPanier;
import fr.legrain.document.model.TaLigneALigneEcheance;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaReglement;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.email.service.EmailProgrammePredefiniService;
import fr.legrain.generation.service.CreationDocumentMultiple;
import fr.legrain.generation.service.GenereDocLigneEcheanceAbonnementVersPanier;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tiers.dto.TaEspaceClientDTO;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaParamEspaceClient;
import fr.legrain.tiers.model.TaTTiers;
import fr.legrain.tiers.model.TaTiers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("paniers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class PanierRestService extends AbstractRestService {
 
	@EJB private IPanierRestMultitenantProxy multitenantProxy;

	@EJB private ITaPanierServiceRemote taPanierService;
	@EJB private ITaLPanierServiceRemote taLPanierService;
	@EJB private ITaParamEspaceClientServiceRemote taParamEspaceClientService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaTLigneServiceRemote taTLigneService;
	@EJB private ITaTTiersServiceRemote taTTiersService;
	
	@EJB private ITaPreferencesServiceRemote taPreferencesService;

	@EJB private ITaBoncdeServiceRemote taBoncdeService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;

	@EJB private CreationDocumentMultiple creationDocumentMultiple;

	@EJB private EmailProgrammePredefiniService emailProgrammePredefiniService;
	
	@EJB private IGenereDocLigneEcheanceAbonnementVersPanierServiceRemote genereEcheanceVersPanier;
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;

	private LgrDozerMapper<TaPanierDTO,TaPanier> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaPanier,TaPanierDTO> mapperModelToUI = new LgrDozerMapper<>();

	private LgrDozerMapper<TaLPanierDTO,TaLPanier> mapperUIToModelLigne = new LgrDozerMapper<>();

	@GET()
	@Path("{id}")
	@Operation(summary = "Retourne un panier", tags = {MyApplication.TAG_PANIER, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getPanierById( @Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {


		TaPanierDTO t = null;

		TaPanier entity = null;

		try {    		
			entity = taPanierService.findById(id);
			t = taPanierService.findByIdDTO(id);

			t.setLignesDTO(new ArrayList<TaLPanierDTO>());
			TaLPanierDTO ldto = null;
			for (TaLPanier l : entity.getLignes()) {
				ldto = taLPanierService.findByIdDTO(l.getIdLDocument());
				t.getLignesDTO().add(ldto);
			}
			System.out.println("BonLivRestService.getBonlivById() : "+t.getCodeTiers());
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

	public TaPanierDTO recupereDerniereVersionPanierDTO(TaPanier entity) throws FinderException {
		TaPanierDTO dto = taPanierService.findByIdDTO(entity.getIdDocument());
		dto.setInfos(entity.getTaInfosDocument());
		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		TaLPanierDTO ldto = null;
		dto.setLignesDTO(new ArrayList<TaLPanierDTO>());
		for (TaLPanier l : entity.getLignes()) {
			ldto = taLPanierService.findByIdDTO(l.getIdLDocument());
			dto.getLignesDTO().add(ldto);
		}
		return dto;
	}

	@GET()
	@Path("/actif")
	@Operation(summary = "Retourne le dernier panier en cours, s'il n'y en a pas retourne un nouveau panier",
	tags = {MyApplication.TAG_PANIER, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getPanierActif(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeTiers") String codeTiers) {

		TaPanierDTO dto = null;

		TaPanier entity = null;

		try {    		
			//rechercher le panier
			int id = 1;
			//	    		entity = taPanierService.findById(id);
			//	    		entity = taPanierService.findByCode("BL20-325-03");
			entity = taPanierService.findByActif(codeTiers);


			if(entity!=null) {
				//un panier actif existe
				dto = taPanierService.findByIdDTO(entity.getIdDocument());
				dto.setLignesDTO(new ArrayList<TaLPanierDTO>());
				TaLPanierDTO ldto = null;
				for (TaLPanier l : entity.getLignes()) {
					ldto = taLPanierService.findByIdDTO(l.getIdLDocument());
					dto.getLignesDTO().add(ldto);
				}
				entity.getTaInfosDocument().setTaDocument(null); //pour ne pas surcharger le DTO, faire des DTO pour les infos document ?
				dto.setInfos(entity.getTaInfosDocument());

			} else {
				//pas de panier actif il faut créer un nouveau panier vide
				dto = new TaPanierDTO();
				TaInfosPanier taInfosDocument = null;
				entity = new TaPanier();
				TaTiers tiers = taTiersService.findByCode(codeTiers);

				if(taInfosDocument==null) {
					//taInfosDocument = newInfosDocument();
					taInfosDocument = new TaInfosPanier();
				}
				if(entity!=null) {
					taInfosDocument.setTaDocumentGeneral(entity);
					entity.setTaInfosDocument(taInfosDocument);
				}

				//taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
				dto.setCodeDocument(taPanierService.genereCode(null)); //ejb
				//validateUIField(Const.C_CODE_TIERS,dto.getCodeTiers());//permet de verrouiller le code genere

				dto.setIdTiers(tiers.getIdTiers());
				dto.setCodeTiers(tiers.getCodeTiers());
				dto.setNomTiers(tiers.getNomTiers());
				dto.setPrenomTiers(tiers.getPrenomTiers());
				entity.setTaTiers(tiers);
				dto.setCodeCompta(entity.getTaTiers().getCodeCompta());
				dto.setCompte(entity.getTaTiers().getCompte());
				entity.getTaInfosDocument().setCodeCompta(entity.getTaTiers().getCodeCompta());
				entity.getTaInfosDocument().setCompte(entity.getTaTiers().getCompte());
				entity.getTaInfosDocument().setNomTiers(entity.getTaTiers().getNomTiers());
				entity.getTaInfosDocument().setPrenomTiers(entity.getTaTiers().getPrenomTiers());

				dto.setLibelleDocument("Panier boutique");
				
				dto.setLignesDTO(new ArrayList<TaLPanierDTO>());

				mapperUIToModel.map(dto, entity);
				
				entity.setNbDecimalesQte(2);
				entity.setNbDecimalesPrix(2);

				//					TaLPanier l = null;
				//					TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);


				//		    		dto.setLignesDTO(new ArrayList<TaLPanierDTO>());
				//		    		TaLPanierDTO ldto = null;
				//		    		for (TaLPanier li : entity.getLignes()) {
				//		    			ldto = taLBonlivService.findByIdDTO(li.getIdLDocument());
				//		    			dto.getLignesDTO().add(ldto);
				//					}

				entity = taPanierService.merge(entity);

				dto = recupereDerniereVersionPanierDTO(entity);

			}
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}

	@GET()
	@Path("/")
	@Operation(summary = "Liste des paniers", tags = {MyApplication.TAG_PANIER, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getListePanier() {
		List<TaPanierDTO> t = null;

		try {
			t = taPanierService.findAllLight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

	@POST()
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Création d'un panier", tags = {MyApplication.TAG_PANIER, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postCreatePanier(
			@Parameter(required = true, allowEmptyValue = false) TaPanierDTO dto) {

		TaPanier masterEntity = new TaPanier();
		TaInfosPanier taInfosDocument = null;

		try {
			if(taInfosDocument==null) {
				taInfosDocument = new TaInfosPanier();
			}
			if(masterEntity!=null) {
				taInfosDocument.setTaDocumentGeneral(masterEntity);
				masterEntity.setTaInfosDocument(taInfosDocument);
			}

			//taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			dto.setCodeDocument(taPanierService.genereCode(null)); //ejb
			//validateUIField(Const.C_CODE_TIERS,dto.getCodeTiers());//permet de verrouiller le code genere

			dto.setIdTiers(dto.getIdTiers());
			dto.setCodeTiers(dto.getCodeTiers());
			masterEntity.setTaTiers(taTiersService.findById(dto.getIdTiers()));
			dto.setCodeCompta(masterEntity.getTaTiers().getCodeCompta());
			dto.setCompte(masterEntity.getTaTiers().getCompte());
			masterEntity.getTaInfosDocument().setCodeCompta(masterEntity.getTaTiers().getCodeCompta());
			masterEntity.getTaInfosDocument().setCompte(masterEntity.getTaTiers().getCompte());

			dto.setLibelleDocument("Panier");

			mapperUIToModel.map(dto, masterEntity);

			TaLPanier l = null;
			TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
			for (TaLPanierDTO ldto : dto.getLignesDTO()) {
				l = new TaLPanier();
				mapperUIToModelLigne.map(ldto, l);
				l.setTaArticle(taArticleService.findById(ldto.getIdArticle()));
				l.setTaLot(taLotService.findByCode(ldto.getNumLot()));
				l.setTaTLigne(taTLigne);
				//l.setQteLDocument(ldto.getQteLDocument());
				masterEntity.addLigne(l);
				l.setTaDocument(masterEntity);
			}

			masterEntity = taPanierService.merge(masterEntity);

			dto = recupereDerniereVersionPanierDTO(masterEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}

	@PUT()
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Mise a jour d'un panier",
	tags = {MyApplication.TAG_PANIER},
	description = "Mise a jour d'un panier existant (tiers, adresses facturation et livraison)",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response putUpdatePanier(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) TaPanierDTO dto) {

		TaPanier t = new TaPanier();

		try {  		
			//t = taPanierService.findById(id);
			t = taPanierService.findByIDFetch(id);
			//mapperUIToModel.map(dto, t);
			
			//mise a jour des informations sur le tiers en cas de modification entre la création du panier et "maintenant"
			t.getTaInfosDocument().setCodeCompta(t.getTaTiers().getCodeCompta());
			t.getTaInfosDocument().setCompte(t.getTaTiers().getCompte());
			t.getTaInfosDocument().setNomTiers(t.getTaTiers().getNomTiers());
			t.getTaInfosDocument().setPrenomTiers(t.getTaTiers().getPrenomTiers());

			t.getTaInfosDocument().setAdresse1(dto.getInfos().getAdresse1());
			t.getTaInfosDocument().setAdresse2(dto.getInfos().getAdresse2());
			t.getTaInfosDocument().setAdresse3(dto.getInfos().getAdresse3());
			t.getTaInfosDocument().setCodepostal(dto.getInfos().getCodepostal());
			t.getTaInfosDocument().setVille(dto.getInfos().getVille());
			t.getTaInfosDocument().setPays(dto.getInfos().getPays());

			t.getTaInfosDocument().setAdresse1Liv(dto.getInfos().getAdresse1Liv());
			t.getTaInfosDocument().setAdresse2Liv(dto.getInfos().getAdresse2Liv());
			t.getTaInfosDocument().setAdresse3Liv(dto.getInfos().getAdresse3Liv());
			t.getTaInfosDocument().setCodepostalLiv(dto.getInfos().getCodepostalLiv());
			t.getTaInfosDocument().setVilleLiv(dto.getInfos().getVilleLiv());
			t.getTaInfosDocument().setPaysLiv(dto.getInfos().getPaysLiv());

			t = taPanierService.merge(t);

			dto = recupereDerniereVersionPanierDTO(t);
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
	@Operation(summary = "Ajouter une ligne au panier",
	tags = {MyApplication.TAG_PANIER},
	description = "Ajouter une ligne au panier existant",
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
			@Parameter(required = true, allowEmptyValue = false) TaLPanierDTO ldto) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		try {  		
			t = taPanierService.findByIDFetch(id);

			if(t.getNbDecimalesPrix()==null) {
				t.setNbDecimalesPrix(2);
			}

			boolean trouve = false;
			for (TaLPanier ligne : t.getLignes()) {
				if(ligne.getTaArticle()!=null && ligne.getTaArticle().getCodeArticle().equals(ldto.getCodeArticle())) {
					trouve = true;
					ligne.setQteLDocument(ligne.getQteLDocument().add(BigDecimal.ONE));
					break;
				}
			}
			if(!trouve) {
				TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
				TaLPanier nouvelleLigne = new TaLPanier();
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
			t = taPanierService.merge(t);

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}
	
	@POST()
	@Path("{id}/lignes-echeances")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Ajouter une ligne d'échéance au panier",
	tags = {MyApplication.TAG_PANIER},
	description = "Ajouter une ligne d'échéance au panier existant",
	responses = {
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postAjouterLigneEcheance(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id,
			@Parameter(required = true, allowEmptyValue = false) TaLEcheanceDTO ldto) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		try {  		
			t = taPanierService.findByIDFetch(id);

			if(t.getNbDecimalesPrix()==null) {
				t.setNbDecimalesPrix(2);
			}
			

			TaLEcheance ech = taLEcheanceService.findById(ldto.getId());
			
			boolean existe = false;
			//on vérifie qu'une ligne de ce panier n'est pas deja lié a cette échéance
			for (TaLPanier ligne : t.getLignes()) {
				ligne = taLPanierService.findById(ligne.getIdLDocument());
				for(TaLigneALigneEcheance lal : ligne.getTaLigneALignesEcheance()) {
					if(lal.getTaLEcheance().getIdLEcheance() == ech.getIdLEcheance()) {
						existe = true;
						break;
					}
				}
			}
			
			
			if(!existe) {
				List<TaLEcheance> listeEch = new ArrayList<TaLEcheance>();			
				listeEch.add(ech);			
				t = (TaPanier) genereEcheanceVersPanier.copieDocumentSpecifique(listeEch, t, false, "");
				t.calculeTvaEtTotaux();
				t = taPanierService.merge(t);
			}
			
			
			

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}

	@DELETE()
	@Path("{id}/lignes/{numLigneLDocument}")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Supprimer une ligne du panier",
	tags = {MyApplication.TAG_PANIER},
	description = "Supprimer une ligne du panier",
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

		TaPanier t = new TaPanier();
		TaPanierDTO dto = null;
		try {  		
			t = taPanierService.findByIDFetch(id);
			List<TaLPanier> ligneASupprimer = new ArrayList<TaLPanier>(); 
			String codeLiaisonLigne = null;
			for (TaLPanier ligne : t.getLignes()) {
				//si num ligne est rempli et que (ce num est egal au param OU que (le codeLiaisonLigne est rempli ET ce code Liaison correspond a laligne que l'on parcours))
				if(ligne.getNumLigneLDocument()!=null && (ligne.getNumLigneLDocument().equals(numLigneLDocument) ||  (codeLiaisonLigne != null && ligne.getCodeLiaisonLigne()!= null && ligne.getCodeLiaisonLigne().equals(codeLiaisonLigne))    )) {
					
					//on n'ajoute pas deux fois la même ligne pour suppréssion
					if(!ligneASupprimer.contains(ligne)) {
						ligneASupprimer.add(ligne);
					}
					
					codeLiaisonLigne = ligne.getCodeLiaisonLigne();
					//ligne.setQteLDocument(ligne.getQteLDocument().add(BigDecimal.ONE));
					//break;
				}

			}
			
			for (TaLPanier taLPanier : ligneASupprimer) {
				t.removeLigne(taLPanier);
				taLPanier.setTaDocument(null);
			}

			t.calculeTvaEtTotaux();
			t = taPanierService.merge(t);

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}

	@PUT()
	@Path("{id}/lignes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.WILDCARD)
	@Operation(summary = "Modifier une ligne du panier",
	tags = {MyApplication.TAG_PANIER},
	description = "Modifier une ligne du panier (quantité essentiellement)",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response putModifierLigne(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) TaLPanierDTO ldto) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = null;

		try {  		
			t = taPanierService.findByIDFetch(id);
			boolean trouve = false;
			for (TaLPanier ligne : t.getLignes()) {
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
			t = taPanierService.merge(t);

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}


	@POST()
	@Path("{id}/ajouter-ligne-prix-variable")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Ajouter la ligne pour l'ajustement des prix variables",
	tags = {MyApplication.TAG_PANIER},
	description = "Ajouter la ligne pour l'ajustement des prix variables",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postAjouterAjustementPrixVariable(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		try {  		
			t = taPanierService.findByIDFetch(id);
			TaArticle articleVar = null;
			TaPreferences prefCodeArtVar = taPreferencesService.findByCode("code_article_pour_ajustement_prix_variable");
			if(prefCodeArtVar!=null && prefCodeArtVar.getValeur() != null) {
				articleVar = taArticleService.findByCode(prefCodeArtVar.getValeur());
			}
			
			TaPreferences prefPourcentVar = taPreferencesService.findByCode("pourcentage_prix_variable");
			Integer pourcentage = null;
			if(prefPourcentVar!=null && prefPourcentVar.getValeur() != null) {
				pourcentage = LibConversion.stringToInteger(prefPourcentVar.getValeur());
			}
			
			if(pourcentage!=null && articleVar!=null) {//on a bien un paramétrage de prix variable
				BigDecimal totalMajorationPrixVariable = BigDecimal.ZERO;
				boolean auMoinsUnArticleAPrixVariableDansDocument = false;
				for (TaLPanier lp : t.getLignes()) {
					if(lp.getTaArticle()!=null && lp.getTaArticle().getPrixVariable()!=null && lp.getTaArticle().getPrixVariable() && lp.getMtHtLDocument()!=null) {
						totalMajorationPrixVariable = totalMajorationPrixVariable.add(lp.getMtHtLDocument());
						auMoinsUnArticleAPrixVariableDansDocument = true;
					}
				}
				totalMajorationPrixVariable = totalMajorationPrixVariable.multiply(new BigDecimal(pourcentage)).divide(new BigDecimal(100));
				
				boolean trouve = false;
				for (TaLPanier ligne : t.getLignes()) {
					if(ligne.getTaArticle()!=null && ligne.getTaArticle().getCodeArticle().equals(articleVar.getCodeArticle())) {
						trouve = true; //il y a deja une ligne pour les prix variable
						break;
					}
				}

				if(!trouve && auMoinsUnArticleAPrixVariableDansDocument) {
					TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
					TaLPanier nouvelleLignePrixVariable = new TaLPanier();

					nouvelleLignePrixVariable.setTaDocument(t);
					nouvelleLignePrixVariable.setTaTLigne(taTLigne);
					nouvelleLignePrixVariable.setTaArticle(articleVar);
					nouvelleLignePrixVariable.setQteLDocument(BigDecimal.ONE);
					nouvelleLignePrixVariable.setLibLDocument(articleVar.getLibellecArticle());
					nouvelleLignePrixVariable.setLibLDocument("Ajustement de prix variable");

					nouvelleLignePrixVariable.setPrixULDocument(totalMajorationPrixVariable);
					

					nouvelleLignePrixVariable.setCodeTvaLDocument(nouvelleLignePrixVariable.getTaArticle().getTaTva().getCodeTva());
					nouvelleLignePrixVariable.setTauxTvaLDocument(nouvelleLignePrixVariable.getTaArticle().getTaTva().getTauxTva());

					nouvelleLignePrixVariable.calculMontant();

					t.addLigne(nouvelleLignePrixVariable);
					
					
					taTLigne = taTLigneService.findByCode("C");

					//    		    		TaLPanier ligneCommentaireSeparation = new TaLPanier();
					//    		    		ligneCommentaireSeparation.setTaDocument(t);
					//    		    		ligneCommentaireSeparation.setTaTLigne(taTLigne);
					//    		    		t.addLigne(ligneCommentaireSeparation);

					TaLPanier ligneCommentaireRetrait = new TaLPanier();
					ligneCommentaireRetrait.setTaDocument(t);
					ligneCommentaireRetrait.setTaTLigne(taTLigne);
		
					ligneCommentaireRetrait.setLibLDocument(TaPanier.debutCommentairePrixVariable+
							" certains articles ont une quantité/poids qui n'est pas fixe, un prix maximum ("+pourcentage+"% de plus) est appliqué sur le montant de ces lignes."
									+ "Le prix réel sera prélevé au moment de la préparation de la commande et sera indiqué sur la facture.");
					
					t.addLigne(ligneCommentaireRetrait);

				}
			} // else pas de ligne a ajouter

			t.calculeTvaEtTotaux();
			t = taPanierService.merge(t);

			dto = recupereDerniereVersionPanierDTO(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}

	@POST()
	@Path("{id}/supprimer-ligne-prix-variable")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Supprimer la ligne pour l'ajustement des prix variables",
	tags = {MyApplication.TAG_PANIER},
	description = "Supprimer la ligne pour l'ajustement des prix variables",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postSupprimerAjustementPrixVariable(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		dto = taPanierService.supprimerAjustementPrixVariable(id);
		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		return Response.status(200).entity(dto).build();
	}    
	
	@POST()
	@Path("{id}/supprimer-options-panier")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Supprimer la ligne pour l'ajustement des prix variables",
	tags = {MyApplication.TAG_PANIER},
	description = "Supprimer la ligne pour l'ajustement des prix variables",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postSupprimerOptionsPanier(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();
		
		dto = taPanierService.supprimerOptionsPanier(id);
		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		return Response.status(200).entity(dto).build();
	}    
	
	@POST()
	@Path("{id}/resumer-panier/{selectedTypeExpedition}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Supprimer la ligne pour l'ajustement des prix variables",
	tags = {MyApplication.TAG_PANIER},
	description = "Supprimer la ligne pour l'ajustement des prix variables",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postResumerPanier(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id,
			@Parameter(required = true, allowEmptyValue = false) @PathParam("selectedTypeExpedition") String selectedTypeExpedition,
			@Parameter(required = true, allowEmptyValue = false) String dateTimeMillis
			) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();
		
		dto = taPanierService.resumerPanier(id,selectedTypeExpedition,new Date(new Long(dateTimeMillis)));
		 
		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		return Response.status(200).entity(dto).build();
	} 

	@POST()
	@Path("{id}/email-confirmation-commande")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Envoyer un email au client client lui indiquant que sa commande est validée",
	tags = {MyApplication.TAG_PANIER},
	description = "Envoyer un email au client client lui indiquant que sa commande est validée",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response emailConfirmationCommandeBoutique(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, ParamEmailConfirmationCommandeBoutique paramEmailConfirmationCommandeBoutique) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();
		TaBoncde cmd = null;
		TaEspaceClient compteEspaceClient = null;

		try {  		
			t = taPanierService.findByIDFetch(id);

			cmd = taPanierService.findCommandePanier(t.getCodeDocument());

			compteEspaceClient = taEspaceClientService.findById(paramEmailConfirmationCommandeBoutique.getIdcompteEspaceClient());
			TaReglement reg = taPanierService.findReglementPourPanier(t.getCodeDocument());
			TaStripePaymentIntent pi = taPanierService.findReglementStripePanier(t.getCodeDocument());
			TaTiers tiers = taTiersService.findByCode(paramEmailConfirmationCommandeBoutique.getCodeTiers());
			//if(t) //verifier que le panier appartient bien a ce client et peut être revérifier qu'il y a bien une commande
			emailProgrammePredefiniService.emailConfirmationCommandeBoutique(t,cmd,compteEspaceClient,tiers,reg,pi);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}  

	@GET()
	@Path("{id}/infos-panier-valide")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Information sur un panier validé",
	tags = {MyApplication.TAG_PANIER},
	description = "Information sur un panier validé",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response informationPanierValide(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeTiers") String codeTiers) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();
		TaBoncde cmd = null;
		TaEspaceClient compteEspaceClient = null;
		InfosPanierValideResult infos = new InfosPanierValideResult();

		try {  		
			t = taPanierService.findByIDFetch(id);

			cmd = taPanierService.findCommandePanier(t.getCodeDocument());

			TaReglement reg = taPanierService.findReglementPourPanier(t.getCodeDocument());
			TaStripePaymentIntent pi = taPanierService.findReglementStripePanier(t.getCodeDocument());

			if(t!=null) {
				infos.setCodePanier(t.getCodeDocument());
			}

			if(cmd!=null) {
				infos.setCodeCommande(cmd.getCodeDocument());
				infos.setMontantHT(cmd.getNetHtCalc());
				infos.setMontantTTC(cmd.getNetTtcCalc());
			}

			if(reg!=null) {
				infos.setCodeReglement(reg.getCodeDocument());
			}

			if(pi!=null) {
				infos.setCodeReglementInternet(pi.getIdExterne());
			}	    		

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(infos).build();
	}


	/**
	 * @see PaiementStripeDossierService#validerPaymentIntentGenerationDoc()
	 * @param dossier
	 * @param login
	 * @param password
	 * @param id
	 * @return
	 */
	@POST()
	@Path("{id}/valider-commande-reglement-ulterieur")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Valider une commande/panier de la boutique pour un paiement ultérieur",
	tags = {MyApplication.TAG_PANIER},
	description = "Valider une commande/panier de la boutique pour un paiement ultérieur",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response validerCommandePourReglementUlterieur(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) ParamConfirmationCommandeBoutique paramConfirmationCommandeBoutique) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();
		TaBoncde cmd = null;
		TaEspaceClient compteEspaceClient = null;

		try {  		
			t = taPanierService.findByIDFetch(id);

			/*****************************************************************************************************************/
			/*
			 * type paiement
			 * 1 CB
			 * 2 sur place
			 * 3 chèque
			 * 4 virement
			 * 
			 * type expedition
			 * 1 retrait
			 * 2 livraison
			 */
			/*
			 * TODO, passer des paramètres en plus (2 id surement) et pouvoir appeler sans paramètres
			 */
			//ajout des lignes de commentaires "informatives" pour la livraison et le type de paiement prévu
			if(paramConfirmationCommandeBoutique!=null) {
				if(paramConfirmationCommandeBoutique.getIdTypePaiementPrevu()!=null) {
					if(paramConfirmationCommandeBoutique.getIdTypePaiementPrevu()==1 
							|| paramConfirmationCommandeBoutique.getIdTypePaiementPrevu()==2
							|| paramConfirmationCommandeBoutique.getIdTypePaiementPrevu()==3
							|| paramConfirmationCommandeBoutique.getIdTypePaiementPrevu()==4 ) {
						TaTLigne taTLigne = taTLigneService.findByCode("C");

						TaLPanier ligneCommentaireSeparation = new TaLPanier();
						ligneCommentaireSeparation.setTaDocument(t);
						ligneCommentaireSeparation.setTaTLigne(taTLigne);
						t.addLigne(ligneCommentaireSeparation);

						TaLPanier ligneCommentairePaiementPrevu = new TaLPanier();
						ligneCommentairePaiementPrevu.setTaDocument(t);
						ligneCommentairePaiementPrevu.setTaTLigne(taTLigne);

						if(paramConfirmationCommandeBoutique.getIdTypePaiementPrevu()==1) {
							ligneCommentairePaiementPrevu.setLibLDocument("Type de moyen de paiement prévu lors de la validation de la commande : Carte bancaire");
						} else if(paramConfirmationCommandeBoutique.getIdTypePaiementPrevu()==2) {
							ligneCommentairePaiementPrevu.setLibLDocument("Type de moyen de paiement prévu lors de la validation de la commande : Sur place, lors du retrait");
						} else if(paramConfirmationCommandeBoutique.getIdTypePaiementPrevu()==3) {
							ligneCommentairePaiementPrevu.setLibLDocument("Type de moyen de paiement prévu lors de la validation de la commande : Chèque");
						} else if(paramConfirmationCommandeBoutique.getIdTypePaiementPrevu()==4) {
							ligneCommentairePaiementPrevu.setLibLDocument("Type de moyen de paiement prévu lors de la validation de la commande : Virement");
						}

						t.addLigne(ligneCommentairePaiementPrevu);
						t = taPanierService.merge(t);
						t = taPanierService.findByIDFetch(id);
					}
				}
			}

			/*****************************************************************************************************************/


			/*
			 * Partie de code a faire évoluer en meme temps que 
			 * PaiementStripeDossierService.validerPaymentIntentGenerationDoc()
			 * en ce qui concerne la génération de bon de commande a partir d'un panier de la boutique
			 */
			ParamAfficheChoixGenerationDocument param = new ParamAfficheChoixGenerationDocument();
			param.setDateDocument(new Date());

			param.setTypeDest(TaBoncde.TYPE_DOC); //génération de bondecommande "par défaut"
			param.setDocumentSrc(t);

			t.setNbDecimalesQte(2);
			t.setNbDecimalesPrix(2);

			List<IDocumentTiers> listeDocumentSrc = new ArrayList<>();
			listeDocumentSrc.add(t); //listeDocumentSrc.add(taAvisEcheance); 
			param.setListeDocumentSrc(listeDocumentSrc);
			param.setRepriseAucun(true);
			param.setRetour(true);

			creationDocumentMultiple.setParam(param);
			RetourGenerationDoc retourGenerationDoc = creationDocumentMultiple.creationDocument(true);

			TaTiers taTiersAverifier = retourGenerationDoc.getDoc().getTaTiers();
			if(taTiersAverifier!=null) {
				if(taTiersAverifier.getTaTTiers()!=null && taTiersAverifier.getTaTTiers().getCodeTTiers().equals(TaTTiers.VISITEUR_BOUTIQUE)) {
					//si le tiers était un "visiteur", il devient un client
					TaTTiers taTTiers = taTTiersService.findByCode(TaTTiers.CLIENT);
					taTiersAverifier.setTaTTiers(taTTiers);
					taTiersService.merge(taTiersAverifier);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}  

	@POST()
	@Path("{id}/ajouter-commentaire-date-retrait")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Ajouter la ligne de commentaire concernant la date de retrait",
	tags = {MyApplication.TAG_PANIER},
	description = "Ajouter la ligne de commentaire concernant la date de retrait",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postAjouterLigneCommentaireRetrait(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) String dateTimeMillis) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();
		
		dto = taPanierService.ajouteCommentaireDateRetrait(id,new Date(new Long(dateTimeMillis)));
		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		return Response.status(200).entity(dto).build();
	}

	@POST()
	@Path("{id}/supprimer-commentaire-date-retrait")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Supprimer la ligne de commentaire concernant la date de retrait",
	tags = {MyApplication.TAG_PANIER},
	description = "Supprimer la ligne de commentaire concernant la date de retrait",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postLigneCommentaireRetrait(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		dto = taPanierService.supprimerLigneCommentaireRetrait(id);
		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		return Response.status(200).entity(dto).build();
	}
	
	@POST()
	@Path("{id}/ajouter-fdp-fixe")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Ajouter la ligne concernant les frais de port fixe",
	tags = {MyApplication.TAG_PANIER},
	description = "Ajouter la ligne concernant les frais de port fixe",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postAjouterFdpFixe(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();
		
		dto = taPanierService.ajouteFdpFixePanier(id);

		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		return Response.status(200).entity(dto).build();
	}

	@POST()
	@Path("{id}/supprimer-fdp-fixe")
	@Consumes(MediaType.APPLICATION_JSON)
	//@Produces(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Supprimer la ligne concernant les frais de port fixe",
	tags = {MyApplication.TAG_PANIER},
	description = "Supprimer la ligne concernant les frais de port fixe",
	responses = {
			//            @ApiResponse(description = "The pet", content = @Content(
			//                    schema = @Schema(implementation = TaTiersDTO.class)
			//            )),
			//            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
			//            @ApiResponse(responseCode = "404", description = "Tiers not found")
	})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postSupprimerFdpFixe(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaPanier t = new TaPanier();
		TaPanierDTO dto = new TaPanierDTO();

		dto = taPanierService.supprimerFdpFixe(id);
		
		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		return Response.status(200).entity(dto).build();
	}    
}
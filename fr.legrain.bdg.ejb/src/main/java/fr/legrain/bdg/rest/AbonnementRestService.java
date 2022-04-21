package fr.legrain.bdg.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.legrain.abonnement.dto.TaAbonnementFullDTO;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePlanServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.rest.filters.request.JWTTokenNeeded;
import fr.legrain.bdg.rest.proxy.multitenant.IEspaceClientRestMultitenantProxy;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaLEcheanceDTO;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.dto.TaLAbonnementDTO;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaInfosAbonnement;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaLAbonnement;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.tiers.model.TaEspaceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("/abonnements")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class AbonnementRestService extends AbstractRestService {

	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaLFactureServiceRemote taLFactureService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private IEspaceClientRestMultitenantProxy multitenantProxy;
	
	@EJB private ITaAbonnementServiceRemote taAbonnementService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
	@EJB private ITaLEcheanceServiceRemote taLEcheanceService;
	@EJB private ITaLAbonnementServiceRemote taLAbonnementService;
	@EJB private ITaTLigneServiceRemote taTLigneService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaStripePlanServiceRemote taStripePlanService;
	@EJB private ITaTPaiementServiceRemote taTPaiementService;
	
	@EJB private ITaEtatServiceRemote taEtatService;
	
	private LgrDozerMapper<TaAbonnementDTO,TaAbonnement> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaAbonnement,TaAbonnementDTO> mapperModelToUI = new LgrDozerMapper<>();

	private LgrDozerMapper<TaLAbonnementDTO,TaLAbonnement> mapperUIToModelLigne = new LgrDozerMapper<>();
	
	private Integer idTiers = null;

	@PostConstruct
	public void init() {
		try {
			bdgProperties = new BdgProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TaAbonnementDTO recupereDerniereVersionAbonnementDTO(TaAbonnement entity) throws FinderException {
		TaAbonnementDTO dto = taAbonnementService.findByIdDTO(entity.getIdDocument());
		dto.setInfos(entity.getTaInfosDocument());
		if(dto.getInfos()!=null) {
			dto.getInfos().setTaDocument(null);
		}
		TaLAbonnementDTO ldto = null;
		dto.setLignesDTO(new ArrayList<TaLAbonnementDTO>());
		for (TaLAbonnement l : entity.getLignes()) {
			ldto = taLAbonnementService.findByIdDTO(l.getIdLDocument());
			dto.getLignesDTO().add(ldto);
		}
		return dto;
	}
	
	@POST()
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Création d'un abonnement", tags = {MyApplication.TAG_ABONNEMENT, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postCreateAbonnement(
			@Parameter(required = true, allowEmptyValue = false) TaAbonnementDTO dto) {

		TaAbonnement masterEntity = new TaAbonnement();
		TaInfosAbonnement taInfosDocument = null;

		try {
			if(taInfosDocument==null) {
				taInfosDocument = new TaInfosAbonnement();
			}
			if(masterEntity!=null) {
				taInfosDocument.setTaDocumentGeneral(masterEntity);
				masterEntity.setTaInfosDocument(taInfosDocument);
			}

			//taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
			dto.setCodeDocument(taAbonnementService.genereCode(null)); //ejb
			//validateUIField(Const.C_CODE_TIERS,dto.getCodeTiers());//permet de verrouiller le code genere

			dto.setIdTiers(dto.getIdTiers());
			dto.setCodeTiers(dto.getCodeTiers());
			masterEntity.setTaTiers(taTiersService.findById(dto.getIdTiers()));
			dto.setCodeCompta(masterEntity.getTaTiers().getCodeCompta());
			dto.setCompte(masterEntity.getTaTiers().getCompte());
			masterEntity.getTaInfosDocument().setCodeCompta(masterEntity.getTaTiers().getCodeCompta());
			masterEntity.getTaInfosDocument().setCompte(masterEntity.getTaTiers().getCompte());
			masterEntity.setTaTPaiement(taTPaiementService.findById(dto.getIdTPaiement()));

			dto.setLibelleDocument("Abonnement");
			mapperUIToModel.map(dto, masterEntity);

			TaLAbonnement l = null;
			TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
			for (TaLAbonnementDTO ldto : dto.getLignesDTO()) {
				l = new TaLAbonnement();
				mapperUIToModelLigne.map(ldto, l);
				l.setTaArticle(taArticleService.findById(ldto.getIdArticle()));
				l.setTaPlan(taStripePlanService.findById(ldto.getIdStripePlan()));
				//l.setTaLot(taLotService.findByCode(ldto.getNumLot()));
				l.setTaTLigne(taTLigne);
				//l.setQteLDocument(ldto.getQteLDocument());
				
				l.setQteTitreTransport(ldto.getQteTitreTransport());
				if(l.getQteTitreTransport() == null) {
					l.setQteTitreTransport(new BigDecimal(0));
				}
				masterEntity.addLigne(l);
				l.setTaDocument(masterEntity);
			}

			masterEntity = taAbonnementService.merge(masterEntity);
			TaEtat etat;
			try {
				etat = taEtatService.findByCode("doc_encours");
				List<TaLEcheance> listePremieresEcheances = taAbonnementService.generePremieresEcheances(masterEntity);
				for (TaLEcheance taLEcheance : listePremieresEcheances) {
					taLEcheance = taLEcheanceService.merge(taLEcheance);
					taLEcheance = taLEcheanceService.donneEtat(taLEcheance, etat);
				}
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			dto = recupereDerniereVersionAbonnementDTO(masterEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(dto).build();
	}
	
	@GET()
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
//	@JWTTokenNeeded
	@Operation(summary = "Liste des abonnements chez un fournisseur", tags = {MyApplication.TAG_ABONNEMENT, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response abonnementClientChezFournisseur(
			//@QueryParam("codeDossierFournisseur") String codeDossierFournisseur,
			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@QueryParam("codeTiers") String codeTiers) {
//		dossier = initTenant(dossier);
		List<TaAbonnementFullDTO> l = null;

//		l = multitenantProxy.abonnementClientChezFournisseurDTO(dossier, codeTiers, null, null);
		
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeTiers);
//		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
//		t.setUtiliseCompteClient(true);
//		t.setDateDerniereConnexionCompteClient(new Date());
//		t = taTiersService.merge(t);
		
		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
			idTiers = null;
			t = multitenantProxy.rechercheEspaceClientMultiTiersSecondaire(codeTiers);
		} else {
			idTiers = t.getTaTiers().getIdTiers();
		}
//		t.setDateDerniereConnexion(new Date());
//		taEspaceClientService.merge(t);
		if(t.getActif()!=null && t.getActif()==true) {
			//List<TaAbonnementDTO> listeDocDTO = new ArrayList<>();
			
			//List<TaAbonnementFullDTO> listeDocDTO = taStripeSubscriptionService.findAllFullDTOByIdTiers(t.getTaTiers().getIdTiers());
			List<TaAbonnementFullDTO> listeDocDTO = taAbonnementService.findAllFullDTOByIdTiers(idTiers);

			LgrDozerMapper<TaAbonnement, TaAbonnementDTO> mapper = new LgrDozerMapper<>();
		
//			TaAbonnementDTO dto = null;
//			for (TaAbonnement taAbonnement : listeDoc) {
//				taAbonnement.calculeTvaEtTotaux();
//				//taDevis.calculSommeAvoirIntegres();
//				//listeDocDTO.add(mapper.mapEntityToDto(taFacture, null));
//				dto = new TaAbonnementDTO();
//				mapper.map(taAbonnement, dto);
//				listeDocDTO.add(dto);
//			}
			l = listeDocDTO;
		} else {
			System.out.println("Le service compte client n'est pas actif pour le client "+codeTiers+" chez ce fournisseur");
			l = new ArrayList<TaAbonnementFullDTO>();
		}
		
		return Response.status(200)
				//.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.entity(l)
				.build();
	}
	
	
	
	@GET()
	@Path("/echeances/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
//	@JWTTokenNeeded
	@Operation(summary = "Liste des échéances", tags = {MyApplication.TAG_ABONNEMENT, MyApplication.TAG_DOCUMENTS})
	public Response echeances(
			//@QueryParam("codeDossierFournisseur") String codeDossierFournisseur,
			//@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@QueryParam("codeTiers") String codeTiers,
			@DefaultValue("null") @QueryParam("listeCodeEtat") List<String> listeCodeEtat,
			@DefaultValue("false") @QueryParam("codeModuleBDG") Boolean codeModuleBDG) {
//		dossier = initTenant(dossier);
		List<TaLEcheanceDTO> l = null;

//		l = multitenantProxy.abonnementClientChezFournisseurDTO(dossier, codeClientChezCeFournisseur, null, null);
		
//		initTenantRegistry();
//		initTenant(codeDossierFournisseur);
		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeTiers);
//		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
//		t.setUtiliseCompteClient(true);
//		t.setDateDerniereConnexionCompteClient(new Date());
//		t = taTiersService.merge(t);
		
		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
			idTiers = null;
			t = multitenantProxy.rechercheEspaceClientMultiTiersSecondaire(codeTiers);
		} else {
			idTiers = t.getTaTiers().getIdTiers();
		}
//		t.setDateDerniereConnexion(new Date());
//		taEspaceClientService.merge(t);
		if(t.getActif()!=null && t.getActif()==true) {
			//List<TaAbonnementDTO> listeDocDTO = new ArrayList<>();
			
			//List<TaAbonnementFullDTO> listeDocDTO = taStripeSubscriptionService.findAllFullDTOByIdTiers(t.getTaTiers().getIdTiers());
			//List<TaLEcheanceDTO> listeDocDTO = taLEcheanceService.findAllEcheanceEnCoursOuSuspendueModuleBDGDTO(idTiers);
			String str = listeCodeEtat.get(0);
			str = str.replace("[", "");
			str = str.replace("]", "");
			str= str.replaceAll("\\s+","");
			String[]parts = str.split(",");
			List<String> codeEtats = new ArrayList<String>();
			for (String string : parts) {
				codeEtats.add(string);
			}
			List<TaLEcheanceDTO> listeDocDTO = taLEcheanceService.findAllEcheanceByCodeEtatsAndByIdTiersDTO( codeEtats,  codeModuleBDG,  idTiers);

			//LgrDozerMapper<TaAbonnement, TaAbonnementDTO> mapper = new LgrDozerMapper<>();
		
//			TaAbonnementDTO dto = null;
//			for (TaAbonnement taAbonnement : listeDoc) {
//				taAbonnement.calculeTvaEtTotaux();
//				//taDevis.calculSommeAvoirIntegres();
//				//listeDocDTO.add(mapper.mapEntityToDto(taFacture, null));
//				dto = new TaAbonnementDTO();
//				mapper.map(taAbonnement, dto);
//				listeDocDTO.add(dto);
//			}
			l = listeDocDTO;
		} else {
			System.out.println("Le service compte client n'est pas actif pour le client "+codeTiers+" chez ce fournisseur");
			l = new ArrayList<TaLEcheanceDTO>();
		}
		
		return Response.status(200)
				//.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.entity(l)
				.build();
	}
}

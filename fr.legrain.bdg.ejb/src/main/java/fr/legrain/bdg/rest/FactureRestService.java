package fr.legrain.bdg.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.rest.filters.request.JWTTokenNeeded;
import fr.legrain.bdg.rest.proxy.multitenant.IEspaceClientRestMultitenantProxy;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaLFactureDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaEspaceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("/factures")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class FactureRestService extends AbstractRestService {

	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaLFactureServiceRemote taLFactureService;
	@EJB private IEspaceClientRestMultitenantProxy multitenantProxy;

	@EJB private ITaActionEditionServiceRemote taActionEditionService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;

	@PostConstruct
	public void init() {
		try {
			bdgProperties = new BdgProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET()
	@Path("/")
	@Operation(summary = "Liste des factures chez un fournisseur", tags = {MyApplication.TAG_FACTURES, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response facturesClientChezFournisseur(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeTiers") String codeTiers, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("debut") String sdebut, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("fin") String sfin) {

		List<TaFactureDTO> l = null;

		Date debut = parseDateStringToDate(sdebut);
		Date fin = parseDateStringToDate(sfin);

		//Test récupération du tiers à partir du token
		//		try {
		//			Integer idTiers = getTiersFromJwtToken();
		//			if(idTiers!=null) {
		//				System.out.println("FactureRestService.facturesClientChezFournisseur() "+idTiers);
		//				TaTiersDTO tiersDto = taTiersService.findByIdDTO(idTiers);
		//				if(tiersDto!=null && tiersDto.getCodeTiers()!=null) {
		//					codeTiers = tiersDto.getCodeTiers();
		//				}
		//			}
		//		} catch(Exception e) {
		//			e.printStackTrace();
		//		}

		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeTiers);
		//		TaTiers t = infosClientChezFournisseur(codeDossierFournisseur,codeClientChezCeForunisseur);
		//		t.setUtiliseCompteClient(true);
		//		t.setDateDerniereConnexionCompteClient(new Date());
		//		t = taTiersService.merge(t);

		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
			t = multitenantProxy.rechercheEspaceClientMultiTiersSecondaire(codeTiers);
		}
		//		t.setDateDerniereConnexion(new Date());
		//		taEspaceClientService.merge(t);
		//		if(t.getActif()!=null && t.getActif()==true) {
		List<TaFacture> listeDoc = taFactureService.findByCodeTiersAndDateCompteClient(codeTiers, debut, fin);

		LgrDozerMapper<TaFacture, TaFactureDTO> mapper = new LgrDozerMapper<>();
		List<TaFactureDTO> listeDocDTO = new ArrayList<>();
		TaFactureDTO dto = null;
		for (TaFacture taFacture : listeDoc) {
			taFacture.calculeTvaEtTotaux();
			taFacture.calculSommeAvoirIntegres();

			dto = new TaFactureDTO();
			mapper.map(taFacture, dto);
			listeDocDTO.add(dto);
		}
		l= listeDocDTO;
		//		} else {
		//			System.out.println("Le service compte client n'est pas actif pour le client "+codeTiers+" chez ce fournisseur");
		//			l= new ArrayList<TaFactureDTO>();
		//		}

		return Response.status(200)
				.entity(l)
				.build();
	}

	@GET()
	@Path("/{id}")
	@Operation(summary = "Retourne une facture", tags = {MyApplication.TAG_FACTURES, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getFactureById(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeTiers") String codeTiers ) {

		TaFactureDTO f = null;
		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeTiers);

		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
			t = multitenantProxy.rechercheEspaceClientMultiTiersSecondaire(codeTiers);
		}
		//		t.setDateDerniereConnexion(new Date());
		//		taEspaceClientService.merge(t);
		if(t.getActif()!=null && t.getActif()==true) {

			TaFacture entity = null;
			try {

				entity = taFactureService.findById(id);
				f = taFactureService.findByIdDTO(id);

				f.setLignesDTO(new ArrayList<TaLFactureDTO>());
				TaLFactureDTO ldto = null;
				for (TaLFacture l : entity.getLignes()) {
					ldto = taLFactureService.findByIdDTO(l.getIdLDocument());
					f.getLignesDTO().add(ldto);
				}
				System.out.println("FlashRestService.getFactureById() : "/*+t.getCodeTiers()*/);
			} catch (FinderException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("Le service compte client n'est pas actif pour le client "+codeTiers+" chez ce fournisseur");
			f = new TaFactureDTO();
		}

		return Response.status(200)
				.entity(f)
				.build();
	}

	@GET()
	@Path("/pdf")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "PDF", tags = {MyApplication.TAG_FACTURES, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response pdfFacture(
//			@HeaderParam(X_HEADER_DOSSIER) String dossier,
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeDocument") String codeDocument) {
//		dossier = initTenant(dossier);

		File file = null;

		try {
			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
			TaFacture t = taFactureService.findByCode(codeDocument);
			String pdfPath = taFactureService.generePDF(t.getIdDocument(), new HashMap<String,Object>(), null, null,taActionEdition);

			file = new File( pdfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
		return response.build();
	}

	public List<TaFacture> facturesClientChezFournisseur(String codeDossierFournisseur, String codeTiers) {
		codeDossierFournisseur = initTenant(codeDossierFournisseur);

		return null;
	}

}

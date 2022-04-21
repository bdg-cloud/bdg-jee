package fr.legrain.bdg.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
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

import fr.legrain.bdg.compteclientfinal.service.LgrEmail;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.rest.filters.request.JWTTokenNeeded;
import fr.legrain.bdg.rest.proxy.multitenant.IEspaceClientRestMultitenantProxy;
import fr.legrain.bdg.tiers.service.remote.ITaEspaceClientServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaAvisEcheanceDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.dto.TaLAvisEcheanceDTO;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaLAvisEcheance;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.tiers.model.TaEspaceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("/avis-echeance")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class AvisEcheanceRestService extends AbstractRestService {

	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaLFactureServiceRemote taLFactureService;
	@EJB private IEspaceClientRestMultitenantProxy multitenantProxy;

	@EJB private ITaAvisEcheanceServiceRemote taAvisEcheanceService;
	@EJB private ITaActionEditionServiceRemote taActionEditionService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
	@EJB private ITaLAvisEcheanceServiceRemote taLAvisEcheanceService;


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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Liste des avis d'échéances chez un fournisseur", tags = {MyApplication.TAG_AVIS_ECHEANCES, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response avisEcheanceClientChezFournisseur(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeTiers") String codeTiers, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("debut") String sdebut, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("fin") String sfin) {

		List<TaAvisEcheanceDTO> l = null;

		Date debut = parseDateStringToDate(sdebut);
		Date fin = parseDateStringToDate(sfin);


		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeTiers);

		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
			t = multitenantProxy.rechercheEspaceClientMultiTiersSecondaire(codeTiers);
		}
		//		t.setDateDerniereConnexion(new Date());
		//		taEspaceClientService.merge(t);
		if(t.getActif()!=null && t.getActif()==true) {
			List<TaAvisEcheance> listeDoc = taAvisEcheanceService.findByCodeTiersAndDateCompteClient(codeTiers, debut, fin);
			LgrDozerMapper<TaAvisEcheance, TaAvisEcheanceDTO> mapper = new LgrDozerMapper<>();
			List<TaAvisEcheanceDTO> listeDocDTO = new ArrayList<>();
			TaAvisEcheanceDTO dto = null;
			for (TaAvisEcheance taAvisEcheance : listeDoc) {
				taAvisEcheance.calculeTvaEtTotaux();
				dto = new TaAvisEcheanceDTO();
				mapper.map(taAvisEcheance, dto);
				listeDocDTO.add(dto);
			}
			l = listeDocDTO;
		} else {
			System.out.println("Le service compte client n'est pas actif pour le client "+codeTiers+" chez ce fournisseur");
			l = new ArrayList<TaAvisEcheanceDTO>();
		}

		return Response.status(200)
				.entity(l)
				.build();
	}

	@GET()
	@Path("/{id}")
	@Operation(summary = "Retourne un avis d'échéance", tags = {MyApplication.TAG_AVIS_ECHEANCES, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getAvisEcheanceById(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeTiers") String codeTiers ) {

		TaAvisEcheanceDTO f = null;
		TaEspaceClient t = taEspaceClientService.findByCodeTiers(codeTiers);


		if(t==null) { //surement un code multi compte tiers et travail pas sur le tiers principal
			t = multitenantProxy.rechercheEspaceClientMultiTiersSecondaire(codeTiers);
		}
		//		t.setDateDerniereConnexion(new Date());
		//		taEspaceClientService.merge(t);
		if(t.getActif()!=null && t.getActif()==true) {

			TaAvisEcheance entity = null;

			try {
				entity = taAvisEcheanceService.findById(id);
				f = taAvisEcheanceService.findByIdDTO(id);

				f.setLignesDTO(new ArrayList<TaLAvisEcheanceDTO>());
				TaLAvisEcheanceDTO ldto = null;
				for (TaLAvisEcheance l : entity.getLignes()) {
					ldto = taLAvisEcheanceService.findByIdDTO(l.getIdLDocument());
					f.getLignesDTO().add(ldto);
				}
				System.out.println("FlashRestService.getBonlivById() : "/*+t.getCodeTiers()*/);
			} catch (FinderException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("Le service compte client n'est pas actif pour le client "+codeTiers+" chez ce fournisseur");
			f = new TaAvisEcheanceDTO();
		}

		return Response.status(200)
				.entity(f)
				.build();
	}

	@GET()
	@Path("/pdf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "PDF", tags = {MyApplication.TAG_AVIS_ECHEANCES, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response pdfAvisEcheance(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeDocument") String codeDocument) {

		File file = null;

		try {
			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
			TaAvisEcheance t = taAvisEcheanceService.findByCode(codeDocument);
			String pdfPath = taAvisEcheanceService.generePDF(t.getIdDocument(), null, null, null);

			file = new File(pdfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
		return response.build();
	}

	@GET()
	@Path("/facture-pour-avis-echeance")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Retourne la facture générée à partir d'un avis d'échéance si elle existe", tags = {MyApplication.TAG_FACTURES, MyApplication.TAG_AVIS_ECHEANCES, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response facturePourAvisEcheance(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeAvisEcheance") String codeAvisEcheance) {

		try {

			TaFactureDTO taFactureDTO = null;
			try {
				taFactureDTO = taFactureService.findByCodeDTO(codeAvisEcheance);
			} catch (FinderException e) {
				e.printStackTrace();
			}

			return Response.status(200)
					.entity(taFactureDTO)
					.build(); 
		} catch(EJBException e) {
			throw new EJBException(e);
		}
	}
}

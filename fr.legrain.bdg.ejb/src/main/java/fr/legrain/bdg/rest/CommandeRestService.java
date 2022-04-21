package fr.legrain.bdg.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
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
import fr.legrain.document.dto.TaBoncdeDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaDevis;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.tiers.model.TaEspaceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("/commandes")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class CommandeRestService extends AbstractRestService {

	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaLFactureServiceRemote taLFactureService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private IEspaceClientRestMultitenantProxy multitenantProxy;

	@EJB private ITaActionEditionServiceRemote taActionEditionService;
	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
	@EJB private ITaBoncdeServiceRemote taBoncdeService;

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
	@Operation(summary = "Liste des commandes chez un fournisseurs", tags = {MyApplication.TAG_COMMANDE, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response commandeClientChezFournisseur(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeTiers") String codeTiers, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("debut") String sdebut, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("fin") String sfin) {

		List<TaBoncdeDTO> l = null;

		Date debut = parseDateStringToDate(sdebut);
		Date fin = parseDateStringToDate(sfin);

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
		if(t.getActif()!=null && t.getActif()==true) {
			List<TaBoncde> listeDoc = taBoncdeService.findByCodeTiersAndDateCompteClient(codeTiers, debut, fin);
			LgrDozerMapper<TaBoncde, TaBoncdeDTO> mapper = new LgrDozerMapper<>();
			List<TaBoncdeDTO> listeDocDTO = new ArrayList<>();
			TaBoncdeDTO dto = null;
			for (TaBoncde taBoncde : listeDoc) {
				taBoncde.calculeTvaEtTotaux();
				dto = new TaBoncdeDTO();
				mapper.map(taBoncde, dto);
				listeDocDTO.add(dto);
			}
			l = listeDocDTO;
		} else {
			System.out.println("Le service compte client n'est pas actif pour le client "+codeTiers+" chez ce fournisseur");
			l = new ArrayList<TaBoncdeDTO>();
		}

		return Response.status(200)
				.entity(l)
				.build();
	}

	@GET()
	@Path("/pdf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "PDF", tags = {MyApplication.TAG_COMMANDE, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response pdfCommande(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeDocument") String codeDocument) {

		File file = null;

		try {
			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
			TaBoncde t = taBoncdeService.findByCode(codeDocument);
			String pdfPath = taBoncdeService.generePDF(t.getIdDocument(), null, null, null);

			file = new File(pdfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
		return response.build();
	}

}

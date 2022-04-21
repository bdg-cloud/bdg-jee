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
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.model.TaDevis;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.tiers.model.TaEspaceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("/devis")
//@Consumes(MediaType.APPLICATION_JSON)
//@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class DevisRestService extends AbstractRestService {

	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaFactureServiceRemote taFactureService;
	@EJB private ITaLFactureServiceRemote taLFactureService;
	@EJB private ITaDevisServiceRemote taDevisService;
	@EJB private IEspaceClientRestMultitenantProxy multitenantProxy;

	@EJB private ITaEspaceClientServiceRemote taEspaceClientService;
	@EJB private ITaActionEditionServiceRemote taActionEditionService;

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
	@Operation(summary = "Liste des devis chez un fournisseurs", tags = {MyApplication.TAG_DEVIS, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response devisClientChezFournisseur(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeTiers") String codeTiers, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("debut") String sdebut, 
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("fin") String sfin) {
		List<TaDevisDTO> l = null;

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
			List<TaDevis> listeDoc = taDevisService.findByCodeTiersAndDateCompteClient(codeTiers, debut, fin);
			LgrDozerMapper<TaDevis, TaDevisDTO> mapper = new LgrDozerMapper<>();
			List<TaDevisDTO> listeDocDTO = new ArrayList<>();
			TaDevisDTO dto = null;
			for (TaDevis taDevis : listeDoc) {
				taDevis.calculeTvaEtTotaux();
				dto = new TaDevisDTO();
				mapper.map(taDevis, dto);
				listeDocDTO.add(dto);
			}
			l = listeDocDTO;
		} else {
			System.out.println("Le service compte client n'est pas actif pour le client "+codeTiers+" chez ce fournisseur");
			l = new ArrayList<TaDevisDTO>();
		}

		return Response.status(200)
				.entity(l)
				.build();
	}

	@GET()
	@Path("/pdf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Operation(summary = "PDF", tags = {MyApplication.TAG_DEVIS, MyApplication.TAG_DOCUMENTS})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response devisFacture(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeDocument") String codeDocument) {

		File file = null;

		try {
			TaActionEdition taActionEdition =  taActionEditionService.findByCode(TaActionEdition.code_espacecli);
			TaDevis t = taDevisService.findByCode(codeDocument);
			String pdfPath = taDevisService.generePDF(t.getIdDocument(), null, null, null);

			file = new File(pdfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=new-android-book.pdf");
		return response.build();
	}

	public List<TaDevis> devisClientChezFournisseur(String codeDossierFournisseur, String codeClientChezCeForunisseur) {
		return null;
	}

}

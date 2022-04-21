package fr.legrain.bdg.rest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFlashServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaLFlashServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.dto.TaFlashDTO;
import fr.legrain.document.dto.TaLFlashDTO;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaTLigne;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("flash")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class FlashRestService extends AbstractRestService {

	@EJB private ITaFlashServiceRemote taFlashService;
	@EJB private ITaLFlashServiceRemote taLFlashService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaTLigneServiceRemote taTLigneService;

	private LgrDozerMapper<TaFlashDTO,TaFlash> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaFlash,TaFlashDTO> mapperModelToUI = new LgrDozerMapper<>();

	private LgrDozerMapper<TaLFlashDTO,TaLFlash> mapperUIToModelLigne = new LgrDozerMapper<>();

	@GET()
	@Path("{id}")
	@Operation(summary = "Recherche d'un document de flashage", tags = {MyApplication.TAG_FLASH})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getFlashById(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaFlashDTO t = null;

		TaFlash entity = null;

		try {
			entity = taFlashService.findById(id);
			t = taFlashService.findByIdDTO(id);

			t.setLignesDTO(new ArrayList<TaLFlashDTO>());
			TaLFlashDTO ldto = null;
			for (TaLFlash l : entity.getLignes()) {
				ldto = taLFlashService.findByIdDTO(l.getIdLFlash());
				t.getLignesDTO().add(ldto);
			}
			System.out.println("FlashRestService.getBonlivById() : "/*+t.getCodeTiers()*/);
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();

	}

	@GET()
	@Path("/")
	@Operation(summary = "Recherche la liste des documents de flashage", tags = {MyApplication.TAG_FLASH})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getListeFlash() {

		List<TaFlashDTO> t = null;

		try {

			t = taFlashService.selectAllDTO();
			System.out.println("FlashRestService.getListeFlash() : ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();

	}

	@POST()
	@Path("/")
	@Operation(summary = "Création d'un document de flashage", tags = {MyApplication.TAG_FLASH})
	@Consumes(MediaType.APPLICATION_JSON)
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postCreateFlash(@Parameter(required = true, allowEmptyValue = false) TaFlashDTO dto) {

		TaFlash masterEntity = new TaFlash();
		TaFlashDTO t = null;
		TaInfosBonliv taInfosDocument = null;

		try {	    		
			////	    		public void initInfosDocument() {
			//	    			if(taInfosDocument==null) {
			//	    				//taInfosDocument = newInfosDocument();
			//	    				taInfosDocument = new TaInfosBonliv();
			//	    			}
			//	    			if(masterEntity!=null) {
			//	    				taInfosDocument.setTaDocumentGeneral(masterEntity);
			//	    				masterEntity.setTaInfosDocument(taInfosDocument);
			//	    			}
			////	    		}

			dto.setCodeFlash(taFlashService.genereCode(null)); //ejb

			dto.setIdTiers(dto.getIdTiers());
			dto.setCodeTiers(dto.getCodeTiers());
			if(dto.getIdTiers()!=null) {
				masterEntity.setTaTiers(taTiersService.findById(dto.getIdTiers()));
			}
			//				dto.setCodeCompta(masterEntity.getTaTiers().getCodeCompta());
			//				dto.setCompte(masterEntity.getTaTiers().getCompte());
			//				masterEntity.getTaInfosDocument().setCodeCompta(masterEntity.getTaTiers().getCodeCompta());
			//				masterEntity.getTaInfosDocument().setCompte(masterEntity.getTaTiers().getCompte());

			dto.setLibelleFlash("Flash android");
			dto.setDateTransfert(new Date());

			mapperUIToModel.map(dto, masterEntity);

			TaLFlash l = null;
			TaTLigne taTLigne = taTLigneService.findByCode(SWTDocument.C_CODE_T_LIGNE_H);
			for (TaLFlashDTO ldto : dto.getLignesDTO()) {
				l = new TaLFlash();
				mapperUIToModelLigne.map(ldto, l);
				//l.setTaArticle(taArticleService.findById(ldto.getIdArticle()));
				//					l.setTaLot(taLotService.findByCode(ldto.getNumLot()));
				//					l.setTaArticle(l.getTaLot().getTaArticle());
				l.setNumLot(ldto.getNumLot());
				l.setLibLFlash(ldto.getLibLDocument());
				//l.setTaTLigne(taTLigne);
				l.setQteLFlash(ldto.getQteLDocument());
				l.addREtatLigneDoc(taFlashService.etatLigneInsertion(masterEntity));

				//si il y a un tiers dans l'entete, on l'affecte à toute les lignes du document
				//l.setIdTiers(dto.getIdTiers());
				l.setCodeTiers(dto.getCodeTiers());

				if(masterEntity.getLignes()==null) {
					masterEntity.setLignes(new ArrayList<>());
				}
				masterEntity.getLignes().add(l); //masterEntity.addLigne(l);
				l.setTaFlash(masterEntity);
			}

			masterEntity = taFlashService.merge(masterEntity);
			t = taFlashService.findByIdDTO(masterEntity.getIdFlash());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();
	}

}
package fr.legrain.bdg.rest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEan128ServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaParamEanServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.rest.filters.request.JWTTokenNeeded;
import fr.legrain.bdg.rest.model.RechercheParCodeBarreResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

@Path("articles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class ArticleRestService extends AbstractRestService {

	//http://www.mkyong.com/tutorials/jax-rs-tutorials/

	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaLotServiceRemote taLotService;
	@EJB private ITaMouvementStockServiceRemote taMouvementStockService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;

	@EJB private ITaParamEan128ServiceRemote taParamEan128Service;
	@EJB private ITaParamEanServiceRemote taParamEanService;

	private LgrDozerMapper<TaArticleDTO,TaArticle> mapperUIToModel = new LgrDozerMapper<>();
	private LgrDozerMapper<TaArticle,TaArticleDTO> mapperModelToUI = new LgrDozerMapper<>();

	@GET()
	@Path("{id}")
	@Operation(summary = "Find item by ID", 
	tags = {MyApplication.TAG_ARTICLE},
	description = "Returns an item with ID")
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getArticleById(
			@Parameter(required = true, allowEmptyValue = false) @PathParam("id") int id) {

		TaArticleDTO t = null;

		try {
			t = taArticleService.findByIdDTO(id);
			System.out.println("ArticleRestService.getArticleById() : "+t.getCodeArticle());
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();

	}

	@GET()
	@Path("/barcode/")
	@Operation(summary = "Recherche un article a partir de son code barre", tags = {MyApplication.TAG_ARTICLE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getArticleByCodeBarre(
			@Parameter(required = true, allowEmptyValue = false) @QueryParam("codeBarre") /*@Encoded*/ String codeBarre
			) {

		TaArticleDTO t = null;
		Map<String, Object> r = null;
		List<Object> lr = null;
		RechercheParCodeBarreResult re = null;

		try {

			Map<String, String>  listeRetour = taParamEan128Service.decodeEan128(codeBarre);
			String codeArticle = taParamEan128Service.decodeCodeArticle(listeRetour);
			String ean = taParamEan128Service.decodeEanArticle(listeRetour);
			String numLot = taParamEan128Service.decodeLotFabrication(listeRetour);

			TaArticle art = taArticleService.findByCodeBarre(codeBarre);
			t = taArticleService.findByIdDTO(art.getIdArticle());
			List<ArticleLotEntrepotDTO> l = onChangeListArticleMP(t.getCodeArticle(), "*");

			//	    		r = new HashMap<>();
			//	    		r.put("article", t);
			//	    		r.put("lot_code128", l.get(0));
			//	    		r.put("lots_disponible", l);

			re = new RechercheParCodeBarreResult();
			re.setTaArticleDTO(t);
			//re.setTaLotSelectionneDTO(l.get(1));
			re.setListeLotDisponible(l);

			//	    		lr = new ArrayList<>();
			//	    		lr.add(t);
			//	    		lr.add(l.get(0));
			//	    		lr.add(l);
			System.out.println("ArticleRestService.getArticleByCodeBarre() : "+t.getCodeArticle());
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.status(Status.OK).entity(re).build();

	}

	//TODO copier depuis AbtractDocumentController (autocomplete des lots dispo) voir pour homogénéiser
	private List<ArticleLotEntrepotDTO> onChangeListArticleMP(String codeArticle, String numlot) {
		List<ArticleLotEntrepotDTO> listArticleLotEntrepot = null;
		try {

			listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
			listArticleLotEntrepot = taMouvementStockService.getEtatStockByArticle(codeArticle, null);
			List<ArticleLotEntrepotDTO> temp= new ArrayList<ArticleLotEntrepotDTO>();
			List<BigDecimal> qte = new ArrayList<BigDecimal>();
			//			for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){ //liste des lots en stocks par articles
			//	
			//				for(Ligne mp : masterEntity.getLignes()){ //liste des MP de la fabrication courante
			//					TaMouvementStock mouv = mp.getTaMouvementStock();
			//	
			//					//TaLot lot =lotService.findByCode(mouv.getNumLot());
			//					//mouv.setTaLot(lot);
			//					
			//					//meme emplacement, meme entrepot, meme article, meme lot, meme Unite 1
			//					if(mouv.getEmplacement()!=null && mouv.getEmplacement().equals(dto.getEmplacement()) 
			//							&& mouv.getTaEntrepot()!=null && mouv.getTaEntrepot().getCodeEntrepot()!=null && mouv.getTaEntrepot().getCodeEntrepot().equals(dto.getCodeEntrepot()) 
			//							&& mouv.getTaLot().getTaArticle().getCodeArticle().equals(codeArticle)  
			//							&& ((Integer)mouv.getTaLot().getIdLot()).equals(dto.getIdLot())
			////							&& (mouv.getNumLot()).equals(dto.getNumLot())
			//							&& dto.getCodeUnite()!=null 
			//							&& (mouv.getUn1Stock()).equals(dto.getCodeUnite())
			//						){
			//						temp.add(dto);
			//						qte.add(mouv.getQte1Stock()) ;
			//					}
			//				}
			//			}

			//			int i = 0;
			//			for(ArticleLotEntrepotDTO dtoTemp: temp){
			//				//soustraction des quantités qui serait déjà utilisé dans d'autre lignes de MP de cette fabrication
			//				listArticleLotEntrepot.remove(dtoTemp);
			//				dtoTemp.setQte1(dtoTemp.getQte1().subtract(qte.get(i)));
			//				i++;
			//				listArticleLotEntrepot.add(dtoTemp);
			//			}

			List<ArticleLotEntrepotDTO> filteredValues = new ArrayList<ArticleLotEntrepotDTO>();

			for (int j = 0; j < listArticleLotEntrepot.size(); j++) {
				//filtre en fonction de la saisie
				ArticleLotEntrepotDTO ale = listArticleLotEntrepot.get(j);
				if(numlot.equals("*") || ale.getNumLot().toLowerCase().contains(numlot.toLowerCase())) {
					filteredValues.add(ale);
				}
			}
			listArticleLotEntrepot = filteredValues;
			return listArticleLotEntrepot;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listArticleLotEntrepot;
	}

	@GET()
	@Path("/")
	@Operation(summary = "Retourne la liste des articles", tags = {MyApplication.TAG_ARTICLE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response getListeArticle() {

		List<TaArticleDTO> t = null;

		try {
			t = taArticleService.findAllLight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(t).build();

	}

	@POST()
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Création d'un nouvel article", tags = {MyApplication.TAG_ARTICLE})
	@Parameter(name = "X-Dossier", required = true, allowEmptyValue = false,in = ParameterIn.HEADER,description = "Dossier BDG")
	public Response postCreateArticle(
			@Parameter(required = true, allowEmptyValue = false) TaArticleDTO dto) {

		TaArticle t = new TaArticle();


		//    	try {
		//    		initTenantRegistry();
		//    		initTenant(dossier);
		//    		
		//    		///
		//    		String codeTiersDefaut = "C";
		//
		////			taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
		//			dto.setCodeTiers(taBonlivService.genereCode(null)); //ejb
		////			validateUIField(Const.C_CODE_TIERS,dto.getCodeTiers());//permet de verrouiller le code genere
		//
		//			dto.setCodeTTiers(codeTiersDefaut);
		//
		//			dto.setCodeCompta(dto.getCodeTiers()); //ejb
		////			validateUIField(Const.C_CODE_COMPTA,dto.getCodeCompta()); //ejb
		//			dto.setCompte("411"); //ejb
		////			validateUIField(Const.C_COMPTE,dto.getCompte()); //ejb
		//
		//			//TaTTiersDAO daoTTiers = new TaTTiersDAO(getEm());
		//			TaTTiers taTTiers;
		//
		//			taTTiers = taTTiersService.findByCode(codeTiersDefaut);
		//
		//			if(taTTiers!=null && taTTiers.getCompteTTiers()!=null) {
		//				dto.setCompte(taTTiers.getCompteTTiers());
		//				//this.taTTiers = taTTiers;
		//			} else {
		//				//dto.setCompte(TiersPlugin.getDefault().getPreferenceStore().
		//				//getString(PreferenceConstants.TIERS_COMPTE_COMPTALE_DEFAUT));
		//			}
		//			t.setTaTTiers(taTTiers);
		//			dto.setActifTiers(true);
		//			//swtTiers.setTtcTiers(TiersPlugin.getDefault().getPreferenceStore().
		//			//getBoolean(PreferenceConstants.TIERS_SAISIE_TTC_DEFAUT));
		//			dto.setCodeTTvaDoc("F");
		//    		////
		//			
		//			mapperUIToModel.map(dto, t);
		//    		
		//    		t = taBonlivService.merge(t);
		//			//System.out.println("BonLivRestService.getTiersById() : "+t.getCodeTiers());
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		return Response.status(200).entity(t).build();
	}

}
package fr.legrain.bdg.webapp.articles;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaCatalogueWebDTO;
import fr.legrain.article.dto.TaCategorieArticleDTO;
import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.dto.TaMarqueArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.dto.TaCatalogueWebDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.article.model.TaCategorieArticle;
import fr.legrain.article.model.TaFamille;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.article.model.TaPrix;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.article.model.TaCatalogueWeb;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCatalogueWebServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCategorieArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaCatalogueWebServiceRemote;
import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.general.model.TaFichier;
import fr.legrain.general.service.remote.ITaFichierServiceRemote;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibChaine;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped
public class CatalogueWebArticleController extends AbstractController implements Serializable {

	private TaCatalogueWebDTO dto = new TaCatalogueWebDTO();
	
	private TaArticle masterEntity ;

	private String modeEcranDefaut;

	private TaCatalogueWeb taCatalogueWeb = new TaCatalogueWeb();
	
	private TaCategorieArticleDTO taCategorieArticleDTO = null;
	private TaCategorieArticle taCategorieArticle = null;
		
	private List<TaCategorieArticleDTO> listeTaCategorieArticleDTO = null;

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaCatalogueWebServiceRemote taCatalogueWebService;
	private @EJB ITaCategorieArticleServiceRemote taCategorieArticleService;
	private @EJB ITaArticleServiceRemote taArticleService;
	
	private LgrDozerMapper<TaCatalogueWebDTO,TaCatalogueWeb> mapperUIToModel  = new LgrDozerMapper<TaCatalogueWebDTO,TaCatalogueWeb>();
	private LgrDozerMapper<TaCatalogueWeb,TaCatalogueWebDTO> mapperModelToUI  = new LgrDozerMapper<TaCatalogueWeb,TaCatalogueWebDTO>();
	
	private LgrDozerMapper<TaCategorieArticle,TaCategorieArticleDTO> mapperModelToUICategorie  = new LgrDozerMapper<TaCategorieArticle,TaCategorieArticleDTO>();
	
	@PostConstruct
	public void postConstruct(){
		try {
			refresh();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CatalogueWebArticleController() {  
	}  
	
	public void refresh(){
		refresh(null);
	}

	public void refresh(ActionEvent ev){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		try {
			if(params.get("idMasterEntity")!=null) {
				masterEntity = taArticleService.findById(LibConversion.stringToInteger(params.get("idMasterEntity")));
			}
//			values = TaCatalogueWebService.selectAllDTO();
//			values = TaCatalogueWebService.findByArticleLight(masterEntity.getIdArticle());
			
//			modeEcranDefaut = params.get("modeEcranDefaut");
//			if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
////				modeEcranDefaut = C_DIALOG;
//				actInserer(null);
//			} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
//				modeEcranDefaut = C_DIALOG;
				if(masterEntity!=null && masterEntity.getTaCatalogueWeb()!=null) {
					dto = taCatalogueWebService.findByIdDTO(masterEntity.getTaCatalogueWeb().getIdCatalogueWeb());
					taCatalogueWeb = taCatalogueWebService.findById(masterEntity.getTaCatalogueWeb().getIdCatalogueWeb());
					listeTaCategorieArticleDTO = taCategorieArticleService.findCategorieDTOByIdArticle(masterEntity.getIdArticle());
					if(LibChaine.empty(dto.getLibelleCatalogue())) { //s'il n'y a pas de libellé catalogue on reprend celui de l'article
						dto.setLibelleCatalogue(masterEntity.getLibellecArticle());
						taCatalogueWeb.setLibelleCatalogue(masterEntity.getLibellecArticle());
					}
				}
				actModifier(null);
				autoCompleteMapDTOtoUI();
//			} else {
//				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
//		mapLogos = new HashMap<>();
//		for (TaCatalogueWebDTO TaCatalogueWeb : values) {
//			mapLogos.put(TaCatalogueWeb.getId(), TaCatalogueWeb.getBlobImageOrigine()!=null ? new DefaultStreamedContent(new ByteArrayInputStream(TaCatalogueWeb.getBlobImageOrigine())):null);
//		}
	}

	public void actInserer(ActionEvent actionEvent){
//		nouveau = new TaCatalogueWebDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
//		nouveau = dto;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//		refresLogo();
	}

	public void actSupprimer(){
		TaCatalogueWeb TaCatalogueWeb = new TaCatalogueWeb();
		try {
			if(dto!=null && dto.getId()!=null){
				TaCatalogueWeb = taCatalogueWebService.findById(dto.getId());
			}

			taCatalogueWebService.remove(TaCatalogueWeb);

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Image article", "actSupprimer"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Image article", e.getCause().getCause().getCause().getCause().getMessage()));
		}
	}


	public void actEnregistrer(ActionEvent actionEvent){
		try {
			autoCompleteMapUIToDTO();
			
			TaCatalogueWebDTO retour = null;
			taCatalogueWeb=new TaCatalogueWeb();
			if(dto.getId()==null || taCatalogueWebService.findById(dto.getId()) == null){
				mapperUIToModel.map(dto, taCatalogueWeb);
				if(taCatalogueWeb.getTaPrixCatalogueDefaut()!=null) {
					taCatalogueWeb.getTaPrixCatalogueDefaut().setTaArticle(taArticleService.findById(masterEntity.getIdArticle()));
				}
//				taCatalogueWeb.setTaArticle(masterEntity);
				taCatalogueWeb = taCatalogueWebService.merge(taCatalogueWeb, ITaTCiviliteServiceRemote.validationContext);
				mapperModelToUI.map(taCatalogueWeb, dto);

//				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					TaArticle a = null;
					if(dto.getIdCategorie()!=null 
//							&& listeTaCategorieArticleDTO!=null 
//							&& !listeTaCategorieArticleDTO.isEmpty() 
//							&& listeTaCategorieArticleDTO.get(0).getId()!=dto.getIdCategorie()
							) {
						//TODO à changer quand il y aura plusieurs catégories
						a = taArticleService.affecteCategorie(masterEntity,taCategorieArticleService.findById(dto.getIdCategorie()));
					} else {
						a = taArticleService.supprimeTouteCategorie(masterEntity);
					}
					
//					if(taCatalogueWeb.getTaPrixCatalogueDefaut()!=null) {
//						taCatalogueWeb.getTaPrixCatalogueDefaut().setTaArticle(taArticleService.findById(masterEntity.getIdArticle()));
//					}
					//taCatalogueWeb = taCatalogueWebService.findById(dto.getId());
					taCatalogueWeb = a.getTaCatalogueWeb();
					mapperUIToModel.map(dto, taCatalogueWeb);
					//taCatalogueWeb.setNonDisponibleCatalogueWeb(LibConversion.booleanToInt(dto.getNonDisponibleCatalogueWeb()));
					if(taCatalogueWeb.getTaPrixCatalogueDefaut()!=null) {
						//taCatalogueWeb.getTaPrixCatalogueDefaut().setTaArticle(taArticleService.findById(masterEntity.getIdArticle()));
						taCatalogueWeb.getTaPrixCatalogueDefaut().setTaArticle(a);
					}
					
					taCatalogueWeb = taCatalogueWebService.merge(taCatalogueWeb, ITaTCiviliteServiceRemote.validationContext);
					
					
					mapperModelToUI.map(taCatalogueWeb, dto);

//					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			autoCompleteMapDTOtoUI();
//			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
//				PrimeFaces.current().dialog().closeDynamic(taCatalogueWeb);
//			}
////			refresLogo();
		} catch(Exception e) {
			e=ThrowableExceptionLgr.renvoieCauseRuntimeException(e);
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			//context.addMessage(null, new FacesMessage("Tiers", "actEnregistrer")); 
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Catalogue", e.getMessage()));
		}
	}
	
	public void actAnnuler(ActionEvent actionEvent){
//		nouveau = new TaCatalogueWebDTO();

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public TaCatalogueWebDTO getDto() {
		return dto;
	}

	public void setDto(TaCatalogueWebDTO selectedTaCatalogueWeb) {
		this.dto = selectedTaCatalogueWeb;
	}

	public void actFermerDialog(ActionEvent actionEvent) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public boolean etatBouton(String bouton) {
		boolean retour = true;
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_EDITION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		case C_MO_CONSULTATION:
			switch (bouton) {
			case "supprimer":
			case "modifier":
			case "inserer":
			case "imprimer":
			case "fermer":
				retour = false;
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}

		return retour;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public String getModeEcranDefaut() {
		return modeEcranDefaut;
	}

	public void setModeEcranDefaut(String modeEcranDefaut) {
		this.modeEcranDefaut = modeEcranDefaut;
	}
	
	public List<TaCategorieArticleDTO> categorieAutoCompleteLight(String query) {
        List<TaCategorieArticleDTO> allValues = taCategorieArticleService.findByCodeLight(query.toUpperCase());
        List<TaCategorieArticleDTO> filteredValues = new ArrayList<TaCategorieArticleDTO>();
        TaCategorieArticleDTO civ = new TaCategorieArticleDTO();
//        civ.setCodeFamille(Const.C_AUCUN); // il y en a pas besoin car on peut supprimer avec la croix
//        filteredValues.add(civ);
        for (int i = 0; i < allValues.size(); i++) {
        	 civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeCategorieArticle().toUpperCase().contains(query.toUpperCase())) {
                filteredValues.add(civ);
            }
        }
        return filteredValues;
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		valideAutoCompleteVide(nomChamp,value);
	}
	
	public void clearCategorie() {
		taCategorieArticleDTO = null;
	}
	
	public void valideAutoCompleteVide(String nomChamp,Object value ){
		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaFamilleDTO && ((TaFamilleDTO) value).getCodeFamille()!=null && ((TaFamilleDTO) value).getCodeFamille().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaUniteDTO && ((TaUniteDTO) value).getCodeUnite()!=null && ((TaUniteDTO) value).getCodeUnite().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTvaDTO && ((TaTvaDTO) value).getCodeTva()!=null && ((TaTvaDTO) value).getCodeTva().equals(Const.C_AUCUN))value=null;
			if(value instanceof TaTTvaDTO && ((TaTTvaDTO) value).getCodeTTva()!=null && ((TaTTvaDTO) value).getCodeTTva().equals(Const.C_AUCUN))value=null;	
			if(value instanceof TaMarqueArticle && ((TaMarqueArticle) value).getCodeMarqueArticle()!=null && ((TaMarqueArticle) value).getCodeMarqueArticle().equals(Const.C_AUCUN))value=null;	
		}
		
		validateUIField(nomChamp,value);
	}
	
	public void validateObject(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		
		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");
			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaCatalogueWebDTO>> violations = factory.getValidator().validateValue(TaCatalogueWebDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaCatalogueWebDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				validateUIField(nomChamp,value);
			}
		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}
	
	/**
	 * Creation d'un objet "TaPrix" pour l'objet "TaArticle" gerer par cet ecran
	 * dans le cas ou la propriete taPrix de ce dernier est nulle.
	 */
	public TaArticle initPrixArticleCatalogue(TaArticle taArticle,/*TaArticleDTO dto*/ TaCatalogueWeb taCatalogueWeb) {
		if(taCatalogueWeb.getTaPrixCatalogueDefaut()==null) {
			//initialisation du prix
			TaPrix p = new TaPrix();
			//if(uniteEstRempli(dto)){
				p.setPrixPrix(new BigDecimal(0));
				p.setPrixttcPrix(new BigDecimal(0));				
			//}
			p.setTaArticle(taArticle);
			taCatalogueWeb.setTaPrixCatalogueDefaut(p);
			taArticle.getTaPrixes().add(p);
			taArticle.setTaCatalogueWeb(taCatalogueWeb);
		}
		return taArticle;
	}

	public boolean validateUIField(String nomChamp,Object value) {
		try {
			if(nomChamp.equals(Const.C_PRIX_PRIX)) {

				TaPrixDTO dtoPrix = new TaPrixDTO();
				int change =0;

				if(value!=null && !value.equals("")) {
					masterEntity=initPrixArticleCatalogue(masterEntity,taCatalogueWeb);
				}
				PropertyUtils.setSimpleProperty(dtoPrix, nomChamp, value);
				if(value!=null && dto.getPrixPrix()!=null) {
					change =dtoPrix.getPrixPrix().compareTo(dto.getPrixPrix());
				} else {
					change=-1;
				}
				dtoPrix.setPrixttcPrix(dto.getPrixttcPrix());													

				if ((change!=0 || dtoPrix.getPrixPrix().compareTo(new BigDecimal(0))==0) && masterEntity.getTaCatalogueWeb()!=null){
					if( ( value==null || value.equals("") || value.equals(0) )  && masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut()!=null ){	
						masterEntity.removePrix(taCatalogueWeb.getTaPrixCatalogueDefaut());
						taCatalogueWeb.setTaPrixCatalogueDefaut(null);
					} else {
						if((value!=null && !value.equals(""))&& taCatalogueWeb.getTaPrixCatalogueDefaut()!=null){
							masterEntity=initPrixArticleCatalogue(masterEntity,taCatalogueWeb);						
							masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().setPrixPrix(dtoPrix.getPrixPrix());
							masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().setPrixttcPrix(dtoPrix.getPrixttcPrix());
							masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().majPrix();
							
							taCatalogueWeb.getTaPrixCatalogueDefaut().setPrixPrix(dtoPrix.getPrixPrix());
							taCatalogueWeb.getTaPrixCatalogueDefaut().setPrixttcPrix(dtoPrix.getPrixttcPrix());
							taCatalogueWeb.getTaPrixCatalogueDefaut().majPrix();
						}
					}
					if(masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut()!=null) {
					dto.setPrixPrix(masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().getPrixPrix());				
					dto.setPrixttcPrix(masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().getPrixttcPrix());	
					}
				}
			}else if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
				TaPrixDTO dtoPrix = new TaPrixDTO();
				int change =0;

				if(value!=null && !value.equals("")) {
					masterEntity=initPrixArticleCatalogue(masterEntity,taCatalogueWeb);
				}
				PropertyUtils.setSimpleProperty(dtoPrix, nomChamp, value);
				if(value!=null && dto.getPrixttcPrix()!=null) {
					change =dtoPrix.getPrixttcPrix().compareTo(dto.getPrixttcPrix());
				} else {
					change=-1;
				}
				dtoPrix.setPrixPrix(dto.getPrixPrix());						

				if((change!=0 || dtoPrix.getPrixttcPrix().compareTo(new BigDecimal(0))==0) && masterEntity.getTaCatalogueWeb()!=null){
					if((value==null||value.equals("")||value.equals(0))&& taCatalogueWeb.getTaPrixCatalogueDefaut()!=null){
						masterEntity.removePrix(taCatalogueWeb.getTaPrixCatalogueDefaut());
						taCatalogueWeb.setTaPrixCatalogueDefaut(null);
					} else {
						if((value!=null && !value.equals(""))&& masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut()!=null){
							masterEntity=initPrixArticleCatalogue(masterEntity,taCatalogueWeb);						
							masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().setPrixPrix(dtoPrix.getPrixPrix());
							masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().setPrixttcPrix(dtoPrix.getPrixttcPrix());
							masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().majPrixTTC();
							
							taCatalogueWeb.getTaPrixCatalogueDefaut().setPrixPrix(dtoPrix.getPrixPrix());
							taCatalogueWeb.getTaPrixCatalogueDefaut().setPrixttcPrix(dtoPrix.getPrixttcPrix());
							taCatalogueWeb.getTaPrixCatalogueDefaut().majPrix();
						}
					}
					dto.setPrixPrix(masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().getPrixPrix());				
					dto.setPrixttcPrix(masterEntity.getTaCatalogueWeb().getTaPrixCatalogueDefaut().getPrixttcPrix()); 						
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void autoCompleteMapUIToDTO() {

		if(taCategorieArticleDTO!=null) {
			valideAutoCompleteVide(Const.C_CODE_CATEGORIE_ARTICLE,taCategorieArticleDTO);
			dto.setCodeCategorie(taCategorieArticleDTO.getCodeCategorieArticle());
			dto.setIdCategorie(taCategorieArticleDTO.getId());
		} else {
			dto.setCodeCategorie(null);
			dto.setIdCategorie(null);
		}
	
	}
	

	public void autoCompleteMapDTOtoUI() {
		try {
			taCategorieArticle = null;
		
			taCategorieArticleDTO = null;

			if(listeTaCategorieArticleDTO!=null && !listeTaCategorieArticleDTO.isEmpty()) {
				//TODO à modifier quand il y aura plusieurs catégorie
				dto.setCodeCategorie(listeTaCategorieArticleDTO.get(0).getCodeCategorieArticle()); 
				if(dto.getCodeCategorie()!=null && !dto.getCodeCategorie().equals("")) {
					taCategorieArticle = taCategorieArticleService.findByCode(dto.getCodeCategorie());
					taCategorieArticleDTO = mapperModelToUICategorie.map(taCategorieArticle, TaCategorieArticleDTO.class);
				}
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public TaCatalogueWeb getTaCatalogueWeb() {
		return taCatalogueWeb;
	}

	public void setTaCatalogueWeb(TaCatalogueWeb TaCatalogueWeb) {
		this.taCatalogueWeb = TaCatalogueWeb;
	}

	public TaArticle getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaArticle masterEntity) {
		this.masterEntity = masterEntity;
	}

	public TaCategorieArticleDTO getTaCategorieArticleDTO() {
		return taCategorieArticleDTO;
	}

	public void setTaCategorieArticleDTO(TaCategorieArticleDTO taCategorieArticleDTO) {
		this.taCategorieArticleDTO = taCategorieArticleDTO;
	}
}

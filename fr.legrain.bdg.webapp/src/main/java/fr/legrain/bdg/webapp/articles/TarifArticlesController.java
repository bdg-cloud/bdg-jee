package fr.legrain.bdg.webapp.articles;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
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
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaPrixDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaPrix;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaPrixServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.LgrDozerMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.multi_tarifs.TaLMultiTarifDTOJSF;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ExceptLgr;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTBanqueDTO;
import fr.legrain.tiers.model.TaRPrix;
import fr.legrain.tiers.model.TaRPrixTiers;
import fr.legrain.tiers.model.TaTTarif;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped  
public class TarifArticlesController extends AbstractController implements Serializable { 
	


	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	

	
	private String paramId;
	
	private TabView tabViewArticle; //Le Binding sur les onglets "secondaire" semble ne pas fonctionné, les onglets ne sont pas générés au bon moment avec le c:forEach

	private List<TaLMultiTarifDTOJSF> values; 
//	private List<TaPrixDTO> valuesFiltered; 
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaTTarifServiceRemote taTTarifService;
	private @EJB ITaPrixServiceRemote taPrixService;

	
	private TaPrix taPrix = new TaPrix();
	private TaLMultiTarifDTOJSF selectedTaPrixDTO = new TaLMultiTarifDTOJSF();
	
	
	private boolean saisieHT=true;
	private String titreSaisieHt="Saisie des prix en HT";
	private String titreSaisieTtc="Saisie des prix en TTC";
	
	private LgrDozerMapper<TaPrixDTO,TaPrix> mapperUIToModel  = new LgrDozerMapper<TaPrixDTO,TaPrix>();
	private LgrDozerMapper<TaPrix,TaPrixDTO> mapperModelToUI  = new LgrDozerMapper<TaPrix,TaPrixDTO>();
	
	private TaArticle masterEntity;

	private String codeArticle;
	private String libelle;
	private BigDecimal tauxTva;



	
	public TarifArticlesController() {  
	}  

	@PostConstruct
	public void postConstruct(){

		refresh(null);

	}
	public void refresh(){
		refresh(null);
	}
	
	public TaPrix recherchePrixRef() {
		for (TaPrix p : masterEntity.getTaPrixes()) {
			if(masterEntity.getTaPrix()!=null && masterEntity.getTaPrix().equals(p)) {
				return p;
			}
		}
		return null;
	}
	
	public TaPrix recherchePrixRefGrille(TaTTarif tt) {
		for (TaPrix prix : masterEntity.getTaPrixes()) {
			for (TaRPrix p : prix.getTaRPrixes()) {
				if(p.getTaTTarif()!=null && p.getTaTTarif().equals(tt)) {
					return p.getTaPrix();
				}
			}
		}
		return null;
	}

	
	public void refresh(ActionEvent ev){
		if(masterEntity!=null) {

			try {
				if(masterEntity.getIdArticle()!=0) masterEntity = taArticleService.findById(masterEntity.getIdArticle());
				values = new ArrayList<>();
				codeArticle=masterEntity.getCodeArticle();
				libelle=masterEntity.getLibellecArticle();
				if(masterEntity.getTaTva()!=null)tauxTva=masterEntity.getTaTva().getTauxTva();
//				values=taArticleService.remonteGrilleTarifaireComplete(codeArticle, "%", "%", "%");
				List<TaArticleDTO> liste=taArticleService.remonteGrilleTarifaireComplete(codeArticle, "%", "%", "%");
				for (TaArticleDTO articleDTO : liste) {
					
					TaLMultiTarifDTOJSF multi=new TaLMultiTarifDTOJSF();
					TaPrixDTO prixDTO=new TaPrixDTO();
					prixDTO.setAccepte(true);
					prixDTO.setId(articleDTO.getIdPrixCalc());
					prixDTO.setIdArticle(articleDTO.getId());
					prixDTO.setCodeArticle(articleDTO.getCodeArticle());
					prixDTO.setLibellecArticle(articleDTO.getLibellecArticle());
					prixDTO.setPrixPrix(articleDTO.getPrixPrix());
//					if(articleDTO.getPrixttcPrix()!=null)
//						prixDTO.setMtTva(articleDTO.getPrixttcPrix().subtract(articleDTO.getPrixPrix()));
//					else prixDTO.setMtTva(BigDecimal.ZERO);
					prixDTO.setPrixttcPrix(articleDTO.getPrixttcPrix());
					prixDTO.setTauxTva(articleDTO.getTauxTva());
					prixDTO.setCodeTTarif(articleDTO.getCodeTTarif());
					prixDTO.setCoef(articleDTO.getCoef());
					prixDTO.setPrixPrixCalc(articleDTO.getPrixPrixCalc());
					prixDTO.setPrixttcPrixCalc(articleDTO.getPrixttcPrixCalc());
					if(articleDTO.getPrixttcPrixCalc()!=null)
						prixDTO.setMtTva(articleDTO.getPrixttcPrixCalc().subtract(articleDTO.getPrixPrixCalc()));
					else prixDTO.setMtTva(BigDecimal.ZERO);					
					multi.setDto(prixDTO);
					multi.setArticleDTO(articleDTO);
					prixDTO.setCodeTiers(articleDTO.getCodeTiers());
					if(articleDTO.getCodeTiers()!=null && !articleDTO.getCodeTiers().equals(""))multi.setSupprimable(true);
					else if(rechercheSiPrixEstReferencePourAutrePrix(prixDTO))multi.setSupprimable(false);
					if(prixDTO.getPrixPrix()==null)prixDTO.setPrixPrix(BigDecimal.ZERO);
					if(prixDTO.getPrixttcPrix()==null)prixDTO.setPrixttcPrix(BigDecimal.ZERO);
					values.add(multi);

				}
				
			} catch (FinderException e) {

			}
			
			TaPrix taPrixRef=recherchePrixRef();
			if(taPrixRef!=null) {
				TaLMultiTarifDTOJSF obj = new TaLMultiTarifDTOJSF();
				obj.setDto(new TaPrixDTO());
				mapperModelToUI.map(taPrixRef, obj.getDto());
				obj.getDto().setLibellecArticle(masterEntity.getLibellecArticle());
				if(masterEntity.getTaTva()!=null)obj.getDto().setTauxTva(masterEntity.getTaTva().getTauxTva());
				obj.getDto().setCodeTTarif("Grille de référence");
				obj.setSupprimable(false);
				obj.getDto().setPrixPrixCalc(obj.getDto().getPrixPrix());
				obj.getDto().setPrixttcPrixCalc(obj.getDto().getPrixttcPrix());
				if(obj.getDto().getPrixttcPrixCalc()!=null)obj.getDto().setMtTva(obj.getDto().getPrixttcPrixCalc().subtract(obj.getDto().getPrixPrixCalc()));
				values.add(0,obj);
			}
			for (TaLMultiTarifDTOJSF obj : values) {
				TaPrix prix=new TaPrix();
				
				//recalculer tous les prix qui ont un coefficient
				if(obj.getDto().getCoef()!=null) {
					BigDecimal rajout=obj.getDto().getPrixPrix().multiply(obj.getDto().getCoef()).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
					prix.setPrixPrix(rajout.add(obj.getDto().getPrixPrix()));
					prix.setPrixttcPrix(BigDecimal.ZERO);
					if(obj.getDto().getPrixPrix()==null || obj.getDto().getPrixPrix().equals("") ||obj.getDto().getPrixPrix().compareTo(BigDecimal.ZERO)<=0)
						prix.setPrixttcPrix(prix.getPrixttcPrix());	
					try {
						prix.majPrix(obj.getDto().getTauxTva());
						if(obj.getDto().getPrixPrixCalc().compareTo(prix.getPrixPrix())!=0)
							obj.getDto().setDiff(true);
						if(obj.getDto().getPrixttcPrixCalc().compareTo(prix.getPrixttcPrix())!=0)
							obj.getDto().setDiff(true);
					} catch (ExceptLgr e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			if(!values.isEmpty()&&selectedTaPrixDTO==null)
				selectedTaPrixDTO=values.get(0);
		}
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void refreshddd(ActionEvent ev){

		if(masterEntity!=null) {

			try {
				if(masterEntity.getIdArticle()!=0) masterEntity = taArticleService.findById(masterEntity.getIdArticle());
				values = new ArrayList<>();
				codeArticle=masterEntity.getCodeArticle();
				libelle=masterEntity.getLibellecArticle();
				if(masterEntity.getTaTva()!=null)tauxTva=masterEntity.getTaTva().getTauxTva();
			} catch (FinderException e) {

			}
			TaPrix taPrixRef=recherchePrixRef();
			if(taPrixRef!=null) {
				TaLMultiTarifDTOJSF obj = new TaLMultiTarifDTOJSF();
				obj.setDto(new TaPrixDTO());
				mapperModelToUI.map(taPrix, obj.getDto());
				obj.getDto().setLibellecArticle(masterEntity.getLibellecArticle());
				if(masterEntity.getTaTva()!=null)obj.getDto().setTauxTva(masterEntity.getTaTva().getTauxTva());
				obj.getDto().setCodeTTarif("Grille de référence");
				//					obj.setSupprimable(false);
				taPrixRef=taPrix;
				obj.getDto().setPrixPrixCalc(obj.getDto().getPrixPrix());
				obj.getDto().setPrixttcPrixCalc(obj.getDto().getPrixttcPrix());
				if(obj.getDto().getPrixttcPrixCalc()!=null)obj.getDto().setMtTva(obj.getDto().getPrixttcPrixCalc().subtract(obj.getDto().getPrixPrixCalc()));
				values.add(obj);
			}
			
			
			
			for (TaPrix taPrix : masterEntity.getTaPrixes()) {
				if(masterEntity.getTaPrix()==null || !masterEntity.getTaPrix().equals(taPrix)) {
					//si ce n'est pas le prix de référence récupéré auparavant, alors je récupère
					TaLMultiTarifDTOJSF obj = new TaLMultiTarifDTOJSF();
					obj.setDto(new TaPrixDTO());
					mapperModelToUI.map(taPrix, obj.getDto());
					obj.getDto().setLibellecArticle(masterEntity.getLibellecArticle());
					if(masterEntity.getTaTva()!=null)obj.getDto().setTauxTva(masterEntity.getTaTva().getTauxTva());
					
					if(!taPrix.getTaRPrixes().isEmpty()) {
					// je recherche le prix de référence de la grille pour le mettre en premier dans les éléments de la grille
						taPrixRef=recherchePrixRefGrille(taPrix.getTaRPrixes().iterator().next().getTaTTarif());
					}
					for (TaRPrix RPrix : taPrix.getTaRPrixes()) {
						if(!RPrix.getTaPrix().equals(taPrixRef)) {
						if(RPrix.getTaTTarif()!=null) {
							
							obj.getDto().setCodeTTarif(RPrix.getTaTTarif().getCodeTTarif());
						}
						if(taPrixRef!=null) {
							obj.getDto().setPrixPrix(taPrixRef.getPrixPrix());
							obj.getDto().setPrixttcPrix(taPrixRef.getPrixttcPrix());
						}
						if(RPrix.getTaPrix()!=null) {
							obj.getDto().setPrixPrixCalc(RPrix.getTaPrix().getPrixPrix());
							obj.getDto().setPrixttcPrixCalc(RPrix.getTaPrix().getPrixttcPrix());
							if(obj.getDto().getPrixttcPrixCalc()!=null)obj.getDto().setMtTva(obj.getDto().getPrixttcPrixCalc().subtract(obj.getDto().getPrixPrixCalc()));
							obj.getDto().setCoef(RPrix.getCoef());
						}
						values.add(obj);
						}
					}
					for (TaRPrixTiers RPrixTiers : taPrix.getTaRPrixesTiers()) {
						//récupérer le prix de la grille si idem codeTiers et codeTTarif
						taPrixRef=recherchePrixCodeTTarif(RPrixTiers.getTaTTarif());
						if(taPrixRef!=null) {
							obj.getDto().setPrixPrix(taPrixRef.getPrixPrix());
							obj.getDto().setPrixttcPrix(taPrixRef.getPrixttcPrix());
						}
						if(RPrixTiers.getTaTTarif()!=null)obj.getDto().setCodeTTarif(RPrixTiers.getTaTTarif().getCodeTTarif());
						if(RPrixTiers.getTaTiers()!=null)obj.getDto().setCodeTiers(RPrixTiers.getTaTiers().getCodeTiers());
						if(RPrixTiers.getTaPrix()!=null) {
							obj.getDto().setPrixPrixCalc(RPrixTiers.getTaPrix().getPrixPrix());
							obj.getDto().setPrixttcPrixCalc(RPrixTiers.getTaPrix().getPrixttcPrix());
							if(obj.getDto().getPrixttcPrixCalc()!=null)obj.getDto().setMtTva(obj.getDto().getPrixttcPrixCalc().subtract(obj.getDto().getPrixPrixCalc()));
							obj.getDto().setCoef(RPrixTiers.getCoef());
						}
					}


					//rechercher s'il y a une différence entre le prix calculé enregistré et le prix que l'on peut calculer à partir du coef enregistré
					TaPrix prix=new TaPrix();						
					//recalculer tous les prix qui ont un coefficient
					if(obj.getDto().getCoef()!=null) {
						BigDecimal rajout=obj.getDto().getPrixPrix().multiply(obj.getDto().getCoef()).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
						prix.setPrixPrix(rajout.add(obj.getDto().getPrixPrix()));
						prix.setPrixttcPrix(BigDecimal.ZERO);
						if(obj.getDto().getPrixPrix()==null || obj.getDto().getPrixPrix().equals("") ||obj.getDto().getPrixPrix().compareTo(BigDecimal.ZERO)<=0)
							prix.setPrixttcPrix(prix.getPrixttcPrix());	
						try {
							prix.majPrix(obj.getDto().getTauxTva());
							if(obj.getDto().getPrixPrixCalc().compareTo(prix.getPrixPrix())!=0)
								obj.getDto().setDiff(true);
							if(obj.getDto().getPrixttcPrixCalc().compareTo(prix.getPrixttcPrix())!=0)
								obj.getDto().setDiff(true);
						} catch (ExceptLgr e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					values.add(obj);
				}
			}

			if(!values.isEmpty()&&selectedTaPrixDTO==null)
				selectedTaPrixDTO=values.get(0);
		}
		autoCompleteMapDTOtoUI();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	
	public TaPrix recherchePrixCodeTTarif(TaTTarif taTTarif) {
		for (TaPrix taPrix : masterEntity.getTaPrixes()) {
			for (TaRPrix rPrix : taPrix.getTaRPrixes()) {
				if(rPrix.getTaTTarif().equals(taTTarif)) {
					return rPrix.getTaPrix();
				}
			}
		}
		return null;
	}
	
	public TaPrix recherchePrixCodeTTarifTiers(TaTTarif taTTarif,TaTiers taTiers) {
		for (TaPrix taPrix : masterEntity.getTaPrixes()) {
			for (TaRPrixTiers rPrixTiers : taPrix.getTaRPrixesTiers()) {
				if(rPrixTiers.getTaTTarif().equals(taTTarif) && 
						rPrixTiers.getTaTiers().equals(taTiers) ) {
					return rPrixTiers.getTaPrix();
				}
			}
		}
		return null;
	}
	
	public List<TaLMultiTarifDTOJSF> getValues(){  
		return values;
	}
	


	public void actAnnuler(ActionEvent actionEvent) {

			refresh(null);
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
	}
	
	public void autoCompleteMapUIToDTO() {

		
	}
	
	public void autoCompleteMapDTOtoUI() {
		try {


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	


	public TaPrix recherchePrixDansArticle(TaPrixDTO dto){
		for (TaPrix adr : masterEntity.getTaPrixes()) {
			if(adr.getIdPrix()==dto.getId()) {
				return adr;
			}
		}
		return null;
	}

	

	public boolean rechercheSiPrixEstReferencePourAutrePrix(TaPrixDTO dto){
		if(dto.getCodeTiers()!=null && !dto.getCodeTiers().equals(""))return false;
		TaPrix prix=recherchePrixDansArticle(dto);
		if(prix!=null) {
			if(prix.getTaArticle()!=null && prix.getTaArticle().getTaPrixes()!=null )
				for (TaPrix p : prix.getTaArticle().getTaPrixes()) {
					if(!prix.equals(p)) {
						for (TaRPrix obj : p.getTaRPrixes()) {
							if(obj.getTaTTarif()!=null && obj.getTaTTarif().getCodeTTarif().equals(dto.getCodeTTarif()))
								return true;
						}

						for (TaRPrixTiers obj : p.getTaRPrixesTiers()) {
							if(obj.getTaTTarif()!=null && obj.getTaTTarif().getCodeTTarif().equals(dto.getCodeTTarif()))
								return true;
						}	
					}

				}
		}
		return false;
	}
	

	


				
		public void actEnregistrer(ActionEvent actionEvent){
			try {
				boolean enregistre=false;
			TaArticle article= taArticleService.findById(masterEntity.getIdArticle());
			for (TaLMultiTarifDTOJSF obj : values) {
				if(obj.isModifie()) {
					TaPrix prix = new TaPrix();
					TaArticle art;
					TaTTarif tTarif;
					TaRPrix taRPrix=null;

//						tTarif=taTTarifService.findByCode(taTTarifDTO.getCodeTTarif());
//						majDesPrixCalcules(obj.getDto().getCoef());
//
//						if(obj.getArticleDTO().getIdPrixCalc()!=null && obj.getArticleDTO().getIdPrixCalc()!=0) {
//							prix=taPrixService.findById(obj.getArticleDTO().getIdPrixCalc());
//							if(prix.getTaRPrixes()!=null) {
//								for (TaRPrix rprix : prix.getTaRPrixes()) {
//									if(rprix.getTaTTarif()!=null && tTarif!=null && rprix.getTaTTarif().getCodeTTarif().equals(tTarif.getCodeTTarif())) {
//										taRPrix=rprix;
//									}
//								}
//							}
//						}else {
//							art = taArticleService.findByCode(obj.getArticleDTO().getCodeArticle());
//							taRPrix=new TaRPrix();
//							prix.setTaArticle(art);
//							taRPrix.setTaPrix(prix);
//						}
//
//						prix.setPrixPrix(obj.getDto().getPrixPrixCalc());
//						prix.setPrixttcPrix(obj.getDto().getPrixttcPrixCalc());

						if(obj.isSuppression() && !rechercheSiPrixEstReferencePourAutrePrix(obj.getDto())) {

							for (TaPrix p : article.getTaPrixes()) {
								if(p.getIdPrix()==(obj.getDto().getId()))
									prix=p;
							}
							article.removePrix(prix);
							enregistre=true;

						}


				}
			}
			if(enregistre)masterEntity=taArticleService.merge(article);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			refresh();
		}

	public void actInserer(ActionEvent actionEvent) {

	}
	
	public void actModifier() {
		actModifier(null);
	}

	public void actModifier(ActionEvent actionEvent) {
		
		try {
			//taCompteBanque = taPrixService.findById(selectedTaPrixDTO.getId());
			for (TaPrix prix : masterEntity.getTaPrixes()) {
				if(prix.getIdPrix()==selectedTaPrixDTO.getDto().getId()) {
					taPrix = prix;
				}
			}
		
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Article", "actModifier"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void actRecalculerTarifsLigne() {
		
		try {
			TaPrix prix=new TaPrix();
//			for (TaPrixDTO dto : values) {
				if(selectedTaPrixDTO.getDto().getCoef()!=null ) {
					//rechercher le prix dans masterEntity et le modifier
					prix=recherchePrixDansArticle(selectedTaPrixDTO.getDto());
					//recalculer tous les prix qui ont un coefficient
					BigDecimal rajout=selectedTaPrixDTO.getDto().getPrixPrix().multiply(selectedTaPrixDTO.getDto().getCoef()).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
					prix.setPrixPrix(rajout.add(selectedTaPrixDTO.getDto().getPrixPrix()));
					prix.setPrixttcPrix(BigDecimal.ZERO);
					if(selectedTaPrixDTO.getDto().getPrixPrix()==null || selectedTaPrixDTO.getDto().getPrixPrix().equals("") ||selectedTaPrixDTO.getDto().getPrixPrix().compareTo(BigDecimal.ZERO)<=0)
						prix.setPrixttcPrix(prix.getPrixttcPrix());	
					prix.majPrix();
					selectedTaPrixDTO.getDto().setPrixPrixCalc(prix.getPrixPrix());				
					selectedTaPrixDTO.getDto().setPrixttcPrixCalc(prix.getPrixttcPrix()); 					
				}
//			}
			masterEntity = taArticleService.merge(masterEntity);
			
			masterEntity = taArticleService.findById(masterEntity.getIdArticle());
			refresh();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Article", "actRecalculerTarifs"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actRecalculerTarifs(ActionEvent actionEvent) {
		
		try {
			TaPrix prix=new TaPrix();
			for (TaLMultiTarifDTOJSF dto : values) {
				if(dto.getDto().getCoef()!=null ) {
					//rechercher le prix dans masterEntity et le modifier
					prix=recherchePrixDansArticle(dto.getDto());
					//recalculer tous les prix qui ont un coefficient
					BigDecimal rajout=dto.getDto().getPrixPrix().multiply(dto.getDto().getCoef()).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
					prix.setPrixPrix(rajout.add(dto.getDto().getPrixPrix()));
					prix.setPrixttcPrix(BigDecimal.ZERO);
					if(dto.getDto().getPrixPrix()==null || dto.getDto().getPrixPrix().equals("") ||dto.getDto().getPrixPrix().compareTo(BigDecimal.ZERO)<=0)
						prix.setPrixttcPrix(prix.getPrixttcPrix());	
					prix.majPrix();
					dto.getDto().setPrixPrixCalc(prix.getPrixPrix());				
					dto.getDto().setPrixttcPrixCalc(prix.getPrixttcPrix()); 					
				}
			}
			masterEntity = taArticleService.merge(masterEntity);
			
			masterEntity = taArticleService.findById(masterEntity.getIdArticle());
			refresh();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Article", "actRecalculerTarifs"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void actAide(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 320);
        Map<String,List<String>> params = null;
        PrimeFaces.current().dialog().openDynamic("aide", options, params);
		
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Article", "actAide"));
		}
	}
	
//	public void actAideRetour(ActionEvent actionEvent) {
//		
//	}
	   
    public void nouveau(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_ARTICLE);
		b.setTitre("Article");
		b.setStyle(LgrTab.CSS_CLASS_TAB_TIERS);
		b.setTemplate("tiers/tiers.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TIERS);
		b.setParamId("0");
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
    	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Article", 
	    		"Nouveau"
	    		)); }
    } 
    
    public void supprimer(ActionEvent actionEvent) {
    	actSupprimer(actionEvent);
    }
    
    public void detail() {
    	detail(null);
    }
    
    public void detail(ActionEvent actionEvent) {
    	onRowSelect(null);
    }    
	
	public void actSupprimer() {
		actSupprimer(null);
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		TaPrix taPrix = new TaPrix();
		try {
			if(selectedTaPrixDTO!=null && selectedTaPrixDTO.getDto().getId()!=null){
				taPrix = taPrixService.findById(selectedTaPrixDTO.getDto().getId());
			}
			taPrix.setTaArticle(null);
			taPrixService.remove(taPrix);
			values.remove(selectedTaPrixDTO);
			
			if(!values.isEmpty()) {
				selectedTaPrixDTO = values.get(0);
			}else {
				selectedTaPrixDTO=new TaLMultiTarifDTOJSF();
			}
			updateTab();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Article", "actSupprimer"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Article", e.getCause().getCause().getCause().getCause().getMessage()));
		} 	    
	}

	public void actFermer(ActionEvent actionEvent) {
		
		switch (modeEcran.getMode()) {
		case C_MO_INSERTION:
			actAnnuler(actionEvent);
			break;
		case C_MO_EDITION:
			actAnnuler(actionEvent);							
			break;

		default:
			break;
		}
		selectedTaPrixDTO=new TaLMultiTarifDTOJSF();
	
	}

	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Article", "actImprimer"));
		}
		try {
			
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("tiers", taArticleService.findById(selectedTaPrixDTO.getDto().getId()));
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Test génération de fichier PDF
		
//		HashMap<String,Object> hm = new HashMap<>();
//		hm.put( "tiers", taTiersService.findById(selectedTaPrixDTO.getId()));
//		BirtUtil.setAppContextEdition(hm);
//		
//		BirtUtil.startReportEngine();
//		
////		BirtUtil.renderReportToFile(
////				"/donnees/Projet/Java/Eclipse/GestionCommerciale_branche_2_0_13_JEE_E46/fr.legrain.bdg.webapp/src/main/webapp/reports/tiers/FicheTiers.rptdesign", 
////				"/tmp/tiers.pdf", 
////				new HashMap<>(), 
////				BirtUtil.PDF_FORMAT);
//		
//		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("/reports/tiers/FicheTiers.rptdesign");
//		BirtUtil.renderReportToFile(
//				inputStream, 
//				"/tmp/tiers.pdf", 
//				new HashMap<>(), 
//				BirtUtil.PDF_FORMAT);
		////////////////////////////////////////////////////////////////////////////////////////
		//java.net.URL.setURLStreamHandlerFactory();
//		taTiersService.generePDF(selectedTaPrixDTO.getId());
		////////////////////////////////////////////////////////////////////////////////////////
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaPrixDTO.getId()));
			System.out.println("TiersController.actImprimer()");
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_ARTICLE);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 
	
	public void onRowSimpleSelect(SelectEvent event) {
		
		if(pasDejaOuvert()==false){
			onRowSelect(event);
			
			autoCompleteMapDTOtoUI();
		} else {
			tabViews.selectionneOngletCentre(LgrTab.TYPE_TAB_ARTICLE);
		}
	} 
	public void updateTab(){
		try {

		autoCompleteMapDTOtoUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {  
		
		updateTab();
		

	} 
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actDialogTypeCompteBanque(ActionEvent actionEvent) {
		
  
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("modal", true);
        

        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("tiers/dialog_type_banque", options, params);
 	    
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
			case "inserer":
			case "fermer":
				retour = false;
				break;
			case "supprimerListe":retour = false;break;	
			case "supprimer":
			case "modifier":
			case "imprimer":
				if(selectedTaPrixDTO!=null && selectedTaPrixDTO.getDto().getId()!=null  && selectedTaPrixDTO.getDto().getId()!=0 ) retour = false;
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

	public void validateCompteBanque(FacesContext context, UIComponent component, Object value) throws ValidatorException {
	
		String messageComplet = "";
		
		try {
		 
		  String nomChamp =  (String) component.getAttributes().get("nomChamp");
		  		  
//			//validation automatique via la JSR bean validation
		  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		  Set<ConstraintViolation<TaPrixDTO>> violations = factory.getValidator().validateValue(TaPrixDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";

				for (ConstraintViolation<TaPrixDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));

			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
					 validateUIField(nomChamp,value);
			}


		} catch(Exception e) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}
	

	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		
		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");

		if(value!=null) {
			if(value instanceof String && value.equals(Const.C_AUCUN))value="";	
			if(value instanceof TaTBanqueDTO && ((TaTBanqueDTO) value).getCodeTBanque()!=null && ((TaTBanqueDTO) value).getCodeTBanque().equals(Const.C_AUCUN))value=null;
		}
		
		validateUIField(nomChamp,value);
	}
	
	




	public TaLMultiTarifDTOJSF getSelectedTaPrixDTO() {
		return selectedTaPrixDTO;
	}

	public void setSelectedTaPrixDTO(TaLMultiTarifDTOJSF selectedTaPrixDTO) {
		this.selectedTaPrixDTO = selectedTaPrixDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	} 


	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}
	

	public TaPrix getTaTiers() {
		return taPrix;
	}

	public void setTaTiers(TaPrix taCompteBanque) {
		this.taPrix = taCompteBanque;
	}

	public TabView getTabViewArticle() {
		return tabViewArticle;
	}

	public void setTabViewArticle(TabView tabViewArticle) {
		this.tabViewArticle = tabViewArticle;
	}

	public TaArticle getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaArticle masterEntity) {
		this.masterEntity = masterEntity;
	}

	
	public boolean validateUIField(String nomChamp,Object value) {
		boolean changement=false;
		try {
			if(nomChamp.equals(Const.C_PRIX_PRIX)) {
				if(value==null)value=BigDecimal.ZERO;
//				if(value!=null && !value.equals("")) {
					TaPrix prix=new TaPrix();
					prix.setPrixPrix((BigDecimal) value);
					prix.setPrixttcPrix(selectedTaPrixDTO.getDto().getPrixttcPrix());
					if(selectedTaPrixDTO.getDto().getPrixttcPrix()==null || selectedTaPrixDTO.getDto().getPrixttcPrix().equals("") ||selectedTaPrixDTO.getDto().getPrixttcPrix().compareTo(BigDecimal.ZERO)<=0)
						prix.setPrixttcPrix(prix.getPrixPrix());
					prix.majPrix(selectedTaPrixDTO.getDto().getTauxTva());
					selectedTaPrixDTO.getDto().setPrixPrixCalc(prix.getPrixPrix());				
					selectedTaPrixDTO.getDto().setPrixttcPrixCalc(prix.getPrixttcPrix());
//				}
			}else 
				if(nomChamp.equals(Const.C_PRIXTTC_PRIX)) {
					if(value==null)value=BigDecimal.ZERO;
//					if(value!=null && !value.equals("")) {
						TaPrix prix=new TaPrix();
						prix.setPrixPrix(selectedTaPrixDTO.getDto().getPrixPrix());
						prix.setPrixttcPrix((BigDecimal) value);
						if(selectedTaPrixDTO.getDto().getPrixPrix()==null || selectedTaPrixDTO.getDto().getPrixPrix().equals("") ||selectedTaPrixDTO.getDto().getPrixPrix().compareTo(BigDecimal.ZERO)<=0)
							prix.setPrixttcPrix(prix.getPrixttcPrix());						
						prix.majPrixTTC(selectedTaPrixDTO.getDto().getTauxTva());
						selectedTaPrixDTO.getDto().setPrixPrixCalc(prix.getPrixPrix());				
						selectedTaPrixDTO.getDto().setPrixttcPrixCalc(prix.getPrixttcPrix());
//					}
				}else
					if(nomChamp.equals(Const.C_COEFFICIENT)) {
						if(selectedTaPrixDTO.getDto().getPrixPrix()==null)selectedTaPrixDTO.getDto().setPrixPrix(BigDecimal.ZERO) ;
						if(value==null)value=BigDecimal.ZERO;
							TaPrix prix=new TaPrix();
							BigDecimal rajout=selectedTaPrixDTO.getDto().getPrixPrix().multiply((BigDecimal) value).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
							prix.setPrixPrix(rajout.add(selectedTaPrixDTO.getDto().getPrixPrix()));
							prix.setPrixttcPrix(BigDecimal.ZERO);
							if(selectedTaPrixDTO.getDto().getPrixPrix()==null || selectedTaPrixDTO.getDto().getPrixPrix().equals("") ||selectedTaPrixDTO.getDto().getPrixPrix().compareTo(BigDecimal.ZERO)<=0)
								prix.setPrixttcPrix(prix.getPrixttcPrix());						
							prix.majPrix(selectedTaPrixDTO.getDto().getTauxTva());
							selectedTaPrixDTO.getDto().setPrixPrixCalc(prix.getPrixPrix());				
							selectedTaPrixDTO.getDto().setPrixttcPrixCalc(prix.getPrixttcPrix()); 
						
					}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public String titreSaisie() {
		if(isSaisieHT())return titreSaisieHt;
		else return titreSaisieTtc;
	}

	public void recalculCoef(boolean surTTC) {
		BigDecimal coef=BigDecimal.ZERO;
		BigDecimal diff=BigDecimal.ZERO;

		if(!surTTC) {
			if(selectedTaPrixDTO.getDto().getPrixPrixCalc().compareTo(BigDecimal.ZERO)==0)
				coef=BigDecimal.ZERO;
			else {
				diff=selectedTaPrixDTO.getDto().getPrixPrixCalc().subtract(selectedTaPrixDTO.getDto().getPrixPrix());
				//			coef=selectedTaPrixDTO.getPrixPrix().divide(BigDecimal.valueOf(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				coef=diff.divide(selectedTaPrixDTO.getDto().getPrixPrix(),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));				
			}
			selectedTaPrixDTO.getDto().setCoef(coef);
			majPrixHT(selectedTaPrixDTO.getDto().getPrixPrixCalc());
		}else {
			if(selectedTaPrixDTO.getDto().getPrixttcPrixCalc().compareTo(BigDecimal.ZERO)==0)
				coef=BigDecimal.ZERO;
			else {
				diff=selectedTaPrixDTO.getDto().getPrixttcPrixCalc().subtract(selectedTaPrixDTO.getDto().getPrixttcPrix());
				//			coef=selectedTaPrixDTO.getPrixttcPrixCalc().divide(BigDecimal.valueOf(100),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP);
				coef=diff.divide(selectedTaPrixDTO.getDto().getPrixttcPrix(),MathContext.DECIMAL128).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
			}
			selectedTaPrixDTO.getDto().setCoef(coef);
			majPrixTTC(selectedTaPrixDTO.getDto().getPrixttcPrixCalc());
		}
		//		majDesPrixCalcules(coef);
	}

	
	public void majDesPrixCalcules(BigDecimal value) {
		validateUIField(Const.C_COEFFICIENT,value);
	}
	public void majPrixHT(BigDecimal value) {
		validateUIField(Const.C_PRIX_PRIX,value);
	}
	public void majPrixTTC(BigDecimal value) {
		validateUIField(Const.C_PRIXTTC_PRIX,value);
	}

	public boolean isSaisieHT() {
		return saisieHT;
	}

	public void setSaisieHT(boolean saisieHT) {
		this.saisieHT = saisieHT;
	}
	
	public void actSupprimerLigne(){
		selectedTaPrixDTO.setSuppression(true);
		selectedTaPrixDTO.setModifie(true);

		actModifier(null);
	}
	
	public boolean suppressionAutorisee(TaLMultiTarifDTOJSF ligne) {
		if(ligne == null || ligne.isSuppression() || !ligne.isSupprimable())
			return false;
		return true;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public BigDecimal getTauxTva() {
		return tauxTva;
	}

	public void setTauxTva(BigDecimal tauxTva) {
		this.tauxTva = tauxTva;
	}

}  

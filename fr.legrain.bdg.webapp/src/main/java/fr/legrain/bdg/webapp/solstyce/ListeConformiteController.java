package fr.legrain.bdg.webapp.solstyce;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaBaremeServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaGroupeServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaListeConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaModeleConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaTypeConformiteServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.webapp.SessionListener;
import fr.legrain.conformite.model.TaBareme;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaGroupe;
import fr.legrain.conformite.model.TaModeleBareme;
import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.conformite.model.TaTypeConformite;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named("listeConformiteController")
@ViewScoped  
public class ListeConformiteController implements Serializable {  

	private LinkedList<TaConformite> values; 
	private List<TaConformite> filteredValues; 
	private TaConformite nouveau ;
	private TaBareme nouveauBareme;
	private TaConformite selection;
	private TaBareme selectionBareme;
	private TaArticle masterEntity ;
	
	private Boolean tableVide;
	
	private List<TaTypeConformite> listeTypeControle;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private ModeObjetEcranServeur modeEcranBareme = new ModeObjetEcranServeur();

	private @EJB ITaListeConformiteServiceRemote taListeConformiteService;
	private @EJB ITaGroupeServiceRemote taGroupeService;
	private @EJB ITaTypeConformiteServiceRemote taTypeConformiteService;
	private @EJB ITaConformiteServiceRemote taConformiteService;
	private @EJB ITaBaremeServiceRemote taBaremeService;
	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaModeleConformiteServiceRemote taModeleConformiteService;
	private @EJB ITaGenCodeExServiceRemote taGenCodeExService;
	
	private StreamedContent file;

	@PostConstruct
	public void postConstruct(){
		try {

			
			setTableVide(false);
			
			//values =  taConformiteService.selectAll();
			//values =  taConformiteService.findByCodeArticle("AA");
			
			//values = new ArrayList<TaConformite>();
			
			//masterEntity = taArticleService.findByCode("AA");
			//masterEntity = taArticleService.findById(masterController.getSelectedTaArticleDTO().getId());
			//values.addAll(masterEntity.getTaConformites());
			refresh();
			
			listeTypeControle = taTypeConformiteService.selectAll();
			
			if(values == null){
				setTableVide(true);
			}
			if(!values.isEmpty()) {
				selection = values.get(0);	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setFilteredValues(values);
	}
	
	public void actSupprimerFichier(ActionEvent actionEvent){
		nouveauBareme.setBlobFichier(null);
		nouveauBareme.setCheminDoc(null);
//		if(nouveauBareme.getIdBareme()!=0) {
//			int id = nouveauBareme.getIdBareme();
//			taBaremeService.removeOID(nouveauBareme);
//			actEnregistrerBareme(actionEvent);
//			try {
//				nouveauBareme = taBaremeService.findById(id);
//				nouveauBareme.setBlobFichier(null);
//				nouveauBareme.setCheminDoc(null);
//			} catch (FinderException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

	}
	
	public StreamedContent getFile() {
	   // InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/images/optimus.jpg");
       // file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
		InputStream stream = new ByteArrayInputStream(nouveauBareme.getBlobFichier()); 
		file = new DefaultStreamedContent(stream,null,nouveauBareme.getCheminDoc());
        return file;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        
        try {
        	nouveauBareme.setCheminDoc(event.getFile().getFileName());
        	nouveauBareme.setBlobFichier(IOUtils.toByteArray(event.getFile().getInputStream()));
//			fichier.setBlobFichierMiniature(fichier.resizeImage(IOUtils.toByteArray(event.getFile().getInputstream()), event.getFile().getFileName(), 640, 480));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        Integer idPiece = (Integer) event.getComponent().getAttributes().get("idPiece");
//    	
//    	piece = stockService.findById(idPiece);
//    	
//    	ImagePiece img = new ImagePiece();
//    	if(piece.getImages()==null) {
//    		piece.setImages(new ArrayList<ImagePiece>());
//    	}
//    	String nomFichier = "img_"+idPiece+"_"+piece.getImages().size()/*+"."*/+event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf("."));
//    	img.setChemin(nomFichier);
// 
//
//    	Path target3 = Paths.get("/var/careco/images/"+nomFichier);
//    	try {
//			Files.copy(event.getFile().getInputstream(), target3);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//    	
//    	img.setIdPiece(piece);
//    	piece.getImages().add(img);
//    	
//    	piece = stockService.enregistrerMerge(piece);
    }
	
	public void refresh(){
		values = new LinkedList<TaConformite>();
//		try {
//			masterEntity = taArticleService.findById(masterController.getSelectedTaArticleDTO().getId());
		if(masterEntity!=null) {
//			values.addAll(masterEntity.getTaConformites());
			values.addAll(taConformiteService.controleArticleDerniereVersion(masterEntity.getIdArticle()));
		}
//		} catch (FinderException e) {
//			e.printStackTrace();
//		}
		
		//values = taConformiteService.selectAll();
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		
		String type = params.get("type");
		
		if(params.get("idEntityArticle")!=null) {
			try {
				masterEntity = taArticleService.findByIdAvecConformite(LibConversion.stringToInteger(params.get("idEntityArticle")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(params.get("idEntityConformite")!=null) {
			try {
				nouveau = taConformiteService.findById(LibConversion.stringToInteger(params.get("idEntityConformite")));
				selection = nouveau;
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(params.get("idEntityBareme")!=null) {
			try {
				nouveauBareme = taBaremeService.findById(LibConversion.stringToInteger(params.get("idEntityBareme")));
				selectionBareme = nouveauBareme;
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
			if(type.equals("type_controle")) {
				actInserer(null);
			} else {
				actInsererBareme(null);
			}
		} else if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_EDITION))) {
			modeEcranDefaut = C_DIALOG;
			if(type.equals("type_controle")) {
				actModifier(null);
			} else {
				actModifierBareme(null);
			}
		} else {
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		}

	}

	public ListeConformiteController() {  
	}  

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaConformite();
		nouveau.setTaTypeConformite(new TaTypeConformite());
		if(masterEntity.getTaConformites()!=null) {
			nouveau.setPosition(masterEntity.getTaConformites().size());
		} else {
			nouveau.setPosition(1);
		}
		
		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
		if(nouveau.getCode()!=null) {
			taConformiteService.annuleCode(nouveau.getCode());
		}
		nouveau.setCode(taConformiteService.genereCode(null));
		
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}
	
	public void actInsererBareme(ActionEvent actionEvent){
		nouveauBareme = new TaBareme();
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		modeEcranBareme.setMode(EnumModeObjet.C_MO_INSERTION);
		
		taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));
		if(nouveauBareme.getCode()!=null) {
			taBaremeService.annuleCode(nouveau.getCode());
		}
		nouveauBareme.setCode(taBaremeService.genereCode(null));
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		
		values.addAll(taConformiteService.controleArticleDerniereVersion(nouveau.getTaArticle().getIdArticle()));
//		for (TaConformite c : values) {
//			if(c.getTaTypeConformite()!=null && c.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CALCULE)) {
//				List<Integer> idCtrlDependant =  c.findListeIdControleDependant();
//				
//				if(idCtrlDependant!=null && !idCtrlDependant.isEmpty() && idCtrlDependant.contains(nouveau.getIdConformite())) {
//					FacesContext context = FacesContext.getCurrentInstance();  
//					context.addMessage(null, new FacesMessage(
//							FacesMessage.SEVERITY_WARN, 
//							"Controles de conformité", 
//							"Attention il y a des références à ce controle dans un champ de type 'valeur calculée', pensez à mettre à jour son identifiant dans les formules.")); 
//				}
//			}
//		}
		
		//vérifier si le controle ou le modele a deja été utilisé pour controler un lot,
		//s'il est déjà utiliser, le dupliquer (avec ses barèmes) et modifier la copie ("cacher" l'ancien et mettre à jour champ "versionCtrl")
		if(nouveau!=null && taConformiteService.controleUtiliseDansUnLot(nouveau.getIdConformite())) {
			
//			int ancienId = nouveau.getIdConformite();
//			List<Integer> idCtrlDependant =  nouveau.findListeIdControleDependant();
			
			TaConformite conf = dupliquerConformite(nouveau);	
			values.remove(nouveau);
			values.add(conf);
			
			nouveau = conf;
			

			/*
			 * copier les controles liés pour les formules.
			 */
			//List<Integer> idCtrlDependant =  conf.findListeIdControleDependant();
		}
		
		
	}
	
	public TaConformite dupliquerConformite(TaConformite c) {
		try {
			int idControleOrigine = c.getIdConformite();
			TaConformite conf = new TaConformite();
			conf.setTaArticle(c.getTaArticle());
			conf.setTaGroupe(c.getTaGroupe());
			conf.setTaModeleConformiteOrigine(c.getTaModeleConformiteOrigine());
			conf.setTaModeleGroupeOrigine(c.getTaModeleGroupeOrigine());
			conf.setTaTypeConformite(c.getTaTypeConformite());
			conf.setCtrlBloquant(c.getCtrlBloquant());
			conf.setCtrlFacultatif(c.getCtrlFacultatif());
			conf.setDeuxiemeValeur(c.getDeuxiemeValeur());
			conf.setLibelleConformite(c.getLibelleConformite());
			conf.setPosition(c.getPosition());
			conf.setValeurCalculee(c.getValeurCalculee());
			conf.setValeurDefaut(c.getValeurDefaut());
			
			conf.setTaBaremes(new HashSet<>());
			if(c.getTaBaremes()!=null && !c.getTaBaremes().isEmpty()) {
				
				TaBareme ba = null;
				for (TaBareme b : c.getTaBaremes()) {
					ba =  new TaBareme();
					ba.setActioncorrective(b.getActioncorrective());
					ba.setBlobFichier(b.getBlobFichier());
					ba.setCheminDoc(b.getCheminDoc());
					ba.setExpressionVerifiee(b.getExpressionVerifiee());
					ba.setForcage(b.getForcage());
					ba.setTaConformite(conf);
					conf.getTaBaremes().add(ba);
//					if(selectionBareme!=null && b.getIdBareme()==selectionBareme.getIdBareme()) {
//						selectionBareme = ba;
//					}
				}
			}
			conf.setVersionCtrl(c.getVersionCtrl()!=null?(c.getVersionCtrl().intValue()+1):1);
			
			//* Avant ==>
			conf.setTaConformiteParentAvantModif(c.getTaConformiteParentAvantModif()!=null?c.getTaConformiteParentAvantModif():c);
			//* Essayer ==>
			//conf.setTaConformiteParentAvantModif(c);
			
			conf = taConformiteService.merge(conf);
			
			/*
			 * copier les controles liés pour les formules.
			 */	
//			int ancienId = conf.getIdConformite();
//			Map<Integer,Integer> mapAncienIds = new HashMap<>();
//			mapAncienIds.put(ancienId, conf.getIdConformite());
//			List<Integer> idCtrlDependant =  conf.findListeIdControleDependant();
//			TaConformite newCtrlDep = null;
//			for (Integer id : idCtrlDependant) {
//				newCtrlDep = dupliquerConformite(taConformiteService.findById(id));
//				mapAncienIds.put(id, newCtrlDep.getIdConformite());
//			}
//			majFormuleChampCalcule(masterEntity, mapAncienIds);

			//*******************
			List<TaConformite> listeDernierControleArticle = taConformiteService.controleArticleDerniereVersion(masterEntity.getIdArticle());
			List<String> listeIdControleDisponiblePourFormule = new ArrayList<String>();
			Map<String,String> mapIdModeleVersIdReel = new ConcurrentHashMap<String,String>();
			for(TaConformite controle : listeDernierControleArticle) {
				listeIdControleDisponiblePourFormule.add(LibConversion.integerToString(controle.getIdConformite()));
				mapIdModeleVersIdReel.put(
						LibConversion.integerToString(controle.getIdConformite()), 
						LibConversion.integerToString(controle.getIdConformite())
						);
			}
			mapIdModeleVersIdReel.put(
					LibConversion.integerToString(idControleOrigine), 
					LibConversion.integerToString(conf.getIdConformite())
					);
			majFormuleChampCalcule(masterEntity, mapIdModeleVersIdReel);
			
			if(conf.getTaBaremes()!=null && !conf.getTaBaremes().isEmpty()) {
				//on ne gere qu'un seul bareme
				TaBareme ee = conf.getTaBaremes().iterator().next();
				selectionBareme = ee;
			}
			return conf;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void actModifierBareme(ActionEvent actionEvent){
		if(masterEntity==null) {
			masterEntity = selectionBareme.getTaConformite().getTaArticle();
		}
		actModifier(actionEvent);
		nouveauBareme = selectionBareme;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		modeEcranBareme.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void actCtrlBloquantChangeEvent(AjaxBehaviorEvent event){
		if(nouveau.getCtrlBloquant()) {
			nouveau.setCtrlFacultatif(false);
		}
	}
	
	public void actDialogAjouterControleArticle(ActionEvent actionEvent) {
		
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("height", 650);
        options.put("modal", true);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("/solstyce/dialog_inserer_un_modele_ctrl", options, params);
	    
	}
	
	public void handleReturnDialogAjouterControleArticle(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			List<TaModeleConformite> l = (List<TaModeleConformite>) event.getObject();
			System.out.println("Ajout d'une liste de modèle de controle : ");
			ajouteListeCtrl(l);
		}
	}
	
	public void actDialogInsererModele(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("height", 650);
        options.put("modal", true);
        
//        Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
//        
//        FacesContext context = FacesContext.getCurrentInstance();
//		ControleConformiteJSF c = context.getApplication().evaluateExpressionGet(context, "#{c}", ControleConformiteJSF.class);
//		
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.remove("controle");
//		sessionMap.put("controle", c);
        
//        List<String> listCtrl = new ArrayList<String>();
//        listCtrl.add(c);
//        params.put("controle", listCtrl);
        
//        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//		modeEcranDefaut = params.get("modeEcranDefaut");
        
        PrimeFaces.current().dialog().openDynamic("/solstyce/dialog_inserer_modele_ctrl", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogInsererModele(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaModeleGroupe taModeleGroupe = (TaModeleGroupe) event.getObject();
			System.out.println("Ajout du groupe de modèle de controle : "+taModeleGroupe.getCodeGroupe());
			ajouteListeCtrl(taModeleGroupe);
		}
	}
	
	public void ajouteListeCtrl(TaModeleGroupe taModeleGroupe) {
		List<TaModeleConformite> l = taModeleConformiteService.findByModeleGroupe(taModeleGroupe);
		ajouteListeCtrl(taModeleGroupe,l);
	}
	
	public void ajouteListeCtrl(List<TaModeleConformite> l) {
		ajouteListeCtrl(null,l);
	}
	
	public void ajouteListeCtrl(TaModeleGroupe taModeleGroupe,List<TaModeleConformite> l) {
		if(l!=null) {
			TaConformite ctrl = null;
			TaBareme bareme = null;
			int pos = masterEntity.getTaConformites()!=null?masterEntity.getTaConformites().size():0;
			for(TaModeleConformite mc : l) {
				System.out.println("Ajout du modèle de controle : "+mc.getLibelleConformite());
				ctrl = new TaConformite();
				ctrl.setTaTypeConformite(mc.getTaTypeConformite());
				ctrl.setTaGroupe(mc.getTaGroupe());
				ctrl.setLibelleConformite(mc.getLibelleConformite());
				ctrl.setValeurDefaut(mc.getValeurDefaut());
				ctrl.setValeurCalculee(mc.getValeurCalculee());
				ctrl.setDeuxiemeValeur(mc.getDeuxiemeValeur());
				ctrl.setCtrlBloquant(mc.getCtrlBloquant());
				ctrl.setCtrlFacultatif(mc.getCtrlFacultatif());
				//conserver un liens vers les infos d'origine
				ctrl.setTaModeleConformiteOrigine(mc);
				ctrl.setTaModeleGroupeOrigine(taModeleGroupe);
				ctrl.setPosition(++pos);
				for(TaModeleBareme mb : mc.getTaModeleBaremes()) {
					bareme = new TaBareme();
					if(ctrl.getTaBaremes()==null) {
						ctrl.setTaBaremes(new HashSet<TaBareme>());
					}
					bareme.setActioncorrective(mb.getActioncorrective());
					bareme.setCheminDoc(mb.getCheminDoc());
					bareme.setExpressionVerifiee(mb.getExpressionVerifiee());
					bareme.setForcage(mb.getForcage());
					
					bareme.setTaConformite(ctrl);
					ctrl.getTaBaremes().add(bareme);
				}
				
				ctrl.setTaArticle(masterEntity);
				masterEntity.getTaConformites().add(ctrl);

				nouveau = ctrl;
				selection=ctrl;
				actEnregistrer(null);					
			}

			majFormuleChampCalculeModele();
			

			values = new LinkedList<TaConformite>();
//			values.addAll(masterEntity.getTaConformites());
			values.addAll(taConformiteService.controleArticleDerniereVersion(masterEntity.getIdArticle()));
			selection = null;
			if(values.size()>0)selection = values.get(0);				
		}
	}
	
	/**
	 * Mise à jour des formules de calcul des valeur calculés pour les champs ajouté depuis un modèle
	 */
	public void majFormuleChampCalculeModele() {
		try {
			masterEntity = taArticleService.findById(masterEntity.getIdArticle()); //pour être sur d'avoir les id des derniers controle enregistrés
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<TaConformite> listeDernierControleArticle = taConformiteService.controleArticleDerniereVersion(masterEntity.getIdArticle());
		List<String> listeIdControleDisponiblePourFormule = new ArrayList<String>();
		Map<String,String> mapIdModeleVersIdReel = new ConcurrentHashMap<String,String>();
		for(TaConformite controle : listeDernierControleArticle) {
			listeIdControleDisponiblePourFormule.add(LibConversion.integerToString(controle.getIdConformite()));
			mapIdModeleVersIdReel.put(
					LibConversion.integerToString(controle.getTaModeleConformiteOrigine().getIdModeleConformite()), 
					LibConversion.integerToString(controle.getIdConformite())
					);
		}
		majFormuleChampCalcule(masterEntity, mapIdModeleVersIdReel);
//		for(TaConformite controle : masterEntity.getTaConformites()) {
//			if(controle.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CALCULE)) {
//				//faire un mapping sur les formules
//				Pattern pattern = Pattern.compile(TaBareme.REGEX_FORMULE_VALEUR_CALC); 
//				// cherche les occurrences c1,c535,c22 en début de formule ou dans la formule si elles sont précéder ou suivies de +-/*()=
//				String expr = controle.getDeuxiemeValeur();
//				if(expr != null && !expr.equals("")) {
//					System.out.println("**** Expression du modèle (après ajout) : "+expr);
//					Matcher matcher = pattern.matcher(expr);
//					// Check all occurrences
//					while (matcher.find()) {
//					
//						String match = matcher.group().replaceAll(TaBareme.REGEX_FORMULE_CARACTERE_PONCTUATION,"");
////						String match = matcher.group().substring(1);
////						match = match.substring(0,match.length()-1);
//						String idControleModele = match.substring(1);
//						System.out.println("**** id à rechercher : "+idControleModele+" à remplacer par "+mapIdModeleVersIdReel.get(idControleModele));
//						if(mapIdModeleVersIdReel.get(idControleModele)!=null) {
//							expr = expr.replace(idControleModele, mapIdModeleVersIdReel.get(idControleModele));
//						} else {
//							expr = expr.replace(idControleModele, "##ERREUR##");
//							System.out.println("L'expression calculé du modèle est incohérente, ou un modèle utilisé dans l'expression n'est pas présent dans le groupe");
//							//exemple : si formule c15+c18 et c18 n'est pas dans le groupe, donc pas affecté à l'article => impossible de créer une correspondance avec c18
//						}
//					}
//					System.out.println("**** Expression modifiée : "+expr);
//					controle.setDeuxiemeValeur(expr);
//				}
//			}
//			taConformiteService.merge(controle, ITaConformiteServiceRemote.validationContext);
//		}
//		//actEnregistrer(null);
	}
	
	public void majFormuleChampCalcule(TaArticle article, Map<String,String> mapIdOrigineVersIdReel) {
		List<TaConformite> listeDernierControleArticle = taConformiteService.controleArticleDerniereVersion(article.getIdArticle());
		for(TaConformite controle : listeDernierControleArticle) {
			if(controle.getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CALCULE)) {
				//faire un mapping sur les formules
				Pattern pattern = Pattern.compile(TaBareme.REGEX_FORMULE_VALEUR_CALC); 
				// cherche les occurrences c1,c535,c22 en début de formule ou dans la formule si elles sont précéder ou suivies de +-/*()=
				String expr = controle.getDeuxiemeValeur();
				if(expr != null && !expr.equals("")) {
					System.out.println("**** Expression du modèle (après ajout) : "+expr);
					Matcher matcher = pattern.matcher(expr);
					// Check all occurrences
					while (matcher.find()) {
					
						String match = matcher.group().replaceAll(TaBareme.REGEX_FORMULE_CARACTERE_PONCTUATION,"");
//						String match = matcher.group().substring(1);
//						match = match.substring(0,match.length()-1);
						String idControleModele = match.substring(1);
						System.out.println("**** id à rechercher : "+idControleModele+" à remplacer par "+mapIdOrigineVersIdReel.get(idControleModele));
						if(mapIdOrigineVersIdReel.get(idControleModele)!=null) {
							//expr = expr.replace(idControleModele, mapIdOrigineVersIdReel.get(idControleModele));
							expr = expr.replace(match, "c"+mapIdOrigineVersIdReel.get(idControleModele));
						} else {
							expr = expr.replace(idControleModele, "##ERREUR##");
							System.out.println("L'expression calculé du modèle est incohérente, ou un modèle utilisé dans l'expression n'est pas présent dans le groupe");
							//exemple : si formule c15+c18 et c18 n'est pas dans le groupe, donc pas affecté à l'article => impossible de créer une correspondance avec c18
						}
					}
					System.out.println("**** Expression modifiée : "+expr);
					controle.setDeuxiemeValeur(expr);
				}
			}
			taConformiteService.merge(controle, ITaConformiteServiceRemote.validationContext);
		}
		//actEnregistrer(null);
	}

//	public void actSupprimer(){
	public void actSupprimer(TaConformite c){
		try {
			selection = c;
			selection.setSupprime(true);
			taConformiteService.merge(selection);
			
			masterEntity=taArticleService.findById(masterEntity.getIdArticle());

		} catch (/*Remove*/Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values.clear();
		values.addAll(taConformiteService.controleArticleDerniereVersion(masterEntity.getIdArticle()));
		if(values.size()>0) {
			selection = values.get(0);
		}

	}
	
	public void actSupprimerBareme(){
		try {
			selection.getTaBaremes().remove(selectionBareme);
			selectionBareme.setTaConformite(null);
			
			taConformiteService.merge(selection);
			//selectionBareme = values.get(0);	
		} catch (/*Remove*/Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
		values.clear();
		values.addAll(taConformiteService.controleArticleDerniereVersion(masterEntity.getIdArticle()));
		if(values.size()>0) {
			selection = values.get(0);
		}
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {

			TaConformite retour = null;
			if(nouveau.getCode()!=null && nouveau.getCode().equals("")) {
				nouveau.setCode(null);
			}
			if(taConformiteService.findById(nouveau.getIdConformite()) == null){
				nouveau.setTaArticle(masterEntity);
				//masterEntity.getTaConformites().add(nouveau);
				retour = taConformiteService.merge(nouveau, ITaConformiteServiceRemote.validationContext);
				//values= taConformiteService.selectAll();
				if(masterEntity!=null) {
					//values = new ArrayList<TaConformite>();
					//values.addAll(masterEntity.getTaConformites());
					values.add(retour);
					
				}
				if(!values.isEmpty()) {
					selection = values.get(0);
				}

				nouveau = new TaConformite();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

//				if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
//					PrimeFaces.current().dialog().closeDynamic(retour);
//				}
			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					nouveau.setTaArticle(masterEntity);
					retour = taConformiteService.merge(nouveau, ITaConformiteServiceRemote.validationContext);
//					values= taConformiteService.selectAll();
					if(masterEntity!=null) {
						values = new LinkedList<TaConformite>();
						values.addAll(masterEntity.getTaConformites());
					}
					selection = values.get(0);

					nouveau = new TaConformite();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}

			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(retour);
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void actEnregistrerBareme(ActionEvent actionEvent){
		try {

			TaBareme retour = null;
			if(nouveauBareme.getCode()!=null && nouveauBareme.getCode().equals("")) {
				nouveauBareme.setCode(null);
			}
//			if(taBaremeService.findById(nouveauBareme.getIdBareme()) == null){
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)!=0){
				nouveauBareme.setTaConformite(nouveau);
				nouveau.setTaArticle(masterEntity);
				//masterEntity.getTaConformites().add(nouveau);
				retour = taBaremeService.merge(nouveauBareme, ITaBaremeServiceRemote.validationContext);
				//values= taConformiteService.selectAll();
//				if(masterEntity!=null) {
//
//					values.add(retour);
//					
//				}
				if(!values.isEmpty()) {
					selection = values.get(0);
				}

				nouveauBareme = new TaBareme();

				modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

//				if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
//					PrimeFaces.current().dialog().closeDynamic(retour);
//				}
			}else{
				if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_EDITION)==0){
					nouveauBareme.setTaConformite(nouveau);
					nouveau.setTaArticle(masterEntity);
					retour = taBaremeService.merge(nouveauBareme, ITaBaremeServiceRemote.validationContext);
//					values= taConformiteService.selectAll();
//					if(masterEntity!=null) {
//						values = new ArrayList<TaConformite>();
//						values.addAll(masterEntity.getTaConformites());
//					}
					//selection = values.get(0);

					nouveauBareme = new TaBareme();

					modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Code déja utilisé"));
				}
			}
			if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
				PrimeFaces.current().dialog().closeDynamic(retour);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actAnnuler(ActionEvent actionEvent){
//		values= taTFamilleArticlesService.selectAll();
		if(values.size()>= 1){
			selection = values.get(0);
		}else selection=new TaConformite();		
		nouveau = new TaConformite();
		//		creation = false;
		//		modification = false;
		
		if(nouveau.getCode()!=null) {
			taConformiteService.annuleCode(nouveau.getCode());
		}	

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
			PrimeFaces.current().dialog().closeDynamic(null);
		}
	}
	
	public void actAnnulerBareme(ActionEvent actionEvent){
		if(values.size()>= 1){
			selection = values.get(0);
		}		
		//nouveau = new TaConformite();
		
		if(nouveauBareme.getCode()!=null) {
			taBaremeService.annuleCode(nouveauBareme.getCode());
		}	

		modeEcranBareme.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
			PrimeFaces.current().dialog().closeDynamic(null);
		}
	}

	public List<TaConformite> getValues(){  
		//List<Value> l = new LinkedList<Value>();
		return values;
	}

	//	public Boolean getModification() {
	//		return modification;
	//	}
	//	public void setModification(Boolean modification) {
	//		this.modification = modification;
	//	}

	public TaConformite getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaConformite newTaConformite) {
		this.nouveau = newTaConformite;
	}

	public TaConformite getSelection() {
		return selection;
	}

	public void setSelection(TaConformite selectedTaConformite) {
		this.selection = selectedTaConformite;
	}

	public void setValues(LinkedList<TaConformite> values) {
		this.values = values;
	}


	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}
	
	public void onRowToggle(ToggleEvent event) {
//	    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
//	                                        "Row State " + event.getVisibility(),
//	                                        "Model:" + ((Car) event.getData()).getModel());
		selection = ((TaConformite) event.getData());

//	    FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void onRowSelectBareme(SelectEvent event) {  
		if(modeEcranBareme.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveauBareme = selectionBareme;
		}
	}
	
	public void onRowReorder(ReorderEvent event) {
//        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Row Moved", "From: " + event.getFromIndex() + ", To:" + event.getToIndex());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
		DataTable objDataTable = (DataTable) event.getSource();
		
//		TaConformite c = values.get(event.getToIndex());
//		values.set(event.getToIndex(),values.get(event.getFromIndex()));
//		values.set(event.getFromIndex(),c);
		
		int i = 0;
		for (TaConformite ctrl : values) {
			ctrl.setPosition(i);
//			taConformiteService.merge(ctrl, ITaConformiteServiceRemote.validationContext);
			values.set(ctrl.getPosition(),taConformiteService.merge(ctrl, ITaConformiteServiceRemote.validationContext));
			i++;
		}

		
//		objDataTable.getd
//		
//		if(org.hibernate.collection.internal.PersistentSet)
//		((LinkedHashSet<TaConformite>)masterEntity.getTaConformites()).size();
		System.out.println("ListeConformiteController.onRowReorder()");
    }

	public List<TaConformite> getFilteredValues() {
		return filteredValues;
	}

	public void setFilteredValues(List<TaConformite> filteredValues) {
		this.filteredValues = filteredValues;
	}

	public Boolean getTableVide() {
		return tableVide;
	}

	public void setTableVide(Boolean tableVide) {
		this.tableVide = tableVide;
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
			case "inserer":
			case "fermer":
				retour = false;
				break;
			case "supprimerListe":retour = false;break;	
			case "supprimer":retour = false;break;	
//			case "modifier":
			case "imprimer":
				if(nouveau!=null &&  nouveau.getIdConformite()!=0 ) retour = false;
				break;	
			case "modifier": retour = false;
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
	
	public boolean etatBoutonBareme(String bouton) {
		boolean retour = true;
		switch (modeEcranBareme.getMode()) {
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
	
	public List<TaGroupe> groupeAutoComplete(String query) {
        List<TaGroupe> allValues = taGroupeService.selectAll();
        List<TaGroupe> filteredValues = new ArrayList<TaGroupe>();
         
        for (int i = 0; i < allValues.size(); i++) {
        	TaGroupe civ = allValues.get(i);
            if(query.equals("*") || civ.getCodeGroupe().toLowerCase().contains(query.toLowerCase())) {
                filteredValues.add(civ);
            }
        }
         
        return filteredValues;
    }
	
	public void actDialogGroupeControle(ActionEvent actionEvent) {
		
//		PrimeFaces.current().dialog().openDynamic("aide");
    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("resizable", false);
        options.put("contentHeight", 640);
        options.put("height", 650);
        
        //Map<String,List<String>> params = null;
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        PrimeFaces.current().dialog().openDynamic("solstyce/dialog_groupe_controle", options, params);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void handleReturnDialogGroupeControle(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//taGroupe = (TaGroupe) event.getObject();
		}
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

	public ModeObjetEcranServeur getModeEcranBareme() {
		return modeEcranBareme;
	}

	public List<TaTypeConformite> getListeTypeControle() {
		return listeTypeControle;
	}

	public TaBareme getNouveauBareme() {
		return nouveauBareme;
	}

	public void setNouveauBareme(TaBareme nouveauBareme) {
		this.nouveauBareme = nouveauBareme;
	}

	public TaBareme getSelectionBareme() {
		return selectionBareme;
	}

	public void setSelectionBareme(TaBareme selectionBareme) {
		this.selectionBareme = selectionBareme;
	}

	public TaArticle getMasterEntity() {
		return masterEntity;
	}

	public void setMasterEntity(TaArticle masterEntity) {
		this.masterEntity = masterEntity;
	}
}  

package fr.legrain.bdg.webapp.app;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.bdg.dossier.service.remote.ITaCategoriePreferenceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaGroupePreferenceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.dossier.dto.TaPreferencesDTO;
import fr.legrain.dossier.model.TaCategoriePreference;
import fr.legrain.dossier.model.TaGroupePreference;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibCrypto;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.servicewebexterne.model.UtilServiceWebExterne;

@Named
@ViewScoped 
public class PreferencesBean implements Serializable {

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	private List<TaPreferences> values; 
	private List<TaPreferences> valuesPourGroupe; 
	
	private TaPreferencesDTO[] selectedTaArticleDTOs; 
    private TaPreferencesDTO newTaArticleDTO = new TaPreferencesDTO();
    private TaPreferences taArticle = new TaPreferences();
    private TaArticleDTO selectedTaArticleDTO = new TaArticleDTO();
    
    private List<TaCategoriePreference> listeCategories;
	
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private String objectCourant;
	
	@EJB private ITaPreferencesServiceRemote taPreferencesService;
	@EJB private ITaCategoriePreferenceServiceRemote taCategoriePreferenceService;
	@EJB private ITaGroupePreferenceServiceRemote taGroupePreferenceService;
	
	private TreeNode root;
	private TreeNode selectedNode;
	private Map<Integer,TreeNode> mapIdCategorieNoeud = new HashMap<Integer,TreeNode>();
	private Map<String,TaCategoriePreference> mapRowKeyCategorie = new HashMap<String,TaCategoriePreference>();
	private Map<TaGroupePreference,LinkedList<TaPreferences>> mapPreferenceDansGroupe = new HashMap<>();
	private int idCategorieCourante=-1;
	
	@PostConstruct
	public void init() {
		refresh();
	}
	
	class NoeudPreference {
		public TaCategoriePreference n;
		public List<TaCategoriePreference> enfants;
	}

	public void refresh(){
		//values=taPreferencesService.selectAll();
		listeCategories = taCategoriePreferenceService.selectAll();
		
		root = new DefaultTreeNode("Root", null);

		for (TaCategoriePreference taCategoriePreference : listeCategories) {
			chercheParent(taCategoriePreference,null);
		}
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	
	public TreeNode chercheParent(TaCategoriePreference taCategoriePreference, LinkedList<TaCategoriePreference> l) {
		if(l==null) {
			l = new LinkedList<>();
		}
		l.addFirst(taCategoriePreference);
		while(taCategoriePreference.getCategorieMerePreference()!=null) {
			taCategoriePreference = taCategoriePreference.getCategorieMerePreference();
			l.addFirst(taCategoriePreference);
		}
		
		TreeNode parent = root;
		for (TaCategoriePreference taCategoriePreference2 : l) {
			TreeNode node = null;
			if(mapIdCategorieNoeud.get(taCategoriePreference2.getIdCategoriePreference())!=null) { //on a deja ce noeud
				parent = mapIdCategorieNoeud.get(taCategoriePreference2.getIdCategoriePreference());
			} else {
				node = new DefaultTreeNode(taCategoriePreference2.getLibelleCategoriePreference(),parent);
				node.setRowKey(LibConversion.integerToString(taCategoriePreference2.getIdCategoriePreference()));
				mapRowKeyCategorie.put(taCategoriePreference2.getLibelleCategoriePreference(), taCategoriePreference2);
				mapIdCategorieNoeud.put(taCategoriePreference2.getIdCategoriePreference(),node);
//				if(node.getData().equals("Général")) {
//					selectedNode = node;
//				}
			}
			
		}
		return parent;
	}
	
	public void onNodeSelect(NodeSelectEvent event) {
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
//        FacesContext.getCurrentInstance().addMessage(null, message);
		
		enregistrer();
		idCategorieCourante=mapRowKeyCategorie.get(event.getTreeNode().getData()).getIdCategoriePreference();
		recupPrefNode(idCategorieCourante);
		
//        values = taPreferencesService.findByCategorie(mapRowKeyCategorie.get(event.getTreeNode().getData()).getIdCategoriePreference());
//        
////        values.sort(new Comparator<TaPreferences>() {
////    		@Override
////    		public int compare(TaPreferences o1, TaPreferences o2) {
////    			int position1 = o1.getPosition()!=null ? o1.getPosition():0;
////    			position1 = o1.getTaGroupePreference()!=null ? (o1.getPositionDansGroupe()!=null?o1.getPositionDansGroupe():0):position1;
////    			int position2 = o2.getPosition()!=null ? o2.getPosition():0;
////    			position2 = o2.getTaGroupePreference()!=null ? (o2.getPositionDansGroupe()!=null?o2.getPositionDansGroupe():0):position2;
////    			return position2 - position1;
////    		}
////    	});	
//        
//        valuesPourGroupe = new LinkedList<>();
//        mapPreferenceDansGroupe = new HashMap<>();
//        List<TaGroupePreference> gp = new LinkedList<>();
//        for (TaPreferences p : values) {
//        	if(p.getTypeDonnees().equals("password") && p.getValeur()!=null && !p.getValeur().equals("")) {
//				p.setValeur(LibCrypto.decrypt(p.getValeur()));
//			}
//			if(p.getTaGroupePreference()!=null) {
//				
//				if(!gp.contains(p.getTaGroupePreference())) {
//					gp.add(p.getTaGroupePreference());
//					valuesPourGroupe.add(p); //premiere preference pour ce groupe (pour marquer sa sa position)
//					mapPreferenceDansGroupe.put(p.getTaGroupePreference(), new LinkedList<>());
//				}
//				mapPreferenceDansGroupe.get(p.getTaGroupePreference()).add(p);
//			} else {
//				valuesPourGroupe.add(p);
//			}
//		}
//        
////        values.clear();
//        for (TaGroupePreference gpp : mapPreferenceDansGroupe.keySet()) {
//        	mapPreferenceDansGroupe.get(gpp).sort(new Comparator<TaPreferences>() {
//	      		@Override
//	      		public int compare(TaPreferences o1, TaPreferences o2) {
//	      			int position1 = o1.getPositionDansGroupe()!=null ? o1.getPositionDansGroupe():0;
//	      			int position2 = o2.getPositionDansGroupe()!=null ? o2.getPositionDansGroupe():0;
//	      			return position2 - position1;
//	      		}
//        	});	
//        }
//        
////        TaGroupePreference gpCourant = null;
////        TaGroupePreference gpPrececent = null;
////
////        for (TaPreferences pp : valuesPourGroupe) {
////        	if(pp.getTaGroupePreference()!=null) {
////        		values.addAll(valuesPourGroupe.indexOf(pp), mapPreferenceDansGroupe.get(pp.getTaGroupePreference()));
////        	} else {
////        		values.add(pp);
////        	}
////		}
        
        
    }
	
	
	public void recupPrefNode(int idCategorieCourante) {
		//      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
		//      FacesContext.getCurrentInstance().addMessage(null, message);

		if(idCategorieCourante!=-1) {
			values = taPreferencesService.findByCategorie(idCategorieCourante);

			valuesPourGroupe = new LinkedList<>();
			mapPreferenceDansGroupe = new HashMap<>();
			List<TaGroupePreference> gp = new LinkedList<>();
			for (TaPreferences p : values) {
				if(p.getTypeDonnees().equals("password") && p.getValeur()!=null && !p.getValeur().equals("")) {
					p.setValeur(LibCrypto.decrypt(p.getValeur()));
				}
				if(p.getTaGroupePreference()!=null) {

					if(!gp.contains(p.getTaGroupePreference())) {
						gp.add(p.getTaGroupePreference());
						valuesPourGroupe.add(p); //premiere preference pour ce groupe (pour marquer sa sa position)
						mapPreferenceDansGroupe.put(p.getTaGroupePreference(), new LinkedList<>());
					}
					mapPreferenceDansGroupe.get(p.getTaGroupePreference()).add(p);
				} else {
					valuesPourGroupe.add(p);
				}
			}

			//      values.clear();
			for (TaGroupePreference gpp : mapPreferenceDansGroupe.keySet()) {
				mapPreferenceDansGroupe.get(gpp).sort(new Comparator<TaPreferences>() {
					@Override
					public int compare(TaPreferences o1, TaPreferences o2) {
						int position1 = o1.getPositionDansGroupe()!=null ? o1.getPositionDansGroupe():0;
						int position2 = o2.getPositionDansGroupe()!=null ? o2.getPositionDansGroupe():0;
						return position2 - position1;
					}
				});	
			}
		}
	}
	
	
	public List<TaPreferences> getValues(){  
		return values;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}
	
	public void actAnnuler(ActionEvent actionEvent) {
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("Préférences", "actAnnuler")); 
	}
	
	public void actFermer() {

	}
	private void enregistrer() {
		try {	
			UtilServiceWebExterne utilServiceWebExterne = new UtilServiceWebExterne();
//			for (TaPreferences pref : values) {
//				TaPreferences p = taPreferencesService.merge(pref,ITaPreferencesServiceRemote.validationContext);
//			}
			
			if(valuesPourGroupe!=null) {
				TaPreferences p = null;
				for (TaPreferences pref : valuesPourGroupe) {
					if(pref.getTaGroupePreference()==null) {
						if(pref.getTypeDonnees().equals("password") && pref.getValeur()!=null && !pref.getValeur().equals("")) {
							pref.setValeur(LibCrypto.encrypt(pref.getValeur()));
						}
						p = taPreferencesService.merge(pref,ITaPreferencesServiceRemote.validationContext);
						if(pref.getTypeDonnees().equals("password") && pref.getValeur()!=null && !pref.getValeur().equals("")) {
							pref.setValeur(LibCrypto.decrypt(pref.getValeur()));
						}
					} else {
						for (TaPreferences pref2 : mapPreferenceDansGroupe.get(pref.getTaGroupePreference())) {
							if(pref2.getTypeDonnees().equals("password") && pref2.getValeur()!=null && !pref2.getValeur().equals("")) {
								pref2.setValeur(LibCrypto.encrypt(pref2.getValeur()));
							}
							p = taPreferencesService.merge(pref2,ITaPreferencesServiceRemote.validationContext);
							if(pref2.getTypeDonnees().equals("password") && pref2.getValeur()!=null && !pref2.getValeur().equals("")) {
								pref2.setValeur(LibCrypto.decrypt(pref2.getValeur()));
							}
						}
					}
				}
			}

			//values=taPreferencesService.selectAll();
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		} catch(Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			//	   context.addMessage(null, new FacesMessage("Préférences", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Préférences", e.getMessage()));
		}

	}
	public void actEnregistrer(ActionEvent actionEvent) {
		enregistrer();
		recupPrefNode(idCategorieCourante);
	}
	
	public boolean changeObject(String nouvelObject){
		if(objectCourant==null && nouvelObject==null) {
			objectCourant=nouvelObject;
			return false;
		}
		if(objectCourant==null && nouvelObject!=null) {
			objectCourant=nouvelObject;
			return true;
		}
		if(objectCourant!=null && nouvelObject==null) {
			objectCourant=nouvelObject;
			return true;
		}
		if(!objectCourant.equals(nouvelObject)) {
			objectCourant=nouvelObject;
			return true;
		}
		else {
			objectCourant=nouvelObject;
			return false;
		}
		
	}

	public List<TaCategoriePreference> getListeCategories() {
		return listeCategories;
	}

	public void setListeCategories(List<TaCategoriePreference> listeCategories) {
		this.listeCategories = listeCategories;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}


	public TreeNode getSelectedNode() {
		return selectedNode;
	}


	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}


	public List<TaPreferences> getValuesPourGroupe() {
		return valuesPourGroupe;
	}


	public void setValuesPourGroupe(List<TaPreferences> valuesPourGroupe) {
		this.valuesPourGroupe = valuesPourGroupe;
	}


	public Map<TaGroupePreference, LinkedList<TaPreferences>> getMapPreferenceDansGroupe() {
		return mapPreferenceDansGroupe;
	}


	public void setMapPreferenceDansGroupe(Map<TaGroupePreference, LinkedList<TaPreferences>> mapPreferenceDansGroupe) {
		this.mapPreferenceDansGroupe = mapPreferenceDansGroupe;
	}
	
}

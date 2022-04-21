package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TracabiliteLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.document.dto.ITaLFabrication;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class DetailLotController implements Serializable {  
	
	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	private String parameter;

	private TaLot taLot;
	
	private TreeNode root;
    private TreeNode selectedNode;
    
    private Boolean origine = true;
    private Boolean utilisation = true;
    private String sens = "origine";
    
    private List<DetailLotOngletJSF> listeLot = new ArrayList<DetailLotOngletJSF>();
    private Map<String, DetailLotOngletJSF> mapLot = new HashMap<String, DetailLotOngletJSF>();

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaFabricationServiceRemote taFabricationService;
	private @EJB ITaBonReceptionServiceRemote taBonReceptionService;
	private @EJB ITaLotServiceRemote taLotService;

	@PostConstruct
	public void postConstruct(){
		try {
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public DetailLotController() {  
	}  
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List retrieveListAsSet(Set set) {
		return new ArrayList(set);
	}

	public void refresh(){
		if(taLot==null) {
			try {
				
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				Map<String, Object> sessionMap = externalContext.getSessionMap();
				
				parameter = (String) sessionMap.get("parameter");
				//parameter = "BR_00001_1";
				taLot = taLotService.findByCode(parameter);
				
				
//				taLot = (TaLot) sessionMap.get("lot");
				
				if(taLot==null) {
					if(sessionMap.get("numLot")!=null) {
						parameter = (String) sessionMap.get("numLot");
					}
					taLot = taLotService.findByCode(parameter);
				}
				
				if(taLot!=null && !mapLot.containsKey(taLot.getNumLot())) {
					TracabiliteLot t = taLotService.findTracaLot(taLot.getNumLot());
					root = createDocuments(t,null, false, sens);
					
					DetailLotOngletJSF onglet = new DetailLotOngletJSF(taLot, root, root);
					onglet.setSens(sens);
					listeLot.add(onglet);
					mapLot.put(taLot.getNumLot(), onglet);
					System.out.println(t);
				}
				
				System.out.println("ControleLotController.refresh()");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	//public void actChangeSens(AjaxBehaviorEvent event) {
	public void actChangeSens(String numLot) {
		System.out.println("DetailLotController.actChangeSens() "+numLot);
		System.out.println(mapLot.get(numLot).getSens());
		sens = mapLot.get(numLot).getSens();
		
		TracabiliteLot t = taLotService.findTracaLot(numLot);
		root = createDocuments(t,null,false,mapLot.get(numLot).getSens());
		mapLot.get(numLot).setRoot(root);
	}
	
	public void actDialogDetailLot(ActionEvent actionEvent) {
		    
        Map<String,Object> options = new HashMap<String, Object>();
//        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", false);
        options.put("contentHeight", 400);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<String>();
        list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
        params.put("modeEcranDefaut", list);
        
        FacesContext context = FacesContext.getCurrentInstance();
//		ControleConformiteJSF c = context.getApplication().evaluateExpressionGet(context, "#{c}", ControleConformiteJSF.class);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		String numLot =  (String) actionEvent.getComponent().getAttributes().get("lot");
		Boolean fermerDetail =  (Boolean) actionEvent.getComponent().getAttributes().get("fermerDetail");
		sessionMap.put("parameter", numLot);
		
//		if(fermerDetail!=null && fermerDetail) {
//			PrimeFaces.current().dialog().closeDynamic(null);
//		}

//		sessionMap.remove("lot");
//		sessionMap.put("lot", taLot);
		
//		controleConformiteJSF = c;
        
        PrimeFaces.current().dialog().openDynamic("/solstyce/dialog_detail_lot", options, params);
        
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Tiers", "actAbout")); 	    
	}
	
	public void onNodeExpand(NodeExpandEvent event) {  
        DefaultTreeNode parent = (DefaultTreeNode) event.getTreeNode();
        if (parent.getChildCount() == 1 && parent.getChildren().get(0).getData().toString().equals("DUMMY")) {
            parent.getChildren().remove(0);
            
            TracabiliteLot t = taLotService.findTracaLot(((DetailLotJSF)parent.getData()).getNom());
            createDocuments(t,parent,true,sens);
            
//            createNode("Node A", parent);
//            createNode("Node B", parent);
//            createNode("Node C", parent);
        }
   }

   private void createNode(String tag, TreeNode parent) {
       TreeNode node = new DefaultTreeNode(tag, parent); 
       // Create Dummy node, just to make the parent node expandable
       new DefaultTreeNode("DUMMY", node);
   }
 
    public void onNodeCollapse(NodeCollapseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Collapsed", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
 
    public void onNodeSelect(NodeSelectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
 
    public void onNodeUnselect(NodeUnselectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public void handleReturnDialogActionCorrective(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
		}
	}

//	public void actInserer(ActionEvent actionEvent){
//		nouveau = new TaResultatConformite();
//		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
//	}
//
//	public void actModifier(ActionEvent actionEvent){
//		nouveau = selection;
//		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
//	}

	public void actSupprimer(){
//		try {
//			taResultatConformiteService.remove(selection);
//			selection = values.get(0);	
//		} catch (RemoveException e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
//			e.printStackTrace();
//		}
//		values = taResultatConformiteService.selectAll();
//		selection = null;
	}
	
	private LinkedList<DetailLotJSF> listeImpression = new LinkedList<DetailLotJSF>();
	
	public void actImprimer(ActionEvent actionEvent){
		listeImpression.clear();
//		listeImpression.add((DetailLotJSF)root.getData());
		
		String numLot =  (String) actionEvent.getComponent().getAttributes().get("numlot");
		imprime(mapLot.get(numLot).getRoot(),0);
		
//		FacesContext context = FacesContext.getCurrentInstance();  
//		context.addMessage(null, new FacesMessage("Traçablite", "actImprimer")); 
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put("fabrication", fabricationService.findById(selectedTaFabricationDTO.getId()));
//		sessionMap.put("fabrication", fabricationService.findWithCodeArticleNoDate(codeArticle));
//		List<EditionEtatTracabilite> edition = editionsService.editionCADate(codeArticle, dateDebut, dateFin);
//		List<TaFabrication> lf = fabricationService.findWithCodeArticle(codeArticle, dateDebut, dateFin);
//		sessionMap.put("fabrications", lf);
//		sessionMap.put("codeArticle", codeArticle);
//		sessionMap.put("edition", edition);
//		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
//		sessionMap.put("dateFin", dateFin);
		sessionMap.put("listeImpression", listeImpression);
		
	}
	
	public void imprime(TreeNode n, int niveau) {
		DetailLotJSF noeud = null;
		for (TreeNode c : n.getChildren()) {
			noeud = (DetailLotJSF)c.getData();
			noeud.setNiveauArbre(niveau);
			listeImpression.add(noeud);
			if(c.isExpanded()) {
				imprime(c,niveau+1);
			}
		}
	}

	public void actEnregistrer(ActionEvent actionEvent){
		try {
			
//			for (ControleConformiteJSF c : listeControle) {
//				//TaResultatConformite res = new TaResultatConformite();
//				//res.setValeurConstatee(c.getValeurTexte());
//				//res.setTaLot(taLot);
//				//res.setTaConformite(c.getC());
//				//res.setTaBareme(); /// ????????????????
//				
//				if(c.getR()!=null) {
//					//une valeur constatée a été saisie et validé par rapport aux barèmes
//					@SuppressWarnings("unused")
//					TaResultatConformite p = taResultatConformiteService.merge(c.getR(),ITaResultatConformiteServiceRemote.validationContext);
//				}//else, aucune valeur saisie, donc on enregistre rien
//			}
//			//PrimeFaces.current().dialog().closeDynamic(controleConformiteJSF);
			PrimeFaces.current().dialog().closeDynamic(null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TreeNode createDocuments(TaBonReceptionDTO traca, TreeNode parent) throws FinderException {
		TreeNode doc = new DefaultTreeNode("BR",new DetailLotJSF(traca), parent);
		TracabiliteLot t = null;
		TaBonReception br = taBonReceptionService.findByCode(traca.getCodeDocument());
		DetailLotJSF detailLot = null;
		for (TaLBonReception l : br.getLignes()) {
			t = taLotService.findTracaLot(l.getTaLot().getNumLot());
			detailLot = new DetailLotJSF(t);
			detailLot.setBr(traca);
			detailLot.setQte(l.getQteLDocument().toString());
			detailLot.setUnite(l.getU1LDocument());
			TreeNode lot = new DefaultTreeNode("Lot",detailLot, doc);
			new DefaultTreeNode("Lot","DUMMY", lot);
		}
		return doc;
	}
	
	public TreeNode createDocuments(TaFabricationDTO traca, TreeNode parent) throws FinderException {
		TreeNode doc = new DefaultTreeNode("Fab",new DetailLotJSF(traca), parent);
		TracabiliteLot t = null;
		TaFabrication fab = taFabricationService.findByCode(traca.getCodeDocument());
		DetailLotJSF detailLot = null;
		fab.findProduit();
		for (ITaLFabrication lpf : fab.getLignesProduits()) {
			t = taLotService.findTracaLot(lpf.getTaLot().getNumLot());
			detailLot = new DetailLotJSF(t);
			if(lpf.getQteLDocument()!=null) {
				detailLot.setQte(lpf.getQteLDocument().toString());
			}
			detailLot.setFab(traca);
			detailLot.setUnite(lpf.getU1LDocument());
			detailLot.setMpf("PF");
			TreeNode lot = new DefaultTreeNode("Lot",detailLot, doc);
			new DefaultTreeNode("Lot","DUMMY", lot);
		}
		for (ITaLFabrication lmp : fab.getLignesMatierePremieres()) {
			t = taLotService.findTracaLot(lmp.getTaLot().getNumLot());
			detailLot = new DetailLotJSF(t);
			if(lmp.getQteLDocument()!=null) {
				detailLot.setQte(lmp.getQteLDocument().toString());
			}
			detailLot.setFab(traca);
			detailLot.setUnite(lmp.getU1LDocument());
			detailLot.setMpf("MP");
			TreeNode lot = new DefaultTreeNode("Lot",detailLot, doc);
			new DefaultTreeNode("Lot","DUMMY", lot);
		}
		return doc;
	}
	
	public TreeNode createDocuments(TracabiliteLot traca, TreeNode parent) {
		return createDocuments(traca,parent,false,"tout");
	}
	
	public TreeNode createDocuments(TracabiliteLot traca, TreeNode parent, boolean dynamic, String sens) {
		origine = false;
		utilisation = false;
		if(sens.equals("origine")) {
			origine = true;
		} else if(sens.equals("utilisation")) {
			utilisation = true;
		} else {
			origine = true;
			utilisation = true;
		}
		TreeNode root = null;
		TreeNode root2 = null;
		if(parent==null) {
			root = new DefaultTreeNode("Lot",new DetailLotJSF(traca), null);
			root2 = new DefaultTreeNode("Lot",new DetailLotJSF(traca), root);
		} else {
			if(!dynamic) {
				root2 = new DefaultTreeNode("Lot",new DetailLotJSF(traca), parent);
			}
		}
        
        
        TreeNode nodeOrigine = null; 
        TreeNode nodeUtilisation = null; 
        if(dynamic) {
        	if(origine) {
        		nodeOrigine = new DefaultTreeNode("Origine",new DetailLotJSF("Origine", "Origine"), parent);
        	}
        	if(utilisation) {
        		nodeUtilisation = new DefaultTreeNode("Utilisation",new DetailLotJSF("Utilisation", "Utilisation"), parent);
        	}
        } else {
        	if(origine) {
        		nodeOrigine =new DefaultTreeNode("Origine",new DetailLotJSF("Origine", "Origine"), root2);
        	}
        	if(utilisation) {
        		nodeUtilisation =new DefaultTreeNode("Utilisation",new DetailLotJSF("Utilisation", "Utilisation"), root2);
        	}
        }
        
        try {
        	if(origine) {
		        if(traca.origineBonReception!=null) {
		        	createDocuments(traca.origineBonReception, nodeOrigine);
		        }
		        
		        if(traca.origineFabrication!=null) {
		        	createDocuments(traca.origineFabrication, nodeOrigine);
		        }
        	}
	        
	        if(utilisation) {
		        if(traca.listeUtilFabrication!=null) {
		        	for (TaFabricationDTO f : traca.listeUtilFabrication) {
		        		createDocuments(f, nodeUtilisation);
					}
//		        	createDocuments(traca.listeUtilFabrication.get(0), nodeUtilisation);
		        }
	        }
        } catch(Exception e) {
        	e.printStackTrace();
        }
         
        return root;
    }

//	public void actAnnuler(ActionEvent actionEvent){
//		if(values.size()>= 1){
//			selection = values.get(0);
//		}		
//		nouveau = new TaResultatConformite();
//
//		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		PrimeFaces.current().dialog().closeDynamic(null);
//	}

//	public List<TaResultatConformite> getValues(){  
//		return values;
//	}
//
//	public TaResultatConformite getNouveau() {
//		return nouveau;
//	}
//
//	public void setNouveau(TaResultatConformite newTaResultatConformite) {
//		this.nouveau = newTaResultatConformite;
//	}
//
//	public TaResultatConformite getSelection() {
//		return selection;
//	}
//
//	public void setSelection(TaResultatConformite selectedTaResultatConformite) {
//		this.selection = selectedTaResultatConformite;
//	}
//
//	public void setValues(List<TaResultatConformite> values) {
//		this.values = values;
//	}
//
//	public void onRowSelect(SelectEvent event) {  
//		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
//			nouveau = selection;
//		}
//	}
//
//	public List<TaResultatConformite> getFilteredValues() {
//		return filteredValues;
//	}
//
//	public void setFilteredValues(List<TaResultatConformite> filteredValues) {
//		this.filteredValues = filteredValues;
//	}
	
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
	
	public void afficheTraca(ActionEvent actionEvent) {  
    	LgrTab b = new LgrTab();
    	b.setTypeOnglet(LgrTab.TYPE_TAB_TRACABILITE);
		b.setTitre("Traçabilite");
		b.setStyle(LgrTab.CSS_CLASS_TAB_TRACABILITE);
		b.setTemplate("solstyce/tracabilite.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TRACABILITE);
		b.setParamId("0");
		
//		<f:attribute name="numLot" value="delete" />
		String numLot = (String)actionEvent.getComponent().getAttributes().get("numLot");
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("parameter",numLot);
		taLot = null;
		refresh();
		
		tabsCenter.ajouterOnglet(b);
		tabViews.selectionneOngletCentre(b);
		//actInserer(actionEvent);
		
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Solstyce", 
	    		"Traçabilite"
	    		)); 
		}
    } 
	
	public void refreshCorrection(){
//		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
//		modeEcranDefaut = params.get("modeEcranDefaut");
//		
//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		controleConformiteJSF = (ControleConformiteJSF) sessionMap.get("controle");
//
//		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
//			modeEcranDefaut = C_DIALOG;
//			actInserer(null);
//		} else {
//			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
//		}

	}

	public void actFermerDialogCorrection(ActionEvent actionEvent) {
		//PrimeFaces.current().dialog().closeDynamic(null);
//		showCorrection = false;
	}

	
	public void actEnregistrerCorrection(ActionEvent actionEvent){
//		controleConformiteJSF.setAction(ControleConformiteJSF.C_TYPE_ACTION_CORRIGE);
		//PrimeFaces.current().dialog().closeDynamic(controleConformiteJSF);
//		showCorrection = false;
	}
	
	public void validateCorrection(AjaxBehaviorEvent event){
		//http://stackoverflow.com/questions/9805276/jsf-and-primefaces-how-to-pass-parameter-to-a-method-in-managedbean
	    //System.out.println("controle : "+controleAVerifier.getValeurTexte());
		FacesContext context = FacesContext.getCurrentInstance();
		TaResultatCorrection c = context.getApplication().evaluateExpressionGet(context, "#{var}", TaResultatCorrection.class);
		//System.out.println("Validation du controle :"+c.getC().getLibelleConformite()+" avec la valeur : ("+c.getValeurTexte()+")("+c.getValeurBool()+")("+c.getDate()+")("+c.getValeurResultat()+")");
		if(event.getComponent().getClientId().contains("idCombo")) {
			if(c.getEffectuee()) {
				c.setEffectuee(true);
				c.setValide(true);
			} else {
				c.setEffectuee(false);
				c.setValide(false);
			}
		} else if(event.getComponent().getClientId().contains("idTxt")) {
			if(c.getObservation()!=null && !c.getObservation().equals("")) {
				c.setValide(true);
			} else {
				c.setValide(false);
			}
		}
		
		
	}
	
	public boolean etatBoutonCorrection(String bouton) {
		boolean retour = true;
		
		
		FacesContext context = FacesContext.getCurrentInstance();
//		TaResultatCorrection c = context.getApplication().evaluateExpressionGet(context, "#{var}", TaResultatCorrection.class);
		
//		switch (bouton) {
//			case "enregistrer":
//				boolean toutValide = true;
//				for(TaResultatCorrection a : controleConformiteJSF.getR().getTaResultatCorrections()) {
//					if(!a.getValide()) {
//						toutValide=false;
//						break;
//					}
//				}
//				retour = !toutValide;
//				break;
//			case "precedent":
//				if(controleConformiteJSF.getR().getTaResultatCorrections().indexOf(c)!=0) {
//					retour = false;
//				}
//				break;
//			case "suivant":
//				if((c.getValide()!=null && c.getValide() )
//					&& controleConformiteJSF.getR().getTaResultatCorrections().indexOf(c)!=controleConformiteJSF.getR().getTaResultatCorrections().size()-1
//					) {
//					retour = false;
//				}
//				
//				break;
//			default:
//				break;
//		}
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

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public TaLot getTaLot() {
		return taLot;
	}

	public void setTaLot(TaLot taLot) {
		this.taLot = taLot;
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

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public TabViewsBean getTabViews() {
		return tabViews;
	}

	public void setTabViews(TabViewsBean tabViews) {
		this.tabViews = tabViews;
	}

	public List<DetailLotOngletJSF> getListeLot() {
		return listeLot;
	}

	public void setListeLot(List<DetailLotOngletJSF> listeLot) {
		this.listeLot = listeLot;
	}

	public Boolean getOrigine() {
		return origine;
	}

	public void setOrigine(Boolean origine) {
		this.origine = origine;
	}

	public Boolean getUtilisation() {
		return utilisation;
	}

	public void setUtilisation(Boolean utilisation) {
		this.utilisation = utilisation;
	}

	public String getSens() {
		return sens;
	}

	public void setSens(String sens) {
		this.sens = sens;
	}

//	public List<ControleConformiteJSF> getListeControle() {
//		return listeControle;
//	}
//
//	public List<TaGroupe> getListeGroupe() {
//		return listeGroupe;
//	}
//
//	public ControleConformiteJSF getControleAVerifier() {
//		return controleAVerifier;
//	}
//
//	public void setControleAVerifier(ControleConformiteJSF controleAVerifier) {
//		this.controleAVerifier = controleAVerifier;
//	}
//
//	public ControleConformiteJSF getControleConformiteJSF() {
//		return controleConformiteJSF;
//	}
//
//	public void setControleConformiteJSF(ControleConformiteJSF controleConformiteJSF) {
//		this.controleConformiteJSF = controleConformiteJSF;
//	}
}  

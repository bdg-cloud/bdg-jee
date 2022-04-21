package fr.legrain.bdg.webapp.synchronisation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaCompteServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLiaisonServiceExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaLigneShippingboServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaShippingBoServiceWebExterneServiceRemote;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaSynchroServiceExterneServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.bdg.webapp.app.TabViewsBean;
import fr.legrain.bdg.webapp.documents.ILigneDocumentJSF;
import fr.legrain.bdg.webapp.documents.OuvertureArticleBean;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.bdg.webapp.documents.TaLigneServiceWebExterneDTOJSF;
import fr.legrain.bdg.webapp.documents.TaLigneServiceWebExterneDTOJSF;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.ImageLgr;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLEcheance;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.generation.service.CreationDocumentFlashMultiple;
import fr.legrain.generation.service.CreationDocumentLigneServiceWebExterneMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.EnteteDocExterne;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;


@SuppressWarnings({ "rawtypes", "rawtypes" })
@Named
@ViewScoped  
public class ShippingBoController extends AbstractController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3850888132933080252L;
	
	@EJB private ITaShippingBoServiceWebExterneServiceRemote shippingBoService;
	@EJB private ITaLigneServiceWebExterneServiceRemote ligneServiceWebExterneService;
	@EJB private ITaLiaisonServiceExterneServiceRemote liaisonService;
	@EJB private ITaServiceWebExterneServiceRemote serviceWebExterneService;
	@EJB private ITaCompteServiceWebExterneServiceRemote compteServiceWebExterneService;
	protected @EJB ITaTiersServiceRemote taTiersService;
	protected @EJB ITaArticleServiceRemote taArticleService;
	protected @EJB ITaFactureServiceRemote taFactureService;
	protected @EJB ITaPreferencesServiceRemote taPreferencesService;
	protected @EJB ITaMouvementStockServiceRemote taMouvementStockService;
	protected @EJB ITaLotServiceRemote taLotService;
	@EJB private ITaSynchroServiceExterneServiceRemote synchroService;
	@EJB private CreationDocumentLigneServiceWebExterneMultiple creation;
	
	protected @EJB ITaTPaiementServiceRemote taTPaiementService;
	
	private StreamedContent exportationLigneServiceExterne;
	
	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

    @Inject @Named(value="ouvertureArticleBean")
    private OuvertureArticleBean ouvertureArticleBean;

	
	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;
	
	@Inject @Named(value="tabViewsBean")
	private TabViewsBean tabViews;
	
	protected ModeObjetEcranServeur modeEcranLigne = new ModeObjetEcranServeur();
	
	
	private List<TaLigneServiceWebExterne> lignesServiceWebExterne = new ArrayList<TaLigneServiceWebExterne>();
	
	private List<TaLigneServiceWebExterne> selectedlignesServiceWebExterne = new ArrayList<TaLigneServiceWebExterne>();
	
	private List<TaLigneServiceWebExterneDTO> lignesServiceWebExterneDTO = new ArrayList<TaLigneServiceWebExterneDTO>();
	private List<TaLigneServiceWebExterneDTO> lignesServiceWebExterneDTOTermine = new ArrayList<TaLigneServiceWebExterneDTO>();
	
	private List<TaLigneServiceWebExterneDTO> selectedlignesServiceWebExterneDTO = new ArrayList<TaLigneServiceWebExterneDTO>();
	
	protected List<TaLigneServiceWebExterneDTOJSF> lignesDTOJSF= new ArrayList<TaLigneServiceWebExterneDTOJSF>();
	
	private TreeNode treeNodeLignesDTOJSF;
	private TreeNode[] selectedNodesLignesDTOJSF;
	protected TreeNode selectedTreeNodeLignesDTOJSF;
	
	protected List<TaLigneServiceWebExterneDTOJSF> selectedlignesDTOJSF= new ArrayList<TaLigneServiceWebExterneDTOJSF>();
	protected TaLigneServiceWebExterneDTOJSF selectedLigneDTOJSF;
	
	
	protected ImageLgr selectedTypeCreation;
	
	protected TaLigneServiceWebExterne ligneDocument = null;
	
	private List<SelectItem> listeParamCreeDoc;
	private String selectionParamCreeDoc=C_PARAM_TIERS;

	public static final String C_PARAM_TIERS = "par tiers";

	private boolean fusionArticle = false;
	
	private boolean regleDoc = false;
	
	
	
	private TaServiceWebExterne serviceWeb;
	
	private TaLigneServiceWebExterne selectedligneServiceWebExterne;
	protected TaTiersDTO taTiersDTO;
	protected TaLigneServiceWebExterneDTOJSF detailLigneOverLayPanel = null;
	//list permettant de faire la correspondance entre les lots / articles , les entrepot et les emplacement
	protected List<ArticleLotEntrepotDTO> listArticleLotEntrepot;
	
	private Date derniereSynchro;
	private String typeEntite = "";
	
	@PostConstruct
	public void postConstruct(){
		try {
			serviceWeb = serviceWebExterneService.findByCode("SHIPPINGBO");
			typeEntite = TaSynchroServiceExterne.TYPE_ENTITE_COMMANDE;
			listeParamCreeDoc = new ArrayList<SelectItem>();
			listeParamCreeDoc.add(new SelectItem(C_PARAM_TIERS, C_PARAM_TIERS));
			rechercheAvecCommentaire(true);

		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refresh();
	}
	
	public void updateDateDernierSynchro() {
		
		TaCompteServiceWebExterne compteServiceWeb;
		try {
			compteServiceWeb = compteServiceWebExterneService.findCompteDefautPourFournisseurService(serviceWeb.getCodeServiceWebExterne());
			if(compteServiceWeb != null) {
				derniereSynchro=synchroService.findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO(typeEntite, compteServiceWeb.getIdCompteServiceWebExterne());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void refresh()  {
		//lignesServiceWebExterne = ligneServiceWebExterneService.selectAll();
		updateDateDernierSynchro();
		
		lignesServiceWebExterneDTO = ligneServiceWebExterneService.findAllDTONonTermineByIdServiceWebExterne(serviceWeb.getIdServiceWebExterne());
		lignesServiceWebExterneDTOTermine = ligneServiceWebExterneService.findAllDTOTermineByIdServiceWebExterne(serviceWeb.getIdServiceWebExterne());
		lignesDTOJSF.clear();
		
		selectedTreeNodeLignesDTOJSF = null;
		selectedNodesLignesDTOJSF = null;
		if(selectedlignesDTOJSF != null) {
			selectedlignesDTOJSF.clear();
		}
		
		selectedTypeCreation = new ImageLgr();
		selectedTypeCreation.setDisplayName("Facture");
		selectedTypeCreation.setName("Facture");
		
		//faire une recherche avec des dates, codeTiers etc comme sur l'écran de flash d'Isa		
//		List<TaLigneALigneDTO> l= modelLigneALigne.remplirListe(dateDeb,
//				dateFin,codeTiers,
//				selectedTypeSelection.getName(),selectedTypeCreation.getName(),document,selectionEtat);
		
		for (TaLigneServiceWebExterneDTO ligne : lignesServiceWebExterneDTO) {
			TaLigneServiceWebExterneDTOJSF ll=new TaLigneServiceWebExterneDTOJSF();
			if(ligne.getCodeArticle()!=null) {
				TaArticleDTO art;
				try {
					art = taArticleService.findByCodeDTO(ligne.getCodeArticle());
					if(art!=null)ll.setTaArticleDTO(art);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(ligne.getCodeTiers()!=null) {
				TaTiersDTO tiers;
				try {
					tiers = taTiersService.findByCodeDTO(ligne.getCodeTiers());
					if(tiers!=null)ll.setTaTiersDTO(tiers);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(ligne.getCodeTPaiement()!=null) {
				TaTPaiementDTO tp;
				try {
					tp = taTPaiementService.findByCodeDTO(ligne.getCodeTPaiement());
					if(tp!=null)ll.setTaTPaiementDTO(tp);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(ligne.getNumLot()!=null) {
				TaLotDTO lot;
				try {
					lot = taLotService.findByCodeDTO(ligne.getNumLot());
					if(lot!=null)ll.setTaLotDTO(lot);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			ll.setDto(ligne);
			ll.setDtoOld(ll.getDto().copy(ligne));

			lignesDTOJSF.add(ll);
		}
		
		rempliNodes();
		
		
		
	}
	
	private void rempliNodes() {
		//on regroupe par commande
		treeNodeLignesDTOJSF = new CheckboxTreeNode(null, null);
		Map<String,List<TaLigneServiceWebExterneDTOJSF>> mapLignesParCommandes = new HashMap<>();
		for (TaLigneServiceWebExterneDTOJSF ech : lignesDTOJSF) {
			
				//String cle = ech.getTaStripeSubscription().getTaAbonnement().getTaTiers().getCodeTiers();
				String cle = ech.getDto().getRefCommandeExterne();
				if(!mapLignesParCommandes.keySet().contains(cle)) {
					mapLignesParCommandes.put(cle, new ArrayList<>());
				}
				mapLignesParCommandes.get(cle).add(ech);
			
		}
		
		for (String idCommande : mapLignesParCommandes.keySet()) {
			boolean complete = true;
			if(!mapLignesParCommandes.get(idCommande).isEmpty()) {
				TaLigneServiceWebExterneDTOJSF commande = new TaLigneServiceWebExterneDTOJSF();
				TaLigneServiceWebExterneDTO dto = new TaLigneServiceWebExterneDTO();
				TaTiersDTO tiersDTO = mapLignesParCommandes.get(idCommande).get(0).getTaTiersDTO();
				TaTPaiementDTO taTPaiementDTO = mapLignesParCommandes.get(idCommande).get(0).getTaTPaiementDTO();
				TaLigneServiceWebExterneDTO dtoEntete = mapLignesParCommandes.get(idCommande).get(0).getDto();
				dto.setEnteteDoc(true);
				dto.setDateCreationExterne(dtoEntete.getDateCreationExterne());
				dto.setLibelle("Commande n° "+idCommande);
//				dto.setMontantHt(montantHt);
				dto.setIdCommandeExterne(idCommande);
				dto.setRefCommandeExterne(dtoEntete.getRefCommandeExterne());
				dto.setMontantTtc(dtoEntete.getMontantTtcTotalDoc());
				dto.setRefTiers(dtoEntete.getRefTiers());
				dto.setRefTypePaiement(dtoEntete.getRefTypePaiement());
				dto.setNomTiers(dtoEntete.getNomTiers());
				dto.setPrenomTiers(dtoEntete.getPrenomTiers());
				dto.setCodeTiers(mapLignesParCommandes.get(idCommande).get(0).getTaTiersDTO().getCodeTiers());
				commande.setDto(dto );
				commande.setTaTiersDTO(tiersDTO);
				commande.setTaTPaiementDTO(taTPaiementDTO);
				TreeNode root = new CheckboxTreeNode(commande, treeNodeLignesDTOJSF);
				
				//je parcours  les lignes de cette commandes
				for (TaLigneServiceWebExterneDTOJSF ligne : mapLignesParCommandes.get(idCommande)) {
					TreeNode ligneNode = new CheckboxTreeNode(ligne, root);
					ligneNode.setSelectable(false);
					//je vérifie que chaque ligne est complete pour rendre la ligne séléctionnable ou non
					if(ligne.getTaTiersDTO() == null || ligne.getTaArticleDTO() == null || ligne.getTaArticleDTO().getCodeTva() == null) {
					   complete = false;
					}
				}
				
				if(complete) {
					commande.getDto().setComplete(complete);
				}else {
					commande.getDto().setComplete(complete);
					root.setSelectable(false);
				}
			}
			
		}
	}
	
	public boolean disabledBoutonCreation() {
		boolean disabled = true;
		if(selectedNodesLignesDTOJSF != null && selectedNodesLignesDTOJSF.length>0) {
			disabled =  false;
		}
		return disabled;
	}
	
    public void actEnregistrer() {  	
		System.out.println("actEnregistrer  ");
		FacesContext context = FacesContext.getCurrentInstance();
				//créer le document à partir des documents selectionné dans le modelDocument
				final  ParamAfficheChoixGenerationDocument paramTmp = new ParamAfficheChoixGenerationDocument();
				ParamAfficheChoixGenerationDocument param = new ParamAfficheChoixGenerationDocument();
				String finDeLigne = "\r\n";
				param.setTypeSrc("");
				param.setTypeDest("");
				String libelle="Reprise de ";
				
				
				paramTmp.setLibelle("Lignes de commande ShippingBo séléctionnées");
				
		if(selectedNodesLignesDTOJSF != null && selectedNodesLignesDTOJSF.length>0) {
			actDialogGenerationDocument(null);
		}else {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide", "Veuillez choisir au moins une ligne pour pouvoir générer un document.")); 
		} 
		
    }
    
    public void actRafraichirLiaisons() {
    	lignesServiceWebExterneDTO = ligneServiceWebExterneService.findAllDTONonTermine();
    	for (TaLigneServiceWebExterneDTO ligneDTO : lignesServiceWebExterneDTO) {
    		TaLigneServiceWebExterne ligne;
			try {
				ligne = ligneServiceWebExterneService.findById(ligneDTO.getId());
				ligne = ligneServiceWebExterneService.affecteLiaison(ligne, serviceWeb);
			} catch (FinderException e) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur pendant le rafraîchissement d'une ligne : ", e.getMessage() ));
				e.printStackTrace();
			}
    		
		}
    	refresh();
    	FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rafraîchissement des lignes terminé ","" ));
    	
    }
    
    public List<ArticleLotEntrepotDTO> lotAutoComplete(String query) {
    	FacesContext context = FacesContext.getCurrentInstance();
    	String codeArticle = (String) UIComponent.getCurrentComponent(context).getAttributes().get("codeArticle");
    	if(codeArticle != null) {
    		return onChangeListArticleMP(codeArticle,query);
    	}
		return null;
	}
	
	// copier de Fabricationcontroller ==> à homogénéiser si au final les 2 méthodes reste identique (voir aussi webservice rest des articles)
	public List<ArticleLotEntrepotDTO> onChangeListArticleMP(String codeArticle, String numlot) {
		try {
			
			TaPreferences p = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_LOTS, ITaPreferencesServiceRemote.LOTS_AUTORISER_UTILISATION_LOTS_NON_CONFORME);
			Boolean utiliserLotNonConforme = null;
			if(p!=null && LibConversion.StringToBoolean(p.getValeur()) != null) {
				utiliserLotNonConforme = LibConversion.StringToBoolean(p.getValeur());
			}
			
			listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
			listArticleLotEntrepot = taMouvementStockService.getEtatStockByArticle(codeArticle,utiliserLotNonConforme);
			List<ArticleLotEntrepotDTO> temp= new ArrayList<ArticleLotEntrepotDTO>();
			List<BigDecimal> qte = new ArrayList<BigDecimal>();			
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
    
    
    
    
    
    
    
    
    
    public void actDialogGenerationDocument(ActionEvent actionEvent) {

		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("modal", true);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Map<String,ParamAfficheChoixGenerationDocument> mapEdition = new HashMap<String,ParamAfficheChoixGenerationDocument>();
		ParamAfficheChoixGenerationDocument paramGeneration=new ParamAfficheChoixGenerationDocument();
		paramGeneration.setTypeSrc("");
		paramGeneration.setTypeDest(selectedTypeCreation.getName());
//		String libelle="Reprise de ";
		paramGeneration.setDateDocument(new Date());
		paramGeneration.setDateLivraison(new Date());
		paramGeneration.setLibelle("");
		paramGeneration.setTiersModifiable(false);
		paramGeneration.setMultiple(true);
		

		paramGeneration.setTitreFormulaire("Génération multiple");
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}
    
	public void handleReturnDialogGenerationDocument(SelectEvent event) {
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			
			if(selectedNodesLignesDTOJSF != null && selectedNodesLignesDTOJSF.length>0){
				if(event!=null && event.getObject()!=null) {
					if(event.getObject() instanceof ParamAfficheChoixGenerationDocument){
						ParamAfficheChoixGenerationDocument param =  (ParamAfficheChoixGenerationDocument) event.getObject();
						if(param.getRetour()){
							String finDeLigne = "\r\n";
		
							List<IDocumentTiers> listeDocCrees=new LinkedList<IDocumentTiers>();
		
		
							param.getListeLigneServiceWebExterneSrc().clear();
							param.setDateLivraison(param.getDateDocument());
							
							List<EnteteDocExterne> listeEnteteDoc = new ArrayList<>();
							//pour chaque ligne d'entete de commande, on créer un objet enteteDocExterne
							for (TreeNode node : selectedNodesLignesDTOJSF) {
								EnteteDocExterne entete = new EnteteDocExterne();
								TaLigneServiceWebExterneDTOJSF ligneJSF = (TaLigneServiceWebExterneDTOJSF) node.getData();
								TaLigneServiceWebExterneDTO dto = ligneJSF.getDto();
								
								if(dto.getEnteteDoc()) {
									entete.setIdCommandeExterne(dto.getIdCommandeExterne());
									entete.setDateCreationExterne(dto.getDateCreationExterne());
									entete.setTaTiersDTO(ligneJSF.getTaTiersDTO());
									
									
									for (TreeNode ligneNode : node.getChildren()) {
										TaLigneServiceWebExterneDTOJSF ligneJSFChildren = (TaLigneServiceWebExterneDTOJSF) ligneNode.getData();
										TaLigneServiceWebExterneDTO dtoChildren = ligneJSFChildren.getDto();
										TaLigneServiceWebExterne l = ligneDocumentValide(dtoChildren.getId());
										//entete.getListeLigneServiceWebExterneDTO().add(dtoChildren);
										entete.getListeLigneServiceWebExterne().add(l);
									}
									
									listeEnteteDoc.add(entete);
								}
								
								
								
							}
							
												
							
							//trie de la liste de commande par date de création commande 
							Collections.sort(listeEnteteDoc, new Comparator<EnteteDocExterne>() {
								  public int compare(EnteteDocExterne o1, EnteteDocExterne o2) {
								      return o1.getDateCreationExterne().compareTo(o2.getDateCreationExterne());
								  }
								});
							//on parcours les commandes et on créer un doc par commandes
							RetourGenerationDoc retour=null;
							for (EnteteDocExterne enteteDocExterne : listeEnteteDoc) {
								String idCommande = null;
								String codeTiers = null;
								for (TaLigneServiceWebExterne l : enteteDocExterne.getListeLigneServiceWebExterne()) {
									//ajoute les lignes pour un meme commande a param
									if(!param.getListeLigneServiceWebExterneSrc().contains(l))
										param.getListeLigneServiceWebExterneSrc().add(l);
									
									 codeTiers=l.getTaTiers().getCodeTiers();
									 idCommande=l.getIdCommandeExterne();
								}
								
								//creation document						
								if(param.getListeLigneServiceWebExterneSrc().size()!=0){
									retour=null; 
									param.setCodeTiers(codeTiers);
									param.setIdDocumentExterne(idCommande);
									param.setCodeServiceExterne(serviceWeb.getCodeServiceWebExterne());
									param.setTypeEntiteExterne(typeEntite);
									creation.setCodeTiers(codeTiers);
									creation.setIdDocumentExterne(idCommande);
									creation.setParam(param);
									creation.setRegleDoc(regleDoc);
									retour=creation.creationDocument(false);
									if(retour!=null && retour.isRetour()&&retour.getDoc()!=null){
										listeDocCrees.add(retour.getDoc());								
									}
								}
								
								param.getListeLigneServiceWebExterneSrc().clear();
								
								
							}
							
									
		
							String message="";
							for (IDocumentTiers iDocumentTiers : listeDocCrees) {
								message+=iDocumentTiers.getCodeDocument()+finDeLigne;
							}
							if (!message.equals("")){
								LgrTab typeDocPresent = LgrTab.getInstance();
								String tab = typeDocPresent.getTabDocCorrespondance().get(selectedTypeCreation.getName());
								retour.setTypeTabCSS("tab tab-preparation");
								retour.setOuvrirDoc(true);
								if(retour.getMessage()!=null && !retour.getMessage().isEmpty())
									context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Création document", retour.getMessage()));
								if(retour.isOuvrirDoc())
									ouvertureDocumentBean.detailDocument(retour.getDoc());						
							}
		
		
						}
		
		
					}
		
				}
				refresh();
			}else{
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide","Aucunes lignes séléctionnés")); 	
			}
				
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
    
    
	private TaLigneServiceWebExterne ligneDocumentValide(Integer id){
		try {	
				//ligneDocument=ligneServiceWebExterneService.findById(id);
			ligneDocument=ligneServiceWebExterneService.findByIdAvecTiers(id);
			return ligneDocument;
		} catch (FinderException e) {
			return null;
		}
	}
    

    
    public void onRowSelectLigneShippingboAll(SelectEvent event){
    	System.out.println("onRowSelectLigneShippingboAll a etait cliquéééé");
    }
    public void onRowUnSelectLigneShippingboAll(NodeUnselectEvent event){
    	System.out.println("onRowUnSelectLigneShippingboAll a etait cliquéééé");
    	System.out.println(selectedNodesLignesDTOJSF.length);
    }
    public void unSelect(UnselectEvent event){
    	System.out.println("unSelect a etait cliquéééé");
    }
    public void onToggleSelectLigneShippingboAll(ToggleSelectEvent event){
    	System.out.println("onToggleSelectLigneShippingboAll a etait cliquéééé");
    }
    public void actionGroupee2() {
    	System.out.println("action groupée 2");
    }
    
	public void onRowEditInit(RowEditEvent event) {
		selectedTreeNodeLignesDTOJSF =  (TreeNode) event.getObject();
		actModifier(null);
	}
	public void actModifier(ActionEvent actionEvent) {		
		if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION)) {
			modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
		}
			
	}
	//pour entete
	public List<TaLigneServiceWebExterne> editEnteteFinal(TaLigneServiceWebExterneDTOJSF ligneCommandeJSF){
		TaTPaiement taTPaiement = null;
		TaTiers tiers = null;
		List<TaLigneServiceWebExterne> listeAMerge = new ArrayList<TaLigneServiceWebExterne>();
		try {
			if(ligneCommandeJSF.getTaTPaiementDTO() != null) {
				//si il y a une ref tier
				if(ligneCommandeJSF.getDto().getRefTypePaiement() != null) {
					TaLiaisonServiceExterne liaisonPaiement = liaisonService.findByIdTaTPaiementAndByIdServiceWebExterne(ligneCommandeJSF.getTaTPaiementDTO().getId(), serviceWeb.getIdServiceWebExterne());
					TaLiaisonServiceExterne liaisonRefPaiement = liaisonService.findByRefTaTPaiementAndByIdServiceWebExterne(ligneCommandeJSF.getDto().getRefTypePaiement(), serviceWeb.getIdServiceWebExterne());
					
					//si une liaison avec cette ref existe deja, on la supprime
					if(liaisonRefPaiement != null ) {
						 liaisonService.supprimer(liaisonRefPaiement);
					}
					//une liaison existe deja, on met juste la ref
					if(liaisonPaiement != null) {
						liaisonPaiement.setRefExterne(ligneCommandeJSF.getDto().getRefTypePaiement());
					}else {
						//aucune liaison donc on l'a créé
						liaisonPaiement = new TaLiaisonServiceExterne();
						liaisonPaiement.setIdEntite(ligneCommandeJSF.getTaTPaiementDTO().getId());
						liaisonPaiement.setRefExterne(ligneCommandeJSF.getDto().getRefTypePaiement());
						liaisonPaiement.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT);
						liaisonPaiement.setServiceWebExterne(serviceWeb);
					}
					
					liaisonService.merge(liaisonPaiement);
				}
				
				taTPaiement = taTPaiementService.findByCode(ligneCommandeJSF.getTaTPaiementDTO().getCodeTPaiement());
			}//si pas de taTPaiement
			
			
			if(ligneCommandeJSF.getTaTiersDTO() != null) {
				//si il y a une ref tier
				if(ligneCommandeJSF.getDto().getRefTiers() != null) {
					TaLiaisonServiceExterne liaisonTiers = liaisonService.findByIdTiersAndByIdServiceWebExterne(ligneCommandeJSF.getTaTiersDTO().getId(), serviceWeb.getIdServiceWebExterne());
					TaLiaisonServiceExterne liaisonRefTiers = liaisonService.findByRefTiersAndByIdServiceWebExterne(ligneCommandeJSF.getDto().getRefTiers(), serviceWeb.getIdServiceWebExterne());
					
					if(liaisonRefTiers != null ) {
						 liaisonService.supprimer(liaisonRefTiers);
					}
					
					
					//une liaison existe deja, on met juste la ref
					if(liaisonTiers != null) {
						liaisonTiers.setRefExterne(ligneCommandeJSF.getDto().getRefTiers());
					}else {
						//aucune liaison donc on l'a créé
						liaisonTiers = new TaLiaisonServiceExterne();
						liaisonTiers.setIdEntite(ligneCommandeJSF.getTaTiersDTO().getId());
						liaisonTiers.setRefExterne(ligneCommandeJSF.getDto().getRefTiers());
						liaisonTiers.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS);
						liaisonTiers.setServiceWebExterne(serviceWeb);
					}
					
					
					liaisonService.merge(liaisonTiers);
				}
				tiers = taTiersService.findByCode(ligneCommandeJSF.getTaTiersDTO().getCodeTiers());
			}
			
									
				
			String idCommande = ligneCommandeJSF.getDto().getIdCommandeExterne();
			
			
			for (TreeNode treeNode : treeNodeLignesDTOJSF.getChildren()) {
				TaLigneServiceWebExterneDTOJSF li = (TaLigneServiceWebExterneDTOJSF) treeNode.getData();
				if(li.getDto().getEnteteDoc() && li.getDto().getIdCommandeExterne().equals(idCommande)) {
					for (TreeNode node : treeNode.getChildren()) {
						TaLigneServiceWebExterneDTOJSF liChild = (TaLigneServiceWebExterneDTOJSF) node.getData();
						try {
							TaLigneServiceWebExterne lis = ligneServiceWebExterneService.findById(liChild.getDto().getId());
							
							if(ligneCommandeJSF.getTaTPaiementDTO() != null && taTPaiement != null) {
								li.setTaTPaiementDTO(ligneCommandeJSF.getTaTPaiementDTO());
								lis.setTaTPaiement(taTPaiement);
							}
							if(ligneCommandeJSF.getTaTiersDTO() != null && tiers != null) {
								li.setTaTiersDTO(ligneCommandeJSF.getTaTiersDTO());
								lis.setTaTiers(tiers);
							}
							
							
							
							listeAMerge.add(lis);
						} catch (FinderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
				}
			}
				
						
			
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listeAMerge;
	}
	
	
	
	public void onRowEdit(RowEditEvent event) throws Exception {
		System.out.println("onRowEdit a etait cliquéééé");
		TreeNode node = (TreeNode) event.getObject();
		TaLigneServiceWebExterneDTOJSF ligne = (TaLigneServiceWebExterneDTOJSF) node.getData();
		TaArticle article;
		TaTPaiement taTPaiement;
		TaTiers tiers;
		TaLot lot;
		List<TaLigneServiceWebExterne> listeAMerge = new ArrayList<TaLigneServiceWebExterne>();
		try {
			if(ligne != null) {
				
				
				
					//si c'est une entête de commande
					if(ligne.getDto().getEnteteDoc()) {
						
						//on va attribuer le tiers à toutes les lignes de la commande et les merger 
						listeAMerge.addAll(editEnteteFinal(ligne)); 
						for (TaLigneServiceWebExterne li : listeAMerge) {
							ligneServiceWebExterneService.merge(li);
						}
						
						
					//si c'est une ligne normale	
					}else {
						boolean merge = false;
						TaLigneServiceWebExterne ligneService = ligneServiceWebExterneService.findById(ligne.getDto().getId());
						if(ligneService != null) {
							if(ligne.getTaTiersDTO() != null) {
								//si il y a une ref tier
								if(ligne.getDto().getRefTiers() != null) {
									TaLiaisonServiceExterne liaison = liaisonService.findByIdTiersAndByIdServiceWebExterne(ligne.getTaTiersDTO().getId(), serviceWeb.getIdServiceWebExterne());
									TaLiaisonServiceExterne liaisonRef = liaisonService.findByRefTiersAndByIdServiceWebExterne(ligne.getDto().getRefTiers(), serviceWeb.getIdServiceWebExterne());
									//si une liaison avec cette ref existe deja, on la supprime
									if(liaisonRef != null ) {
										 liaisonService.supprimer(liaisonRef);
									}
									//une liaison existe deja, on met juste la ref
									if(liaison != null) {
										liaison.setRefExterne(ligne.getDto().getRefTiers());
									}else {
										//aucune liaison donc on l'a créé
										liaison = new TaLiaisonServiceExterne();
										liaison.setIdEntite(ligne.getTaTiersDTO().getId());
										liaison.setRefExterne(ligne.getDto().getRefTiers());
										liaison.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_TIERS);
										liaison.setServiceWebExterne(serviceWeb);
									}					
									
									
									liaisonService.merge(liaison);
								}
								
								
								tiers = taTiersService.findByCode(ligne.getTaTiersDTO().getCodeTiers());
								ligneService.setTaTiers(tiers);
								merge = true;
							}
						
						
						
							if(ligne.getTaArticleDTO() != null) {
								
								if(ligne.getDto().getRefArticle() != null && !ligne.getDto().getRefArticle().equals("")) {
									TaLiaisonServiceExterne liaison = liaisonService.findByIdArticleAndByIdServiceWebExterne(ligne.getTaArticleDTO().getId(), serviceWeb.getIdServiceWebExterne());
									TaLiaisonServiceExterne liaisonRef = liaisonService.findByRefArticleAndByIdServiceWebExterne(ligne.getDto().getRefArticle(), serviceWeb.getIdServiceWebExterne());
									//si une liaison avec cette ref existe deja, on la supprime
									if(liaisonRef != null ) {
										 liaisonService.supprimer(liaisonRef);
									}
									//si une liaison avec cet artice existe deja, on la modifie, sinon on l'a crée
									if(liaison != null) {
										liaison.setRefExterne(ligne.getDto().getRefArticle());
									}else {
										liaison = new TaLiaisonServiceExterne();
										liaison.setIdEntite(ligne.getTaArticleDTO().getId());
										liaison.setRefExterne(ligne.getDto().getRefArticle());
										liaison.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_ARTICLE);
										liaison.setServiceWebExterne(serviceWeb);
									}
															
															
									liaisonService.merge(liaison);
								}
								
								
								article = taArticleService.findByCode(ligne.getTaArticleDTO().getCodeArticle());
								ligneService.setTaArticle(article);
								merge = true;
								
							}
							if(ligne.getTaTPaiementDTO() != null) {
								
								if(ligne.getDto().getRefTypePaiement() != null && !ligne.getDto().getRefTypePaiement().equals("")) {
									TaLiaisonServiceExterne liaison = liaisonService.findByIdTaTPaiementAndByIdServiceWebExterne(ligne.getTaTPaiementDTO().getId(), serviceWeb.getIdServiceWebExterne());
									TaLiaisonServiceExterne liaisonRef = liaisonService.findByRefTaTPaiementAndByIdServiceWebExterne(ligne.getDto().getRefTypePaiement(), serviceWeb.getIdServiceWebExterne());
									//si une liaison avec cette ref existe deja, on la supprime
									if(liaisonRef != null ) {
										 liaisonService.supprimer(liaisonRef);
									}
									//si une liaison avec cet artice existe deja, on la modifie, sinon on l'a crée
									if(liaison != null) {
										liaison.setRefExterne(ligne.getDto().getRefTypePaiement());
									}else {
										liaison = new TaLiaisonServiceExterne();
										liaison.setIdEntite(ligne.getTaTPaiementDTO().getId());
										liaison.setRefExterne(ligne.getDto().getRefTypePaiement());
										liaison.setTypeEntite(TaLiaisonServiceExterne.TYPE_ENTITE_T_PAIEMENT);
										liaison.setServiceWebExterne(serviceWeb);
									}
															
															
									liaisonService.merge(liaison);
								}
								
								
								taTPaiement = taTPaiementService.findByCode(ligne.getTaTPaiementDTO().getCodeTPaiement());
								ligneService.setTaTPaiement(taTPaiement);
								merge = true;
								
							}
							
							if(ligne.getArticleLotEntrepotDTO() != null) {
								if(ligne.getArticleLotEntrepotDTO().getIdLot() != null) {
									lot = taLotService.findById(ligne.getArticleLotEntrepotDTO().getIdLot());
									ligneService.setTaLot(lot);
									merge = true;
								}
								
							}
						
							//on merge la ligne (qui n'est pas une entête)
							if(merge) {
								ligneServiceWebExterneService.merge(ligneService);
							}
						
						}
					}
					
					
				
				
			}
			
			//refresh();
			
			//actRafraichirLiaisons();

		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ligne", e.getMessage()));
			context.validationFailed();
		}
		
		
	}
	
	public void enregistreLigne()  throws Exception {
		//code à Isa a adapter
//		selectedLigneALigneDTO.getDto().setAccepte(true);
//		selectedLigneALigneDTO.setDtoOld(selectedLigneALigneDTO.getDto().copy(selectedLigneALigneDTO.getDto()));
//		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		if(selectedlignesServiceWebExterne.get(0) != null) {
			
		}
	}

	public void onRowCancel(RowEditEvent event) {

//		selectionLigne((LigneJSF) event.getObject());
		actAnnuler(null);
    }

	//code d'Isa a adapté 
	public void actAnnuler(ActionEvent e) {
//		if(selectedLigneALigneDTO!=null) {
//		//on remet l'ancien
//		selectedLigneALigneDTO.setDto(selectedLigneALigneDTO.getDto().copy(selectedLigneALigneDTO.getDtoOld()));
//		selectedLigneALigneDTO.getListeSupplement().clear();
//		}
	}

	
	public void onClearTiers(AjaxBehaviorEvent event){
		taTiersDTO=null;
	}
	
	public void selectAllRow() {
		
		List<TreeNode> listeNodes = new ArrayList<TreeNode>();
		for (TreeNode treeNode : treeNodeLignesDTOJSF.getChildren()) {
			TaLigneServiceWebExterneDTOJSF li = (TaLigneServiceWebExterneDTOJSF) treeNode.getData();
			if(treeNode.isSelectable()) {
				if(li.getDto().getEnteteDoc()) {
					treeNode.setSelected(true);
					listeNodes.add(treeNode);
				}
			}
			
		}
		
		selectedNodesLignesDTOJSF = new TreeNode[listeNodes.size()];
		for (int i = 0; i < listeNodes.size(); i++) {
			selectedNodesLignesDTOJSF[i]=listeNodes.get(i);
        }
		
	}
	
	public void deselectAllRow() {
		selectedNodesLignesDTOJSF = null;
		for (TreeNode treeNode : treeNodeLignesDTOJSF.getChildren()) {
			TaLigneServiceWebExterneDTOJSF li = (TaLigneServiceWebExterneDTOJSF) treeNode.getData();
			if(li.getDto().getEnteteDoc()) {
				treeNode.setSelected(false);
			}
			
		}
		
	}

	
	public void actTelecharger(ActionEvent actionEvent) {
		try {
			Integer nbFiles = shippingBoService.recupereFichier();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Téléchargement terminé : ", nbFiles+" fichier(s) téléchargé(s)" ));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur pendant le téléchargement des fichiers depuis le serveur: ", e.getMessage()));
			context.validationFailed();
		}	
		
		updateDateDernierSynchro();
	}
	public void actParcourir(ActionEvent actionEvent) {
		Integer nbLignes;
		try {
			nbLignes = shippingBoService.parcourir();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Enregistrement des lignes depuis les fichiers terminé : ", nbLignes+" ligne(s) extraite(s)" ));
		} catch (EJBException e) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Attention, Il y a eu une ou plusieurs erreurs pendant l'enregistrement des lignes  : ", e.getMessage() ));
			e.printStackTrace();
		}
		refresh();
		
	}
	
	public List<TaArticleDTO> articleAutoCompleteDTOLight(String query) {
		List<TaArticleDTO> allValues = taArticleService.findByCodeAndLibelleLight(query.toUpperCase());
		List<TaArticleDTO> filteredValues = new ArrayList<TaArticleDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaArticleDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeArticle().toLowerCase().contains(query.toLowerCase())
					|| (civ.getLibellelArticle()!=null && civ.getLibellelArticle().toLowerCase().contains(query.toLowerCase()))
					|| (civ.getLibellecArticle()!=null && civ.getLibellecArticle().toLowerCase().contains(query.toLowerCase()))
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public void onClearArticle(AjaxBehaviorEvent event){
	   // selectedligneServiceWebExterne.setTaArticle(null);
	    validateUIField(Const.C_CODE_ARTICLE, null);

	}
	
	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("autcompleteSelection() : "+nomChamp);

		validateUIField(nomChamp,value);
	}
	
	public boolean validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_CODE_TIERS)) {
				TaTiers entity = null;
				//A adapter code d'Isa
				//selectedLigneALigneDTO.getDto().setCodeTiers(((TaTiersDTO)value).getCodeTiers());
			}else if(nomChamp.equals(Const.C_CODE_ARTICLE)) {
				TaArticle entity = null;
				if(value!=null){
					if(value instanceof TaArticleDTO){
						entity = taArticleService.findByCodeAvecLazy(((TaArticleDTO) value).getCodeArticle(),false);
					}else{
						entity = taArticleService.findByCodeAvecLazy((String)value,false);
					}
				}
				
//				if(selectedlignesServiceWebExterne.get(0) != null) {
//					
//				}
//				if(selectedligneServiceWebExterne.getTaArticle()== null || selectedligneServiceWebExterne.getTaArticle().getIdArticle()!=entity.getIdArticle()) {
//					selectedligneServiceWebExterne.setTaArticle(entity);
//				}
				

			}			
			return false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public List<TaTPaiementDTO> typePaiementAutoCompleteDTO(String query) {
		List<TaTPaiementDTO> allValues = taTPaiementService.selectAllDTO();
		List<TaTPaiementDTO> filteredValues = new ArrayList<TaTPaiementDTO>();
		TaTPaiementDTO cp = new TaTPaiementDTO();
		cp.setCodeTPaiement(Const.C_AUCUN);
		filteredValues.add(cp);
		for (int i = 0; i < allValues.size(); i++) {
			cp = allValues.get(i);
			if(query.equals("*") || cp.getCodeTPaiement().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(cp);
			}
		}

		return filteredValues;
	}
	public List<TaTiersDTO> tiersAutoCompleteDTOLight(String query) {
		List<TaTiersDTO> allValues = taTiersService.findByCodeLight("*");
		List<TaTiersDTO> filteredValues = new ArrayList<TaTiersDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaTiersDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getCodeTiers().toLowerCase().contains(query.toLowerCase())
					|| civ.getNomTiers().toLowerCase().contains(query.toLowerCase())
					|| (civ.getPrenomTiers() != null && civ.getPrenomTiers().toLowerCase().contains(query.toLowerCase()))
					|| (civ.getNomEntreprise() != null && civ.getNomEntreprise().toLowerCase().contains(query.toLowerCase()))
					) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public boolean etatBoutonLigne(String bouton) {
		boolean retour = true;
		if(modeEcran.dataSetEnModif()) {
			retour = false;
		}
		switch (modeEcranLigne.getMode()) {
		case C_MO_INSERTION:
			switch (bouton) {
			case "annuler":
			case "enregistrer":
			case "fermer":
				retour = false;
				break;
			case "rowEditor":
				//retour = modeEcran.dataSetEnModif()?true:false;
				retour = true;
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
			case "rowEditor":
				//retour = modeEcran.dataSetEnModif()?true:false;
				retour = true;
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
				retour = modeEcran.dataSetEnModif()?false:true;
				break;
			case "rowEditor":
				//retour = modeEcran.dataSetEnModif()?true:false;
				retour = true;
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
	


	
	public void actFermer(ActionEvent actionEvent) {


	}


	


	public List<TaLigneServiceWebExterne> getSelectedlignesServiceWebExterne() {
		return selectedlignesServiceWebExterne;
	}


	public void setSelectedlignesServiceWebExterne(List<TaLigneServiceWebExterne> selectedlignesServiceWebExterne) {
		this.selectedlignesServiceWebExterne = selectedlignesServiceWebExterne;
	}


	public TaLigneServiceWebExterne getSelectedligneShippingbo() {
		return selectedligneServiceWebExterne;
	}


	public void setSelectedligneShippingbo(TaLigneServiceWebExterne selectedligneServiceWebExterne) {
		this.selectedligneServiceWebExterne = selectedligneServiceWebExterne;
	}


	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}


	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}


	public TaServiceWebExterne getServiceWeb() {
		return serviceWeb;
	}


	public void setServiceWeb(TaServiceWebExterne serviceWeb) {
		this.serviceWeb = serviceWeb;
	}


	public ITaLigneServiceWebExterneServiceRemote getLigneServiceWebExterneService() {
		return ligneServiceWebExterneService;
	}


	public void setLigneServiceWebExterneService(ITaLigneServiceWebExterneServiceRemote ligneServiceWebExterneService) {
		this.ligneServiceWebExterneService = ligneServiceWebExterneService;
	}


	public List<TaLigneServiceWebExterne> getLignesServiceWebExterne() {
		return lignesServiceWebExterne;
	}


	public void setLignesServiceWebExterne(List<TaLigneServiceWebExterne> lignesServiceWebExterne) {
		this.lignesServiceWebExterne = lignesServiceWebExterne;
	}


	public TaLigneServiceWebExterne getSelectedligneServiceWebExterne() {
		return selectedligneServiceWebExterne;
	}


	public void setSelectedligneServiceWebExterne(TaLigneServiceWebExterne selectedligneServiceWebExterne) {
		this.selectedligneServiceWebExterne = selectedligneServiceWebExterne;
	}


	public List<TaLigneServiceWebExterneDTO> getLignesServiceWebExterneDTO() {
		return lignesServiceWebExterneDTO;
	}


	public void setLignesServiceWebExterneDTO(List<TaLigneServiceWebExterneDTO> lignesServiceWebExterneDTO) {
		this.lignesServiceWebExterneDTO = lignesServiceWebExterneDTO;
	}


	public List<TaLigneServiceWebExterneDTO> getSelectedlignesServiceWebExterneDTO() {
		return selectedlignesServiceWebExterneDTO;
	}


	public void setSelectedlignesServiceWebExterneDTO(List<TaLigneServiceWebExterneDTO> selectedlignesServiceWebExterneDTO) {
		this.selectedlignesServiceWebExterneDTO = selectedlignesServiceWebExterneDTO;
	}


	public List<TaLigneServiceWebExterneDTOJSF> getLignesDTOJSF() {
		return lignesDTOJSF;
	}


	public void setLignesDTOJSF(List<TaLigneServiceWebExterneDTOJSF> lignesDTOJSF) {
		this.lignesDTOJSF = lignesDTOJSF;
	}


	public List<TaLigneServiceWebExterneDTOJSF> getSelectedlignesDTOJSF() {
		return selectedlignesDTOJSF;
	}


	public void setSelectedlignesDTOJSF(List<TaLigneServiceWebExterneDTOJSF> selectedlignesDTOJSF) {
		this.selectedlignesDTOJSF = selectedlignesDTOJSF;
	}


	public TaLigneServiceWebExterneDTOJSF getSelectedLigneDTOJSF() {
		return selectedLigneDTOJSF;
	}


	public void setSelectedLigneDTOJSF(TaLigneServiceWebExterneDTOJSF selectedLigneDTOJSF) {
		this.selectedLigneDTOJSF = selectedLigneDTOJSF;
	}


	public List<TaLigneServiceWebExterneDTO> getLignesServiceWebExterneDTOTermine() {
		return lignesServiceWebExterneDTOTermine;
	}


	public void setLignesServiceWebExterneDTOTermine(List<TaLigneServiceWebExterneDTO> lignesServiceWebExterneDTOTermine) {
		this.lignesServiceWebExterneDTOTermine = lignesServiceWebExterneDTOTermine;
	}


	public List<SelectItem> getListeParamCreeDoc() {
		return listeParamCreeDoc;
	}


	public void setListeParamCreeDoc(List<SelectItem> listeParamCreeDoc) {
		this.listeParamCreeDoc = listeParamCreeDoc;
	}


	public String getSelectionParamCreeDoc() {
		return selectionParamCreeDoc;
	}


	public void setSelectionParamCreeDoc(String selectionParamCreeDoc) {
		this.selectionParamCreeDoc = selectionParamCreeDoc;
	}


	public boolean isFusionArticle() {
		return fusionArticle;
	}


	public void setFusionArticle(boolean fusionArticle) {
		this.fusionArticle = fusionArticle;
	}


	public boolean isRegleDoc() {
		return regleDoc;
	}


	public void setRegleDoc(boolean regleDoc) {
		this.regleDoc = regleDoc;
	}


	public List<ArticleLotEntrepotDTO> getListArticleLotEntrepot() {
		return listArticleLotEntrepot;
	}


	public void setListArticleLotEntrepot(List<ArticleLotEntrepotDTO> listArticleLotEntrepot) {
		this.listArticleLotEntrepot = listArticleLotEntrepot;
	}


	public TaLigneServiceWebExterneDTOJSF getDetailLigneOverLayPanel() {
		return detailLigneOverLayPanel;
	}


	public void setDetailLigneOverLayPanel(TaLigneServiceWebExterneDTOJSF detailLigneOverLayPanel) {
		this.detailLigneOverLayPanel = detailLigneOverLayPanel;
	}


	public TreeNode getTreeNodeLignesDTOJSF() {
		return treeNodeLignesDTOJSF;
	}


	public void setTreeNodeLignesDTOJSF(TreeNode treeNodeLignesDTOJSF) {
		this.treeNodeLignesDTOJSF = treeNodeLignesDTOJSF;
	}


	public TreeNode[] getSelectedNodesLignesDTOJSF() {
		return selectedNodesLignesDTOJSF;
	}


	public void setSelectedNodesLignesDTOJSF(TreeNode[] selectedNodesLignesDTOJSF) {
		this.selectedNodesLignesDTOJSF = selectedNodesLignesDTOJSF;
	}


	public TreeNode getSelectedTreeNodeLignesDTOJSF() {
		return selectedTreeNodeLignesDTOJSF;
	}


	public void setSelectedTreeNodeLignesDTOJSF(TreeNode selectedTreeNodeLignesDTOJSF) {
		this.selectedTreeNodeLignesDTOJSF = selectedTreeNodeLignesDTOJSF;
	}


	public String getTypeEntite() {
		return typeEntite;
	}


	public void setTypeEntite(String typeEntite) {
		this.typeEntite = typeEntite;
	}


	public Date getDerniereSynchro() {
		return derniereSynchro;
	}


	public void setDerniereSynchro(Date derniereSynchro) {
		this.derniereSynchro = derniereSynchro;
	}

	public StreamedContent getExportationLigneServiceExterne() {
		File f = null;
		InputStream stream = null;
		List<TaLigneServiceWebExterneDTO> values = new ArrayList<TaLigneServiceWebExterneDTO>();
		
		for (TreeNode node : treeNodeLignesDTOJSF.getChildren()) {
			TaLigneServiceWebExterneDTOJSF ligneJSF= (TaLigneServiceWebExterneDTOJSF) node.getData();
			TaLigneServiceWebExterneDTO dto = ligneJSF.getDto();

			if(dto.getEnteteDoc()) {			
				for (TreeNode ligneNode : node.getChildren()) {
					TaLigneServiceWebExterneDTOJSF ligneJSFChildren = (TaLigneServiceWebExterneDTOJSF) ligneNode.getData();
					TaLigneServiceWebExterneDTO dtoChildren = ligneJSFChildren.getDto();
					values.add(dtoChildren);
				}
				
			}
		}
		
		try {	
			f = ligneServiceWebExterneService.exportToCSV(values);
			stream = new FileInputStream(f); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		exportationLigneServiceExterne = new DefaultStreamedContent(stream,null,"ligne_service_externe.csv");
		
		return exportationLigneServiceExterne;
	}


	public void setExportationLigneServiceExterne(StreamedContent exportationLigneServiceExterne) {
		this.exportationLigneServiceExterne = exportationLigneServiceExterne;
	}
	
	
	

}

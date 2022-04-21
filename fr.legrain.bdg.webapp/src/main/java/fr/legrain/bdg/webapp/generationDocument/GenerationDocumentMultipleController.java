package fr.legrain.bdg.webapp.generationDocument;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import fr.legrain.bdg.documents.service.remote.IModelRecupDocumentCreationDocServiceRemote;
import fr.legrain.bdg.documents.service.remote.IModelTiersCreationDocServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAbonnementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaApporteurServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvisEcheanceServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaAvoirServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBoncdeServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonlivServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaDevisServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaFactureServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPanierServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPreparationServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.dossierIntelligent.service.remote.ITaParamDossierIntelligentServiceRemote;
import fr.legrain.bdg.dossierIntelligent.service.remote.ITaTypeDonneeServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamCreeDocTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.document.dto.CreationDocLigneDTO;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.model.ImageLgr;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaAbonnement;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaCreationDoc;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaPanier;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaPreparation;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.dossier.model.TaPreferences;
import fr.legrain.dossierIntelligent.model.TaParamDossierIntel;
import fr.legrain.dossierIntelligent.model.TaRParamDossierIntel;
import fr.legrain.dossierIntelligent.model.TaTypeDonnee;
import fr.legrain.generation.service.CreationDocumentMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaAdresse;
import fr.legrain.tiers.model.TaEntreprise;
import fr.legrain.tiers.model.TaFamilleTiers;
import fr.legrain.tiers.model.TaParamCreeDocTiers;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped 
public class GenerationDocumentMultipleController extends AbstractController{

	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisation;

	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

	@EJB private CreationDocumentMultiple creation;	

	private ParamAfficheChoixGenerationDocument result;


	private final String C_MESS_SELECTION_VIDE = "Aucun document n'a été sélectionné !!!";
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	private IDocumentTiers detailLigne;
	private TaTiers detailLigneTiers;
	private Date dateDeb=null;
	private Date dateFin=null;
	//	private TaTiers tiers = null;
	private String libelle="";
	private List<TaTiers>listeTiers = null;

	@EJB private ITaTiersServiceRemote daoTiers;
	@EJB private ITaFactureServiceRemote daoFacture;
	@EJB private ITaAvoirServiceRemote daoAvoir;
	@EJB private ITaBonlivServiceRemote daoBonLiv;
	@EJB private ITaBoncdeServiceRemote daoBoncde;
	@EJB private ITaBoncdeAchatServiceRemote daoBoncdeAchat;
	@EJB private ITaApporteurServiceRemote daoApporteur;
	@EJB private ITaProformaServiceRemote daoProforma;
	@EJB private ITaDevisServiceRemote daoDevis;
	@EJB private ITaPrelevementServiceRemote daoPrelevement;
	@EJB private ITaAcompteServiceRemote daoAcompte;
	@EJB private ITaTicketDeCaisseServiceRemote daoTicketDeCaisse;
	@EJB private ITaAvisEcheanceServiceRemote daoAvisEcheance;
	@EJB private ITaPreparationServiceRemote daoPreparation;
	@EJB private ITaAbonnementServiceRemote daoAbonnement;
	@EJB private ITaBonReceptionServiceRemote daoBonReception;
	@EJB private ITaPanierServiceRemote daoPanier;
	@EJB private ITaPreferencesServiceRemote PreferencesService;
	@EJB private ITaInfoEntrepriseServiceRemote daoInfos;
	@EJB private ITaTPaiementServiceRemote daoTPaiement ;
	@EJB private ITaParamCreeDocTiersServiceRemote daoParamCreeDocTiers;
	@EJB private ITaParamDossierIntelligentServiceRemote taParamDao;
	@EJB private ITaTypeDonneeServiceRemote daoType;
	
	

	private TaBonliv taBonLiv = null;
	private IDocumentTiers document = null;
	private String codeDocument="";

	TaInfoEntreprise infos =null;

	private List<String> listeTypePaiement=new LinkedList<String>();
	private String selectedTypePaiement = "";
	private String oldSelectedTypePaiement = "";

	private List<String> listeChamps=new LinkedList<String>();
	private String selectedChamps = "";
	private String oldSelectedChamps = "";

	private List<String> listeMots=new LinkedList<String>();
	private String selectedMots = "";
	private String oldSelectedMots = "";

//	private List<String> listeTypeDocSelection=new LinkedList<String>();
//	private String selectedTypeSelection = "";
//	private String oldSelectedTypeSelection = "";
	private List<ImageLgr> listeTypeDocSelection=new LinkedList<ImageLgr>();
	private ImageLgr selectedTypeSelection ;
	private ImageLgr oldSelectedTypeSelection=new ImageLgr() ;
	
	private List<ImageLgr> listeTypeDocCreation=new LinkedList<ImageLgr>();
	private ImageLgr selectedTypeCreation ;
	private ImageLgr oldSelectedTypeCreation =new ImageLgr() ;

	private String pathImageSelectionDocSelection=TypeDoc.PATH_IMAGE_ROUE_DENTEE;
	private String pathImageSelectionDocCreation=TypeDoc.PATH_IMAGE_ROUE_DENTEE;
	
	private TreeNode root=new DefaultTreeNode("", null);
    private TreeNode[] selectedNode;
    
	@EJB private IModelRecupDocumentCreationDocServiceRemote modelDocument ;
	private List<CreationDocLigneDTO> listeDocumentDTO;
	private CreationDocLigneDTO[] selectedDocuments;
	
	@EJB private IModelTiersCreationDocServiceRemote modelTiers ;
	private LinkedList<TaTiersDTO> listeTiersDTO;
	private TaTiersDTO[] selectedTierses ;	
	private TaTiersDTO selectedCritere ;

	private Map<String,String> listeCorrespondanceTiers=null;
	private Map<String,String> listeTitreChampsTiers=null;
	private List<String> listeRequeteTiers=null;
	private Map<String,Object> listeObjetTiers=null;


	private TaTypeDonnee typeDonnee=null;
	private String sql=null;
	private String champsTiers=null;
	private String mot=null;
	private String valeurCritere="";
	private String valeurCritere2="";
	private Class classNameSelectionTiers=null;

	private TaTPaiement taTPaiement=null;
//	private TypeDoc typeDocPresent;

	private List<SelectItem> listeParamCreeDocTiers;
	private String selectionParamCreeDocTiers=C_PARAM_TIERS;

	public static final String C_PARAM_TIERS = "par tiers";
	public static final String C_PARAM_DOCUMENT = "par document";
	public static final String C_PARAM_SEMAINE = "par semaine";
	public static final String C_PARAM_DEUX_SEMAINE = "par quinzaine";
	public static final String C_PARAM_MOIS = "par mois de document";
	public static final String C_PARAM_DECADE = "par decade";
	public static final String C_PARAM_XJOURS = "pour x jour(s) de document";

	private Integer nbJours=1;
	private boolean appliquerATous=true;

	private Integer prefNbDecimalesPrix;
	private Integer prefNbDecimalesQte;

	@PostConstruct
	public void init() {

		resetTous();
		infos=daoInfos.findInstance();
		
		dateDeb=infos.getDatedebInfoEntreprise();
		dateFin=infos.getDatefinInfoEntreprise();

		TaPreferences nb;
		nb = PreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_PRIX);
		if(nb!=null && nb.getValeur() != null) {
			prefNbDecimalesPrix=LibConversion.stringToInteger(nb.getValeur());
		}

//		nb = PreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_NB_DECIMALES, ITaPreferencesServiceRemote.NB_DECIMALES_QTE);
//		if(nb!=null && nb.getValeur() != null) {
//			prefNbDecimalesQte=LibConversion.stringToInteger(nb.getValeur());
//		}
		
		listeParamCreeDocTiers = new ArrayList<SelectItem>();
		listeParamCreeDocTiers.add(new SelectItem(C_PARAM_TIERS, C_PARAM_TIERS));
		listeParamCreeDocTiers.add(new SelectItem(C_PARAM_DOCUMENT, C_PARAM_DOCUMENT));
		listeParamCreeDocTiers.add(new SelectItem(C_PARAM_SEMAINE, C_PARAM_SEMAINE));
		listeParamCreeDocTiers.add(new SelectItem(C_PARAM_DEUX_SEMAINE, C_PARAM_DEUX_SEMAINE));
		listeParamCreeDocTiers.add(new SelectItem(C_PARAM_MOIS, C_PARAM_MOIS));
		listeParamCreeDocTiers.add(new SelectItem(C_PARAM_DECADE, C_PARAM_DECADE));
		listeParamCreeDocTiers.add(new SelectItem(C_PARAM_XJOURS, C_PARAM_XJOURS));

		//remplir la liste des type de documents que l'on peut selectionner
		listeTypeDocSelection.clear();

		for (String type : autorisation.getTypeDocCompletPresent().values()) {
			if(!type.equals(TypeDoc.TYPE_REGLEMENT)&&!type.equals(TypeDoc.TYPE_REMISE)&&!type.equals(TypeDoc.TYPE_ACOMPTE)){				
				listeTypeDocSelection.add(TypeDoc.getInstance().getPathImageCouleurDoc().get(type));
			}
		}

		int rang=0;
		rang=(listeTypeDocSelection.indexOf(TypeDoc.getInstance().getPathImageCouleurDoc().get(TypeDoc.TYPE_BON_LIVRAISON)));
		if(rang==-1)rang=0;
		selectedTypeSelection=listeTypeDocSelection.get(rang);
		
		selectionComboSelection(null);

		
		//traiter critères Tiers
		listeCorrespondanceTiers=new LinkedHashMap<String, String>();
		listeTitreChampsTiers=new LinkedHashMap<String, String>();
		listeObjetTiers=new LinkedHashMap<String,Object>();
		listeRequeteTiers=new ArrayList<String>();


		listeTitreChampsTiers.put("code Tiers","codeTiers");
		listeTitreChampsTiers.put("Nom","nomTiers");
		listeTitreChampsTiers.put("prenom","prenomTiers");
		listeTitreChampsTiers.put("code postal","codepostalAdresse");
		listeTitreChampsTiers.put("ville","villeAdresse");
		listeTitreChampsTiers.put("nom de l'entreprise","nomEntreprise");
		listeTitreChampsTiers.put("Famille tiers","FamilleTiers");
		//				listeTitreChampsTiers.put("Note tiers","NoteTiers");
		//				listeTitreChampsTiers.put("Type de notes","TnoteTiers");

		TaTiers tiers =new TaTiers();
		TaAdresse adresse=new TaAdresse();
		listeObjetTiers.put("codeTiers",tiers);
		listeObjetTiers.put("nomTiers",tiers);
		listeObjetTiers.put("prenomTiers",tiers);
		listeObjetTiers.put("codepostalAdresse",adresse);
		listeObjetTiers.put("villeAdresse",adresse);
		listeObjetTiers.put("nomEntreprise",new TaEntreprise());
		listeObjetTiers.put("FamilleTiers",new TaFamilleTiers());
		//				listeObjetTiers.put("NoteTiers",new TaNoteTiers());
		//				listeObjetTiers.put("TnoteTiers",new TaTNoteTiers());

		listeCorrespondanceTiers.put("codeTiers","a.codeTiers");
		listeCorrespondanceTiers.put("nomTiers","a.nomTiers");
		listeCorrespondanceTiers.put("prenomTiers","a.prenomTiers");
		listeCorrespondanceTiers.put("codepostalAdresse","adr.codepostalAdresse");
		listeCorrespondanceTiers.put("villeAdresse","adr.villeAdresse");
		listeCorrespondanceTiers.put("nomEntreprise","Ent.nomEntreprise");
		listeCorrespondanceTiers.put("FamilleTiers","ft.codeFamille");
		//				listeCorrespondanceTiers.put("NoteTiers","nt.noteTiers");
		//				listeCorrespondanceTiers.put("TnoteTiers","tnt.codeTNoteTiers");

		listeRequeteTiers.add("select a from fr.legrain.tiers.model.TaTiers a ");
		listeRequeteTiers.add("left join a.taAdresse adr ");
		listeRequeteTiers.add("left join a.taEntreprise Ent ");
		listeRequeteTiers.add("left join a.taFamilleTierses ft ");
		listeRequeteTiers.add("left join a.taTTiers tt ");
		//				listeRequeteTiers.add("left join a.taNotes nt ");
		//				listeRequeteTiers.add("left join nt.taTNoteTiers tnt ");
		//dernière ligne
		listeRequeteTiers.add("where tt.idTTiers <>-1");

		listeChamps.clear();
		for (String champs : listeTitreChampsTiers.keySet()) {
			listeChamps.add(champs);
		}
		if(!listeChamps.isEmpty())selectedChamps=listeChamps.get(0);
		selectionChamp();

		listeTypePaiement.clear();
		listeTypePaiement.add("Tous");
		List<TaTPaiement> listePaiement =null;
		listePaiement=daoTPaiement.selectAll();
		for (TaTPaiement taTPaiement : listePaiement) {
			listeTypePaiement.add(taTPaiement.getCodeTPaiement());
		}
		selectedTypePaiement=listeTypePaiement.get(0);

	}


	public void actFermer(ActionEvent e) {

	}
	public void actAnnuler(ActionEvent e) {

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
		paramGeneration.setTypeSrc(selectedTypeSelection.getName());
		paramGeneration.setTypeDest(selectedTypeCreation.getName());
//		String libelle="Reprise de ";
		paramGeneration.setDateDocument(new Date());
		paramGeneration.setDateLivraison(new Date());
		paramGeneration.setLibelle(initialiseLibelleDoc(selectedTypeSelection.getName())
				+" du "+LibDate.dateToString(dateDeb)+" au "+
				LibDate.dateToString(dateFin));
		paramGeneration.setTiersModifiable(false);
		paramGeneration.setMultiple(true);
		paramGeneration.setConserveNbDecimalesDoc(false);

		paramGeneration.setTitreFormulaire("Génération multiple");
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}
	
	public void handleReturnDialogGenerationDocument(SelectEvent event) {

		FacesContext context = FacesContext.getCurrentInstance();
		if(selectedNode.length>0){
			if(event!=null && event.getObject()!=null) {
				try {
					if(event.getObject() instanceof ParamAfficheChoixGenerationDocument){
						ParamAfficheChoixGenerationDocument param =  (ParamAfficheChoixGenerationDocument) event.getObject();
						if(param.getRetour()){
							String finDeLigne = "\r\n";
							//		List<IHMEnteteDocument> listeTriee=new LinkedList<IHMEnteteDocument>();
							List<IDocumentTiers> listeDocCrees=new LinkedList<IDocumentTiers>();


							param.getListeDocumentSrc().clear();
							//				param.setDateDocument(param.getDateDocument());
							param.setDateLivraison(param.getDateDocument());
							param.setGenerationLigne(false);
							//				for (TaCreationDoc elem : modelDocument.getModelCreationDoc().getListeEntity()) {
							List<IDocumentTiers> listeDoc = new LinkedList<>();
							String tiersCourant="";
							String codeFacticeCourant="";
							boolean changer=false;
							int i=0;
							while(tiersCourant.isEmpty() && i<=selectedNode.length){
								TreeNode node=selectedNode[i];
								tiersCourant=((CreationDocLigneDTO) node.getData()).getCodeTiersParent();
								i++;
							}
							for (TreeNode node : selectedNode) {
								if(((CreationDocLigneDTO) node.getData()).isLigneFinale()){
									if((!tiersCourant.isEmpty() && !((CreationDocLigneDTO) node.getData()).getCodeTiersParent().equals(tiersCourant)) ||(
											!codeFacticeCourant.isEmpty() && !((CreationDocLigneDTO) node.getData()).getCodeDocument().equals(codeFacticeCourant))){
										changer=true;
									}
									//changement de tiers
									if(listeDoc!=null && !listeDoc.isEmpty()&& changer){
										for (IDocumentTiers doc : listeDoc) {
											if(doc.getAccepte()){
												param.getListeDocumentSrc().add(doc);
												//									param.setDateDocument(paramTmp.getDateDocument());
												param.setDateLivraison(param.getDateDocument());
												//libelle+=" - "+doc.getCodeDocument();
											}
										}
										//creation document
										if(param.getListeDocumentSrc().size()!=0){
											RetourGenerationDoc retour=null; 
											//param.setLibelle(libelle);
											creation.setParam(param);
											retour=creation.creationDocument(true);
											if(retour!=null && retour.isRetour()){
												listeDocCrees.add(retour.getDoc());
											}
										}
										param.getListeDocumentSrc().clear();
										changer=false;
										listeDoc.clear();
									}
									tiersCourant=((CreationDocLigneDTO) node.getData()).getCodeTiersParent();
									codeFacticeCourant=((CreationDocLigneDTO) node.getData()).getCodeDocument();
									listeDoc.add(rechercheDocumentNode(node));
								}
							}
							//traiter dernière ligne
							RetourGenerationDoc retour=null; 
							if(listeDoc!=null && !listeDoc.isEmpty()){
								for (IDocumentTiers doc : listeDoc) {
									if(doc.getAccepte()){
										param.getListeDocumentSrc().add(doc);
										//									param.setDateDocument(paramTmp.getDateDocument());
										param.setDateLivraison(param.getDateDocument());
										//libelle+=" - "+doc.getCodeDocument();
									}
								}
								//creation document

								if(param.getListeDocumentSrc().size()!=0){

									//param.setLibelle(libelle);
									creation.setCodeTiers(null);
									creation.setParam(param);
									retour=creation.creationDocument(true);
									if(retour!=null && retour.isRetour()){
										listeDocCrees.add(retour.getDoc());
									}
								}
							}
							param.setDateDocument(new Date());
							param.setDateLivraison(new Date());


							String message="";
							for (IDocumentTiers iDocumentTiers : listeDocCrees) {
								message+=iDocumentTiers.getCodeDocument()+finDeLigne;
								LgrTab typeDocPresent = LgrTab.getInstance();
								String tab = typeDocPresent.getTabDocCorrespondance().get(param.getTypeDest());
								retour.setTypeTabCSS(tab);
								retour.setOuvrirDoc(param.isOuvrir());
								//						FacesContext context = FacesContext.getCurrentInstance();  
								//						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Création document", "Le document '"+listeDocCrees.get(0).getCodeDocument()+"' à été généré correctement. Voulez-vous le visualiser ?"));
								{
									if(retour.isRetour() && retour.getDoc()!=null) {
										if(retour.getDoc() instanceof TaBoncdeAchat) {
											retour.setDoc(daoBoncdeAchat.findByIDFetch(retour.getDoc().getIdDocument()));
										}
										if(retour.getDoc() instanceof TaFacture) {
											retour.setDoc(daoFacture.findByIDFetch(retour.getDoc().getIdDocument()));
										}
										if(retour.getDoc() instanceof TaDevis) {
											retour.setDoc(daoDevis.findByIDFetch(retour.getDoc().getIdDocument()));
										}
										if(retour.getDoc() instanceof TaPanier) {
											retour.setDoc(daoPanier.findByIDFetch(retour.getDoc().getIdDocument()));
										}
										if(retour.getDoc() instanceof TaAvisEcheance) {

											retour.setDoc(daoAvisEcheance.findByIDFetch(retour.getDoc().getIdDocument()));

											if(retour.getDoc() instanceof TaAvoir) {
												retour.setDoc(daoAvoir.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaAcompte) {
												retour.setDoc(daoAcompte.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaApporteur) {
												retour.setDoc(daoApporteur.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaAbonnement) {
												retour.setDoc(daoAbonnement.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaBonReception) {
												retour.setDoc(daoBonReception.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaBoncde) {
												retour.setDoc(daoBoncde.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaBonliv) {
												retour.setDoc(daoBonLiv.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaPrelevement) {
												retour.setDoc(daoPrelevement.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaPreparation) {
												retour.setDoc(daoPreparation.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaProforma) {
												retour.setDoc(daoProforma.findByIDFetch(retour.getDoc().getIdDocument()));
											}
											if(retour.getDoc() instanceof TaTicketDeCaisse) {
												retour.setDoc(daoTicketDeCaisse.findByIDFetch(retour.getDoc().getIdDocument()));
											}					
										}
										ouvertureDocument(retour);
									}						
								}
								if (!message.equals("")){
									context.addMessage(null, new FacesMessage("Liste des documents créés", message)); 	

									//						MessageDialog.openInformation(vue.getShell(), "Liste des documents créés", message);
								}

							}


						}

					}
					resetTous();
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide",C_MESS_SELECTION_VIDE)); 	
		}


	
		
	}
	public void ouvertureDocument(RetourGenerationDoc retour ) {
		String css = null;
		IDocumentTiers docCree = null; 
		LgrTabEvent retourEvent;
		FacesContext context = FacesContext.getCurrentInstance();
		if(retour!=null) {
//			if(retour.getMessage()!=null && !retour.getMessage().isEmpty())
//				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Création document", retour.getMessage()));
			if(retour.isOuvrirDoc())ouvertureDocumentBean.detailDocument(retour.getDoc());

		}
	}

	
//	
	
	public void actEnregistrer(ActionEvent e) {
		FacesContext context = FacesContext.getCurrentInstance();
		//        paSelectionLigneDocControlleur.actEnregistrer();
				//créer le document à partir des documents selectionné dans le modelDocument
				final  ParamAfficheChoixGenerationDocument paramTmp = new ParamAfficheChoixGenerationDocument();
				ParamAfficheChoixGenerationDocument param = new ParamAfficheChoixGenerationDocument();
//				selectionComboSelection(null);
				String finDeLigne = "\r\n";
				param.setTypeSrc(selectedTypeSelection.getName());
				param.setTypeDest(selectedTypeCreation.getName());
				String libelle="Reprise de ";
//				paramTmp.setDateDocument(new Date());
//				paramTmp.setDateLivraison(new Date());
				
				
				paramTmp.setLibelle(initialiseLibelleDoc(selectedTypeSelection.getName())
						+" du "+LibDate.dateToString(dateDeb)+" au "+
						LibDate.dateToString(dateFin));
		if(selectedNode.length>0)
			actDialogGenerationDocument(null);
		else context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide", C_MESS_SELECTION_VIDE)); 

	}

	public void actRefresh(ActionEvent e) {
		actRefresh();
	}



public void actChangeDocSelection() {
		resetTous();
		if(selectedTypeSelection!=null){
			pathImageSelectionDocSelection=selectedTypeSelection.getDisplayName();
		}
		selectionComboSelection(null);
		if(selectedTypeCreation.equals("")){
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Aucune correspondance pour la création", "Ce type de document ne peut générer aucun autre type de document.")); 	
		}
}

	public void actRefresh() {
		try{

			resetTous();
			selectionComboSelection(null);
			if(selectedTypeCreation.equals("")){
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Aucune correspondance pour la création", "Ce type de document ne peut générer aucun autre type de document.")); 	
				throw new Exception();
			}


			final String filtre=creeFiltre();
			taTPaiement=null;
			if(selectedTypePaiement!=null && !selectedTypePaiement.isEmpty()){
				taTPaiement=daoTPaiement.findByCode(selectedTypePaiement);
			}
			document=null;
			if(codeDocument!=null && !codeDocument.isEmpty()){
				document=documentValide(codeDocument);
			}

			listeTiersDTO= modelTiers.remplirListe(dateDeb,
					dateFin,
					selectedTypeSelection.getName(),selectedTypeCreation.getName(),filtre,document,taTPaiement);
			selectedTierses= rempliSelection(listeTiersDTO);


		}catch (Exception e1) {

		}
		finally{

		}
	}


	
	public TaTiersDTO[] rempliSelection(List<TaTiersDTO> liste){
		List<TaTiersDTO> tmp = new LinkedList<>();
		for (TaTiersDTO taTiersDTO : liste) {
			if(taTiersDTO.getAccepte())tmp.add(taTiersDTO);
		}
		return selectedTierses= Arrays.copyOf(tmp.toArray(),tmp.size(),TaTiersDTO[].class);		
	}
	public List<TaTiersDTO> modifListeTiersSurSelection(TaTiersDTO[] tab){
		for (TaTiersDTO obj : listeTiersDTO) {
			obj.setAccepte(false);
		}
		for (TaTiersDTO taTiersDTO : tab) {
			taTiersDTO.setAccepte(true);
//			int rang=listeTiersDTO.indexOf(taTiersDTO);
//			if(rang>0)listeTiersDTO.get(rang).setAccepte(true);
		}
		return listeTiersDTO;
	}
	
	public void resetTous() {
		try{
			//récupérer la liste des documents associable au type de document à créer
			//ces documents ne doivent pas déjà avoir de relation dans rDocument par rapport au
			//type document à créer.
			modelTiers.getListeObjet().clear();

			modelDocument.getListeObjet().clear();
			modelDocument.getModelCreationDoc().getListeObjet().clear();

			listeTiersDTO.clear();
			selectedTierses=rempliSelection(listeTiersDTO);
			listeDocumentDTO.clear();
			root=new DefaultTreeNode();
			selectedNode=null;
			resetDoc();
		}catch (Exception e) {

		}
		finally{
		}
	}
	public void resetDoc() {
		modelDocument.getListeObjet().clear();
		modelDocument.getModelCreationDoc().getListeObjet().clear();
		listeDocumentDTO=modelDocument.getModelCreationDoc().getListeObjet();
	}









	public Date getDateDeb() {
		return dateDeb;
	}

	public void setDateDeb(Date dateDeb) {
		this.dateDeb = dateDeb;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}



	private IDocumentTiers documentValide(String code){
		try {	
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_FACTURE)) {
				document=daoFacture.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_APPORTEUR)) {
				document=daoApporteur.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_AVOIR)) {
				document=daoAvoir.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_BON_COMMANDE)) {
				document=daoBoncde.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
				document=daoBoncdeAchat.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_BON_LIVRAISON)) {
				document=daoBonLiv.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_DEVIS)) {
				document=daoDevis.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_PROFORMA)) {
				document=daoProforma.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_ACOMPTE)) {
				document=daoAcompte.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_PRELEVEMENT)) {
				document=daoPrelevement.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_AVIS_ECHEANCE)) {
				document=daoAvisEcheance.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_TICKET_DE_CAISSE)) {
				document=daoTicketDeCaisse.findByCodeFetch(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_PREPARATION)) {
				document=daoPreparation.findByCodeFetch(code);
			}	
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_PANIER)) {
				document=daoPanier.findByCodeFetch(code);
			}			
			return document;
		} catch (FinderException e) {
			return null;
		}
	}


	public ImageLgr getSelectedTypeSelection() {
		return selectedTypeSelection;
	}

	public void setSelectedTypeSelection(ImageLgr selectedType) {
		this.selectedTypeSelection = selectedType;
	}

	public boolean selectionComboSelection(SelectEvent event) {
		boolean changement = false;
		try{
			TypeDoc.getInstance();
			pathImageSelectionDocSelection="";
			if(selectedTypeSelection!=null){
				pathImageSelectionDocSelection=selectedTypeSelection.getDisplayName();
			}
			if(!oldSelectedTypeSelection.equals(selectedTypeSelection)){
				resetTous();
				changement=true;
				listeTypeDocCreation.clear();
				for (int i = 0; i < TypeDoc.getInstance().getEditorDocPossibleCreationDocument().size(); i++) {
					if(TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[0].equalsIgnoreCase(selectedTypeSelection.getName())){
						String cle=TypeDoc.getInstance().getEditorDocPossibleCreationDocument().get(i)[1];
						listeTypeDocCreation.add(TypeDoc.getInstance().getPathImageCouleurDoc().get(cle));
					}
					if(!listeTypeDocCreation.isEmpty()){
						selectedTypeCreation=listeTypeDocCreation.get(0);
					}
				}
				selectionComboCreation(null);
			}
			oldSelectedTypeSelection=selectedTypeSelection;

		}catch (Exception e) {
			return false;
		}
		return changement;
	}

	public void actSelectionDocCreation(){
		selectionComboCreation(null);
	}
	
	public void selectionComboCreation(SelectEvent event) {
		pathImageSelectionDocCreation="";
		if(!oldSelectedTypeCreation.equals(selectedTypeCreation)){
				resetDoc();
				if(selectedTypeCreation!=null){
					pathImageSelectionDocCreation=selectedTypeCreation.getDisplayName();
				}
		}
		oldSelectedTypeCreation=selectedTypeCreation;
	}


	public ImageLgr getSelectedTypeCreation() {
		return selectedTypeCreation;
	}

	public void setSelectedTypeCreation(ImageLgr selectedTypeCreation) {
		this.selectedTypeCreation = selectedTypeCreation;
	}


	public class TaDocComparator implements Comparator<IDocumentDTO> {

		public int compare(IDocumentDTO doc1, IDocumentDTO doc2) {

			int premier = doc1.getCodeTiers().compareTo(doc2.getCodeTiers());

			int deuxieme = doc1.getDateDocument().compareTo(doc2.getDateDocument());

			int compared = premier;
			if (compared == 0) {
				compared = deuxieme;
			}

			return compared;
		}
	}




	public TaParamCreeDocTiers rempliParamGeneral(){
		TaParamCreeDocTiers paramGen = new TaParamCreeDocTiers();
		switch (selectionParamCreeDocTiers) {
		case C_PARAM_TIERS:paramGen.setTiers(1);			
		break;
		case C_PARAM_DOCUMENT:paramGen.setDocument(1);			
		break;
		case C_PARAM_SEMAINE:paramGen.setSemaine(1);			
		break;
		case C_PARAM_DEUX_SEMAINE:paramGen.setDeuxSemaines(1);			
		break;
		case C_PARAM_MOIS:paramGen.setMois(1);			
		break;
		case C_PARAM_DECADE:paramGen.setDecade(1);			
		break;
		case C_PARAM_XJOURS:paramGen.setxJours(1);			
		break;

		default:
			break;
		}
		paramGen.setNbJours(nbJours);
		paramGen.setAppliquerATous(appliquerATous);
		paramGen.setTaTiers(null);
		return paramGen;
	}



	public void selectionChamp() {
		String[] tab= new String[]{};
		String champPropertie;

		champsTiers=listeTitreChampsTiers.get(selectedChamps);
		champPropertie=listeCorrespondanceTiers.get(champsTiers);
		tab=champPropertie.split("\\.");
		if(tab.length>0)
			champPropertie=tab[tab.length-1];

		classNameSelectionTiers =renvoiTypeChampsTiers(champPropertie,listeObjetTiers.get(champsTiers));

		try {
			typeDonnee=daoType.findByCode(classNameSelectionTiers.getName());
		} catch (FinderException e) {

		}
		if(typeDonnee!=null){
			listeMots.clear();
			for (TaRParamDossierIntel rParam : typeDonnee.getTaRParamDossierInteles()) {
				listeMots.add(rParam.getTaParamDossierIntel().getMot());
			}
			int index=listeMots.indexOf("EST EGALE A");
			if(index==-1)index=0;
			if(!listeMots.isEmpty())selectedMots=listeMots.get(index);
			selectionMot();
		}
		champsTiers=listeCorrespondanceTiers.get(champsTiers);


		valeurCritere="";
		valeurCritere2="";
		selectionMot();

	}

	public void selectionTPaiement(){
		String code=selectedTypePaiement;
		if(code.equals("Tous")){
			taTPaiement=null;
		}else{
			try {
				taTPaiement=daoTPaiement.findByCode(code);
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		resetTous();
	}


	public void selectionMot(){
		mot="";
		//TODO 0 A gérer dans l'écran
		//		vue.getTfCritereTiers2().setEnabled(false);
		valeurCritere="";
		valeurCritere2="";
		mot=selectedMots;
		if(mot!=null && !mot.equals("")){
			TaParamDossierIntel taParamDossierIntel = null;
			try {
				taParamDossierIntel = taParamDao.findByCode(mot);
			} catch (FinderException e) {

			}
			if(taParamDossierIntel!=null){
				sql=taParamDossierIntel.getSql();
				//TODO 0 A gérer dans l'écran
				//				listeMots.setToolTipText(sql);
				//TODO 0 A gérer dans l'écran
				//				vue.getTfCritereTiers2().setEnabled(taParamDossierIntel.getNbZones()>1);
			}
		}
		resetTous();
	}

	public Class renvoiTypeChampsTiers(String champs,Object objet){
		try {
			return PropertyUtils.getPropertyType(objet, champs);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String creeFiltre(){
		try {
			Boolean appliquer=false;
			String sqlSuppDeb="";
			String sqlSuppFin="";
			String sqlLoc="";
			String critere="";
			sqlLoc=sql;
			//champs=listeCorrespondanceTiers.get(champs);
			if(!valeurCritere2.equals("")){
				//				valeurCritere=vue.getTfCritereTiers().getText();
				//				valeurCritere2=vue.getTfCritereTiers2().getText();			
				valeurCritere=valeurCritere.replace("*", "%");
				valeurCritere2=valeurCritere2.replace("*", "%");
				if(classNameSelectionTiers == Date.class){

					valeurCritere=LibDate.StringDateToStringSql(valeurCritere,new Date());

					valeurCritere2=LibDate.StringDateToStringSql(valeurCritere2,new Date());
					critere=champsTiers+" "+sqlLoc+" cast('"+valeurCritere+"' as date) and cast('"+valeurCritere2+"' as date) ";
				}else if(classNameSelectionTiers == String.class){
					critere="upper("+champsTiers+") "+sqlLoc+" upper('"+valeurCritere+"') and upper('"+valeurCritere2+"')";
				}else{
					critere=champsTiers+" "+sqlLoc+" "+valeurCritere+" and "+valeurCritere2;	
				}
			}
			else
				if(!valeurCritere.isEmpty()){
					valeurCritere=valeurCritere.replace("*", "%");
					if(classNameSelectionTiers == Date.class){
						valeurCritere=LibDate.StringDateToStringSql(valeurCritere,new Date());
						critere=champsTiers+" "+sqlLoc+" cast('"+valeurCritere+"' as date)";
					}else if(classNameSelectionTiers == String.class){					
						if(sqlLoc.contains("like")){
							valeurCritere=valeurCritere.replace("%", "");
							sqlSuppDeb="%";
							sqlSuppFin="%";
						}
						if(sqlLoc.contains("start with")){
							valeurCritere=valeurCritere.replace("%", "");
							sqlLoc="like";
							sqlSuppDeb="";
							sqlSuppFin="%";
						}
						if(sqlLoc.contains("finish with")){
							valeurCritere=valeurCritere.replace("%", "");
							sqlLoc="like";
							sqlSuppDeb="%";
							sqlSuppFin="";
						}
						critere="upper("+champsTiers+") "+sqlLoc+" upper('"+sqlSuppDeb+valeurCritere+sqlSuppFin+"')";
					}
					else{
						critere=champsTiers+" "+sqlLoc+" "+sqlSuppDeb+valeurCritere+sqlSuppFin;	
					}
				}

			String requete="";
			//"select a from TaTiers a left join a.taCommerciaux com where a.taTTiers.idTTiers <>-1 ";
			for (String req : listeRequeteTiers) {
				requete=requete+" "+req;
			}

			if(!critere.equals("")){
				requete+=" and "+critere;
			}
			return requete;
		} catch (java.text.ParseException e) {
		}
		return "";
	}



	public String initialiseLibelleDoc(String typeSelection){
		if(typeSelection.equals(TaBonliv.TYPE_DOC)){
			libelle="Reprise des BL ";
		}
		if(typeSelection.equals(TaBoncde.TYPE_DOC)){
			libelle="Reprise des Bcde ";
		}
		if(typeSelection.equals(TaFacture.TYPE_DOC)){
			libelle="Reprise des Factures ";
		}
		if(typeSelection.equals(TaAvoir.TYPE_DOC)){
			libelle="Reprise des Avoirs ";
		}
		if(typeSelection.equals(TaDevis.TYPE_DOC)){
			libelle="Reprise des Devis ";
		}
		if(typeSelection.equals(TaPrelevement.TYPE_DOC)){
			libelle="Reprise des Prel ";
		}
		if(typeSelection.equals(TaAcompte.TYPE_DOC)){
			libelle="Reprise des Acomptes ";
		}
		if(typeSelection.equals(TaProforma.TYPE_DOC)){
			libelle="Reprise des Prof. ";
		}
		if(typeSelection.equals(TaAvisEcheance.TYPE_DOC)){
			libelle="Reprise des Avis ";
		}	
		if(typeSelection.equals(TaTicketDeCaisse.TYPE_DOC)){
			libelle="Reprise des Tickets de caisse ";
		}
		if(typeSelection.equals(TaPreparation.TYPE_DOC)){
			libelle="Reprise des bons de préparation ";
		}
		if(typeSelection.equals(TaPanier.TYPE_DOC)){
			libelle="Reprise des paniers ";
		}
		return libelle;
	}

	public AutorisationBean getAutorisation() {
		return autorisation;
	}

	public void setAutorisation(AutorisationBean autorisation) {
		this.autorisation = autorisation;
	}

	public CreationDocumentMultiple getCreation() {
		return creation;
	}

	public void setCreation(CreationDocumentMultiple creation) {
		this.creation = creation;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public List<TaTiers> getListeTiers() {
		return listeTiers;
	}

	public void setListeTiers(List<TaTiers> listeTiers) {
		this.listeTiers = listeTiers;
	}

	public TaBonliv getTaBonLiv() {
		return taBonLiv;
	}

	public void setTaBonLiv(TaBonliv taBonLiv) {
		this.taBonLiv = taBonLiv;
	}

	public IDocumentTiers getDocument() {
		return document;
	}

	public void setDocument(IDocumentTiers document) {
		this.document = document;
	}





	public TaTiersDTO getSelectedCritere() {
		return selectedCritere;
	}

	public void setSelectedCritere(TaTiersDTO selectedCritere) {
		this.selectedCritere = selectedCritere;
	}

	public Map<String, String> getListeCorrespondanceTiers() {
		return listeCorrespondanceTiers;
	}

	public void setListeCorrespondanceTiers(Map<String, String> listeCorrespondanceTiers) {
		this.listeCorrespondanceTiers = listeCorrespondanceTiers;
	}

	public Map<String, String> getListeTitreChampsTiers() {
		return listeTitreChampsTiers;
	}

	public void setListeTitreChampsTiers(Map<String, String> listeTitreChampsTiers) {
		this.listeTitreChampsTiers = listeTitreChampsTiers;
	}

	public List<String> getListeRequeteTiers() {
		return listeRequeteTiers;
	}

	public void setListeRequeteTiers(List<String> listeRequeteTiers) {
		this.listeRequeteTiers = listeRequeteTiers;
	}

	public Map<String, Object> getListeObjetTiers() {
		return listeObjetTiers;
	}

	public void setListeObjetTiers(Map<String, Object> listeObjetTiers) {
		this.listeObjetTiers = listeObjetTiers;
	}

	public TaTypeDonnee getTypeDonnee() {
		return typeDonnee;
	}

	public void setTypeDonnee(TaTypeDonnee typeDonnee) {
		this.typeDonnee = typeDonnee;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getChampsTiers() {
		return champsTiers;
	}

	public void setChampsTiers(String champsTiers) {
		this.champsTiers = champsTiers;
	}

	public String getMot() {
		return mot;
	}

	public void setMot(String mot) {
		this.mot = mot;
	}

	public String getValeurCritere() {
		return valeurCritere;
	}

	public void setValeurCritere(String valeurCritere) {
		this.valeurCritere = valeurCritere;
	}

	public String getValeurCritere2() {
		return valeurCritere2;
	}

	public void setValeurCritere2(String valeurCritere2) {
		this.valeurCritere2 = valeurCritere2;
	}

	public TaTPaiement getTaTPaiement() {
		return taTPaiement;
	}

	public void setTaTPaiement(TaTPaiement taTPaiement) {
		this.taTPaiement = taTPaiement;
	}



	public List<SelectItem> getListeParamCreeDocTiers() {
		return listeParamCreeDocTiers;
	}

	public void setListeParamCreeDocTiers(List<SelectItem> listeParamCreeDocTiers) {
		this.listeParamCreeDocTiers = listeParamCreeDocTiers;
	}

	public String getSelectionParamCreeDocTiers() {
		return selectionParamCreeDocTiers;
	}

	public void setSelectionParamCreeDocTiers(String selectionParamCreeDocTiers) {
		this.selectionParamCreeDocTiers = selectionParamCreeDocTiers;
	}

	public Integer getNbJours() {
		return nbJours;
	}

	public void setNbJours(Integer nbJours) {
		this.nbJours = nbJours;
	}

	public boolean isAppliquerATous() {
		return appliquerATous;
	}

	public void setAppliquerATous(boolean appliquerATous) {
		this.appliquerATous = appliquerATous;
	}

	public List<ImageLgr> getListeTypeDocSelection() {
		return listeTypeDocSelection;
	}

	public void setListeTypeDocSelection(List<ImageLgr> listeTypeDocSelection) {
		this.listeTypeDocSelection = listeTypeDocSelection;
	}

	public List<ImageLgr> getListeTypeDocCreation() {
		return listeTypeDocCreation;
	}

	public void setListeTypeDocCreation(List<ImageLgr> listeTypeDocCreation) {
		this.listeTypeDocCreation = listeTypeDocCreation;
	}

	public List<String> getListeTypePaiement() {
		return listeTypePaiement;
	}

	public void setListeTypePaiement(List<String> listeTypePaiement) {
		this.listeTypePaiement = listeTypePaiement;
	}

	public String getSelectedTypePaiement() {
		return selectedTypePaiement;
	}

	public void setSelectedTypePaiement(String selectedTypePaiement) {
		this.selectedTypePaiement = selectedTypePaiement;
	}

	public List<String> getListeChamps() {
		return listeChamps;
	}

	public void setListeChamps(List<String> listeChamps) {
		this.listeChamps = listeChamps;
	}

	public String getSelectedChamps() {
		return selectedChamps;
	}

	public void setSelectedChamps(String selectedChamps) {
		this.selectedChamps = selectedChamps;
	}

	public List<String> getListeMots() {
		return listeMots;
	}

	public void setListeMots(List<String> listeMots) {
		this.listeMots = listeMots;
	}

	public String getSelectedMots() {
		return selectedMots;
	}

	public void setSelectedMots(String selectedMots) {
		this.selectedMots = selectedMots;
	}



	public void actValiderTiers(ActionEvent event)  {
		try {

			//prendre tout ce qu'il y a dans la grille et l'envoyer dans l'autre grille
			modifListeTiersSurSelection(selectedTierses);
			
			LinkedList<IDocumentTiers> list =new LinkedList<IDocumentTiers>();
			LinkedList<IDocumentTiers> listTemp =new LinkedList<IDocumentTiers>();
			LinkedList<TaCreationDoc> listCreation =new LinkedList<TaCreationDoc>();

			for (Object tiers : selectedTierses) {
				if(((TaTiersDTO)tiers).getAccepte()){
					listTemp.clear();
					listTemp=modelDocument.remplirListe( dateDeb, dateFin, ((TaTiersDTO)tiers).getCodeTiers(),
							getSelectedTypeSelection().getName(), getSelectedTypeCreation().getName(),null);
					for (IDocumentTiers doc : listTemp) {
						list.add(doc);
					}
				}
			}


			Calendar lundiSuivant = Calendar.getInstance();
			Calendar lundi15Suivant = Calendar.getInstance();
			Calendar moisSuivant = Calendar.getInstance();
			Calendar jourSuivant = Calendar.getInstance();
			Calendar decadeSuivant = Calendar.getInstance();

			boolean changement=false;
			int compteur=0;
			int compteurFacture=1;
			String codeTiersEnCours="";
			int ttcEnCours=0;
			TaCreationDoc c=null;
			String libelleInit=initialiseLibelleDoc(getSelectedTypeSelection().getName());
			libelle=libelleInit;
			String libelleSupp=" (saisie en TTC)";

			List<IDocumentTiers> listeDocAvecEscompte=new LinkedList<IDocumentTiers>();
			TaParamCreeDocTiers paramTiers=null;
			for (IDocumentTiers doc1 : list) {
				if(doc1.getTxRemTtcDocument()!=null && doc1.getTxRemTtcDocument().signum()!=0){
					//on traite le document dans un deuxième temps
					listeDocAvecEscompte.add(doc1);
				}else {//si on traite le document maintenant
					if(!codeTiersEnCours.equals(doc1.getTaTiers().getCodeTiers())){
						//récupérer les param de chaque tiers
						paramTiers=rempliParamGeneral();
						if(!isAppliquerATous()){ 
							try {
								List<TaParamCreeDocTiers>liste=daoParamCreeDocTiers.findByCodeTypeDoc(doc1.getTaTiers().getCodeTiers(),selectedTypeSelection.getName());
								if(liste!=null && liste.size()>0)
									paramTiers=liste.get(0);
							} catch (Exception e) {
								paramTiers=rempliParamGeneral();
							}
						}
						if(paramTiers==null)paramTiers=rempliParamGeneral();
						changement=true;
						lundiSuivant.setTime(dateDeb);//initialise avec la date début
						lundiSuivant.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//indique le premier jour de la semaine
						lundiSuivant.add(Calendar.DAY_OF_WEEK, 7);//initialise le prochain lundi


						lundi15Suivant.setTime(dateDeb);//initialise avec la date début
						lundi15Suivant.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//indique le premier jour de la semaine
						lundi15Suivant.add(Calendar.DAY_OF_WEEK, 14);//initialise le prochain lundi

						jourSuivant.setTime(dateDeb);//initialise avec la date début
						jourSuivant.add(Calendar.DAY_OF_WEEK, paramTiers.getNbJours());//initialise le prochain jour en fonction d'un nb de jour donné


						Calendar moisSuivant1 = Calendar.getInstance();
						moisSuivant.setTime(dateDeb);//initialise avec la date début
						moisSuivant.set(Calendar.DAY_OF_MONTH,1);
						moisSuivant.setTime(LibDate.incrementDate(moisSuivant.getTime(), 0, 1, 0));

						decadeSuivant.setTime(dateDeb);//initialise avec la date début
						decadeSuivant.set(Calendar.DAY_OF_MONTH,1);
						if(LibConversion.stringToInteger(LibDate.getJour(decadeSuivant.getTime()))>20){
							decadeSuivant.set(Calendar.DAY_OF_MONTH,1);
							decadeSuivant.setTime(LibDate.incrementDate(decadeSuivant.getTime(), 0, 1, 0));
						}
						else
							decadeSuivant.add(Calendar.DAY_OF_MONTH, 10);
					}
					if(doc1.getTtc().compareTo(ttcEnCours )!=0){
						if(!changement){
							changement=true;
							lundiSuivant.setTime(dateDeb);//initialise avec la date début
							lundiSuivant.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//indique le premier jour de la semaine
							lundiSuivant.add(Calendar.DAY_OF_WEEK, 7);//initialise le prochain lundi


							lundi15Suivant.setTime(dateDeb);//initialise avec la date début
							lundi15Suivant.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//indique le premier jour de la semaine
							lundi15Suivant.add(Calendar.DAY_OF_WEEK, 14);//initialise le prochain lundi

							jourSuivant.setTime(dateDeb);//initialise avec la date début
							jourSuivant.add(Calendar.DAY_OF_WEEK, paramTiers.getNbJours());//initialise le prochain jour en fonction d'un nb de jour donné


							Calendar moisSuivant1 = Calendar.getInstance();
							moisSuivant.setTime(dateDeb);//initialise avec la date début
							moisSuivant.set(Calendar.DAY_OF_MONTH,1);
							moisSuivant.setTime(LibDate.incrementDate(moisSuivant.getTime(), 0, 1, 0));

							decadeSuivant.setTime(dateDeb);//initialise avec la date début
							if(LibConversion.stringToInteger(LibDate.getJour(decadeSuivant.getTime()))>20){
								decadeSuivant.set(Calendar.DAY_OF_MONTH,1);
								decadeSuivant.setTime(LibDate.incrementDate(decadeSuivant.getTime(), 0, 1, 0));
							}
							else
								decadeSuivant.add(Calendar.DAY_OF_MONTH, 10);
						}

					}
					if(paramTiers.getTiers().compareTo(1)==0){
						if(changement){
							if(doc1.getTtc().compareTo(new Integer(1))==0)
								libelle=libelleInit+" (saisie en TTC)";
							else libelle=libelleInit;
							//création d'un nouveau document
							c=new TaCreationDoc();
							c.setCodeDocument(getSelectedTypeCreation()+" :"+compteurFacture);
							c.setLibelleDocument(libelle);
							c.setCodeTiers(doc1.getTaTiers().getCodeTiers());
							c.setAccepte(true);
							c.setNbDecimalesPrix(doc1.getNbDecimalesPrix());
							listCreation.add(c);
							codeTiersEnCours=doc1.getTaTiers().getCodeTiers();
							ttcEnCours=doc1.getTtc();
							changement=false;
							compteurFacture++;
							compteur=0;
						}
						c.getListeDoc().add(doc1);
						compteur++;
					}
					if(paramTiers.getDocument().compareTo(1)==0){
						if(changement){
							if(doc1.getTtc().compareTo(new Integer(1))==0)
								libelle=libelleInit+" (saisie en TTC)";
							else libelle=libelleInit;
							//création d'un nouveau document
							c=new TaCreationDoc();
							c.setCodeDocument(getSelectedTypeCreation()+" :"+compteurFacture);
							c.setLibelleDocument(libelle);
							c.setCodeTiers(doc1.getTaTiers().getCodeTiers());
							c.setAccepte(true);
							c.setNbDecimalesPrix(doc1.getNbDecimalesPrix());
							listCreation.add(c);
							codeTiersEnCours=doc1.getTaTiers().getCodeTiers();
							ttcEnCours=doc1.getTtc();
							changement=true;
							compteurFacture++;
							compteur=0;
						}
						c.getListeDoc().add(doc1);
						compteur++;
					}
					if(paramTiers.getSemaine().compareTo(1)==0){
						if(LibDate.compareTo(lundiSuivant.getTime(),doc1.getDateDocument())<=0||
								changement){	
							if(doc1.getTtc().compareTo(new Integer(1))==0)
								libelle=libelleInit+" (saisie en TTC)";
							else libelle=libelleInit;
							//création d'un nouveau document
							c=new TaCreationDoc();
							c.setCodeDocument(getSelectedTypeCreation()+" :"+compteurFacture);
							c.setLibelleDocument(libelle);
							c.setCodeTiers(doc1.getTaTiers().getCodeTiers());
							c.setAccepte(true);
							c.setNbDecimalesPrix(doc1.getNbDecimalesPrix());
							listCreation.add(c);
							codeTiersEnCours=doc1.getTaTiers().getCodeTiers();
							ttcEnCours=doc1.getTtc();
							changement=false;
							compteurFacture++;
							compteur=0;
							while(LibDate.compareTo(lundiSuivant.getTime(),doc1.getDateDocument())<=0)
								lundiSuivant.add(Calendar.DAY_OF_WEEK, 7);
							//						if(lundiSuivant.getTime().before(doc1.getDateDocument()))lundiSuivant.add(Calendar.DAY_OF_WEEK, 7);
						}
						c.getListeDoc().add(doc1);
						compteur++;
					}
					if(paramTiers.getDeuxSemaines().compareTo(1)==0){
						if(LibDate.compareTo(lundi15Suivant.getTime(),doc1.getDateDocument())<=0||
								changement){	
							if(doc1.getTtc().compareTo(new Integer(1))==0)
								libelle=libelleInit+" (saisie en TTC)";
							else libelle=libelleInit;
							//création d'un nouveau document
							c=new TaCreationDoc();
							c.setCodeDocument(getSelectedTypeCreation()+" :"+compteurFacture);
							c.setLibelleDocument(libelle);
							c.setCodeTiers(doc1.getTaTiers().getCodeTiers());
							c.setAccepte(true);
							c.setNbDecimalesPrix(doc1.getNbDecimalesPrix());
							listCreation.add(c);
							codeTiersEnCours=doc1.getTaTiers().getCodeTiers();
							ttcEnCours=doc1.getTtc();
							changement=false;
							compteurFacture++;
							compteur=0;
							while(LibDate.compareTo(lundi15Suivant.getTime(),doc1.getDateDocument())<=0)
								lundi15Suivant.add(Calendar.DAY_OF_WEEK,14);
							//						if(lundi15Suivant.getTime().before(doc1.getDateDocument()))	lundi15Suivant.add(Calendar.DAY_OF_WEEK, 14);
						}
						c.getListeDoc().add(doc1);
						compteur++;
					}
					if(paramTiers.getDecade().compareTo(1)==0){
						if(LibDate.compareTo(decadeSuivant.getTime(), doc1.getDateDocument())<=0||
								changement){	
							if(doc1.getTtc().compareTo(new Integer(1))==0)
								libelle=libelleInit+" (saisie en TTC)";
							else libelle=libelleInit;
							//création d'un nouveau document
							c=new TaCreationDoc();
							c.setCodeDocument(getSelectedTypeCreation()+" :"+compteurFacture);
							c.setLibelleDocument(libelle);
							c.setCodeTiers(doc1.getTaTiers().getCodeTiers());
							c.setAccepte(true);
							c.setNbDecimalesPrix(doc1.getNbDecimalesPrix());
							listCreation.add(c);
							codeTiersEnCours=doc1.getTaTiers().getCodeTiers();
							ttcEnCours=doc1.getTtc();
							changement=false;
							compteurFacture++;
							compteur=0;
							while(LibDate.compareTo(decadeSuivant.getTime(),doc1.getDateDocument())<=0)		
								if(LibConversion.stringToInteger(LibDate.getJour(decadeSuivant.getTime()))>20){
									decadeSuivant.set(Calendar.DAY_OF_MONTH,1);
									decadeSuivant.setTime(LibDate.incrementDate(decadeSuivant.getTime(), 0, 1, 0));
								}
								else
									decadeSuivant.add(Calendar.DAY_OF_WEEK, 10);
						}
						c.getListeDoc().add(doc1);
						compteur++;
					}
					if(paramTiers.getMois().compareTo(1)==0){
						if(LibDate.compareTo(moisSuivant.getTime(),doc1.getDateDocument())<=0||
								changement){	
							if(doc1.getTtc().compareTo(new Integer(1))==0)
								libelle=libelleInit+" (saisie en TTC)";
							else libelle=libelleInit;
							//création d'un nouveau document
							c=new TaCreationDoc();
							c.setCodeDocument(getSelectedTypeCreation()+" :"+compteurFacture);
							c.setLibelleDocument(libelle);
							c.setCodeTiers(doc1.getTaTiers().getCodeTiers());
							c.setAccepte(true);
							c.setNbDecimalesPrix(doc1.getNbDecimalesPrix());
							listCreation.add(c);
							codeTiersEnCours=doc1.getTaTiers().getCodeTiers();
							ttcEnCours=doc1.getTtc();
							changement=false;
							compteurFacture++;
							compteur=0;
							while(LibDate.compareTo(moisSuivant.getTime(),doc1.getDateDocument())<=0)
								moisSuivant.setTime(LibDate.incrementDate(moisSuivant.getTime(), 0, 1, 0));
							//						if(moisSuivant.getTime().before(doc1.getDateDocument()))
							//							moisSuivant.setTime(LibDate.incrementDate(moisSuivant.getTime(), 0, 1, 0));
						}
						c.getListeDoc().add(doc1);
						compteur++;
					}
					if(paramTiers.getxJours().compareTo(1)==0){
						if(LibDate.compareTo(jourSuivant.getTime(),doc1.getDateDocument())<=0||
								changement){		
							if(doc1.getTtc().compareTo(new Integer(1))==0)
								libelle=libelleInit+" (saisie en TTC)";
							else libelle=libelleInit;
							//création d'un nouveau document
							c=new TaCreationDoc();
							c.setCodeDocument(getSelectedTypeCreation()+" :"+compteurFacture);
							c.setLibelleDocument(libelle);
							c.setCodeTiers(doc1.getTaTiers().getCodeTiers());
							c.setAccepte(true);
							c.setNbDecimalesPrix(doc1.getNbDecimalesPrix());
							listCreation.add(c);
							codeTiersEnCours=doc1.getTaTiers().getCodeTiers();
							ttcEnCours=doc1.getTtc();
							changement=false;
							compteurFacture++;
							compteur=0;
							while(LibDate.compareTo(jourSuivant.getTime(),doc1.getDateDocument())<=0)
								jourSuivant.add(Calendar.DAY_OF_WEEK, paramTiers.getNbJours());
							//						if(jourSuivant.getTime().before(doc1.getDateDocument()))
							//							jourSuivant.add(Calendar.DAY_OF_WEEK, paramTiers.getNbJours());
						}
						c.getListeDoc().add(doc1);
						compteur++;
					}


				}//fin si on traite le document maintenant	
			}
			changement=true;
			for (IDocumentTiers doc1 : listeDocAvecEscompte) {
				if(changement){
					//création d'un nouveau document
					c=new TaCreationDoc();
					c.setCodeDocument(getSelectedTypeCreation()+" :"+compteurFacture);
					c.setLibelleDocument(libelleInit+" (avec escompte)");
					c.setCodeTiers(doc1.getTaTiers().getCodeTiers());
					c.setAccepte(true);
					c.setNbDecimalesPrix(doc1.getNbDecimalesPrix());
					listCreation.add(c);
					codeTiersEnCours=doc1.getTaTiers().getCodeTiers();
					changement=true;
					compteurFacture++;
					compteur=0;
				}
				c.getListeDoc().add(doc1);
				compteur++;
			}


			modelDocument.getModelCreationDoc().setListeEntity(listCreation);
			modelDocument.getModelCreationDoc().remplirListe();
			listeDocumentDTO=modelDocument.getModelCreationDoc().getListeObjet();
			root=createDocuments();
//			selectedNode=rempliSelectionDocument(root);
		}catch (Exception e) {
		}
		finally{
		}
	}

	public void actRemplacerSelectionTiers(ActionEvent event)  {
		actValiderTiers(null);
	}


	public void actSupprimerNonCochesTiers(ActionEvent event)  {
		modifListeTiersSurSelection(selectedTierses);
		List<TaTiersDTO> listeTemp=new LinkedList<TaTiersDTO>();
		//(controles de sortie et fermeture de la fenetre) => onClose()
		for (Object tiers : listeTiersDTO) {
			if(!((TaTiersDTO)tiers).getAccepte())
				listeTemp.add(((TaTiersDTO)tiers));				
		}
		listeTiersDTO.removeAll(listeTemp);
		selectedTierses= rempliSelection(listeTiersDTO);
	}

	public void actReinitialiserTiers(ActionEvent event)  {		
		listeTiersDTO.clear();
		listeDocumentDTO.clear();
		modelTiers.getListeEntity().clear();
		modelTiers.getListeObjet().clear();
		selectedTierses= rempliSelection(listeTiersDTO);
	}

	public void actToutCocherTiers(ActionEvent event)  {
		modifListeTiersSurSelection(selectedTierses);
		selectedTierses=new TaTiersDTO[]{}; 
		for (Object obj : listeTiersDTO) {
			((TaTiersDTO)obj).setAccepte(true);
		}
		selectedTierses= rempliSelection(listeTiersDTO);
	}

	public void actToutDecocher(ActionEvent event)  {
		modifListeTiersSurSelection(selectedTierses);
		selectedTierses=new TaTiersDTO[]{}; 
		for (Object obj : listeTiersDTO) {
			((TaTiersDTO)obj).setAccepte(false);
		}
	}

	
	
	public void actInverserCoches(ActionEvent event)  {
		modifListeTiersSurSelection(selectedTierses);
		selectedTierses=new TaTiersDTO[]{}; 
		// TODO Auto-generated method stub
		for (TaTiersDTO tiers : listeTiersDTO) {
			((TaTiersDTO)tiers).setAccepte(!((TaTiersDTO)tiers).getAccepte());
		}
		selectedTierses= rempliSelection(listeTiersDTO);
	}

	public void actSupprimerNonCochesDocument(ActionEvent event)  {
		List<TreeNode> listeTemp=new LinkedList<TreeNode>();
		//(controles de sortie et fermeture de la fenetre) => onClose()
		for (TreeNode tiers : root.getChildren()) {
			if(!((CreationDocLigneDTO)tiers.getData()).getAccepte())
				listeTemp.add((TreeNode) (tiers));				
		}
		root.getChildren().removeAll(listeTemp);
	}

	public void actRefreshDocument(ActionEvent event)  {
		try{	
			modelDocument.getModelCreationDoc().remplirListe();

		}catch (Exception e) {
		}
		finally{
		}
	}

	public void actAccepterDocuments(ActionEvent event)  {

	}


	
    public void collapseAll() {
        setExpandedRecursively(root, false);
    }

    public void expandAll() {
        setExpandedRecursively(root, true);
    }

    private void setExpandedRecursively(final TreeNode node, final boolean expanded) {
        for (final TreeNode child : node.getChildren()) {
            setExpandedRecursively(child, expanded);
        }
        node.setExpanded(expanded);
    }
	
    public void actInverserLesCochesDocument() {
    	InverserLesCochesDocument(root);
    }
    private void InverserLesCochesDocument(final TreeNode node) {
        for (final TreeNode child : node.getChildren()) {
        	InverserLesCochesDocument(child);
        }
        node.setSelected(!node.isSelected());
    }
    
	public void actInverserDocuments(ActionEvent event)  {
		List<TreeNode> listeNode=new LinkedList<>();
		// TODO Auto-generated method stub
//		selectedNode=new DefaultTreeNode[]{}; 
//		root.setSelected(!root.isSelected());
		if(!listeNode.contains(root))listeNode.add(root);
		for (TreeNode node1 : root.getChildren()) {
			for (TreeNode node2 : node1.getChildren()) {
				for (TreeNode node3 : node2.getChildren()) {
					if(!listeNode.contains(node3))listeNode.add(node3);
//					node3.setSelected(!node3.isSelected());
				}
				if(!listeNode.contains(node2))listeNode.add(node2);
//				node2.setSelected(!node2.isSelected());
			}
			if(!listeNode.contains(node1))listeNode.add(node1);
//			node1.setSelected(!node1.isSelected());
		}
		for (TreeNode treeNode : listeNode) {
			treeNode.setSelected(!treeNode.isSelected());
		}
//		selectedNode= rempliSelectionDocument(root);
	}

	public List<IDocumentTiers> rechercheEntiteDocument(CreationDocLigneDTO ligne) {
		int i = 0;
		Collection<TaCreationDoc> model=modelDocument.getModelCreationDoc().getListeEntity();
		Collection<IDocumentTiers> listTemp=null;
		for (TaCreationDoc taCreationDoc : model) {
			if(taCreationDoc.getCodeTiers().equals(ligne.getCodeTiers())){
				listTemp=taCreationDoc.getListeDoc();
				return (List<IDocumentTiers>) listTemp;
//				for (IDocumentTiers iDocumentTiers : listTemp) {
//					if(iDocumentTiers.getCodeDocument().equals(ligne.getCodeDocumentRecup()))
//						return iDocumentTiers;
//				}
			}
		}
		return null;

	}
	
	
	public IDocumentTiers rechercheDocumentNode(TreeNode ligne) {
		return documentValide(((CreationDocLigneDTO)ligne.getData()).getCodeDocumentRecup());
	}

	public void actToutcocherDocuments(ActionEvent event)  {
//		selectedNode=new DefaultTreeNode[]{}; 
		root.setSelected(true);
		for (TreeNode node1 : root.getChildren()) {
			node1.setSelected(true);
			for (TreeNode node2 : node1.getChildren()) {
				node2.setSelected(true);
				for (TreeNode node3 : node2.getChildren()) {
					node3.setSelected(true);
				}
			}
		}
//		selectedNode= rempliSelectionDocument(root);
	}

	public void actToutDecocherDocuments(ActionEvent event)  {
//		selectedNode=new DefaultTreeNode[]{}; 
		root.setSelected(false);
		for (TreeNode node1 : root.getChildren()) {
			node1.setSelected(false);
			for (TreeNode node2 : node1.getChildren()) {
				node2.setSelected(false);
				for (TreeNode node3 : node2.getChildren()) {
					node3.setSelected(false);
				}
			}
		}
//		selectedNode= rempliSelectionDocument(root);
	}
	
	
	public OuvertureDocumentBean getOuvertureDocumentBean() {
		return ouvertureDocumentBean;
	}

	public void setOuvertureDocumentBean(OuvertureDocumentBean ouvertureDocumentBean) {
		this.ouvertureDocumentBean = ouvertureDocumentBean;
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
			case "modifier":
			case "supprimer":
			case "imprimer":
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

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}

	public CreationDocLigneDTO[] getSelectedDocuments() {
		return selectedDocuments;
	}

	public void setSelectedDocuments(CreationDocLigneDTO[] selectedDocuments) {
		this.selectedDocuments = selectedDocuments;
	}

	public TaTiersDTO[] getSelectedTierses() {
		return selectedTierses;
	}

	public void setSelectedTierses(TaTiersDTO[] selectedTierses) {
		this.selectedTierses = selectedTierses;
	}

	public IModelRecupDocumentCreationDocServiceRemote getModelDocument() {
		return modelDocument;
	}

	public void setModelDocument(IModelRecupDocumentCreationDocServiceRemote modelDocument) {
		this.modelDocument = modelDocument;
	}

	public IModelTiersCreationDocServiceRemote getModelTiers() {
		return modelTiers;
	}

	public void setModelTiers(IModelTiersCreationDocServiceRemote modelTiers) {
		this.modelTiers = modelTiers;
	}

	public List<CreationDocLigneDTO> getListeDocumentDTO() {
		return listeDocumentDTO;
	}

	public void setListeDocumentDTO(List<CreationDocLigneDTO> listeDocumentDTO) {
		this.listeDocumentDTO = listeDocumentDTO;
	}

	public LinkedList<TaTiersDTO> getListeTiersDTO() {
		return listeTiersDTO;
	}

	public void setListeTiersDTO(LinkedList<TaTiersDTO> listeTiersDTO) {
		this.listeTiersDTO = listeTiersDTO;
	}

	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}
	
	public TreeNode createDocuments(CreationDocLigneDTO obj, TreeNode parent){
		TreeNode doc = new DefaultTreeNode(obj, parent);
		doc.setSelected(true);
		for (CreationDocLigneDTO l : obj.getList()) {
			TreeNode node = new DefaultTreeNode(l, doc);
			node.setSelected(true);
			if(!l.getList().isEmpty()){
				for (CreationDocLigneDTO l2 : l.getList()) {
					createDocuments(l2,node);
				}			
			}
//			new DefaultTreeNode("Lot","DUMMY", node);
		}
		return doc;
		
	}
	
	
	public TreeNode createDocuments(){
		TreeNode doc = new DefaultTreeNode("", null);
		doc.setSelected(true);
		if(listeDocumentDTO!=null && !listeDocumentDTO.isEmpty()){
			doc=new DefaultTreeNode(listeDocumentDTO.get(0), null);
			doc.setSelected(true);
			for (CreationDocLigneDTO l : listeDocumentDTO) {
				createDocuments(l,doc);						
			}
		}
		return doc;
	}

		
	
	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode[] selectedNode) {
		this.selectedNode = selectedNode;
	}


	public TaTiers getDetailLigneTiers() {
		return detailLigneTiers;
	}


	public void setDetailLigneTiers(TaTiers detailLigneTiers) {
		this.detailLigneTiers = detailLigneTiers;
	}

public TaTiers recupCodetiers(String code){
	FacesContext context = FacesContext.getCurrentInstance();
	if(code!=null && !code.isEmpty())
		try {
			return daoTiers.findByCode(code);
		} catch (FinderException e) {
			context.addMessage(null, new FacesMessage("erreur", "code tiers vide")); 	
		}
	return null;
}

public IDocumentTiers recupCodeDocument(String code){
	FacesContext context = FacesContext.getCurrentInstance();
	if(code!=null && !code.isEmpty())
		return documentValide(code);
	return null;
}




	public IDocumentTiers getDetailLigne() {
		return detailLigne;
	}


	public void setDetailLigne(IDocumentTiers detailLigne) {
		this.detailLigne = detailLigne;
	}


	public String getPathImageSelectionDocSelection() {
		return pathImageSelectionDocSelection;
	}


	public void setPathImageSelectionDocSelection(String pathImageSelectionDocSelection) {
		this.pathImageSelectionDocSelection = pathImageSelectionDocSelection;
	}


	public String getPathImageSelectionDocCreation() {
		return pathImageSelectionDocCreation;
	}


	public void setPathImageSelectionDocCreation(String pathImageSelectionDocCreation) {
		this.pathImageSelectionDocCreation = pathImageSelectionDocCreation;
	}			

	public boolean ImageEstPasVide(String pathImage){
		return ( pathImage!=null && !pathImage.isEmpty() );
	}

	public boolean afficheMessageNbDecimale(CreationDocLigneDTO doc) {
		if(doc==null)return false;
		if(doc.getNbDecimalesPrix()==null) return false;
		if(prefNbDecimalesPrix == null) return false;
		if (!prefNbDecimalesPrix.equals(doc.getNbDecimalesPrix()))	return true;		
		return false;
	}




	public Integer getPrefNbDecimalesPrix() {
		return prefNbDecimalesPrix;
	}


	public void setPrefNbDecimalesPrix(Integer prefNbDecimalesPrix) {
		this.prefNbDecimalesPrix = prefNbDecimalesPrix;
	}


	public Integer getPrefNbDecimalesQte() {
		return prefNbDecimalesQte;
	}


	public void setPrefNbDecimalesQte(Integer prefNbDecimalesQte) {
		this.prefNbDecimalesQte = prefNbDecimalesQte;
	}
}
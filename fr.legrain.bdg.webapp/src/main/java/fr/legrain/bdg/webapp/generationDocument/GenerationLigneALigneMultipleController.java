package fr.legrain.bdg.webapp.generationDocument;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;

import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.documents.service.remote.IModelRecupLigneDocumentCreationDocServiceRemote;
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
import fr.legrain.bdg.documents.service.remote.ITaLBoncdeAchatServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaPrelevementServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaProformaServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.dossierIntelligent.service.remote.ITaParamDossierIntelligentServiceRemote;
import fr.legrain.bdg.dossierIntelligent.service.remote.ITaTypeDonneeServiceRemote;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.tiers.service.remote.ITaEtatServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaParamCreeDocTiersServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.documents.OuvertureDocumentBean;
import fr.legrain.bdg.webapp.documents.TaLigneALigneDTOJSF;
import fr.legrain.document.dto.IDocumentDTO;
import fr.legrain.document.dto.IDocumentTiers;
import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.ImageLgr;
import fr.legrain.document.model.RetourGenerationDoc;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaAvisEcheance;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBoncdeAchat;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaHistREtatLigneDocument;
import fr.legrain.document.model.TaLBoncdeAchat;
import fr.legrain.document.model.TaPrelevement;
import fr.legrain.document.model.TaProforma;
import fr.legrain.document.model.TaREtat;
import fr.legrain.document.model.TaREtatLigneDocument;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.document.model.TypeDoc;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.generation.service.CreationDocumentMultiple;
import fr.legrain.generationDocument.model.ParamAfficheChoixGenerationDocument;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped 
public class GenerationLigneALigneMultipleController extends AbstractController{

	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisation;

	@Inject @Named(value="ouvertureDocumentBean")
	private OuvertureDocumentBean ouvertureDocumentBean;

	@EJB private CreationDocumentMultiple creation;	
	@EJB private ITaLotServiceRemote taLotService ;

	private ParamAfficheChoixGenerationDocument result;

	protected String messageTerminerLigne=Const.C_MESSAGE_TERMINER_LIGNE_DOCUMENT;
	protected String messageReintegrerLigne=Const.C_MESSAGE_REINTEGRER_LIGNE_DOCUMENT;
	private final String C_MESS_SELECTION_VIDE = "Aucun document n'a été sélectionné !!!";
	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected IDocumentTiers detailLigne;
//	private TaTiers detailLigneTiers;
	protected Date dateDeb=null;
	protected Date dateFin=null;
	//	private TaTiers tiers = null;
	protected String libelle="";
//	private List<TaTiers>listeTiers = null;
	protected boolean ouvrir=false;
	protected TaEtat selectionEtat;
//	boolean afficheLigneAvecEtat=true;
	
	@EJB private ITaTiersServiceRemote daoTiers;
	@EJB private ITaFactureServiceRemote daoFacture;
	@EJB private ITaAvoirServiceRemote daoAvoir;
	@EJB private ITaBonlivServiceRemote daoBonLiv;
	@EJB private ITaBoncdeServiceRemote daoBoncde;
	@EJB private ITaBonReceptionServiceRemote daoBonReception;
	@EJB private ITaBoncdeAchatServiceRemote daoBoncdeAchat;
	@EJB private ITaLBoncdeAchatServiceRemote daoLBoncdeAchat;
	@EJB private ITaApporteurServiceRemote daoApporteur;
	@EJB private ITaProformaServiceRemote daoProforma;
	@EJB private ITaDevisServiceRemote daoDevis;
	@EJB private ITaPrelevementServiceRemote daoPrelevement;
	@EJB private ITaAcompteServiceRemote daoAcompte;
	@EJB private ITaTicketDeCaisseServiceRemote daoTicketDeCaisse;
	@EJB private ITaAvisEcheanceServiceRemote daoAvisEcheance;
	@EJB private ITaPreferencesServiceRemote PreferencesService;
	@EJB private ITaInfoEntrepriseServiceRemote daoInfos;
	@EJB private ITaTPaiementServiceRemote daoTPaiement ;
	@EJB private ITaParamCreeDocTiersServiceRemote daoParamCreeDocTiers;
	@EJB private ITaParamDossierIntelligentServiceRemote taParamDao;
	@EJB private ITaTypeDonneeServiceRemote daoType;
	@EJB private ITaArticleServiceRemote taArticleService;
	@EJB private ITaTiersServiceRemote taTiersService;
	@EJB private ITaEtatServiceRemote taEtatService;
	

	private TaBonliv taBonLiv = null;
	protected IDocumentTiers document = null;
	protected String codeDocument="";
	protected String codeTiers="";
	protected TaInfoEntreprise infos =null;
	protected TaEtat taEtat;


	protected List<ImageLgr> listeTypeDocSelection=new LinkedList<ImageLgr>();
	protected ImageLgr selectedTypeSelection ;
	protected ImageLgr oldSelectedTypeSelection=new ImageLgr() ;
	
	protected List<ImageLgr> listeTypeDocCreation=new LinkedList<ImageLgr>();
	protected ImageLgr selectedTypeCreation ;
	protected ImageLgr oldSelectedTypeCreation =new ImageLgr() ;

	protected String pathImageSelectionDocSelection=TypeDoc.PATH_IMAGE_ROUE_DENTEE;
	protected String pathImageSelectionDocCreation=TypeDoc.PATH_IMAGE_ROUE_DENTEE;
	

	protected LinkedList<TaTiersDTO> listeTiersDTO;
	
	@EJB protected IModelRecupLigneDocumentCreationDocServiceRemote modelLigneALigne ;
	protected LinkedList<ILigneDocumentTiers> listeLigneDocumentTiers;
	protected LinkedList<TaLigneALigneDTOJSF> listeLigneALigneDTO=new LinkedList<>();
	protected TaLigneALigneDTOJSF[] selectedLigneALigneDTOs ;
	protected TaLigneALigneDTOJSF selectedLigneALigneDTO ;
	protected TaTiersDTO selectedCritere ;
	protected List<IDocumentTiers> listeDocumentChangementEtat = new LinkedList<>();
	
//	protected LinkedList<TaLigneALigneSupplementDTO> listeLigneALigneDTOSupplement=new LinkedList<>();
	protected TaLigneALigneSupplementDTO selectedLigneALigneDTOSupplement ;


	@PostConstruct
	public void init() {
//		afficheLigneAvecEtat=false;
		resetTous();
		infos=daoInfos.findInstance();
		
		dateDeb=infos.getDatedebInfoEntreprise();
		dateFin=infos.getDatefinInfoEntreprise();


		//remplir la liste des type de documents que l'on peut selectionner
		listeTypeDocSelection.clear();

		for (String type : autorisation.getTypeDocCompletPresent().values()) {
			if(!type.equals(TypeDoc.TYPE_REGLEMENT)&&!type.equals(TypeDoc.TYPE_REMISE)&&!type.equals(TypeDoc.TYPE_ACOMPTE)){				
				listeTypeDocSelection.add(TypeDoc.getInstance().getPathImageCouleurDoc().get(type));
			}
		}

		int rang=0;
		rang=(listeTypeDocSelection.indexOf(TypeDoc.getInstance().getPathImageCouleurDoc().get(TypeDoc.TYPE_BON_COMMANDE_ACHAT)));
		if(rang==-1)rang=0;
		selectedTypeSelection=listeTypeDocSelection.get(rang);
		
		selectionComboSelection(null);


	}


	public void actFermer(ActionEvent e) {

	}
	public void actAnnuler(ActionEvent e) {
		if(selectedLigneALigneDTO!=null) {
		//on remet l'ancien
		selectedLigneALigneDTO.setDto(selectedLigneALigneDTO.getDto().copy(selectedLigneALigneDTO.getDtoOld()));
		selectedLigneALigneDTO.getListeSupplement().clear();
		}
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
		

		paramGeneration.setTitreFormulaire("Génération multiple");
		sessionMap.put("generation", paramGeneration);
		
		PrimeFaces.current().dialog().openDynamic("generation/generation_documents_simple", options, params);
		
	}
	
	public void handleReturnDialogGenerationDocument(SelectEvent event) {
		try{
		FacesContext context = FacesContext.getCurrentInstance();
//		enregistreEtatLigne();
		
		//liste doit être triée en fonction des groupes
		//prendre la première trouvée
		if(selectedLigneALigneDTOs.length>0){
		if(event!=null && event.getObject()!=null) {
			if(event.getObject() instanceof ParamAfficheChoixGenerationDocument){
				ParamAfficheChoixGenerationDocument param =  (ParamAfficheChoixGenerationDocument) event.getObject();
				if(param.getRetour()){
					String finDeLigne = "\r\n";

					List<IDocumentTiers> listeDocCrees=new LinkedList<IDocumentTiers>();
					param.setGenerationLigne(true);

					param.getListeDocumentSrc().clear();

					param.setDateLivraison(param.getDateDocument());

					List<IDocumentTiers> listeDoc = new LinkedList<>();
					String tiersCourant=codeTiers;
					String codeFacticeCourant="";
					boolean changer=false;
					int i=0;
					
					
					for (TaLigneALigneDTOJSF ligne : selectedLigneALigneDTOs) {
						if(ligne.getDto().getAccepte()) {
							try {
								IDocumentTiers doc = documentValide(ligne.getDto().getCodeDocument());
								listeDoc.add(doc);
								param.getListeDocumentSrc().add(doc);


								for (ILigneDocumentTiers ldoc : doc.getLignesGeneral()) {
									if(ligne.getDto().getIdLDocument().equals(ldoc.getIdLDocument())) {
										for (TaLigneALigneSupplementDTO ll : ligne.getListeSupplement()) {
											if(ll.getQteRecue()!=null && ll.getQteRecue().compareTo(BigDecimal.ZERO)!=0) {
												ldoc.getListeSupplement().add(ll);
											}
										}
										for (TaLigneALigneSupplementDTO ll : ligne.getListeAIntegrer()) {
											if(ll.getQteRecue()!=null && ll.getQteRecue().compareTo(BigDecimal.ZERO)!=0) {
												ldoc.getListeLigneAIntegrer().add(ll);
											}
										}										
										ldoc.setQteGenere(ligne.getDto().getQteRecue());
										ldoc.setUniteGenere(ligne.getDto().getUniteOrigine());
										ldoc.setNumlotGenere(ligne.getDto().getNumLot());
										ldoc.setGenereLigne(true);

										param.getListeLigneDocumentSrc().add(ldoc);
									}
									else ldoc.setGenereLigne(false);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();

							}
						}
					}

//					if(listeDoc!=null && !listeDoc.isEmpty()){
						for (IDocumentTiers doc : listeDoc) {
							if(doc.getAccepte()){
//								param.getListeDocumentSrc().add(doc);
								param.setDateLivraison(param.getDateDocument());
							}
						}
						//creation document
						RetourGenerationDoc retour=null;						
						if(param.getListeDocumentSrc().size()!=0){
							retour=null; 
							param.setCodeTiers(codeTiers);
							creation.setCodeTiers(codeTiers);
							creation.setParam(param);
							retour=creation.creationDocument(true);
							if(retour!=null && retour.isRetour()&&retour.getDoc()!=null){
								listeDocCrees.add(retour.getDoc());
								for (IDocumentTiers doc : param.getListeDocumentSrc()) {
									if(doc instanceof TaBoncdeAchat) {
										doc=daoBoncdeAchat.findByIDFetch(doc.getIdDocument());
										daoBoncdeAchat.merge((TaBoncdeAchat) doc);
									}
								}								
							}
						}
//					}
					param.setDateDocument(new Date());
					param.setDateLivraison(new Date());

					String message="";
					for (IDocumentTiers iDocumentTiers : listeDocCrees) {
						message+=iDocumentTiers.getCodeDocument()+finDeLigne;
					}
					if (!message.equals("")){
						LgrTab typeDocPresent = LgrTab.getInstance();
						String tab = typeDocPresent.getTabDocCorrespondance().get(selectedTypeCreation.getName());
						retour.setTypeTabCSS("tab tab-reception");
						retour.setOuvrirDoc(true);
						if(retour.getMessage()!=null && !retour.getMessage().isEmpty())
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Création document", retour.getMessage()));
						if(retour.isOuvrirDoc())
							ouvertureDocumentBean.detailDocument(retour.getDoc());						
//						context.addMessage(null, new FacesMessage("Liste des documents créés", message)); 	

						//						MessageDialog.openInformation(vue.getShell(), "Liste des documents créés", message);
					}

				}


			}

		}
		resetTous();
	}else{
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide",C_MESS_SELECTION_VIDE)); 	
	}
		
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	}


	public void enregistreEtatLigne(TaLigneALigneDTOJSF ligne) throws Exception {
		listeDocumentChangementEtat.clear();
		if(ligne!=null)
			if(!ligne.equalsEtat(ligne.getTaEtatOld())) {
				//trouver la ligne correspondante
				if(selectedTypeSelection.getName().equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
					TaBoncdeAchat bca=daoBoncdeAchat.findByIDFetch(ligne.getDto().getIdDocument());
					if(bca!=null) {
						for (TaLBoncdeAchat lbca : bca.getLignes()) {
							if(lbca.getIdLDocument()==ligne.getDto().getIdLDocument()) {
								
								lbca.addREtatLigneDoc(ligne.getTaEtat());								
								listeDocumentChangementEtat.add(bca);
							}
						}
					}
				}
			}
//		}
		for (IDocumentTiers doc : listeDocumentChangementEtat) {
			daoBoncdeAchat.merge((TaBoncdeAchat) doc);
			//enlever les lignes avec état des lignes à mettre dans nouveau Document
			for (TaLigneALigneDTOJSF li : selectedLigneALigneDTOs) {
				if(li.getDto().getIdDocument().equals(doc.getIdDocument())) {
					li.getDto().setAccepte(false);
					li.setTaEtatOld(li.getTaEtat());
				}
			}
		}

	}
	
	public void enregistreEtatLigne() throws Exception {
		listeDocumentChangementEtat.clear();
		for (TaLigneALigneDTOJSF l : listeLigneALigneDTO) {
//			if(l.getTaEtatOld()!=null || l.getTaEtat()!=null) {
			if(!l.equalsEtat(l.getTaEtatOld())) {
				//trouver la ligne correspondante
				if(selectedTypeSelection.getName().equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
					TaBoncdeAchat bca=daoBoncdeAchat.findByIDFetch(l.getDto().getIdDocument());
					if(bca!=null) {
						for (TaLBoncdeAchat lbca : bca.getLignes()) {
							if(lbca.getIdLDocument()==l.getDto().getIdLDocument()) {
								
								lbca.addREtatLigneDoc(l.getTaEtat());
//								TaREtatLigneDocument taREtat = lbca.getTaREtatLigneDocument(); 
//								if(taREtat==null)taREtat =new TaREtatLigneDocument();
//								else {
//									//mettre l'ancien dans historique
//									TaHistREtatLigneDocument taHist = new TaHistREtatLigneDocument();
//									taHist.setTaEtat(taREtat.getTaEtat());
//									taHist.setTaLBonReception(taREtat.getTaLBonReception());
//									lbca.getTaHistREtatLigneDocuments().add(taHist);
//
//								}
//								taREtat.setTaEtat(l.getTaEtat());
//								taREtat.setTaLBoncdeAchat(lbca);								
//								lbca.setTaREtatLigneDocument(taREtat);
								
								listeDocumentChangementEtat.add(bca);
							}
						}
					}
				}
			}
		}
		//		//gerer les doublons
		//		   Set<IDocumentTiers> list_unique=new HashSet<IDocumentTiers>(listeDocumentChangementEtat);
		//		   listeDocumentChangementEtat=new LinkedList(list_unique);
		for (IDocumentTiers doc : listeDocumentChangementEtat) {
			daoBoncdeAchat.merge((TaBoncdeAchat) doc);
			//enlever les lignes avec état des lignes à mettre dans nouveau Document
			for (TaLigneALigneDTOJSF ligne : selectedLigneALigneDTOs) {
				if(ligne.getDto().getIdDocument().equals(doc.getIdDocument())) {
					ligne.getDto().setAccepte(false);
					ligne.setTaEtatOld(ligne.getTaEtat());
				}
			}
		}

	}
	
	public void groupeLigne() {
		String oldGroupe=null;
		TaLigneALigneDTOJSF enCours = null;
		for (TaLigneALigneDTOJSF l : selectedLigneALigneDTOs) {
			if((l.getDto().getGroupe()!=null && !l.getDto().getGroupe().isEmpty()) 
					&& !l.getDto().equalsGroupe(oldGroupe)) {
				enCours=l;
				oldGroupe=l.getDto().getGroupe();
			}else if(l.getDto().equalsGroupe(oldGroupe) 
					&& (l.getDto().getGroupe()!=null && !l.getDto().getGroupe().isEmpty())  
					&& enCours!=null){
				TaLigneALigneSupplementDTO supp = new TaLigneALigneSupplementDTO();
				try {
					IDocumentTiers doc = null;
					switch (selectedTypeSelection.getName()) {
					case TypeDoc.TYPE_BON_COMMANDE_ACHAT:
						doc= daoBoncdeAchat.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_BON_COMMANDE:
						doc= daoBoncde.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_BON_LIVRAISON:
						doc= daoBonLiv.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_BON_RECEPTION:
						doc= daoBonReception.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_ACOMPTE:
						doc= daoAcompte.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_APPORTEUR:
						doc= daoApporteur.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_AVIS_ECHEANCE:
						doc= daoAvisEcheance.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_AVOIR:
						doc= daoAvoir.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_FACTURE:
						doc= daoFacture.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_DEVIS:
						doc= daoDevis.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_PRELEVEMENT:
						doc= daoPrelevement.findByIDFetch(l.getDto().getIdDocument());
						break;
					case TypeDoc.TYPE_PROFORMA:
						doc= daoProforma.findByIDFetch(l.getDto().getIdDocument());
						break;
					default:
						break;
					}

					for (ILigneDocumentTiers ld : doc.getLignesGeneral()) {
						if(ld.getIdLDocument()==l.getDto().getIdLDocument())
							supp.setLigne(ld);
					}
					
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				supp.setQteRecue(l.getDto().getQteRecue());
				supp.setNumLot(l.getDto().getNumLot());
				supp.setAccepte(enCours.getDto().getAccepte());
				enCours.getListeAIntegrer().add(supp);
				l.getDto().setAccepte(false);
			}
				
		}
	}
	
	public void actEnregistrer(ActionEvent e) {
//		selectedLigneALigneDTOs=rempliSelection(listeLigneALigneDTO);
		//enregistrer avant l'état des lignes, si état !=null alors ne peut plus rentrer dans génération document
		try {
			//tri la liste en fonction des groupes
			listeLigneALigneDTO.sort(new Comparator<TaLigneALigneDTOJSF>() {
				@Override
				public int compare(TaLigneALigneDTOJSF o1, TaLigneALigneDTOJSF o2) {
					String elem1 = o1.getDto().getGroupe()!=null ? o1.getDto().getGroupe():"";
					String elem2 = o2.getDto().getGroupe()!=null ? o2.getDto().getGroupe():"";
					return elem1.compareTo(elem2);
				}
			});

			selectedLigneALigneDTOs=rempliSelection(listeLigneALigneDTO);

		
		for (TaLigneALigneDTOJSF l : selectedLigneALigneDTOs) {
			if(l.getDto().getNumLot()!=null &&l.getDto().getNumLot().equals("*"))
				l.getDto().setNumLot("");
			l.getDto().setAccepte(true);
		}
		
		groupeLigne();
		
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
		if(selectedLigneALigneDTOs.length>0)
			actDialogGenerationDocument(null);
		else context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selection vide", C_MESS_SELECTION_VIDE)); 
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void onRowEditSupplement(RowEditEvent event) {
		selectedLigneALigneDTO.getDto().setAccepte(true);
	}
	
	public void onCellEdit(CellEditEvent  event) {
		selectedLigneALigneDTO.getDto().setAccepte(true);

	}
	
	
	public void enregistreLigne()  throws Exception {
//		enregistreEtatLigne();
		String message=controleUniciteNumLot(selectedLigneALigneDTO.getDto().getNumLot());
		if(!message.equals("")) {
			throw new Exception(message);
		}
		String message2=controleCoherenceGroupe(selectedLigneALigneDTO.getDto().getGroupe());
		if(!message2.equals("")) {
			throw new Exception(message2);
		}
		selectedLigneALigneDTO.getDto().setAccepte(true);
		selectedLigneALigneDTO.setDtoOld(selectedLigneALigneDTO.getDto().copy(selectedLigneALigneDTO.getDto()));
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	public void onRowEdit(RowEditEvent event) throws Exception {
		try {
			enregistreLigne();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesContext context = FacesContext.getCurrentInstance();  
			//	    context.addMessage(null, new FacesMessage("Articles", "actEnregistrer"));
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ligne", e.getMessage()));
			context.validationFailed();
//			throw new Exception(e.getMessage());
		}
	}
	
	public void onRowEditInit(RowEditEvent event) {
		actModifier(null);
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
			if(selectedCritere!=null)codeTiers=selectedCritere.getCodeTiers();
			resetTous();
			selectionComboSelection(null);
			if(selectedTypeCreation.equals("")){
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Aucune correspondance pour la création", "Ce type de document ne peut générer aucun autre type de document.")); 	
				throw new Exception();
			}

			document=null;
			if(codeDocument!=null && !codeDocument.isEmpty()){
				document=documentValide(codeDocument);
			}

			List<TaLigneALigneDTO> l= modelLigneALigne.remplirListe(dateDeb,
					dateFin,codeTiers,
					selectedTypeSelection.getName(),selectedTypeCreation.getName(),document,selectionEtat);
			
			for (TaLigneALigneDTO taLigneALigneDTO : l) {
				taLigneALigneDTO.setQteRecue(taLigneALigneDTO.getQteRestante());
				TaLigneALigneDTOJSF ll=new TaLigneALigneDTOJSF();
				ll.setDto(taLigneALigneDTO);
				ll.setDtoOld(ll.getDto().copy(taLigneALigneDTO));
				if(taLigneALigneDTO.getEtat()!=null) {
					ll.setTaEtat(taEtatService.findByCode(taLigneALigneDTO.getEtat()));
					ll.setTaEtatOld(taEtatService.findByCode(taLigneALigneDTO.getEtat()));
				}
				listeLigneALigneDTO.add(ll);
			}
			if(listeLigneALigneDTO!=null && listeLigneALigneDTO.size()>0)
				selectedLigneALigneDTO=listeLigneALigneDTO.get(0);
			selectedLigneALigneDTOs= rempliSelection(listeLigneALigneDTO);


		}catch (Exception e1) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("erreur", e1.getMessage())); 	
		}
		finally{

		}
	}


	
	public TaLigneALigneDTOJSF[] rempliSelection(List<TaLigneALigneDTOJSF> liste){
		List<TaLigneALigneDTOJSF> tmp = new LinkedList<>();
		for (TaLigneALigneDTOJSF l : liste) {
			if(l.getDto().getAccepte() && !disableSiEtat(l)) {
				tmp.add(l);
			}
		}
		return selectedLigneALigneDTOs= Arrays.copyOf(tmp.toArray(),tmp.size(),TaLigneALigneDTOJSF[].class);		
	}
	public List<TaLigneALigneDTOJSF> modifListeTiersSurSelection(TaLigneALigneDTOJSF[] tab){
		for (TaLigneALigneDTOJSF obj : listeLigneALigneDTO) {
			obj.getDto().setAccepte(false);
		}
		for (TaLigneALigneDTOJSF taTiersDTO : tab) {
			taTiersDTO.getDto().setAccepte(true);
		}
		return listeLigneALigneDTO;
	}
	
	public void resetTous() {
		try{
			//récupérer la liste des documents associable au type de document à créer
			//ces documents ne doivent pas déjà avoir de relation dans rDocument par rapport au
			//type document à créer.
			modelLigneALigne.getListeObjet().clear();
			listeLigneALigneDTO.clear();
			selectedLigneALigneDTOs=rempliSelection(listeLigneALigneDTO);

		}catch (Exception e) {

		}
		finally{
		}
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
				document=daoFacture.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_APPORTEUR)) {
				document=daoApporteur.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_AVOIR)) {
				document=daoAvoir.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_BON_COMMANDE)) {
				document=daoBoncde.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_BON_COMMANDE_ACHAT)) {
				document=daoBoncdeAchat.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_BON_LIVRAISON)) {
				document=daoBonLiv.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_DEVIS)) {
				document=daoDevis.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_PROFORMA)) {
				document=daoProforma.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_ACOMPTE)) {
				document=daoAcompte.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_PRELEVEMENT)) {
				document=daoPrelevement.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_AVIS_ECHEANCE)) {
				document=daoAvisEcheance.findByCode(code);
			}
			if(getSelectedTypeSelection().getName().equals(TypeDoc.TYPE_TICKET_DE_CAISSE)) {
				document=daoTicketDeCaisse.findByCode(code);
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



//	public void actSupprimerNonCochesTiers(ActionEvent event)  {
//		modifListeTiersSurSelection(selectedLigneALigneDTOs);
//		List<TaLigneALigneDTO> listeTemp=new LinkedList<TaLigneALigneDTO>();
//		//(controles de sortie et fermeture de la fenetre) => onClose()
//		for (Object tiers : listeLigneALigneDTO) {
//			if(!((TaLigneALigneDTO)tiers).getAccepte())
//				listeTemp.add(((TaLigneALigneDTO)tiers));				
//		}
//		listeLigneALigneDTO.removeAll(listeTemp);
//		selectedLigneALigneDTOs= rempliSelection(listeLigneALigneDTO);
//	}
//
//	public void actReinitialiserTiers(ActionEvent event)  {		
//		listeLigneALigneDTO.clear();
//		modelLigneALigne.getListeEntity().clear();
//		modelLigneALigne.getListeObjet().clear();
//		selectedLigneALigneDTOs= rempliSelection(listeLigneALigneDTO);
//	}
//
//	public void actToutCocherTiers(ActionEvent event)  {
//		modifListeTiersSurSelection(selectedLigneALigneDTOs);
//		selectedLigneALigneDTOs=new TaLigneALigneDTO[]{}; 
//		for (Object obj : listeLigneALigneDTO) {
//			((TaLigneALigneDTO)obj).setAccepte(true);
//		}
//		selectedLigneALigneDTOs= rempliSelection(listeLigneALigneDTO);
//	}
//
//	public void actToutDecocher(ActionEvent event)  {
//		modifListeTiersSurSelection(selectedLigneALigneDTOs);
//		selectedLigneALigneDTOs=new TaLigneALigneDTO[]{}; 
//		for (Object obj : listeLigneALigneDTO) {
//			((TaLigneALigneDTO)obj).setAccepte(false);
//		}
//	}

//	
//	
//	public void actInverserCoches(ActionEvent event)  {
//		modifListeTiersSurSelection(selectedLigneALigneDTOs);
//		selectedLigneALigneDTOs=new TaLigneALigneDTO[]{}; 
//		// TODO Auto-generated method stub
//		for (TaLigneALigneDTO tiers : listeLigneALigneDTO) {
//			((TaLigneALigneDTO)tiers).setAccepte(!((TaLigneALigneDTO)tiers).getAccepte());
//		}
//		selectedLigneALigneDTOs= rempliSelection(listeLigneALigneDTO);
//	}


	
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

	

	public String getCodeDocument() {
		return codeDocument;
	}

	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
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


	public String getCodeTiers() {
		return codeTiers;
	}


	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
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


	public LinkedList<TaLigneALigneDTOJSF> getListeLigneALigneDTO() {
		return listeLigneALigneDTO;
	}


	public void setListeLigneALigneDTO(LinkedList<TaLigneALigneDTOJSF> listeLigneALigneDTO) {
		this.listeLigneALigneDTO = listeLigneALigneDTO;
	}


	public TaLigneALigneDTOJSF[] getSelectedLigneALigneDTOs() {
		return selectedLigneALigneDTOs;
	}


	public void setSelectedLigneALigneDTOs(TaLigneALigneDTOJSF[] selectedLigneALigneDTO) {
		this.selectedLigneALigneDTOs = selectedLigneALigneDTO;
	}


	public TaLigneALigneDTOJSF getSelectedLigneALigneDTO() {
		return selectedLigneALigneDTO;
	}


	public void setSelectedLigneALigneDTO(TaLigneALigneDTOJSF selectedLigneALigneDTO) {
		this.selectedLigneALigneDTO = selectedLigneALigneDTO;
	}
	
	public List<TaEtat> etatAutoComplete(String query) {
		List<TaEtat> allValues = taEtatService.findByIdentifiantAndTypeEtat(TaEtat.ETAT_DOCUMENT,null);
		List<TaEtat> filteredValues = new ArrayList<TaEtat>();
		int positionEtatLigneCourante=0;
		allValues.add(0, new TaEtat(TaEtat.ETAT_TOUS));
		for (int i = 0; i < allValues.size(); i++) {
			TaEtat civ = allValues.get(i);
			//je ne prends pas tout ce qui est avant enCours
			
			int positionEnCours = taEtatService.documentEncours(null).getTaTEtat().getPosition();
			if(civ.getPosition()!=null && civ.getPosition().compareTo(positionEnCours)>=0) {
				if(query.equals("*") || civ.getCodeEtat().toLowerCase().contains(query.toLowerCase())) {
					filteredValues.add(civ);
				}
			}
		}

		return filteredValues;
	}
	
	public List<TaEtat> etatLigneAutoComplete(String query) {
		List<TaEtat> allValues = taEtatService.findByIdentifiantAndTypeEtat(TaEtat.ETAT_DOCUMENT,null);
		List<TaEtat> filteredValues = new ArrayList<TaEtat>();
		int positionEtatLigneCourante=0;
		if(selectedLigneALigneDTO!=null && selectedLigneALigneDTO.getTaEtat()!=null )
			positionEtatLigneCourante= selectedLigneALigneDTO.getTaEtat().getPosition();
		//		allValues.add(0, new TaEtat(TaEtat.ETAT_TOUS));
		for (int i = 0; i < allValues.size(); i++) {
			TaEtat civ = allValues.get(i);
			if(civ.getPosition().compareTo(positionEtatLigneCourante)>0) {
				if(query.equals("*") || civ.getCodeEtat().toLowerCase().contains(query.toLowerCase())) {
					filteredValues.add(civ);
				}
			}
		}

		return filteredValues;
	}


	public TaEtat getTaEtat() {
		return taEtat;
	}


	public void setTaEtat(TaEtat taEtat) {
		this.taEtat = taEtat;
	}

	public void autcompleteSelection(SelectEvent event) {
		Object value = event.getObject();
		//		FacesMessage msg = new FacesMessage("Selected", "Item:" + value);

		String nomChamp =  (String) event.getComponent().getAttributes().get("nomChamp");
		System.out.println("autcompleteSelection() : "+nomChamp);

		if(value!=null) {
		}
//		validateUIField(nomChamp,value);
	}


//	public boolean isAfficheLigneAvecEtat() {
//		return afficheLigneAvecEtat;
//	}
//
//
//	public void setAfficheLigneAvecEtat(boolean afficheLigneAvecEtat) {
//		this.afficheLigneAvecEtat = afficheLigneAvecEtat;
//	}


//	public LinkedList<TaLigneALigneSupplementDTO> getListeLigneALigneDTOSupplement() {
//		return listeLigneALigneDTOSupplement;
//	}
//
//
//	public void setListeLigneALigneDTOSupplement(LinkedList<TaLigneALigneSupplementDTO> listeLigneALigneDTOSupplement) {
//		this.listeLigneALigneDTOSupplement = listeLigneALigneDTOSupplement;
//	}


	public TaLigneALigneSupplementDTO getSelectedLigneALigneDTOSupplement() {
		return selectedLigneALigneDTOSupplement;
	}


	public void setSelectedLigneALigneDTOSupplement(TaLigneALigneSupplementDTO selectedLigneALigneDTOSupplement) {
		this.selectedLigneALigneDTOSupplement = selectedLigneALigneDTOSupplement;
	}
	
	
	public void actInsererLigneSupplement(ActionEvent actionEvent) {		
		TaLigneALigneSupplementDTO l = new TaLigneALigneSupplementDTO();
		BigDecimal qteRestante=selectedLigneALigneDTO.getDto().getQteRestante().subtract(selectedLigneALigneDTO.getDto().getQteRecue());
		for (TaLigneALigneSupplementDTO li : selectedLigneALigneDTO.getListeSupplement()) {
			qteRestante=qteRestante.subtract(li.getQteRecue());
		}
		if(selectedLigneALigneDTO!=null && selectedLigneALigneDTO.getDto()!=null) {
			l.setId(selectedLigneALigneDTO.getDto().getIdLDocument());
			l.setQteRecue(qteRestante);
			selectedLigneALigneDTO.getListeSupplement().add(l);
//			listeLigneALigneDTOSupplement.add(l);
			selectedLigneALigneDTOSupplement=l;
		}
	}
	
	public void onRowToggle(ToggleEvent event) {
		try {
//			listeLigneALigneDTOSupplement.clear();
			selectedLigneALigneDTO = ((TaLigneALigneDTOJSF) event.getData());
//			for (TaLigneALigneSupplementDTO l : selectedLigneALigneDTO.getListeSupplement()) {
//				listeLigneALigneDTOSupplement.add(l);
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void actReintegrerLigne() {
		TaEtat etat = taEtatService.documentEncours(null);
		if(selectedLigneALigneDTO!=null && selectedLigneALigneDTO.getDto()!=null) {
			try {
				selectedLigneALigneDTO.setTaEtat(etat);
				selectedLigneALigneDTO.getDto().setEtat(etat.getCodeEtat());
				enregistreEtatLigne(selectedLigneALigneDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void actTerminerLigne() {
		TaEtat etat = taEtatService.documentAbandonne(null);
		if(selectedLigneALigneDTO!=null && selectedLigneALigneDTO.getDto()!=null) {
			try {
				selectedLigneALigneDTO.setTaEtat(etat);
				selectedLigneALigneDTO.getDto().setEtat(etat.getCodeEtat());
				enregistreEtatLigne(selectedLigneALigneDTO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void actSupprimerLigneSupplement(ActionEvent actionEvent) {
		selectedLigneALigneDTO.getListeSupplement().remove(selectedLigneALigneDTOSupplement);
		if(selectedLigneALigneDTO.getListeSupplement().size()>0)
			selectedLigneALigneDTOSupplement=selectedLigneALigneDTO.getListeSupplement().get(0);
		else selectedLigneALigneDTOSupplement=null;
	}
	
	public String validateUIField(String nomChamp,Object value) {

		boolean changement=false;

		try {				
			if(nomChamp.equals(Const.C_NUM_LOT)) {
				 //return controleUniciteNumLot((String) value);
			}else
				if(nomChamp.equals(Const.C_GROUPE)) {
					//return controleCoherenceGroupe((String) value);
				}
				else
					if(nomChamp.equals(Const.C_ACCEPTE)) {
						String retour="";
						retour= controleCoherenceGroupe(selectedLigneALigneDTO.getDto().getGroupe());
						if(retour.isEmpty()) {
							retour=controleUniciteNumLot(selectedLigneALigneDTO.getDto().getNumLot());
						}
						return retour;
					}			
	
			return "";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void validateDocument(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		String messageComplet = "";
		try {
			String nomChamp =  (String) component.getAttributes().get("nomChamp");



			//validation automatique via la JSR bean validation
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Set<ConstraintViolation<TaLigneALigneDTO>> violations = factory.getValidator().validateValue(TaLigneALigneDTO.class,nomChamp,value);
			if (violations.size() > 0) {
				messageComplet = "Erreur de validation : ";
				for (ConstraintViolation<TaLigneALigneDTO> cv : violations) {
					messageComplet+=" "+cv.getMessage()+"\n";
				}
				throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
			} else {
				//aucune erreur de validation "automatique", on déclanche les validations du controller
				messageComplet=validateUIField(nomChamp,value);
				if(!messageComplet.isEmpty())
					throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
					
			}
		} catch(Exception e) {
			//messageComplet += e.getMessage();
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, messageComplet, messageComplet));
		}
	}
	
	public String controleUniciteNumLot(String numLot) {
		if(numLot!=null && !numLot.equals("")) {
			for (TaLigneALigneDTOJSF l : listeLigneALigneDTO) {
				if(!selectedLigneALigneDTO.equals(l)) {
					if(!selectedLigneALigneDTO.getDto().equalsGroupe(l.getDto().getGroupe()) || 
							(l.getDto().getGroupe()!=null && l.getDto().getGroupe().equals(""))) {
						if(numLot.equals(l.getDto().getNumLot()))
							return "Le numéro de lot '"+numLot+"' est déjà utilisé !!!";
					}
				}
			}
		}
		return "";
	}
	
	public String controleCoherenceGroupe(String groupe) {
		if(groupe!=null && !groupe.equals("")) {
			for (TaLigneALigneDTOJSF l : listeLigneALigneDTO) {
				if(!selectedLigneALigneDTO.equals(l)) {
					if(selectedLigneALigneDTO.getDto().getGroupe()!=null && !selectedLigneALigneDTO.getDto().getGroupe().equals("")) {
						if(groupe.equals(l.getDto().getGroupe())) {
							//même groupe
							if(!selectedLigneALigneDTO.getDto().getCodeArticle().equals(l.getDto().getCodeArticle()) ||
									!selectedLigneALigneDTO.getDto().getUniteOrigine().equals(l.getDto().getUniteOrigine()))
								return "Dans un même groupe, les codes articles et les unités doivent correspondre !!!";
							if(!selectedLigneALigneDTO.getDto().equalsNumLot(l.getDto().getNumLot()))
								return "Dans un même groupe, le numéro de lot doit être identique !!!";							
						}			
					}
				}
			}
		}
		return "";
	}

	 public void onChecked(SelectEvent evt) throws Exception {
			try {
				enregistreLigne();
			} catch (Exception e) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ligne", e.getMessage()));
				context.validationFailed();
			}
		}

	public boolean disableSiEtat(TaLigneALigneDTOJSF courant) {
		if(courant.getTaEtat()!=null && courant.getTaEtat().getTaTEtat()!=null && !courant.getTaEtat().getTaTEtat().getCodeTEtat().equals(TaEtat.ENCOURS.toUpperCase()))
			return true;
		return false;
	}
	
	public boolean disableSiTermine(TaLigneALigneDTOJSF courant) {
		if(courant.getTaEtat()==null || courant.getTaEtat().getTaTEtat()==null)return true;
		if(courant.getTaEtat()!=null && courant.getTaEtat().getTaTEtat()!=null && !courant.getTaEtat().getTaTEtat().getCodeTEtat().equals(TaEtat.TERMINE.toUpperCase()))
			return true;
		return false;
	}

	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actModifier(ActionEvent actionEvent) {		
			if(!modeEcran.getMode().equals(EnumModeObjet.C_MO_EDITION) && !modeEcran.getMode().equals(EnumModeObjet.C_MO_INSERTION))
				modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void onRowCancel(RowEditEvent event) {
		actAnnuler(null);
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
    }




	



	public TaEtat getSelectionEtat() {
		return selectionEtat;
	}


	public void setSelectionEtat(TaEtat selectionEtat) {
		this.selectionEtat = selectionEtat;
	}


	public String getMessageTerminerLigne() {
		return messageTerminerLigne;
	}


	public void setMessageTerminerLigne(String messageTerminerLigne) {
		this.messageTerminerLigne = messageTerminerLigne;
	}
	
}
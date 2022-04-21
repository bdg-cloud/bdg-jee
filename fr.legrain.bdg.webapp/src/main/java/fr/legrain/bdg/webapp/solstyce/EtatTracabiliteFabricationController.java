package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat; // par Dima
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.dto.ArticleInterfaceDTO;
import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.dto.TracabilitePMDTO;
import fr.legrain.article.model.EditionEtatTracabilite;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLFabricationMP;
import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.article.model.TracabiliteLot;
import fr.legrain.article.model.TracabiliteMP;
import fr.legrain.bdg.article.service.remote.IEditionEtatTracabiliteServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEntrepotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaFabricationServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaGrMouvStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaInventaireServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaUniteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaBonReceptionServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceLocal;
import fr.legrain.bdg.lib.osgi.Const;
import fr.legrain.bdg.model.mapping.mapper.TaFabricationMapper;
import fr.legrain.bdg.webapp.ConstWeb;
import fr.legrain.bdg.webapp.app.EtiquetteCodeBarreBean;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.app.TabListModelBean;
import fr.legrain.document.dto.ITaLFabrication;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.model.TaBonReception;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibDate;
import fr.legrain.lib.data.ModeObjetEcranServeur;
import fr.legrain.stock.dto.GrMouvStockDTO;
import fr.legrain.stock.dto.InventaireDTO;
import fr.legrain.stock.model.TaGrMouvStock;
import fr.legrain.stock.model.TaInventaire;
import fr.legrain.stock.model.TaMouvementStock;


@Named
@ViewScoped  
public class EtatTracabiliteFabricationController implements Serializable {  

	private TaFabrication fabrication;
	
	/** - Dima - Variables - Début - **/
	// --- Variables pour la Recherche d'Etat Traçabilité Fabrication --- //
	private String codeArticle; // code Article
	private String mouvementArticle; // code de mouvement Article
	private String mouvementArticleParDate; // code de mouvement Article
	private String mouvementLot; // numéro de mouvement Lot
	private String codeFabrication; // code Fabrication
	private String codeBR; // code BR
	private String codeInventaire; // code inventaire
	private String pfLibelle; // Produit Final - Libellé
	private String pfNumLot; // Produit Final - Numéro du Lot
	private String mpLibelle; // Matière Première - Libellé
	private String mpNumLot; // Matière Première - Numéro du Lot
	private String brNumLot; // Matière Première - Numéro du Lot
	
	private String familleArticle; // Etat des stocks - famille article
	private String codeEntrepot; // Etat des stocks - code entrepot
	
	private Date dateDebut; // date de Début de la Recherche
	private Date dateFin; // date de Fin de la Recherche
	
	private TaFabrication fabTmp; //variable de tests
	
	private TaBonReception reception; // attente de faire le lien avec la fabrication par les methodes internes
	
	private @EJB IEditionEtatTracabiliteServiceRemote editionsService;
	private @EJB ITaGrMouvStockServiceRemote taGrMouvStockService;	
	private @EJB ITaArticleServiceRemote taArticleService;	
	private GrMouvStockDTO selectedGrMouvStockDTO = new GrMouvStockDTO();
	private TaInfoEntreprise infosEntreprise;
	
	// - Dima - Début
		private String codeLot;
		
		public String getCodeLot() {
			return codeLot;
		}

		public void setCodeLot(String codeLot) {
			this.codeLot = codeLot;
		}
	// - Dima -  Fin
	
	/** - Dima - Variables -  Fin  - **/
	
	@Inject @Named(value="etiquetteCodeBarreBean")
	private EtiquetteCodeBarreBean etiquetteCodeBarreBean;

	private TaLFabricationMP selectedTaMatierePremiere;
	private TaLFabricationMP taMatierePremiereEnModification;
	private TaLFabricationPF selectedTaProduit;
	private TaLFabricationPF taProduitEnModification;
	
	private Boolean selectionneMP;
	private Boolean selectionneP;

	private TaLFabricationMP newTaMatierePremiere;

	private TaLFabricationPF newTaProduit;

	private List<TaLFabricationPF> produits;

	private List<TaFabricationDTO> listeFabrication;
	private TaFabricationDTO selectedTaFabricationDTO;

	@Inject @Named(value="tabListModelCenterBean")
	private TabListModelBean tabsCenter;

	//list permettant de faire la correspondance entre les lots / articles , les entrepot et les emplacement
	private List<ArticleLotEntrepotDTO> listArticleLotEntrepot;
	private ArticleLotEntrepotDTO selectedArticleLotEntrepot;

	//listes deroulantes creation mp
	//valeur de la list déroulante d'article en stock
	private List<String> listArticleMP;

	//listes deroulantes creation pFinis
	//valeur de la list déroulante d'article général
	private List<String> listArticle;
	
	//valeur de la liste deroulante d'entrepot pour Produit
	private List<String> listEntrepotP;

	//valeur de la liste deroulante d'emplacement pour Produit
	private List<String> listEmplacement;

	private String articleUnite1;
	private String articleUnite2;

	//variable de stokage de l'entrepot selectionné lors de la création d'une nouvelle matière premiere ou d'un nouveau produit
	private String entrepot;
	//variable de stokage de l'article selectionné lors de la création d'une nouvelle matière premiere ou d'un nouveau produit
	private String article;
	//variable de stokage de l'article selectionné lors de la création d'un nouveau produit
	private String articlePF;
	//variable de stokage du lot selectionné lors de la création d'une nouvelle matière premiere ou d'un nouveau produit
	private String lot;
	//variable de stokage de la quantité saisie lors de la création d'une nouvelle matière premiere ou d'un nouveau produit
	private String commentaireLot;
	private BigDecimal qte1 = new BigDecimal(0);
	//variable de stokage de l'emplacement selectionné lors de la création d'une nouvelle matière premiere ou d'un nouveau produit
	private String emplacement;

	//code de la fabrication en cas d'acces en modification
//	private String codeFabrication;
	//etat réel de la fabrication
	private Boolean fabricationDemarree;
	private Boolean fabricationTerminee;
	
	
	// element vide pour les listes
	TaLotDTO lotVide = new TaLotDTO();
	ArticleInterfaceDTO articleVide = new ArticleInterfaceDTO();
	TaEntrepotDTO entrepotVide = new TaEntrepotDTO();

	protected ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranMatierePremiere = new ModeObjetEcranServeur();
	protected ModeObjetEcranServeur modeEcranProduit = new ModeObjetEcranServeur();

	
	private @EJB ITaInfoEntrepriseServiceLocal infoEntrepriseService;
	private @EJB ITaFabricationServiceRemote fabricationService;
	private @EJB ITaInventaireServiceRemote inventaireService;
	private @EJB ITaLotServiceRemote lotService;
	private @EJB ITaMouvementStockServiceRemote mouvementService;
	private @EJB ITaEntrepotServiceRemote entrepotService;
	private @EJB ITaArticleServiceRemote articleService;
	private @EJB ITaTypeMouvementServiceRemote typeMouvementService;
	private @EJB ITaUniteServiceRemote uniteService;
	private @EJB ITaTLigneServiceRemote taTLigneService;
	private @EJB ITaBonReceptionServiceRemote bonReceptionService;
		
	public EtatTracabiliteFabricationController() {  

		
	}  

	@PostConstruct
	public void postConstruct(){

//		refresh();
//
//		selectionneMP = false;
//		selectionneP = false;
//
//		if(codeFabrication != null){
//			articleUnite1= "";
//			this.getValues(codeFabrication);
//		} else {
//			this.getValues(null);
//		}
//		//actInserer();
		dateDebut=new Date();
		dateFin=new Date();
		
		infosEntreprise=infoEntrepriseService.findInstance();
		if(infosEntreprise!=null){
			dateDebut=infosEntreprise.getDatedebInfoEntreprise();
			dateFin=infosEntreprise.getDatefinInfoEntreprise();
		}
	}

	public void refresh(){
		listeFabrication = fabricationService.selectAllDTO();
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public boolean pasDejaOuvert() {
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FABRICATION);
		return tabsCenter.ongletDejaOuvert(b)==null;
	} 

	public void nouveau(ActionEvent actionEvent) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FABRICATION);
		b.setTitre("Fabrication");
		//b.setStyle(LgrTab.CSS_CLASS_TAB_ARTICLE);
		b.setTemplate("solstyce/fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_FABRICATION);
		b.setParamId("0");

		tabsCenter.ajouterOnglet(b);
		//actInserer(actionEvent);
		actInserer();

		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", 
					"Nouveau"
					)); 
		}
	} 
	
	public boolean etatBouton(String bouton) {
		return etatBouton(bouton,modeEcran);
	}
	
	public boolean etatBoutonMatierePremiere(String bouton) {
		return etatBouton(bouton,modeEcranMatierePremiere);
	}
	
	public boolean etatBoutonProduit(String bouton) {
		return etatBouton(bouton,modeEcranProduit);
	}
	
	public boolean etatBouton(String bouton, ModeObjetEcranServeur mode) {
		boolean retour = true;
		switch (mode.getMode()) {
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

	//************************************************************************************************************//
	//	Fabrication
	//************************************************************************************************************//
	public void actEnregistrer(){  
		try {
			if(fabrication.getDateFinR()==null) {
				fabrication.setDateFinR(fabrication.getDateDebR());
			}
			if(fabrication.getDateDebT()==null) {
				fabrication.setDateDebT(fabrication.getDateDebR());
			}
			if(fabrication.getDateFinT()==null) {
				fabrication.setDateFinT(fabrication.getDateDebR());
			}
			fabrication.getTaGrMouvStock().setCodeGrMouvStock(fabrication.getCodeDocument());
			fabrication.getTaGrMouvStock().setDateGrMouvStock(fabrication.getDateDebR());
			fabrication.getTaGrMouvStock().setLibelleGrMouvStock(fabrication.getLibelleDocument());
			//fusionner les 2 listes dans l'entité en évitant les doublons
			for (ITaLFabrication mp : fabrication.getLignesMatierePremieres()) {
				if(mp.getQteLDocument()!=null)mp.getTaMouvementStock().setQte1Stock(mp.getQteLDocument().abs().negate());
				if(mp.getQte2LDocument()!=null)mp.getTaMouvementStock().setQte2Stock(mp.getQte2LDocument().abs().negate());
				mp.getTaMouvementStock().setTaLot(null);
				fabrication.getLignes().add(mp);
			}
			for (ITaLFabrication p : fabrication.getLignesProduits()) {
				if(p.getQteLDocument()!=null)p.getTaMouvementStock().setQte1Stock(p.getQteLDocument().abs());
				if(p.getQte2LDocument()!=null)p.getTaMouvementStock().setQte2Stock(p.getQte2LDocument().abs());
				p.getTaMouvementStock().setTaLot(null);;
				fabrication.getLignes().add(p);
			}
			for (ITaLFabrication l : fabrication.getLignes()) {
				l.setTaTLigne(taTLigneService.findByCode(Const.C_CODE_T_LIGNE_H));
			}
			
			fabrication = fabricationService.merge(fabrication, ITaFabricationServiceRemote.validationContext) ;
			
			if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_INSERTION)==0) {
				listeFabrication.add(new TaFabricationMapper().mapEntityToDto(fabrication, null));
			}
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actAnnuler() {
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actFermer() {
		
	}

	public void actInserer() {
		fabricationDemarree = new Boolean(true);
		fabrication.setDateDebR(new Date());
		fabrication.setCodeDocument(LibDate.dateToStringTimeStamp(new Date(), "", "", ""));
		
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}
	
	public void actModifier() {
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}
	
	public void actSupprimer(ActionEvent actionEvent) {
		try {
			if(selectedTaFabricationDTO!=null && selectedTaFabricationDTO.getId()!=null){
				fabrication = fabricationService.findById(selectedTaFabricationDTO.getId());
			}
			fabrication.findProduit();
			for (ITaLFabrication ligne : fabrication.getLignesMatierePremieres()) {
				ligne.setTaLot(null);
			}
			
			fabricationService.remove(fabrication);
			listeFabrication.remove(selectedTaFabricationDTO);

			if(!listeFabrication.isEmpty()) {
				selectedTaFabricationDTO = listeFabrication.get(0);
			}

			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);

			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Bon de réception", "actSupprimer"));
			}
		} catch (RemoveException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}	    
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimer")); 
		}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			sessionMap.put("fabrication", fabricationService.findAvecResultatConformites(selectedTaFabricationDTO.getId()));

			System.out.println("BonReceptionController.actImprimer()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}    
	
	public void actImprimerEtiquetteCB(ActionEvent actionEvent) {
		
		etiquetteCodeBarreBean.videListe();
		try {
			for (ITaLFabrication l : fabrication.getLignesProduits()) {
				etiquetteCodeBarreBean.getListeArticle().add(l.getTaArticle());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
		if(ConstWeb.DEBUG) {
		   	FacesContext context = FacesContext.getCurrentInstance();  
		    context.addMessage(null, new FacesMessage("Bon de réception", "actImprimerEtiquetteCB")); 
		}
	}

	public void terminer() {
		fabricationTerminee = new Boolean(true);
		fabrication.setDateFinR(new Date());

	}

	//************************************************************************************************************//
	//	Matière Premiere
	//************************************************************************************************************//
	public void selectionLigneMP() {
		selectionneMP = true;
	}

	public void getValues(String code){  
		if(code != null){
			try {
				fabrication  = fabricationService.findByCodeWithList(code);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			fabrication = new TaFabrication();
			fabrication.setTaGrMouvStock(new TaGrMouvStock());
			try {
				fabrication.getTaGrMouvStock().setTaTypeMouvStock(typeMouvementService.findByCode("F"));
				fabrication.getTaGrMouvStock().setCodeGrMouvStock(fabrication.getCodeDocument());
			} catch (FinderException e) {
				e.printStackTrace();
			}

			fabrication.setLignesMatierePremieres(new ArrayList<TaLFabricationMP>());
			fabrication.setLignesProduits(new ArrayList<TaLFabricationPF>());

		}

		if(fabrication.getDateDebR() != null ){
			fabricationDemarree = new Boolean(true);
		} else{
			fabricationDemarree = new Boolean(false);
		}

		if(fabrication.getDateFinR() != null){
			fabricationTerminee = new Boolean(true);
		} else {
			fabricationTerminee = new Boolean(false);
		}

		listArticleMP = mouvementService.articleEnStock();
//		listArticle = mouvementService.articleEnStock();
		listArticle=new LinkedList<String>();
		List<TaArticle>liste= articleService.selectAll();
		for (TaArticle taArticle : liste) {
			listArticle.add(taArticle.getCodeArticle()+ "~" +taArticle.getLibellecArticle());
		}
		List<TaEntrepot> listEntrepotTemp = entrepotService.selectAll();

		listEntrepotP = new ArrayList<String>();
		try {
			for (TaEntrepot e : listEntrepotTemp){
				listEntrepotP.add(e.getCodeEntrepot() + "~" + e.getLibelle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onChangeListArticleMP() {
		try {
		listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
		listArticleLotEntrepot = mouvementService.getEtatStockByArticle((article.split("~"))[0], null);
		List<ArticleLotEntrepotDTO> temp= new ArrayList<ArticleLotEntrepotDTO>();
		List<BigDecimal> qte = new ArrayList<BigDecimal>();
		for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){

			for(ITaLFabrication mp : fabrication.getLignesMatierePremieres()){
				TaMouvementStock mouv = mp.getTaMouvementStock();

//					TaLot lot =lotService.findByCode(mouv.getNumLot());
//					mouv.setTaLot(lot);
				if(mouv.getEmplacement().equals(dto.getEmplacement()) && mouv.getTaEntrepot().getCodeEntrepot().equals(dto.getCodeEntrepot() ) &&
						( mouv.getTaLot().getTaArticle().getCodeArticle()).equals((article.split("~"))[0])  &&
						((Integer)mouv.getTaLot().getIdLot()).equals(dto.getIdLot())&& dto.getCodeUnite()!=null && (mouv.getUn1Stock()).equals(dto.getCodeUnite())){
					temp.add(dto);
					qte.add(mouv.getQte1Stock()) ;
				}
			}
		}

		int i = 0;
		for(ArticleLotEntrepotDTO dtoTemp: temp){
			listArticleLotEntrepot.remove(dtoTemp);
			dtoTemp.setQte1(dtoTemp.getQte1().subtract(qte.get(i)));
			i++;
			listArticleLotEntrepot.add(dtoTemp);

		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actEnregistrerMatierePremiere() {

		try {
			//On recupere le lot article selectionné et on l'affecte au mouvement generé par notre nouvelle matiere premiere

			for(ArticleLotEntrepotDTO articleLotEntrepotDTO : listArticleLotEntrepot){
				if(articleLotEntrepotDTO.getQteSelec() != null && articleLotEntrepotDTO.getQte1().signum()!=0){
					TaRapportUnite rapport=null;
					TaLFabricationMP ligneFab = new TaLFabricationMP();
					TaMouvementStock mouv = new TaMouvementStock();
					mouv.setDateStock(new Date());
					mouv.setQte1Stock(articleLotEntrepotDTO.getQteSelec());
					mouv.setEmplacement(articleLotEntrepotDTO.getEmplacement());
					
					ligneFab.setTaArticle(articleService.findByCode(article.split("~")[0]));
					rapport=ligneFab.getTaArticle().getTaRapportUnite();
//					if(rapport==null && ligneFab.getTaArticle().getTaPrix()!=null && ligneFab.getTaArticle().getTaUnite1()!=null){
//						for (TaRapportUnite obj : ligneFab.getTaArticle().getTaRapportUnites()) {
//							if(obj.getTaUnite1()!=null && obj.getTaUnite1().equals(ligneFab.getTaArticle().getTaUnite1()))
//								rapport=obj;
//						}						
//					}
					if(rapport!=null){// on rempli l'unité 2 et Qté 2 à partir du rapport
							switch (rapport.getSens()) {
							case 1:
								ligneFab.setQte2LDocument(articleLotEntrepotDTO.getQteSelec().multiply(rapport.getRapport()));
								mouv.setQte2Stock(ligneFab.getQte2LDocument());
								if(rapport.getTaUnite2()!=null){
									ligneFab.setU2LDocument(rapport.getTaUnite2().getCodeUnite());
									mouv.setUn2Stock(rapport.getTaUnite2().getCodeUnite());
								}
								break;
							case 0:
								ligneFab.setQte2LDocument(articleLotEntrepotDTO.getQteSelec().divide(rapport.getRapport(),MathContext.DECIMAL128).setScale(rapport.getNbDecimal(),BigDecimal.ROUND_HALF_UP));
								mouv.setQte2Stock(ligneFab.getQte2LDocument());
								if(rapport.getTaUnite2()!=null){
									ligneFab.setU2LDocument(rapport.getTaUnite2().getCodeUnite());
									mouv.setUn2Stock(rapport.getTaUnite2().getCodeUnite());
								}
								break;
							default:
								break;
							} 
						
					}
					ligneFab.setQteLDocument(articleLotEntrepotDTO.getQteSelec());

					//mouv.setType(typeMouvementService.findById(5)); //==> qte en négatif et Type Fabrication sur le groupe de mouvement
					mouv.setTaGrMouvStock(fabrication.getTaGrMouvStock());
					
					mouv.setQte1Stock(articleLotEntrepotDTO.getQteSelec());
					
					mouv.setTaLot(lotService.findByCodeAAndNumLot(article.split("~")[0], articleLotEntrepotDTO.getNumLot()));
					ligneFab.setTaLot(mouv.getTaLot());
//					mouv.setNumLot(mouv.getTaLot().getNumLot());
//					mouv.setTaLot(null);
					
					mouv.setTaEntrepot(entrepotService.findByCode(articleLotEntrepotDTO.getCodeEntrepot()));
					ligneFab.setTaEntrepot(mouv.getTaEntrepot());
					
					mouv.setUn1Stock(articleLotEntrepotDTO.getCodeUnite());

					ligneFab.setTaMouvementStock(mouv);
					ligneFab.setTaDocument(fabrication);
					fabrication.getLignesMatierePremieres().add(ligneFab);
				}

			}

			listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
			article = "";

			taMatierePremiereEnModification = new TaLFabricationMP();
			
			modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);

		} catch (FinderException e) {

			e.printStackTrace();
		}

	}

	public void actSupprimerMatierePremiere() {
		List<TaLFabricationMP> l = fabrication.getLignesMatierePremieres();
		Boolean trouve = l.remove(selectedTaMatierePremiere);
		if(trouve){
			selectedTaMatierePremiere.setTaDocument(null);
			fabrication.setLignesMatierePremieres(l);
			selectionneMP = false;
		}
	}

	public void actInsererMatierePremiere(ActionEvent actionEvent) {
		newTaMatierePremiere = new TaLFabricationMP();
		newTaMatierePremiere.setTaMouvementStock(new TaMouvementStock());

		initOrigineMouvement(newTaMatierePremiere);
		
		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void initOrigineMouvement(ITaLFabrication m) {
		m.getTaMouvementStock().setTaGrMouvStock(fabrication.getTaGrMouvStock());
	}

	public void actModifierMatierePremiere () {
		
		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_EDITION);
		
		taMatierePremiereEnModification = selectedTaMatierePremiere;
		article = selectedTaMatierePremiere.getTaLot().getTaArticle().getCodeArticle() + "~" + selectedTaMatierePremiere.getTaLot().getTaArticle().getLibellecArticle();
		listArticleLotEntrepot = new ArrayList<ArticleLotEntrepotDTO>();
		listArticleLotEntrepot = mouvementService.getEtatStockByArticle((article.split("~"))[0], null);
		List<ArticleLotEntrepotDTO> temp= new ArrayList<ArticleLotEntrepotDTO>();
		List<BigDecimal> qte = new ArrayList<BigDecimal>();
		for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){

			for(ITaLFabrication mp : fabrication.getLignesMatierePremieres()){
				if (mp.getTaMouvementStock().getIdMouvementStock() != selectedTaMatierePremiere.getTaMouvementStock().getIdMouvementStock()){
					TaMouvementStock mouv = mp.getTaMouvementStock();
					if(mouv.getEmplacement().equals(dto.getEmplacement()) && mouv.getTaEntrepot().getCodeEntrepot().equals(dto.getCodeEntrepot() ) &&
							( mouv.getTaLot().getTaArticle().getCodeArticle()).equals((article.split("~"))[0])  &&
							((Integer)mouv.getTaLot().getIdLot()).equals(dto.getIdLot()) && (mouv.getUn1Stock()).equals(dto.getCodeUnite())){
						temp.add(dto);
						qte.add(mouv.getQte1Stock()) ;
					}
				}
			}

		}

		ITaLFabrication mouv = selectedTaMatierePremiere;
		for(ArticleLotEntrepotDTO dto: listArticleLotEntrepot){
			if(mouv.getTaMouvementStock().getEmplacement().equals(dto.getEmplacement()) && mouv.getTaEntrepot().getCodeEntrepot().equals(dto.getCodeEntrepot() ) &&
					( mouv.getTaLot().getTaArticle().getCodeArticle()).equals((article.split("~"))[0])  &&
					((Integer)mouv.getTaLot().getIdLot()).equals(dto.getIdLot()) && (mouv.getTaMouvementStock().getUn1Stock()).equals(dto.getCodeUnite())){
				listArticleLotEntrepot.remove(dto);
				dto.setQteSelec(mouv.getTaMouvementStock().getQte1Stock());
				listArticleLotEntrepot.add(dto);
			}
		}

		actSupprimerMatierePremiere();
		selectedTaMatierePremiere = new TaLFabricationMP();
	}

	public void actAnnulerMatierePremiere(){
		List<TaLFabricationMP> lmp = fabrication.getLignesMatierePremieres();
		if(taMatierePremiereEnModification == null || taMatierePremiereEnModification.getTaMouvementStock().getIdMouvementStock() != 0){
			lmp.add(taMatierePremiereEnModification);
		}

		fabrication.setLignesMatierePremieres(lmp);
		taMatierePremiereEnModification = new TaLFabricationMP();
		
		modeEcranMatierePremiere.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	//************************************************************************************************************//
	//	Produit
	//************************************************************************************************************//

	public void selectionLigneP() {
		selectionneP = true;
	}

	public void onChangeListArticleP() {
		TaArticle art = null;
		try {
			if(articlePF!=null && !articlePF.equals("")){
				art = articleService.findByCode((articlePF.split("~"))[0]);

				if(art!=null && art.getTaRapportUnite()!=null) {
					articleUnite1 = art.getTaRapportUnite().getTaUnite1().getCodeUnite();
				} else {
					//pas d'unité
					articleUnite1= "";
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fabrication", 
							"Cet article ne possède pas d'unité"
							)); 
				}
			} else {
				articleUnite1= "";
			}
		} catch (FinderException e) {
			e.printStackTrace();

		}
	}

	public void onChangeListEntrepotP() {
		listEmplacement = new ArrayList<String>();
		listEmplacement.clear();
		listEmplacement = mouvementService.emplacementParEntrepot((entrepot.split("~"))[0]);
	}

	public void actModifierProduit () {
		
		modeEcranProduit.setMode(EnumModeObjet.C_MO_EDITION);
		
		//TODO mettre a jour
		taProduitEnModification = selectedTaProduit;
		articlePF = selectedTaProduit.getTaLot().getTaArticle().getCodeArticle() + "~" + selectedTaProduit.getTaLot().getTaArticle().getLibellecArticle();

		TaMouvementStock mouv = selectedTaProduit.getTaMouvementStock();

		commentaireLot = mouv.getDescription();
		lot = mouv.getTaLot().getNumLot().substring(fabrication.getCodeDocument().length());
		qte1 = mouv.getQte1Stock();
		articleUnite1 = mouv.getUn1Stock();
		entrepot = mouv.getTaEntrepot().getCodeEntrepot()+"~"+mouv.getTaEntrepot().getLibelle();
		emplacement = mouv.getEmplacement();
		actSupprimerProduit();
		selectedTaProduit = new TaLFabricationPF();
	}

	public void actSupprimerProduit () {
		List<TaLFabricationPF> l = fabrication.getLignesProduits();
		Boolean trouve = l.remove(selectedTaProduit);
		if(trouve){
			selectedTaProduit.setTaDocument(null);
			fabrication.setLignesProduits(l);
			selectionneP = false;
		}
	}

	public void actEnregistrerProduit() {	
		newTaProduit = new TaLFabricationPF();
		TaMouvementStock mouv = new TaMouvementStock();
		mouv.setDateStock(new Date());
		mouv.setQte1Stock(qte1);
		mouv.setEmplacement(emplacement);
		TaRapportUnite rapport=null;
		try {
			//mouv.setType(typeMouvementService.findById(5)); //==> qte en positif et Type Fabrication sur le groupe de mouvement
			mouv.setTaGrMouvStock(fabrication.getTaGrMouvStock());

			TaLot lotp = new TaLot();
			if(lot == null || lot.isEmpty()){
				lot = String.valueOf(fabrication.getLignesProduits().size() + 1);
			}
			lotp.setNumLot(fabrication.getCodeDocument() + lot);
			lotp.setTaArticle(articleService.findByCode(articlePF.split("~")[0]));
			
			
			rapport=lotp.getTaArticle().getTaRapportUnite();
			
			newTaProduit.setTaLot(lotp);
			newTaProduit.setTaArticle(lotp.getTaArticle());

			mouv.setTaLot(lotp);
//			mouv.setNumLot(lotp.getNumLot());
			mouv.setTaEntrepot(entrepotService.findByCode(entrepot.split("~")[0]));
			newTaProduit.setTaEntrepot(mouv.getTaEntrepot());
			newTaProduit.setEmplacementLDocument(mouv.getEmplacement());
			newTaProduit.setQteLDocument(mouv.getQte1Stock());
			
			if(rapport!=null) {
				if(rapport.getTaUnite1()!=null)mouv.setUn1Stock(rapport.getTaUnite1().getCodeUnite());
			} else {
				mouv.setUn1Stock(null);
				mouv.setUn2Stock(null);
			}
			mouv.setDescription(commentaireLot);
			
			if(rapport==null && newTaProduit.getTaArticle().getTaPrix()!=null && newTaProduit.getTaArticle().getTaUnite1()!=null){
				for (TaRapportUnite obj : newTaProduit.getTaArticle().getTaRapportUnites()) {
					if(obj.getTaUnite1()!=null && obj.getTaUnite1().equals(newTaProduit.getTaArticle().getTaUnite1()))
						rapport=obj;
				}						
			}
			if(rapport!=null){// on rempli l'unité 2 et Qté 2 à partir du rapport
					switch (rapport.getSens()) {
					case 1:
						newTaProduit.setQte2LDocument(qte1.multiply(rapport.getRapport()));
						mouv.setQte2Stock(newTaProduit.getQte2LDocument());
						if(rapport.getTaUnite2()!=null){
							newTaProduit.setU2LDocument(rapport.getTaUnite2().getCodeUnite());
							mouv.setUn2Stock(rapport.getTaUnite2().getCodeUnite());
						}
						break;
					case 0:
						newTaProduit.setQte2LDocument(qte1.divide(rapport.getRapport(),MathContext.DECIMAL128).setScale(rapport.getNbDecimal(),BigDecimal.ROUND_HALF_UP));
						mouv.setQte2Stock(newTaProduit.getQte2LDocument());
						if(rapport.getTaUnite2()!=null){
							newTaProduit.setU2LDocument(rapport.getTaUnite2().getCodeUnite());
							mouv.setUn2Stock(rapport.getTaUnite2().getCodeUnite());
						}
						break;
					default:
						break;
					} 
					lotp.setRapport(rapport.getRapport());
					lotp.setNbDecimal(rapport.getNbDecimal());
					lotp.setSens(rapport.getSens());

			}
			lotp.setUnite1(mouv.getUn1Stock());
			lotp.setUnite2(mouv.getUn2Stock());
		} catch (FinderException e) {

			e.printStackTrace();
		}

		initOrigineMouvement(newTaProduit);

		
		if (fabrication.getLignesProduits()==null){
			fabrication.setLignesProduits(new ArrayList<TaLFabricationPF>());

		}
		newTaProduit.setTaDocument(fabrication);
		newTaProduit.setTaMouvementStock(mouv);
		fabrication.getLignesProduits().add(newTaProduit);

		articlePF = "";
		emplacement = "";
		lot = "";
		entrepot = new String();
		qte1 = new BigDecimal(0);
		articleUnite1= "";
		
		modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void actInsererProduit(ActionEvent actionEvent) {
		newTaProduit = new TaLFabricationPF();

		initOrigineMouvement(newTaProduit);
		if(fabrication.getLignesProduits()!=null) {
			this.setLot("_"+fabrication.getLignesProduits().size()+1);
		} else {
			this.setLot("_1");
		}
		
		modeEcranProduit.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	
	public void actAnnulerProduit () {
		
		List<TaLFabricationPF> lmp = fabrication.getLignesProduits();
		if(taProduitEnModification == null || taProduitEnModification.getTaMouvementStock().getIdMouvementStock() != 0){
			lmp.add(taProduitEnModification);
		}

		fabrication.setLignesProduits(lmp);
		taProduitEnModification = new TaLFabricationPF();
		
		modeEcranProduit.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}

	public void supprimer(ActionEvent actionEvent) {
//		actSupprimer(actionEvent);
	}
	
	public void detail(ActionEvent actionEvent) {
//		onRowSelect(null);
	}

	public void onRowSimpleSelect(SelectEvent event) {

		try {
			if(pasDejaOuvert()==false){
				onRowSelect(event);
	
//				autoCompleteMapDTOtoUI();
//				valuesLigne = IHMmodel();
	
				if(ConstWeb.DEBUG) {
					FacesContext context = FacesContext.getCurrentInstance();  
					context.addMessage(null, new FacesMessage("Fabrication", 
							"Chargement de fabrication N°"+selectedTaFabricationDTO.getCodeDocument()
							)); 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	public void onRowSelect(SelectEvent event) {  
		LgrTab b = new LgrTab();
		b.setTypeOnglet(LgrTab.TYPE_TAB_FABRICATION);
		b.setTitre("Fabrication");
		b.setTemplate("solstyce/fabrication.xhtml");
		b.setIdTab(LgrTab.ID_TAB_TYPE_FABRICATION);
		tabsCenter.ajouterOnglet(b);

		try {
			
//			autoCompleteMapDTOtoUI();
			
			fabrication = fabricationService.findById(selectedTaFabricationDTO.getId());
			//séparer en 2 listes dans l'entité les lignes de fabrication
			fabrication.findProduit();
			
//			masterEntity = taBonReceptionService.findById(selectedTaBonReceptionDTO.getId());
//			valuesLigne = IHMmodel();
	
			if(ConstWeb.DEBUG) {
				FacesContext context = FacesContext.getCurrentInstance();  
				context.addMessage(null, new FacesMessage("Fabrication", 
						"Chargement de fabrication N°"+selectedTaFabricationDTO.getCodeDocument()
						)); 
			}
		
		} catch (FinderException e) {
			e.printStackTrace();
		}
	} 
	//************************************************************************************************************//
	//	Commun
	//************************************************************************************************************//

	public List<TaLFabricationPF> getProduits() {
		return produits;
	}

	public void setProduits(List<TaLFabricationPF> produits) {
		this.produits = produits;
	}

	public void setFabrication(TaFabrication fabrication) {
		this.fabrication = fabrication;
	}

	public void setSelectedTaMatierePremiere(
			TaLFabricationMP selectedTaMatierePremiere) {
		this.selectedTaMatierePremiere = selectedTaMatierePremiere;
	}

	public void setSelectedTaProduit(TaLFabricationPF selectedTaProduit) {
		this.selectedTaProduit = selectedTaProduit;
	}

	public void setNewTaMatierePremiere(TaLFabricationMP newTaMatierePremiere) {
		this.newTaMatierePremiere = newTaMatierePremiere;
	}

	public void setNewTaProduit(TaLFabricationPF newTaProduit) {
		this.newTaProduit = newTaProduit;
	}

	public Boolean getFabricationDemarree() {
		return fabricationDemarree;
	}

	public void setFabricationDemarree(Boolean fabricationDemarree) {
		this.fabricationDemarree = fabricationDemarree;
	}

	public Boolean getFabricationTerminee() {
		return fabricationTerminee;
	}

	public void setFabricationTerminee(Boolean fabricationTerminee) {
		this.fabricationTerminee = fabricationTerminee;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public List<String> getListArticleMP() {
		return listArticleMP;
	}

	public void setListArticleMP(List<String> listArticle) {
		this.listArticleMP = listArticle;
	}

	public String getEntrepot() {
		return entrepot;
	}

	public void setEntrepot(String entrepot) {
		this.entrepot = entrepot;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public String getCodeFabrication() {
		return codeFabrication;
	}

	public void setCodeFabrication(String codeFabrication) {
		this.codeFabrication = codeFabrication;
	}

	public TaFabrication getFabrication() {
		return fabrication;
	}

	public TaLFabricationMP getSelectedTaMatierePremiere() {
		return selectedTaMatierePremiere;
	}

	public TaLFabricationPF getSelectedTaProduit() {
		return selectedTaProduit;
	}

	public TaLFabricationMP getNewTaMatierePremiere() {
		return newTaMatierePremiere;
	}

	public TaLFabricationPF getNewTaProduit() {
		return newTaProduit;
	}

	public List<ArticleLotEntrepotDTO> getListArticleLotEntrepot() {
		return listArticleLotEntrepot;
	}

	public void setListArticleLotEntrepot(List<ArticleLotEntrepotDTO> listArticleLotEntrepot) {
		this.listArticleLotEntrepot = listArticleLotEntrepot;
	}

	public List<String> getListEmplacement() {
		return listEmplacement;
	}

	public void setListEmplacement(List<String> listEmplacement) {
		this.listEmplacement = listEmplacement;
	}

	public String getArticleUnite1() {
		return articleUnite1;
	}

	public void setArticleUnite1(String articleUnite1) {
		this.articleUnite1 = articleUnite1;
	}

	public String getArticleUnite2() {
		return articleUnite2;
	}

	public void setArticleUnite2(String articleUnite2) {
		this.articleUnite2 = articleUnite2;
	}

	public List<String> getListEntrepotP() {
		return listEntrepotP;
	}

	public void setListEntrepotP(List<String> listEntrepotP) {
		this.listEntrepotP = listEntrepotP;
	}

	public BigDecimal getQte1() {
		return qte1;
	}

	public void setQte1(BigDecimal qte1) {
		this.qte1 = qte1;
	}

	public String getEmplacement() {
		return emplacement;
	}

	public void setEmplacement(String emplacement) {
		this.emplacement = emplacement;
	}

	public TaLFabricationMP getTaMatierePremiereEnModification() {
		return taMatierePremiereEnModification;
	}

	public void setTaMatierePremiereEnModification(
			TaLFabricationMP taMatierePremiereEnModification) {
		this.taMatierePremiereEnModification = taMatierePremiereEnModification;
	}

	public TaLFabricationPF getTaProduitEnModification() {
		return taProduitEnModification;
	}

	public void setTaProduitEnModification(TaLFabricationPF taProduitEnModification) {
		this.taProduitEnModification = taProduitEnModification;
	}

	public String getCommentaireLot() {
		return commentaireLot;
	}

	public void setCommentaireLot(String commentaireLot) {
		this.commentaireLot = commentaireLot;
	}

	public Boolean getSelectionneMP() {
		return selectionneMP;
	}

	public void setSelectionneMP(Boolean selectionne) {
		this.selectionneMP = selectionne;
	}

	public Boolean getSelectionneP() {
		return selectionneP;
	}

	public void setSelectionneP(Boolean selectionneP) {
		this.selectionneP = selectionneP;
	}

	public List<TaFabricationDTO> getListeFabrication() {
		return listeFabrication;
	}

	public TaFabricationDTO getSelectedTaFabricationDTO() {
		return selectedTaFabricationDTO;
	}

	public void setSelectedTaFabricationDTO(
			TaFabricationDTO selectedTaFabricationDTO) {
		this.selectedTaFabricationDTO = selectedTaFabricationDTO;
	}

	public TabListModelBean getTabsCenter() {
		return tabsCenter;
	}

	public void setTabsCenter(TabListModelBean tabsCenter) {
		this.tabsCenter = tabsCenter;
	}

	public ModeObjetEcranServeur getModeEcranMatierePremiere() {
		return modeEcranMatierePremiere;
	}

	public ModeObjetEcranServeur getModeEcranProduit() {
		return modeEcranProduit;
	}

	public ArticleLotEntrepotDTO getSelectedArticleLotEntrepot() {
		return selectedArticleLotEntrepot;
	}

	public void setSelectedArticleLotEntrepot(
			ArticleLotEntrepotDTO selectedArticleLotEntrepot) {
		this.selectedArticleLotEntrepot = selectedArticleLotEntrepot;
	}

	public ModeObjetEcranServeur getModeEcran() {
		return modeEcran;
	}

	public void setModeEcran(ModeObjetEcranServeur modeEcran) {
		this.modeEcran = modeEcran;
	}

	public List<String> getListArticle() {
		return listArticle;
	}

	public void setListArticle(List<String> listArticle) {
		this.listArticle = listArticle;
	}

	public String getArticlePF() {
		return articlePF;
	}

	public void setArticlePF(String articlePF) {
		this.articlePF = articlePF;
	}

	public EtiquetteCodeBarreBean getEtiquetteCodeBarreBean() {
		return etiquetteCodeBarreBean;
	}

	public void setEtiquetteCodeBarreBean(
			EtiquetteCodeBarreBean etiquetteCodeBarreBean) {
		this.etiquetteCodeBarreBean = etiquetteCodeBarreBean;
	}
	
	/** - Dima - Début - **/
	
	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public String getPfLibelle() {
		return pfLibelle;
	}

	public void setPfLibelle(String pfLibelle) {
		this.pfLibelle = pfLibelle;
	}

	public String getPfNumLot() {
		return pfNumLot;
	}

	public void setPfNumLot(String pfNumLot) {
		this.pfNumLot = pfNumLot;
	}

	public String getMpLibelle() {
		return mpLibelle;
	}

	public void setMpLibelle(String mpLibelle) {
		this.mpLibelle = mpLibelle;
	}

	public String getMpNumLot() {
		return mpNumLot;
	}

	public void setMpNumLot(String mpNumLot) {
		this.mpNumLot = mpNumLot;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public TaBonReception getReception() {
		return reception;
	}

	public void setReception(TaBonReception reception) {
		this.reception = reception;
	}

	public List<String> completeText(String query) {
        List<String> results = new ArrayList<String>();
        for(int i = 0; i < 10; i++) {
            results.add(query + i);
        }
         
        return results;
    }
	
	public void buttonAction(ActionEvent actionEvent) {
//        addMessage(actionEvent.getSource().toString());
		addMessage("Impression en cours...");
        
    }
     
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary,  null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }
    
    public void click() {
         
        PrimeFaces.current().ajax().update("form:display");
        PrimeFaces.current().executeScript("PF('dlg').show()");
    }
   

    
    public String getMouvementArticle() {
		return mouvementArticle;
	}

	public void setMouvementArticle(String mouvementArticle) {
		this.mouvementArticle = mouvementArticle;
	}

	public String getMouvementLot() {
		return mouvementLot;
	}

	public void setMouvementLot(String mouvementLot) {
		this.mouvementLot = mouvementLot;
	}

	public List<String> completeTextCodeArticle(String query) {
        List<String> filteredValues = new ArrayList<String>();
        List<TaArticleDTO> allBR = articleService.selectAllInReception();
        for (TaArticleDTO br : allBR){
        		if (query.equals("*") || (br.getCodeArticle().toLowerCase().contains(query.toLowerCase()))){
//        			filteredValues.add(br.getCodeArticle()+"-"+br.getLibellecArticle());
        			filteredValues.add(br.getCodeArticle());
//        			filteredValues.add(br);
        		}
        	
        }        
//        List<TaBonReception> allBR = bonReceptionService.selectAll();
//        for (TaBonReception br : allBR){
//        	for (TaLBonReception lbr : br.getLignes()){
//        		if (query.equals("*") || (lbr.getTaArticle().getCodeArticle().toLowerCase().contains(query.toLowerCase())
//        				&& !filteredValues.contains(lbr.getTaArticle().getCodeArticle()))){
//        			filteredValues.add(lbr.getTaArticle().getCodeArticle());
//        		}
//        	}
//        }
        
        return filteredValues;
    }

    public List<String> completeTextDetailMouvementArticle(String query) {
//    	List<TaArticleDTO> allValues = articleService.selectAllInReception();
    	List<TaArticleDTO> allValues = articleService.findAllLight();
        List<String> filteredValues = new ArrayList<String>();
        
        for(TaArticleDTO f : allValues) {

        		if ((query.equals("*") || (f.getCodeArticle().toLowerCase().contains(query.toLowerCase())))){
//            		filteredValues.add(f.getCodeArticle()+"-"+f.getLibellecArticle());
            		filteredValues.add(f.getCodeArticle());
            	}
        	
        }
        
//    	List<TaBonReception> allValues = bonReceptionService.selectAll();
//        List<String> filteredValues = new ArrayList<String>();
//        
//        for(TaBonReception f : allValues) {
//        	for(TaLBonReception lf : f.getLignes()) {
//        		if ((query.equals("*") || (lf.getTaArticle().getCodeArticle().toLowerCase().contains(query.toLowerCase()))	&&
//        				!filteredValues.contains(lf.getTaArticle().getCodeArticle()))){
//            		filteredValues.add(lf.getTaArticle().getCodeArticle());
//            	}
//        	}
//        }
        
        return filteredValues;
    }

    public List<String> completeTextDetailMouvementLot(String query) {
    	
    	List<TaLotDTO> allValues = lotService.selectAllDTOLight();
        List<String> filteredValues = new ArrayList<String>();        
        for(TaLotDTO f : allValues) {

        		if (query.equals("*") ||( f.getNumLot().toLowerCase().contains(query.toLowerCase()))) {
//            		filteredValues.add(f.getNumLot()+"-"+f.getLibelle());
            		filteredValues.add(f.getNumLot());
            	}
        	
        }
        
//    	List<TaBonReception> allValues = bonReceptionService.selectAll();
//        List<String> filteredValues = new ArrayList<String>();        
//        for(TaBonReception f : allValues) {
//        	for(TaLBonReception lf : f.getLignes()) {
//        		if (query.equals("*") ||( lf.getTaLot().getNumLot().toLowerCase().contains(query.toLowerCase()))
//        				&& !filteredValues.contains(lf.getTaLot().getNumLot())) {
//            		filteredValues.add(lf.getTaLot().getNumLot());
//            	}
//        	}
//        }
        
        return filteredValues;
    }
    
    public List<String> completeTextCodeFabrication(String query) {
    	List<TaFabricationDTO> allValues = fabricationService.findAllLight();
        List<String> filteredValues = new ArrayList<String>();
        
        for(TaFabricationDTO f : allValues) {
        		if (query.equals("*") || (f.getCodeDocument().toLowerCase().contains(query.toLowerCase())
        				&& !filteredValues.contains(f.getCodeDocument()))) {
            		filteredValues.add(f.getCodeDocument());
            	}
//        	}
        }
        
        return filteredValues;
    }
  
    public List<String> completeTextCodeInventaire(String query) {
    	List<InventaireDTO> allValues = inventaireService.findAllLight();
        List<String> filteredValues = new ArrayList<String>();
        
        for(InventaireDTO f : allValues) {
        		if (query.equals("*") || (f.getCodeInventaire().toLowerCase().contains(query.toLowerCase())
        				&& !filteredValues.contains(f.getCodeInventaire()))) {
            		filteredValues.add(f.getCodeInventaire());
            	}
//        	}
        }
        
        return filteredValues;
    }
    public List<String> completeTextLotFabrique(String query) {
    	List<TaLotDTO> allValues = lotService.selectAllPFInFabrication();
        List<String> filteredValues = new ArrayList<String>();
        
        for(TaLotDTO f : allValues) {
        		if (query.equals("*") || (f.getNumLot().toLowerCase().contains(query.toLowerCase())
        				&& !filteredValues.contains(f.getNumLot()))) {
            		filteredValues.add(f.getNumLot());
            	}
//        	}
        }
        
        return filteredValues;
    }
    
    public List<String> completeTextLotReceptionne(String query) {
    	List<TaLotDTO> allValues = lotService.selectAllInReception();
        List<String> filteredValues = new ArrayList<String>();
        
        for(TaLotDTO f : allValues) {
        		if (query.equals("*") || (f.getNumLot().toLowerCase().contains(query.toLowerCase())
        				&& !filteredValues.contains(f.getNumLot()))) {
            		filteredValues.add(f.getNumLot());
            	}
//        	}
        }
        
        return filteredValues;
    }
    
    
    public List<String> completeTextArticleFabrique(String query) {
    	List<TaArticleDTO> allValues = articleService.selectAllPFInFabrication();
        List<String> filteredValues = new ArrayList<String>();
        
        for(TaArticleDTO f : allValues) {
        		if (query.equals("*") || (f.getCodeArticle().toLowerCase().contains(query.toLowerCase())
        				&& !filteredValues.contains(f.getCodeArticle()))) {
            		filteredValues.add(f.getCodeArticle());
            	}
//        	}
        }
        
        return filteredValues;
    }
    
    public List<String> completeTextArticleReceptionne(String query) {
    	List<TaArticleDTO> allValues = articleService.selectAllInReception();
        List<String> filteredValues = new ArrayList<String>();
        
        for(TaArticleDTO f : allValues) {
        		if (query.equals("*") || (f.getCodeArticle().toLowerCase().contains(query.toLowerCase())
        				&& !filteredValues.contains(f.getCodeArticle()))) {
            		filteredValues.add(f.getCodeArticle());
            	}
//        	}
        }
        
        return filteredValues;
    }
    
    
    public List<String> completeTextCodeBR(String query) {
    	List<TaBonReceptionDTO> allValues = bonReceptionService.findAllLight();
        List<String> filteredValues = new ArrayList<String>();
        
        for(TaBonReceptionDTO f : allValues) {
        		if (query.equals("*") || (f.getCodeDocument().toLowerCase().contains(query.toLowerCase())
        				&& !filteredValues.contains(f.getCodeDocument()))) {
            		filteredValues.add(f.getCodeDocument());
            	}
//        	}
        }
        
        return filteredValues;
    }
    
    public List<String> completeTextPFLibelle(String query) {
    	List<TaArticleDTO> allValues = articleService.selectAllMPInFabrication();
        List<String> filteredValues = new ArrayList<String>();
        List<TaArticleDTO> allBR = articleService.selectAllInReception();
        boolean test = false;
        
        for(TaArticleDTO f : allValues) {
        		if ((f.getProduitFini()==true && (query.equals("*") || f.getLibellecArticle().toLowerCase().contains(query.toLowerCase()))) 
        				&& (f.getProduitFini())  ) {
            		if(!filteredValues.contains(f.getCodeArticle()+"-"+f.getLibellecArticle()))
//            			filteredValues.add(f.getCodeArticle()+"-"+f.getLibellecArticle());
            			filteredValues.add(f.getLibellecArticle());
            	}        	
        }
        
        for (TaArticleDTO f : allBR){
        			if((f.getProduitFini()==true && (query.equals("*") || f.getLibellecArticle().toLowerCase().contains(query.toLowerCase())))
            				&& (f.getProduitFini())  ) {
                		if(!filteredValues.contains(f.getCodeArticle()+"-"+f.getLibellecArticle()))
//                			filteredValues.add(f.getCodeArticle()+"-"+f.getLibellecArticle());
                			filteredValues.add(f.getLibellecArticle());
        		}        	
        }    	
//    	List<TaFabrication> allValues = fabricationService.selectAll();
//        List<String> filteredValues = new ArrayList<String>();
//        List<TaBonReception> allBR = bonReceptionService.selectAll();
//        boolean test = false;
//        
//        for(TaFabrication f : allValues) {
//        	for(TaLFabrication lf : f.getLignes()) {
//        		if ((lf.getTaArticle().getProduitFini()==true && (query.equals("*") || lf.getTaArticle().getLibellecArticle().toLowerCase().contains(query.toLowerCase()))) 
//        				&& (lf.getTaArticle().getProduitFini()) ) {
//            		filteredValues.add(lf.getTaArticle().getLibellecArticle());
//            	}
//        	}
//        }
//        
//        for (TaBonReception br : allBR){
//        	for (TaLBonReception lbr : br.getLignes()){
//        			if((lbr.getTaArticle().getProduitFini()==true && (query.equals("*") || lbr.getTaArticle().getLibellecArticle().toLowerCase().contains(query.toLowerCase())))
//        				&& (lbr.getTaArticle().getProduitFini()) ) {
//        				filteredValues.add(lbr.getTaArticle().getLibellecArticle());
//        		}
//        	}
//        }
        
        return filteredValues;
    }
    
    
    
    public List<String> completeTextPFNumLot(String query) {
    	List<TaLotDTO> allValues = lotService.selectAllInFabrication();
    	List<TaLotDTO> allBr = lotService.selectAllInReception();
        List<String> filteredValues = new ArrayList<String>();
        
        for(TaLotDTO f : allValues) {
        		if (f.getProduitFini()==true && (query.equals("*") || f.getNumLot().toLowerCase().contains(query.toLowerCase()))) {
            		if(!filteredValues.contains(f.getNumLot()+"-"+f.getLibelle()))
//            			filteredValues.add(f.getNumLot()+"-"+f.getLibelle());
            			filteredValues.add(f.getNumLot());
            	}        	
        }   
        for(TaLotDTO f : allBr) {
    		if (f.getProduitFini()==true && (query.equals("*") || f.getNumLot().toLowerCase().contains(query.toLowerCase()))) {
        		if(!filteredValues.contains(f.getNumLot()+"-"+f.getLibelle()))
//        			filteredValues.add(f.getNumLot()+"-"+f.getLibelle());
        			filteredValues.add(f.getNumLot());
        	}        	
    }          
//    	List<TaFabrication> allValues = fabricationService.selectAll();
//        List<String> filteredValues = new ArrayList<String>();
//        
//        for(TaFabrication f : allValues) {
//        	for(TaLFabrication lf : f.getLignes()) {
//        		if (lf.getTaArticle().getProduitFini()==true && (query.equals("*") || lf.getTaLot().getNumLot().toLowerCase().contains(query.toLowerCase()))) {
//            		filteredValues.add(lf.getTaLot().getNumLot());
//            	}
//        	}
//        }
        
        return filteredValues;
    }
    
    public List<String> completeTextMPLibelle(String query) {
    	List<TaArticleDTO> allValues = articleService.selectAllMPInFabrication();
        List<String> filteredValues = new ArrayList<String>();
        List<TaArticleDTO> allBr = articleService.selectAllInReception();
        
        for(TaArticleDTO f : allValues) {
        		if ((f.getMatierePremiere()==true && query.equals("*")) || f.getCodeArticle().toLowerCase().contains(query.toLowerCase())){
            		if(!filteredValues.contains(f.getCodeArticle()))
//            			filteredValues.add(f.getCodeArticle()+"-"+f.getLibellecArticle());
            			filteredValues.add(f.getCodeArticle());
            	}        	
        }    
        for(TaArticleDTO f : allBr) {
    		if ((f.getMatierePremiere()==true && query.equals("*")) || f.getCodeArticle().toLowerCase().contains(query.toLowerCase())) {
        		if(!filteredValues.contains(f.getCodeArticle()))
//        			filteredValues.add(f.getCodeArticle()+"-"+f.getLibellecArticle());
        			filteredValues.add(f.getCodeArticle());
        	}        	
    }         
        
        Collections.sort(filteredValues);
//    	List<TaFabrication> allValues = fabricationService.selectAll();
//        List<String> filteredValues = new ArrayList<String>();
//        
//        for(TaFabrication f : allValues) {
//        	for(TaLFabrication lf : f.getLignes()) {
//        		if (lf.getTaArticle().getMatierePremiere()==true && (query.equals("*") || lf.getTaArticle().getLibellecArticle().toLowerCase().contains(query.toLowerCase()))) {
//            		filteredValues.add(lf.getTaArticle().getLibellecArticle());
//            	}
//        	}
//        }
        
        return filteredValues;
    }
	
    
    public List<String> completeTextMPNumLot(String query) {
    	List<TaLotDTO> allValues = lotService.selectAllInFabrication();
        List<String> filteredValues = new ArrayList<String>();
        List<TaLotDTO> allBr = lotService.selectAllInReception();
        
        for(TaLotDTO f : allValues) {
        		if ((f.getMatierePremiere()==true && (query.equals("*")) || f.getNumLot().toLowerCase().contains(query.toLowerCase()))) {
            		if(!filteredValues.contains(f.getNumLot()))
//            			filteredValues.add(f.getNumLot()+"-"+f.getLibelle());
            			filteredValues.add(f.getNumLot());
            	}        	
        }
        for(TaLotDTO f : allBr) {
    		if ((f.getMatierePremiere()==true && (query.equals("*")) || f.getNumLot().toLowerCase().contains(query.toLowerCase())))
    				 {
        		if(!filteredValues.contains(f.getNumLot()))
//        			filteredValues.add(f.getNumLot()+"-"+f.getLibelle());
        			filteredValues.add(f.getNumLot());
        	}        	
    }        
        Collections.sort(filteredValues);
//    	List<TaFabrication> allValues = fabricationService.selectAll();
//        List<String> filteredValues = new ArrayList<String>();
//        
//        for(TaFabrication f : allValues) {
//        	for(TaLFabrication lf : f.getLignes()) {
//        		if (lf.getTaArticle().getMatierePremiere()==true && (query.equals("*") || lf.getTaLot().getNumLot().toLowerCase().contains(query.toLowerCase()))) {
//            		filteredValues.add(lf.getTaLot().getNumLot());
//            	}
//        	}
//        }
        
        return filteredValues;
    }
    
    public void validateControle(AjaxBehaviorEvent event){
		//http://stackoverflow.com/questions/9805276/jsf-and-primefaces-how-to-pass-parameter-to-a-method-in-managedbean
	    //System.out.println("controle : "+controleAVerifier.getValeurTexte());
//		FacesContext context = FacesContext.getCurrentInstance();
//		ControleConformiteJSF c = context.getApplication().evaluateExpressionGet(context, "#{c}", ControleConformiteJSF.class);
		//System.out.println("Validation du controle :"+c.getC().getLibelleConformite()+" avec la valeur : ("+c.getValeurTexte()+")("+c.getValeurBool()+")("+c.getDate()+")("+c.getValeurResultat()+")");
		
//		c.validation(taLot);
	}
    
    public void tmp(){// tmp
		List<TaFabrication> allValues = fabricationService.selectAll();
		for (TaFabrication f : allValues){
			for (ITaLFabrication fl : f.getLignes()){
				System.out.println(fl.getTaArticle().getCodeArticle());
			}
		}
	}
    
    public void tmp2(String ca){ //tmp2
    	List<TaFabrication> allValues = fabricationService.selectAll();
		for (TaFabrication f : allValues){
			for (ITaLFabrication fl : f.getLignes()){
				if (fl.getTaArticle().getCodeArticle().equals(ca)){
					System.out.println("Trouvé : "+fl.getTaArticle().getCodeArticle());
					System.out.println("Libellé : "+fl.getTaArticle().getLibellecArticle());
					System.out.println("fabricationService.findWithCodeArticleNoDate(ca) : "+fabricationService.findWithCodeArticleNoDate(ca));
//					TaFabrication test = new TaFabrication();
					//test = fabricationService.findWithCodeArticleNoDate(ca);
//					{
//						fl.getTaEntrepot().getCodeEntrepot();
//						fl.getTaEntrepot().getLibelle();
//						fl.getTaLot().getTaArticle().getLibellecArticle();
//					}
					
				}
//				else
//					System.out.println(" Rien Trouvé ! ");
//				System.out.println(" \"ca\" est : "+ca);
//				System.out.println(" \"codeArticle\" est : "+codeArticle);
			}
		}
    }
    
//    public void tmp3(String lib, List<TaLFabrication> lf){
//    	int i;
//    	TaLFabrication f = new TaLFabrication();
//    	for (i = 0 ; i < lf.size() ; i++){
//    		f = lf.get(i);
//    		if (f.getTaLot().getTaArticle().getLibellecArticle().contains(lib)){
//    			System.out.println("CodeDocument ( tmp3() ) - "+f.getTaDocument().getCodeDocument());
//    			
//    			f.getTaDocument().getCodeDocument();
//    		}
//    	}
//    }
    
    public void actImprimerCA(ActionEvent actionEvent) throws FinderException {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerCA")); 			
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put("fabrication", fabricationService.findById(selectedTaFabricationDTO.getId()));
//		sessionMap.put("fabrication", fabricationService.findWithCodeArticleNoDate(codeArticle));
		List<EditionEtatTracabilite> edition = editionsService.editionCA(codeArticle);
		List<TaFabrication> lf = fabricationService.findWithCodeArticle(codeArticle, dateDebut, dateFin);
		sessionMap.put("fabrications", lf);
		sessionMap.put("codeArticle", codeArticle);
		sessionMap.put("edition", edition);
		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
		sessionMap.put("dateFin", dateFin);
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerCA()");
	}

    public void actImprimerCADate(ActionEvent actionEvent) throws FinderException {
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerCA")); 			
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put("fabrication", fabricationService.findById(selectedTaFabricationDTO.getId()));
//		sessionMap.put("fabrication", fabricationService.findWithCodeArticleNoDate(codeArticle));
		List<EditionEtatTracabilite> edition = editionsService.editionCADate(codeArticle, dateDebut, dateFin);
		List<TaFabrication> lf = fabricationService.findWithCodeArticle(codeArticle, dateDebut, dateFin);
		sessionMap.put("fabrications", lf);
		sessionMap.put("codeArticle", codeArticle);
		sessionMap.put("edition", edition);
		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
		sessionMap.put("dateFin", dateFin);
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerCA()");
	}
    public void actImprimerDMAParDate(ActionEvent actionEvent) throws FinderException { // Detail de Mouvement d'Article
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Fabrication", "actImprimerDMA")); 
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		List <EditionEtatTracabilite> edition2 =  editionsService.editionDMA(mouvementArticleParDate, dateDebut, dateFin);
		
		sessionMap.put("codeArticle", mouvementArticleParDate);
		sessionMap.put("edition2", edition2);
		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
		sessionMap.put("dateFin", dateFin);
		
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerDMA()");
	}
    public void actImprimerDMAParType(ActionEvent actionEvent) throws FinderException { // Detail de Mouvement d'Article
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Fabrication", "actImprimerDMA")); 
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		List <EditionEtatTracabilite> edition2 =  editionsService.editionDMA(mouvementArticle, dateDebut, dateFin);
		
		sessionMap.put("codeArticle", mouvementArticle);
		sessionMap.put("edition2", edition2);
		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
		sessionMap.put("dateFin", dateFin);
		
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerDMA()");
	}
    public void actImprimerDMATous(ActionEvent actionEvent) throws FinderException { // Detail de Mouvement d'Article
		if(ConstWeb.DEBUG) {
		FacesContext context = FacesContext.getCurrentInstance();  
		context.addMessage(null, new FacesMessage("Fabrication", "actImprimerDMATous")); 
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		List <EditionEtatTracabilite> edition2 =  editionsService.editionDMAtous("%", dateDebut, dateFin);
		
		sessionMap.put("codeArticle", "%");
		sessionMap.put("edition2", edition2);
		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
		sessionMap.put("dateFin", dateFin);
		
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerDMATous()");
	}
    // -------------------------------------------------------------------------------------------------------------
    public void actImprimerDML(ActionEvent actionEvent) throws FinderException { // Détail des Mouvements d'un Lot
    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerDML")); 
    	}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		List <EditionEtatTracabilite> edition =  editionsService.editionDML(mouvementLot, dateDebut, dateFin);
		
		sessionMap.put("numLot", mouvementLot);
		sessionMap.put("edition", edition);
		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
		sessionMap.put("dateFin", dateFin);
		
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerDML()");
//		System.out.println("***************************************> dateDebut - "+dateDebut);
//		System.out.println("***************************************> dateFin - "+dateFin);
	}
    
    public void actImprimerCF(ActionEvent actionEvent) { // il faut refaire avec le requête avec les dates de début et de fin
    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerCF")); // CF = Code Fabrication
    	}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			
			TracabiliteLot traca =lotService.findTracaLot(pfNumLot);
			TaFabricationDTO fab=null;
			if(traca!=null)fab=traca.origineFabrication;
			if(fab!=null){
				TaFabrication f = fabricationService.findAvecResultatConformites(fab.getId());
				f.findProduit();				
				mapEdition.put("fabrication", f);	
				mapEdition.put("lignesProduits", f.getLignesProduits());		
				mapEdition.put("lignesMatierePremieres", f.getLignesMatierePremieres());		
			}
			
			sessionMap.put("edition", mapEdition);
			System.out.println("EtatTracabiliteFabricationController.actImprimerCF()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void actImprimerCFArticle(ActionEvent actionEvent) { // il faut refaire avec le requête avec les dates de début et de fin
//    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerCFArticle")); // CF = Code Fabrication
			context.addMessage(null, new FacesMessage("Fabrication", "Nom implémentée")); // CF = Code Fabrication
//    	}
		try {

//			//		FacesContext facesContext = FacesContext.getCurrentInstance();
//			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
//
//			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//			Map<String, Object> sessionMap = externalContext.getSessionMap();
//			TaFabricationDTO fab=lotService.findTracaLot(pfNumLot).origineFabrication;
//			if(fab!=null){
//				sessionMap.put("fabrication", fabricationService.findByCode(fab.getCodeDocument()));
//				//			sessionMap.put("fabrication", fabricationService.findById(selectedTaFabricationDTO.getId()));
//			}
//			System.out.println("EtatTracabiliteFabricationController.actImprimerCF()");
		} 
//		catch (FinderException e) {
//			e.printStackTrace();
//					
//		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void actImprimerCBRArticle(ActionEvent actionEvent) { // il faut refaire avec le requête avec les dates de début et de fin
//    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerCBRArticle")); // CF = Code Fabrication
			context.addMessage(null, new FacesMessage("Fabrication", "Non implémentée")); // CF = Code Fabrication
//    	}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			TaBonReceptionDTO br=lotService.findTracaLot(getMpNumLot()).origineBonReception;
			
			if(br!=null) {
				TaBonReception obj=bonReceptionService.findAvecResultatConformites(br.getId());
				mapEdition.put("document",obj );
				mapEdition.put("lignes",obj.getLignes());
			}
			
			sessionMap.put("edition", mapEdition);
			
			
			System.out.println("EtatTracabiliteFabricationController.actImprimerCBRArticle()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void actImprimerInventaire(ActionEvent actionEvent) { // il faut refaire avec le requête avec les dates de début et de fin
//    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
//			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerCBRArticle")); // CF = Code Fabrication
//			context.addMessage(null, new FacesMessage("Inventaire", "Non implémentée")); // CF = Code Fabrication
//    	}
		try {


			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			TaInventaire br=inventaireService.findByCode(codeInventaire);
			
			if(br!=null){
				sessionMap.put("inventaire", br);
//				SortedSet.sort(, new LigneInventaireComparator());
				sessionMap.put("listeInventaire", br.getLignes());
			}
			
			System.out.println("EtatTracabiliteFabricationController.actImprimerInventaire()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void actImprimerCBR(ActionEvent actionEvent) { // il faut refaire avec le requête avec les dates de début et de fin
    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerCF")); // CF = Code Fabrication
    	}
		try {

			//		FacesContext facesContext = FacesContext.getCurrentInstance();
			//		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			TaBonReceptionDTO br=lotService.findTracaLot(brNumLot).origineBonReception;
			
			if(br!=null) {
				TaBonReception obj=bonReceptionService.findAvecResultatConformites(br.getId());
				mapEdition.put("document",obj );
				mapEdition.put("lignes",obj.getLignes());
			}
			
			sessionMap.put("edition", mapEdition);
			
			System.out.println("EtatTracabiliteFabricationController.actImprimerCBR()");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void actImprimerPFL(ActionEvent actionEvent) throws FinderException {
    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerPFL")); 
    	}
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		List<TaLFabricationPF> lf = fabricationService.findWithProdFinLibelle(pfLibelle, dateDebut, dateFin);
		List<EditionEtatTracabilite> edition = new ArrayList<EditionEtatTracabilite>();
		
		if (lf.size()==1) {
			edition = editionsService.editionPFL(pfLibelle);
		} else {
			for (ITaLFabrication f : lf){
//				System.out.println("*** Produit Fini + que 2 - Début ********************** ");
//				System.out.println("****** getNumLot : "+ f.getTaLot().getNumLot());
				edition.addAll(editionsService.editionPFNL(f.getTaLot().getNumLot()));
//				System.out.println("*** Produit Fini + que 2 -  Fin  ********************** ");
			}
		}
		
//		for (EditionEtatTracabilite e : edition){
//			System.out.println("*** EditionEtatTracabilite - Début *** ");
//			System.out.println("****** getPfNumLot : "+ e.getPfNumLot());
//			System.out.println("****** getBrNomFournisseur : "+ e.getBrNomFournisseur());
//			System.out.println("****** getBrCodeFournisseur : "+ e.getBrCodeFournisseur());
//			System.out.println("****** getBrNumReception : "+ e.getBrNumReception());
//			System.out.println("****** getBrUniteReception : "+ e.getBrUniteReception());
//			System.out.println("****** getBrDateReception : "+ e.getBrDateReception());
//			System.out.println("****** getBrQteReception : "+ e.getBrQteReception());
//			System.out.println("****** getMpCodeArticle : "+ e.getMpCodeArticle());
//			System.out.println("****** getMpLibelleArticle : "+ e.getMpLibelleArticle());
//			System.out.println("****** getMpNumLot : "+ e.getMpNumLot());
//			System.out.println("****** getMpQte1 : "+ e.getMpQte1());
//			System.out.println("****** getMpQte2 : "+ e.getMpQte2());
//			System.out.println("****** getMpUnite1 : "+ e.getMpUnite1());
//			System.out.println("****** getMpUnite2 : "+ e.getMpUnite2());
//			System.out.println("****** getMpCodeEntrepot : "+ e.getMpCodeEntrepot());
//			System.out.println("****** getMpLibelleEntrepot : "+ e.getMpLibelleEntrepot());
//			System.out.println("****** getMpDateFabrication : "+ e.getMpDateFabrication());
//			System.out.println("****** getMpDluo : "+ e.getMpDluo());
//			System.out.println("****** getMpFamille : "+ e.getMpFamille());
//			System.out.println("****** getMpQteEmplacement : "+ e.getMpQteEmplacement());
//			System.out.println("*** EditionEtatTracabilite -  Fin  *** ");
//		}
		
		//sessions
		sessionMap.put("produitFini", lf); // liste avec un seul objet - Produit Fini
		sessionMap.put("nomLot", pfLibelle); // libellé d'article de produit fini
		sessionMap.put("dateDebut", dateDebut); // date de début de la requête
		sessionMap.put("dateFin", dateFin); // date de fin
		sessionMap.put("edition", edition); // editionMPBR
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerPFL()");
		System.out.println("***************************************> edition - "+edition);
		System.out.println("***************************************> produitFini - "+lf);
		System.out.println("***************************************> dateDebut - "+dateDebut);
		System.out.println("***************************************> dateFin - "+dateFin);
	}
    
    public void actImprimerPFNL(ActionEvent actionEvent) throws FinderException {
    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerPFNL"));
    	}
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		List<TaLFabricationPF> lf = fabricationService.findWithProdFinNumLot(pfNumLot, dateDebut, dateFin);
		List<EditionEtatTracabilite> edition = editionsService.editionPFNL(pfNumLot);
		
//		TaFabrication f = new TaFabrication(); // objet (transversal) de lieson entre PF, MPs et Bons de Reception
//		List<TaLBonReception> rec = new ArrayList<TaLBonReception>();
//		List<TaLFabrication> lfr = new ArrayList<TaLFabrication>();
//		for (int i = 0 ; i < lf.size() ; i++){
//			TaLFabrication tlf = lf.get(i);
//			System.out.println("       ********************************************* "+tlf.getTaDocument().getCodeDocument());
//			//List<TaLFabrication> llf = fabricationService.findWithMatPremNumLot(tlf.getTaDocument().getLibelleDocument(), dateDebut, dateFin);
//			f = fabricationService.findByCode(tlf.getTaDocument().getCodeDocument());
//			List<TaLFabrication> llf = f.getLignes();
//			for(int j = 0 ; j < llf.size() ; j++){
//				TaLFabrication lff = llf.get(j);
//					List<TaLBonReception> r = bonReceptionService.bonRecepFindByCodeArticle(lff.getTaArticle().getCodeArticle());
//					rec.addAll(r);
//					lfr.add(lff);
//			}
//		}
		
		
		
//		for (EditionEtatTracabilite e : edition){
//			System.out.println("*** EditionEtatTracabilite - Début *** ");
//			System.out.println("****** getBrNomFournisseur : "+ e.getBrNomFournisseur());
//			System.out.println("****** getBrCodeFournisseur : "+ e.getBrCodeFournisseur());
//			System.out.println("****** getBrNumReception : "+ e.getBrNumReception());
//			System.out.println("****** getBrUniteReception : "+ e.getBrUniteReception());
//			System.out.println("****** getBrDateReception : "+ e.getBrDateReception());
//			System.out.println("****** getBrQteReception : "+ e.getBrQteReception());
//			System.out.println("****** getMpCodeArticle : "+ e.getMpCodeArticle());
//			System.out.println("****** getMpLibelleArticle : "+ e.getMpLibelleArticle());
//			System.out.println("****** getMpNumLot : "+ e.getMpNumLot());
//			System.out.println("****** getMpQte1 : "+ e.getMpQte1());
//			System.out.println("****** getMpQte2 : "+ e.getMpQte2());
//			System.out.println("****** getMpUnite1 : "+ e.getMpUnite1());
//			System.out.println("****** getMpUnite2 : "+ e.getMpUnite2());
//			System.out.println("****** getMpCodeEntrepot : "+ e.getMpCodeEntrepot());
//			System.out.println("****** getMpLibelleEntrepot : "+ e.getMpLibelleEntrepot());
//			System.out.println("****** getMpDateFabrication : "+ e.getMpDateFabrication());
//			System.out.println("****** getMpDluo : "+ e.getMpDluo());
//			System.out.println("****** getMpFamille : "+ e.getMpFamille());
//			System.out.println("****** getMpQteEmplacement : "+ e.getMpQteEmplacement());
//			System.out.println("*** EditionEtatTracabilite -  Fin  *** ");
//		}
		
		//sessions
		sessionMap.put("produitFini", lf); // liste avec un seul objet - Produit Fini
		sessionMap.put("numLot", pfNumLot); // numèro du lot de produit fini
		sessionMap.put("dateDebut", dateDebut); // date de début de la requête
		sessionMap.put("dateFin", dateFin); // date de fin
		sessionMap.put("edition", edition); // editionMPBR
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerPFNL()");
		System.out.println("***************************************> edition - "+edition);
		System.out.println("***************************************> produitFini - "+lf);
		System.out.println("***************************************> dateDebut - "+dateDebut);
		System.out.println("***************************************> dateFin - "+dateFin);
		
	}
    
    public void actImprimerMPL(ActionEvent actionEvent) throws FinderException {
    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerMPL")); 
    	}
//		int nbLigneTotal=0;
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put("fabrication", fabricationService.findWithMatPremLibelle(mpLibelle, dateDebut, dateFin));
		List<TracabiliteMP> listeTracabilite = new LinkedList<TracabiliteMP>();
		List<TracabilitePMDTO> listeEdition = new LinkedList<TracabilitePMDTO>();
		List<TaLot> listeLot = new LinkedList<TaLot>();
		
		sessionMap.put("code", mpLibelle);
		
		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
		sessionMap.put("dateFin", dateFin);
		
//		List<TaLBonReception> rec = new ArrayList<TaLBonReception>();
		List<ITaLFabrication> lFabPF = new LinkedList<ITaLFabrication>();
		List<ITaLFabrication> lFabMP = new LinkedList<ITaLFabrication>();
		List<TaFabrication> FabMP = new LinkedList<TaFabrication>();
		
		List<Integer> listeIdLigneMP=new LinkedList<Integer>();
		TaArticleDTO article = taArticleService.findByCodeDTO(mpLibelle);
		if(article!=null)mpLibelle=article.getLibellecArticle();
		List<TaLFabricationMP> lFabGeneral = fabricationService.findWithMatPremLibelle(mpLibelle, dateDebut, dateFin);
		for (TaLFabricationMP taLFabrication : lFabGeneral) {
			if(!FabMP.contains(taLFabrication.getTaDocument()))
				FabMP.add(taLFabrication.getTaDocument());
			listeIdLigneMP.add(taLFabrication.getIdLDocument());
		}
		
		for (TaFabrication fab : FabMP) {
			fab.findProduit();
			for (ITaLFabrication taLFabrication : fab.getLignesMatierePremieres()) {
				if(!lFabMP.contains(taLFabrication) &&(
						taLFabrication.getTaArticle()!=null && taLFabrication.getTaArticle().getLibellecArticle().equals(mpLibelle)))
						lFabMP.add(taLFabrication);
			}
		}
		
		
//		for (TaLFabrication l1 : lFabGeneral) {
//			if(l1.getTaMouvementStock()!=null){				
//				if(l1.getTaMouvementStock().getQte1Stock().signum()<0 && !listeLot.contains(l1.getTaLot()))	
//					lFabMP.add(l1);
//				if(!listeLot.contains(l1.getTaLot()))
//					listeLot.add(l1.getTaLot());
//			}
//		}
		for (ITaLFabrication l2 : lFabMP) {
//			nbLigneTotal++;
			TracabiliteMP t;
			//recherche tracabilité du lot de la MP
			if(l2.getTaLot()!=null){
				t=(TracabiliteMP) lotService.findTracaMP(l2.getTaLot().getNumLot());
//				nbLigneTotal+=t.getListePFini().size()-1;
				listeTracabilite.add(t);


			}
		}

		List<String> listeUtile=new LinkedList<String>();
		
		for (TracabiliteMP obj : listeTracabilite) {
			for (TaFabrication fab : obj.ListUtilFabrication) {
				if(!listeUtile.contains(fab.getCodeDocument())){
				listeUtile.add(fab.getCodeDocument());
				// déclencher le remplissage des listes de PF ou MP dans la fabrication
				
				fab.findProduit();
				BigDecimal qte1MP=BigDecimal.ZERO;
				BigDecimal qte2MP=BigDecimal.ZERO;
				String u1="";
				String u2="";
				for (TaLFabricationMP ligne : fab.getLignesMatierePremieres()) {
					if(listeIdLigneMP.contains(ligne.getIdLDocument())){//si ligne de mP demandée
						if(ligne.getQteLDocument()!=null)qte1MP=qte1MP.add(ligne.getQteLDocument());
						if(ligne.getQte2LDocument()!=null)qte2MP=qte2MP.add(ligne.getQte2LDocument());
						if(ligne.getU1LDocument()!=null)u1=ligne.getU1LDocument();
						if(ligne.getU2LDocument()!=null)u2=ligne.getU2LDocument();
						
					}
				}

				for (ITaLFabrication ligne : fab.getLignesProduits()) {
					TracabilitePMDTO tracaDTO = new TracabilitePMDTO();
					tracaDTO.setMpQte1(qte1MP);
					tracaDTO.setMpQte2(qte2MP);
					tracaDTO.setMpUnite1(u1);
					tracaDTO.setMpUnite2(u2);
					if(obj.tracaLot.lot!=null){
						tracaDTO.setCodeArticleMP(obj.tracaLot.lot.getCodeArticle());
						tracaDTO.setLibelleArticleMP(obj.tracaLot.lot.getLibelleArticle());
						tracaDTO.setNumLotMP(obj.tracaLot.lot.getNumLot());
						tracaDTO.setLibelleLotMP(obj.tracaLot.lot.getLibelle());
					}
					if(obj.tracaLot.origineBonReception!=null){
						tracaDTO.setPfCodeDocOrigine(obj.tracaLot.origineBonReception.getCodeDocument());
						tracaDTO.setPfDateDocOrigine(obj.tracaLot.origineBonReception.getDateDocument());
						tracaDTO.setPfFournisseurOrigine(obj.tracaLot.origineBonReception.getNomTiers());
						tracaDTO.setPfTypeDocOrigine(obj.tracaLot.origineBonReception.getCodeTReception());
					}
					if(obj.tracaLot.origineFabrication!=null){
						tracaDTO.setPfCodeDocOrigine(obj.tracaLot.origineFabrication.getCodeDocument());
						tracaDTO.setPfDateDocOrigine(obj.tracaLot.origineFabrication.getDateDocument());
						tracaDTO.setPfFournisseurOrigine("");					
						tracaDTO.setPfTypeDocOrigine(obj.tracaLot.origineFabrication.getCodeTFabrication());
					}					
					if(ligne.getTaArticle()!=null)tracaDTO.setPFCodeArticle(ligne.getTaArticle().getCodeArticle());
					if(ligne.getTaLot()!=null){
						tracaDTO.setPFNumLot(ligne.getTaLot().getNumLot());
						tracaDTO.setPFLibelleLot(ligne.getTaLot().getLibelle());
					}
					tracaDTO.setPfDateFab(fab.getDateDocument());
					tracaDTO.setPfQte1(ligne.getQteLDocument());
					tracaDTO.setPfQte2(ligne.getQte2LDocument());
					tracaDTO.setPfUnite1(ligne.getU1LDocument());
					tracaDTO.setPfUnite2(ligne.getU2LDocument());
					TaLot lot= lotService.findAvecResultatConformite(ligne.getTaLot().getNumLot());
					String control=lotService.retourControl(lot);
//					String control=lot.retourControl();
					tracaDTO.setPfValid(control);
//					if(control!=null){
//						if(control)tracaDTO.setPfValid("Bon");else tracaDTO.setPfValid("Mauvais");
//						}
//					else tracaDTO.setPfValid("NC");
					
					listeEdition.add(tracaDTO);
				}
			}
			}

		}

		
		
		sessionMap.put("listeEdition", listeEdition);
		sessionMap.put("listeTracabilite", listeTracabilite);
//		sessionMap.put("nbLigneTotal", nbLigneTotal);
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerMPL()");
		System.out.println("***************************************> dateDebut - "+dateDebut);
		System.out.println("***************************************> dateFin - "+dateFin);
		//tmp3(pfLibelle, fabricationService.findWithProdFinLibelle(pfLibelle, dateDebut, dateFin));
	}
    
    public void actImprimerMPNL_old(ActionEvent actionEvent) throws FinderException {
    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerMPNL")); 
    	}
		
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.put("lFabrication", fabricationService.findWithMatPremNumLot(mpNumLot, dateDebut, dateFin));

		TaLot lot= lotService.findByCode(mpNumLot);
		sessionMap.put("numLot", mpNumLot);
		sessionMap.put("code", lot.getTaArticle().getCodeArticle());

		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
		sessionMap.put("dateFin", dateFin);
		
		List<TaLBonReception> rec = new ArrayList<TaLBonReception>();
		List<TaLFabricationMP> llf = fabricationService.findWithMatPremNumLot(mpNumLot, dateDebut, dateFin);
		for(int i = 0 ; i < llf.size() ; i++){
			ITaLFabrication lf = llf.get(i);
				List<TaLBonReception> r = bonReceptionService.bonRecepFindByCodeArticle(lf.getTaArticle().getCodeArticle());
				rec.addAll(r);
		}
			
		sessionMap.put("lBonReception", rec);


		System.out.println("EtatTracabiliteFabricationController.actImprimerMPNL()");
		System.out.println("***************************************> dateDebut - "+dateDebut);
		System.out.println("***************************************> dateFin - "+dateFin);
		
	}

    
    public void actImprimerMPNL(ActionEvent actionEvent) throws FinderException {
    	if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Fabrication", "actImprimerMPL")); 
    	}
//		int nbLigneTotal=0;
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		sessionMap.put("fabrication", fabricationService.findWithMatPremLibelle(mpLibelle, dateDebut, dateFin));
		List<TracabiliteMP> listeTracabilite = new LinkedList<TracabiliteMP>();
		List<TracabilitePMDTO> listeEdition = new LinkedList<TracabilitePMDTO>();
		List<TaLot> listeLot = new LinkedList<TaLot>();
		
		sessionMap.put("code", mpNumLot);
		
		sessionMap.put("dateDebut", dateDebut); // les dates de la requête
		sessionMap.put("dateFin", dateFin);
		
//		List<TaLBonReception> rec = new ArrayList<TaLBonReception>();
		List<ITaLFabrication> lFabPF = new LinkedList<ITaLFabrication>();
		List<ITaLFabrication> lFabMP = new LinkedList<ITaLFabrication>();
		List<TaFabrication> FabMP = new LinkedList<TaFabrication>();
		
		List<Integer> listeIdLigneMP=new LinkedList<Integer>();
		
		List<TaLFabricationMP> lFabGeneral = fabricationService.findWithMatPremNumLot(mpNumLot, dateDebut, dateFin);
		for (TaLFabricationMP taLFabrication : lFabGeneral) {
			if(!FabMP.contains(taLFabrication.getTaDocument()))
				FabMP.add(taLFabrication.getTaDocument());
			listeIdLigneMP.add(taLFabrication.getIdLDocument());
		}
		
		for (TaFabrication fab : FabMP) {
			fab.findProduit();
			for (ITaLFabrication taLFabrication : fab.getLignesMatierePremieres()) {
				if(!lFabMP.contains(taLFabrication)&&(
						taLFabrication.getTaLot()!=null && taLFabrication.getTaLot().getNumLot().equals(mpNumLot)))
					lFabMP.add(taLFabrication);
			}
		}
		
		
//		for (TaLFabrication l1 : lFabGeneral) {
//			if(l1.getTaMouvementStock()!=null){				
//				if(l1.getTaMouvementStock().getQte1Stock().signum()<0 && !listeLot.contains(l1.getTaLot()))	
//					lFabMP.add(l1);
//				if(!listeLot.contains(l1.getTaLot()))
//					listeLot.add(l1.getTaLot());
//			}
//		}
		for (ITaLFabrication l2 : lFabMP) {
//			nbLigneTotal++;
			TracabiliteMP t;
			//recherche tracabilité du lot de la MP
			if(l2.getTaLot()!=null){
				t=(TracabiliteMP) lotService.findTracaMP(l2.getTaLot().getNumLot());
//				nbLigneTotal+=t.getListePFini().size()-1;
				listeTracabilite.add(t);


			}
		}

		List<String> listeUtile=new LinkedList<String>();
		
		for (TracabiliteMP obj : listeTracabilite) {
			for (TaFabrication fab : obj.ListUtilFabrication) {
				if(!listeUtile.contains(fab.getCodeDocument())){
				listeUtile.add(fab.getCodeDocument());
				// déclencher le remplissage des listes de PF ou MP dans la fabrication
				
				fab.findProduit();
				BigDecimal qte1MP=BigDecimal.ZERO;
				BigDecimal qte2MP=BigDecimal.ZERO;
				String u1="";
				String u2="";
				for (TaLFabricationMP ligne : fab.getLignesMatierePremieres()) {
					if(listeIdLigneMP.contains(ligne.getIdLDocument())){//si ligne de mP demandée
						if(ligne.getQteLDocument()!=null)qte1MP=qte1MP.add(ligne.getQteLDocument());
						if(ligne.getQte2LDocument()!=null)qte2MP=qte2MP.add(ligne.getQte2LDocument());
						if(ligne.getU1LDocument()!=null)u1=ligne.getU1LDocument();
						if(ligne.getU2LDocument()!=null)u2=ligne.getU2LDocument();
						
					}
				}

				for (ITaLFabrication ligne : fab.getLignesProduits()) {
					TracabilitePMDTO tracaDTO = new TracabilitePMDTO();
					tracaDTO.setMpQte1(qte1MP);
					tracaDTO.setMpQte2(qte2MP);
					tracaDTO.setMpUnite1(u1);
					tracaDTO.setMpUnite2(u2);
					if(obj.tracaLot.lot!=null){
						tracaDTO.setCodeArticleMP(obj.tracaLot.lot.getCodeArticle());
						tracaDTO.setLibelleArticleMP(obj.tracaLot.lot.getLibelleArticle());
						tracaDTO.setNumLotMP(obj.tracaLot.lot.getNumLot());
						tracaDTO.setLibelleLotMP(obj.tracaLot.lot.getLibelle());
					}
					if(obj.tracaLot.origineBonReception!=null){
						tracaDTO.setPfCodeDocOrigine(obj.tracaLot.origineBonReception.getCodeDocument());
						tracaDTO.setPfDateDocOrigine(obj.tracaLot.origineBonReception.getDateDocument());
						tracaDTO.setPfFournisseurOrigine(obj.tracaLot.origineBonReception.getNomTiers());
						tracaDTO.setPfTypeDocOrigine(obj.tracaLot.origineBonReception.getCodeTReception());
					}
					if(obj.tracaLot.origineFabrication!=null){
						tracaDTO.setPfCodeDocOrigine(obj.tracaLot.origineFabrication.getCodeDocument());
						tracaDTO.setPfDateDocOrigine(obj.tracaLot.origineFabrication.getDateDocument());
						tracaDTO.setPfFournisseurOrigine("");					
						tracaDTO.setPfTypeDocOrigine(obj.tracaLot.origineFabrication.getCodeTFabrication());
					}					
					if(ligne.getTaArticle()!=null)tracaDTO.setPFCodeArticle(ligne.getTaArticle().getCodeArticle());
					if(ligne.getTaLot()!=null){
						tracaDTO.setPFNumLot(ligne.getTaLot().getNumLot());
						tracaDTO.setPFLibelleLot(ligne.getTaLot().getLibelle());
					}
					tracaDTO.setPfDateFab(fab.getDateDocument());
					tracaDTO.setPfQte1(ligne.getQteLDocument());
					tracaDTO.setPfQte2(ligne.getQte2LDocument());
					tracaDTO.setPfUnite1(ligne.getU1LDocument());
					tracaDTO.setPfUnite2(ligne.getU2LDocument());
					TaLot lot= lotService.findAvecResultatConformite(ligne.getTaLot().getNumLot());
					String control=lotService.retourControl(lot);
//					String control=lot.retourControl();
					tracaDTO.setPfValid(control);
//					if(control!=null){
//						if(control)tracaDTO.setPfValid("Bon");else tracaDTO.setPfValid("Mauvais");
//						}
//					else tracaDTO.setPfValid("NC");
					
					listeEdition.add(tracaDTO);
				}
			}
			}

		}

		
		
		sessionMap.put("listeEdition", listeEdition);
		sessionMap.put("listeTracabilite", listeTracabilite);
//		sessionMap.put("nbLigneTotal", nbLigneTotal);
		
		System.out.println("EtatTracabiliteFabricationController.actImprimerMPNL()");
		System.out.println("***************************************> dateDebut - "+dateDebut);
		System.out.println("***************************************> dateFin - "+dateFin);
		//tmp3(pfLibelle, fabricationService.findWithProdFinLibelle(pfLibelle, dateDebut, dateFin));
	}
	public TaFabrication getFabTmp() {
		return fabTmp;
	}

	public void setFabTmp(TaFabrication fabTmp) {
		this.fabTmp = fabTmp;
	}
	
	public void actImprimerEdSG(ActionEvent actionEvent) throws FinderException {  // Méthode ne se sert pas des données d'Inventaire - à Modifier
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Impression d'Etat des Stocks", "actImprimer")); 
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		setCodeLot(selectedGrMouvStockDTO.getCodeGrMouvStock());

		List<EditionEtatTracabilite> m2 = editionsService.editionSG(dateDebut, dateFin,codeArticle,null,codeEntrepot);
//		List<EditionEtatTracabilite> m2 = editionsService.editionSG(dateDebut, dateFin);
		/*
		for (EditionEtatTracabilite stock : m2){
			stock.getMouvEntrepot();
			stock.getMouvCodeArticle();
			stock.getMouvNumLot();
			stock.getMouvLibelle();
			stock.getMouvQteDepart();
			stock.getMouvQteEntree();
			stock.getMouvQteSortie();
			stock.getMouvQteDisponible();
		}
		*/
		sessionMap.put("lEditionSD", m2);
		sessionMap.put("dateDebut", dateDebut);
		sessionMap.put("dateFin", dateFin);
		sessionMap.put("article", codeArticle);
		sessionMap.put("familleArticle", familleArticle);
		sessionMap.put("entrepot", codeEntrepot);
		
			System.out.println("EtatTracabiliteFabricationController.actImprimer.actImprimerEdSG() - à Modifier!!!");
			System.out.println("***************************************> lEditionSD - "+m2);
			System.out.println("***************************************> dateDebut - "+dateDebut);
			System.out.println("***************************************> dateFin - "+dateFin);
	} 
    
	public void actImprimerEdNC(ActionEvent actionEvent) throws FinderException {  // Méthode ne se sert pas des données d'Inventaire - à Modifier
		if(ConstWeb.DEBUG) {
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Impression d'Etat des Non-Conformités", "actImprimer")); 
		}
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		List<EditionEtatTracabilite> editionTmp = new LinkedList<EditionEtatTracabilite>();
		List<EditionEtatTracabilite> edition = new LinkedList<EditionEtatTracabilite>();
		
		editionTmp = editionsService.editionNonConforme(dateDebut, dateFin);
		
		for (EditionEtatTracabilite e : editionTmp){
//			System.out.println("***************************************> ");
//			System.out.println("e.getNumLot() : "+e.getNumLot());
			TaLot lot= lotService.findAvecResultatConformite(e.getNumLot());
			e.setResultat(lotService.retourControl(lot));
//			e.setResultat(lot.retourControl());
			if(lot.auMoinsUnControleFaux() || e.getResultat().equals(e.R_VIDE) || (e.getObservation()!=null && !e.getObservation().equals("")) ||e.isForcage() )
				edition.add(e);
//			System.out.println("e.getDateModif() : "+e.getDateModif());
//			System.out.println("e.getCodeArticle() : "+e.getCodeArticle());
//			System.out.println("e.getLibelleGroupe() : "+e.getLibelleGroupe());
//			System.out.println("e.getLibelleConformite() : "+e.getLibelleConformite());
//			System.out.println("e.getResultat() : "+e.getResultat());
//			System.out.println("e.getActionCorrective() : "+e.getActionCorrective());
//			System.out.println("e.isEffectuee() : "+e.isEffectuee());
//			System.out.println("dateDebut : "+dateDebut);
//			System.out.println("dateFin : "+dateFin);
//			
//			System.out.println("***************************************> ");
		}
		
		sessionMap.put("edition", edition);
		sessionMap.put("dateDebut", dateDebut);
		sessionMap.put("dateFin", dateFin);
	}

	public String getFamilleArticle() {
		return familleArticle;
	}

	public void setFamilleArticle(String familleArticle) {
		this.familleArticle = familleArticle;
	}

	public String getCodeEntrepot() {
		return codeEntrepot;
	}

	public void setCodeEntrepot(String codeEntrepot) {
		this.codeEntrepot = codeEntrepot;
	}

	public String getCodeBR() {
		return codeBR;
	}

	public void setCodeBR(String codeBR) {
		this.codeBR = codeBR;
	}

	public String getMouvementArticleParDate() {
		return mouvementArticleParDate;
	}

	public void setMouvementArticleParDate(String mouvementArticleParDate) {
		this.mouvementArticleParDate = mouvementArticleParDate;
	}

	public String getBrNumLot() {
		return brNumLot;
	}

	public void setBrNumLot(String brNumLot) {
		this.brNumLot = brNumLot;
	}

	public String getCodeInventaire() {
		return codeInventaire;
	}

	public void setCodeInventaire(String codeInventaire) {
		this.codeInventaire = codeInventaire;
	}

	public TaInfoEntreprise getInfosEntreprise() {
		return infosEntreprise;
	}

	public void setInfosEntreprise(TaInfoEntreprise infosEntreprise) {
		this.infosEntreprise = infosEntreprise;
	}


    
	/** - Dima -  Fin  - **/
	



	
}  

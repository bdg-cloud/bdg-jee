package fr.legrain.bdg.webapp.solstyce;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.model.TaLot;
import fr.legrain.bdg.article.service.remote.ITaArticleServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaGrMouvStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaLotServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTypeMouvementServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaResultatConformiteServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaResultatCorrectionServiceRemote;
import fr.legrain.bdg.conformite.service.remote.ITaStatusConformiteServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.conformite.model.TaBareme;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaGroupe;
import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.conformite.model.TaStatusConformite;
import fr.legrain.conformite.model.TaTypeConformite;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.lib.data.ModeObjetEcranServeur;

@Named
@ViewScoped  
public class ControleLotController implements Serializable {  
	
	
	private String parameter;

//	private List<TaResultatConformite> values; 
//	private List<TaResultatConformite> filteredValues; 

	private TaResultatConformite nouveau;
	private TaResultatConformite selection;
	
	private TaLot taLot;
	private List<ControleConformiteJSF> listeControle;
	private List<TaGroupe> listeGroupe;
	
	private ControleConformiteJSF controleAVerifier;
	
	private ControleConformiteJSF controleApresActionCorrective;
	
	private ControleConformiteJSF controleConformiteJSF;
	private List<TaResultatCorrection> oldResultatConformite;
	private List<TaResultatConformite> historique;
	private Map<String, TaStatusConformite> mapStatusConformite;
//	private boolean oldCorrectionValide;
	

	private BigDecimal qteDefinitivementNonConforme;
	private Boolean lotDefinitivementNonConforme = false;
	private BigDecimal qteEnStock;

	private Boolean showCorrection = false;
	private Boolean showObservation = false;
	
	private StreamedContent fichierActionCorrective;
	private TaBareme baremeDefautActionAcorrective;
	
	private String statusGlobalLot;

	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";

	private ModeObjetEcranServeur modeEcran = new ModeObjetEcranServeur();

	private @EJB ITaArticleServiceRemote taArticleService;
	private @EJB ITaConformiteServiceRemote taConformiteService;
	private @EJB ITaResultatConformiteServiceRemote taResultatConformiteService;
	private @EJB ITaResultatCorrectionServiceRemote taResultatCorrectionService;
	private @EJB ITaLotServiceRemote taLotService;
	private @EJB ITaUtilisateurServiceRemote utilisateurService;
	private @EJB ITaStatusConformiteServiceRemote taStatusConformiteService;
	private @EJB ITaMouvementStockServiceRemote taMouvementStockService;
	private @EJB ITaGrMouvStockServiceRemote taGrMouvStockService;
	private @EJB ITaTypeMouvementServiceRemote taTypeMouvementService;
	private @EJB ITaEtatStockServiceRemote taEtatStockService;
	private @Inject SessionInfo sessionInfo;
	
	@PostConstruct
	public void postConstruct(){
		try {
			setShowCorrection(false);
//			values =  taResultatConformiteService.selectAll();
			List<TaStatusConformite>  l = taStatusConformiteService.selectAll();
			mapStatusConformite = new HashMap<>();
			for (TaStatusConformite taStatusConformite : l) {
				mapStatusConformite.put(taStatusConformite.getCodeStatusConformite(), taStatusConformite);
			}
			
			refresh();
			
//			if(values == null){
//				setShowCorrection(true);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		this.setFilteredValues(values);
	}

	public ControleLotController() {  
	}  
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List retrieveListAsSet(Set set) {
		return new ArrayList(set);
	}

	public void refresh(){
		try {
			if(taLot==null) {
				
	//				taLot = taLotService.findByCode(parameter);
					
					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					Map<String, Object> sessionMap = externalContext.getSessionMap();
					taLot = (TaLot) sessionMap.get("lotBR");
					
					if(taLot==null) {
						if(sessionMap.get("numLot")!=null) {
							parameter = (String) sessionMap.get("numLot");
						}
						taLot = taLotService.findByCode(parameter);
					}
					
			} 
			else {
				taLot = taLotService.findById(taLot.getIdLot());
			}
			
			TaLot lotTmp = taLot; //comme la requete est basé sur les resultats de conformité, si le lot n'a aucun controle definit, renvoi NULL, donc on garde la référenc eà cet objet pour ce cas précis (else du if) à améliorer
			taLot = taLotService.fetchResultatControleLazy(taLot.getIdLot());
			
//				List<TaConformite> l = retrieveListAsSet(taLot.getTaArticle().getTaConformites());
				List<String> l = new ArrayList<>();
				listeGroupe = new ArrayList<TaGroupe>();
				listeControle = new LinkedList<ControleConformiteJSF>();
				List<String> lg = new ArrayList<String>();
				TaConformite taConformite = null;
				TaStatusConformite s = null;
				//for (TaConformite taConformite : l) {
				if(taLot!=null && taLot.getTaResultatConformites()!=null) {
					for (TaResultatConformite taResultatConformite : taLot.getTaResultatConformites()) {
						taConformite = taResultatConformite.getTaConformite();
						if(!l.contains(LibConversion.integerToString(taConformite.getIdConformite()))) {
							l.add(LibConversion.integerToString(taConformite.getIdConformite())); //pour éviter d'afficher plusieur fois un controle dans le cas ou il aurait plusieur résultat (historique)
							
							//Si des résultat existe pour ce controle pour ce lot, remonter les résultats, 
							//sinon affiché un nouveau controle vierge pour la saisie de valeur constatée
		//					TaResultatConformite taResultatConformite = taResultatConformiteService.findByLotAndControleConformite(taLot.getIdLot(),taConformite.getIdConformite());
							List<TaResultatConformite> h = taResultatConformiteService.findByLotAndControleConformiteHistorique(taLot.getIdLot(),taConformite.getIdConformite());
		//					if(taResultatConformite!=null) {
		//						listeControle.add(new ControleConformiteJSF(taConformite,taResultatConformite));
		//					} else {
		//						listeControle.add(new ControleConformiteJSF(taConformite));
		//					}
							if(h!=null) {
								listeControle.add(new ControleConformiteJSF(taConformite,null,h));
							} else {
								listeControle.add(new ControleConformiteJSF(taConformite));
							}
	//						if(taConformite.getTaGroupe()!=null && !lg.contains(taConformite.getTaGroupe().getCodeGroupe())) {
	//							
	//							lg.add(taConformite.getTaGroupe().getCodeGroupe());
	//							listeGroupe.add(taConformite.getTaGroupe());
	//						}
						}
					}
				
	//				for (ControleConformiteJSF cc : listeControle) {
	//					System.out.println(cc.getC().getLibelleConformite()+" - "+ cc.getC().getPosition());
	//				}
					Collections.sort(listeControle, (c1, c2) -> c1.getC().getPosition().compareTo(c2.getC().getPosition()));
	//				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	//				for (ControleConformiteJSF cc : listeControle) {
	//					System.out.println(cc.getC().getLibelleConformite()+" - "+ cc.getC().getPosition());
	//				}
					for (ControleConformiteJSF cc : listeControle) {
						//mise à jour de la liste des groupe après avoir trier les controles
						if(cc.getC().getTaGroupe()!=null && !lg.contains(cc.getC().getTaGroupe().getCodeGroupe())) {
							lg.add(cc.getC().getTaGroupe().getCodeGroupe());
							listeGroupe.add(cc.getC().getTaGroupe());
						}
					}
					
	//				int qte = taEtatStockService.qteArticleLotEnStock(taLot.getIdLot());
	//				qteEnStock = new BigDecimal(qte);
	//				lotDefinitivementNonConforme = taLot.getLotCompletDefinitivementInvalide();
				
					s = taResultatConformiteService.etatLot(taLot.getIdLot());
				} else {
					taLot = lotTmp;
					s = taStatusConformiteService.findByCode(TaStatusConformite.C_TYPE_ACTION_AUCUN_CONTROLE_DEFINIT);
				}
				if(s!=null) {
					taLot.setTaStatusConformite(s);
					String action = taLot.getTaStatusConformite().getCodeStatusConformite();
					statusGlobalLot = action;
				}
					
				System.out.println("ControleLotController.refresh()");
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String validationLot() {
		TaStatusConformite s = taResultatConformiteService.etatLot(taLot.getIdLot());
		if(s!=null) {
			taLot.setTaStatusConformite(s);
			String action = taLot.getTaStatusConformite().getCodeStatusConformite();
			statusGlobalLot = action;
		}
		if(taLot!=null) {
			return taLotService.validationLot(taLot);
		} else {
			return "";
		}
	}

	public void validateControle(AjaxBehaviorEvent event){
		//http://stackoverflow.com/questions/9805276/jsf-and-primefaces-how-to-pass-parameter-to-a-method-in-managedbean
		try {
		    //System.out.println("controle : "+controleAVerifier.getValeurTexte());
			FacesContext context = FacesContext.getCurrentInstance();
			ControleConformiteJSF c = context.getApplication().evaluateExpressionGet(context, "#{c}", ControleConformiteJSF.class);
			//System.out.println("Validation du controle :"+c.getC().getLibelleConformite()+" avec la valeur : ("+c.getValeurTexte()+")("+c.getValeurBool()+")("+c.getDate()+")("+c.getValeurResultat()+")");
			c = (ControleConformiteJSF) event.getComponent().getAttributes().get("c");
			
			if(c.getUtilisateurControleurDTO()!=null) {
				if(c.getR()!=null) {
					c.getR().setTaUtilisateurControleur(utilisateurService.findById(c.getUtilisateurControleurDTO().getId()));
				}
			} else {
				if(c.getR()!=null) {
					c.getR().setTaUtilisateurControleur(utilisateurService.findById(sessionInfo.getUtilisateur().getId()));
				}
				c.setUtilisateurControleurDTO(utilisateurService.findByIdDTO(sessionInfo.getUtilisateur().getId()));
			}
			
			if(c.getDateControle()!=null) {
				if(c.getR()!=null) {
					c.getR().setDateControle(c.getDateControle());
				}
			} else {
				Date d = new Date();
				if(c.getR()!=null) {
					c.getR().setDateControle(d);
				}
				c.setDateControle(d);
			}
			
			//c.validation(taLot);
			c.validation(taLot,listeControle);
			c.validationChampsCalcules(taLot,listeControle);
			
			statusGlobalLot = validationLot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void valideValeurParDefautControle(AjaxBehaviorEvent event){
		//http://stackoverflow.com/questions/9805276/jsf-and-primefaces-how-to-pass-parameter-to-a-method-in-managedbean
	    //System.out.println("controle : "+controleAVerifier.getValeurTexte());
		FacesContext context = FacesContext.getCurrentInstance();
		ControleConformiteJSF c = context.getApplication().evaluateExpressionGet(context, "#{c}", ControleConformiteJSF.class);
		//System.out.println("Validation du controle :"+c.getC().getLibelleConformite()+" avec la valeur : ("+c.getValeurTexte()+")("+c.getValeurBool()+")("+c.getDate()+")("+c.getValeurResultat()+")");
		c = (ControleConformiteJSF) event.getComponent().getAttributes().get("c");
		
		
		if(c.getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_DATE)
				|| c.getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_HEURE)) {
			//Pour les controles de type date ou heure la valeur par defaut est "maintenant"
			c.setDate(new Date());
		}else {
			c.setValeurTexte(c.getC().getValeurDefaut());
		}
		
		c.validation(taLot,listeControle);
		c.validationChampsCalcules(taLot,listeControle);
		
		statusGlobalLot = validationLot();
	}
	
	public void actSelectLotDefinitivementNonConforme(AjaxBehaviorEvent event){
		if(lotDefinitivementNonConforme) {
			qteDefinitivementNonConforme = new BigDecimal(0);
		} else {
			qteDefinitivementNonConforme = null;
		}
	}
	
	public boolean editable() {
		return !modeEcran.dataSetEnModif();
	}
	
	public void actDialogActionCorrective(ControleConformiteJSF actionEvent) {
//	public void actDialogActionCorrective(ActionEvent actionEvent) {
        
        FacesContext context = FacesContext.getCurrentInstance();
		//ControleConformiteJSF c = context.getApplication().evaluateExpressionGet(context, "#{c}", ControleConformiteJSF.class);
		ControleConformiteJSF c = actionEvent;

		
		controleConformiteJSF = c;
		if(c.getR().getTaResultatCorrections()!=null && !c.getR().getTaResultatCorrections().isEmpty()) {
//			if(c.getR().getTaResultatCorrections().get(0).getTaResultatConformiteApresActionCorrective()!=null) { //en commentaire pour correction du bug #965
//				controleApresActionCorrective = new ControleConformiteJSF(
//					c.getC(),
//					c.getR().getTaResultatCorrections().get(0).getTaResultatConformiteApresActionCorrective()
//					);
//			} else {
								
			if(c!=null 
					&& c.getR().getTaResultatCorrections().get(0).getTaResultatConformite().getTaStatusConformite()!=null
					&& 
					( c.getR().getTaResultatCorrections().get(0).getTaResultatConformite().getTaStatusConformite().getCodeStatusConformite().equals(TaStatusConformite.C_TYPE_ACTION_A_CORRIGER)
					 || c.getR().getTaResultatCorrections().get(0).getTaResultatConformite().getTaStatusConformite().getCodeStatusConformite().equals(TaStatusConformite.C_TYPE_ACTION_NON_CORRIGE)
					)
					){
				controleApresActionCorrective = new ControleConformiteJSF(c.getC(),c.getR().getTaResultatCorrections().get(0).getTaResultatConformiteApresActionCorrective(),true);
			} else {
				controleApresActionCorrective = new ControleConformiteJSF(c.getC(),c.getR().getTaResultatCorrections().get(0).getTaResultatConformiteApresActionCorrective());
			}
//			}
			showCorrection = true;
		} else {
			//le controle est valide, ce c'est pas une action corrective, l'écran de "commentaire" est affiché
			showObservation = true;
		}
		oldResultatConformite = TaResultatConformite.copie(c.getR().getTaResultatCorrections());
		
		baremeDefautActionAcorrective = null;
		if(controleConformiteJSF.getC().getTaBaremes()!=null && !controleConformiteJSF.getC().getTaBaremes().isEmpty()) {
			baremeDefautActionAcorrective = controleConformiteJSF.getC().getTaBaremes().iterator().next();
		}

		historique = taResultatConformiteService.findByLotAndControleConformiteHistorique(taLot.getIdLot(),c.getC().getIdConformite());
		Collections.sort(historique, (c1, c2) -> c1.getDateControle().compareTo(c2.getDateControle()));
		
		//refreshCorrection();	    
	}
	
	public void handleReturnDialogActionCorrective(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//taGroupe = (ControleConformiteJSF) event.getObject();
		}
	}
	
	public void actDialogDocumentActionCorrective(ActionEvent actionEvent) {
	    
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", true);
		options.put("resizable", true);
		options.put("contentHeight", 1024);
		options.put("contentWidth", 1024);

		Map<String,List<String>> params = new HashMap<String,List<String>>();
		List<String> list = new ArrayList<String>();
		//list.add(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION));
		params.put("modeEcranDefaut", list);

//		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//		Map<String, Object> sessionMap = externalContext.getSessionMap();
//		try {
//			cgPartenaireCourant = wsMonCompte.findCgPartenaireCourant();
//		} catch (RemoteException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		sessionMap.put("cgv", cgPartenaire);

		PrimeFaces.current().dialog().openDynamic("/solstyce/dialog_pdf_action_corrective", options, params);
	}
	
	public void actImprimerControleLot(ActionEvent actionEvent) {
		try {
			
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		Map<String, Object> edition = new HashMap<>();
		
		taLot = taLotService.findById(taLot.getIdLot());
		
		List<TaResultatConformite> dernieresValeur = new ArrayList<>();
		for (ControleConformiteJSF c : listeControle) {
			dernieresValeur.add(c.getR());
		}
		
		edition.put("listeRC", dernieresValeur);
		
		edition.put("lot", taLot);
		edition.put("modeLot", true);
		
		List<TaResultatConformite> h = null;
		Map<String,List<TaResultatConformite>> historique = new HashMap<>();
		for (TaResultatConformite taResultatConformite : dernieresValeur) {
			 h = taResultatConformiteService.findByLotAndControleConformiteHistorique(taLot.getIdLot(),taResultatConformite.getTaConformite().getIdConformite());
			 historique.put(taResultatConformite.getTaConformite().getLibelleConformite(), h);
		}
		edition.put("historique", historique);
		
		sessionMap.put("edition", edition);
		
//			session.setAttribute("tiers", taTiersService.findById(selectedTaTiersDTO.getId()));
			System.out.println("EtatNonConformiteBean.actImprimerControleLot()");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public StreamedContent getFichierActionCorrective() {
		   // InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/images/optimus.jpg");
	       // file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
		
		InputStream stream = new ByteArrayInputStream(baremeDefautActionAcorrective.getBlobFichier()); 
		fichierActionCorrective = new DefaultStreamedContent(stream,null,"action_corrective.pdf");
		return fichierActionCorrective;
	}

	public void actInserer(ActionEvent actionEvent){
		nouveau = new TaResultatConformite();
		modeEcran.setMode(EnumModeObjet.C_MO_INSERTION);
	}

	public void actModifier(ActionEvent actionEvent){
		nouveau = selection;
		modeEcran.setMode(EnumModeObjet.C_MO_EDITION);
	}

	public void actSupprimer(){
		try {
			taResultatConformiteService.remove(selection);
//			selection = values.get(0);	
		} catch (RemoveException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", e.getMessage()));
			e.printStackTrace();
		}
//		values = taResultatConformiteService.selectAll();
		selection = null;
	}

	public void actModifier() {
		actModifier(null);
	}
	
	public void actEnregistrer(ActionEvent actionEvent){
		try {
			for (ControleConformiteJSF c : listeControle) {	
				if(c.getR()!=null) { //normalement il y a resultat vide affecter à la création du lot donc ce test n'est plus obligatoire
					
					if(c.getUtilisateurControleurDTO()!=null) {
						c.getR().setTaUtilisateurControleur(utilisateurService.findById(c.getUtilisateurControleurDTO().getId()));
					} else {
						c.getR().setTaUtilisateurControleur(utilisateurService.findById(sessionInfo.getUtilisateur().getId()));
					}
					
					if(c.getDateControle()!=null) {
						c.getR().setDateControle(c.getDateControle());
					} else {
						c.getR().setDateControle(new Date());
					}
					
					if(c.getAction()!=null && mapStatusConformite.get(c.getAction())!=null) {
						//fixe le status actuel du controle avant son enregistrement
						c.getR().setTaStatusConformite(mapStatusConformite.get(c.getAction()));
					}
					
					//c.setR(taResultatConformiteService.merge(c.getR(),ITaResultatConformiteServiceRemote.validationContext));
				}//else, aucune valeur saisie, donc on enregistre rien
			}
			
			//Mise à jour des résultat de conformié du lot à partir de ceux présent à l'écran (historique compris)
//			taLot.getTaResultatConformites().clear();
//			for(ControleConformiteJSF ctrl : listeControle) {
//				if(ctrl.getR()!=null) {
//					taLot.getTaResultatConformites().addAll(ctrl.getHistorique());
//					taLot.getTaResultatConformites().add(ctrl.getR());
//				}
//			}
						
			for(ControleConformiteJSF ctrl : listeControle) {
				System.out.println("===========**********************======================");
//				System.out.println("============ctrl=============="+ctrl.getC().getLibelleConformite()+"=======================================");
//				System.out.println("===========**********************======================");
				for(TaResultatConformite res : taLot.getTaResultatConformites()) {
					System.out.println("ControleLotController.actEnregistrer() res TaResultatConformite "+res+" "+res.getIdResultatConformite()+" - "+res.getValeurConstatee() +" - "+res.getTaConformite().getLibelleConformite()+" - "+res.getTaConformite().getVersionObj());
					if(ctrl.getR().getIdResultatConformite()==res.getIdResultatConformite()) {
						//on cherche le ResultatConformite courant, celui qui est lié au "controle jsf" dans tous les ResultatConformite du lot
						//Dans le cas d'une première saisie de valeur constatée, on met à jour le ResultatConformite à partir des valeurs saisie 
						//(on met à jour car un ResultatConformite 'vide' est créé pour chaque controle à la création du lot, on met donc à jour ce résultat qui était vide.
						//Dans le cas d'une nouvelle saisie, comme les champ de saisie sont vérouillés dans le "controle jsf" il réaffecte la même valeur.
//						System.out.println("==ID="+res.getIdResultatConformite()+"=========res========="+res.getValeurConstatee()+"====ctrl.getR()=="+ctrl.getR().getValeurConstatee()+"=================================");
						res.setDateControle(ctrl.getR().getDateControle());
						res.setTaStatusConformite(ctrl.getR().getTaStatusConformite());
						res.setTaUtilisateurControleur(ctrl.getR().getTaUtilisateurControleur());
						res.setValeurConstatee(ctrl.getR().getValeurConstatee());
						res.getTaResultatCorrections().clear();
						if(ctrl.getR().getTaResultatCorrections()!=null && !ctrl.getR().getTaResultatCorrections().isEmpty()) {
							//Dans ce cas, une action corrective a été réaliser sur le "controle jsf", on affecte donc le résultat ResultatCorrection au dernier ResultatConformite
							//Si ce nouveau ResultatCorrection contient à son tour un taResultatConformiteApresActionCorrective, celui ci deviendra le dernier ResultatCorrection pour ce controle
//							System.out.println("==========="+ctrl.getR().getTaResultatCorrections()+"==================");
							
							TaResultatCorrection rr = ctrl.getR().getTaResultatCorrections().get(0);
							//rr.setTaResultatConformite(res);
							System.out.println("taResultatConformiteApresActionCorrective rr "+rr.getTaResultatConformite());
							if(rr.getTaResultatConformiteApresActionCorrective()!=null) {
								System.out.println("taResultatConformiteApresActionCorrective apres  "+rr.getTaResultatConformiteApresActionCorrective());
							}
							
							//res.getTaResultatCorrections().add(ctrl.getR().getTaResultatCorrections().get(0)); //ok
							
							res.getTaResultatCorrections().add(rr);
							System.out.println("taResultatConformiteApresActionCorrective "+res+" "+res.getIdResultatConformite()+" - "+res.getValeurConstatee() +" - "+res.getTaConformite().getLibelleConformite()+" - "+res.getTaConformite().getVersionObj());
							
							//Force le status de ce controle
							if(ctrl.getR().getTaResultatCorrections().get(0).getValidationForcee()) {
								res.setTaStatusConformite(mapStatusConformite.get(TaStatusConformite.C_TYPE_ACTION_CORRIGE));
							}
						}
					}
				}
			}
			
			taLot = taLotService.merge(taLot); //merge avant la requete NATIVE de récupération de l'état global du lot
			
			taLot.setTaStatusConformite(taResultatConformiteService.etatLot(taLot.getIdLot()));//pour les controles obligatoire
			String action = taLot.getTaStatusConformite().getCodeStatusConformite();
			
			if(action.equals(TaStatusConformite.C_TYPE_ACTION_A_CORRIGER)
//					|| action.equals(TaStatusConformite.C_TYPE_ACTION_A_CORRIGER_BLOQUANT)
					|| action.equals(TaStatusConformite.C_TYPE_ACTION_NON_CORRIGE)
//					|| action.equals(TaStatusConformite.C_TYPE_ACTION_NON_CORRIGE_BLOQUANT)
					|| action.equals(TaStatusConformite.C_TYPE_ACTION_CORRIGE)
					) {
				taLot.setPresenceDeNonConformite(true);
			} else {
				taLot.setPresenceDeNonConformite(false);
			} 
			
			if(action.equals(TaStatusConformite.C_TYPE_ACTION_OK)
					|| action.equals(TaStatusConformite.C_TYPE_ACTION_CORRIGE)
					|| action.equals(TaStatusConformite.C_TYPE_ACTION_AUCUN_CONTROLE_DEFINIT)
//					|| action.equals(TaStatusConformite.C_TYPE_ACTION_VIDE_FACULTATIF)
					) {
				taLot.setLotConforme(true);
			} else {
				taLot.setLotConforme(false);
			} 
			
			
//			if(lotDefinitivementNonConforme) {
//				//mettre à jour ce booleen dans le lot pour l'enregistrer pour pouvoir le remonté
//				taLot.setLotCompletDefinitivementInvalide(true);
//			} else if(qteDefinitivementNonConforme!=null) {
//				//faire un mouvement de sortie du stock correspondant à cette Qte sur ce lot
//				Date maintenant = new Date();
//				TaGrMouvStock grpMouvStock = new TaGrMouvStock();
//				
//				grpMouvStock.setCodeGrMouvStock(taGrMouvStockService.genereCode(null)); //????????????????????????????????????????????????????
//				grpMouvStock.setDateGrMouvStock(maintenant);
//				grpMouvStock.setLibelleGrMouvStock("Sortie de stock pour cause de non conformité");
//				grpMouvStock.setTaTypeMouvStock(taTypeMouvementService.findByCode("S")); // type mouvement sortie //????????????????????????????????????????????????????
//				
//				//grpMouvStock.setTaFacture(masterEntity); //????????????????????????????????????????????????????
//				grpMouvStock.setManuel(true); //????????????????????????????????????????????????????
//				
//				grpMouvStock.getTaMouvementStockes().clear();
//				
//				TaMouvementStock mouv = new TaMouvementStock();
//				mouv.setLibelleStock("Sortie de stock pour cause de non conformité");
//				mouv.setDateStock(maintenant); 
//				mouv.setEmplacement(null); //????????????????????????????????????????????????????
//				mouv.setTaEntrepot(null); //????????????????????????????????????????????????????
//				mouv.setTaLot(taLot);
//				if(qteDefinitivementNonConforme!=null)mouv.setQte1Stock(qteDefinitivementNonConforme.multiply(BigDecimal.valueOf(-1)));
//				//if(ligne.getQte2LDocument()!=null)mouv.setQte2Stock(ligne.getQte2LDocument().multiply(BigDecimal.valueOf(-1)));
//				mouv.setUn1Stock(taLot.getUnite1());
//				mouv.setUn2Stock(taLot.getUnite2());
//				mouv.setTaGrMouvStock(grpMouvStock);
//
//				grpMouvStock.getTaMouvementStockes().add(mouv);
//				
//				taGrMouvStockService.merge(grpMouvStock);
//			}
			
			taLot = taLotService.merge(taLot);
			
			refresh();
			statusGlobalLot = action;
			
			modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
			//PrimeFaces.current().dialog().closeDynamic(listeControle);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void actAnnulerCorrection(ActionEvent actionEvent){
//		if(values.size()>= 1){
//			selection = values.get(0);
//		}		
//		nouveau = new TaResultatConformite();
		
		controleConformiteJSF.getR().setTaResultatCorrections(TaResultatConformite.renvoi(oldResultatConformite, controleConformiteJSF.getR().getTaResultatCorrections())); 
		showCorrection=false;
		showObservation=false;
	}
	
	public void actAnnuler(ActionEvent actionEvent){
//		if(values.size()>= 1){
//			selection = values.get(0);
//		}		
//		nouveau = new TaResultatConformite();
		
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		PrimeFaces.current().dialog().closeDynamic(null);
		actFermerDialogCorrection(actionEvent);
	}

//	public List<TaResultatConformite> getValues(){  
//		return values;
//	}

	public TaResultatConformite getNouveau() {
		return nouveau;
	}

	public void setNouveau(TaResultatConformite newTaResultatConformite) {
		this.nouveau = newTaResultatConformite;
	}

	public TaResultatConformite getSelection() {
		return selection;
	}

	public void setSelection(TaResultatConformite selectedTaResultatConformite) {
		this.selection = selectedTaResultatConformite;
	}

//	public void setValues(List<TaResultatConformite> values) {
//		this.values = values;
//	}

	public void onRowSelect(SelectEvent event) {  
		if(modeEcran.getMode().compareTo(EnumModeObjet.C_MO_CONSULTATION)==0) {
			nouveau = selection;
		}
	}

//	public List<TaResultatConformite> getFilteredValues() {
//		return filteredValues;
//	}
//
//	public void setFilteredValues(List<TaResultatConformite> filteredValues) {
//		this.filteredValues = filteredValues;
//	}

	public Boolean getShowCorrection() {
		return showCorrection;
	}

	public void setShowCorrection(Boolean showCorrection) {
		this.showCorrection = showCorrection;
	}
	
	public void actFermerDialog(ActionEvent actionEvent) {
		//PrimeFaces.current().dialog().closeDynamic(null);
		
		//PrimeFaces.current().dialog().closeDynamic(listeControle);
		PrimeFaces.current().dialog().closeDynamic(taLot);
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
		showCorrection = false;
		showObservation=false;
	}

	
	public void actEnregistrerCorrection(ActionEvent actionEvent){
		try {
//			if(estToutValide()) {
//				controleConformiteJSF.setAction(TaStatusConformite.C_TYPE_ACTION_CORRIGE);
//			} else {
//				controleConformiteJSF.setAction(TaStatusConformite.C_TYPE_ACTION_A_CORRIGER);
//			}
			
			if(controleApresActionCorrective!=null && controleApresActionCorrective.getR()!=null) { 
				//une nouvelle valeur a bien été saisie
				
				//initialisation automatique des champs date et utilisateur s'il sont vides
				if(controleApresActionCorrective.getUtilisateurControleurDTO()!=null) {
					controleApresActionCorrective.getR().setTaUtilisateurControleur(utilisateurService.findById(controleApresActionCorrective.getUtilisateurControleurDTO().getId()));
				} else {
					controleApresActionCorrective.getR().setTaUtilisateurControleur(utilisateurService.findById(sessionInfo.getUtilisateur().getId()));
				}
				
				if(controleApresActionCorrective.getDateControle()!=null) {
					controleApresActionCorrective.getR().setDateControle(controleApresActionCorrective.getDateControle());
				} else {
					controleApresActionCorrective.getR().setDateControle(new Date());
				}
				
				controleApresActionCorrective.getR().setTaStatusConformite(mapStatusConformite.get(controleApresActionCorrective.getAction()));
				
				//Ajout du nouveau résultat dans la chaine d'historique (TaResultatConformite->TaResultatCorrection[0]->TaResultatConformite suivant
				controleConformiteJSF.getR().getTaResultatCorrections().get(0).setTaResultatConformiteApresActionCorrective(controleApresActionCorrective.getR());
				
//				for (ControleConformiteJSF c : listeControle) {		
//					if(c.getC().getIdConformite()==controleConformiteJSF.getC().getIdConformite()) {
//						c.getR().getTaResultatCorrections().get(0).setTaResultatConformiteApresActionCorrective(controleApresActionCorrective.getR());
//					}
//				}
				
				//Mise à jour des valeur des champs calculés une fois que toutes les nouvelles valeurs des controles sont prise en compte
				List<ControleConformiteJSF> listeControleTemporaire = new ArrayList<>();
				listeControleTemporaire.add(controleApresActionCorrective);
				for (ControleConformiteJSF c : listeControle) {
					if(c.getC().getTaTypeConformite()!=null && c.getC().getTaTypeConformite().getCode().equals(TaTypeConformite.C_TYPE_CALCULE)) {
						List<Integer> idCtrlDependant =  c.getC().findListeIdControleDependant();
						
						//TODO il serait préférable d'arriver à faire le calcul temporaire sur une variable temporaire et non sur la "vrai" variable 
						// maintenant x=5 => calcul temp => x=8 => x dans resultat tmp => recalcul => x=5
						// mieux x=5 => nouvelle variable  y => calcul temp => y =8 => y dans resultat tmp 
						if(idCtrlDependant!=null && !idCtrlDependant.isEmpty() && idCtrlDependant.contains(controleConformiteJSF.getC().getIdConformite())) {
							
							TaResultatConformite aa = new TaResultatConformite();
							aa.setTaConformite(c.getC());
							aa.setTaResultatCorrections(null);
							aa.setTaLot(c.getR().getTaLot());
							aa.setTaBareme((c.getC().getTaBaremes()!=null && !c.getC().getTaBaremes().isEmpty()?c.getC().getTaBaremes().iterator().next():null));
							
							//Premier calcul avec les saisie "temporaire" pour avec les résultat de formule "temporaire" aussi, mais pour pouvoir le notifier à l'affichage
							c.validationChampsCalcules(taLot,listeControle,listeControleTemporaire); 
							
							TaResultatConformite bb = new TaResultatConformite();
							bb.setTaConformite(c.getC());
							bb.setTaResultatCorrections(c.getR().getTaResultatCorrections());
							bb.setTaLot(c.getR().getTaLot());
							bb.setTaBareme(c.getR().getTaBareme());
							bb.setTaStatusConformite(c.getR().getTaStatusConformite());
							bb.setTaUtilisateurControleur(c.getR().getTaUtilisateurControleur());
							bb.setDateControle(c.getR().getDateControle());
							bb.setValeurConstatee(c.getR().getValeurConstatee());
		
							if(!c.getR().getTaResultatCorrections().isEmpty()) {
								c.getR().getTaResultatCorrections().get(0).setTaResultatConformiteApresActionCorrective(bb);
							}
							//Recalcul cette fois sans les valeur "temporaire" pour revenir à la valeur précédente => pas de moficiation
							c.validationChampsCalcules(taLot,listeControle);
						}
					}
				}
				
			} else if(controleConformiteJSF.getR().getTaResultatCorrections().get(0).getValidationForcee()) { //pas de nouvelle valeur mais la validation est forcer par l'utilisateur
				System.out.println("ControleLotController.actEnregistrerCorrection()");
				controleConformiteJSF.getR().setTaStatusConformite(mapStatusConformite.get(TaStatusConformite.C_TYPE_ACTION_CORRIGE));
			}
			//PrimeFaces.current().dialog().closeDynamic(controleConformiteJSF);
			showCorrection  = false;
			showObservation = false;
			statusGlobalLot = validationLot();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void validateCorrection(AjaxBehaviorEvent event){
		//http://stackoverflow.com/questions/9805276/jsf-and-primefaces-how-to-pass-parameter-to-a-method-in-managedbean
	    //System.out.println("controle : "+controleAVerifier.getValeurTexte());
		FacesContext context = FacesContext.getCurrentInstance();
		TaResultatCorrection c = context.getApplication().evaluateExpressionGet(context, "#{var}", TaResultatCorrection.class);
		//System.out.println("Validation du controle :"+c.getC().getLibelleConformite()+" avec la valeur : ("+c.getValeurTexte()+")("+c.getValeurBool()+")("+c.getDate()+")("+c.getValeurResultat()+")");
		if(event.getComponent().getClientId().contains("idCombo")) {
			if(c.getEffectuee()!=null && c.getEffectuee()) {
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
	
	public void forcerLeControle(AjaxBehaviorEvent event){
		TaResultatCorrection r = controleConformiteJSF.getR().getTaResultatCorrections().get(0);
		if(r.getValidationForcee()!=null) {
			if(r.getValidationForcee()) {
				r.setDateForceValidation(new Date());
				
				try {
					if(r.getTaUtilisateurForceValidation()!=null) {
						r.setTaUtilisateurForceValidation(utilisateurService.findById(r.getTaUtilisateurForceValidation().getId()));
					} else {
						r.setTaUtilisateurForceValidation(utilisateurService.findById(sessionInfo.getUtilisateur().getId()));
						//r.setUtilisateurControleurDTO(utilisateurService.findByIdDTO(sessionInfo.getUtilisateur().getId()));
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else {
				r.setDateForceValidation(null);
			}
		}
	}
	
	public boolean etatBoutonCorrection(String bouton) {
		boolean retour = true;
		
		
		FacesContext context = FacesContext.getCurrentInstance();
		TaResultatCorrection c = context.getApplication().evaluateExpressionGet(context, "#{var}", TaResultatCorrection.class);
		
		switch (bouton) {
			case "enregistrer":
				boolean toutValide = controleConformiteJSF.getR().getTaResultatCorrections().size()>0;
//				for(TaResultatCorrection a : controleConformiteJSF.getR().getTaResultatCorrections()) {
////					if(!a.getValide() &&  !oldCorrectionValide) {
//					if(!a.getValide()  ) {						
//						toutValide=false;
//						break;
//					}
//				}
				retour = !toutValide;
				break;
			case "precedent":
				if(controleConformiteJSF.getR().getTaResultatCorrections().indexOf(c)!=0) {
					retour = false;
				}
				break;
			case "suivant":
				if((c.getValide()!=null && c.getValide() )
					&& controleConformiteJSF.getR().getTaResultatCorrections().indexOf(c)!=controleConformiteJSF.getR().getTaResultatCorrections().size()-1
					) {
					retour = false;
				}
				
				break;
			default:
				break;
		}
		return retour;
	}
	
	public List<TaUtilisateurDTO> utilisateurAutoCompleteDTOLight(String query) {
//		List<TaUtilisateurDTO> allValues = utilisateurService.findByCodeLight(query);
		List<TaUtilisateurDTO> allValues = utilisateurService.selectAllDTO();
		List<TaUtilisateurDTO> filteredValues = new ArrayList<TaUtilisateurDTO>();

		for (int i = 0; i < allValues.size(); i++) {
			TaUtilisateurDTO civ = allValues.get(i);
			if(query.equals("*") || civ.getEmail()!=null && civ.getEmail().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
			}
		}

		return filteredValues;
	}
	
	public List<TaUtilisateur> utilisateurAutoComplete(String query) {
		//List<TaUtilisateur> allValues = utilisateurService.findByCode(query);
		List<TaUtilisateur> allValues = utilisateurService.selectAll();
		List<TaUtilisateur> filteredValues = new ArrayList<TaUtilisateur>();

		for (int i = 0; i < allValues.size(); i++) {
			TaUtilisateur civ = allValues.get(i);
//			if(query.equals("*") || civ.getEmail().toLowerCase().contains(query.toLowerCase())) {
				filteredValues.add(civ);
//			}
		}

		return filteredValues;
	}

	public boolean estToutValide(){
		boolean toutValide = true;
		for(TaResultatCorrection a : controleConformiteJSF.getR().getTaResultatCorrections()) {
			if(!a.getValide() ) {						
				toutValide=false;
				break;
			}
		}
		return toutValide;
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

	public List<ControleConformiteJSF> getListeControle() {
		return listeControle;
	}

	public List<TaGroupe> getListeGroupe() {
		return listeGroupe;
	}

	public ControleConformiteJSF getControleAVerifier() {
		return controleAVerifier;
	}

	public void setControleAVerifier(ControleConformiteJSF controleAVerifier) {
		this.controleAVerifier = controleAVerifier;
	}

	public ControleConformiteJSF getControleConformiteJSF() {
		return controleConformiteJSF;
	}

	public void setControleConformiteJSF(ControleConformiteJSF controleConformiteJSF) {
		this.controleConformiteJSF = controleConformiteJSF;
	}

	public ControleConformiteJSF getControleApresActionCorrective() {
		return controleApresActionCorrective;
	}

	public void setControleApresActionCorrective(ControleConformiteJSF controleApresActionCorrective) {
		this.controleApresActionCorrective = controleApresActionCorrective;
	}

	public Boolean getShowObservation() {
		return showObservation;
	}

	public void setShowObservation(Boolean showObservation) {
		this.showObservation = showObservation;
	}

	public List<TaResultatConformite> getHistorique() {
		return historique;
	}

	public void setHistorique(List<TaResultatConformite> historique) {
		this.historique = historique;
	}

	public String getStatusGlobalLot() {
		return statusGlobalLot;
	}

	public void setStatusGlobalLot(String statusGlobalLot) {
		this.statusGlobalLot = statusGlobalLot;
	}

	public TaBareme getBaremeDefautActionAcorrective() {
		return baremeDefautActionAcorrective;
	}

	public void setBaremeDefautActionAcorrective(TaBareme baremeDefautActionAcorrective) {
		this.baremeDefautActionAcorrective = baremeDefautActionAcorrective;
	}

	public BigDecimal getQteDefinitivementNonConforme() {
		return qteDefinitivementNonConforme;
	}

	public void setQteDefinitivementNonConforme(BigDecimal qteDefinitivementNonConforme) {
		this.qteDefinitivementNonConforme = qteDefinitivementNonConforme;
	}

	public Boolean getLotDefinitivementNonConforme() {
		return lotDefinitivementNonConforme;
	}

	public void setLotDefinitivementNonConforme(Boolean lotDefinitivementNonConforme) {
		this.lotDefinitivementNonConforme = lotDefinitivementNonConforme;
	}

	public BigDecimal getQteEnStock() {
		return qteEnStock;
	}

	public void setQteEnStock(BigDecimal qteEnStock) {
		this.qteEnStock = qteEnStock;
	}

}  

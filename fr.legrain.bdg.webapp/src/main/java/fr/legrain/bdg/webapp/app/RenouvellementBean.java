package fr.legrain.bdg.webapp.app;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.paiement.service.remote.ILgrStripe;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaInfosFacture;
import fr.legrain.document.model.TaLFacture;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.dossier.model.TaInfoEntreprise;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.email.service.LgrEmailSMTPService;
import fr.legrain.moncompte.ws.TaAdresse;
import fr.legrain.moncompte.ws.TaAutorisation;
import fr.legrain.moncompte.ws.TaCategoriePro;
import fr.legrain.moncompte.ws.TaCgv;
import fr.legrain.moncompte.ws.TaClient;
import fr.legrain.moncompte.ws.TaCommission;
import fr.legrain.moncompte.ws.TaDossier;
import fr.legrain.moncompte.ws.TaLignePanier;
import fr.legrain.moncompte.ws.TaPanier;
import fr.legrain.moncompte.ws.TaPrixParUtilisateur;
import fr.legrain.moncompte.ws.TaPrixParUtilisateurPerso;
import fr.legrain.moncompte.ws.TaPrixPerso;
import fr.legrain.moncompte.ws.TaProduit;
import fr.legrain.moncompte.ws.TaTNiveau;
import fr.legrain.moncompte.ws.TaTypePaiement;
import fr.legrain.moncompte.ws.client.MonCompteWebServiceClientCXF;
import fr.legrain.tiers.model.TaCompl;
import fr.legrain.tiers.model.TaTiers;



@Named
@ViewScoped
//@SessionScoped 
public class RenouvellementBean implements Serializable {  
	

	private static final long serialVersionUID = 1331238708223531056L;

	@Inject private	TenantInfo tenantInfo;
	
//	private @EJB IAdministrationServiceRemote adminService;
	private MonCompteWebServiceClientCXF wsMonCompte = null;
//	private AutorisationWebServiceClientCXF wsAutorisation = null;
//	private @EJB IDatabaseServiceLocal adminService;
	
	private static final String CODE_ARTICLE_NB_UTILISATEUR = "Nb_Utilisateur";
	private static final String CODE_ARTICLE_NB_POSTE = "Nb_Poste";
	private static final String CODE_ARTICLE_WS = "WS";
	
	private @EJB ILgrStripe lgrStripe;
	private @EJB LgrEmailSMTPService lgrEmail;
	
	private String nomEntreprise;	
	private String emailEntreprise;
	private String dossier;
	private String moyenPaiement;
	private String paramPartenaire;
	private BdgProperties bdgProperties;
	private List<TaProduit> listeTousProduit;
	private List<LignePanierJSF> lignesPanierJSF;
	private List<TaTNiveau> listeTaTNiveau;
	private List<TaCategoriePro> listeTaCategoriePro;
	private List<TaPrixParUtilisateur> listPrixParUtilisateur;
	private List<TaPrixParUtilisateurPerso> listPrixParUtilisateurPerso;
	private Map<Integer,TaPrixParUtilisateur> mapPrixParUtilisateur;
	private Map<Integer,TaPrixParUtilisateurPerso> mapPrixParUtilisateurPerso;
	private Map<String,TaPrixPerso> mapPrixPerso;
	private Map<String,TaAutorisation> mapAutorisation;
	private TaDossier dossierCourant = null;
	private TaClient clientCourant = null;
	private TaPanier panier = null;
	private TaCgv cgvCourant = null;
	
	private int[] listeMoisCB = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
	private int[] listeAnneeCB = new int[]{2019,2020,2021,2022,2023,2024,2025,2026,2027,2028,2029};
	
	private TaPrixParUtilisateur selectedTaPrixParUtilisateur;
	private TaPrixParUtilisateurPerso selectedTaPrixParUtilisateurPerso;
	private TaTNiveau selectedTaTNiveau;
	private TaCategoriePro selectedTaCategoriePro;
	
	private int nbUtilisateur;
	private int nbPoste;
	private int nbMois = 1;
	private boolean webservice;
	private boolean cgv;
	private String version="";
	
	private TaFacture fac ;
	private BigDecimal totalHT;
	private BigDecimal totalTTC;
	private BigDecimal totalTVA;
	
	private String nomCarte;
	private int moisCarte;
	private int anneeCarte;
	private String cryptoCarte;
	private String numCarte;
	private String typeCarte;
	
	private String nextLabel = "Suivant";
	private String backLabel = "Précédent";
	private boolean nextLabelVisible = true;
	private boolean backLabelVisible = false;
	
	private List<LignePanierJSF> selectedModules = new ArrayList<LignePanierJSF>();
	
	private List<TaAutorisation> listeAutorisationDossier = null;
	
	private boolean prixPerso = false;
	private boolean prixPersoParUtilisateur = false;
	
	private StreamedContent fichierCgv;
	
	private int idProduit = 4;
	private boolean tousLesModules = true;
	
//	@EJB private ITaProduitServiceRemote taProduitService;
//	@EJB private ITaClientServiceRemote taClientService;
//	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
//	@EJB private ITaDossierServiceRemote taDossierService;
	
	public void preRenderView() {
		if(paramPartenaire!=null) {
			
		}
	}

		
	public void refresh() {
		try {
			if(paramPartenaire!=null) {
				
			}
			if(dossier!=null) {
				dossierCourant = wsMonCompte.findDossier(dossier);
				clientCourant = dossierCourant.getTaClient();
//				clientCourant = wsMonCompte.findClientDossier(dossier);
//				dossierCourant.setTaClient(clientCourant);
				
//				dossierCourant = clientCourant.getListeDossier().get(0);
				
				listeTaCategoriePro = wsMonCompte.selectAllCategoriePro();
				
				if(clientCourant.getAdresse1()==null) {
					clientCourant.setAdresse1(new TaAdresse());
				}
				
				List<TaTNiveau> listeTaTNiveauTmp = wsMonCompte.selectAllTNiveau();
				listeTaTNiveau = new ArrayList<>();
				for (TaTNiveau n : listeTaTNiveauTmp) {
					if(n.getNiveau()>=dossierCourant.getTaTNiveau().getNiveau() && !n.getCode().equals("DEMO")) {
						listeTaTNiveau.add(n);
					}
				}
				if(selectedTaTNiveau==null) {
					selectedTaTNiveau = listeTaTNiveau.get(0);
				}
				
				listPrixParUtilisateur = wsMonCompte.findPrixParUtilisateur();
				mapPrixParUtilisateur = new HashMap<Integer,TaPrixParUtilisateur>();
				selectedTaPrixParUtilisateur = listPrixParUtilisateur.get(0);
				for (TaPrixParUtilisateur taPrixParUtilisateur : listPrixParUtilisateur) {
					mapPrixParUtilisateur.put(taPrixParUtilisateur.getNbUtilisateur(), taPrixParUtilisateur);
				}
				
				listPrixParUtilisateurPerso = dossierCourant.getListePrixParUtilisateurPerso();
				if(!listPrixParUtilisateurPerso.isEmpty()) {
					prixPersoParUtilisateur = true;
					selectedTaPrixParUtilisateurPerso = listPrixParUtilisateurPerso.get(0);
					mapPrixParUtilisateurPerso = new HashMap<Integer,TaPrixParUtilisateurPerso>();
					for (TaPrixParUtilisateurPerso taPrixParUtilisateur : listPrixParUtilisateurPerso) {
						mapPrixParUtilisateurPerso.put(taPrixParUtilisateur.getNbUtilisateur(), taPrixParUtilisateur);
					}
				}
				
				
				List<TaPrixPerso> listePrixPerso = dossierCourant.getListePrixPerso();
				if(!listePrixPerso.isEmpty()) {
					prixPerso = true;
					mapPrixPerso = new HashMap<String,TaPrixPerso>();
					for (TaPrixPerso taPrixPerso : listePrixPerso) {
						mapPrixPerso.put(taPrixPerso.getTaProduit().getIdentifiantModule(), taPrixPerso);
					}
				}
				
				listeAutorisationDossier = dossierCourant.getListeAutorisation();
				List<TaAutorisation> listeAutorisationVendable = new ArrayList<TaAutorisation>();
				mapAutorisation = new HashMap<String, TaAutorisation>();
				for (TaAutorisation aut : listeAutorisationDossier) {
					if(aut.getTaProduit().isVendable()!=null && aut.getTaProduit().isVendable()) {
						listeAutorisationVendable.add(aut);
						mapAutorisation.put(aut.getTaProduit().getIdentifiantModule(), aut);
					}
				}
				listeAutorisationDossier = listeAutorisationVendable;
				
				initListeModule();
				
				recalculTotaux();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@PostConstruct
	public void init() {
//		initBdd();
		bdgProperties = new BdgProperties();
		
		try {
			dossier = tenantInfo.getTenantId();
			wsMonCompte = new MonCompteWebServiceClientCXF();
//			wsAutorisation = new AutorisationWebServiceClientCXF();
			
			refresh();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		refresh();
		
		initListeModule();
		
		recalculTotaux();

//		setNomCarte("test");
//		setNumCarte("5555555555554444");
//		setAnneeCarte(2017);
//		setMoisCarte(8);
//		setCryptoCarte("314");
	}
	
	
	public void initListeModule() {
		try {
			String constNiveauPremium = "PREMIUM";
			String constNiveauBasic = "BASIC";
			String constCategorieProStandard = "Standard";
			listeTousProduit = wsMonCompte.listeProduit(null,null,null);
			
			if(selectedTaCategoriePro == null && listeTaCategoriePro != null && !listeTaCategoriePro.isEmpty()) {
				selectedTaCategoriePro = listeTaCategoriePro.get(0); // catégorie STANDARD, tous les produits
			}
			
			if(selectedModules!=null) {
				selectedModules.clear();
			}
		
			List<TaProduit> listeModuleVendable = new ArrayList<TaProduit>();
			Map<String,TaProduit> listeModuleVendablePremium = new HashMap<String,TaProduit>();
			Map<String,TaProduit> listeModuleVendableBasic = new HashMap<String,TaProduit>();
			Map<String,TaProduit> listeModuleVendableCategoriePro = new HashMap<String,TaProduit>();
			List<TaProduit> listeIdModuleVendableNiveauASupprimer = new ArrayList<TaProduit>();
			
			//préparation du filtre sur le niveau
			for (TaProduit p : listeTousProduit) {
				if(p.isVendable()!=null && p.isVendable() ) {
					
					if(p.getTaTNiveau().getCode().equals(constNiveauPremium)) {
						listeModuleVendablePremium.put(p.getIdentifiantModule(), p);
					} else {
						listeModuleVendableBasic.put(p.getIdentifiantModule(), p);
					}
				}
			}
			
			//préparation du filtre sur la catégorie pro
			if(selectedTaCategoriePro != null) {
				if(selectedTaCategoriePro.getLibelle().equals(constCategorieProStandard)) {
					//pas de filtre
				} else {
					for (TaProduit p : selectedTaCategoriePro.getListeProduit()) {
						listeModuleVendableCategoriePro.put(p.getIdentifiantModule(), p);
					}
				}
			}
		
			//filtre en fonction du niveau
//			for (String id : listeModuleVendablePremium.keySet()) {
//				if(selectedTaTNiveau!=null && selectedTaTNiveau.getCode().equals(constNiveauBasic)) {
//					if(listeModuleVendableBasic.containsKey(id)) {
//						listeModuleVendable.add(listeModuleVendableBasic.get(id));
//					} else {
//						listeModuleVendable.add(listeModuleVendablePremium.get(id));
//					}
//				} else {
//					listeModuleVendable.add(listeModuleVendablePremium.get(id));
//				}
//			}
			if(selectedTaTNiveau!=null && selectedTaTNiveau.getCode().equals(constNiveauBasic)) {
				listeModuleVendable.addAll(listeModuleVendableBasic.values());
			} else {
				listeModuleVendable.addAll(listeModuleVendablePremium.values());
			}
			
			//filtre en fonction de la catégorie
			if(!listeModuleVendableCategoriePro.isEmpty()) {
				listeIdModuleVendableNiveauASupprimer.clear();
				for (TaProduit taProduit : listeModuleVendable) {
					if(!listeModuleVendableCategoriePro.containsKey(taProduit.getIdentifiantModule())) {
						listeIdModuleVendableNiveauASupprimer.add(taProduit);
					}
				}
				listeModuleVendable.removeAll(listeIdModuleVendableNiveauASupprimer);
			}
			
			
	//		selectedModules = new TaProduit[listeModuleVendable.size()];
	//		selectedModules = listeModuleVendable.toArray(selectedModules);
						
			if(tousLesModules) {
	
			}
			
			lignesPanierJSF = new ArrayList<LignePanierJSF>();
//			for (TaProduit p : listeTousProduit) {
			
			LignePanierJSF l = null;
			for (TaProduit p : listeModuleVendable) {
				if(prixPerso && mapPrixPerso!=null && mapPrixPerso.get(p.getIdentifiantModule())!=null) {
					l = new LignePanierJSF(p,mapPrixPerso.get(p.getIdentifiantModule()));
				} else {
					l = new LignePanierJSF(p);
				}
				if(mapAutorisation!=null && mapAutorisation.get(p.getIdentifiantModule())!=null) {
					l.setDateExpiration(mapAutorisation.get(p.getIdentifiantModule()).getDateFin().toGregorianCalendar().getTime());
				}
				lignesPanierJSF.add(l);
			}
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onRowSelect(SelectEvent event) {
	    System.out.println("onRowSelect");
	    recalculTotaux();
	}

	public void onRowUnselect(UnselectEvent event) {
	    System.out.println("onRowUnselect");
	    recalculTotaux();
	}
	
	public void onToggleSelect(ToggleSelectEvent event) {
	    System.out.println("onToggleSelect");
	    recalculTotaux();
	}
	
	public void filtreVersion() {
		if(selectedModules!=null) {
			selectedModules.clear();
			recalculTotaux();
		}
		initListeModule();
	}
	
	public void recalculTotaux() {
		fac=new TaFacture(true);
		fac.setTtc(0);
		totalHT = new BigDecimal(0);
		BigDecimal totalOptionWS = new BigDecimal(0);
		BigDecimal totalOptionNbPoste = new BigDecimal(0);
		Map<TaProduit, Integer> mapProduitRetardMois = new HashMap<>();
		
		BigDecimal prixTotalHTModule = null;
		BigDecimal tauxTvaModule = new BigDecimal(0);
		BigDecimal TvaModule = null;
		String infosComplArticle="";	
		infosComplArticle=" - pour "+nbMois+" mois";
		
		for (LignePanierJSF p : selectedModules) {
			TaLFacture lf = new TaLFacture(false);
			prixTotalHTModule = new BigDecimal(0);
			tauxTvaModule = new BigDecimal(0);
			TvaModule = new BigDecimal(0);			
			//prix par défaut
			prixTotalHTModule = prixTotalHTModule.add(p.getPrixHT());
			tauxTvaModule = p.getProduit().getTauxTVA();

			if(webservice) {
//				infosComplArticle+=" - option WS";
				//ajouter le prix de l'option webservice
				if(p.getPrixHTWs()!=null) {
					totalOptionWS = totalOptionWS.add(p.getPrixHTWs());
				}
			}
			
			if(nbPoste>0) {
//				infosComplArticle+=" - "+nbPoste+" poste(s)";
				//ajouter le prix du nombre de poste installé en fonction du nombre
				if(p.getPrixHTParPosteInstalle()!=null) {
					totalOptionNbPoste = totalOptionNbPoste.add(p.getPrixHTParPosteInstalle().multiply(new BigDecimal(nbPoste)));
				}
			}
			
			//on rempli 1 ligne de facture par module
			lf.setTaArticle(new TaArticle());
			lf.getTaArticle().setCodeArticle(p.getProduit().getCode());
			lf.setLibLDocument(p.getProduit().getLibelle()+infosComplArticle);
			lf.setPrixULDocument(prixTotalHTModule);
			lf.setTauxTvaLDocument(tauxTvaModule);
			lf.setCodeTvaLDocument(tauxTvaModule.toPlainString());
			int nbMoisRetardPaiement = verifiePaiementInterrompuModule(dossierCourant,p.getProduit());
			if(nbMoisRetardPaiement>0) {
				mapProduitRetardMois.put(p.getProduit(), nbMoisRetardPaiement);
			}
			lf.setQteLDocument(BigDecimal.valueOf(nbMois+nbMoisRetardPaiement));
			lf.setTaTLigne(new TaTLigne(SWTDocument.C_CODE_T_LIGNE_H));
			lf.setTaDocument(fac);
			//lf.setRemHtLDocument(remHtLFacture);
			fac.getLignes().add(lf);
			lf.setLegrain(true);//pour générer les calculs de tva
			lf.calculMontant();
			p.setMontantHT(lf.getMtHtLApresRemiseGlobaleDocument());
			p.setMontantTVA(lf.getMtTtcLApresRemiseGlobaleDocument().subtract(lf.getMtHtLApresRemiseGlobaleDocument()));
			p.setMontantTTC(lf.getMtTtcLApresRemiseGlobaleDocument());
			//on ajoute le prix HT du module (options comprises) au total général
			totalHT = totalHT.add(prixTotalHTModule);
		}
		

		TaLFacture lf = new TaLFacture(false);
		if(prixPersoParUtilisateur) {
			if(selectedTaPrixParUtilisateurPerso.getPrixHT()!=null){
				totalHT = totalHT.add(selectedTaPrixParUtilisateurPerso.getPrixHT());
				lf.setTaArticle(new TaArticle());
				lf.getTaArticle().setCodeArticle(CODE_ARTICLE_NB_UTILISATEUR);
				lf.setLibLDocument("Prix pour "+selectedTaPrixParUtilisateurPerso.getNbUtilisateur()+" utilisateur(s)"+infosComplArticle);
				//on rempli 1 ligne de facture 
				lf.setPrixULDocument(selectedTaPrixParUtilisateurPerso.getPrixHT());
				lf.setTauxTvaLDocument(selectedTaPrixParUtilisateurPerso.getTauxTVA());
				if(selectedTaPrixParUtilisateurPerso.getTauxTVA()!=null) {
					lf.setCodeTvaLDocument(selectedTaPrixParUtilisateurPerso.getTauxTVA().toPlainString());
				}
				lf.setQteLDocument(BigDecimal.valueOf(nbMois));
				lf.setTaTLigne(new TaTLigne(SWTDocument.C_CODE_T_LIGNE_H));
				lf.setTaDocument(fac);
				nbUtilisateur = selectedTaPrixParUtilisateurPerso.getNbUtilisateur();
				//lf.setRemHtLDocument(remHtLFacture);
				fac.getLignes().add(lf);
				lf.setLegrain(true);
				lf.calculMontant();
			}
		} else { //prix par défaut
			//ajouter le prix de la tranche d'utilisateur correspondante
			if(selectedTaPrixParUtilisateur.getPrixHT()!=null){
				totalHT = totalHT.add(selectedTaPrixParUtilisateur.getPrixHT());
				lf.setTaArticle(new TaArticle());
				lf.getTaArticle().setCodeArticle(CODE_ARTICLE_NB_UTILISATEUR);
				lf.setLibLDocument("Prix client pour "+selectedTaPrixParUtilisateur.getNbUtilisateur()+" utilisateur(s)"+infosComplArticle);
				//on rempli 1 ligne de facture 
				lf.setPrixULDocument(selectedTaPrixParUtilisateur.getPrixHT());
				lf.setTauxTvaLDocument(selectedTaPrixParUtilisateur.getTauxTVA());
				if(selectedTaPrixParUtilisateur.getTauxTVA()!=null) {
					lf.setCodeTvaLDocument(selectedTaPrixParUtilisateur.getTauxTVA().toPlainString());
				}
				lf.setQteLDocument(BigDecimal.valueOf(nbMois));
				lf.setTaTLigne(new TaTLigne(SWTDocument.C_CODE_T_LIGNE_H));
				lf.setTaDocument(fac);
				nbUtilisateur = selectedTaPrixParUtilisateur.getNbUtilisateur();
				//lf.setRemHtLDocument(remHtLFacture);
				fac.getLignes().add(lf);
				lf.setLegrain(true);
				lf.calculMontant();
			}
		}
		if(totalOptionNbPoste.compareTo(BigDecimal.ZERO)!=0){
			lf = new TaLFacture(false);
			lf.setTaArticle(new TaArticle());
			lf.getTaArticle().setCodeArticle(CODE_ARTICLE_NB_POSTE);
			lf.setLibLDocument(nbPoste+" poste(s)"+infosComplArticle);
			lf.setPrixULDocument(totalOptionNbPoste);
			lf.setTauxTvaLDocument(tauxTvaModule);
			lf.setCodeTvaLDocument(tauxTvaModule.toPlainString());
			lf.setQteLDocument(BigDecimal.valueOf(nbMois));
			lf.setTaTLigne(new TaTLigne(SWTDocument.C_CODE_T_LIGNE_H));
			lf.setTaDocument(fac);
			//lf.setRemHtLDocument(remHtLFacture);
			fac.getLignes().add(lf);
			lf.setLegrain(true);//pour générer les calculs de tva
			lf.calculMontant();
		}
		if(totalOptionWS.compareTo(BigDecimal.ZERO)!=0){
			lf = new TaLFacture(false);
			lf.setTaArticle(new TaArticle());
			lf.getTaArticle().setCodeArticle(CODE_ARTICLE_WS);
			lf.setLibLDocument("Option WebService"+infosComplArticle);
			lf.setPrixULDocument(totalOptionWS);
			lf.setTauxTvaLDocument(tauxTvaModule);
			lf.setCodeTvaLDocument(tauxTvaModule.toPlainString());
			lf.setQteLDocument(BigDecimal.valueOf(nbMois));
			lf.setTaTLigne(new TaTLigne(SWTDocument.C_CODE_T_LIGNE_H));
			lf.setTaDocument(fac);
			//lf.setRemHtLDocument(remHtLFacture);
			fac.getLignes().add(lf);
			lf.setLegrain(true);//pour générer les calculs de tva
			lf.calculMontant();
		}		
		
		//multiplier par le nombre de mois
		if(totalHT!=null){
			totalHT = totalHT.multiply(new BigDecimal(nbMois));
			totalTVA = totalHT.multiply(tauxTvaModule.divide(new BigDecimal(100)));
			totalTTC = totalHT.add(totalTVA);
		}
		
		//calcul facture pour récup les totaux
		fac.calculeTvaEtTotaux();
		totalHT=fac.getNetHtCalc();
		totalTVA = fac.getNetTvaCalc();
		totalTTC = fac.getNetTtcCalc();	
		
		if(mapProduitRetardMois!=null && !mapProduitRetardMois.isEmpty()) {
			FacesContext context = FacesContext.getCurrentInstance();  
			String liste = "(";
			for (TaProduit p : mapProduitRetardMois.keySet()) {
				liste += p.getLibelle()+",";
			}
			liste += ")";
			context.addMessage(null, new FacesMessage("Panier", 
					"L'abonnement à certains module a été interrompu pendant une période inférieure à 24 mois."
					)); 
		}
	}
	
	/**
	 * Si un utilisateur achète un module payant
	 * - soit il n'a jamais acheté ce module avant, on créé une nouvelle autorisation payante (par opposition aux autorisations de la version demo à la création du dossier)
	 * - soit il a déjà une autorisation payante en cours pour ce module, on prolonge cette autorisation avec le tarif classique
	 * - soit il a déjà acheté ce module, mais son autorisation payante est déjà terminée. 
	 * 		Dans ce cas, il doit payer les mois entre la fin de sa précédente autorisation et la nouvelle autorisation (aujourd'hui).
	 * 		Si la fin de sa précédente autorisation date de plus de 24 mois, le tarif normal s'applique (aucun rattrapage)
	 * 
	 * Retourne le nombre de mois de retard à payer
	 */
	public int verifiePaiementInterrompuModule(TaDossier dossier, TaProduit p) {
		int nbMoisRetard = 0;
		for(TaAutorisation a : dossier.getListeAutorisation()) {
			if(a.getTaProduit().getIdentifiantModule().equals(p.getIdentifiantModule()) && a.isPaye()) {
				//le dossier à deja paye pour avoir accès à ce module dans la passé
				if(a.getDateFin().toGregorianCalendar().getTime().before(new Date())) { //cette autorisation payante est terminée
					Calendar startCalendar = new GregorianCalendar();
					startCalendar.setTime(a.getDateFin().toGregorianCalendar().getTime());
					Calendar endCalendar = new GregorianCalendar();
					endCalendar.setTime(new Date());

					int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
					int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
					diffMonth = diffMonth -1 ;//le mois en cours
					if(diffMonth<24) {
						nbMoisRetard = diffMonth;
					}
				}
			}
		}
		return nbMoisRetard;
	}
	
	public void paiementCheque(ActionEvent event) {
		//les cgv sont acceptées
		if(cgv) {
			panier.setTaCgv(cgvCourant);
		}
		
		panier.setPaye(false);
		panier.setValideParClient(true);
		panier.setCodeRevendeur(panier.getTaDossier().getCodePartenaire());

		GregorianCalendar d = new GregorianCalendar();
		Date maintenant = new Date();
		d.setTime(maintenant);
		XMLGregorianCalendar date = null;
		try {
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}			
//		panier.setDatePaiement(date);
		try {
			TaTypePaiement taTypePaiement = wsMonCompte.findTypePaiement("C");
			panier.setTaTypePaiement(taTypePaiement);
			ajouteLigneOptionPanier();
			
			panier = wsMonCompte.mergePanier(panier);
//			if(panier!=null) panier = wsMonCompte.findPanier(panier.getIdPanier());
//			if(panier!=null){
//				XMLGregorianCalendar dateFin=panier.getDatePaiement();
//				d.setTime(LibDate.incrementDate(dateFin.toGregorianCalendar().getTime(), 0, panier.getNbMois(), 0));
//				try {
//					dateFin= DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
//				} catch (DatatypeConfigurationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				for (TaLignePanier l : panier.getLignes()) {
//					ajouteAutorisation( l.getTaProduit(),panier.getDatePaiement(),dateFin,panier.getNbMois());
//				}
//
//				dossierCourant=wsMonCompte.mergeDossier(dossierCourant);
//				dossierCourant=wsMonCompte.findDossier(dossierCourant.getCode());
//			}
//			
//			//Génération des commissions partenaire
//			if(panier.getCodeRevendeur()!=null && !panier.getCodeRevendeur().equals("")) {
//				TaCommission com = wsMonCompte.genereCommission(panier);
//			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paiementVirement(ActionEvent event) {
		//les cgv sont acceptées
		if(cgv) {
			panier.setTaCgv(cgvCourant);
		}
		
		panier.setPaye(false);
		panier.setCodeRevendeur(panier.getTaDossier().getCodePartenaire());

		GregorianCalendar d = new GregorianCalendar();
		Date maintenant = new Date();
		d.setTime(maintenant);
		XMLGregorianCalendar date = null;
		try {
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}			
		try {
			TaTypePaiement taTypePaiement = wsMonCompte.findTypePaiement("VIR");
			panier.setTaTypePaiement(taTypePaiement);
			ajouteLigneOptionPanier();
			
			panier = wsMonCompte.mergePanier(panier);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paiementCB(ActionEvent event) {
		//les cgv sont acceptées
		if(cgv) {
			panier.setTaCgv(cgvCourant);
		}
			
		String descriptionClient = "Legrain Informatique -"+dossier+"/"+panier.getTaClient().getCode()+"/"+panier.getIdPanier();
			
		String idPaiement = lgrStripe.payer(
					fac.getNetAPayer(), 
					getNumCarte(), 
					getMoisCarte(), 
					getAnneeCarte(), 
					getCryptoCarte(), 
					getNomCarte(), 
					descriptionClient
					);
	
			if(idPaiement!=null) {
				panier.setPaye(true);
				panier.setValideParClient(true);
				panier.setCodeRevendeur(panier.getTaDossier().getCodePartenaire());
				panier.setRefPaiement(idPaiement);
				GregorianCalendar d = new GregorianCalendar();
				Date maintenant = new Date();
				d.setTime(maintenant);
				XMLGregorianCalendar date = null;
				try {
					date = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
				} catch (DatatypeConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
				panier.setDatePaiement(date);
				try {
					TaTypePaiement taTypePaiement = wsMonCompte.findTypePaiement("CB");
					panier.setTaTypePaiement(taTypePaiement);
					ajouteLigneOptionPanier();
					
					panier = wsMonCompte.mergePanier(panier);
					if(panier!=null) panier = wsMonCompte.findPanier(panier.getIdPanier());
					
					dossierCourant = wsMonCompte.majAutorisation(panier);
//					dossierCourant=wsMonCompte.findDossier(dossierCourant.getCode());
//					if(panier!=null){
//						XMLGregorianCalendar dateFin=panier.getDatePaiement();
//						d.setTime(LibDate.incrementDate(dateFin.toGregorianCalendar().getTime(), 0, panier.getNbMois(), 0));
//						try {
//							dateFin= DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
//						} catch (DatatypeConfigurationException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						for (TaLignePanier l : panier.getLignes()) {
//							ajouteAutorisation( l.getTaProduit(),panier.getDatePaiement(),dateFin,panier.getNbMois());
//						}
//		
//						dossierCourant=wsMonCompte.mergeDossier(dossierCourant);
//						dossierCourant=wsMonCompte.findDossier(dossierCourant.getCode());
//					}
					
					//Génération des commissions partenaire
					if(panier.getCodeRevendeur()!=null && !panier.getCodeRevendeur().equals("")) {
						TaCommission com = wsMonCompte.genereCommission(panier);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} 
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
////			FacesContext.getCurrentInstance().addMessage(null,
////	                new FacesMessage(e.getMessage()));
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
//		}
		
		
//		else {
//			panier.setPaye(false);
//			panier.setRefPaiement(null);
//		}
		
	}
	
	public void majAutorisations() {
		
	}
	
	public void enregistreCoordonneesClient(ActionEvent event) {
		
		try {
			clientCourant=wsMonCompte.mergeClient(clientCourant);
			clientCourant = wsMonCompte.findClientDossier(dossier);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void detachLignesPanier(){
		for (TaLignePanier l : panier.getLignes()) {
			l.setTaPanier(null);
		}
	}
	
	public void enregistrerPanier(ActionEvent event) {
		if(panier==null) {
			panier = new TaPanier();
		}
		
		try {
			if(panier.getIdPanier()==null || panier.getIdPanier()==0){
				GregorianCalendar d = new GregorianCalendar();
				Date maintenant = new Date();
				d.setTime(maintenant);
				XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
				
				panier.setDateCreation(date);
				panier.setDatePaiement(null);
				panier.setLibelle(null);
				panier.setPaye(false);
				panier.setTaClient(clientCourant);
				panier.setTaDossier(dossierCourant);
			}
			
			panier.setTaCategoriePro(selectedTaCategoriePro);		

			panier.setNbMois(nbMois);
			panier.setNbPosteInstalle(nbPoste);
			panier.setNbUtilisateur(nbUtilisateur);
//			panier.setNiveau(version);
			panier.setNiveau(selectedTaTNiveau.getCode());
			panier.setAccesWS(webservice);			

			panier.setCodeRevendeur(null);
			panier.setTauxReduction(null);
			
			
			panier.setTotalHT(totalHT);
			panier.setTotalTTC(totalTTC);
			panier.setTotalTVA(totalTVA);
			
			panier.setTotalHT(fac.getNetHtCalc());
			panier.setTotalTTC(fac.getNetTtcCalc());
			panier.setTotalTVA(fac.getNetTvaCalc());
			detachLignesPanier();
			panier.getLignes().clear();
			TaLignePanier ligne = null;
			for (LignePanierJSF p : selectedModules) {
				ligne=rechercheLignePanierExistante(p.getProduit());
				if(ligne==null)
					ligne = new TaLignePanier();
				ligne.setTaProduit(p.getProduit());
				ligne.setCodeProduit(p.getProduit().getCode());
				ligne.setLibelleProduit(p.getProduit().getLibelle());
				ligne.setTauxTVA(p.getProduit().getTauxTVA());
				
				ligne.setMontantHT(p.getMontantHT());
				ligne.setMontantTTC(p.getMontantTTC());
				ligne.setMontantTVA(p.getMontantTVA());
				
				ligne.setTaPanier(panier);
				if(!panier.getLignes().contains(ligne))
					panier.getLignes().add(ligne);
			}
			panier = wsMonCompte.mergePanier(panier);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ajouteLigneOptionPanier() {
		TaLignePanier lp = null;
		for (TaLFacture lf : fac.getLignes()) {
			if(lf.getTaArticle().getCodeArticle().equals(CODE_ARTICLE_NB_UTILISATEUR)
					|| lf.getTaArticle().getCodeArticle().equals(CODE_ARTICLE_NB_POSTE)
					|| lf.getTaArticle().getCodeArticle().equals(CODE_ARTICLE_WS)) {
				
				lp = new TaLignePanier();
				lp.setCodeProduit(lf.getTaArticle().getCodeArticle());
				lp.setLibelleProduit(lf.getLibLDocument());
				lp.setMontantHT(lf.getMtHtLDocument());
				lp.setMontantTVA(null);
				lp.setMontantTTC(lf.getMtTtcLDocument());
				lp.setTauxTVA(lf.getTauxTvaLDocument());
				lp.setTaPanier(panier);
				panier.getLignes().add(lp);
				lp.setTaProduit(null);
			}
		}
	}
	
	public void actImprimer(ActionEvent actionEvent) {
		
		try {

			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			Map<String,Object> mapEdition = new HashMap<String,Object>();
			
			fac.setCodeDocument(panier.getIdPanier().toString());
			fac.setDateDocument(panier.getDateCreation().toGregorianCalendar().getTime());
			fac.setDateEchDocument(panier.getDateCreation().toGregorianCalendar().getTime());
			fac.setDateLivDocument(panier.getDateCreation().toGregorianCalendar().getTime());
			
			TaTiers taTiers = new TaTiers();
			taTiers.setNomTiers(panier.getTaClient().getNom());
			taTiers.setCodeTiers(panier.getTaClient().getCode());
			TaCompl compl = new TaCompl();
			compl.setTvaIComCompl(panier.getTaClient().getNumeroTva());
			taTiers.setTaCompl(compl);
			fac.setTaTiers(taTiers);
			
			TaInfosFacture taInfosFacture = new TaInfosFacture();
			taInfosFacture.setNomTiers(panier.getTaClient().getNom());
			taInfosFacture.setAdresse1(panier.getTaClient().getAdresse1().getAdresse1());
			taInfosFacture.setAdresse2(panier.getTaClient().getAdresse1().getAdresse2());
			taInfosFacture.setAdresse3(panier.getTaClient().getAdresse1().getAdresse3());
			taInfosFacture.setCodepostal(panier.getTaClient().getAdresse1().getCodePostal());
			taInfosFacture.setVille(panier.getTaClient().getAdresse1().getVille());
			taInfosFacture.setPays(panier.getTaClient().getAdresse1().getPays());
			taInfosFacture.setNomTiers(panier.getTaClient().getNom());
			fac.setTaInfosDocument(taInfosFacture);
			
			fac.setCommentaire("Type de paiement : "+panier.getTaTypePaiement().getLibelle());
			
			TaFacture doc = fac;
			doc.calculeTvaEtTotaux();
			
			mapEdition.put("doc",doc );
			
			TaInfoEntreprise infosEntrepriseLegrain = new TaInfoEntreprise();
			infosEntrepriseLegrain.setNomInfoEntreprise("Legrain Informatique");
			infosEntrepriseLegrain.setDatedebInfoEntreprise(new Date());
			infosEntrepriseLegrain.setDatefinInfoEntreprise(new Date());
			infosEntrepriseLegrain.setWebInfoEntreprise("http://legrain.fr");
			infosEntrepriseLegrain.setEmailInfoEntreprise("legrain@legrain.biz");
			infosEntrepriseLegrain.setTelInfoEntreprise("05.63.30.31.44");
			infosEntrepriseLegrain.setFaxInfoEntreprise("05.63.30.31.67");
			infosEntrepriseLegrain.setAdresse1InfoEntreprise("290 avenue Charles de Gaulle");
			infosEntrepriseLegrain.setCodepostalInfoEntreprise("82000");
			infosEntrepriseLegrain.setVilleInfoEntreprise("Montauban");
			
			mapEdition.put("taInfoEntreprise", infosEntrepriseLegrain);
			
			mapEdition.put("lignes", fac.getLignes());
			
			mapEdition.put("taRReglement", fac.getTaRReglements());
			
			Map<String,Object> mapParametreEdition = new HashMap<String,Object>();
			mapParametreEdition.put("gestionLot", false);
			mapEdition.put("param", mapParametreEdition);
			
			sessionMap.put("edition", mapEdition);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}   
	
//	public boolean equalsProduitAndVersion(TaProduit obj1,Object obj2) {
//		if (this == obj2)
//			return true;
//		if (obj2 == null)
//			return false;
//		if (obj1.getClass() != obj2.getClass())
//			return false;
//		TaProduit other = (TaProduit) obj2;
//		if (obj1.getCode() == null) {
//			if (other.getCode() != null)
//				return false;
//		} else if (!obj1.getCode().equals(other.getCode()))
//			return false;
//		if (obj1.getIdProduit() == null) {
//			if (other.getIdProduit() != null)
//				return false;
//		} else if (!obj1.getIdProduit().equals(other.getIdProduit()))
//			return false;
//		if (obj1.getVersionBrowser() == null) {
//			if (other.getVersionBrowser() != null)
//				return false;
//		} else if (!obj1.getVersionBrowser().equals(other.getVersionBrowser()))
//			return false;
//		if (obj1.getVersionOS() == null) {
//			if (other.getVersionOS() != null)
//				return false;
//		} else if (!obj1.getVersionOS().equals(other.getVersionOS()))
//			return false;
//		if (obj1.getVersionProduit() == null) {
//			if (other.getVersionProduit() != null)
//				return false;
//		} else if (!obj1.getVersionProduit().equals(other.getVersionProduit()))
//			return false;
//		return true;
//	}
//	
//	private TaAutorisation rechercheAutorisationProduitExistante(TaProduit produit){
//		for (TaAutorisation ligne : dossierCourant.getListeAutorisation()) {
//			if(equalsProduitAndVersion(ligne.getTaProduit(),produit))
//				return ligne;
//		}
//		return null;
//	}	
		
	private TaLignePanier rechercheLignePanierExistante(TaProduit produit){
		for (TaLignePanier ligne : panier.getLignes()) {
			if(ligne.getTaProduit()!=null && produit!=null && ligne.getTaProduit().getCode()!=null && produit.getCode()!=null 
					&& ligne.getTaProduit().getCode().equals(produit.getCode()))
				return ligne;
		}
		return null;
	}
		
		
	public String onFlowProcess(FlowEvent event) {
		//http://stackoverflow.com/questions/13009174/is-it-possible-to-update-non-jsf-components-plain-html-with-jsf-ajax
		
		String ID_STEP_PANIER = "panier";
		String ID_STEP_CONTACT = "contact";
		String ID_STEP_VALIDATION = "validation";
		String ID_STEP_MOYEN_PAIEMENT = "moyen-paiement";
		String ID_STEP_PAIEMENT_CB = "paiement-cb";
		String ID_STEP_PAIEMENT_CHEQUE = "paiement-cheque";
		String ID_STEP_PAIEMENT_VIREMENT = "paiement-virement";
		String ID_STEP_PAIEMENT_PRELEVEMENT = "paiement-prelevement";
		String ID_STEP_PAIEMENT_CB_RECURRENT = "paiement-cb-recurrent";
		String ID_STEP_CONFIRMATION = "confirmation";
		
		String ID_DIV_BTN_NAVIGATION = "idFormPanier:idnavpanier";
		String LABEL_BTN_PRECEDENT = "Précédent";
		String LABEL_BTN_SUIVANT = "Suivant";
		String LABEL_BTN_PAYER = "Payer";
		String LABEL_BTN_VALIDER = "Valider";
		String LABEL_BTN_ANNULER = "Annuler";
		
		String MP_RADIO_CARTE_BANCAIRE = "CB";
		String MP_RADIO_CHEQUE = "C";
		String MP_RADIO_VIREMENT = "VIR";
		String MP_RADIO_PRELEVEMENT = "PREL";
		String MP_RADIO_CB_RECURRENT = "CBP";
		
		if(event.getOldStep().equals(ID_STEP_PANIER) && event.getNewStep().equals(ID_STEP_CONTACT)){
			enregistrerPanier(null);
			nextLabel = LABEL_BTN_SUIVANT;
			backLabel = LABEL_BTN_PRECEDENT;
			backLabelVisible =true;
			nextLabelVisible =true;
//			PrimeFaces.current().ajax().update("idFormPanier:idNext");
//			PrimeFaces.current().ajax().update("idFormPanier:idBack");
			PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
		}
		if(event.getOldStep().equals(ID_STEP_CONTACT) && event.getNewStep().equals(ID_STEP_PANIER)){
			backLabelVisible =false;
			nextLabelVisible =true;
			PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
		}
		if(event.getOldStep().equals(ID_STEP_VALIDATION) && event.getNewStep().equals(ID_STEP_CONTACT)){ //arriere
			backLabelVisible =true;
			nextLabelVisible =true;
			PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
		}
		if(event.getOldStep().equals(ID_STEP_CONTACT) && event.getNewStep().equals(ID_STEP_VALIDATION)){
			enregistreCoordonneesClient(null);
			nextLabel = LABEL_BTN_SUIVANT;
			if(cgv) {
				nextLabelVisible = true;
			} else {
				nextLabelVisible = false;
			}
			backLabel = LABEL_BTN_PRECEDENT;
			PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
		}	
		if(    (event.getOldStep().equals(ID_STEP_PAIEMENT_CB) && event.getNewStep().equals(ID_STEP_MOYEN_PAIEMENT))
			|| (event.getOldStep().equals(ID_STEP_PAIEMENT_CHEQUE) && event.getNewStep().equals(ID_STEP_PAIEMENT_CB)) 
			|| (event.getOldStep().equals(ID_STEP_PAIEMENT_VIREMENT) && event.getNewStep().equals(ID_STEP_PAIEMENT_CHEQUE)) 
			|| (event.getOldStep().equals(ID_STEP_PAIEMENT_PRELEVEMENT) && event.getNewStep().equals(ID_STEP_PAIEMENT_VIREMENT)) 
			|| (event.getOldStep().equals(ID_STEP_PAIEMENT_CB_RECURRENT) && event.getNewStep().equals(ID_STEP_PAIEMENT_PRELEVEMENT)) 
						) { //arriere
			nextLabel = LABEL_BTN_SUIVANT;
			backLabel = LABEL_BTN_PRECEDENT;				
			PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
			return ID_STEP_MOYEN_PAIEMENT;
		}
		if(event.getOldStep().equals(ID_STEP_VALIDATION) && event.getNewStep().equals(ID_STEP_MOYEN_PAIEMENT)){
			nextLabel = LABEL_BTN_SUIVANT;
			backLabel = LABEL_BTN_PRECEDENT;
			PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
		}
		if(event.getOldStep().equals(ID_STEP_MOYEN_PAIEMENT) && ( event.getNewStep().equals(ID_STEP_PAIEMENT_CB) || event.getOldStep().equals(ID_STEP_PAIEMENT_CHEQUE)) ){
			nextLabel = LABEL_BTN_PAYER;
			backLabel = LABEL_BTN_ANNULER;
			PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
			
			if(moyenPaiement.equals(MP_RADIO_CARTE_BANCAIRE)) {
				return ID_STEP_PAIEMENT_CB;
			} else if(moyenPaiement.equals(MP_RADIO_CHEQUE)) {
				nextLabel = LABEL_BTN_VALIDER;
				return ID_STEP_PAIEMENT_CHEQUE;
			} else if(moyenPaiement.equals(MP_RADIO_VIREMENT)) {
				nextLabel = LABEL_BTN_VALIDER;
				return ID_STEP_PAIEMENT_VIREMENT;
			} else if(moyenPaiement.equals(MP_RADIO_PRELEVEMENT)) {
				return ID_STEP_PAIEMENT_PRELEVEMENT;
			} else if(moyenPaiement.equals(MP_RADIO_CB_RECURRENT)) {
				return ID_STEP_PAIEMENT_CB_RECURRENT;
			}
		}		
		if(event.getOldStep().equals(ID_STEP_PAIEMENT_CB) && event.getNewStep().equals(ID_STEP_PAIEMENT_CHEQUE)){
			nomCarte = clientCourant.getNom();			
			try {
				paiementCB(null);
				
				nextLabel = LABEL_BTN_SUIVANT;
				backLabel = LABEL_BTN_PRECEDENT;
				backLabelVisible =false;
				nextLabelVisible =false;
				PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
				
				emailConfirmation(null);
				
				return ID_STEP_CONFIRMATION;
				
			} catch (Exception e) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        return event.getOldStep();
			}
		}
		if(event.getOldStep().equals(ID_STEP_PAIEMENT_CHEQUE) && event.getNewStep().equals(ID_STEP_PAIEMENT_VIREMENT)){
			try {
				paiementCheque(null);
				
				nextLabel = LABEL_BTN_SUIVANT;
				backLabel = LABEL_BTN_PRECEDENT;
				backLabelVisible =false;
				nextLabelVisible =false;
				PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
				
				emailConfirmationChequeVirement(null);
				
				return ID_STEP_CONFIRMATION;
				
			} catch (Exception e) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        return event.getOldStep();
			}
		}
		if(event.getOldStep().equals(ID_STEP_PAIEMENT_VIREMENT) && event.getNewStep().equals(ID_STEP_CONFIRMATION)){
			try {
				paiementVirement(null);
				
				nextLabel = LABEL_BTN_SUIVANT;
				backLabel = LABEL_BTN_PRECEDENT;
				backLabelVisible =false;
				nextLabelVisible =false;
				PrimeFaces.current().ajax().update(ID_DIV_BTN_NAVIGATION);
				
				emailConfirmationChequeVirement(null);
				
				return ID_STEP_CONFIRMATION;
				
			} catch (Exception e) {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null);
		        FacesContext.getCurrentInstance().addMessage(null, msg);
		        return event.getOldStep();
			}
		}
		if(event.getOldStep().equals(ID_STEP_CONFIRMATION)){
			
		}
		
//		idNext
//        if(skip) {
//            skip = false;   //reset in case user goes back
//            return "confirm";
//        }
//        else {
            return event.getNewStep();
//        }
    }
	
	public void emailConfirmation(ActionEvent event) {
//		String subject = "mon sujet ..";
//		String msgTxt = "mon email ...";
//		String fromEmail = "nicolas@legrain.fr";
//		String fromName = "Nicolas";
//		String[][] toEmailandName = new String[][]{ {"nico290382@gmail.com","NP"}};
		
		String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de paiement";
//		String msgTxt = "Votre paiement a bien été pris en compte : "+panier.getRefPaiement();
		
		String msgTxt = "Bonjour "+clientCourant.getPrenom() +" "+clientCourant.getNom()+",\n"
				+"\n"
				+"Nous avons bien enregistré votre règlement pour les modules du Bureau de Gestion que vous avez sélectionnés.\n"
				+"\n"
				+"La référence du règlement est : "+panier.getRefPaiement()+"\n"
				+"\n"
				+"Si vous avez besoin d'aide, contactez le support, soit par téléphone au 05.63.30.31.44, soit par email à support@legrain.fr\n"
				+"\n"
				+"Pour bénéficier d'une formation, il suffit de contacter notre service commercial au 05.63.30.31.44. Nous étudierons ensemble votre besoin et les prises en charge possible.\n"
				+"\n"
				+"Très cordialement,\n"
				+"\n"
				+"Service Gestion \n"
				;
		
		String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
		String fromName = "Bureau de Gestion";
		String[][] toEmailandName = new String[][]{ {clientCourant.getTaUtilisateur().getEmail(),clientCourant.getNom()}};
		
		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		
		//envoie d'une copie à legrain
		toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		//TODO faire avec BCC
	}
	
	public void emailConfirmationChequeVirement(ActionEvent event) {
		String subject = bdgProperties.getProperty(BdgProperties.KEY_NOM_DOMAINE)+" - Confirmation de paiement";
		
		String msgTxt = "Bonjour "+clientCourant.getPrenom() +" "+clientCourant.getNom()+",\n"
				+"\n"
				+"Nous vous confirmons avoir bien enregistré votre commande et votre type de règlement et nous vous en remercions.\n"
				+"\n"
				+"Notez que nous validerons votre commande seulement après le traitement de votre règlement.\n"
				+"\n"
				+"Une fois fait, les modules concernés par votre commande seront mis à jour.\n"
				+"\n"
				+"Nous vous remercions de votre confiance.\n"
				+"\n"
				+"Legrain Informatique \n"
				;
		
		String fromEmail = bdgProperties.getProperty(BdgProperties.KEY_SMTP_LOGIN);
		String fromName = "Bureau de Gestion";
		String[][] toEmailandName = new String[][]{ {clientCourant.getTaUtilisateur().getEmail(),clientCourant.getNom()}};
		
		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		
		//envoie d'une copie à legrain
		toEmailandName = new String[][]{ {"notifications@bdg.cloud",null}};
		lgrEmail.sendEmail(subject, msgTxt, fromEmail, fromName, toEmailandName);
		//TODO faire avec BCC
	}
	
	public Boolean panierPaye(){	
		if( panier!=null && panier.isPaye())return true;
		return false;
	}
	
//	public void ajouteAutorisation( TaProduit p,XMLGregorianCalendar dateAchat,XMLGregorianCalendar dateFin,int nbMois) {
//		TaAutorisation auto =rechercheAutorisationProduitExistante(p);
//		if(auto==null){
//			auto=new TaAutorisation();
//			auto.setDateAchat(dateAchat);
//			auto.setDateFin(dateFin);
//			auto.setTaDossier(dossierCourant);
//			auto.setTaProduit(p);
//			auto.setVersionObj(0);
//			dossierCourant.getListeAutorisation().add(auto);
//		}else{
//			XMLGregorianCalendar date;
//			if(auto.getDateFin().compare(dateAchat)== DatatypeConstants.LESSER
//					|| auto.getDateFin().compare(dateAchat)== DatatypeConstants.EQUAL){
//				//la date de fin d'autorisation du module est deja dépassée, la date d'achat deviens la nouvelle date de référence pour calculer la durée
//				date=dateAchat;
//				auto.setDateAchat(dateAchat);
//			} else {
//				//la date de fin de l'autorisation précédente n'est pas encore dépassée, on prolonge la durée à partir de cette date.
//				date=auto.getDateFin();
//			}
//			GregorianCalendar d = new GregorianCalendar();
//			d.setTime(LibDate.incrementDate(date.toGregorianCalendar().getTime(), 0, nbMois, 0));	
//			try {
//				date = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
//			} catch (DatatypeConfigurationException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}		
//			//on récupère la date de fin en courant et on y rajoute le nb de mois payé
//			auto.setDateFin(date);
//		}
//		
//	}
	
	public StreamedContent getFichierCgv() {
		   // InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/images/optimus.jpg");
	       // file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
		try {
			cgvCourant = wsMonCompte.findCgvCourant();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream stream = new ByteArrayInputStream(cgvCourant.getBlobFichier()); 
		fichierCgv = new DefaultStreamedContent(stream,null,"cgv.pdf");
        return fichierCgv;
	}
	
	public void actDialogCgv(ActionEvent actionEvent) {
		    
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
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		try {
			cgvCourant = wsMonCompte.findCgvCourant();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sessionMap.put("cgv", cgv);
        
        PrimeFaces.current().dialog().openDynamic("dialog_cgv", options, params);
	}
	
	public void handleReturnDialogCgv(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			//taTTiers = (TaTTiers) event.getObject();
		}
	}
	
	public String getDossier() {
		return dossier;
	}

	public void setDossier(String nomDossier) {
		this.dossier = nomDossier;
	}

	public List<TaProduit> getListeTousProduit() {
		return listeTousProduit;
	}

	public List<LignePanierJSF> getSelectedModules() {
		return selectedModules;
	}

	public void setSelectedModules(List<LignePanierJSF> selectedModules) {
		this.selectedModules = selectedModules;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public String getEmailEntreprise() {
		return emailEntreprise;
	}

	public void setEmailEntreprise(String emailEntreprise) {
		this.emailEntreprise = emailEntreprise;
	}

	public List<TaAutorisation> getListeAutorisationDossier() {
		return listeAutorisationDossier;
	}

	public void setListeAutorisationDossier(
			List<TaAutorisation> listeAutorisationDossier) {
		this.listeAutorisationDossier = listeAutorisationDossier;
	}

	public int getNbUtilisateur() {
		return nbUtilisateur;
	}

	public void setNbUtilisateur(int nbUtilisateur) {
		this.nbUtilisateur = nbUtilisateur;
	}

	public int getNbMois() {
		return nbMois;
	}

	public void setNbMois(int nbMois) {
		this.nbMois = nbMois;
	}

	public boolean isTousLesModules() {
		return tousLesModules;
	}

	public void setTousLesModules(boolean tousLesModules) {
		this.tousLesModules = tousLesModules;
	}

	public int getNbPoste() {
		return nbPoste;
	}

	public void setNbPoste(int nbPoste) {
		this.nbPoste = nbPoste;
	}

	public boolean isWebservice() {
		return webservice;
	}

	public void setWebservice(boolean webservice) {
		this.webservice = webservice;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public BigDecimal getTotalHT() {
		return totalHT;
	}

	public void setTotalHT(BigDecimal totalHT) {
		this.totalHT = totalHT;
	}

	public BigDecimal getTotalTTC() {
		return totalTTC;
	}

	public void setTotalTTC(BigDecimal totalTTC) {
		this.totalTTC = totalTTC;
	}

	public BigDecimal getTotalTVA() {
		return totalTVA;
	}

	public void setTotalTVA(BigDecimal totalTVA) {
		this.totalTVA = totalTVA;
	}

	public TaPanier getPanier() {
		return panier;
	}

	public void setPanier(TaPanier panier) {
		this.panier = panier;
	}

	public TaDossier getDossierCourant() {
		return dossierCourant;
	}

	public void setDossierCourant(TaDossier dossierCourant) {
		this.dossierCourant = dossierCourant;
	}

	public String getNomCarte() {
		return nomCarte;
	}

	public void setNomCarte(String nomCarte) {
		this.nomCarte = nomCarte;
	}

	public int getMoisCarte() {
		return moisCarte;
	}

	public void setMoisCarte(int moisCarte) {
		this.moisCarte = moisCarte;
	}

	public int getAnneeCarte() {
		return anneeCarte;
	}

	public void setAnneeCarte(int anneeCarte) {
		this.anneeCarte = anneeCarte;
	}

	public String getCryptoCarte() {
		return cryptoCarte;
	}

	public void setCryptoCarte(String cryptoCarte) {
		this.cryptoCarte = cryptoCarte;
	}

	public String getNumCarte() {
		return numCarte;
	}

	public void setNumCarte(String numCarte) {
		this.numCarte = numCarte;
	}

	public String getTypeCarte() {
		return typeCarte;
	}

	public void setTypeCarte(String typeCarte) {
		this.typeCarte = typeCarte;
	}

	public List<LignePanierJSF> getLignesPanierJSF() {
		return lignesPanierJSF;
	}

	public void setLignesPanierJSF(List<LignePanierJSF> lignesPanierJSF) {
		this.lignesPanierJSF = lignesPanierJSF;
	}

	public List<TaPrixParUtilisateur> getListPrixParUtilisateur() {
		return listPrixParUtilisateur;
	}

	public void setListPrixParUtilisateur(
			List<TaPrixParUtilisateur> listPrixParUtilisateur) {
		this.listPrixParUtilisateur = listPrixParUtilisateur;
	}

	public List<TaPrixParUtilisateurPerso> getListPrixParUtilisateurPerso() {
		return listPrixParUtilisateurPerso;
	}

	public void setListPrixParUtilisateurPerso(
			List<TaPrixParUtilisateurPerso> listPrixParUtilisateurPerso) {
		this.listPrixParUtilisateurPerso = listPrixParUtilisateurPerso;
	}

	public TaPrixParUtilisateur getSelectedTaPrixParUtilisateur() {
		return selectedTaPrixParUtilisateur;
	}

	public void setSelectedTaPrixParUtilisateur(
			TaPrixParUtilisateur selectedTaPrixParUtilisateur) {
		this.selectedTaPrixParUtilisateur = selectedTaPrixParUtilisateur;
	}

	public TaPrixParUtilisateurPerso getSelectedTaPrixParUtilisateurPerso() {
		return selectedTaPrixParUtilisateurPerso;
	}

	public void setSelectedTaPrixParUtilisateurPerso(
			TaPrixParUtilisateurPerso selectedTaPrixParUtilisateurPerso) {
		this.selectedTaPrixParUtilisateurPerso = selectedTaPrixParUtilisateurPerso;
	}

	public boolean isPrixPersoParUtilisateur() {
		return prixPersoParUtilisateur;
	}

	public void setPrixPersoParUtilisateur(boolean prixPersoParUtilisateur) {
		this.prixPersoParUtilisateur = prixPersoParUtilisateur;
	}

	public TaClient getClientCourant() {
		return clientCourant;
	}

	public void setClientCourant(TaClient clientCourant) {
		this.clientCourant = clientCourant;
	}

	public List<TaTNiveau> getListeTaTNiveau() {
		return listeTaTNiveau;
	}

	public void setListeTaTNiveau(List<TaTNiveau> listeTaTNiveau) {
		this.listeTaTNiveau = listeTaTNiveau;
	}

	public TaTNiveau getSelectedTaTNiveau() {
		return selectedTaTNiveau;
	}

	public void setSelectedTaTNiveau(TaTNiveau selectedTaTNiveau) {
		this.selectedTaTNiveau = selectedTaTNiveau;
	}

	public TaFacture getFac() {
		return fac;
	}

	public int[] getListeMoisCB() {
		return listeMoisCB;
	}

	public void setListeMoisCB(int[] listeMoisCB) {
		this.listeMoisCB = listeMoisCB;
	}

	public int[] getListeAnneeCB() {
		return listeAnneeCB;
	}

	public void setListeAnneeCB(int[] listeAnneeCB) {
		this.listeAnneeCB = listeAnneeCB;
	}

	public String getNextLabel() {
		return nextLabel;
	}

	public void setNextLabel(String nextLabel) {
		this.nextLabel = nextLabel;
	}

	public String getBackLabel() {
		return backLabel;
	}

	public void setBackLabel(String backLabel) {
		this.backLabel = backLabel;
	}

	public boolean isNextLabelVisible() {
		return nextLabelVisible;
	}

	public void setNextLabelVisible(boolean nextLabelVisible) {
		this.nextLabelVisible = nextLabelVisible;
	}

	public boolean isBackLabelVisible() {
		return backLabelVisible;
	}

	public void setBackLabelVisible(boolean backLabelVisible) {
		this.backLabelVisible = backLabelVisible;
	}

	public boolean isCgv() {
		return cgv;
	}

	public void setCgv(boolean cgv) {
		this.cgv = cgv;
		
		nextLabelVisible =cgv;
		PrimeFaces.current().ajax().update("idFormPanier:idnavpanier");
	}

	public List<TaCategoriePro> getListeTaCategoriePro() {
		return listeTaCategoriePro;
	}

	public void setListeTaCategoriePro(List<TaCategoriePro> listeTaCategoriePro) {
		this.listeTaCategoriePro = listeTaCategoriePro;
	}

	public TaCategoriePro getSelectedTaCategoriePro() {
		return selectedTaCategoriePro;
	}

	public void setSelectedTaCategoriePro(TaCategoriePro selectedTaCategoriePro) {
		this.selectedTaCategoriePro = selectedTaCategoriePro;
	}

	public String getParamPartenaire() {
		return paramPartenaire;
	}

	public void setParamPartenaire(String paramPartenaire) {
		this.paramPartenaire = paramPartenaire;
	}


	public String getMoyenPaiement() {
		return moyenPaiement;
	}


	public void setMoyenPaiement(String moyenPaiement) {
		this.moyenPaiement = moyenPaiement;
	}
	
}  
              
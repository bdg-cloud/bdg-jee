package fr.legrain.bdg.webapp.recherche.multicritere;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import fr.legrain.article.model.TaArticle;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.recherche.multicritere.service.remote.IRechercheMultiCritereServiceRemote;
import fr.legrain.bdg.webapp.app.EmailParam;
import fr.legrain.bdg.webapp.app.SmsParam;
import fr.legrain.document.model.TaAcompte;
import fr.legrain.document.model.TaApporteur;
import fr.legrain.document.model.TaAvoir;
import fr.legrain.document.model.TaBoncde;
import fr.legrain.document.model.TaBonliv;
import fr.legrain.document.model.TaDevis;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaProforma;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.recherche.multicritere.model.GroupeLigne;
import fr.legrain.recherche.multicritere.model.LigneArticle;
import fr.legrain.recherche.multicritere.model.LigneCritere;
import fr.legrain.recherche.multicritere.model.LigneDocument;
import fr.legrain.recherche.multicritere.model.LigneTiers;
import fr.legrain.sms.model.TaMessageSMS;
import fr.legrain.tiers.model.TaEmail;
import fr.legrain.tiers.model.TaTelephone;
import fr.legrain.tiers.model.TaTiers;

@Named
@ViewScoped 
public class RechercheMulticritereJSF implements Serializable {

	private static final long serialVersionUID = 5609515226922826051L;
	
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	@EJB private IRechercheMultiCritereServiceRemote rechercheMultiCritereService;
	
	private List<GroupeLigneJSF> listeGroupe = new LinkedList<>();
	private ArrayList<Object> resultatRequete = null;
	private List<Object> selectedResultatRequete = null;
	
	private List<String> listeTypeResultat = new LinkedList<>();
	private String selectedTypeResultat = null;
	
	private List<String> listeTypeCommunication = new LinkedList<>();
	private String selectedTypeCommunication = null;
	
	private List<String> listeModeUtilisation = new LinkedList<>();
	private String selectedModeUtilisation = null;
	
	private static final String COMBO_TEXTE_DEFAUT_CHOISIR = "<Choisir>";
	
	private static final String TYPE_RESULTAT_TIERS = "Tiers";
	private static final String TYPE_RESULTAT_ARTICLES = "Articles";
	private static final String TYPE_RESULTAT_DOCUMENTS = "Documents";
	
	private static final String TYPE_CORRESPONDANCE_TOUS = "Tous";
	private static final String TYPE_CORRESPONDANCE_COMMERCIAL = "Commercial";
	private static final String TYPE_CORRESPONDANCE_ADMINISTRATIF = "Administratif";
	
	private static final String TYPE_MODE_UTILISATION_LISTE = "Liste";
	private static final String TYPE_MODE_UTILISATION_EMAIL = "Email";
	private static final String TYPE_MODE_UTILISATION_SMS = "SMS";
	private static final String TYPE_MODE_UTILISATION_PUBLIPOSTAGE = "Publipostage";
	private static final String TYPE_MODE_UTILISATION_ETIQUETTE = "Etiquettes";
	private static final String TYPE_MODE_UTILISATION_FAX = "FAX";
	
	public RechercheMulticritereJSF() {
		//this.rechercheMultiCritereService = rechercheMultiCritereService;
		init();
	}
	
	public void init() {
		init(null);
	}
		
	public void init(RechercheMulticritereJSF modele) {
		listeTypeResultat.add(COMBO_TEXTE_DEFAUT_CHOISIR);
		listeTypeResultat.add(TYPE_RESULTAT_TIERS);
		listeTypeResultat.add(TYPE_RESULTAT_ARTICLES);
		listeTypeResultat.add(TYPE_RESULTAT_DOCUMENTS);
		
		listeTypeCommunication.add(TYPE_CORRESPONDANCE_TOUS);
		listeTypeCommunication.add(TYPE_CORRESPONDANCE_COMMERCIAL);
		listeTypeCommunication.add(TYPE_CORRESPONDANCE_ADMINISTRATIF);
		
		
		
		actCreerGroupe(null);
	}
	
	public void actInitTypeAction(/*AjaxBehaviorEvent e*/){
		if(selectedTypeResultat!=null) {
			listeModeUtilisation.clear();
			selectedModeUtilisation = null;
			if(selectedTypeResultat.equals(TYPE_RESULTAT_TIERS)) {
				listeModeUtilisation.add(TYPE_MODE_UTILISATION_LISTE);
				listeModeUtilisation.add(TYPE_MODE_UTILISATION_EMAIL);
				listeModeUtilisation.add(TYPE_MODE_UTILISATION_SMS);
//				listeModeUtilisation.add(TYPE_MODE_UTILISATION_PUBLIPOSTAGE);
//				listeModeUtilisation.add(TYPE_MODE_UTILISATION_ETIQUETTE);
//				listeModeUtilisation.add(TYPE_MODE_UTILISATION_FAX);
//			} else if(selectedTypeResultat.equals(TYPE_RESULTAT_ARTICLES)) {
//				listeModeUtilisation.add(TYPE_MODE_UTILISATION_ETIQUETTE);
			}
		} else if(selectedTypeResultat.equals(COMBO_TEXTE_DEFAUT_CHOISIR)) {
			selectedTypeResultat = null;
		}
	}

	public void actReinitialise(ActionEvent actionEvent){
		listeModeUtilisation.clear();
		selectedModeUtilisation = null;
		selectedTypeResultat = null;
	}
	
	public void actCreerGroupe(ActionEvent actionEvent){
		GroupeLigneJSF g = new GroupeLigneJSF(listeGroupe.size()+1);
		g.getGroupeLigne().setLibelleGroupe("Groupe "+g.getGroupeLigne().getNumero());
		listeGroupe.add(g);
	}
	
	public void actSupprimerGroupe(GroupeLigneJSF g){
		listeGroupe.remove(g);
	}
	
	public void actAjouterCritereTiers(GroupeLigneJSF g){
		if(listeGroupe.isEmpty()) {
			actCreerGroupe(null);
			g = listeGroupe.get(0);
		}
		LigneTiers ligneTiers = new LigneTiers(g.getGroupeLigne());
		ligneTiers.setType(LigneCritere.TYPE_LIGNE_CRITERE_TIERS);
		g.getGroupeLigne().getGroupe().add(ligneTiers);
	}
	
	public void actAjouterCritereArticle(GroupeLigneJSF g){
		if(listeGroupe.isEmpty()) {
			actCreerGroupe(null);
			g = listeGroupe.get(0);
		}
		LigneArticle ligneArticle = new LigneArticle(g.getGroupeLigne());
		ligneArticle.setType(LigneCritere.TYPE_LIGNE_CRITERE_ARTICLE);
		g.getGroupeLigne().getGroupe().add(ligneArticle);
	}
	
	public void actAjouterCritereDocument(GroupeLigneJSF g, String type){
		if(listeGroupe.isEmpty()) {
			actCreerGroupe(null);
			g = listeGroupe.get(0);
		}
		LigneDocument ligneDocument = new LigneDocument(g.getGroupeLigne(), type);
		ligneDocument.setType(type);
		g.getGroupeLigne().getGroupe().add(ligneDocument);
	}
	
	public void actAjouterCritereFacture(GroupeLigneJSF g){
		actAjouterCritereDocument(g,LigneCritere.TYPE_LIGNE_CRITERE_FACTURE);
	}
	
	public void actAjouterCritereDevis(GroupeLigneJSF g){
		actAjouterCritereDocument(g,LigneCritere.TYPE_LIGNE_CRITERE_DEVIS);
	}
	
	public void actAjouterCritereCommande(GroupeLigneJSF g){
		actAjouterCritereDocument(g,LigneCritere.TYPE_LIGNE_CRITERE_COMMANDE);
	}
	
	public void actAjouterCritereAvoir(GroupeLigneJSF g){
		actAjouterCritereDocument(g,LigneCritere.TYPE_LIGNE_CRITERE_AVOIR);
	}
	
	public void actAjouterCritereApporteur(GroupeLigneJSF g){
		actAjouterCritereDocument(g,LigneCritere.TYPE_LIGNE_CRITERE_APPORTEUR);
	}
	
	public void actAjouterCritereAcompte(GroupeLigneJSF g){
		actAjouterCritereDocument(g,LigneCritere.TYPE_LIGNE_CRITERE_ACOMPTE);
	}
	
	public void actAjouterCritereProforma(GroupeLigneJSF g){
		actAjouterCritereDocument(g,LigneCritere.TYPE_LIGNE_CRITERE_PROFORMA);
	}
	
	public void actAjouterCritereLivraison(GroupeLigneJSF g){
		actAjouterCritereDocument(g,LigneCritere.TYPE_LIGNE_CRITERE_LIVRAISON);
	}
	
	public void actRechercher(ActionEvent actionEvent){
		boolean groupesOK = listeGroupe.size() != 0 ; // booléen de verif contenu groupes
		boolean ligneOK = true;
		boolean aucuneErreurLignes = true;
		for(int i =0; i < listeGroupe.size() && groupesOK; i++){
			groupesOK = listeGroupe.get(i).getGroupeLigne().getSize() != 0;
		}
		for(int i =0; i < listeGroupe.size(); i++){
			for (int j=0; j< listeGroupe.get(i).getGroupeLigne().getGroupe().size(); j++) {
				ligneOK = listeGroupe.get(i).getGroupeLigne().getGroupe().get(j).isValeur1Enabled() 
						|| (!listeGroupe.get(i).getGroupeLigne().getGroupe().get(j).isValeur1Enabled() 
								&& listeGroupe.get(i).getGroupeLigne().getGroupe().get(j).isBooleenEnabled()
							);
				listeGroupe.get(i).getGroupeLigne().getGroupe().get(j).setWarningVisible(!ligneOK);
				if (!ligneOK){
					aucuneErreurLignes = false;
				}
			}
		}
		if (!groupesOK){
			String ttlErreur = "Groupes non remplis";
			String msgErreur = "Au moins un des groupes ne contient pas de critères." +
			"\n Veuillez le(s) remplir ou le(s) supprimer.";
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ttlErreur, msgErreur));
		} else if (!aucuneErreurLignes){
			String ttlErreur = "Lignes non complètes";
			String msgErreur = "Vous devez compléter chaque ligne de critère ajoutée afin de valider la recherche.";
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ttlErreur, msgErreur));

		} else if (selectedTypeResultat.equals("<Choisir>")){
			String ttlErreur = "Résultat non sélectionné";
			String msgErreur = "Vous devez sélectionner le type de résultat que vous souhaitez afficher (Etape 1).";
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ttlErreur, msgErreur));

		} else { 

//			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
//				public void run() {	

					List<GroupeLigne> l = new LinkedList<>();
					for (GroupeLigneJSF groupeLigne : listeGroupe) {
						l.add(groupeLigne.getGroupeLigne());
					}
					resultatRequete = new ArrayList<>();
					resultatRequete = rechercheMultiCritereService.getResultat(selectedTypeResultat,l);
					//larequete = new Requete(selectedTypeResultat,listeGroupe);
//					ArrayList<Object> resultatRequete = larequete.getResultat();

					if (resultatRequete.size()==0){
//						MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//								"Aucun résultat trouvé", "Les critères de votre recherche ne correspondent à aucun résultat.");
						FacesContext context = FacesContext.getCurrentInstance();  
						context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aucun résultat trouvé", 
								"Les critères de votre recherche ne correspondent à aucun résultat."));
					}
					
					System.out.println("RechercheMultiCritereController.actRechercher() *********************************");

					if (resultatRequete.get(0) instanceof TaTiers){
//						masterController.getMessengerPage().creerFeuilleTiers(resultatRequete);
					} else if (resultatRequete.get(0) instanceof TaArticle){
//						masterController.getMessengerPage().creerFeuilleArticle(resultatRequete);
					} else {
//						masterController.getMessengerPage().creerFeuilleDocument(resultatRequete);
					}
//					masterController.getMessengerPage().changeOngletResultats();
//				}
//			});



		}
	}
	
	public void actPrefiltrerPourEmail(ActionEvent actionEvent){
		
	}
	
	public void actEnvoyerEmail(ActionEvent actionEvent){
		List<String> listeAdresseEmail = new ArrayList<>();
		List<TaEmail> listeTaEmail = new ArrayList<>();
		for (Object o : selectedResultatRequete) {
			if( ((TaTiers)o).getTaEmail()!=null 
					&& ((TaTiers)o).getTaEmail().getAdresseEmail()!=null) {
				listeAdresseEmail.add(((TaTiers)o).getTaEmail().getAdresseEmail());
				listeTaEmail.add(((TaTiers)o).getTaEmail());
			}
		}

		EmailParam email = new EmailParam();
		email.setEmailExpediteur(null);
		email.setNomExpediteur(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()); 
//		email.setSubject(taInfoEntrepriseService.findInstance().getNomInfoEntreprise()+" "+nomDocument+" "+masterEntity.getCodeDocument()); 
//		email.setBodyPlain("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
//		email.setBodyHtml("Votre "+nomDocument+" "+ masterEntity.getCodeDocument());
//		email.setDestinataires(new String[]{masterEntity.getTaTiers().getTaEmail().getAdresseEmail()});
//		email.setEmailDestinataires(new TaEmail[]{masterEntity.getTaTiers().getTaEmail()});
		
		email.setSubject(""); 
		email.setBodyPlain("");
		email.setBodyHtml("");
		String[] t = new String[1];
		TaEmail[] t1 = new TaEmail[1];
//		email.setDestinataires(listeAdresseEmail.toArray(t));
		email.setEmailDestinataires(listeTaEmail.toArray(t1));
		
//		EmailPieceJointeParam pj1 = new EmailPieceJointeParam();
//		pj1.setFichier(new File((taDocumentService).generePDF(masterEntity.getIdDocument())));
//		pj1.setTypeMime("pdf");
//		pj1.setNomFichier(pj1.getFichier().getName());
//		email.setPiecesJointes(
//				new EmailPieceJointeParam[]{pj1}
//				);
		actDialogEmail(email);
	}
	
	public void actDialogEmail(EmailParam email) {
		 Map<String,Object> options = new HashMap<String, Object>();
		    options.put("modal", true);
		    options.put("draggable", false);
		    options.put("closable", false);
		    options.put("resizable", true);
		    options.put("contentHeight", 710);
		    options.put("contentWidth", "100%");
		    options.put("width", 1024);
		    
		    Map<String,List<String>> params = new HashMap<String,List<String>>();
		    
		    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> sessionMap = externalContext.getSessionMap();
			
			sessionMap.put(EmailParam.NOM_OBJET_EN_SESSION, email);
		    PrimeFaces.current().dialog().openDynamic("/dialog_email", options, params);	
	}

	public void handleReturnDialogEmail(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaMessageEmail j = (TaMessageEmail) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("Email", "Email envoyée ")); 
		}
	}
	
	public void actPrefiltrerPourSms(ActionEvent actionEvent){
		
	}

	public void actEnvoyerSms(ActionEvent actionEvent){
		List<String> listeNumeroTelephone = new ArrayList<>();
		List<TaTelephone> listeTaTelephone = new ArrayList<>();
		for (Object o : selectedResultatRequete) {
			if( ((TaTiers)o).getTaTelephone()!=null 
					&& ((TaTiers)o).getTaTelephone().getNumeroTelephone()!=null) {
				listeNumeroTelephone.add(((TaTiers)o).getTaTelephone().getNumeroTelephone());
				listeTaTelephone.add(((TaTiers)o).getTaTelephone());
			}
		}
		
		SmsParam sms = new SmsParam();
		sms.setNumeroExpediteur(null);
		
		String[] t = new String[1];
		TaTelephone[] t1 = new TaTelephone[1];
//		sms.setDestinataires(listeNumeroTelephone.toArray(t));
		sms.setTelephoneDestinataires(listeTaTelephone.toArray(t1));
		actDialogSms(sms);
	}
	
	public void actDialogSms(SmsParam sms) {
	    
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", false);
        options.put("closable", false);
        options.put("resizable", true);
        options.put("contentHeight", 710);
        options.put("contentWidth", "100%");
        options.put("width", 1024);
        
        Map<String,List<String>> params = new HashMap<String,List<String>>(); 
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		
		sessionMap.put(SmsParam.NOM_OBJET_EN_SESSION, sms);
        PrimeFaces.current().dialog().openDynamic("/dialog_sms", options, params);    
	}
	
	public void handleReturnDialogSms(SelectEvent event) {
		if(event!=null && event.getObject()!=null) {
			TaMessageSMS j = (TaMessageSMS) event.getObject();
			
			FacesContext context = FacesContext.getCurrentInstance();  
			context.addMessage(null, new FacesMessage("SMS", "SMS envoyé ")); 
		}
	}
	
	public void actPrefiltrerPourPublipostage(ActionEvent actionEvent){
		
	}

	public void actGenererPublipostage(ActionEvent actionEvent){
		for (Object o : selectedResultatRequete) {
			System.out.println("RechercheMulticritereJSF.actGenererPublipostage()");
		}
	}
	
	public void actPrefiltrerPourEtiquetteAdresse(ActionEvent actionEvent){
		
	}

	public void actGenererEtiquetteAdresse(ActionEvent actionEvent){
		
	}
	
	public boolean afficheColonne(String colonne) {
		boolean visible = true;
		if(selectedTypeResultat!=null && selectedTypeResultat.equals(TYPE_RESULTAT_TIERS)) {
			if(selectedModeUtilisation!=null) {
				String titreColonneCode = "Code";
				String titreColonneNomEntreprise = "Nom Entreprise";
				String titreColonneNom = "Nom";
				String titreColonnePrenom = "Prénom";
				String titreColonneMail = "Mail";
				String titreColonneTelephone = "Téléphone";
				String titreColonneAdresse1 = "Adresse 1";
				String titreColonneAdresse2 = "Adresse 2";
				String titreColonneAdresse3 = "Adresse 3";
				String titreColonneCodePostal = "Code Postal";
				String titreColonneVille = "Ville";
				String titreColonnePays = "Pays";
				if(selectedModeUtilisation.equals(TYPE_MODE_UTILISATION_EMAIL)) {
					//code, nom ets, nom, prenom, email
					List<String> titreColonneAAfficherEmail = new ArrayList<>();
					titreColonneAAfficherEmail.add(titreColonneCode);
					titreColonneAAfficherEmail.add(titreColonneNomEntreprise);
					titreColonneAAfficherEmail.add(titreColonneNom);
					titreColonneAAfficherEmail.add(titreColonnePrenom);
					titreColonneAAfficherEmail.add(titreColonneMail);
					visible = titreColonneAAfficherEmail.contains(colonne);
				} else if(selectedModeUtilisation.equals(TYPE_MODE_UTILISATION_SMS) || selectedModeUtilisation.equals(TYPE_MODE_UTILISATION_FAX)) {
					//code, nom ets, nom, prenom, telephone.
					List<String> titreColonneAAfficherTelephone = new ArrayList<>();
					titreColonneAAfficherTelephone.add(titreColonneCode);
					titreColonneAAfficherTelephone.add(titreColonneNomEntreprise);
					titreColonneAAfficherTelephone.add(titreColonneNom);
					titreColonneAAfficherTelephone.add(titreColonnePrenom);
					titreColonneAAfficherTelephone.add(titreColonneTelephone);
					visible = titreColonneAAfficherTelephone.contains(colonne);
				} else if(selectedModeUtilisation.equals(TYPE_MODE_UTILISATION_PUBLIPOSTAGE) || selectedModeUtilisation.equals(TYPE_MODE_UTILISATION_ETIQUETTE)) {
					//code, nom ets, nom, prenom, adr1, adr2, adr3, cp, ville, pays
					List<String> titreColonneAAfficherCourrier = new ArrayList<>();
					titreColonneAAfficherCourrier.add(titreColonneCode);
					titreColonneAAfficherCourrier.add(titreColonneNomEntreprise);
					titreColonneAAfficherCourrier.add(titreColonneNom);
					titreColonneAAfficherCourrier.add(titreColonnePrenom);
					titreColonneAAfficherCourrier.add(titreColonneAdresse1);
					titreColonneAAfficherCourrier.add(titreColonneAdresse2);
					titreColonneAAfficherCourrier.add(titreColonneAdresse3);
					titreColonneAAfficherCourrier.add(titreColonneCodePostal);
					titreColonneAAfficherCourrier.add(titreColonneVille);
					titreColonneAAfficherCourrier.add(titreColonnePays);
					visible = titreColonneAAfficherCourrier.contains(colonne);
				} else { //Liste
					//tout
					List<String> titreColonneACacherListe = new ArrayList<>();
					titreColonneACacherListe.add(titreColonneAdresse1);
					titreColonneACacherListe.add(titreColonneAdresse2);
					titreColonneACacherListe.add(titreColonneAdresse3);
					visible = !titreColonneACacherListe.contains(colonne);
				}
			}
		} else if(selectedTypeResultat!=null && selectedTypeResultat.equals(TYPE_RESULTAT_ARTICLES)) {
			
		} else if(selectedTypeResultat!=null && selectedTypeResultat.equals(TYPE_RESULTAT_DOCUMENTS)) {
			if(resultatRequete!=null && !resultatRequete.isEmpty()) { 
				String titreColonneResteAPayer = "Reste à Payer";
				String titreColonneResteAReglement = "Règlement";
				String titreColonneResteADateEcheance = "Date échéance";
				if(resultatRequete.get(0) instanceof TaFacture) {
				
				} else if(resultatRequete.get(0) instanceof TaDevis) {
					List<String> titreColonneACacher = new ArrayList<>();
					titreColonneACacher.add(titreColonneResteAPayer);
					visible = !titreColonneACacher.contains(colonne);
				} else if(resultatRequete.get(0) instanceof TaBoncde) {
					List<String> titreColonneACacher = new ArrayList<>();
					titreColonneACacher.add(titreColonneResteAPayer);
					visible = !titreColonneACacher.contains(colonne);
				} else if(resultatRequete.get(0) instanceof TaApporteur) {
					List<String> titreColonneACacher = new ArrayList<>();
					titreColonneACacher.add(titreColonneResteAPayer);
					visible = !titreColonneACacher.contains(colonne);
				} else if(resultatRequete.get(0) instanceof TaProforma) {
					List<String> titreColonneACacher = new ArrayList<>();
					titreColonneACacher.add(titreColonneResteAPayer);
					visible = !titreColonneACacher.contains(colonne);
				} else if(resultatRequete.get(0) instanceof TaBonliv) {
					List<String> titreColonneACacher = new ArrayList<>();
					titreColonneACacher.add(titreColonneResteADateEcheance);
					titreColonneACacher.add(titreColonneResteAReglement);
					titreColonneACacher.add(titreColonneResteAPayer);
					visible = !titreColonneACacher.contains(colonne);
				} else if(resultatRequete.get(0) instanceof TaAvoir) {
					List<String> titreColonneACacher = new ArrayList<>();
					titreColonneACacher.add(titreColonneResteAPayer);
					visible = !titreColonneACacher.contains(colonne);
				} else if(resultatRequete.get(0) instanceof TaAcompte) {
					List<String> titreColonneACacher = new ArrayList<>();
					titreColonneACacher.add(titreColonneResteAPayer);
					visible = !titreColonneACacher.contains(colonne);
				}
			}
		}
		return visible;
	}

	public List<GroupeLigneJSF> getListeGroupe() {
		return listeGroupe;
	}

	public void setListeGroupe(List<GroupeLigneJSF> listeGroupe) {
		this.listeGroupe = listeGroupe;
	}

	public List<String> getListeTypeResultat() {
		return listeTypeResultat;
	}

	public void setListeTypeResultat(List<String> listeTypeResultat) {
		this.listeTypeResultat = listeTypeResultat;
	}

	public String getSelectedTypeResultat() {
		return selectedTypeResultat;
	}

	public void setSelectedTypeResultat(String selectedTypeResultat) {
		this.selectedTypeResultat = selectedTypeResultat;
	}

	public ArrayList<Object> getResultatRequete() {
		return resultatRequete;
	}

	public void setResultatRequete(ArrayList<Object> resultatRequete) {
		this.resultatRequete = resultatRequete;
	}

	public List<Object> getSelectedResultatRequete() {
		return selectedResultatRequete;
	}

	public void setSelectedResultatRequete(List<Object> selectedResultatRequete) {
		this.selectedResultatRequete = selectedResultatRequete;
	}

	public List<String> getListeTypeCommunication() {
		return listeTypeCommunication;
	}

	public void setListeTypeCommunication(List<String> listeTypeCommunication) {
		this.listeTypeCommunication = listeTypeCommunication;
	}

	public String getSelectedTypeCommunication() {
		return selectedTypeCommunication;
	}

	public void setSelectedTypeCommunication(String selectedTypeCommunication) {
		this.selectedTypeCommunication = selectedTypeCommunication;
	}

	public List<String> getListeModeUtilisation() {
		return listeModeUtilisation;
	}

	public void setListeModeUtilisation(List<String> listeModeUtilisation) {
		this.listeModeUtilisation = listeModeUtilisation;
	}

	public String getSelectedModeUtilisation() {
		return selectedModeUtilisation;
	}

	public void setSelectedModeUtilisation(String selectedModeUtilisation) {
		this.selectedModeUtilisation = selectedModeUtilisation;
	}

}

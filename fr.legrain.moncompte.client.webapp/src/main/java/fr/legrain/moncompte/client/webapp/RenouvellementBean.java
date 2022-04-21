//package fr.legrain.moncompte.client.webapp;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.ejb.FinderException;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//import javax.faces.context.FacesContext;
//import javax.faces.event.ActionEvent;
//import javax.xml.datatype.DatatypeConfigurationException;
//import javax.xml.datatype.DatatypeFactory;
//import javax.xml.datatype.XMLGregorianCalendar;
//
//import fr.legrain.bdg.moncompte.service.remote.ITaClientServiceRemote;
//import fr.legrain.bdg.moncompte.service.remote.ITaDossierServiceRemote;
//import fr.legrain.bdg.moncompte.service.remote.ITaProduitServiceRemote;
//import fr.legrain.bdg.moncompte.service.remote.ITaUtilisateurServiceRemote;
//import fr.legrain.bdg.ws.client.Admin;
//import fr.legrain.lib.data.LibDate;
//import fr.legrain.moncompte.model.TaAutorisation;
//import fr.legrain.moncompte.model.TaDossier;
//import fr.legrain.moncompte.model.TaProduit;
//
//
//@ManagedBean
//@ViewScoped
////@SessionScoped 
//public class RenouvellementBean implements Serializable {  
//	
//
//	private static final long serialVersionUID = 1331238708223531056L;
//
////	private @EJB IAdministrationServiceRemote adminService;
////	private MonCompteWebServiceClientCXF wsMonCompte = null;
////	private AutorisationWebServiceClientCXF wsAutorisation = null;
////	private @EJB IDatabaseServiceLocal adminService;
//	
//	private String nomEntreprise;	
//	private String emailEntreprise;
//	private String dossier;
//	private List<String> listeDossier;
//	private List<TaProduit> listeTousProduit;
//	private TaDossier dossierCourant = null;
//	
//	private int nbUtilisateur;
//	private int nbMois;
//	
//	private List<TaProduit> selectedModules;
//	
//	private List<TaAutorisation> listeAutorisationDossier = null;
//	
//	
//	private int idProduit = 4;
//	private boolean tousLesModules = true;
//	
//	@EJB private ITaProduitServiceRemote taProduitService;
//	@EJB private ITaClientServiceRemote taClientService;
//	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
//	@EJB private ITaDossierServiceRemote taDossierService;
//
//		
//	public void refresh() {
//		try {
//			dossierCourant = taDossierService.findByCode(dossier);
//			
//			listeAutorisationDossier = dossierCourant.getListeAutorisation();
//			List<TaAutorisation> listeAutorisationVendable = new ArrayList<TaAutorisation>();
//			for (TaAutorisation aut : listeAutorisationDossier) {
//				if(aut.getTaProduit().getVendable()!=null && aut.getTaProduit().getVendable()) {
//					listeAutorisationVendable.add(aut);
//				}
//			}
//			listeAutorisationDossier = listeAutorisationVendable;
//		} catch (FinderException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@PostConstruct
//	public void init() {
////		initBdd();
//		
////		try {
////			wsMonCompte = new MonCompteWebServiceClientCXF();
////			wsAutorisation = new AutorisationWebServiceClientCXF();
////		} catch (FileNotFoundException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		initListeModule();
//	}
//	
//	
//	public void initListeModule() {
////		try {
//			listeTousProduit = taProduitService.selectAll();
////		} catch (RemoteException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		List<TaProduit> listeModuleVendable = new ArrayList<TaProduit>();
//		for (TaProduit p : listeTousProduit) {
//			if(p.getVendable()!=null && p.getVendable()) {
//				listeModuleVendable.add(p);
//			}
//		}
////		selectedModules = new TaProduit[listeModuleVendable.size()];
////		selectedModules = listeModuleVendable.toArray(selectedModules);
//		
//		if(tousLesModules) {
//
//		}
//	}
//	
//	public void ajoutDossier(ActionEvent event) {
//		
//		
//		try {
//			/*
//			 * Vérication que le nom du dossier et le compte utilisateur(email) n'existe pas
//			 */
////			if()
////			wsMonCompte.dossierExiste(nomDossier);
////			wsMonCompte.utilisateurExiste(emailEntreprise);
//			
//			/*
//			 * création schéma
//			 */
////			if(!listeDossier.contains(nomDossier))
////			adminBddService.ajoutDossier(dossier);
//			
//			
//			/*
//			 * Création client
//			 */
////			TaClient c = new TaClient();
////			TaUtilisateur u = new TaUtilisateur();
////			TaDossier dossier = new TaDossier();
////			u.setUsername(emailEntreprise);
////			u.setEmail(emailEntreprise);
////			u.setActif(1);
////			u.setPasswd(dossier);
////			c.setTaUtilisateur(u);
////			c.setNom(nomEntreprise);
////			c.setCode(nomEntreprise);
////			
////			c.getListeDossier().add(dossier);
////			dossier.setTaClient(c);
////			dossier.setActif(true);
////			dossier.setCode(dossier);
////			
////			c = wsMonCompte.mergeClient(c);
//			
//			
//			/*
//			 * Affectation module
//			 */
//			GregorianCalendar d = new GregorianCalendar();
//			GregorianCalendar d2 = new GregorianCalendar();
//			Date maintenant = new Date();
//			d.setTime(maintenant);
//			d2.setTime(LibDate.incrementDate(maintenant, 45, 0, 0)); //+45 jours
//			XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(d);
//			XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(d2);
//	
////			List<TaAutorisation> aut = new ArrayList<TaAutorisation>();
////			TaAutorisation a1 = null;
////			for (TaProduit p : selectedModules) {
////				a1 = new TaAutorisation();
////				a1.setDateAchat(date);
////				a1.setDateFin(date2);
////				a1.setTaProduit(p);
////				a1.setTaDossier(c.getListeDossier().get(0));
//////				if(!c.getListeAutorisation().contains(a1)) {
////					c.getListeDossier().get(0).getListeAutorisation().add(a1);
//////				};
////				aut.add(a1);
////			}
////			
////			/*
////			 * Enregistrement du client avec ses autorisations
////			 */
////			wsMonCompte.mergeClient(c);
////			
////			fr.legrain.autorisation.xml.ListeModules listeXml = new fr.legrain.autorisation.xml.ListeModules();
////			fr.legrain.autorisation.xml.Module moduleXml = null;
////			for (TaAutorisation taAutorisation : aut) {
////				//TODO if(taAutorisation.getDateFin().before(new Date())) { //autorisation encore valide
////	//				moduleXml = new fr.legrain.autorisation.xml.Module();
////	//				moduleXml.nomModule = taAutorisation.getTaProduit().getIdentifiantModule();
////	//				listeXml.module.add(moduleXml);
////	//				System.out.println(moduleXml.nomModule);
////	//				//gestion des dépendances et compostion
////	//				for(TaProduit p : taAutorisation.getTaProduit().getListeProduitDependant()) {
////	//					moduleXml = new fr.legrain.autorisation.xml.Module();
////	//					moduleXml.nomModule = p.getIdentifiantModule();
////	//					listeXml.module.add(moduleXml);
////	//					//TODO appel récursif à faire
////	//				}
////				listeXml = ajouteAutorisation(listeXml,taAutorisation.getTaProduit());
////				//}
////			}
//			
//			/*
//			 * MAJ autorisations
//			 */
////			wsAutorisation.createUpdateDroits(c.getCode(), idProduit, listeXml);
//			
//			/*
//			 * Envoie du mot de passe par email
//			 */
//			
//			
////		} catch (RemoteException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		} catch (DatatypeConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		FacesContext context = FacesContext.getCurrentInstance();  
//	    context.addMessage(null, new FacesMessage("Dossier créé ", 
//	    		"Le dossier '"+dossier+"' a bien été créé.\n "
//	    				+ "Penser à relancer le serveur pour prendre en compte le nouveau sous domaine."
//	    		)); 
//	}
//	
//	/**
//	 * COPIER DE ClientController dans MonCompte à déplacer dans les services si besoin
//	 * @param l
//	 * @param p
//	 * @return
//	 */
//	public fr.legrain.autorisation.xml.ListeModules ajouteAutorisation(fr.legrain.autorisation.xml.ListeModules l, TaProduit p) {
//		fr.legrain.autorisation.xml.Module moduleXml = new fr.legrain.autorisation.xml.Module();
//		moduleXml.nomModule = p.getIdentifiantModule();
//		if(!l.contientModule(moduleXml)) {
//			l.module.add(moduleXml);
//		}
//		System.out.println(moduleXml.nomModule);
//		//gestion des dépendances et compostion
//		for(TaProduit prod : p.getListeProduitDependant()) {
//			moduleXml = new fr.legrain.autorisation.xml.Module();
//			moduleXml.nomModule = prod.getIdentifiantModule();
//			if(!l.contientModule(moduleXml)) {
//				l.module.add(moduleXml);
//			}
//			//TODO appel récursif à faire
//			if(prod.getListeProduitDependant()!=null) {
//				l = ajouteAutorisation(l,prod);
//			}
//		}
//		for(TaProduit prod : p.getListeSousProduit()) {
//			moduleXml = new fr.legrain.autorisation.xml.Module();
//			moduleXml.nomModule = prod.getIdentifiantModule();
//			if(!l.contientModule(moduleXml)) {
//				l.module.add(moduleXml);
//			}
//			//TODO appel récursif à faire
//			if(prod.getListeSousProduit()!=null) {
//				l = ajouteAutorisation(l,prod);
//			}
//		}
//		return l;
//	}
//
//	public String getDossier() {
//		return dossier;
//	}
//
//	public void setDossier(String nomDossier) {
//		this.dossier = nomDossier;
//	}
//
//	public List<TaProduit> getListeTousProduit() {
//		return listeTousProduit;
//	}
//
//	public List<TaProduit> getSelectedModules() {
//		return selectedModules;
//	}
//
//	public void setSelectedModules(List<TaProduit> selectedModules) {
//		this.selectedModules = selectedModules;
//	}
//
//	public String getNomEntreprise() {
//		return nomEntreprise;
//	}
//
//	public void setNomEntreprise(String nomEntreprise) {
//		this.nomEntreprise = nomEntreprise;
//	}
//
//	public String getEmailEntreprise() {
//		return emailEntreprise;
//	}
//
//	public void setEmailEntreprise(String emailEntreprise) {
//		this.emailEntreprise = emailEntreprise;
//	}
//
//	public List<TaAutorisation> getListeAutorisationDossier() {
//		return listeAutorisationDossier;
//	}
//
//	public void setListeAutorisationDossier(
//			List<TaAutorisation> listeAutorisationDossier) {
//		this.listeAutorisationDossier = listeAutorisationDossier;
//	}
//
//	public int getNbUtilisateur() {
//		return nbUtilisateur;
//	}
//
//	public void setNbUtilisateur(int nbUtilisateur) {
//		this.nbUtilisateur = nbUtilisateur;
//	}
//
//	public int getNbMois() {
//		return nbMois;
//	}
//
//	public void setNbMois(int nbMois) {
//		this.nbMois = nbMois;
//	}
//	
//}  
//              
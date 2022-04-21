package fr.legrain.bdg.compteclient.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.compteclient.model.TaFournisseur;
import fr.legrain.bdg.compteclient.model.TaMesFournisseurs;
import fr.legrain.bdg.compteclient.service.droits.SessionInfo;
import fr.legrain.bdg.compteclient.service.interfaces.remote.ITaFournisseurServiceRemote;
import fr.legrain.bdg.compteclient.service.interfaces.remote.ITaMesFournisseursServiceRemote;
import fr.legrain.bdg.compteclient.ws.TiersDossier;
import fr.legrain.bdg.ws.client.CompteClientServiceClientCXF;

@ManagedBean //Est responsable de la gestion des opérations sur une entité notamment grâce à plusieurs méthodes
@ViewScoped
public class FournisseurController implements Serializable {

	private static final long serialVersionUID = 6877514534766460721L;

	private CompteClientServiceClientCXF wsCompteClient;
	private List<TiersDossier> listeFrs = null;	
	private TaFournisseur lesFournisseurs = new TaFournisseur();
	
	@ManagedProperty ("#{c_langue}")
	private ResourceBundle cLangue;

	@Inject private	SessionInfo sessionInfo;	
	private @EJB ITaFournisseurServiceRemote serviceFournisseur; //Permet d'injecter une référence vers un autre EJB / Nom avec lequel l'EJB sera recherché
	private @EJB ITaMesFournisseursServiceRemote serviceMesFournisseurs;
	private List<TaMesFournisseurs> listeMesFournisseurs = new ArrayList<TaMesFournisseurs> ();
	private List<TaFournisseur> listeDesFournisseurs = new ArrayList<TaFournisseur> ();
	//	List<TaFournisseur> listeTamponDesFournisseurs = new ArrayList<TaFournisseur> ();


	@PostConstruct //La méthode est invoquée après que l'instance est créée et que les dépendances sont injectées
	public void debut() {
		
		if (sessionInfo.getSessionLangue()!= null) {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(sessionInfo.getSessionLangue());
		}
		// La liste des fournisseurs est récuperer dans listeDesFournisseurs par l'interface service ITaFournisseur de la DAO  	
		listeDesFournisseurs = serviceFournisseur.selectAll();
		// Si aucune liste n'est constitué, liste inexistante créer l'objet listeDesFournisseurs de TaFournisseur
		if (listeDesFournisseurs == null) {
			listeDesFournisseurs = new ArrayList<TaFournisseur>();													
		}
		// Appelle la méthode filtrer liste fournisseur
		filtrerListeFrs();
	}

	public void filtrerListeFrs(){
		// Gestion de l'exeption
		try {
			// La liste mes fournisseurs est récuperer dans listeMesFournisseurs par l'interface service ITaMesFournisseur de la DAO
			listeMesFournisseurs = serviceMesFournisseurs.rechercheMesFournisseurs(sessionInfo.getUtilisateur().getId());
			// Si liste des fournisseurs est différent d'inexistante entre dans la premiére condition		
			if(listeDesFournisseurs!=null) {
				// Boucle imbriqué 
				// Boucle sur listeDesFournisseurs et place à chaque itération le fournisseur dans tf
				for (TaFournisseur f : listeDesFournisseurs) {
					System.out.println(f.getRaisonSocialeFournisseur());

					for (TaMesFournisseurs mf : listeMesFournisseurs) {
						// Boucle sur listeMesFournisseurs et place à chaque itération le fournisseur dans tmf
						// Si la valeur de l'attribut numéro de siret du fournisseur tf est égal à celui de tmf entre dans la condition			
						if(f.getSiretFournisseur()!=null && f.getSiretFournisseur().equals(mf.getTaFournisseur().getSiretFournisseur())) {
							// Trace de chaque fournisseurs entrant dans cette condition				
							System.out.println(mf.getTaFournisseur().getRaisonSocialeFournisseur());
							// Sete le boolean Affiche à true, pour dire ce fournisseur fait partie de mes fournisseurs			
							f.setAffiche(true);
							System.out.println(f.isAffiche());
						}
					}						
				}						
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void ajouteFournisseur(TiersDossier frs)throws Exception {
		TaFournisseur f = serviceFournisseur.findByCodeDossier(frs.getCodeDossier());

		if (f == null){
			f= new TaFournisseur();
		}

		f.setCodeDossier(frs.getCodeDossier());
		if(frs.getTaTiers() != null){
			if(frs.getTaTiers().getTaEntreprise() != null){
				f.setRaisonSocialeFournisseur(frs.getTaTiers().getTaEntreprise().getNomEntreprise()+ "-"+frs.getTaTiers().getNomTiers());
			}
			if(frs.getTaTiers().getTaAdresse() != null){
				f.setAdresse1Fournisseur(frs.getTaTiers().getTaAdresse().getAdresse1Adresse());	
				f.setAdresse2Fournisseur(frs.getTaTiers().getTaAdresse().getAdresse2Adresse());
				f.setAdresse3Fournisseur(frs.getTaTiers().getTaAdresse().getAdresse3Adresse());
				f.setCodePostalFournisseur(frs.getTaTiers().getTaAdresse().getCodepostalAdresse());
				f.setVilleFournisseur(frs.getTaTiers().getTaAdresse().getVilleAdresse());
				f.setPaysFournisseur(frs.getTaTiers().getTaAdresse().getPaysAdresse());
			}
			if(frs.getTaTiers().getTaInfoJuridique() != null){
				f.setSiretFournisseur(frs.getTaTiers().getTaInfoJuridique().getSiretInfoJuridique());
				f.setCapitalFournisseur(frs.getTaTiers().getTaInfoJuridique().getCapitalInfoJuridique());
				f.setApeFournisseur(frs.getTaTiers().getTaInfoJuridique().getApeInfoJuridique());
				f.setRcsFournisseur(frs.getTaTiers().getTaInfoJuridique().getRcsInfoJuridique());
			}
			if(frs.getTaTiers().getTaTelephone() != null){
				f.setTelFournisseur(frs.getTaTiers().getTaTelephone().getNumeroTelephone());
				f.setFaxFournisseur(frs.getTaTiers().getTaTelephone().getNumeroTelephone());
			}
	
			if(frs.getTaTiers().getTaCompl() != null){
				f.setTvaIntraFournisseur(frs.getTaTiers().getTaCompl().getTvaIComCompl());
			}
	
			if(frs.getTaTiers().getTaEmail() != null){
				f.setEmailFournisseur(frs.getTaTiers().getTaEmail().getAdresseEmail());
			}
			if(frs.getTaTiers().getTaWeb() != null){
				f.setWebFournisseur(frs.getTaTiers().getTaWeb().getAdresseWeb());
			}
			if(frs.getTaTiers().getBlobLogo() != null){
				f.setBlobLogo(frs.getTaTiers().getBlobLogo());
			}
		}
		serviceFournisseur.merge(f);

	}

	public void rafraichir(ActionEvent trs){
		// Gestion de l'exception
		try {
			// Instansiation de  wsCompteClient avec l'objet web service
			wsCompteClient = new CompteClientServiceClientCXF();
			listeFrs = wsCompteClient.listeFournisseur();

			System.out.println("TestController.test()");
			if(listeFrs!=null) {
				for (TiersDossier td : listeFrs) {
					System.out.println("Dossier : "+td.getCodeDossier());
					ajouteFournisseur(td);
				}
				debut();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void onRowSelect(SelectEvent event) {
		FacesMessage msgInfoFournisseur = new FacesMessage( cLangue.getString("popuf_info_fournisseur_header_titre"), ((TaFournisseur) event.getObject()).getRaisonSocialeFournisseur());
		FacesContext.getCurrentInstance().addMessage(null, msgInfoFournisseur);

		infoFournisseur(null);
	}

	public void infoFournisseur(ActionEvent a) {
		Map<String,Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("width", 900);
		options.put("height", 650);
		options.put("contentWidth", "100%");
		options.put("contentHeight", "100%");
		options.put("closable", false);
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String,Object> sessionMap = externalContext.getSessionMap();

		if(a!=null) {
			int id= (int) a.getComponent().getAttributes().get("idFrs");
			if(id!=0){

				try {
					lesFournisseurs = serviceFournisseur.findById(id);
				} catch (FinderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		sessionMap.put("infoFrs", lesFournisseurs);

		RequestContext.getCurrentInstance().openDialog("info-saisie-fournisseur", options, null);
	}

	public void dialogReturn(SelectEvent event) {
		filtrerListeFrs();		
	}

	/**
	 *  Méthode de fermeture du dialogue	
	 * @throws IOException
	 */
	public void fermer() throws IOException{
		RequestContext.getCurrentInstance().closeDialog("info-saisie-fournisseur");
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath()+"");
	}
	
	public TaFournisseur getLesFournisseurs() {
		return lesFournisseurs;
	}
	
	public ResourceBundle getcLangue() {
		return cLangue;
	}

	public void setcLangue(ResourceBundle cLangue) {
		this.cLangue = cLangue;
	}

	public TaFournisseur getListeFournisseur() {
		return lesFournisseurs;
	}

	public void setListeFournisseur(TaFournisseur lesFournisseurs) {
		this.lesFournisseurs = lesFournisseurs;

	}

	public void setLesFournisseurs(TaFournisseur lesFournisseurs) {
		this.lesFournisseurs = lesFournisseurs;
	}

	public List<TaFournisseur> getListeDesFournisseurs() {
		return listeDesFournisseurs;
	}

	public void setListeDesFournisseurs(List<TaFournisseur> listeDesFournisseurs) {
		this.listeDesFournisseurs = listeDesFournisseurs;
	}
}

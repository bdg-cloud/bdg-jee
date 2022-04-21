package fr.legrain.bdg.webapp.documents;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.legrain.bdg.caisse.service.remote.ITaDepotRetraitCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaFondDeCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaLSessionCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaSessionCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaTDepotRetraitCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaTFondDeCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaTLSessionCaisseServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.caisse.dto.TaSessionCaisseDTO;
import fr.legrain.caisse.model.TaSessionCaisse;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.lib.data.EnumModeObjet;

@Named
@ViewScoped  
public class SessionCaisseController extends AbstractController implements Serializable {  
	
	@EJB private ITaDepotRetraitCaisseServiceRemote taDepotRetraitCaisseService;
	@EJB private ITaFondDeCaisseServiceRemote taFondDeCaisseService;
	@EJB private ITaLSessionCaisseServiceRemote taLSessionCaisseService;
	@EJB private ITaSessionCaisseServiceRemote taSessionCaisseService;
	@EJB private ITaTDepotRetraitCaisseServiceRemote taTDepotRetraitCaisseService;
	@EJB private ITaTFondDeCaisseServiceRemote taTFondDeCaisseService;
	@EJB private ITaTLSessionCaisseServiceRemote taTLSessionCaisseService;
	@EJB private ITaGenCodeExServiceRemote taGenCodeExService;
	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;
	
	@Inject protected AutorisationBean autorisationBean;
	//@Inject protected TenantInfo tenantInfo;
	@Inject protected SessionInfo sessionInfo;
	
	private TaSessionCaisseDTO selectedSessionCaisseDTO;
	private List<TaSessionCaisseDTO> listeSessionCaisseDTO;
		
	@PostConstruct
	public void init() {
		refresh();
	}

	public void refresh(){
		//values=taPreferencesService.selectAll();
		
		TaSessionCaisse s = taSessionCaisseService.findSessionCaisseActive(sessionInfo.getUtilisateur().getId(),null);

		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
	}
	
	public void actAnnuler(ActionEvent actionEvent) {
		modeEcran.setMode(EnumModeObjet.C_MO_CONSULTATION);
		
	   	FacesContext context = FacesContext.getCurrentInstance();  
	    context.addMessage(null, new FacesMessage("SessionCaisseController", "actAnnuler")); 
	}
	
	public void actFermer() {

	}
	
	public void actDemarrerSessionCaisse(ActionEvent actionEvent) {
		taSessionCaisseService.demarrerSessionCaisse();
//		try {
//			TaSessionCaisse s = taSessionCaisseService.findSessionCaisseActive(sessionInfo.getUtilisateur().getId(),null);
//			if(s==null) {
//				//il n'y a pas de session deja en cours, on peut en commencer une
//				s = new TaSessionCaisse();
//				
//				taGenCodeExService.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));			
//				if(s.getCodeSessionCaisse()!=null) {
//					taSessionCaisseService.annuleCode(s.getCodeSessionCaisse());
//				}	
//				s.setCodeSessionCaisse(taSessionCaisseService.genereCode(null));
//				s.setTaUtilisateur(taUtilisateurService.findById(sessionInfo.getUtilisateur().getId()));
//				
//				s.setDateSession(new Date());
//				s.setDateDebutSession(new Date());
//				s.setAutomatique(false);
//				s = taSessionCaisseService.merge(s);
//			} else {
//				//une session est deja en cours
//			}
//			
//			//déclanche fond de caisse ouverture
//			//Création des différents TaLSessionCaisse à 0 ?
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public void actCloturerSessionCaisse(ActionEvent actionEvent) {
		//Ticket Z
		TaSessionCaisse s = taSessionCaisseService.findSessionCaisseActive(sessionInfo.getUtilisateur().getId(),null);
		if(s!=null) {
			//il y a bien une session en cours
			s.setDateFinSession(new Date());
			s.setVerrouillageTicketZ(true);
			
			//calcul des totaux
			
			//verrouillage de tous les ticket de la période
			
			
			
			s = taSessionCaisseService.merge(s);
		} else {
			//pas de session en cours
		}
		//Sur le TaSessionCaisse courant mettre la date de cloture/verouillage
		//Faire tout les cumuls définitif généraux et cumul des TaLSessionCaisse
		//déclanche fond de caisse cloture
	}
	
	public void actEtatSessionCaisse(ActionEvent actionEvent) {
		//Ticket X
		//Faire tout les cumuls généraux entre le début de la session et maintenant et cumul des TaLSessionCaisse
	}
	
	public void actFondDeCaisseOuverture(ActionEvent actionEvent) {
		//permet de créer un TaFondDeCaisse de type ouverture
		//permet de saisir un écart de caisse
	}
	
	public void actFondDeCaisseCloture(ActionEvent actionEvent) {
		//permet de créer un TaFondDeCaisse de type cloture
		//permet de saisir un écart de caisse
	}
	
	public void actDepotCaisse(ActionEvent actionEvent) {
		//permet de créer un TaDepotRetraitCaisse de type depot
	}

	public void actRetraitCaisse(ActionEvent actionEvent) {
		//permet de créer un TaDepotRetraitCaisse de type retrait
	}

}

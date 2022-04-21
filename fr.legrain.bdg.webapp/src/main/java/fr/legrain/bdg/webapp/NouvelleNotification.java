package fr.legrain.bdg.webapp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.application.FacesMessage;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;

//import org.primefaces.push.EventBus;
//import org.primefaces.push.EventBusFactory;

import fr.legrain.tache.model.TaNotification;

@ApplicationScoped
public class NouvelleNotification {
	
	//http://jsfcorner.blogspot.fr/2012/09/from-backend-event-to-screen-update.html
	
	//http://docs.oracle.com/javaee/6/tutorial/doc/gkhic.html //Using Events in CDI Applications
	
	//http://showcase.omnifaces.org/push/socket
	
	@Inject
	@Push(channel = PushMessageBean.CHANNEL+"1")
	private PushContext pushProgramme;
	
	@Inject
	@Push(channel = PushMessageBean.CHANNEL+"2")
	private PushContext pushDossier;
	
	@Inject
	@Push(channel = PushMessageBean.CHANNEL+"3")
	private PushContext pushUtilisateur;

    public void observeRegistrationActivity(@Observes TaNotification notification) {


//    	EventBus eventBus = EventBusFactory.getDefault().eventBus();
//		eventBus.publish(PushMessageBean.CHANNEL+"/"+notification.getVersion(), new FacesMessage(notification.getTaEvenementUtilisateur().getLibelleEvenement(), notification.getVersion()));
		
		//push.send(notification.getTaEvenementUtilisateur().getLibelleEvenement()/*, notification.getVersion()*/);
    	
//    	pushProgramme.send(new FacesMessage(notification.getTaEvenementUtilisateur().getLibelleEvenement(), notification.getVersion()));
    	pushDossier.send(new FacesMessage(notification.getTaEvenementUtilisateur().getLibelleEvenement(), notification.getVersion()), notification.getVersion());
//    	pushUtilisateur.send(new FacesMessage(notification.getTaEvenementUtilisateur().getLibelleEvenement(), notification.getVersion()), notification.getVersion()+"/"+notification.getListeUtilisateur().get(0).getUsername());
    	
    	


    }


}



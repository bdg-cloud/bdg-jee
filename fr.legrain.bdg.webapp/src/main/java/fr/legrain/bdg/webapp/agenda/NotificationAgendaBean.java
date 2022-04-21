package fr.legrain.bdg.webapp.agenda;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import fr.legrain.bdg.tache.service.remote.ITaNotificationServiceRemote;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.droits.service.TenantInfo;
import fr.legrain.tache.model.TaNotification;

@Named
@ViewScoped
public class NotificationAgendaBean  implements Serializable {

	private static final long serialVersionUID = -7982832818969045548L;
	
	@Inject private	SessionInfo sessionInfo;
	@Inject private	TenantInfo tenantInfo;
		
	private @EJB ITaNotificationServiceRemote taNotificationServiceRemote;
	
	private List<TaNotification> listeNotifDatatable;
	private TaNotification selectedNotification;
	
	private Integer nbNotificationNonLu;

	@PostConstruct
	public void init() {
		initListeNotification();
	}
	
	public void initListeNotification() {
		listeNotifDatatable = taNotificationServiceRemote.findNotificationInWebAppEnvoyee(null, sessionInfo.getUtilisateur().getId(),null);
		
		List<TaNotification> listeNotifNonLu = taNotificationServiceRemote.findNotificationInWebAppEnvoyee(null, sessionInfo.getUtilisateur().getId(),false);
		nbNotificationNonLu = null;
		if(listeNotifNonLu!=null && !listeNotifNonLu.isEmpty()) {
			nbNotificationNonLu = listeNotifNonLu.size();
			System.out.println(nbNotificationNonLu);
		}
	}
	
	public List<TaNotification> getListeNotifDatatable() {
		return listeNotifDatatable;
	}

	public void setListeNotifDatatable(List<TaNotification> listeNotifDatatable) {
		this.listeNotifDatatable = listeNotifDatatable;
	}

	public TaNotification getSelectedNotification() {
		return selectedNotification;
	}

	public void setSelectedNotification(TaNotification selectedNotification) {
		this.selectedNotification = selectedNotification;
	}

	public Integer getNbNotificationNonLu() {
		return nbNotificationNonLu;
	}

	public void setNbNotificationNonLu(Integer nbNotificationNonLu) {
		this.nbNotificationNonLu = nbNotificationNonLu;
	}


}

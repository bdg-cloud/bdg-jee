package fr.legrain.bdg.webapp.agenda;

import java.util.Date;

import org.primefaces.model.DefaultScheduleEvent;

import com.google.api.services.calendar.model.Event;

import fr.legrain.bdg.webapp.oauth.microsoft.MsEvent;
import fr.legrain.lib.data.LibDate;
import fr.legrain.tache.model.TaEvenement;

public class LgrTabScheduleEvent extends DefaultScheduleEvent {
	
	private static final long serialVersionUID = -8401839619191834026L;
	
	private Integer idObjet;
	private String codeObjet;
	private String cssLgrTab;
	
	private String origine;
	private String urlCalendrierExterne;
	
	private TaEvenement taEvenement;

	public LgrTabScheduleEvent() {
		super();
		taEvenement = new TaEvenement();
	}
	
	public void updateUI() {
//		setTitle(taEvenement.getLibelleEvenement());
//		setDescription(taEvenement.getDescription());
		setStartDate(LibDate.dateToLocalDateTime(taEvenement.getDateDebut()));
		setEndDate(LibDate.dateToLocalDateTime(taEvenement.getDateFin()));
//		setAllDay(taEvenement.isTouteLaJournee());
	}

	public void updateModel() {
//		taEvenement.setLibelleEvenement(getTitle());
//		taEvenement.setDescription(getDescription());
		taEvenement.setDateDebut(LibDate.localDateTimeToDate(getStartDate()));
		taEvenement.setDateFin(LibDate.localDateTimeToDate(getEndDate()));
//		taEvenement.setTouteLaJournee(isAllDay());
	}
	
	public LgrTabScheduleEvent(String title, Date start, Date end, boolean allDay) {
		super(title, LibDate.dateToLocalDateTime(start), LibDate.dateToLocalDateTime(end), allDay);
	}

	public LgrTabScheduleEvent(String title, Date start, Date end, Object data) {
		super(title, LibDate.dateToLocalDateTime(start), LibDate.dateToLocalDateTime(end), data);
	}

	public LgrTabScheduleEvent(String title, Date start, Date end, String styleClass) {
		super(title, LibDate.dateToLocalDateTime(start), LibDate.dateToLocalDateTime(end), styleClass);
	}

	public LgrTabScheduleEvent(String title, Date start, Date end) {
		super(title, LibDate.dateToLocalDateTime(start), LibDate.dateToLocalDateTime(end));
	}

	public Integer getIdObjet() {
		return idObjet;
	}

	public void setIdObjet(Integer integer) {
		this.idObjet = integer;
	}

	public String getCssLgrTab() {
		return cssLgrTab;
	}

	public void setCssLgrTab(String cssLgrTab) {
		this.cssLgrTab = cssLgrTab;
	}

	public String getCodeObjet() {
		return codeObjet;
	}

	public void setCodeObjet(String codeObjet) {
		this.codeObjet = codeObjet;
	}

	public TaEvenement getTaEvenement() {
		return taEvenement;
	}

	public void setTaEvenement(TaEvenement taEvenement) {
		this.taEvenement = taEvenement;
	}
	
	public String getOrigine() {
		return origine;
	}
	public void setOrigine(String origine) {
		this.origine = origine;
	}

	public String getUrlCalendrierExterne() {
		if(getData()!=null && getData() instanceof Event) {
			return ((Event)getData()).getHtmlLink();
		}
		if(getData()!=null && getData() instanceof MsEvent) {
			return ((MsEvent)getData()).getWebLink();
		}
		return urlCalendrierExterne;
	}


}

package fr.legrain.bdg.welcome.webapp;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import fr.legrain.lib.data.LibDate;
 
@ManagedBean(name="timelineView")
@ViewScoped
public class TimelineView implements Serializable {  
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 3269587063527946778L;

	private TimelineModel model;  
   
    private boolean selectable = false;  
    private boolean zoomable = false;  
    private boolean moveable = true;  
    private boolean stackEvents = true;  
    private String eventStyle = "box";  
    private boolean axisOnTop;  
    private boolean showCurrentTime = true;  
    private boolean showNavigation = false;  
   
    @PostConstruct 
    protected void initialize() {  
        model = new TimelineModel();  
   
        Calendar cal = Calendar.getInstance();         

        cal.set(2017, Calendar.JANUARY, 01, 0, 0, 0);  
        model.add(new TimelineEvent("Sortie du BdG Cloud. V.1.0", LibDate.dateToLocalDateTime(cal.getTime())));
   
        cal.set(2017, Calendar.JANUARY, 01, 0, 0, 0);  
        model.add(new TimelineEvent("Catalogue Produits V.1.0", LibDate.dateToLocalDateTime(cal.getTime())));
   
        cal.set(2017, Calendar.JANUARY, 01, 0, 0, 0);  
        model.add(new TimelineEvent("Gestion Contacts V.1.0", LibDate.dateToLocalDateTime(cal.getTime())));

        cal.set(2017, Calendar.JANUARY, 01, 0, 0, 0);  
        model.add(new TimelineEvent("Devis V.1.0", LibDate.dateToLocalDateTime(cal.getTime())));

        cal.set(2017, Calendar.JANUARY, 01, 0, 0, 0);  
        model.add(new TimelineEvent("Facture V.1.0", LibDate.dateToLocalDateTime(cal.getTime())));

        cal.set(2017, Calendar.JANUARY, 10, 0, 0, 0);  
        model.add(new TimelineEvent("Solstyce Traçabilité V.1.0", LibDate.dateToLocalDateTime(cal.getTime())));
        
        cal.set(2017, Calendar.JANUARY, 10, 0, 0, 0);  
        model.add(new TimelineEvent("Stocks / Lots V.1.0", LibDate.dateToLocalDateTime(cal.getTime())));

        cal.set(2017, Calendar.FEBRUARY, 28, 0, 0, 0);  
        model.add(new TimelineEvent("Gestion Commerciale V.1.0", LibDate.dateToLocalDateTime(cal.getTime())));

        cal.set(2017, Calendar.MARCH, 20, 0, 0, 0);  
        model.add(new TimelineEvent("Export Compta Epicéa", LibDate.dateToLocalDateTime(cal.getTime())));

        cal.set(2017, Calendar.APRIL, 12, 0, 0, 0);  
        model.add(new TimelineEvent("Tableau de bord Devis", LibDate.dateToLocalDateTime(cal.getTime())));

        cal.set(2017, Calendar.MAY, 15, 0, 0, 0);  
        model.add(new TimelineEvent("Envoi des factures par Email", LibDate.dateToLocalDateTime(cal.getTime())));

        cal.set(2017, Calendar.JUNE, 19, 0, 0, 0);  
        model.add(new TimelineEvent("Comptes Clients Sécurisés", LibDate.dateToLocalDateTime(cal.getTime())));

        cal.set(2017, Calendar.MARCH, 3, 0, 0, 0);
        Date startReport = cal.getTime();
        cal.set(2017, Calendar.AUGUST, 31, 0, 0, 0);
        Date endReport = cal.getTime();
//        model.add(new TimelineEvent(new Task("Export Compta Epicéa", "images/devis.png", true), startReport, endReport));
//        model.add(new TimelineEvent("Export Compta Epicéa", startReport, endReport )); // la syntaxe permet de ne pas fixer une date précise
    }  
   
    public void onSelect(TimelineSelectEvent e) {  
        TimelineEvent timelineEvent = e.getTimelineEvent();  
   
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected event:", timelineEvent.getData().toString());  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
   
    public TimelineModel getModel() {  
        return model;  
    }  
   
    public boolean isSelectable() {  
        return selectable;  
    }  
   
    public void setSelectable(boolean selectable) {  
        this.selectable = selectable;  
    }  
   
    public boolean isZoomable() {  
        return zoomable;  
    }  
   
    public void setZoomable(boolean zoomable) {  
        this.zoomable = zoomable;  
    }  
   
    public boolean isMoveable() {  
        return moveable;  
    }  
   
    public void setMoveable(boolean moveable) {  
        this.moveable = moveable;  
    }  
   
    public boolean isStackEvents() {  
        return stackEvents;  
    }  
   
    public void setStackEvents(boolean stackEvents) {  
        this.stackEvents = stackEvents;  
    }  
   
    public String getEventStyle() {  
        return eventStyle;  
    }  
   
    public void setEventStyle(String eventStyle) {  
        this.eventStyle = eventStyle;  
    }  
   
    public boolean isAxisOnTop() {  
        return axisOnTop;  
    }  
   
    public void setAxisOnTop(boolean axisOnTop) {  
        this.axisOnTop = axisOnTop;  
    }  
   
    public boolean isShowCurrentTime() {  
        return showCurrentTime;  
    }  
   
    public void setShowCurrentTime(boolean showCurrentTime) {  
        this.showCurrentTime = showCurrentTime;  
    }  
   
    public boolean isShowNavigation() {  
        return showNavigation;  
    }  
   
    public void setShowNavigation(boolean showNavigation) {  
        this.showNavigation = showNavigation;  
    }
    public class Task implements Serializable {
    	 
        /**
		 * 
		 */
		private static final long serialVersionUID = -2458404895644016427L;
		private String title;
        private String imagePath;
        private boolean period;
 
        public Task(String title, String imagePath, boolean period) {
            this.title = title;
            this.imagePath = imagePath;
            this.period = period;
        }
 
        public String getTitle() {
            return title;
        }
 
        public String getImagePath() {
            return imagePath;
        }
 
        public boolean isPeriod() {
            return period;
        }
    }    
}  
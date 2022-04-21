package fr.legrain.bdg.webapp.tiers;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

@Named
@ViewScoped 
public class DashboardTiersBean implements Serializable {  

	private static final long serialVersionUID = -3826948129773825485L;
	private DashboardModel model;  
      
    public DashboardTiersBean() {  
        model = new DefaultDashboardModel();  
        DashboardColumn column1 = new DefaultDashboardColumn();  
        DashboardColumn column2 = new DefaultDashboardColumn();  
          
        column1.addWidget("idPanelIdentite");
        column1.addWidget("idPanelContact");
        
        column2.addWidget("idPanelAdresse");  
        column2.addWidget("idPanelCommentaire");  

        model.addColumn(column1);  
        model.addColumn(column2);   
    }  
      
    public void handleReorder(DashboardReorderEvent event) {  
//        FacesMessage message = new FacesMessage();  
//        message.setSeverity(FacesMessage.SEVERITY_INFO);  
//        message.setSummary("Reordered: " + event.getWidgetId());  
//        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());  
//          
//        addMessage(message);  
    }  
      
      
    private void addMessage(FacesMessage message) {  
//        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
      
    public DashboardModel getModel() {  
        return model;  
    }  
}  
              
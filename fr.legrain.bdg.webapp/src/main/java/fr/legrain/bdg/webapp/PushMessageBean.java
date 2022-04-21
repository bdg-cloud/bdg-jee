package fr.legrain.bdg.webapp;

import java.io.Serializable;

import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

//import org.primefaces.push.EventBus;
//import org.primefaces.push.EventBusFactory;

@Named
//@ApplicationScoped
@ViewScoped 
public class PushMessageBean implements Serializable {
	
	private static final long serialVersionUID = 2790453117406039726L;
	public static final String CHANNEL = "notifications";
	private String text;
	private String summary;
	private String detail;
	
	@Inject
	@Push(channel = PushMessageBean.CHANNEL)
	private PushContext push;
	
	public void send() {
		
		System.out.println("PushMessageBean.send() : "+detail);
		
//		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		//eventBus.publish(PushMessageBean.CHANNEL, detail);
//		eventBus.publish(PushMessageBean.CHANNEL, new FacesMessage(summary, detail));
		
		push.send(detail);

//		PushContext pushContext = PushContextFactory.getDefault().getPushContext();
//		pushContext.push("/notifications", new FacesMessage(summary, detail));
	}

	public String getText() {
		return text;
	}

	public String getSummary() {
		return summary;
	}

	public String getDetail() {
		return detail;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
}

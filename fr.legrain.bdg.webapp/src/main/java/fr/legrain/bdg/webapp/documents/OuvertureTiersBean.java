package fr.legrain.bdg.webapp.documents;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.LgrTabEvent;
import fr.legrain.bdg.webapp.app.LgrTab;
import fr.legrain.bdg.webapp.tiers.TiersController;
import fr.legrain.tiers.model.TaTiers;

@Named
@Dependent
public class OuvertureTiersBean implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8932552862284972273L;



	@Inject @Named(value="tiersController")
	private TiersController tiersController;	
	
	
	
	@EJB private ITaTiersServiceRemote taTiersService;
	
	private LgrTabEvent event = new LgrTabEvent();
	
	@PostConstruct
	public void init() {
		
	}

	
	
	public void onEventSelect(SelectEvent selectEvent) {
        event = (LgrTabEvent) selectEvent.getObject();
    }

	public void openDocument(ActionEvent e) {
		if(event.getCssLgrTab().equals(LgrTab.CSS_CLASS_TAB_TIERS)) {
			tiersController.setTaTiers(((TaTiers) event.getData()));
			tiersController.rempliDTO();
			if(event.isAfficheDoc())tiersController.onRowSelect(null);
		}
	}


	public LgrTabEvent getEvent() {
		return event;
	}


	public void setEvent(LgrTabEvent event) {
		this.event = event;
	}



	public TiersController getTiersController() {
		return tiersController;
	}


	public void setTiersController(TiersController tiersController) {
		this.tiersController = tiersController;
	}





		public void detailTiers(TaTiers detailTiers){
			String tabEcran="";
			if(detailTiers!=null){
				tabEcran=LgrTab.CSS_CLASS_TAB_TIERS;
				if(tabEcran!=null && !tabEcran.isEmpty()){
					setEvent(new LgrTabEvent());
					getEvent().setCodeObjet(detailTiers.getCodeTiers());
					getEvent().setData(detailTiers);
					getEvent().setCssLgrTab(tabEcran);
					getEvent().setAfficheDoc(true);
					openDocument(null);
				}
			}
		}

		
		
		
		public TaTiers recupCodetiers(String code){
			FacesContext context = FacesContext.getCurrentInstance();
			if(code!=null && !code.isEmpty())
				try {
					return taTiersService.findByCode(code);
				} catch (FinderException e) {
					context.addMessage(null, new FacesMessage("erreur", "code tiers vide")); 	
				}
			return null;
		}
		
		
		
}

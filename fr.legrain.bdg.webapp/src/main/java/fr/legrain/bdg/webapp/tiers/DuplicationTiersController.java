package fr.legrain.bdg.webapp.tiers;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.FinderException;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.bdg.webapp.app.AbstractController;
import fr.legrain.bdg.webapp.app.AutorisationBean;
import fr.legrain.lib.data.EnumModeObjet;
import fr.legrain.tiers.model.TaTiers;


@Named
@ViewScoped 
public class DuplicationTiersController extends AbstractController{
	
	@Inject @Named(value="autorisationBean")
	private AutorisationBean autorisation;
	
//	@Inject @Named(value="ouvertureDocumentBean")
//	private OuvertureDocumentBean ouvertureDocumentBean;
	
	
	@EJB private ITaPreferencesServiceRemote PreferencesService;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	private String modeEcranDefaut;
	private static final String C_DIALOG = "dialog";
	
	private ParamDuplicationTiers param ;
	private String titreEcran;
    
    private String codeTiers;
    private TaTiers tiers;
    private TaTiers tiersDuplique;
	
	private @EJB ITaTiersServiceRemote taTiersService;
	
	@PostConstruct
	public void init() {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		modeEcranDefaut = params.get("modeEcranDefaut");
		if(modeEcranDefaut!=null && modeEcranDefaut.equals(modeEcran.modeString(EnumModeObjet.C_MO_INSERTION))) {
			modeEcranDefaut = C_DIALOG;
		}
		param = new ParamDuplicationTiers();
		if(params.get("codeTiers")!=null) {
			try {
				codeTiers=params.get("codeTiers");
				tiers=taTiersService.findByCode(codeTiers);
				if(tiers!=null) {
					titreEcran="Duplication du tiers : "+tiers.getCodeTiers()+" "+tiers.getNomTiers();
				}
				
			} catch (FinderException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	

	
	public void actFermer(Object e) {
		PrimeFaces.current().dialog().closeDynamic(e);
	}
	
	public void actFermer(ActionEvent e) {
		PrimeFaces.current().dialog().closeDynamic(null);
	}
	public void actAnnuler(ActionEvent e) {
			
		}
	
	
	public void actEnregistrer(ActionEvent e) {
		
	}
	
	
	
	public void actImprimer(ActionEvent e)  throws Exception {		

		if(modeEcranDefaut!=null && modeEcranDefaut.equals(C_DIALOG)) {
			PrimeFaces.current().dialog().closeDynamic(param);
		}		
	}


	
	public ITaPreferencesServiceRemote getPreferencesService() {
		return PreferencesService;
	}


	public void setPreferencesService(ITaPreferencesServiceRemote preferencesService) {
		PreferencesService = preferencesService;
	}





	public TaTiers getTiers() {
		return tiers;
	}




	public void setTiers(TaTiers tiers) {
		this.tiers = tiers;
	}




	public TaTiers getTiersDuplique() {
		return tiersDuplique;
	}




	public void setTiersDuplique(TaTiers tiersDuplique) {
		this.tiersDuplique = tiersDuplique;
	}


	public String getModeEcranDefaut() {
		return modeEcranDefaut;
	}

	public void setModeEcranDefaut(String modeEcranDefaut) {
		this.modeEcranDefaut = modeEcranDefaut;
	}




	public ParamDuplicationTiers getParam() {
		return param;
	}




	public void setParam(ParamDuplicationTiers param) {
		this.param = param;
	}




	public String getTitreEcran() {
		return titreEcran;
	}




	public void setTitreEcran(String titreEcran) {
		this.titreEcran = titreEcran;
	}




	public String getCodeTiers() {
		return codeTiers;
	}




	public void setCodeTiers(String codeTiers) {
		this.codeTiers = codeTiers;
	}

}

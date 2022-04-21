package fr.legrain.bdg.webapp.solstyce;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import fr.legrain.lib.data.LibConversion;


@Named
@ViewScoped  
public class ParamDialogModeleFabController implements Serializable { 
	
	private BigDecimal qteModele = BigDecimal.ONE;
	private Integer idModele = null;
	private Integer idRecette = null;

	public ParamDialogModeleFabController() {  

	}  

	@PostConstruct
	public void postConstruct(){
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idModeleFabrication = params.get("idModeleFabrication");
		String idRecetteFabrication = params.get("idRecetteFabrication");
				
		if(idModeleFabrication!=null) {
			idModele = LibConversion.stringToInteger(idModeleFabrication);
		}
		
		if(idRecetteFabrication!=null) {
			idRecette = LibConversion.stringToInteger(idRecetteFabrication);
		}
		
		
	}
	
	public void actValiderDialogParamModeleFab(ActionEvent actionEvent) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("idModele", idModele);
		param.put("idRecette", idRecette);
		param.put("qteModele", qteModele);
		PrimeFaces.current().dialog().closeDynamic(param);
	}
	
	public void actAnnulerCreerModele(ActionEvent actionEvent){
		PrimeFaces.current().dialog().closeDynamic(null);
	}

	public BigDecimal getQteModele() {
		return qteModele;
	}

	public void setQteModele(BigDecimal qteModele) {
		this.qteModele = qteModele;
	}
}  

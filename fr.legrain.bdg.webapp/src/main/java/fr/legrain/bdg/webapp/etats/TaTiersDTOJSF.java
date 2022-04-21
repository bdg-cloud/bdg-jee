package fr.legrain.bdg.webapp.etats;


import fr.legrain.bdg.model.ModelObject;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

public class TaTiersDTOJSF extends ModelObject implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1383409262282190304L;
	
	
	TaTiersDTO dto;
	TaTiers taTiers;
	
	
	public TaTiersDTO getDto() {
		return dto;
	}
	public void setDto(TaTiersDTO dto) {
		this.dto = dto;
	}
	public TaTiers getTaTiers() {
		return taTiers;
	}
	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	public void updateDTO(boolean modification) {
		if(dto!=null){
			
		}
	}

}

package fr.legrain.bdg.webapp.relance;

import fr.legrain.document.dto.TaLRelanceDTO;
import fr.legrain.document.model.TaFacture;
import fr.legrain.document.model.TaLRelance;
import fr.legrain.tiers.model.TaTiers;

public class TaLRelanceDTOJSF  implements java.io.Serializable{

	private static final long serialVersionUID = 7799665331626096260L;
	
	private TaLRelanceDTO dto = new TaLRelanceDTO();
	private TaTiers taTiers = null;
	private TaFacture taFacture = null;
	private TaLRelance taLRelance = null;

	public void updateDTO() {
		if(dto!=null) {
			
		}
	}

	public TaLRelanceDTO getDto() {
		return dto;
	}

	public void setDto(TaLRelanceDTO dto) {
		this.dto = dto;
	}

	public TaTiers getTaTiers() {
		return taTiers;
	}

	public void setTaTiers(TaTiers taTiers) {
		this.taTiers = taTiers;
	}

	public TaFacture getTaFacture() {
		return taFacture;
	}

	public void setTaFacture(TaFacture taFacture) {
		this.taFacture = taFacture;
	}

	public TaLRelance getTaLRelance() {
		return taLRelance;
	}

	public void setTaLRelance(TaLRelance taLRelance) {
		this.taLRelance = taLRelance;
	}
	
}

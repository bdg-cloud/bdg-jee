package fr.legrain.bdg.webapp.multi_tarifs;

import javax.ejb.EJB;

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.tiers.dto.TaTTarifDTO;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;

public class TaGrilleTarifTiersDTOJSF  implements java.io.Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = -6262314606066258609L;
	
	
	@EJB ITaTiersServiceRemote taTiersService;
	@EJB ITaTTarifServiceRemote taTTarifService;
	

	private TaTiersDTO dto=new TaTiersDTO();
	private TaTiers tiers;
	private TaTTarifDTO typeTarifDTO;
	private TaFamilleDTO familleDTO;
	private boolean modifie=false;
	private boolean suppression=false;
	private boolean supprimable=true;

	public boolean isModifie() {
		return modifie;
	}

	public void setModifie(boolean modifie) {
		this.modifie = modifie;
	}

	
	public void updateDTO() {
		if(dto!=null) {
			
		}
	}



	public TaTiersDTO getDto() {
		return dto;
	}


	public void setDto(TaTiersDTO dto) {
		this.dto = dto;
	}






	public TaTTarifDTO getTypeTarifDTO() {
		return typeTarifDTO;
	}



	public void setTypeTarifDTO(TaTTarifDTO typeTarifDTO) {
		this.typeTarifDTO = typeTarifDTO;
	}


	public TaGrilleTarifTiersDTOJSF copy() {
		TaGrilleTarifTiersDTOJSF copy =new TaGrilleTarifTiersDTOJSF();
		copy.setTiers(this.tiers);
		copy.setFamilleDTO(this.familleDTO);
		copy.setTypeTarifDTO(this.typeTarifDTO);
		copy.getDto().setCodeTiers(this.getDto().getCodeTiers());
		copy.getDto().setAccepte(this.getDto().getAccepte());
		copy.getDto().setCodeTTarif(this.getDto().getCodeTTarif());
		copy.getDto().setId(this.getDto().getId());
		copy.setModifie(this.modifie);
		copy.setTypeTarifDTO(this.typeTarifDTO);
		return copy;
	}



	public boolean isSuppression() {
		return suppression;
	}

	public void setSuppression(boolean suppression) {
		this.suppression = suppression;
	}

	public boolean isSupprimable() {
		return supprimable;
	}

	public void setSupprimable(boolean supprimable) {
		this.supprimable = supprimable;
	}

	public TaTiers getTiers() {
		return tiers;
	}

	public void setTiers(TaTiers tiers) {
		this.tiers = tiers;
	}


	public TaFamilleDTO getFamilleDTO() {
		return familleDTO;
	}

	public void setFamilleDTO(TaFamilleDTO familleDTO) {
		this.familleDTO = familleDTO;
	}



	
}

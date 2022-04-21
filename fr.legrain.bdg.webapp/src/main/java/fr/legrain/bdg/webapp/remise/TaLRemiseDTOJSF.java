package fr.legrain.bdg.webapp.remise;

import javax.ejb.EJB;

import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.document.dto.TaLRemiseDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.model.TaCompteBanque;

public class TaLRemiseDTOJSF  implements java.io.Serializable{



	
	/**
	 * 
	 */
	private static final long serialVersionUID = -877004472437504371L;


	@EJB ITaRemiseServiceRemote taRemiseService;
	

	private TaLRemiseDTO dto=new TaLRemiseDTO();
	private TaTPaiement taTPaiement;
	private TaCompteBanque taCompteBanque; 

	
	public void updateDTO() {
		if(dto!=null) {
			
		}
	}



	public TaLRemiseDTO getDto() {
		return dto;
	}


	public void setDto(TaLRemiseDTO dto) {
		this.dto = dto;
	}


	public TaTPaiement getTaTPaiement() {
		return taTPaiement;
	}


	public void setTaTPaiement(TaTPaiement taTPaiement) {
		this.taTPaiement = taTPaiement;
	}


	public TaCompteBanque getTaCompteBanque() {
		return taCompteBanque;
	}


	public void setTaCompteBanque(TaCompteBanque tacompteBanque) {
		this.taCompteBanque = tacompteBanque;
	}

	
}

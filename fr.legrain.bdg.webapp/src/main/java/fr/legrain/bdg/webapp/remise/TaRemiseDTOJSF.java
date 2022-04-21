package fr.legrain.bdg.webapp.remise;

import javax.ejb.EJB;

import fr.legrain.bdg.documents.service.remote.ITaRemiseServiceRemote;
import fr.legrain.document.dto.TaRemiseDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.model.TaCompteBanque;

public class TaRemiseDTOJSF  implements java.io.Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4906773577408826014L;
	
	@EJB ITaRemiseServiceRemote taRemiseService;
	
	private TaRemiseDTO dto=new TaRemiseDTO();
	private TaTPaiement taTPaiement;
	private TaCompteBanque taCompteBanque; 

	
	public void updateDTO() {
		if(dto!=null) {
			
		}
	}



	public TaRemiseDTO getDto() {
		return dto;
	}


	public void setDto(TaRemiseDTO dto) {
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

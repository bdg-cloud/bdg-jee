package fr.legrain.bdg.webapp.reglementMultiple;

import javax.ejb.EJB;

import fr.legrain.bdg.documents.service.remote.ITaReglementServiceRemote;
import fr.legrain.document.dto.TaReglementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.model.TaCompteBanque;

public class TaReglementDTOJSF  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8282613005261532460L;

	@EJB ITaReglementServiceRemote taReglementService;

	private TaReglementDTO dto=new TaReglementDTO();
	private TaTPaiement taTPaiement;
	private TaCompteBanque taCompteBanque; 
	private String typeDocument;
	private String marqueExportReglement;
	private String marqueExportRReglement;
	
	public void updateDTO() {
		if(dto!=null) {
			
		}
	}



	public TaReglementDTO getDto() {
		return dto;
	}


	public void setDto(TaReglementDTO dto) {
		this.dto = dto;
	}




	public String getTypeDocument() {
		return typeDocument;
	}


	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
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


	public String getMarqueExportReglement() {
		return marqueExportReglement;
	}


	public void setMarqueExportReglement(String marqueExportReglement) {
		this.marqueExportReglement = marqueExportReglement;
	}


	public String getMarqueExportRReglement() {
		return marqueExportRReglement;
	}


	public void setMarqueExportRReglement(String marqueExportRReglement) {
		this.marqueExportRReglement = marqueExportRReglement;
	}
	
}

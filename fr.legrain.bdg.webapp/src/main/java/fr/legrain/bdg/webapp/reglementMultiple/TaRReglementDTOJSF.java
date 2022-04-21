package fr.legrain.bdg.webapp.reglementMultiple;

import javax.ejb.EJB;

import fr.legrain.bdg.documents.service.remote.ITaRReglementServiceRemote;
import fr.legrain.document.dto.TaRReglementDTO;

public class TaRReglementDTOJSF  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5312159952511052388L;
	
	

	@EJB ITaRReglementServiceRemote taRReglementService;

	private TaRReglementDTO dto=new TaRReglementDTO();

	private String marqueExportRReglement;
	
	public void updateDTO() {
		if(dto!=null) {
			
		}
	}



	public TaRReglementDTO getDto() {
		return dto;
	}


	public void setDto(TaRReglementDTO dto) {
		this.dto = dto;
	}




	


	public String getMarqueExportRReglement() {
		return marqueExportRReglement;
	}


	public void setMarqueExportRReglement(String marqueExportRReglement) {
		this.marqueExportRReglement = marqueExportRReglement;
	}
	
}

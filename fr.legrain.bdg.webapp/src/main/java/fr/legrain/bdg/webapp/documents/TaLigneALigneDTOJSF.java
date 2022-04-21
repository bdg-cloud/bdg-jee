package fr.legrain.bdg.webapp.documents;

import java.util.LinkedList;
import java.util.List;

import fr.legrain.document.dto.ILigneDocumentTiers;
import fr.legrain.document.dto.TaLigneALigneDTO;
import fr.legrain.document.dto.TaLigneALigneSupplementDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.tiers.dto.TaTiersDTO;


public class TaLigneALigneDTOJSF  implements java.io.Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5855246962655411491L;
	
	private TaLigneALigneDTO dto;
	private TaLigneALigneDTO dtoOld;
	private TaEtat taEtatOld;
	private TaEtat taEtat;
	private TaTiersDTO taTiersDTO;
	private List<TaLigneALigneSupplementDTO> listeSupplement = new LinkedList<>();
	private List<TaLigneALigneSupplementDTO> listeAIntegrer = new LinkedList<>();

	public TaLigneALigneDTOJSF() {
		
	}
	
	





	public TaLigneALigneDTO getDto() {
		return dto;
	}

	public void setDto(TaLigneALigneDTO dto) {
		this.dto = dto;
	}

	public TaEtat getTaEtat() {
		return taEtat;
	}

	public void setTaEtat(TaEtat taEtat) {
		this.taEtat = taEtat;
	}







	public TaEtat getTaEtatOld() {
		return taEtatOld;
	}







	public void setTaEtatOld(TaEtat taEtatOld) {
		this.taEtatOld = taEtatOld;
	}







	public List<TaLigneALigneSupplementDTO> getListeSupplement() {
		return listeSupplement;
	}







	public void setListeSupplement(List<TaLigneALigneSupplementDTO> listeSupplement) {
		this.listeSupplement = listeSupplement;
	}








	
	public boolean equalsEtat(TaEtat oldTaEtat) {
		if (taEtat == null) {
			if (oldTaEtat != null)
				return false;
		} else if (!taEtat.equals(oldTaEtat))
			return false;
		return true;
	}








	public List<TaLigneALigneSupplementDTO> getListeAIntegrer() {
		return listeAIntegrer;
	}







	public void setListeAIntegrer(List<TaLigneALigneSupplementDTO> listeAIntegrer) {
		this.listeAIntegrer = listeAIntegrer;
	}







	public TaLigneALigneDTO getDtoOld() {
		return dtoOld;
	}







	public void setDtoOld(TaLigneALigneDTO dtoOld) {
		this.dtoOld = dtoOld;
	}







	public TaTiersDTO getTaTiersDTO() {
		return taTiersDTO;
	}







	public void setTaTiersDTO(TaTiersDTO taTiersDTO) {
		this.taTiersDTO = taTiersDTO;
	}
	
	
	
}

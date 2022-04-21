package fr.legrain.article.model;

import java.io.Serializable;
import java.util.List;

import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.model.TaBonReception;

public class TracabiliteLot implements Serializable {

	private static final long serialVersionUID = 8405718456857883331L;
	
	public TaLotDTO lot;
	public TaFabricationDTO origineFabrication;
	public TaBonReceptionDTO origineBonReception;
	public List<TaFabricationDTO> listeUtilFabrication;
	
	public TaLotDTO getLot() {
		return lot;
	}
	public void setLot(TaLotDTO lot) {
		this.lot = lot;
	}
	public TaFabricationDTO getOrigineFabrication() {
		return origineFabrication;
	}
	public void setOrigineFabrication(TaFabricationDTO origineFabrication) {
		this.origineFabrication = origineFabrication;
	}
	public TaBonReceptionDTO getOrigineBonReception() {
		return origineBonReception;
	}
	public void setOrigineBonReception(TaBonReceptionDTO origineBonReception) {
		this.origineBonReception = origineBonReception;
	}

	public List<TaFabricationDTO> getListeUtilFabrication() {
		return listeUtilFabrication;
	}
	public void setListeUtilFabrication(List<TaFabricationDTO> listeUtilFabrication) {
		this.listeUtilFabrication = listeUtilFabrication;
	}
	
}

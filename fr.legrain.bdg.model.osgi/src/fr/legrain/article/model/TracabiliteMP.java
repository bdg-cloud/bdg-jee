package fr.legrain.article.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.legrain.article.dto.TaFabricationDTO;

public class TracabiliteMP implements Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3293620610283545560L;
	
	
	public TracabiliteLot tracaLot;
	public List<TaFabrication> ListUtilFabrication = new LinkedList<TaFabrication>();
//	public Map<TaFabricationDTO,List<TaLFabrication>> listePFini = new LinkedHashMap<TaFabricationDTO, List<TaLFabrication>>();
	
	
	
	public TracabiliteLot getTracaLot() {
		return tracaLot;
	}
	public void setTracaLot(TracabiliteLot tracaLot) {
		this.tracaLot = tracaLot;
	}
	public List<TaFabrication> getListUtilFabrication() {
		return ListUtilFabrication;
	}
	public void setListUtilFabrication(List<TaFabrication> listUtilFabrication) {
		ListUtilFabrication = listUtilFabrication;
	}
//	public Map<TaFabricationDTO,List<TaLFabrication>> getListePFini() {
//		return listePFini;
//	}
//	public void setListePFini(Map<TaFabricationDTO,List<TaLFabrication>> listePFini) {
//		this.listePFini = listePFini;
//	}
	
}

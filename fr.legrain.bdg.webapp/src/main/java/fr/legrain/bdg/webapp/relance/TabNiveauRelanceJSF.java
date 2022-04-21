package fr.legrain.bdg.webapp.relance;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import fr.legrain.bdg.tiers.service.remote.ITaTiersServiceRemote;
import fr.legrain.document.model.TaTRelance;
import fr.legrain.tiers.dto.TaTiersDTO;

public class TabNiveauRelanceJSF {
	
	private TaTRelance taTRelance;
	private List<TaLRelanceDTOJSF> listeRelance = new ArrayList<>();
	private List<TaLRelanceDTOJSF> listeSelectionRelance = new ArrayList<>();
	private StreamedContent exportation;
	private ITaTiersServiceRemote taTiersService;
	
	public TabNiveauRelanceJSF(ITaTiersServiceRemote taTiersService) {
		this.taTiersService = taTiersService;
	}
	
	public TaTRelance getTaTRelance() {
		return taTRelance;
	}
	public void setTaTRelance(TaTRelance taTRelance) {
		this.taTRelance = taTRelance;
	}
	public List<TaLRelanceDTOJSF> getListeRelance() {
		return listeRelance;
	}
	public void setListeRelance(List<TaLRelanceDTOJSF> listeRelance) {
		this.listeRelance = listeRelance;
	}
	public List<TaLRelanceDTOJSF> getListeSelectionRelance() {
		return listeSelectionRelance;
	}
	public void setListeSelectionRelance(List<TaLRelanceDTOJSF> listeSelectionRelance) {
		this.listeSelectionRelance = listeSelectionRelance;
	}
	
	public void setExportation(StreamedContent exportation) {
		this.exportation = exportation;
	}
	
	public StreamedContent getExportation() {
		File f = null;
		InputStream stream = null;
		List<TaTiersDTO> values = new ArrayList<>();
		try {	
			for (TaLRelanceDTOJSF taLRelanceDTOJSF : listeRelance) {
				values.add(taTiersService.findByCodeDTO(taLRelanceDTOJSF.getTaLRelance().getCodeTiers()));
			}
			f = taTiersService.exportToCSV(values);
			stream = new FileInputStream(f); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		exportation = new DefaultStreamedContent(stream,null,"tiers.csv");
		
		return exportation;
	}
}

package fr.legrain.document.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import fr.legrain.document.model.TaRReglement;
import fr.legrain.document.model.TaReglement;
import fr.legrain.tiers.model.TaTiers;

public interface IDocumentPayableCB /*extends IDocumentReglementLiable*/ {

	public int getIdDocument();
	public String getCodeDocument();
	public Date getDateDocument();
	public BigDecimal getMtTtcCalc();
	public BigDecimal getNetTtcCalc();
	public TaTiers getTaTiers();
	
	public Set<TaRReglement> getTaRReglements();
	public void setTaRReglements(Set<TaRReglement> rrs);
	public void addRReglement(TaRReglement rr);
	
	public TaReglement getTaReglement();
	
}

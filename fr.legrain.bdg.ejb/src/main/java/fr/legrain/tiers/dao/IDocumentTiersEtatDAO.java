package fr.legrain.tiers.dao;

import java.util.Date;
import java.util.List;

public interface IDocumentTiersEtatDAO<T> {
	
	public List<T> rechercheDocumentEtat(Date dateDeb, Date datefin, String codeEtat);
	
	public List<T> rechercheDocumentEtat(Date dateDeb, Date datefin, String codeEtat, String codeTiers);

}

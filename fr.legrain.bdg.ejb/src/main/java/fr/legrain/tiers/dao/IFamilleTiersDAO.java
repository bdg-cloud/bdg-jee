package fr.legrain.tiers.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.model.TaFamilleTiers;
//import javax.ejb.Remote;

//@Remote
public interface IFamilleTiersDAO extends IGenericDAO<TaFamilleTiers> {
	public TaFamilleTiers rechercheFamilleCogere();
	
	public Boolean exist(String code);
}

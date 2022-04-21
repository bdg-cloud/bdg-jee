package fr.legrain.tiers.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaParamPublipostage;
//import javax.ejb.Remote;

//@Remote
public interface IParamPublipostageDAO extends IGenericDAO<TaParamPublipostage> {
	public TaParamPublipostage findInstance();
}

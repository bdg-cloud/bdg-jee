package fr.legrain.tiers.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.model.TaParamCreeDocTiers;
//import javax.ejb.Remote;

//@Remote
public interface IParamCreeDocTiersDAO extends IGenericDAO<TaParamCreeDocTiers> {

	public List<TaParamCreeDocTiers> findByCodeTypeDoc(String codeTiers,String typeDoc);
}

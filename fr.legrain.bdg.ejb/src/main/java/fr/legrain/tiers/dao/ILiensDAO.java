package fr.legrain.tiers.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.model.TaLiens;
//import javax.ejb.Remote;

//@Remote
public interface ILiensDAO extends IGenericDAO<TaLiens> {

	public TaLiens findLiensTiers(String codeTiers, String codeTypeLiens);
}

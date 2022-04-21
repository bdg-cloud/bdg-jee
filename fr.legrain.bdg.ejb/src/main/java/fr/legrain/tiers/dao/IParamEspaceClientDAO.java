package fr.legrain.tiers.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.model.TaAdresse;
//import javax.ejb.Remote;
import fr.legrain.tiers.model.TaEspaceClient;
import fr.legrain.tiers.model.TaParamEspaceClient;

//@Remote
public interface IParamEspaceClientDAO extends IGenericDAO<TaParamEspaceClient> {
	public TaParamEspaceClient findInstance();
}

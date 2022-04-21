package fr.legrain.general.dao;

import fr.legrain.data.IGenericDAO;
import fr.legrain.general.model.TaAliasEspaceClient;
import fr.legrain.general.model.TaFichier;
import fr.legrain.general.model.TaParametre;

public interface ITaParametreDAO extends IGenericDAO<TaParametre> {
	
	public TaParametre findInstance();

}

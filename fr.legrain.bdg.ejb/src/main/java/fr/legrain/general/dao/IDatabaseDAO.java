package fr.legrain.general.dao;

import java.util.List;

public interface IDatabaseDAO /*extends IGenericDAO<TaFichier>*/ {
	public List<String> listeBdd();
	public List<Object[]> listeSchemaTailleConnection();
}

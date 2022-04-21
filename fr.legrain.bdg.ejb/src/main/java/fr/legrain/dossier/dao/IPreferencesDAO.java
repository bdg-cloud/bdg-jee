package fr.legrain.dossier.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.dossier.model.TaPreferences;

//@Remote
public interface IPreferencesDAO extends IGenericDAO<TaPreferences> {
	public List<TaPreferences> findByGroupe(String groupe);
	public TaPreferences findByGoupeAndCle(String groupe,String cle) ;
	
	public List<TaPreferences> findByCategorie(int idCategorie);
}

package fr.legrain.conformite.dao;

import java.util.List;

import fr.legrain.conformite.model.TaModeleConformite;
import fr.legrain.conformite.model.TaModeleGroupe;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IModeleConformiteDAO extends IGenericDAO<TaModeleConformite> {

	public List<TaModeleConformite> findByModeleGroupe(TaModeleGroupe taModeleGroupe);
	
}

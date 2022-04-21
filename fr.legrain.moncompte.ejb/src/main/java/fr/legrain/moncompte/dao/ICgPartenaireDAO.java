package fr.legrain.moncompte.dao;

import fr.legrain.moncompte.data.IGenericDAO;
import fr.legrain.moncompte.model.TaCgPartenaire;
import fr.legrain.moncompte.model.TaCgv;
import fr.legrain.moncompte.model.TaClient;

//@Remote
public interface ICgPartenaireDAO extends IGenericDAO<TaCgPartenaire> {
	
	public TaCgPartenaire findCgPartenaireCourant();

}

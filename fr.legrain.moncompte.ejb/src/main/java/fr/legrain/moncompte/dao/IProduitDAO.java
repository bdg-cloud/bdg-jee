package fr.legrain.moncompte.dao;

import java.util.List;

import fr.legrain.moncompte.data.IGenericDAO;
import fr.legrain.moncompte.model.TaProduit;

//@Remote
public interface IProduitDAO extends IGenericDAO<TaProduit> {
	public List<TaProduit> selectAllProduitCategoriePro(int idCategoriePro);
}

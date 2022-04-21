package fr.legrain.conformite.dao;

import java.util.List;

import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaListeConformite;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IListeConformiteDAO extends IGenericDAO<TaListeConformite> {

	public List<TaConformite>  findControleConformiteByCodeArticle(String codeArticle);
	
	public List<TaListeConformite>  findByCodeArticle(String codeArticle);
}

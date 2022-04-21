package fr.legrain.conformite.dao;

import java.util.List;

import fr.legrain.conformite.model.TaConformite;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IConformiteDAO extends IGenericDAO<TaConformite> {
	public Boolean controleUtiliseDansUnLot(int idControleConformite);
	public List<TaConformite> controleArticleDerniereVersion(int idArticle);
}

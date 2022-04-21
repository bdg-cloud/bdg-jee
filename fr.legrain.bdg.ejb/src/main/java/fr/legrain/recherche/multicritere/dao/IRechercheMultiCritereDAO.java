package fr.legrain.recherche.multicritere.dao;

import java.util.ArrayList;
import java.util.List;

import fr.legrain.article.model.TaArticle;
import fr.legrain.data.IGenericDAO;
import fr.legrain.recherche.multicritere.model.GroupeLigne;

//@Remote
public interface IRechercheMultiCritereDAO /*extends IGenericDAO<TaArticle>*/ {
	public ArrayList<Object> getResultat(String resultat,List<GroupeLigne> groupe);
}

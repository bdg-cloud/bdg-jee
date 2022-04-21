package fr.legrain.article.dao;

import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IRapportUniteDAO extends IGenericDAO<TaRapportUnite> {
	public TaRapportUnite findByCode1(String code);
	public TaRapportUnite findByCode2(String code) ;
	public TaRapportUnite findByCode1And2(String code1, String code2) ;
}

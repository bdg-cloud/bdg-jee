package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaCoefficientUniteDTO;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ICoefficientUniteDAO extends IGenericDAO<TaCoefficientUnite> {

	public TaCoefficientUnite findByCode(String code1, String code2);
}

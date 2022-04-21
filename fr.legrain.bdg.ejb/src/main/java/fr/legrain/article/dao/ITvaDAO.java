package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaTvaDTO;
import fr.legrain.article.model.TaTva;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ITvaDAO extends IGenericDAO<TaTva> {

	public List<TaTvaDTO> findByCodeLight(String code);
	
}

package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaTTvaDTO;
import fr.legrain.article.model.TaTTva;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ITTvaDAO extends IGenericDAO<TaTTva> {

	public List<TaTTvaDTO> findByCodeLight(String code);
	
}

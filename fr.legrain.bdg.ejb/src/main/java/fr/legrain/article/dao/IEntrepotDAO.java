package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaEntrepotDTO;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IEntrepotDAO extends IGenericDAO<TaEntrepot> {

	public List<TaEntrepotDTO> findByCodeLight(String code);
	public List<TaEntrepotDTO> findAllLight();
}

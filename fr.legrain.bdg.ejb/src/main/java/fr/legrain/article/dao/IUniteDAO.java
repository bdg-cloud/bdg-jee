package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaUniteDTO;
import fr.legrain.article.model.TaUnite;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IUniteDAO extends IGenericDAO<TaUnite> {

	public List<TaUniteDTO> findByCodeLight(String code);
	public List<TaUniteDTO> findByCodeLightUniteStock(String codeUniteStock, String code);
	public List<TaUnite> findByCodeUniteStock(String codeUniteStock, String code);
}

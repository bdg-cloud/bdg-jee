package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaTReceptionDTO;
import fr.legrain.article.model.TaTReception;
import fr.legrain.data.IGenericDAO;
//import javax.ejb.Remote;

//@Remote
public interface ITReceptionDAO extends IGenericDAO<TaTReception> {
	public List<TaTReceptionDTO> findByCodeLight(String code);
}

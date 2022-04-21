package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaTFabricationDTO;
import fr.legrain.article.model.TaTFabrication;
import fr.legrain.data.IGenericDAO;
//import javax.ejb.Remote;

//@Remote
public interface ITFabricationDAO extends IGenericDAO<TaTFabrication> {
	public List<TaTFabricationDTO> findByCodeLight(String code);
}

package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.data.IGenericDAO;
//import javax.ejb.Remote;

//@Remote
public interface ITransporteurDAO extends IGenericDAO<TaTransporteur> {
	public List<TaTransporteurDTO> findByCodeLight(String code);
}

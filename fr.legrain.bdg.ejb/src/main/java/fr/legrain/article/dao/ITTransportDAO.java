package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaTTransportDTO;
import fr.legrain.article.model.TaTTransport;
import fr.legrain.data.IGenericDAO;
//import javax.ejb.Remote;

//@Remote
public interface ITTransportDAO extends IGenericDAO<TaTTransport> {
	public List<TaTTransportDTO> findByCodeLight(String code);
}

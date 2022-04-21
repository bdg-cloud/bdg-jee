package fr.legrain.article.titretransport.dao;

import java.util.List;

import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ITitreTransportCapsulesDAO extends IGenericDAO<TaTitreTransport> {
	public List<TaTitreTransportDTO> findByCodeLight(String code);
}

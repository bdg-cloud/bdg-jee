package fr.legrain.servicewebexterne.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;

//@Remote
public interface ITServiceWebExterneDAO extends IGenericDAO<TaTServiceWebExterne> {
	public List<TaTServiceWebExterneDTO> findByCodeLight(String code);
}

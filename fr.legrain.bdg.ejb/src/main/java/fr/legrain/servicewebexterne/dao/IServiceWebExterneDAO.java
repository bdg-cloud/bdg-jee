package fr.legrain.servicewebexterne.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;

//@Remote
public interface IServiceWebExterneDAO extends IGenericDAO<TaServiceWebExterne> {
	public List<TaServiceWebExterneDTO> findByCodeLight(String code);
	public List<TaServiceWebExterneDTO> findAllLightActif();
}

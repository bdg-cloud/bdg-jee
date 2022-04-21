package fr.legrain.servicewebexterne.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.servicewebexterne.dto.TaSynchroServiceExterneDTO;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;

public interface ITaSynchroServiceWebExterneDAO extends IGenericDAO<TaSynchroServiceExterne> { 
	public List<TaSynchroServiceExterneDTO> findAllByTypeEntiteAndByIdCompteServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne);
	public Date findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne);

}

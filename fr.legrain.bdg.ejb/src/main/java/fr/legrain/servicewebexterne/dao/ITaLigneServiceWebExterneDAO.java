package fr.legrain.servicewebexterne.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;

public interface ITaLigneServiceWebExterneDAO extends IGenericDAO<TaLigneServiceWebExterne> {
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermine();
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermine();
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermineByIdCompteServiceWebExterne(Integer id);
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermineByIdCompteServiceWebExterne(Integer id);
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermineByIdServiceWebExterne(Integer id);
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermineByIdServiceWebExterne(Integer id);
	public List<String> findAllIDExterneByIdCompteService(Integer id);

}

package fr.legrain.servicewebexterne.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.servicewebexterne.dto.TaTAuthentificationDTO;
import fr.legrain.servicewebexterne.model.TaTAuthentification;

//@Remote
public interface ITAuthentificationDAO extends IGenericDAO<TaTAuthentification> {
	public List<TaTAuthentificationDTO> findByCodeLight(String code);
}

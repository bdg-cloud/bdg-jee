package fr.legrain.tiers.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.model.TaTEntite;
//import javax.ejb.Remote;

//@Remote
public interface ITEntiteDAO extends IGenericDAO<TaTEntite> {

	// Dima - Début
	public List<TaTEntiteDTO> findByCodeLight(String code);
	// Dima -  Fin
	
}

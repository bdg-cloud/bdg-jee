package fr.legrain.caisse.dao;

import java.util.List;

import fr.legrain.caisse.dto.TaTFondDeCaisseDTO;
import fr.legrain.caisse.model.TaTFondDeCaisse;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ITFondDeCaisseDAO extends IGenericDAO<TaTFondDeCaisse> {

	public List<TaTFondDeCaisseDTO> findByCodeLight(String code);
	
}

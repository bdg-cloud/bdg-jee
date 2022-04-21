package fr.legrain.caisse.dao;

import java.util.List;

import fr.legrain.caisse.dto.TaTDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaTDepotRetraitCaisse;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ITDepotRetraitCaisseDAO extends IGenericDAO<TaTDepotRetraitCaisse> {

	public List<TaTDepotRetraitCaisseDTO> findByCodeLight(String code);
	
}

package fr.legrain.caisse.dao;

import java.util.List;

import fr.legrain.caisse.dto.TaTLSessionCaisseDTO;
import fr.legrain.caisse.model.TaTLSessionCaisse;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ITLSessionCaisseDAO extends IGenericDAO<TaTLSessionCaisse> {

	public List<TaTLSessionCaisseDTO> findByCodeLight(String code);
	
}

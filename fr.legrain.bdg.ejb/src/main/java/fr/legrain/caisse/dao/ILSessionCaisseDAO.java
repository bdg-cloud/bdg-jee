package fr.legrain.caisse.dao;

import java.util.List;

import fr.legrain.caisse.dto.TaLSessionCaisseDTO;
import fr.legrain.caisse.model.TaLSessionCaisse;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ILSessionCaisseDAO extends IGenericDAO<TaLSessionCaisse> {

	public List<TaLSessionCaisseDTO> findByCodeLight(String code);
	
}

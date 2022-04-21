package fr.legrain.caisse.dao;

import java.util.List;

import fr.legrain.caisse.dto.TaFondDeCaisseDTO;
import fr.legrain.caisse.model.TaFondDeCaisse;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IFondDeCaisseDAO extends IGenericDAO<TaFondDeCaisse> {

	public List<TaFondDeCaisseDTO> findByCodeLight(String code);
	public List<TaFondDeCaisse> findBySessionCaisseCourante();
	
}

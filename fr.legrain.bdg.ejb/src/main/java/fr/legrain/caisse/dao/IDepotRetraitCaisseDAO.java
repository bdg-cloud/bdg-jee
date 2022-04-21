package fr.legrain.caisse.dao;

import java.util.List;

import fr.legrain.caisse.dto.TaDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaDepotRetraitCaisse;
import fr.legrain.caisse.model.TaFondDeCaisse;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IDepotRetraitCaisseDAO extends IGenericDAO<TaDepotRetraitCaisse> {

	public List<TaDepotRetraitCaisseDTO> findByCodeLight(String code);
	public List<TaDepotRetraitCaisse> findBySessionCaisseCourante();
}

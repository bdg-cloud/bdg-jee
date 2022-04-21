package fr.legrain.caisse.dao;

import java.util.List;

import fr.legrain.caisse.dto.TaSessionCaisseDTO;
import fr.legrain.caisse.model.TaSessionCaisse;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface ISessionCaisseDAO extends IGenericDAO<TaSessionCaisse> {

	public List<TaSessionCaisseDTO> findByCodeLight(String code);
	
	public TaSessionCaisse findSessionCaisseActive(int idUtilisateur, String numeroCaisse);
}

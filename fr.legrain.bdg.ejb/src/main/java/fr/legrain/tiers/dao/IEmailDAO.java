package fr.legrain.tiers.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.dto.TaEmailDTO;
import fr.legrain.tiers.model.TaEmail;
//import javax.ejb.Remote;

//@Remote
public interface IEmailDAO extends IGenericDAO<TaEmail> {
	public List<TaEmailDTO> findAllLight();
}

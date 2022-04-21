package fr.legrain.tiers.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.dto.TaTelephoneDTO;
//import javax.ejb.Remote;
import fr.legrain.tiers.model.TaTelephone;

//@Remote
public interface ITelephoneDAO extends IGenericDAO<TaTelephone> {
	public List<TaTelephoneDTO> findAllLight();
}

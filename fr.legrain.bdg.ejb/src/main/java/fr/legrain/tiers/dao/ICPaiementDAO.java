package fr.legrain.tiers.dao;

import java.util.List;

import javax.persistence.Query;

import fr.legrain.data.IGenericDAO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.model.TaCPaiement;
//import javax.ejb.Remote;

//@Remote
public interface ICPaiementDAO extends IGenericDAO<TaCPaiement> {
	public List<TaCPaiementDTO> findAllCPaiementTiers();
	
	public List<TaCPaiementDTO> findAllCPaiementDoc();
	
	public List<TaCPaiement> rechercheParType(String codeType);
}

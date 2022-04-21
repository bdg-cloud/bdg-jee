package fr.legrain.abonnement.dao.jpa;

import java.util.List;

import fr.legrain.abonnement.dto.TaFrequenceDTO;
import fr.legrain.abonnement.model.TaFrequence;
import fr.legrain.data.IGenericDAO;

public interface IFrequenceDAO extends IGenericDAO<TaFrequence>{
	List<TaFrequenceDTO> findByCodeLight(String code);
	public List<TaFrequenceDTO> findAllLight();
//	public List<TaFrequenceDTO> findLightSurCodeTTarif(String codeTTarif) ;
//	public List<TaFrequenceDTO> findLightSurCodeTTarifEtCodeTiers(String codeTTarif,String codeTiers); 
//	public TaFrequenceDTO findByLibelle(String libelle);
}

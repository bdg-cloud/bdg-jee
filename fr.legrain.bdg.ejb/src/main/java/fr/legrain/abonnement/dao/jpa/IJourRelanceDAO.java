package fr.legrain.abonnement.dao.jpa;

import java.util.List;

import fr.legrain.abonnement.dto.TaJourRelanceDTO;
import fr.legrain.abonnement.model.TaJourRelance;
import fr.legrain.data.IGenericDAO;

public interface IJourRelanceDAO extends IGenericDAO<TaJourRelance>{
	List<TaJourRelanceDTO> findByCodeLight(String code);
	public List<TaJourRelanceDTO> findAllLight();
	public List<TaJourRelanceDTO> findByIdArticleDTO(int idArticle);
//	public List<TaJourRelanceDTO> findLightSurCodeTTarif(String codeTTarif) ;
//	public List<TaJourRelanceDTO> findLightSurCodeTTarifEtCodeTiers(String codeTTarif,String codeTiers); 
//	public TaJourRelanceDTO findByLibelle(String libelle);
}

package fr.legrain.article.dao;

import java.util.List;

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.model.TaFamille;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IFamilleDAO extends IGenericDAO<TaFamille> {

	List<TaFamilleDTO> findByCodeLight(String code);
	public List<TaFamilleDTO> findAllLight();
	public List<TaFamilleDTO> findLightSurCodeTTarif(String codeTTarif) ;
	public List<TaFamilleDTO> findLightSurCodeTTarifEtCodeTiers(String codeTTarif,String codeTiers); 
	public TaFamille findByLibelle(String libelle);
}

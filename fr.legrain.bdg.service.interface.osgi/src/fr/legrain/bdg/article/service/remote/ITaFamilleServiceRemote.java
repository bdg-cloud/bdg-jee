package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaFamilleDTO;
import fr.legrain.article.model.TaFamille;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaFamilleServiceRemote extends IGenericCRUDServiceRemote<TaFamille,TaFamilleDTO>,
														IAbstractLgrDAOServer<TaFamille>,IAbstractLgrDAOServerDTO<TaFamilleDTO>{
	public static final String validationContext = "FAMILLE";
	
	public List<TaFamilleDTO> findByCodeLight(String code);
	public List<TaFamilleDTO> findAllLight();
	public List<TaFamilleDTO> findLightSurCodeTTarif(String codeTTarif) ;
	public List<TaFamilleDTO> findLightSurCodeTTarifEtCodeTiers(String codeTTarif,String codeTiers); 
	public TaFamille findByLibelle(String libelle);
}

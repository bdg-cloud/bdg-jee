package fr.legrain.abonnement.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.abonnement.model.TaJourRelance;
import fr.legrain.abonnement.dto.TaJourRelanceDTO;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaJourRelanceServiceRemote extends IGenericCRUDServiceRemote<TaJourRelance,TaJourRelanceDTO>,
														IAbstractLgrDAOServer<TaJourRelance>,IAbstractLgrDAOServerDTO<TaJourRelanceDTO>{
	public static final String validationContext = "JOUR_RELANCE";
	
	//public List<TaJourRelanceDTO> findByCodeLight(String code);
	public List<TaJourRelanceDTO> findAllLight();
	public List<TaJourRelanceDTO> findByIdArticleDTO(int idArticle);
	//public TaJourRelance findByLibelle(String libelle);
}

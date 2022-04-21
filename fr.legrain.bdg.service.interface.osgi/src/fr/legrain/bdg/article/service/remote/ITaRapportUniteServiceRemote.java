package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaRapportUniteDTO;
import fr.legrain.article.model.TaRapportUnite;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaRapportUniteServiceRemote extends IGenericCRUDServiceRemote<TaRapportUnite,TaRapportUniteDTO>,
														IAbstractLgrDAOServer<TaRapportUnite>,IAbstractLgrDAOServerDTO<TaRapportUniteDTO>{
	public static final String validationContext = "RAPPORT_UNITE";
	
	public TaRapportUnite findByCode1(String code);
	public TaRapportUnite findByCode2(String code) ;
	
	public TaRapportUnite findByCode1And2(String code1, String code2) ;
}

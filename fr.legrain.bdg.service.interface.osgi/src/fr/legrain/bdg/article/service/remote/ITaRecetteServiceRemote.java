package fr.legrain.bdg.article.service.remote;

import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.article.model.TaRecette;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaRecetteDTO;

@Remote
public interface ITaRecetteServiceRemote extends IGenericCRUDServiceRemote<TaRecette,TaRecetteDTO>,
														IAbstractLgrDAOServer<TaRecette>,IAbstractLgrDAOServerDTO<TaRecetteDTO>{
	public static final String validationContext = "RECETTE";

	public TaRecette findByCodeWithList(String code);
	
	public String genereCode( Map<String, String> params);
	
}

package fr.legrain.bdg.article.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaModeleFabricationDTO;
import fr.legrain.article.model.TaModeleFabrication;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaModeleFabricationServiceRemote extends IGenericCRUDServiceRemote<TaModeleFabrication,TaModeleFabricationDTO>,
														IAbstractLgrDAOServer<TaModeleFabrication>,IAbstractLgrDAOServerDTO<TaModeleFabricationDTO>{
	public static final String validationContext = "MODELE_FABRICATION";

	public TaModeleFabrication findByCodeWithList(String code);
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
}

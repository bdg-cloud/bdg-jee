package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaCoefficientUniteDTO;
import fr.legrain.article.model.TaCoefficientUnite;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaCoefficientUniteServiceRemote extends IGenericCRUDServiceRemote<TaCoefficientUnite,TaCoefficientUniteDTO>,
														IAbstractLgrDAOServer<TaCoefficientUnite>,IAbstractLgrDAOServerDTO<TaCoefficientUniteDTO>{
	public static final String validationContext = "COEFFICIENT_UNITE";
	
	public static final String METRE = "M";
	public static final String METRE_CUBE = "M3";
	public static final String LITRE = "L";
	public static final String KILOGRAMME = "Kg";

	
	public TaCoefficientUnite findByCode(String code1, String code2);
}

package fr.legrain.bdg.article.service.remote;

import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLFabricationDTO;

@Remote
public interface ITaLFabricationPFServiceRemote extends IGenericCRUDServiceRemote<TaLFabricationPF,TaLFabricationDTO>,
														IAbstractLgrDAOServer<TaLFabricationPF>,IAbstractLgrDAOServerDTO<TaLFabricationDTO>{
	public static final String validationContext = "L_FABRICATION";
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
}

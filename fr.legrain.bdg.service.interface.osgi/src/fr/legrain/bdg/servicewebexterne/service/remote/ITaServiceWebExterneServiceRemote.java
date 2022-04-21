package fr.legrain.bdg.servicewebexterne.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.servicewebexterne.dto.TaServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;


@Remote
public interface ITaServiceWebExterneServiceRemote extends IGenericCRUDServiceRemote<TaServiceWebExterne,TaServiceWebExterneDTO>,
														IAbstractLgrDAOServer<TaServiceWebExterne>,IAbstractLgrDAOServerDTO<TaServiceWebExterneDTO>{
	public static final String validationContext = "SERVICEWEB_EXTERNE";
	
	public List<TaServiceWebExterneDTO> findByCodeLight(String code);
	public List<TaServiceWebExterneDTO> findAllLightActif();
}

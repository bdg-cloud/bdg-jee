package fr.legrain.bdg.servicewebexterne.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.servicewebexterne.dto.TaTServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.TaTServiceWebExterne;


@Remote
public interface ITaTServiceWebExterneServiceRemote extends IGenericCRUDServiceRemote<TaTServiceWebExterne,TaTServiceWebExterneDTO>,
														IAbstractLgrDAOServer<TaTServiceWebExterne>,IAbstractLgrDAOServerDTO<TaTServiceWebExterneDTO>{
	public static final String validationContext = "T_SERVICEWEB_EXTERNE";
	
	public List<TaTServiceWebExterneDTO> findByCodeLight(String code);
}

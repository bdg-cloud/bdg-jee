package fr.legrain.bdg.servicewebexterne.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.servicewebexterne.dto.TaSynchroServiceExterneDTO;
import fr.legrain.servicewebexterne.model.TaSynchroServiceExterne;

@Remote
public interface ITaSynchroServiceExterneServiceRemote extends IGenericCRUDServiceRemote<TaSynchroServiceExterne,TaSynchroServiceExterneDTO>,
IAbstractLgrDAOServer<TaSynchroServiceExterne>,IAbstractLgrDAOServerDTO<TaSynchroServiceExterneDTO> {
	
	public static final String validationContext = "TA_SYNCHRO_SERVICE_EXTERNE_SERVICE";
	public List<TaSynchroServiceExterneDTO> findAllByTypeEntiteAndByIdCompteServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne);
	public Date findLastDateByTypeEntiteAndByIdCompteServiceWebExterneDTO(String typeEntite, Integer idServiceWebExterne);

}

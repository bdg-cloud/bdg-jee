package fr.legrain.bdg.article.titretransport.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.article.titretransport.model.TaTitreTransport;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTitreTransportServiceRemote extends IGenericCRUDServiceRemote<TaTitreTransport,TaTitreTransportDTO>,
														IAbstractLgrDAOServer<TaTitreTransport>,IAbstractLgrDAOServerDTO<TaTitreTransportDTO>{
	public static final String validationContext = "TITRE_TRANSPORT";
	public List<TaTitreTransportDTO> findByCodeLight(String code);
	
}

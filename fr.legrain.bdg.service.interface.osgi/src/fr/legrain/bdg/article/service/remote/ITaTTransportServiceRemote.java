package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTTransportDTO;
import fr.legrain.article.model.TaTTransport;
import fr.legrain.article.titretransport.dto.TaTitreTransportDTO;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTTransportServiceRemote extends IGenericCRUDServiceRemote<TaTTransport,TaTTransportDTO>,
														IAbstractLgrDAOServer<TaTTransport>,IAbstractLgrDAOServerDTO<TaTTransportDTO>{
	public static final String validationContext = "T_TRANSPORT";
	
	public List<TaTTransportDTO> findByCodeLight(String code);
}

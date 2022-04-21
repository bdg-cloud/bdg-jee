package fr.legrain.bdg.article.service.remote;

import javax.ejb.FinderException;
import javax.ejb.Remote;

import fr.legrain.article.dto.TaRTitreTransportDTO;
import fr.legrain.article.model.TaRTaTitreTransport;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaRTitreTransportServiceRemote extends IGenericCRUDServiceRemote<TaRTaTitreTransport,TaRTitreTransportDTO>,
														IAbstractLgrDAOServer<TaRTaTitreTransport>,IAbstractLgrDAOServerDTO<TaRTitreTransportDTO>{
	public static final String validationContext = "R_TITRE_TRANSPORT";
	
	public TaRTaTitreTransport findByIdArticle(int idArticle)throws FinderException;
}

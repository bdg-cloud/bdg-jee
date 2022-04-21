package fr.legrain.bdg.documents.service.remote;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaRAvoirDTO;
import fr.legrain.document.model.TaRAvoir;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaRAvoirServiceRemote extends IGenericCRUDServiceRemote<TaRAvoir,TaRAvoirDTO>,
														IAbstractLgrDAOServer<TaRAvoir>,IAbstractLgrDAOServerDTO<TaRAvoirDTO>,
														IDocumentService<TaRAvoir>{
	public List<TaRAvoir> selectAll(TaTiers taTiers,Date dateDeb,Date dateFin);
	
	
}

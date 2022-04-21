package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaReglementTracaDTO;
import fr.legrain.document.model.TaReglementTraca;

@Remote
public interface ITaReglementTracaServiceRemote extends IGenericCRUDServiceRemote<TaReglementTraca,TaReglementTracaDTO>,
														IAbstractLgrDAOServer<TaReglementTraca>,IAbstractLgrDAOServerDTO<TaReglementTracaDTO>{
	

}

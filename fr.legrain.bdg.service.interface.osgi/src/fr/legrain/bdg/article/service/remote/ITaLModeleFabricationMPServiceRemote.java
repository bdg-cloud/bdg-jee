package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.model.TaLModeleFabricationMP;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLModeleFabricationDTO;

@Remote
public interface ITaLModeleFabricationMPServiceRemote extends IGenericCRUDServiceRemote<TaLModeleFabricationMP,TaLModeleFabricationDTO>,
														IAbstractLgrDAOServer<TaLModeleFabricationMP>,IAbstractLgrDAOServerDTO<TaLModeleFabricationDTO>{
	public static final String validationContext = "L_MODELE_FABRICATION";
}

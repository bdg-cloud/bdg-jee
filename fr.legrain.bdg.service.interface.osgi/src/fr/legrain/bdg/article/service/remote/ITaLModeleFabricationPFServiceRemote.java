package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.model.TaLModeleFabricationPF;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLModeleFabricationDTO;

@Remote
public interface ITaLModeleFabricationPFServiceRemote extends IGenericCRUDServiceRemote<TaLModeleFabricationPF,TaLModeleFabricationDTO>,
														IAbstractLgrDAOServer<TaLModeleFabricationPF>,IAbstractLgrDAOServerDTO<TaLModeleFabricationDTO>{
	public static final String validationContext = "L_MODELE_FABRICATION";
}

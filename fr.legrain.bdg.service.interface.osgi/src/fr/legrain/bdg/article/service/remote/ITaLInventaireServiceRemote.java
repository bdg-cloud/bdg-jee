package fr.legrain.bdg.article.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.stock.dto.LInventaireDTO;
import fr.legrain.stock.model.TaLInventaire;

@Remote
public interface ITaLInventaireServiceRemote extends IGenericCRUDServiceRemote<TaLInventaire,LInventaireDTO>,
														IAbstractLgrDAOServer<TaLInventaire>,IAbstractLgrDAOServerDTO<LInventaireDTO>{
	public static final String validationContext = "L_INVENTAIRE";
	
	public List<LInventaireDTO> findAllLight(Integer idInventaire);
}

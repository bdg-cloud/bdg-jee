package fr.legrain.bdg.article.service.remote;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.stock.dto.InventaireDTO;
import fr.legrain.stock.model.TaInventaire;
import fr.legrain.tiers.dto.TaTiersDTO;

@Remote
public interface ITaInventaireServiceRemote extends IGenericCRUDServiceRemote<TaInventaire,InventaireDTO>,
														IAbstractLgrDAOServer<TaInventaire>,IAbstractLgrDAOServerDTO<InventaireDTO>{
	public static final String validationContext = "INVENTAIRE";
	public String genereCode( Map<String, String> params);
	
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public InventaireDTO findByCodeLight(String code);
	public List<InventaireDTO> findAllLight();
}

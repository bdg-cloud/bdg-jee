package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaCgPartenaireDTO;
import fr.legrain.moncompte.model.TaCgPartenaire;


@Remote
public interface ITaCgPartenaireServiceRemote extends IGenericCRUDServiceRemote<TaCgPartenaire,TaCgPartenaireDTO>,
														IAbstractLgrDAOServer<TaCgPartenaire>,IAbstractLgrDAOServerDTO<TaCgPartenaireDTO>{
	public static final String validationContext = "CG_PARTENAIRE";
	
	public TaCgPartenaire findCgPartenaireCourant();

}

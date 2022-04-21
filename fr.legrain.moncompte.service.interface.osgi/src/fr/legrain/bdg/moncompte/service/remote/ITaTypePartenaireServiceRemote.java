package fr.legrain.bdg.moncompte.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaTypePartenaireDTO;
import fr.legrain.moncompte.model.TaTypePartenaire;


@Remote
public interface ITaTypePartenaireServiceRemote extends IGenericCRUDServiceRemote<TaTypePartenaire,TaTypePartenaireDTO>,
														IAbstractLgrDAOServer<TaTypePartenaire>,IAbstractLgrDAOServerDTO<TaTypePartenaireDTO>{
	public static final String validationContext = "TYPE_PARTENAIRE";
}

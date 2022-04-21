package fr.legrain.bdg.article.service.remote;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaTypeMouvementDTO;
import fr.legrain.article.model.TaTypeMouvement;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaTypeMouvementServiceRemote extends IGenericCRUDServiceRemote<TaTypeMouvement,TaTypeMouvementDTO>,
 IAbstractLgrDAOServer<TaTypeMouvement>,IAbstractLgrDAOServerDTO<TaTypeMouvementDTO>{
    public static final String validationContext = "TYPE_MOUVEMENT";

    public Boolean typeMouvementReserve(String code);

} 

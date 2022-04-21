package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaParamCreeDocTiersDTO;
import fr.legrain.tiers.model.TaParamCreeDocTiers;

@Remote
public interface ITaParamCreeDocTiersServiceRemote  extends IGenericCRUDServiceRemote<TaParamCreeDocTiers,TaParamCreeDocTiersDTO>,
IAbstractLgrDAOServer<TaParamCreeDocTiers>,IAbstractLgrDAOServerDTO<TaParamCreeDocTiersDTO>{

public static final String validationContext = "PARAM_CREE_DOC_TIERS";

public List<TaParamCreeDocTiers> findByCodeTypeDoc(String codeTiers,String typeDoc);

}
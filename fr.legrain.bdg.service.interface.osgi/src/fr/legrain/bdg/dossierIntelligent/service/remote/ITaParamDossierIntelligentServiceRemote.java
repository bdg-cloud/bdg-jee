package fr.legrain.bdg.dossierIntelligent.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.dossierIntelligent.dto.TaParamDossierIntelligentDTO;
import fr.legrain.dossierIntelligent.model.TaParamDossierIntel;

@Remote
public interface ITaParamDossierIntelligentServiceRemote extends IGenericCRUDServiceRemote<TaParamDossierIntel,TaParamDossierIntelligentDTO>,
														IAbstractLgrDAOServer<TaParamDossierIntel>,IAbstractLgrDAOServerDTO<TaParamDossierIntelligentDTO>{
	public static final String validationContext = "PARAM_DOSSIER_INTELLIGENT";
	
	
}

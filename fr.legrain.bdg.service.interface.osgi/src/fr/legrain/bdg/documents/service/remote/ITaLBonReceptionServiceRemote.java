package fr.legrain.bdg.documents.service.remote;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLBonReceptionDTO;
import fr.legrain.document.model.TaLBonReception;

@Remote
public interface ITaLBonReceptionServiceRemote extends IGenericCRUDServiceRemote<TaLBonReception,TaLBonReceptionDTO>,
														IAbstractLgrDAOServer<TaLBonReception>,IAbstractLgrDAOServerDTO<TaLBonReceptionDTO>{
	public static final String validationContext = "L_BON_RECEPTION";
	
	public List<TaLBonReception> findLigneAvecResultatConformites(int idBr) ;
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
}

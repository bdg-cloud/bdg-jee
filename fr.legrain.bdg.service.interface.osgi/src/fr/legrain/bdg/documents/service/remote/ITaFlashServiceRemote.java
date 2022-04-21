package fr.legrain.bdg.documents.service.remote;

import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaFlashDTO;
import fr.legrain.document.model.TaEtat;
import fr.legrain.document.model.TaFlash;
import fr.legrain.document.model.TaLFlash;
import fr.legrain.document.model.TaPreparation;


@Remote
public interface ITaFlashServiceRemote extends IGenericCRUDServiceRemote<TaFlash,TaFlashDTO>,
														IAbstractLgrDAOServer<TaFlash>,IAbstractLgrDAOServerDTO<TaFlashDTO>{
	
	public static final String validationContext = "FLASH";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public TaEtat etatLigneInsertion(TaFlash masterEntity);
	
	public TaFlash findDocByLDoc(TaLFlash lDoc);
	public TaFlash mergeEtat(TaFlash detachedInstance);
}

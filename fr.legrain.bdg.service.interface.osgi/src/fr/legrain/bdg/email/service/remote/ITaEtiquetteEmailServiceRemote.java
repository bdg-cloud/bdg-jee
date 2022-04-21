package fr.legrain.bdg.email.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.email.dto.TaEtiquetteEmailDTO;
import fr.legrain.email.model.TaEtiquetteEmail;

@Remote
public interface ITaEtiquetteEmailServiceRemote extends IGenericCRUDServiceRemote<TaEtiquetteEmail,TaEtiquetteEmailDTO>,
														IAbstractLgrDAOServer<TaEtiquetteEmail>,IAbstractLgrDAOServerDTO<TaEtiquetteEmailDTO>{
	public static final String validationContext = "ETIQUETTE_EMAIL";
	
//	public List<TaEtiquetteEmailDTO> selectAllDTOLight();
//	public List<TaEtiquetteEmailDTO> selectAllDTOLight(Date debut, Date fin);
	
}

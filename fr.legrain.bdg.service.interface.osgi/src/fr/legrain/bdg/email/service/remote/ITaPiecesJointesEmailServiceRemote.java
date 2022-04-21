package fr.legrain.bdg.email.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.email.dto.TaPieceJointeEmailDTO;
import fr.legrain.email.model.TaPieceJointeEmail;
import fr.legrain.email.model.TaPieceJointeEmail;

@Remote
public interface ITaPiecesJointesEmailServiceRemote extends IGenericCRUDServiceRemote<TaPieceJointeEmail,TaPieceJointeEmailDTO>,
														IAbstractLgrDAOServer<TaPieceJointeEmail>,IAbstractLgrDAOServerDTO<TaPieceJointeEmailDTO>{
	public static final String validationContext = "PIECES_JOINTES_EMAIL";
	
//	public List<TaPieceJointeEmailDTO> selectAllDTOLight();
//	public List<TaPieceJointeEmailDTO> selectAllDTOLight(Date debut, Date fin);
	
}

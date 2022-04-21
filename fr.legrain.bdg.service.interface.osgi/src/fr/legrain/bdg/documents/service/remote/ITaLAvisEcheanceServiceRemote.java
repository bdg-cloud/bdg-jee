package fr.legrain.bdg.documents.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaLAvisEcheanceDTO;
import fr.legrain.document.model.TaLAvisEcheance;

@Remote
public interface ITaLAvisEcheanceServiceRemote extends IGenericCRUDServiceRemote<TaLAvisEcheance,TaLAvisEcheanceDTO>,
														IAbstractLgrDAOServer<TaLAvisEcheance>,IAbstractLgrDAOServerDTO<TaLAvisEcheanceDTO>{
	public static final String validationContext = "L_AVIS_ECHEANCE";
	public List<TaLAvisEcheance> findAllByIdEcheance(Integer id);
	public List<TaLAvisEcheance> findAllByIdEcheanceSansLigneCom(Integer id);
}

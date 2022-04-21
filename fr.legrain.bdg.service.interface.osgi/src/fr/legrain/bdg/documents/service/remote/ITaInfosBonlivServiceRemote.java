package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaInfosFacture;

@Remote
//public interface ITaInfosDevisServiceRemote extends IGenericCRUDServiceRemote<TaInfosDevis,TaInfosDevisDTO>,
//														IAbstractLgrDAOServer<TaInfosDevis>,IAbstractLgrDAOServerDTO<TaInfosDevisDTO>{
public interface ITaInfosBonlivServiceRemote extends IGenericCRUDServiceRemote<TaInfosBonliv,TaBonlivDTO>,
														IAbstractLgrDAOServer<TaInfosBonliv>,IAbstractLgrDAOServerDTO<TaBonlivDTO>{
	public TaInfosBonliv findByCodeBonliv(String code);

}

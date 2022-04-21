package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.model.TaInfosAcompte;
import fr.legrain.document.model.TaInfosDevis;

@Remote
//public interface ITaInfosDevisServiceRemote extends IGenericCRUDServiceRemote<TaInfosDevis,TaInfosDevisDTO>,
//														IAbstractLgrDAOServer<TaInfosDevis>,IAbstractLgrDAOServerDTO<TaInfosDevisDTO>{
public interface ITaInfosAcompteServiceRemote extends IGenericCRUDServiceRemote<TaInfosAcompte,TaAcompteDTO>,
														IAbstractLgrDAOServer<TaInfosAcompte>,IAbstractLgrDAOServerDTO<TaAcompteDTO>{
	public TaInfosAcompte findByCodeAcompte(String code);

}

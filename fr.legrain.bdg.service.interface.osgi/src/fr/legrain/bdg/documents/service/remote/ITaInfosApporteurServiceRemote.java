package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaApporteurDTO;
import fr.legrain.document.dto.TaFactureDTO;
import fr.legrain.document.model.TaInfosApporteur;
import fr.legrain.document.model.TaInfosFacture;

@Remote
//public interface ITaInfosDevisServiceRemote extends IGenericCRUDServiceRemote<TaInfosDevis,TaInfosDevisDTO>,
//														IAbstractLgrDAOServer<TaInfosDevis>,IAbstractLgrDAOServerDTO<TaInfosDevisDTO>{
public interface ITaInfosApporteurServiceRemote extends IGenericCRUDServiceRemote<TaInfosApporteur,TaApporteurDTO>,
														IAbstractLgrDAOServer<TaInfosApporteur>,IAbstractLgrDAOServerDTO<TaApporteurDTO>{
	public TaInfosApporteur findByCodeApporteur(String code);

}

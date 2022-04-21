package fr.legrain.bdg.documents.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.dto.TaDevisDTO;
import fr.legrain.document.model.TaInfosAbonnement;
import fr.legrain.document.model.TaInfosAbonnement;

@Remote
//public interface ITaInfosAbonnementServiceRemote extends IGenericCRUDServiceRemote<TaInfosAbonnement,TaInfosAbonnementDTO>,
//														IAbstractLgrDAOServer<TaInfosAbonnement>,IAbstractLgrDAOServerDTO<TaInfosAbonnementDTO>{
public interface ITaInfosAbonnementServiceRemote extends IGenericCRUDServiceRemote<TaInfosAbonnement,TaAbonnementDTO>,
														IAbstractLgrDAOServer<TaInfosAbonnement>,IAbstractLgrDAOServerDTO<TaAbonnementDTO>{
	public TaInfosAbonnement findByCodeDevis(String code);

}

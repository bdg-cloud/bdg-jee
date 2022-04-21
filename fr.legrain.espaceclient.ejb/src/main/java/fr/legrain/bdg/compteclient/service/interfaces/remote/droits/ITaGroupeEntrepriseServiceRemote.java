package fr.legrain.bdg.compteclient.service.interfaces.remote.droits;

import javax.ejb.Remote;

import fr.legrain.bdg.compteclient.dto.droits.TaGroupeEntrepriseDTO;
import fr.legrain.bdg.compteclient.model.droits.TaGroupeEntreprise;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServer;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.compteclient.service.interfaces.remote.general.IGenericCRUDServiceRemote;

@Remote
public interface ITaGroupeEntrepriseServiceRemote extends IGenericCRUDServiceRemote<TaGroupeEntreprise,TaGroupeEntrepriseDTO>,
														IAbstractLgrDAOServer<TaGroupeEntreprise>,IAbstractLgrDAOServerDTO<TaGroupeEntrepriseDTO>{
	public static final String validationContext = "GROUPE_ENTREPRISE";
}

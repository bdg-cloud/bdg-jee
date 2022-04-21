package fr.legrain.bdg.droits.service.remote;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.droits.dto.TaGroupeEntrepriseDTO;
import fr.legrain.droits.model.TaGroupeEntreprise;

@Remote
public interface ITaGroupeEntrepriseServiceRemote extends IGenericCRUDServiceRemote<TaGroupeEntreprise,TaGroupeEntrepriseDTO>,
														IAbstractLgrDAOServer<TaGroupeEntreprise>,IAbstractLgrDAOServerDTO<TaGroupeEntrepriseDTO>{
	public static final String validationContext = "GROUPE_ENTREPRISE";
}

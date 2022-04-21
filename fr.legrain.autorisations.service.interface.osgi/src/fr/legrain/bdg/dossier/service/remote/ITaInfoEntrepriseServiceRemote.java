package fr.legrain.bdg.dossier.service.remote;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Remote;

import fr.legrain.autorisations.dossier.dto.TaInfoEntrepriseDTO;
import fr.legrain.autorisations.dossier.model.TaInfoEntreprise;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;

@Remote
public interface ITaInfoEntrepriseServiceRemote extends IGenericCRUDServiceRemote<TaInfoEntreprise,TaInfoEntrepriseDTO>,
														IAbstractLgrDAOServer<TaInfoEntreprise>,IAbstractLgrDAOServerDTO<TaInfoEntrepriseDTO>{
	public static final String validationContext = "INFO_ENTREPRISE";
	public TaInfoEntreprise findInstance();
	public Date dateDansExercice(Date valeur) throws Exception;
}

package fr.legrain.bdg.dossier.service.remote;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dossier.dto.TaInfoEntrepriseDTO;
import fr.legrain.moncompte.dossier.model.TaInfoEntreprise;

@Remote
public interface ITaInfoEntrepriseServiceRemote extends IGenericCRUDServiceRemote<TaInfoEntreprise,TaInfoEntrepriseDTO>,
														IAbstractLgrDAOServer<TaInfoEntreprise>,IAbstractLgrDAOServerDTO<TaInfoEntrepriseDTO>{
	public static final String validationContext = "INFO_ENTREPRISE";
	public TaInfoEntreprise findInstance();
	public Date dateDansExercice(Date valeur) throws Exception;
}

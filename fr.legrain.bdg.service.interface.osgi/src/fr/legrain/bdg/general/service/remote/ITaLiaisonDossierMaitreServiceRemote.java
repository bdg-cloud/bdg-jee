package fr.legrain.bdg.general.service.remote;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.general.dto.TaLiaisonDossierMaitreDTO;
import fr.legrain.general.model.TaLiaisonDossierMaitre;


@Remote
public interface ITaLiaisonDossierMaitreServiceRemote extends IGenericCRUDServiceRemote<TaLiaisonDossierMaitre,TaLiaisonDossierMaitreDTO>,
														IAbstractLgrDAOServer<TaLiaisonDossierMaitre>,IAbstractLgrDAOServerDTO<TaLiaisonDossierMaitreDTO>{
	public static final String validationContext = "LIAISON_DOSSIER_MAITRE";
	
	public TaLiaisonDossierMaitre login(String login, String password) throws EJBException;
	public TaLiaisonDossierMaitreDTO loginDTO(String login, String password);
	
	public List<TaLiaisonDossierMaitreDTO> findAllLight();
	
	public TaLiaisonDossierMaitre findInstance();
	public TaLiaisonDossierMaitre findInstance(String email, String password,  String codeTiers);
}

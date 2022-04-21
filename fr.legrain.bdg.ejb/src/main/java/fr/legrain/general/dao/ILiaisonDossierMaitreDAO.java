package fr.legrain.general.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.general.dto.TaLiaisonDossierMaitreDTO;
import fr.legrain.general.model.TaLiaisonDossierMaitre;
//import javax.ejb.Remote;

//@Remote
public interface ILiaisonDossierMaitreDAO extends IGenericDAO<TaLiaisonDossierMaitre> {
	public TaLiaisonDossierMaitre login(String login, String password);
	public TaLiaisonDossierMaitreDTO loginDTO(String login, String password);
	
	public List<TaLiaisonDossierMaitreDTO> findAllLight();
	public TaLiaisonDossierMaitre findInstance();
	public TaLiaisonDossierMaitre findInstance(String email, String password,  String codeTiers);
}

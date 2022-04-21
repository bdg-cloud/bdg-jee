package fr.legrain.droits.dao;

import java.util.List;

import javax.ejb.EJBException;

import fr.legrain.data.IGenericDAO;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.dto.TaUtilisateurWebServiceDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.model.TaUtilisateurWebService;

//@Remote
public interface IUtilisateurWebServiceDAO extends IGenericDAO<TaUtilisateurWebService> {
	public List<TaUtilisateurWebServiceDTO> findByCodeLight(String code);
	public List<TaUtilisateurWebServiceDTO> findByNomLight(String nom);
	
	public TaUtilisateurWebService login(String login, String password) throws EJBException;
	public TaUtilisateurWebServiceDTO loginDTO(String login, String password);
	
	public void synchroniseCompteUtilisateurDossierEtWebService();
}

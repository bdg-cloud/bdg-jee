package fr.legrain.droits.dao;

import java.util.List;

import javax.ejb.EJBException;

import fr.legrain.data.IGenericDAO;
import fr.legrain.droits.dto.TaUtilisateurDTO;
import fr.legrain.droits.model.TaUtilisateur;

//@Remote
public interface IUtilisateurDAO extends IGenericDAO<TaUtilisateur> {
	public List<TaUtilisateurDTO> findByCodeLight(String code);
	public List<TaUtilisateurDTO> findByNomLight(String nom);
	
	public TaUtilisateur login(String login, String password) throws EJBException;
	public TaUtilisateurDTO loginDTO(String login, String password);
	
	public List<TaUtilisateur> selectAll(Boolean systeme);
}

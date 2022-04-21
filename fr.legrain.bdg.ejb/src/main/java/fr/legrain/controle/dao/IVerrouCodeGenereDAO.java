package fr.legrain.controle.dao;

import java.util.List;

import fr.legrain.controle.model.TaVerrouCodeGenere;
import fr.legrain.data.IGenericDAO;

//@Remote
public interface IVerrouCodeGenereDAO extends IGenericDAO<TaVerrouCodeGenere> {
	public boolean estVerrouille(String entite, String champ, String valeur);
	public TaVerrouCodeGenere findVerrou(String entite, String champ, String valeur);
	public void libereVerrouTout();
	public void libereVerrouTout(List<String> listeSessionID);
	public void libereVerrouTout(String entite, String champ, String sessionID);
}

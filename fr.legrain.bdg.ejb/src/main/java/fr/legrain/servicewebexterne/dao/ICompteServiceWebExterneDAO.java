package fr.legrain.servicewebexterne.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.servicewebexterne.model.TaCompteServiceWebExterne;

//@Remote
public interface ICompteServiceWebExterneDAO extends IGenericDAO<TaCompteServiceWebExterne> {
	//public List<TaAgenda> findAgendaUtilisateur(TaUtilisateur utilisateur);
	
	public void changeDefaut(TaCompteServiceWebExterne detachedInstance);
	
	public TaCompteServiceWebExterne findCompteDefautPourAction(String codeTypeServiceWebExterne); //param TaTServiceWebExterne
	public TaCompteServiceWebExterne findCompteDefautPourAction(String codeTypeServiceWebExterne, boolean compteTest);
	public TaCompteServiceWebExterne findCompteDefautPourType(String codeTypeServiceWebExterne); //param TaTServiceWebExterne
	public TaCompteServiceWebExterne findCompteDefautPourType(String codeTypeServiceWebExterne, boolean compteTest);
	
	public List<TaCompteServiceWebExterne> findListeComptePourAction(String codeTypeServiceWebExterne); //param TaTServiceWebExterne
	public List<TaCompteServiceWebExterne> findListeComptePourAction(String codeTypeServiceWebExterne, boolean compteTest);
	public List<TaCompteServiceWebExterne> findListeComptePourType(String codeTypeServiceWebExterne); //param TaTServiceWebExterne
	public List<TaCompteServiceWebExterne> findListeComptePourType(String codeTypeServiceWebExterne, boolean compteTest);
	
	public TaCompteServiceWebExterne findCompteDefautPourFournisseurService(String codeServiceWebExterne); //param TaServiceWebExterne
	public TaCompteServiceWebExterne findCompteDefautPourFournisseurService(String codeServiceWebExterne, boolean compteTest);
	public List<TaCompteServiceWebExterne> findListeComptePourFournisseurService(String codeServiceWebExterne); //param TaServiceWebExterne
	public List<TaCompteServiceWebExterne> findListeComptePourFournisseurService(String codeServiceWebExterne, boolean compteTest);
}

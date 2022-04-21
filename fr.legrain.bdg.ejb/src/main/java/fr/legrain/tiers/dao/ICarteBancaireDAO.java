package fr.legrain.tiers.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
//import javax.ejb.Remote;
import fr.legrain.tiers.model.TaTiers;

//@Remote
public interface ICarteBancaireDAO extends IGenericDAO<TaCarteBancaire> {
	
	public TaCarteBancaire findByTiersEntreprise();
	public TaCarteBancaire findByTiersEntreprise(TaTPaiement tPaiement);	
	public List<TaCarteBancaire> findByTiersEntrepriseListeComptePrelevement(TaTPaiement tPaiement);
	public List<TaCarteBancaire> selectCompteEntreprise();
	public List<TaCarteBancaire> selectCompteTiers(TaTiers taTiers) ;
	
}

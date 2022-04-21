package fr.legrain.tiers.dao;

import java.util.List;

import fr.legrain.data.IGenericDAO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.model.TaCompteBanque;
//import javax.ejb.Remote;
import fr.legrain.tiers.model.TaTiers;

//@Remote
public interface ICompteBanqueDAO extends IGenericDAO<TaCompteBanque> {
	
	public TaCompteBanque findByTiersEntreprise();
	public TaCompteBanque findByTiersEntreprise(TaTPaiement tPaiement);	
	public List<TaCompteBanque> findByTiersEntrepriseListeComptePrelevement(TaTPaiement tPaiement);
	public List<TaCompteBanque> selectCompteEntreprise();
	public List<TaCompteBanque> selectCompteTiers(TaTiers taTiers) ;
	
}

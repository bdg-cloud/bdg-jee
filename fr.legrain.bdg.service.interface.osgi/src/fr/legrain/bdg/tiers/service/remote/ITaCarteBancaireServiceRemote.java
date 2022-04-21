package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.dto.TaCarteBancaireDTO;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaCarteBancaireServiceRemote extends IGenericCRUDServiceRemote<TaCarteBancaire,TaCarteBancaireDTO>,
														IAbstractLgrDAOServer<TaCarteBancaire>,IAbstractLgrDAOServerDTO<TaCarteBancaireDTO>{
	public static final String validationContext = "CARTE_BANCAIRE";
	public TaCarteBancaire findByTiersEntreprise();
	public TaCarteBancaire findByTiersEntreprise(TaTPaiement tPaiement);	
	public List<TaCarteBancaire> findByTiersEntrepriseListeComptePrelevement(TaTPaiement tPaiement);
	public List<TaCarteBancaire> selectCompteEntreprise();
	public List<TaCarteBancaire> selectCompteTiers(TaTiers taTiers) ;
	
}

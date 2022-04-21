package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.tiers.dto.TaCompteBanqueDTO;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;

@Remote
public interface ITaCompteBanqueServiceRemote extends IGenericCRUDServiceRemote<TaCompteBanque,TaCompteBanqueDTO>,
														IAbstractLgrDAOServer<TaCompteBanque>,IAbstractLgrDAOServerDTO<TaCompteBanqueDTO>{
	public static final String validationContext = "COMPTE_BANQUE";
	public TaCompteBanque findByTiersEntreprise();
	public TaCompteBanque findByTiersEntreprise(TaTPaiement tPaiement);	
	public List<TaCompteBanque> findByTiersEntrepriseListeComptePrelevement(TaTPaiement tPaiement);
	public List<TaCompteBanque> selectCompteEntreprise();
	public List<TaCompteBanque> selectCompteTiers(TaTiers taTiers) ;
	
}

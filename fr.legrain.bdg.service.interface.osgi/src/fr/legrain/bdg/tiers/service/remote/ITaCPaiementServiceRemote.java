package fr.legrain.bdg.tiers.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.model.TaCPaiement;

@Remote
public interface ITaCPaiementServiceRemote extends IGenericCRUDServiceRemote<TaCPaiement,TaCPaiementDTO>,
														IAbstractLgrDAOServer<TaCPaiement>,IAbstractLgrDAOServerDTO<TaCPaiementDTO>{
	public static final String validationContext = "C_PAIEMENT";
	
	public List<TaCPaiementDTO> findAllCPaiementTiers();
	
	public List<TaCPaiementDTO> findAllCPaiementDoc();
	
	public List<TaCPaiement> rechercheParType(String codeType);
}

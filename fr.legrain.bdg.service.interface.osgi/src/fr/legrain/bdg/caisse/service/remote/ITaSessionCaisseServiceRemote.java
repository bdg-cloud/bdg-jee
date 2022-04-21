package fr.legrain.bdg.caisse.service.remote;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.caisse.dto.TaSessionCaisseDTO;
import fr.legrain.caisse.model.TaDepotRetraitCaisse;
import fr.legrain.caisse.model.TaFondDeCaisse;
import fr.legrain.caisse.model.TaSessionCaisse;
import fr.legrain.caisse.model.TaSessionCaisse;

@Remote
public interface ITaSessionCaisseServiceRemote extends IGenericCRUDServiceRemote<TaSessionCaisse,TaSessionCaisseDTO>,
														IAbstractLgrDAOServer<TaSessionCaisse>,IAbstractLgrDAOServerDTO<TaSessionCaisseDTO>{
	public static final String validationContext = "SESSION_CAISSE";
	
	public List<TaSessionCaisseDTO> findByCodeLight(String code);
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public TaSessionCaisse findSessionCaisseActive(int idUtilisateur, String numeroCaisse);
	public TaSessionCaisse demarrerSessionCaisse();
	public TaSessionCaisse demarrerSessionCaisse(TaFondDeCaisse fondDeCaisse);
	public TaSessionCaisse cloturerSessionCaisse();
	public TaSessionCaisse cloturerSessionCaisse(TaFondDeCaisse fondDeCaisse);
	
	public TaSessionCaisse deposerEnCaisse(TaDepotRetraitCaisse depotCaisse);
	public TaSessionCaisse retirerCaisse(TaDepotRetraitCaisse retraitCaisse);
	public TaSessionCaisse ecartCaisse(TaFondDeCaisse ecartCaisse);
	
	public TaSessionCaisse calculMontantSessionCaisse(TaSessionCaisse session);
	public TaSessionCaisse calculMontantSessionCaisse(TaSessionCaisse session, boolean update);
}

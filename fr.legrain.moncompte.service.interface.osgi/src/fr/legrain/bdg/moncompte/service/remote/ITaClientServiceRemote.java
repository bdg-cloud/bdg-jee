package fr.legrain.bdg.moncompte.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.moncompte.dto.TaClientDTO;
import fr.legrain.moncompte.model.TaClient;
import fr.legrain.moncompte.model.TaPartenaire;


@Remote
public interface ITaClientServiceRemote extends IGenericCRUDServiceRemote<TaClient,TaClientDTO>,
														IAbstractLgrDAOServer<TaClient>,IAbstractLgrDAOServerDTO<TaClientDTO>{
	public static final String validationContext = "CLIENT";
	
	public List<TaClient> listeDemandePartenariat();
	public List<TaClient> listePartenaire();
	public List<TaClient> listePartenaireType(int idTypePartenaire);
	public String genereCodePartenaire(TaClient c);
	
	//public String genereCodeClient(TaClient c);
	public String genereCodeClient(String nomClient);
	
	public TaClient findByCodePartenaire(String codePartenaire);
}

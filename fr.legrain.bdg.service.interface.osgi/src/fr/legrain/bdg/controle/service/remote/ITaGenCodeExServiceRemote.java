package fr.legrain.bdg.controle.service.remote;

import java.util.List;

import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.bdg.general.service.remote.ITaGenCodeControlServiceRemote;
import fr.legrain.controle.dto.TaGenCodeExDTO;
import fr.legrain.controle.model.TaGenCodeEx;
import fr.legrain.controle.model.TaVerrouCodeGenere;

@Remote
public interface ITaGenCodeExServiceRemote extends IGenericCRUDServiceRemote<TaGenCodeEx,TaGenCodeExDTO>,
														IAbstractLgrDAOServer<TaGenCodeEx>,IAbstractLgrDAOServerDTO<TaGenCodeExDTO>,ITaGenCodeControlServiceRemote{
	public static final String validationContext = "GEN_CODE_EX";
	
	public String genereCodeExJPA(TaGenCodeEx genCodeEx, int rajoutCompteur, String section, String exo) throws Exception;
	public String genereCodeExJPA(int rajoutCompteur, String section, String exo) throws Exception;
	public void libereVerrouTout();
	public void libereVerrouTout(List<String> listeSessionID);
	public boolean rentreCodeGenere(TaGenCodeEx genCode, String code, String sessionID) throws Exception;
	public void annulerCodeGenere(TaGenCodeEx genCode, String code) throws Exception;
	public void annulerCodeGenere(TaGenCodeEx genCode, String code, boolean tous) throws Exception;
}

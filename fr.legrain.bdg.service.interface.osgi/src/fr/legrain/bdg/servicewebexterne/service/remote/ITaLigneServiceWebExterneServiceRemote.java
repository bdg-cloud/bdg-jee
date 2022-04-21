package fr.legrain.bdg.servicewebexterne.service.remote;

import java.io.File;
import java.util.List;

import javax.ejb.FinderException;
import javax.ejb.Remote;

import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.servicewebexterne.dto.TaLigneServiceWebExterneDTO;
import fr.legrain.servicewebexterne.model.EnteteDocExterne;
import fr.legrain.servicewebexterne.model.TaLiaisonServiceExterne;
import fr.legrain.servicewebexterne.model.TaLigneServiceWebExterne;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;

@Remote
public interface ITaLigneServiceWebExterneServiceRemote extends IGenericCRUDServiceRemote<TaLigneServiceWebExterne,TaLigneServiceWebExterneDTO>,
IAbstractLgrDAOServer<TaLigneServiceWebExterne>,IAbstractLgrDAOServerDTO<TaLigneServiceWebExterneDTO> {
	
	public static final String validationContext = "TA_LIGNE_SERVICE_WEB_EXTERNE";
	public TaLigneServiceWebExterne findByIdAvecTiers(int id) throws FinderException;
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermine();
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermine();
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermineByIdCompteServiceWebExterne(Integer id);
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermineByIdCompteServiceWebExterne(Integer id);
	public List<TaLigneServiceWebExterneDTO> findAllDTOTermineByIdServiceWebExterne(Integer id);
	public List<TaLigneServiceWebExterneDTO> findAllDTONonTermineByIdServiceWebExterne(Integer id);
	
	public TaLigneServiceWebExterne affecteLiaison(TaLigneServiceWebExterne ligne, TaServiceWebExterne service);
	public TaLigneServiceWebExterne affecteLiaison(TaLigneServiceWebExterne ligne, TaServiceWebExterne service, List<TaLiaisonServiceExterne> listeLiaison);
	public TaLigneServiceWebExterne affecteLiaison(TaLigneServiceWebExterne ligne, TaServiceWebExterne service, List<TaLiaisonServiceExterne> listeLiaison, boolean merge);
	
	public List<String> findAllIDExterneByIdCompteService(Integer id);
	
	public File exportToCSV(List<TaLigneServiceWebExterneDTO> liste);

}

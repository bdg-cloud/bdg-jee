package fr.legrain.bdg.article.service.remote;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaFabricationDTO;
import fr.legrain.article.model.TaFabrication;
import fr.legrain.article.model.TaLFabricationMP;
import fr.legrain.article.model.TaLFabricationPF;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceDocumentRemote;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.document.model.TaBonReception;

@Remote
public interface ITaFabricationServiceRemote extends IGenericCRUDServiceDocumentRemote<TaFabrication,TaFabricationDTO>,
														IAbstractLgrDAOServer<TaFabrication>,IAbstractLgrDAOServerDTO<TaFabricationDTO>{
	public static final String validationContext = "FABRICATION";
	
	public List<TaFabricationDTO> findAllLight();
	public List<TaFabricationDTO> findByCodeLight(String code);
	public List<TaFabricationDTO> findByDateLight(Date debut, Date fin);

	public TaFabrication findByCodeWithList(String code);
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	//-ajouté par Dima - Début
	public List<TaFabrication> findWithCodeArticle(String codeArticle, Date dateDebut, Date dateFin);
	public List<TaLFabricationMP> findWithMatPremNumLot(String numLot, Date dateDebut, Date dateFin);
	public List<TaLFabricationMP> findWithMatPremLibelle(String nomProdtuit, Date dateDebut, Date dateFin);
	public List<TaLFabricationPF> findWithProdFinNumLot(String numLot, Date dateDebut, Date dateFin);
	public List<TaLFabricationPF> findWithProdFinLibelle(String nomProdtuit, Date dateDebut, Date dateFin);
	public List<TaFabrication> findWithNumFab(String codeFabrication, Date dateDebut, Date dateFin);
	
	public TaFabrication mergeAndFindById(TaFabrication detachedInstance, String validationContext);
	
	public List<TaFabrication> findWithCodeArticleNoDate(String codeArticle);
	public List<TaFabrication> findWithMatPremNumLotNoDate(String numLot);
	public List<TaFabrication> findWithMatPremLibelleNoDate(String nomProdtuit);
	public List<TaFabrication> findWithProdFinNumLotNoDate(String numLot);
	public List<TaFabrication> findWithProdFinLibelleNoDate(String nomProdtuit);
	public List<TaFabrication> findWithNumFabNoDate(String codeFabrication);
	public TaFabrication findAvecResultatConformites(int idf);
}

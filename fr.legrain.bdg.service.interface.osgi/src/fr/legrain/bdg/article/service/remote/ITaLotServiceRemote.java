package fr.legrain.bdg.article.service.remote;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TracabiliteLot;
import fr.legrain.article.model.TracabiliteMP;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServer;
import fr.legrain.bdg.general.service.remote.IAbstractLgrDAOServerDTO;
import fr.legrain.bdg.general.service.remote.IGenericCRUDServiceRemote;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaResultatCorrection;

@Remote
public interface ITaLotServiceRemote extends IGenericCRUDServiceRemote<TaLot,TaLotDTO>,
														IAbstractLgrDAOServer<TaLot>,IAbstractLgrDAOServerDTO<TaLotDTO>{
	public static final String validationContext = "LOT";
	
	public String genereCode( Map<String, String> params);
	public void annuleCode(String code);
	public void verrouilleCode(String code);
	
	public List<TaLotDTO> findAllLight() ;
	public List<TaLotDTO> findByCodeLight(String code);
	public List<TaLotDTO> selectAllDTOLight();
	
	public TaLot fetchResultatControleLazy(int idLot);
	
	public void changeLotTermine(List<Integer> listeDesIdLotATraite, boolean termine);
	
	public TaLot findOrCreateLotVirtuelArticle(int idArticle);
	public TaLot createLotVirtuelArticleFabOrBR(int idArticle,Map<String, String> params);
	
	public TaLot findByIdAAndIdLot(int idArticle, int idLot) ;
	
	public TaLot findByCodeAAndNumLot(String codeA, String numLot) ;
	public List<TaArticle>  articleEnStock();
	public List<TaLot> lotsArticle(String codeArticle, String codeEntrepot, String emplacement, Boolean termine);
	public List<TaLot>  lotsNonTermine();
	
	public TracabiliteLot findTracaLot(String numLot);
	public TracabiliteMP findTracaMP(String numLot);
	
	/* - Dima - Début - */
//	public List<TaLot> findByNonConforme();
	public List<TaLot> findByNonConformeNoDate();
	public List<TaResultatCorrection> findByNonConformeCorrection();
	/* - Dima -  Fin  - */
	public List<TaLotDTO> selectAllInFabrication();
	public List<TaLotDTO> selectAllPFInFabrication();
	
	public List<TaLotDTO> selectAllInReception();	
	
	public String validationLot(TaLot lot);
	
	public List<TaLotDTO> findLotNonConforme(Date debut, Date fin);
	public List<TaLotDTO> findLot(Date debut, Date fin, Boolean presenceDeNonConformite);
	public List<TaLotDTO> findLotAvecQte(Date debut, Date fin, Boolean presenceDeNonConformite);
	public TaLot findAvecResultatConformite(String numLot);
	public boolean controle(TaLot lot);
	public String retourControl(TaLot lot);
	public TaLot initListeResultatControle(List<TaConformite> l, TaLot lot);
	public void suppression_lot_non_utilise();
	public TaLot findLotVirtuelArticle(int idArticle);
	
}

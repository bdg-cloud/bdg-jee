package fr.legrain.article.dao;

import java.util.Date;
import java.util.List;

import fr.legrain.article.dto.TaLotDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.model.TaLot;
import fr.legrain.article.model.TracabiliteLot;
import fr.legrain.article.model.TracabiliteMP;
import fr.legrain.conformite.model.TaConformite;
import fr.legrain.conformite.model.TaResultatCorrection;
import fr.legrain.data.IGenericDAO;

public interface ILotDAO extends IGenericDAO<TaLot> {
	public TaLot findByIdAAndIdLot(Integer idArticle, Integer idLot);
	public TaLot findByCodeAAndNumLot(String codeA, String numLot) ;
	public List<Object[]> findAllObjectLight();
	public List<TaLot> lotsArticle(String codeArticle, String codeEntrepot, String emplacement, Boolean termine);
	public List<TaLot>  lotsNonTermine();
	public List<TaArticle>  articleEnStock();
	public List<TaLotDTO> findAllLight() ;
	public List<TaLotDTO> findByCodeLight(String code);
	
	public TaLot findLotVirtuelArticle(int idArticle);
	
	public TracabiliteLot findTracaLot(String numLot);
	public TracabiliteMP findTracaMP(String numLot);
	
	public TaLot fetchResultatControleLazy(int idLot);
	
	public void changeLotTermine(List<Integer> listeDesIdLotATraite, boolean termine);
	
	/* - Dima - Début - */
//	public List<TaLot> findByNonConforme();
	public List<TaLot> findByNonConformeNoDate();
	public List<TaResultatCorrection> findByNonConformeCorrection();
	
	
	public List<TaLotDTO> selectAllInFabrication();
	public List<TaLotDTO> selectAllPFInFabrication();
	
	public List<TaLotDTO> selectAllInReception();	
	/* - Dima -  Fin  - */
	
	public List<TaLotDTO> findLot(Date debut, Date fin, Boolean presenceDeNonConformite);
	public List<TaLotDTO> findLotNonConforme(Date debut, Date fin);
	public List<TaLotDTO> findLotAvecQte(Date debut, Date fin, Boolean presenceDeNonConformite);
	
	public TaLot findAvecResultatConformite(String numLot);
	public TaLot initListeResultatControle(List<TaConformite> l, TaLot lot);
	public String retourControl(TaLot lot);
	public boolean controle(TaLot lot);
	public void suppression_lot_non_utilise();
	
}

package fr.legrain.article.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.article.dao.IArticleDAO;
import fr.legrain.article.dao.IMouvementDAO;
import fr.legrain.article.dto.ArticleLotEntrepotDTO;
import fr.legrain.article.model.TaArticle;
import fr.legrain.article.titretransport.model.TaEtatStockCapsules;
import fr.legrain.bdg.article.service.remote.ITaMouvementStockServiceRemote;
import fr.legrain.bdg.general.service.remote.BusinessValidationException;
import fr.legrain.bdg.model.mapping.mapper.TaMouvementStockMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.dms.model.TaEtatMouvementDms;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.stock.dto.MouvementStocksDTO;
import fr.legrain.stock.model.TaMouvementStock;


/**
 * Session Bean implementation class TaMouvementStockBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaMouvementStockService extends AbstractApplicationDAOServer<TaMouvementStock, MouvementStocksDTO> implements ITaMouvementStockServiceRemote {

	static Logger logger = Logger.getLogger(TaMouvementStockService.class);

	@Inject private IMouvementDAO dao;
	@Inject private IArticleDAO daoArticle;

	/**
	 * Default constructor. 
	 */
	public TaMouvementStockService() {
		super(TaMouvementStock.class,MouvementStocksDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaMouvementStock a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaMouvementStock transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaMouvementStock transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaMouvementStock persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdMouvementStock()));
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public TaMouvementStock merge(TaMouvementStock detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaMouvementStock merge(TaMouvementStock detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaMouvementStock findById(int id) throws FinderException {
		return dao.findById(id);
	}


	public TaMouvementStock findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaMouvementStock> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<MouvementStocksDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<MouvementStocksDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaMouvementStock> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<MouvementStocksDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public MouvementStocksDTO entityToDTO(TaMouvementStock entity) {
//		MouvementStocksDTO dto = new MouvementStocksDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaMouvementStockMapper mapper = new TaMouvementStockMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<MouvementStocksDTO> listEntityToListDTO(List<TaMouvementStock> entity) {
		List<MouvementStocksDTO> l = new ArrayList<MouvementStocksDTO>();

		for (TaMouvementStock taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<MouvementStocksDTO> selectAllDTO() {
		System.out.println("List of MouvementStocksDTO EJB :");
		ArrayList<MouvementStocksDTO> liste = new ArrayList<MouvementStocksDTO>();

		List<TaMouvementStock> projects = selectAll();
		for(TaMouvementStock project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public MouvementStocksDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public MouvementStocksDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(MouvementStocksDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(MouvementStocksDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(MouvementStocksDTO dto, String validationContext) throws EJBException {
		try {
			TaMouvementStockMapper mapper = new TaMouvementStockMapper();
			TaMouvementStock entity = null;
			if(dto.getId()!=null) {
				entity = dao.findById(dto.getId());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
				} else {
					 entity = mapper.mapDtoToEntity(dto,entity);
				}
			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(MouvementStocksDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(MouvementStocksDTO dto, String validationContext) throws CreateException {
		try {
			TaMouvementStockMapper mapper = new TaMouvementStockMapper();
			TaMouvementStock entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(MouvementStocksDTO dto) throws RemoveException {
		try {
			TaMouvementStockMapper mapper = new TaMouvementStockMapper();
			TaMouvementStock entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}


	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaMouvementStock value, String validationContext) /*throws ExceptLgr*/ {
		try {
			if(validationContext==null) validationContext="";
			validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
			//dao.validate(value); //validation automatique via la JSR bean validation
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaMouvementStock value, String propertyName, String validationContext) {
		try {
			if(validationContext==null) validationContext="";
			validate(value, propertyName, validationContext);
			//dao.validateField(value,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOValidationContext")
	public void validateDTO(MouvementStocksDTO dto, String validationContext) {
		try {
			validateDTOAll(dto, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(MouvementStocksDTO dto, String propertyName, String validationContext)throws BusinessValidationException {
		try {
			if(validationContext==null) validationContext="";
			validateDTO(dto, propertyName, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
//			throw new EJBException(e.getMessage());
			throw new BusinessValidationException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(MouvementStocksDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(MouvementStocksDTO dto, String propertyName) throws BusinessValidationException {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaMouvementStock value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaMouvementStock value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	protected TaMouvementStock refresh(TaMouvementStock persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

//	public List<ArticleLotEntrepotDTO> etatStock(){
//		boolean trouve = false;
//		//a.taLotArticle.taArticle.idArticle, a.taLotArticle.taLot.id, , a.entrepotOrg.id, a.emplacementOrg, a.entrepotDest.id, a.emplacementDest, a.qte1
//		List<Object[]> list = dao.getEtatStock();
//		if(list != null){
//			List<ArticleLotEntrepotDTO> listDTO = new ArrayList<ArticleLotEntrepotDTO>();
//			for(Object[] o : list){
//				if ((Integer) o[4] != null && (Integer) o[4] != 0){
//					//mouvement avec entrepot de destination
//					
//					ArticleLotEntrepotDTO dto = new ArticleLotEntrepotDTO();
//					for(int i = 0; i< listDTO.size(); i++){
//						dto = listDTO.get(i);
//						//si la combinaison article lot entrepot emplacement existe déja
//						if (dto.getIdEntrepot() == o[4] && dto.getEmplacement() == o[5]){
//							trouve = true;
//							dto.setQte1(dto.getQte1() + (Float)o[6]);
//						}
//					}
//					// sinon on la crée
//					if(!trouve){
//						dto.setIdArticle((Integer) o[0]);
//						dto.setIdEntrepot((Integer) o[4]);
//						dto.setIdLot((Integer) o[1]);
//						dto.setQte1((Float) o[6]);
//						dto.setEmplacement((String) o[5]);
//						listDTO.add(dto);
//					}
//					//mouvement avec entrepot d'origine
//					for(int i = 0; i< listDTO.size(); i++){
//						dto = listDTO.get(i);
//						//si la combinaison article lot entrepot emplacement existe déja
//						if (dto.getIdEntrepot() == o[2] && dto.getEmplacement() == o[3]){
//							trouve = true;
//							dto.setQte1(dto.getQte1() - (Float)o[6]);
//						}
//					}
//					// sinon on la crée
//					if(!trouve){
//						dto.setIdArticle((Integer) o[0]);
//						dto.setIdEntrepot((Integer) o[2]);
//						dto.setIdLot((Integer) o[1]);
//						dto.setQte1(- (Float) o[6]);
//						dto.setEmplacement((String) o[3]);
//						listDTO.add(dto);
//					}
//				}
//				
//			}
//			return listDTO;
//		}
//		else{
//			return null;
//		}
//			
//	}
	public List<ArticleLotEntrepotDTO>  getEtatStockByArticle(String codeArticle, Boolean utiliserLotNonConforme){
		
		//a.taLotArticle.taArticle.idArticle, a.taLotArticle.taLot.id, , a.entrepotOrg.id, a.emplacementOrg, a.entrepotDest.id, a.emplacementDest, a.qte1
		TaArticle art=daoArticle.findByCode(codeArticle);
		if(art.getGestionLot())
			return dao.getEtatStockByArticle(codeArticle, utiliserLotNonConforme);
		else 
			return dao.getEtatStockByArticleVirtuel(codeArticle, utiliserLotNonConforme);
		
		
	}
	public List<String>  articleEnStock() {
		return dao.articleEnStock();
	}
	
	public List<String>  articleEnStock(Boolean mPremiere,Boolean pFini) {
		return dao.articleEnStock(mPremiere,pFini);
	}
	
	public List<String>   lotEnStock(){
		return dao.lotEnStock();
	}
	public List<String>   entrepotAvecStock() {
		return dao.entrepotAvecStock();
	}
	public List<String>  lotEnStockParArticle(String codeArticle){
		return dao.lotEnStockParArticle(codeArticle);
	}
	public List<String>  entrepotAvecStockParArticleEtLot(String codeArticle, String numLot) {
		return dao.entrepotAvecStockParArticleEtLot(codeArticle, numLot);
	}
	public List<String>  emplacementParArticleLotEtEntrepot(String codeArticle, String numLot, String codeEntrepot){
		return dao.emplacementParArticleLotEtEntrepot(codeArticle, numLot, codeEntrepot);
	}
	public List<String>  emplacementParEntrepot( String codeEntrepot){
		return dao.emplacementParEntrepot( codeEntrepot);
	}
	
	public List<ArticleLotEntrepotDTO>  getQuantiteTotaleEnStockArticle(String codeArticle, Boolean utiliserLotNonConforme) {
		return dao.getQuantiteTotaleEnStockArticle(codeArticle,utiliserLotNonConforme);
	}
//
//	@Override
//	public List<ArticleLotEntrepotDTO> etatStock() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	/* - Dima - Début - */
	public List<TaMouvementStock> findByCodeListe(String codeLot){
		return dao.findByCodeListe(codeLot);
	}

	@Override
	public List<MouvementStocksDTO> etatStocksEntrepotEmplacement(Date dateD, Date dateF) {
		// TODO Auto-generated method stub
		return dao.etatStocksEntrepotEmplacement(dateD, dateF);
	}

	@Override
	public List<MouvementStocksDTO> etatStocksEntrepotEmplacement(Date dateD, Date dateF, String codeEntrepot,
			String emplacement,boolean parFamille,boolean afficherLesTermine) {
		// TODO Auto-generated method stub
		return dao.etatStocksEntrepotEmplacement(dateD, dateF, codeEntrepot, emplacement,parFamille,afficherLesTermine);
	}

	@Override
	public Integer getNBEtatStockByArticleVirtuel(TaArticle article, Boolean utiliserLotNonConforme) {
		// TODO Auto-generated method stub
		return dao.getNBEtatStockByArticleVirtuel(article, utiliserLotNonConforme);
	}

	@Override
	public List<ArticleLotEntrepotDTO> getEtatStockByArticleVirtuel(String codeArticle,
			Boolean utiliserLotNonConforme) {
		// TODO Auto-generated method stub
		return dao.getEtatStockByArticleVirtuel(codeArticle, utiliserLotNonConforme);
	}

	@Override
	public List<TaEtatMouvementDms> selectSumMouvementEntreesSortiesDms(TaEtatMouvementDms criteres) {
		// TODO Auto-generated method stub
		return dao.selectSumMouvementEntreesSortiesDms(criteres);
	}

	@Override
	public List<TaEtatMouvementDms> selectSumSyntheseMouvEntreesSortiesDms(TaEtatMouvementDms criteres) {
		// TODO Auto-generated method stub
		return dao.selectSumSyntheseMouvEntreesSortiesDms(criteres);
	}

	@Override
	public List<TaEtatStockCapsules> selectSumMouvEntreesCRD(TaEtatStockCapsules criteres, Date dateDeb,
			boolean strict) {
		// TODO Auto-generated method stub
		return dao.selectSumMouvEntreesCRD(criteres, dateDeb, strict);
	}

	@Override
	public List<TaEtatStockCapsules> selectSumMouvSortiesCRD(TaEtatStockCapsules criteres, Date dateDeb,
			boolean strict) {
		// TODO Auto-generated method stub
		return dao.selectSumMouvSortiesCRD(criteres, dateDeb, strict);
	}



//	public List<TaMouvementStock> findAllWithDates(Date dateDebut, Date dateFin){
//		return dao.findAllWithDates(dateDebut, dateFin);
//	}
	/* - Dima -  Fin  - */
//	public List<MouvementStocksDTO> findByCodeLight(String code) {
//		return dao.findByCodeLight(code);
//	}
//	
//	public List<MouvementStocksDTO> findAllLight() {
//		return dao.findAllLight();
//	}
}

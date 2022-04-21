package fr.legrain.article.service;

import java.util.ArrayList;
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

import fr.legrain.article.dao.IEtatStockDAO;
import fr.legrain.article.model.TaEntrepot;
import fr.legrain.bdg.article.service.remote.ITaEtatStockServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaEtatStockMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.stock.dto.EtatStocksDTO;
import fr.legrain.stock.model.TaEtatStock;


/**
 * Session Bean implementation class TaEtatStockBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaEtatStockService extends AbstractApplicationDAOServer<TaEtatStock, EtatStocksDTO> implements ITaEtatStockServiceRemote {

	static Logger logger = Logger.getLogger(TaEtatStockService.class);

	@Inject private IEtatStockDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaEtatStockService() {
		super(TaEtatStock.class,EtatStocksDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaEtatStock a";
	
	public List<String> emplacementLotArticle(String codeArticle, String codeEntrepot) {
		return dao.emplacementLotArticle(codeArticle,codeEntrepot);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaEtatStock transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaEtatStock transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaEtatStock persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdEtatStock()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaEtatStock merge(TaEtatStock detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaEtatStock merge(TaEtatStock detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaEtatStock findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaEtatStock findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaEtatStock> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<EtatStocksDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<EtatStocksDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaEtatStock> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<EtatStocksDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public EtatStocksDTO entityToDTO(TaEtatStock entity) {
//		EtatStocksDTO dto = new EtatStocksDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaEtatStockMapper mapper = new TaEtatStockMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<EtatStocksDTO> listEntityToListDTO(List<TaEtatStock> entity) {
		List<EtatStocksDTO> l = new ArrayList<EtatStocksDTO>();

		for (TaEtatStock taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<EtatStocksDTO> selectAllDTO() {
		System.out.println("List of EtatStocksDTO EJB :");
		ArrayList<EtatStocksDTO> liste = new ArrayList<EtatStocksDTO>();

		List<TaEtatStock> projects = selectAll();
		for(TaEtatStock project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public EtatStocksDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public EtatStocksDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(EtatStocksDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(EtatStocksDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(EtatStocksDTO dto, String validationContext) throws EJBException {
		try {
			TaEtatStockMapper mapper = new TaEtatStockMapper();
			TaEtatStock entity = null;
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
	
	public void persistDTO(EtatStocksDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(EtatStocksDTO dto, String validationContext) throws CreateException {
		try {
			TaEtatStockMapper mapper = new TaEtatStockMapper();
			TaEtatStock entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(EtatStocksDTO dto) throws RemoveException {
		try {
			TaEtatStockMapper mapper = new TaEtatStockMapper();
			TaEtatStock entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaEtatStock refresh(TaEtatStock persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaEtatStock value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaEtatStock value, String propertyName, String validationContext) {
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
	public void validateDTO(EtatStocksDTO dto, String validationContext) {
		try {
			TaEtatStockMapper mapper = new TaEtatStockMapper();
			TaEtatStock entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<EtatStocksDTO> validator = new BeanValidator<EtatStocksDTO>(EtatStocksDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(EtatStocksDTO dto, String propertyName, String validationContext) {
		try {
			TaEtatStockMapper mapper = new TaEtatStockMapper();
			TaEtatStock entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<EtatStocksDTO> validator = new BeanValidator<EtatStocksDTO>(EtatStocksDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(EtatStocksDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(EtatStocksDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaEtatStock value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaEtatStock value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<String> emplacementLotArticle(String codeArticle,
			String numLot, String codeEntrepot, Boolean termine) {
		return dao.emplacementLotArticle(codeArticle, numLot, codeEntrepot, termine);
	}

	@Override
	public List<String> emplacementEntrepot(String codeEntrepot, Boolean termine) {
		return dao.emplacementEntrepot(codeEntrepot, termine);
	}

	@Override
	public List<String> emplacementEntrepot(String codeEntrepot) {
		return dao.emplacementEntrepot(codeEntrepot);
	}

	@Override
	public List<TaEntrepot> entrepotLotArticle(String codeArticle,
			String numLot, String emplacement, Boolean termine) {
		return dao.entrepotLotArticle(codeArticle, numLot, emplacement, termine);
	}

	@Override
	public Integer qteArticleEnStock(Integer idArticle) {
		return dao.qteArticleEnStock(idArticle);
	}

	@Override
	public Integer qteArticleEnStock(Integer idArticle, Integer idEntrepot) {
		return dao.qteArticleEnStock(idArticle, idEntrepot);
	}

	@Override
	public Integer qteArticleEnStock(Integer idArticle, Integer idEntrepot,
			String emplacement) {
		return dao.qteArticleEnStock(idArticle, idEntrepot, emplacement);
	}

	@Override
	public Integer qteArticleLotEnStock(Integer idLot) {
		return dao.qteArticleLotEnStock(idLot);
	}

	@Override
	public Integer qteArticleLotEnStock(Integer idLot, Integer idEntrepot) {
		return dao.qteArticleLotEnStock(idLot, idEntrepot);
	}

	@Override
	public Integer qteArticleLotEnStock(Integer idLot, Integer idEntrepot,
			String emplacement) {
		return dao.qteArticleLotEnStock(idLot, idEntrepot, emplacement);
	}
	
	public Integer supprimeEtatStockLot(Integer idLot) {
		return dao.supprimeEtatStockLot(idLot);
	}

	@Override
	public List<TaEtatStock> qteLotentrepotStock(String idEntrepot,
			String numLot) {
		// TODO Auto-generated method stub
		return dao.qteLotentrepotStock( idEntrepot, numLot);
	}

	@Override
	public List<TaEtatStock> qteLotentrepotStock(String idEntrepot,
			String numLot, Boolean termine) {
		// TODO Auto-generated method stub
		return dao.qteLotentrepotStock( idEntrepot, numLot,termine);
	}

	@Override
	public void recalculEtatStock() {
		// TODO Auto-generated method stub
		 dao.recalculEtatStock();
	}

	@Override
	public List<EtatStocksDTO> findByCodeLight(String code) {
		// TODO Auto-generated method stub
		return dao.findByCodeLight(code);
	}

	@Override
	public List<EtatStocksDTO> findAllLight() {
		// TODO Auto-generated method stub
		return dao.findAllLight();
	}
	
	public List<EtatStocksDTO> findAllLight(Boolean termine) {
		return dao.findAllLight(termine);
	}

	@Override
	public List<EtatStocksDTO> qteLotentrepotStockLight(String idEntrepot,
			String numLot, Boolean termine) {
		// TODO Auto-generated method stub
		return dao.qteLotentrepotStockLight(idEntrepot, numLot, termine);
		
	}

	@Override
	public List<EtatStocksDTO> qteLotentrepotStockLight(String idEntrepot,
			String numLot) {
		// TODO Auto-generated method stub
		return dao.qteLotentrepotStockLight(idEntrepot, numLot);
	}

}

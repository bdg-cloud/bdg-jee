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

import fr.legrain.article.dao.IConditionnementArticleDAO;
import fr.legrain.article.dto.TaConditionnementArticleDTO;
import fr.legrain.article.model.TaConditionnementArticle;
import fr.legrain.bdg.article.service.remote.ITaConditionnementArticleServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaConditionnementArticleMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaConditionnementArticleBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaConditionnementArticleService extends AbstractApplicationDAOServer<TaConditionnementArticle, TaConditionnementArticleDTO> implements ITaConditionnementArticleServiceRemote {

	static Logger logger = Logger.getLogger(TaConditionnementArticleService.class);

	@Inject private IConditionnementArticleDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaConditionnementArticleService() {
		super(TaConditionnementArticle.class,TaConditionnementArticleDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaConditionnementArticle a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaConditionnementArticle transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaConditionnementArticle transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaConditionnementArticle persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdConditionnementArticle()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaConditionnementArticle merge(TaConditionnementArticle detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaConditionnementArticle merge(TaConditionnementArticle detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaConditionnementArticle findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaConditionnementArticle findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaConditionnementArticle> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaConditionnementArticleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaConditionnementArticleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaConditionnementArticle> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaConditionnementArticleDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaConditionnementArticleDTO entityToDTO(TaConditionnementArticle entity) {
//		TaConditionnementArticleDTO dto = new TaConditionnementArticleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaConditionnementArticleMapper mapper = new TaConditionnementArticleMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaConditionnementArticleDTO> listEntityToListDTO(List<TaConditionnementArticle> entity) {
		List<TaConditionnementArticleDTO> l = new ArrayList<TaConditionnementArticleDTO>();

		for (TaConditionnementArticle taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaConditionnementArticleDTO> selectAllDTO() {
		System.out.println("List of TaConditionnementArticleDTO EJB :");
		ArrayList<TaConditionnementArticleDTO> liste = new ArrayList<TaConditionnementArticleDTO>();

		List<TaConditionnementArticle> projects = selectAll();
		for(TaConditionnementArticle project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaConditionnementArticleDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaConditionnementArticleDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaConditionnementArticleDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaConditionnementArticleDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaConditionnementArticleDTO dto, String validationContext) throws EJBException {
		try {
			TaConditionnementArticleMapper mapper = new TaConditionnementArticleMapper();
			TaConditionnementArticle entity = null;
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
	
	public void persistDTO(TaConditionnementArticleDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaConditionnementArticleDTO dto, String validationContext) throws CreateException {
		try {
			TaConditionnementArticleMapper mapper = new TaConditionnementArticleMapper();
			TaConditionnementArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaConditionnementArticleDTO dto) throws RemoveException {
		try {
			TaConditionnementArticleMapper mapper = new TaConditionnementArticleMapper();
			TaConditionnementArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaConditionnementArticle refresh(TaConditionnementArticle persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaConditionnementArticle value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaConditionnementArticle value, String propertyName, String validationContext) {
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
	public void validateDTO(TaConditionnementArticleDTO dto, String validationContext) {
		try {
			TaConditionnementArticleMapper mapper = new TaConditionnementArticleMapper();
			TaConditionnementArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaConditionnementArticleDTO> validator = new BeanValidator<TaConditionnementArticleDTO>(TaConditionnementArticleDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaConditionnementArticleDTO dto, String propertyName, String validationContext) {
		try {
			TaConditionnementArticleMapper mapper = new TaConditionnementArticleMapper();
			TaConditionnementArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaConditionnementArticleDTO> validator = new BeanValidator<TaConditionnementArticleDTO>(TaConditionnementArticleDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaConditionnementArticleDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaConditionnementArticleDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaConditionnementArticle value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaConditionnementArticle value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

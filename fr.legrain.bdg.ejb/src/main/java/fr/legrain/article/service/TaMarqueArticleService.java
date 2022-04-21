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

import fr.legrain.article.dao.IMarqueArticleDAO;
import fr.legrain.article.dto.TaMarqueArticleDTO;
import fr.legrain.article.model.TaMarqueArticle;
import fr.legrain.bdg.article.service.remote.ITaMarqueArticleServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaMarqueArticleMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaMarqueArticleBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaMarqueArticleService extends AbstractApplicationDAOServer<TaMarqueArticle, TaMarqueArticleDTO> implements ITaMarqueArticleServiceRemote {

	static Logger logger = Logger.getLogger(TaMarqueArticleService.class);

	@Inject private IMarqueArticleDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaMarqueArticleService() {
		super(TaMarqueArticle.class,TaMarqueArticleDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaMarqueArticle a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaMarqueArticle transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaMarqueArticle transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaMarqueArticle persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdMarqueArticle()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaMarqueArticle merge(TaMarqueArticle detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaMarqueArticle merge(TaMarqueArticle detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaMarqueArticle findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaMarqueArticle findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaMarqueArticle> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaMarqueArticleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaMarqueArticleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaMarqueArticle> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaMarqueArticleDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaMarqueArticleDTO entityToDTO(TaMarqueArticle entity) {
//		TaMarqueArticleDTO dto = new TaMarqueArticleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaMarqueArticleMapper mapper = new TaMarqueArticleMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaMarqueArticleDTO> listEntityToListDTO(List<TaMarqueArticle> entity) {
		List<TaMarqueArticleDTO> l = new ArrayList<TaMarqueArticleDTO>();

		for (TaMarqueArticle taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaMarqueArticleDTO> selectAllDTO() {
		System.out.println("List of TaMarqueArticleDTO EJB :");
		ArrayList<TaMarqueArticleDTO> liste = new ArrayList<TaMarqueArticleDTO>();

		List<TaMarqueArticle> projects = selectAll();
		for(TaMarqueArticle project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaMarqueArticleDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaMarqueArticleDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaMarqueArticleDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaMarqueArticleDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaMarqueArticleDTO dto, String validationContext) throws EJBException {
		try {
			TaMarqueArticleMapper mapper = new TaMarqueArticleMapper();
			TaMarqueArticle entity = null;
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
	
	public void persistDTO(TaMarqueArticleDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaMarqueArticleDTO dto, String validationContext) throws CreateException {
		try {
			TaMarqueArticleMapper mapper = new TaMarqueArticleMapper();
			TaMarqueArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaMarqueArticleDTO dto) throws RemoveException {
		try {
			TaMarqueArticleMapper mapper = new TaMarqueArticleMapper();
			TaMarqueArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaMarqueArticle refresh(TaMarqueArticle persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaMarqueArticle value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaMarqueArticle value, String propertyName, String validationContext) {
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
	public void validateDTO(TaMarqueArticleDTO dto, String validationContext) {
		try {
			TaMarqueArticleMapper mapper = new TaMarqueArticleMapper();
			TaMarqueArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaMarqueArticleDTO> validator = new BeanValidator<TaMarqueArticleDTO>(TaMarqueArticleDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaMarqueArticleDTO dto, String propertyName, String validationContext) {
		try {
			TaMarqueArticleMapper mapper = new TaMarqueArticleMapper();
			TaMarqueArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaMarqueArticleDTO> validator = new BeanValidator<TaMarqueArticleDTO>(TaMarqueArticleDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaMarqueArticleDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaMarqueArticleDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaMarqueArticle value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaMarqueArticle value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<TaMarqueArticleDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}

}

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

import fr.legrain.article.dao.ITNoteArticleDAO;
import fr.legrain.article.dto.TaTNoteArticleDTO;
import fr.legrain.article.model.TaTNoteArticle;
import fr.legrain.bdg.article.service.remote.ITaTNoteArticleServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTNoteArticleMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTNoteArticleBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTNoteArticleService extends AbstractApplicationDAOServer<TaTNoteArticle, TaTNoteArticleDTO> implements ITaTNoteArticleServiceRemote {

	static Logger logger = Logger.getLogger(TaTNoteArticleService.class);

	@Inject private ITNoteArticleDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTNoteArticleService() {
		super(TaTNoteArticle.class,TaTNoteArticleDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTNoteArticle a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTNoteArticle transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTNoteArticle transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTNoteArticle persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTNoteArticle()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTNoteArticle merge(TaTNoteArticle detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTNoteArticle merge(TaTNoteArticle detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTNoteArticle findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTNoteArticle findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTNoteArticle> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTNoteArticleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTNoteArticleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTNoteArticle> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTNoteArticleDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTNoteArticleDTO entityToDTO(TaTNoteArticle entity) {
//		TaTNoteArticleDTO dto = new TaTNoteArticleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTNoteArticleMapper mapper = new TaTNoteArticleMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTNoteArticleDTO> listEntityToListDTO(List<TaTNoteArticle> entity) {
		List<TaTNoteArticleDTO> l = new ArrayList<TaTNoteArticleDTO>();

		for (TaTNoteArticle taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTNoteArticleDTO> selectAllDTO() {
		System.out.println("List of TaTNoteArticleDTO EJB :");
		ArrayList<TaTNoteArticleDTO> liste = new ArrayList<TaTNoteArticleDTO>();

		List<TaTNoteArticle> projects = selectAll();
		for(TaTNoteArticle project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTNoteArticleDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTNoteArticleDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTNoteArticleDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTNoteArticleDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTNoteArticleDTO dto, String validationContext) throws EJBException {
		try {
			TaTNoteArticleMapper mapper = new TaTNoteArticleMapper();
			TaTNoteArticle entity = null;
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
	
	public void persistDTO(TaTNoteArticleDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTNoteArticleDTO dto, String validationContext) throws CreateException {
		try {
			TaTNoteArticleMapper mapper = new TaTNoteArticleMapper();
			TaTNoteArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTNoteArticleDTO dto) throws RemoveException {
		try {
			TaTNoteArticleMapper mapper = new TaTNoteArticleMapper();
			TaTNoteArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTNoteArticle refresh(TaTNoteArticle persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTNoteArticle value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTNoteArticle value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTNoteArticleDTO dto, String validationContext) {
		try {
			TaTNoteArticleMapper mapper = new TaTNoteArticleMapper();
			TaTNoteArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTNoteArticleDTO> validator = new BeanValidator<TaTNoteArticleDTO>(TaTNoteArticleDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTNoteArticleDTO dto, String propertyName, String validationContext) {
		try {
			TaTNoteArticleMapper mapper = new TaTNoteArticleMapper();
			TaTNoteArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTNoteArticleDTO> validator = new BeanValidator<TaTNoteArticleDTO>(TaTNoteArticleDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTNoteArticleDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTNoteArticleDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTNoteArticle value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTNoteArticle value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

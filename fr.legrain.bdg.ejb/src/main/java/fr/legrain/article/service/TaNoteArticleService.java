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

import fr.legrain.article.dao.INoteArticleDAO;
import fr.legrain.article.dto.TaNoteArticleDTO;
import fr.legrain.article.model.TaNoteArticle;
import fr.legrain.bdg.article.service.remote.ITaNoteArticleServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaNoteArticleMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaNoteArticleBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaNoteArticleService extends AbstractApplicationDAOServer<TaNoteArticle, TaNoteArticleDTO> implements ITaNoteArticleServiceRemote {

	static Logger logger = Logger.getLogger(TaNoteArticleService.class);

	@Inject private INoteArticleDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaNoteArticleService() {
		super(TaNoteArticle.class,TaNoteArticleDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaNoteArticle a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaNoteArticle transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaNoteArticle transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaNoteArticle persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdNoteArticle()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaNoteArticle merge(TaNoteArticle detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaNoteArticle merge(TaNoteArticle detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaNoteArticle findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaNoteArticle findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaNoteArticle> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaNoteArticleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaNoteArticleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaNoteArticle> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaNoteArticleDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaNoteArticleDTO entityToDTO(TaNoteArticle entity) {
//		TaNoteArticleDTO dto = new TaNoteArticleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaNoteArticleMapper mapper = new TaNoteArticleMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaNoteArticleDTO> listEntityToListDTO(List<TaNoteArticle> entity) {
		List<TaNoteArticleDTO> l = new ArrayList<TaNoteArticleDTO>();

		for (TaNoteArticle taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaNoteArticleDTO> selectAllDTO() {
		System.out.println("List of TaNoteArticleDTO EJB :");
		ArrayList<TaNoteArticleDTO> liste = new ArrayList<TaNoteArticleDTO>();

		List<TaNoteArticle> projects = selectAll();
		for(TaNoteArticle project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaNoteArticleDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaNoteArticleDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaNoteArticleDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaNoteArticleDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaNoteArticleDTO dto, String validationContext) throws EJBException {
		try {
			TaNoteArticleMapper mapper = new TaNoteArticleMapper();
			TaNoteArticle entity = null;
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
	
	public void persistDTO(TaNoteArticleDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaNoteArticleDTO dto, String validationContext) throws CreateException {
		try {
			TaNoteArticleMapper mapper = new TaNoteArticleMapper();
			TaNoteArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaNoteArticleDTO dto) throws RemoveException {
		try {
			TaNoteArticleMapper mapper = new TaNoteArticleMapper();
			TaNoteArticle entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaNoteArticle refresh(TaNoteArticle persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaNoteArticle value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaNoteArticle value, String propertyName, String validationContext) {
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
	public void validateDTO(TaNoteArticleDTO dto, String validationContext) {
		try {
			TaNoteArticleMapper mapper = new TaNoteArticleMapper();
			TaNoteArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaNoteArticleDTO> validator = new BeanValidator<TaNoteArticleDTO>(TaNoteArticleDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaNoteArticleDTO dto, String propertyName, String validationContext) {
		try {
			TaNoteArticleMapper mapper = new TaNoteArticleMapper();
			TaNoteArticle entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaNoteArticleDTO> validator = new BeanValidator<TaNoteArticleDTO>(TaNoteArticleDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaNoteArticleDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaNoteArticleDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaNoteArticle value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaNoteArticle value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

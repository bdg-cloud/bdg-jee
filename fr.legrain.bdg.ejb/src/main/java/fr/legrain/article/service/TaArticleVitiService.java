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

import fr.legrain.article.dao.IArticleVitiDAO;
import fr.legrain.article.dto.TaArticleDTO;
import fr.legrain.article.model.TaArticleViti;
import fr.legrain.bdg.article.service.remote.ITaArticleVitiServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaArticleVitiMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaArticleVitiBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaArticleVitiService extends AbstractApplicationDAOServer<TaArticleViti, TaArticleDTO> implements ITaArticleVitiServiceRemote {

	static Logger logger = Logger.getLogger(TaArticleVitiService.class);

	@Inject private IArticleVitiDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaArticleVitiService() {
		super(TaArticleViti.class,TaArticleDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaArticleViti a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaArticleViti transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaArticleViti transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaArticleViti persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdArticle()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaArticleViti merge(TaArticleViti detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaArticleViti merge(TaArticleViti detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaArticleViti findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaArticleViti findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaArticleViti> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaArticleDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaArticleDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaArticleViti> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaArticleDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaArticleDTO entityToDTO(TaArticleViti entity) {
//		TaArticleDTO dto = new TaArticleDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaArticleVitiMapper mapper = new TaArticleVitiMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaArticleDTO> listEntityToListDTO(List<TaArticleViti> entity) {
		List<TaArticleDTO> l = new ArrayList<TaArticleDTO>();

		for (TaArticleViti taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaArticleDTO> selectAllDTO() {
		System.out.println("List of TaArticleDTO EJB :");
		ArrayList<TaArticleDTO> liste = new ArrayList<TaArticleDTO>();

		List<TaArticleViti> projects = selectAll();
		for(TaArticleViti project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaArticleDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaArticleDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaArticleDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaArticleDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaArticleDTO dto, String validationContext) throws EJBException {
		try {
			TaArticleVitiMapper mapper = new TaArticleVitiMapper();
			TaArticleViti entity = null;
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
	
	public void persistDTO(TaArticleDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaArticleDTO dto, String validationContext) throws CreateException {
		try {
			TaArticleVitiMapper mapper = new TaArticleVitiMapper();
			TaArticleViti entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaArticleDTO dto) throws RemoveException {
		try {
			TaArticleVitiMapper mapper = new TaArticleVitiMapper();
			TaArticleViti entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaArticleViti refresh(TaArticleViti persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaArticleViti value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaArticleViti value, String propertyName, String validationContext) {
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
	public void validateDTO(TaArticleDTO dto, String validationContext) {
		try {
			TaArticleVitiMapper mapper = new TaArticleVitiMapper();
			TaArticleViti entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaArticleDTO> validator = new BeanValidator<TaArticleDTO>(TaArticleDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaArticleDTO dto, String propertyName, String validationContext) {
		try {
			TaArticleVitiMapper mapper = new TaArticleVitiMapper();
			TaArticleViti entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaArticleDTO> validator = new BeanValidator<TaArticleDTO>(TaArticleDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaArticleDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaArticleDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaArticleViti value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaArticleViti value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

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

import fr.legrain.article.dao.ICodeBarreDAO;
import fr.legrain.article.dto.TaCodeBarreDTO;
import fr.legrain.article.model.TaCodeBarre;
import fr.legrain.bdg.article.service.remote.ITaCodeBarreServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaCodeBarreMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaCodeBarreBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaCodeBarreService extends AbstractApplicationDAOServer<TaCodeBarre, TaCodeBarreDTO> implements ITaCodeBarreServiceRemote {

	static Logger logger = Logger.getLogger(TaCodeBarreService.class);

	@Inject private ICodeBarreDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaCodeBarreService() {
		super(TaCodeBarre.class,TaCodeBarreDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCodeBarre a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCodeBarre transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttribute.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCodeBarre transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCodeBarre persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCodeBarre merge(TaCodeBarre detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCodeBarre merge(TaCodeBarre detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCodeBarre findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCodeBarre findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCodeBarre> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCodeBarreDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCodeBarreDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCodeBarre> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCodeBarreDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCodeBarreDTO entityToDTO(TaCodeBarre entity) {
//		TaCodeBarreDTO dto = new TaCodeBarreDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCodeBarreMapper mapper = new TaCodeBarreMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCodeBarreDTO> listEntityToListDTO(List<TaCodeBarre> entity) {
		List<TaCodeBarreDTO> l = new ArrayList<TaCodeBarreDTO>();

		for (TaCodeBarre ta : entity) {
			l.add(entityToDTO(ta));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCodeBarreDTO> selectAllDTO() {
		System.out.println("List of TaCodeBarreDTO EJB :");
		ArrayList<TaCodeBarreDTO> liste = new ArrayList<TaCodeBarreDTO>();

		List<TaCodeBarre> projects = selectAll();
		for(TaCodeBarre project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCodeBarreDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCodeBarreDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCodeBarreDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCodeBarreDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCodeBarreDTO dto, String validationContext) throws EJBException {
		try {
			TaCodeBarreMapper mapper = new TaCodeBarreMapper();
			TaCodeBarre entity = null;
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
	
	public void persistDTO(TaCodeBarreDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCodeBarreDTO dto, String validationContext) throws CreateException {
		try {
			TaCodeBarreMapper mapper = new TaCodeBarreMapper();
			TaCodeBarre entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCodeBarreDTO dto) throws RemoveException {
		try {
			TaCodeBarreMapper mapper = new TaCodeBarreMapper();
			TaCodeBarre entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCodeBarre refresh(TaCodeBarre persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCodeBarre value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCodeBarre value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCodeBarreDTO dto, String validationContext) {
		try {
			TaCodeBarreMapper mapper = new TaCodeBarreMapper();
			TaCodeBarre entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCodeBarreDTO> validator = new BeanValidator<TaCodeBarreDTO>(TaCodeBarreDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCodeBarreDTO dto, String propertyName, String validationContext) {
		try {
			TaCodeBarreMapper mapper = new TaCodeBarreMapper();
			TaCodeBarre entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCodeBarreDTO> validator = new BeanValidator<TaCodeBarreDTO>(TaCodeBarreDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCodeBarreDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCodeBarreDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCodeBarre value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCodeBarre value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

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

import fr.legrain.article.dao.ITypeCodeBarreDAO;
import fr.legrain.article.dto.TaTypeCodeBarreDTO;
import fr.legrain.article.model.TaTypeCodeBarre;
import fr.legrain.bdg.article.service.remote.ITypeCodeBarreServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTypeCodeBarreMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTypeCodeBarreBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTypeCodeBarreService extends AbstractApplicationDAOServer<TaTypeCodeBarre, TaTypeCodeBarreDTO> implements ITypeCodeBarreServiceRemote {

	static Logger logger = Logger.getLogger(TaTypeCodeBarreService.class);

	@Inject private ITypeCodeBarreDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTypeCodeBarreService() {
		super(TaTypeCodeBarre.class,TaTypeCodeBarreDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTypeCodeBarre a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTypeCodeBarre transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTypeCodeBarre transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTypeCodeBarre persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTypeCodeBarre()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTypeCodeBarre merge(TaTypeCodeBarre detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTypeCodeBarre merge(TaTypeCodeBarre detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTypeCodeBarre findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTypeCodeBarre findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTypeCodeBarre> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTypeCodeBarreDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTypeCodeBarreDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTypeCodeBarre> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTypeCodeBarreDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTypeCodeBarreDTO entityToDTO(TaTypeCodeBarre entity) {
//		TaTypeCodeBarreDTO dto = new TaTypeCodeBarreDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTypeCodeBarreMapper mapper = new TaTypeCodeBarreMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTypeCodeBarreDTO> listEntityToListDTO(List<TaTypeCodeBarre> entity) {
		List<TaTypeCodeBarreDTO> l = new ArrayList<TaTypeCodeBarreDTO>();

		for (TaTypeCodeBarre taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTypeCodeBarreDTO> selectAllDTO() {
		System.out.println("List of TaTypeCodeBarreDTO EJB :");
		ArrayList<TaTypeCodeBarreDTO> liste = new ArrayList<TaTypeCodeBarreDTO>();

		List<TaTypeCodeBarre> projects = selectAll();
		for(TaTypeCodeBarre project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTypeCodeBarreDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTypeCodeBarreDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTypeCodeBarreDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTypeCodeBarreDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTypeCodeBarreDTO dto, String validationContext) throws EJBException {
		try {
			TaTypeCodeBarreMapper mapper = new TaTypeCodeBarreMapper();
			TaTypeCodeBarre entity = null;
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
	
	public void persistDTO(TaTypeCodeBarreDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTypeCodeBarreDTO dto, String validationContext) throws CreateException {
		try {
			TaTypeCodeBarreMapper mapper = new TaTypeCodeBarreMapper();
			TaTypeCodeBarre entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTypeCodeBarreDTO dto) throws RemoveException {
		try {
			TaTypeCodeBarreMapper mapper = new TaTypeCodeBarreMapper();
			TaTypeCodeBarre entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTypeCodeBarre refresh(TaTypeCodeBarre persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTypeCodeBarre value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTypeCodeBarre value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTypeCodeBarreDTO dto, String validationContext) {
		try {
			TaTypeCodeBarreMapper mapper = new TaTypeCodeBarreMapper();
			TaTypeCodeBarre entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeCodeBarreDTO> validator = new BeanValidator<TaTypeCodeBarreDTO>(TaTypeCodeBarreDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTypeCodeBarreDTO dto, String propertyName, String validationContext) {
		try {
			TaTypeCodeBarreMapper mapper = new TaTypeCodeBarreMapper();
			TaTypeCodeBarre entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeCodeBarreDTO> validator = new BeanValidator<TaTypeCodeBarreDTO>(TaTypeCodeBarreDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTypeCodeBarreDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTypeCodeBarreDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTypeCodeBarre value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTypeCodeBarre value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

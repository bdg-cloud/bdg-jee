package fr.legrain.tache.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaTypeNotificationMapper;
import fr.legrain.bdg.tache.service.remote.ITaTypeNotificationServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tache.dao.ITypeNotificationDAO;
import fr.legrain.tache.dto.TaTypeNotificationDTO;
import fr.legrain.tache.model.TaTypeNotification;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTypeNotificationService extends AbstractApplicationDAOServer<TaTypeNotification, TaTypeNotificationDTO> implements ITaTypeNotificationServiceRemote {

	static Logger logger = Logger.getLogger(TaTypeNotificationService.class);

	@Inject private ITypeNotificationDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTypeNotificationService() {
		super(TaTypeNotification.class,TaTypeNotificationDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTypeNotification a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTypeNotification transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTypeNotification transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTypeNotification persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTypeNotification()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTypeNotification merge(TaTypeNotification detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTypeNotification merge(TaTypeNotification detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTypeNotification findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTypeNotification findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTypeNotification> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTypeNotificationDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTypeNotificationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTypeNotification> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTypeNotificationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTypeNotificationDTO entityToDTO(TaTypeNotification entity) {
//		TaTypeNotificationDTO dto = new TaTypeNotificationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTypeNotificationMapper mapper = new TaTypeNotificationMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTypeNotificationDTO> listEntityToListDTO(List<TaTypeNotification> entity) {
		List<TaTypeNotificationDTO> l = new ArrayList<TaTypeNotificationDTO>();

		for (TaTypeNotification taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTypeNotificationDTO> selectAllDTO() {
		System.out.println("List of TaTypeNotificationDTO EJB :");
		ArrayList<TaTypeNotificationDTO> liste = new ArrayList<TaTypeNotificationDTO>();

		List<TaTypeNotification> projects = selectAll();
		for(TaTypeNotification project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTypeNotificationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTypeNotificationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTypeNotificationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTypeNotificationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTypeNotificationDTO dto, String validationContext) throws EJBException {
		try {
			TaTypeNotificationMapper mapper = new TaTypeNotificationMapper();
			TaTypeNotification entity = null;
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
	
	public void persistDTO(TaTypeNotificationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTypeNotificationDTO dto, String validationContext) throws CreateException {
		try {
			TaTypeNotificationMapper mapper = new TaTypeNotificationMapper();
			TaTypeNotification entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTypeNotificationDTO dto) throws RemoveException {
		try {
			TaTypeNotificationMapper mapper = new TaTypeNotificationMapper();
			TaTypeNotification entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTypeNotification refresh(TaTypeNotification persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTypeNotification value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTypeNotification value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTypeNotificationDTO dto, String validationContext) {
		try {
			TaTypeNotificationMapper mapper = new TaTypeNotificationMapper();
			TaTypeNotification entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeNotificationDTO> validator = new BeanValidator<TaTypeNotificationDTO>(TaTypeNotificationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTypeNotificationDTO dto, String propertyName, String validationContext) {
		try {
			TaTypeNotificationMapper mapper = new TaTypeNotificationMapper();
			TaTypeNotification entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeNotificationDTO> validator = new BeanValidator<TaTypeNotificationDTO>(TaTypeNotificationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTypeNotificationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTypeNotificationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTypeNotification value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTypeNotification value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

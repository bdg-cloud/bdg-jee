package fr.legrain.tache.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.model.mapping.mapper.TaNotificationMapper;
import fr.legrain.bdg.tache.service.remote.ITaNotificationServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tache.dao.INotificationDAO;
import fr.legrain.tache.dto.TaNotificationDTO;
import fr.legrain.tache.model.TaNotification;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaNotificationService extends AbstractApplicationDAOServer<TaNotification, TaNotificationDTO> implements ITaNotificationServiceRemote {

	static Logger logger = Logger.getLogger(TaNotificationService.class);

	@Inject private INotificationDAO dao;
	private @EJB TimerProgEvenementService timerProgEvenementService;

	/**
	 * Default constructor. 
	 */
	public TaNotificationService() {
		super(TaNotification.class,TaNotificationDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaNotification a";
	
	public List<TaNotification> findNotificationInWebAppEnvoyee(Integer idCalendrier, Integer utilisateur, Boolean lu) {
		return dao.findNotificationInWebAppEnvoyee(idCalendrier, utilisateur,lu);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaNotification transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaNotification transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaNotification persistentInstance) throws RemoveException {
		try {
//			TimerHandle h = (TimerHandle) SerializationUtils.deserialize(persistentInstance.getTimerHandle());
//			try {
//				Timer t = h.getTimer();
//				t.cancel();
//			} catch (NoSuchObjectLocalException ex) {
//				System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
//			}
			dao.remove(findById(persistentInstance.getIdNotification()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaNotification merge(TaNotification detachedInstance) {
//		if(detachedInstance.getTimerHandle()==null) {
//			detachedInstance.setTimerHandle(SerializationUtils.serialize((Serializable) timerProgEvenementService.creerTimer(detachedInstance)));
//			System.out.println("EvenementAgendaController.actEnregistrerEvenement()");
//		}
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaNotification merge(TaNotification detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		return dao.merge(detachedInstance);
	}

	public TaNotification findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaNotification findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaNotification> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaNotificationDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaNotificationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaNotification> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaNotificationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaNotificationDTO entityToDTO(TaNotification entity) {
//		TaNotificationDTO dto = new TaNotificationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaNotificationMapper mapper = new TaNotificationMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaNotificationDTO> listEntityToListDTO(List<TaNotification> entity) {
		List<TaNotificationDTO> l = new ArrayList<TaNotificationDTO>();

		for (TaNotification taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaNotificationDTO> selectAllDTO() {
		System.out.println("List of TaNotificationDTO EJB :");
		ArrayList<TaNotificationDTO> liste = new ArrayList<TaNotificationDTO>();

		List<TaNotification> projects = selectAll();
		for(TaNotification project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaNotificationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaNotificationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaNotificationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaNotificationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaNotificationDTO dto, String validationContext) throws EJBException {
		try {
			TaNotificationMapper mapper = new TaNotificationMapper();
			TaNotification entity = null;
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
	
	public void persistDTO(TaNotificationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaNotificationDTO dto, String validationContext) throws CreateException {
		try {
			TaNotificationMapper mapper = new TaNotificationMapper();
			TaNotification entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaNotificationDTO dto) throws RemoveException {
		try {
			TaNotificationMapper mapper = new TaNotificationMapper();
			TaNotification entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaNotification refresh(TaNotification persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaNotification value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaNotification value, String propertyName, String validationContext) {
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
	public void validateDTO(TaNotificationDTO dto, String validationContext) {
		try {
			TaNotificationMapper mapper = new TaNotificationMapper();
			TaNotification entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaNotificationDTO> validator = new BeanValidator<TaNotificationDTO>(TaNotificationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaNotificationDTO dto, String propertyName, String validationContext) {
		try {
			TaNotificationMapper mapper = new TaNotificationMapper();
			TaNotification entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaNotificationDTO> validator = new BeanValidator<TaNotificationDTO>(TaNotificationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaNotificationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaNotificationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaNotification value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaNotification value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

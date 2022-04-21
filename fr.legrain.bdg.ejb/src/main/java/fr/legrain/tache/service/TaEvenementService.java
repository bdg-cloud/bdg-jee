package fr.legrain.tache.service;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.RemoveException;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.model.mapping.mapper.TaEvenementMapper;
import fr.legrain.bdg.tache.service.remote.ITaEvenementServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tache.dao.IEvenementDAO;
import fr.legrain.tache.dto.TaEvenementDTO;
import fr.legrain.tache.model.TaAgenda;
import fr.legrain.tache.model.TaEvenement;
import fr.legrain.tache.model.TaNotification;
import fr.legrain.tache.model.TaRDocumentEvenement;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaEvenementService extends AbstractApplicationDAOServer<TaEvenement, TaEvenementDTO> implements ITaEvenementServiceRemote {

	static Logger logger = Logger.getLogger(TaEvenementService.class);

	@Inject private IEvenementDAO dao;
	private @EJB TimerProgEvenementService timerProgEvenementService;
	

	/**
	 * Default constructor. 
	 */
	public TaEvenementService() {
		super(TaEvenement.class,TaEvenementDTO.class);
	}
	
	public List<TaEvenement> findByDate(Date debut, Date fin, List<TaAgenda> listeAgenda) {	
		return dao.findByDate(debut, fin, listeAgenda);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaEvenement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaEvenement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaEvenement persistentInstance) throws RemoveException {
		try {
//			if(persistentInstance.getTaNotificationUtilisateur()!=null) {
//				for (TaNotification n : persistentInstance.getTaNotificationUtilisateur()) {
//					TimerHandle h = (TimerHandle) SerializationUtils.deserialize(n.getTimerHandle());
//					try {
//						Timer t = h.getTimer();
//						t.cancel();
//					} catch (NoSuchObjectLocalException ex) {
//						System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
//					}
//				}
//			}
//			if(persistentInstance.getTaNotificationTiers()!=null) {
//				for (TaNotification n : persistentInstance.getTaNotificationTiers()) {
//					TimerHandle h = (TimerHandle) SerializationUtils.deserialize(n.getTimerHandle());
//					try {
//						Timer t = h.getTimer();
//						t.cancel();
//					} catch (NoSuchObjectLocalException ex) {
//						System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
//					}
//				}
//			}
			dao.remove(findById(persistentInstance.getIdEvenement()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
		
	public TaEvenement merge(TaEvenement detachedInstance) {
		detachedInstance = merge(detachedInstance, null);
		
		//Création des timers de notification, après un  premier merge pour que les infos des timers possède bien l'ID des notifcations et des evenment
		if(detachedInstance.getTaNotificationUtilisateur()!=null) {
			for (TaNotification n : detachedInstance.getTaNotificationUtilisateur()) {
				if(n.getTimerHandle()!=null) {
					//un timer existe deja pour cette notification, on l'annule et on le recréer
					TimerHandle h = (TimerHandle) SerializationUtils.deserialize(n.getTimerHandle());
					try {
						Timer t = h.getTimer();
						t.cancel();
					} catch (NoSuchObjectLocalException ex) {
						System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
					}
				}
				n.setTimerHandle(SerializationUtils.serialize((Serializable) timerProgEvenementService.creerTimer(n)));
			}
		} else {
			detachedInstance.setTaNotificationUtilisateur(new HashSet<>());
		}
		
		if(detachedInstance.getTaNotificationTiers()!=null) {
			for (TaNotification n : detachedInstance.getTaNotificationTiers()) {
				if(n.getTimerHandle()!=null) {
					//un timer existe deja pour cette notification, on l'annule et on le recréer
					TimerHandle h = (TimerHandle) SerializationUtils.deserialize(n.getTimerHandle());
					try {
						Timer t = h.getTimer();
						t.cancel();
					} catch (NoSuchObjectLocalException ex) {
						System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
					}
				}
				n.setTimerHandle(SerializationUtils.serialize((Serializable) timerProgEvenementService.creerTimer(n)));
			}
		} else {
			detachedInstance.setTaNotificationTiers(new HashSet<>());
		}
		
		if(detachedInstance.getListeDocument()==null) {
			detachedInstance.setListeDocument(new ArrayList<>());
		}
		
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaEvenement merge(TaEvenement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaEvenement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaEvenement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaEvenement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaEvenementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaEvenementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaEvenement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaEvenementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaEvenementDTO entityToDTO(TaEvenement entity) {
//		TaEvenementDTO dto = new TaEvenementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaEvenementMapper mapper = new TaEvenementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaEvenementDTO> listEntityToListDTO(List<TaEvenement> entity) {
		List<TaEvenementDTO> l = new ArrayList<TaEvenementDTO>();

		for (TaEvenement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaEvenementDTO> selectAllDTO() {
		System.out.println("List of TaEvenementDTO EJB :");
		ArrayList<TaEvenementDTO> liste = new ArrayList<TaEvenementDTO>();

		List<TaEvenement> projects = selectAll();
		for(TaEvenement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaEvenementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaEvenementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaEvenementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaEvenementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaEvenementDTO dto, String validationContext) throws EJBException {
		try {
			TaEvenementMapper mapper = new TaEvenementMapper();
			TaEvenement entity = null;
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
	
	public void persistDTO(TaEvenementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaEvenementDTO dto, String validationContext) throws CreateException {
		try {
			TaEvenementMapper mapper = new TaEvenementMapper();
			TaEvenement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaEvenementDTO dto) throws RemoveException {
		try {
			TaEvenementMapper mapper = new TaEvenementMapper();
			TaEvenement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaEvenement refresh(TaEvenement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaEvenement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaEvenement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaEvenementDTO dto, String validationContext) {
		try {
			TaEvenementMapper mapper = new TaEvenementMapper();
			TaEvenement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEvenementDTO> validator = new BeanValidator<TaEvenementDTO>(TaEvenementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaEvenementDTO dto, String propertyName, String validationContext) {
		try {
			TaEvenementMapper mapper = new TaEvenementMapper();
			TaEvenement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaEvenementDTO> validator = new BeanValidator<TaEvenementDTO>(TaEvenementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaEvenementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaEvenementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaEvenement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaEvenement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public List<TaRDocumentEvenement> findListeDocuments(TaEvenement event) {
		// TODO Auto-generated method stub
		return dao.findListeDocuments(event);
	}

}

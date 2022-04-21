package fr.legrain.sms.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaMessageSMSMapper;
import fr.legrain.bdg.sms.service.remote.ITaMessageSMSServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.email.dao.IMessageEmailDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.sms.dao.IMessageSMSDAO;
import fr.legrain.sms.dto.TaMessageSMSDTO;
import fr.legrain.sms.model.TaMessageSMS;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaMessageSMSService extends AbstractApplicationDAOServer<TaMessageSMS, TaMessageSMSDTO> implements ITaMessageSMSServiceRemote {

	static Logger logger = Logger.getLogger(TaMessageSMSService.class);

	@Inject private IMessageSMSDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaMessageSMSService() {
		super(TaMessageSMS.class,TaMessageSMSDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaMessageSMS a";
	
	public List<TaMessageSMSDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaMessageSMS> selectAll(int idTiers) {
		return dao.selectAll(idTiers);
	}
	
	public List<TaMessageSMSDTO> selectAllDTO(int idTiers) {
		System.out.println("List of TaMessageSMSDTO EJB :");
		ArrayList<TaMessageSMSDTO> liste = new ArrayList<TaMessageSMSDTO>();

		List<TaMessageSMS> projects = selectAll(idTiers);
		for(TaMessageSMS project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaMessageSMSDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaMessageSMSDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaMessageSMS transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaMessageSMS transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaMessageSMS persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdSms()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaMessageSMS merge(TaMessageSMS detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaMessageSMS merge(TaMessageSMS detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaMessageSMS findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaMessageSMS findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaMessageSMS> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaMessageSMSDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaMessageSMSDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaMessageSMS> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaMessageSMSDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaMessageSMSDTO entityToDTO(TaMessageSMS entity) {
//		TaMessageSMSDTO dto = new TaMessageSMSDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaMessageSMSMapper mapper = new TaMessageSMSMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaMessageSMSDTO> listEntityToListDTO(List<TaMessageSMS> entity) {
		List<TaMessageSMSDTO> l = new ArrayList<TaMessageSMSDTO>();

		for (TaMessageSMS taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaMessageSMSDTO> selectAllDTO() {
		System.out.println("List of TaMessageSMSDTO EJB :");
		ArrayList<TaMessageSMSDTO> liste = new ArrayList<TaMessageSMSDTO>();

		List<TaMessageSMS> projects = selectAll();
		for(TaMessageSMS project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaMessageSMSDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaMessageSMSDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaMessageSMSDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaMessageSMSDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaMessageSMSDTO dto, String validationContext) throws EJBException {
		try {
			TaMessageSMSMapper mapper = new TaMessageSMSMapper();
			TaMessageSMS entity = null;
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
	
	public void persistDTO(TaMessageSMSDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaMessageSMSDTO dto, String validationContext) throws CreateException {
		try {
			TaMessageSMSMapper mapper = new TaMessageSMSMapper();
			TaMessageSMS entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaMessageSMSDTO dto) throws RemoveException {
		try {
			TaMessageSMSMapper mapper = new TaMessageSMSMapper();
			TaMessageSMS entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaMessageSMS refresh(TaMessageSMS persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaMessageSMS value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaMessageSMS value, String propertyName, String validationContext) {
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
	public void validateDTO(TaMessageSMSDTO dto, String validationContext) {
		try {
			TaMessageSMSMapper mapper = new TaMessageSMSMapper();
			TaMessageSMS entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaMessageSMSDTO> validator = new BeanValidator<TaMessageSMSDTO>(TaMessageSMSDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaMessageSMSDTO dto, String propertyName, String validationContext) {
		try {
			TaMessageSMSMapper mapper = new TaMessageSMSMapper();
			TaMessageSMS entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaMessageSMSDTO> validator = new BeanValidator<TaMessageSMSDTO>(TaMessageSMSDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaMessageSMSDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaMessageSMSDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaMessageSMS value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaMessageSMS value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}

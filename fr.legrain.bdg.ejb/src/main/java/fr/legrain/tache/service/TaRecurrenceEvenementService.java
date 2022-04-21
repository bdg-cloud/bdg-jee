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

import fr.legrain.bdg.model.mapping.mapper.TaRecurrenceEvenementMapper;
import fr.legrain.bdg.tache.service.remote.ITaRecurrenceEvenementServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tache.dao.IRecurrenceEvenementDAO;
import fr.legrain.tache.dto.TaRecurrenceEvenementDTO;
import fr.legrain.tache.model.TaRecurrenceEvenement;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRecurrenceEvenementService extends AbstractApplicationDAOServer<TaRecurrenceEvenement, TaRecurrenceEvenementDTO> implements ITaRecurrenceEvenementServiceRemote {

	static Logger logger = Logger.getLogger(TaRecurrenceEvenementService.class);

	@Inject private IRecurrenceEvenementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRecurrenceEvenementService() {
		super(TaRecurrenceEvenement.class,TaRecurrenceEvenementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaRecurrenceEvenement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaRecurrenceEvenement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRecurrenceEvenement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRecurrenceEvenement persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdRecurrence()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaRecurrenceEvenement merge(TaRecurrenceEvenement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRecurrenceEvenement merge(TaRecurrenceEvenement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRecurrenceEvenement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRecurrenceEvenement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaRecurrenceEvenement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRecurrenceEvenementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRecurrenceEvenementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRecurrenceEvenement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRecurrenceEvenementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRecurrenceEvenementDTO entityToDTO(TaRecurrenceEvenement entity) {
//		TaRecurrenceEvenementDTO dto = new TaRecurrenceEvenementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaRecurrenceEvenementMapper mapper = new TaRecurrenceEvenementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaRecurrenceEvenementDTO> listEntityToListDTO(List<TaRecurrenceEvenement> entity) {
		List<TaRecurrenceEvenementDTO> l = new ArrayList<TaRecurrenceEvenementDTO>();

		for (TaRecurrenceEvenement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRecurrenceEvenementDTO> selectAllDTO() {
		System.out.println("List of TaRecurrenceEvenementDTO EJB :");
		ArrayList<TaRecurrenceEvenementDTO> liste = new ArrayList<TaRecurrenceEvenementDTO>();

		List<TaRecurrenceEvenement> projects = selectAll();
		for(TaRecurrenceEvenement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRecurrenceEvenementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRecurrenceEvenementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaRecurrenceEvenementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRecurrenceEvenementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRecurrenceEvenementDTO dto, String validationContext) throws EJBException {
		try {
			TaRecurrenceEvenementMapper mapper = new TaRecurrenceEvenementMapper();
			TaRecurrenceEvenement entity = null;
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
	
	public void persistDTO(TaRecurrenceEvenementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRecurrenceEvenementDTO dto, String validationContext) throws CreateException {
		try {
			TaRecurrenceEvenementMapper mapper = new TaRecurrenceEvenementMapper();
			TaRecurrenceEvenement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRecurrenceEvenementDTO dto) throws RemoveException {
		try {
			TaRecurrenceEvenementMapper mapper = new TaRecurrenceEvenementMapper();
			TaRecurrenceEvenement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRecurrenceEvenement refresh(TaRecurrenceEvenement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRecurrenceEvenement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRecurrenceEvenement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRecurrenceEvenementDTO dto, String validationContext) {
		try {
			TaRecurrenceEvenementMapper mapper = new TaRecurrenceEvenementMapper();
			TaRecurrenceEvenement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRecurrenceEvenementDTO> validator = new BeanValidator<TaRecurrenceEvenementDTO>(TaRecurrenceEvenementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRecurrenceEvenementDTO dto, String propertyName, String validationContext) {
		try {
			TaRecurrenceEvenementMapper mapper = new TaRecurrenceEvenementMapper();
			TaRecurrenceEvenement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRecurrenceEvenementDTO> validator = new BeanValidator<TaRecurrenceEvenementDTO>(TaRecurrenceEvenementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRecurrenceEvenementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRecurrenceEvenementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRecurrenceEvenement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRecurrenceEvenement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

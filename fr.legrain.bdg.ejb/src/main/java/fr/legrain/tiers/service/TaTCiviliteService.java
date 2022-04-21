package fr.legrain.tiers.service;

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
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.model.mapping.mapper.TaTCiviliteMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCiviliteServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITCiviliteDAO;
import fr.legrain.tiers.dto.TaTCiviliteDTO;
import fr.legrain.tiers.model.TaTCivilite;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;

/**
 * Session Bean implementation class TaTCiviliteBean
 */
//@Stateless(name="taTCiviliteBean")
@Stateless
//@Remote(TaTCiviliteBeanRemote.class)
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
//public class TaTCiviliteService extends AbstractLgrDAOServer<TaTCivilite> implements ITaTCiviliteServiceRemote {
public class TaTCiviliteService extends AbstractApplicationDAOServer<TaTCivilite, TaTCiviliteDTO> implements ITaTCiviliteServiceRemote {

	static Logger logger = Logger.getLogger(TaTCiviliteService.class);

	@Inject private ITCiviliteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTCiviliteService() {
		super(TaTCivilite.class,TaTCiviliteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTCivilite a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTCivilite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTCivilite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
//transientInstance.setCodeTCivilite(transientInstance.getCodeTCivilite().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTCivilite persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTCivilite()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTCivilite merge(TaTCivilite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTCivilite merge(TaTCivilite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		//detachedInstance.setCodeTCivilite(detachedInstance.getCodeTCivilite().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTCivilite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTCivilite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTCivilite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTCiviliteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTCiviliteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTCivilite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTCiviliteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTCiviliteDTO entityToDTO(TaTCivilite entity) {
//		TaTCiviliteDTO dto = new TaTCiviliteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTCiviliteMapper mapper = new TaTCiviliteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTCiviliteDTO> listEntityToListDTO(List<TaTCivilite> entity) {
		List<TaTCiviliteDTO> l = new ArrayList<TaTCiviliteDTO>();

		for (TaTCivilite taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTCiviliteDTO> selectAllDTO() {
		System.out.println("List of TaTCiviliteDTO EJB :");
		ArrayList<TaTCiviliteDTO> liste = new ArrayList<TaTCiviliteDTO>();

		List<TaTCivilite> projects = selectAll();
		for(TaTCivilite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTCiviliteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTCiviliteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTCiviliteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTCiviliteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTCiviliteDTO dto, String validationContext) throws EJBException {
		try {
			TaTCiviliteMapper mapper = new TaTCiviliteMapper();
			TaTCivilite entity = null;
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
	
	public void persistDTO(TaTCiviliteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTCiviliteDTO dto, String validationContext) throws CreateException {
		try {
			TaTCiviliteMapper mapper = new TaTCiviliteMapper();
			TaTCivilite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTCiviliteDTO dto) throws RemoveException {
		try {
			TaTCiviliteMapper mapper = new TaTCiviliteMapper();
			TaTCivilite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTCivilite refresh(TaTCivilite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTCivilite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTCivilite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTCiviliteDTO dto, String validationContext) {
		try {
			TaTCiviliteMapper mapper = new TaTCiviliteMapper();
			TaTCivilite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTCiviliteDTO> validator = new BeanValidator<TaTCiviliteDTO>(TaTCiviliteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTCiviliteDTO dto, String propertyName, String validationContext) {
		try {
			TaTCiviliteMapper mapper = new TaTCiviliteMapper();
			TaTCivilite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTCiviliteDTO> validator = new BeanValidator<TaTCiviliteDTO>(TaTCiviliteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTCiviliteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTCiviliteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTCivilite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTCivilite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

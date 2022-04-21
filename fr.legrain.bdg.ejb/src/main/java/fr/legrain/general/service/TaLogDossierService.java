package fr.legrain.general.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.legrain.bdg.general.service.remote.ITaLogDossierServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.general.dao.ITaLogDossierDAO;
import fr.legrain.general.dto.TaLogDossierDTO;
import fr.legrain.general.mapper.TaLogDossierMapper;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import fr.legrain.general.model.TaLogDossier;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLogDossierService extends AbstractApplicationDAOServer<TaLogDossier, TaLogDossierDTO> implements ITaLogDossierServiceRemote {

	static Logger logger = Logger.getLogger(TaLogDossierService.class);

	@Inject private ITaLogDossierDAO dao;
	
	@Context
	private HttpServletRequest servletRequest;

	/**
	 * Default constructor. 
	 */
	public TaLogDossierService() {
		super(TaLogDossier.class,TaLogDossierDTO.class);
	}
	
	public TaLogDossier findByUUID(String uuid) {
		return dao.findByUUID(uuid);
	}
	


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLogDossier transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLogDossier transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
//		transientInstance.setCodeFichier(transientInstance.getCodeFichier().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaLogDossier persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLogDossier merge(TaLogDossier detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLogDossier merge(TaLogDossier detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
//		detachedInstance.setCodeFichier(detachedInstance.getCodeFichier().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaLogDossier findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLogDossier findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLogDossier> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLogDossierDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLogDossierDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLogDossier> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLogDossierDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLogDossierDTO entityToDTO(TaLogDossier entity) {
		TaLogDossierMapper mapper = new TaLogDossierMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLogDossierDTO> listEntityToListDTO(List<TaLogDossier> entity) {
		List<TaLogDossierDTO> l = new ArrayList<TaLogDossierDTO>();

		for (TaLogDossier TaLogDossier : entity) {
			l.add(entityToDTO(TaLogDossier));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLogDossierDTO> selectAllDTO() {
		System.out.println("List of TaLogDossierDTO EJB :");
		ArrayList<TaLogDossierDTO> liste = new ArrayList<TaLogDossierDTO>();

		List<TaLogDossier> projects = selectAll();
		for(TaLogDossier project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLogDossierDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLogDossierDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLogDossierDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLogDossierDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLogDossierDTO dto, String validationContext) throws EJBException {
		try {
			TaLogDossierMapper mapper = new TaLogDossierMapper();
			TaLogDossier entity = null;
//			if(dto.getId()!=null) {
//				entity = dao.findById(dto.getId());
//				if(dto.getVersionObj()!=entity.getVersionObj()) {
//					throw new OptimisticLockException(entity,
//							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
//				} else {
					 entity = mapper.mapDtoToEntity(dto,entity);
//				}
//			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaLogDossierDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLogDossierDTO dto, String validationContext) throws CreateException {
		try {
			TaLogDossierMapper mapper = new TaLogDossierMapper();
			TaLogDossier entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLogDossierDTO dto) throws RemoveException {
		try {
			TaLogDossierMapper mapper = new TaLogDossierMapper();
			TaLogDossier entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLogDossier refresh(TaLogDossier persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLogDossier value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLogDossier value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLogDossierDTO dto, String validationContext) {
		try {
			TaLogDossierMapper mapper = new TaLogDossierMapper();
			TaLogDossier entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLogDossierDTO> validator = new BeanValidator<TaLogDossierDTO>(TaLogDossierDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLogDossierDTO dto, String propertyName, String validationContext) {
		try {
			TaLogDossierMapper mapper = new TaLogDossierMapper();
			TaLogDossier entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLogDossierDTO> validator = new BeanValidator<TaLogDossierDTO>(TaLogDossierDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLogDossierDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLogDossierDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLogDossier value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLogDossier value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

package fr.legrain.general.service;

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

import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.general.dao.ITaFichierDAO;
import fr.legrain.general.dto.TaFichierDTO;
import fr.legrain.general.mapper.TaFichierMapper;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import fr.legrain.general.model.TaFichier;
import fr.legrain.general.service.remote.ITaFichierServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaFichierService extends AbstractApplicationDAOServer<TaFichier, TaFichierDTO> implements ITaFichierServiceRemote {

	static Logger logger = Logger.getLogger(TaFichierService.class);

	@Inject private ITaFichierDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaFichierService() {
		super(TaFichier.class,TaFichierDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaFichier a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaFichier transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaFichier transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
//		transientInstance.setCodeFichier(transientInstance.getCodeFichier().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaFichier persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaFichier merge(TaFichier detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaFichier merge(TaFichier detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
//		detachedInstance.setCodeFichier(detachedInstance.getCodeFichier().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaFichier findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaFichier findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaFichier> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaFichierDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaFichierDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaFichier> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaFichierDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaFichierDTO entityToDTO(TaFichier entity) {
		TaFichierMapper mapper = new TaFichierMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaFichierDTO> listEntityToListDTO(List<TaFichier> entity) {
		List<TaFichierDTO> l = new ArrayList<TaFichierDTO>();

		for (TaFichier TaFichier : entity) {
			l.add(entityToDTO(TaFichier));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaFichierDTO> selectAllDTO() {
		System.out.println("List of TaFichierDTO EJB :");
		ArrayList<TaFichierDTO> liste = new ArrayList<TaFichierDTO>();

		List<TaFichier> projects = selectAll();
		for(TaFichier project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaFichierDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaFichierDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaFichierDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaFichierDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaFichierDTO dto, String validationContext) throws EJBException {
		try {
			TaFichierMapper mapper = new TaFichierMapper();
			TaFichier entity = null;
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
	
	public void persistDTO(TaFichierDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaFichierDTO dto, String validationContext) throws CreateException {
		try {
			TaFichierMapper mapper = new TaFichierMapper();
			TaFichier entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaFichierDTO dto) throws RemoveException {
		try {
			TaFichierMapper mapper = new TaFichierMapper();
			TaFichier entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaFichier refresh(TaFichier persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaFichier value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaFichier value, String propertyName, String validationContext) {
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
	public void validateDTO(TaFichierDTO dto, String validationContext) {
		try {
			TaFichierMapper mapper = new TaFichierMapper();
			TaFichier entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFichierDTO> validator = new BeanValidator<TaFichierDTO>(TaFichierDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaFichierDTO dto, String propertyName, String validationContext) {
		try {
			TaFichierMapper mapper = new TaFichierMapper();
			TaFichier entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaFichierDTO> validator = new BeanValidator<TaFichierDTO>(TaFichierDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaFichierDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaFichierDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaFichier value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaFichier value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

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

import fr.legrain.bdg.model.mapping.mapper.TaTWebMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTWebServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITWebDAO;
import fr.legrain.tiers.dto.TaTWebDTO;
import fr.legrain.tiers.model.TaTWeb;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTWebService extends AbstractApplicationDAOServer<TaTWeb, TaTWebDTO> implements ITaTWebServiceRemote {

	static Logger logger = Logger.getLogger(TaTWebService.class);

	@Inject private ITWebDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTWebService() {
		super(TaTWeb.class,TaTWebDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTWeb a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTWeb transientInstance) throws CreateException {
		persist(transientInstance,null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTWeb transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
		transientInstance.setCodeTWeb(transientInstance.getCodeTWeb().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTWeb persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTWeb()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTWeb merge(TaTWeb detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTWeb merge(TaTWeb detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTWeb(detachedInstance.getCodeTWeb().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTWeb findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTWeb findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTWeb> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTWebDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTWebDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTWeb> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTWebDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTWebDTO entityToDTO(TaTWeb entity) {
		TaTWebMapper mapper = new TaTWebMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTWebDTO> listEntityToListDTO(List<TaTWeb> entity) {
		List<TaTWebDTO> l = new ArrayList<TaTWebDTO>();

		for (TaTWeb TaTWeb : entity) {
			l.add(entityToDTO(TaTWeb));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTWebDTO> selectAllDTO() {
		System.out.println("List of TaTWebDTO EJB :");
		ArrayList<TaTWebDTO> liste = new ArrayList<TaTWebDTO>();

		List<TaTWeb> projects = selectAll();
		for(TaTWeb project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTWebDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTWebDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTWebDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTWebDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTWebDTO dto, String validationContext) throws EJBException {
		try {
			TaTWebMapper mapper = new TaTWebMapper();
			TaTWeb entity = null;
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
	
	public void persistDTO(TaTWebDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTWebDTO dto, String validationContext) throws CreateException {
		try {
			TaTWebMapper mapper = new TaTWebMapper();
			TaTWeb entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTWebDTO dto) throws RemoveException {
		try {
			TaTWebMapper mapper = new TaTWebMapper();
			TaTWeb entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTWeb refresh(TaTWeb persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTWeb value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTWeb value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTWebDTO dto, String validationContext) {
		try {
			TaTWebMapper mapper = new TaTWebMapper();
			TaTWeb entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTWebDTO> validator = new BeanValidator<TaTWebDTO>(TaTWebDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTWebDTO dto, String propertyName, String validationContext) {
		try {
			TaTWebMapper mapper = new TaTWebMapper();
			TaTWeb entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTWebDTO> validator = new BeanValidator<TaTWebDTO>(TaTWebDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTWebDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTWebDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTWeb value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTWeb value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

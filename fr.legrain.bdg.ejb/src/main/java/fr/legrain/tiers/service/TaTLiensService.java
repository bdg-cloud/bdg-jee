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

import fr.legrain.bdg.model.mapping.mapper.TaTLiensMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTLiensServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITLiensDAO;
import fr.legrain.tiers.dto.TaTLiensDTO;
import fr.legrain.tiers.model.TaTLiens;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTLiensService extends AbstractApplicationDAOServer<TaTLiens, TaTLiensDTO> implements ITaTLiensServiceRemote {

	static Logger logger = Logger.getLogger(TaTLiensService.class);

	@Inject private ITLiensDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTLiensService() {
		super(TaTLiens.class,TaTLiensDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTLiens a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTLiens transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTLiens transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTLiens(transientInstance.getCodeTLiens().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTLiens persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTLiens()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTLiens merge(TaTLiens detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTLiens merge(TaTLiens detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTLiens(detachedInstance.getCodeTLiens().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTLiens findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTLiens findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTLiens> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTLiensDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTLiensDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTLiens> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTLiensDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTLiensDTO entityToDTO(TaTLiens entity) {
		TaTLiensMapper mapper = new TaTLiensMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTLiensDTO> listEntityToListDTO(List<TaTLiens> entity) {
		List<TaTLiensDTO> l = new ArrayList<TaTLiensDTO>();

		for (TaTLiens TaTLiens : entity) {
			l.add(entityToDTO(TaTLiens));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTLiensDTO> selectAllDTO() {
		System.out.println("List of TaTLiensDTO EJB :");
		ArrayList<TaTLiensDTO> liste = new ArrayList<TaTLiensDTO>();

		List<TaTLiens> projects = selectAll();
		for(TaTLiens project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTLiensDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTLiensDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTLiensDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTLiensDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTLiensDTO dto, String validationContext) throws EJBException {
		try {
			TaTLiensMapper mapper = new TaTLiensMapper();
			TaTLiens entity = null;
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
	
	public void persistDTO(TaTLiensDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTLiensDTO dto, String validationContext) throws CreateException {
		try {
			TaTLiensMapper mapper = new TaTLiensMapper();
			TaTLiens entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTLiensDTO dto) throws RemoveException {
		try {
			TaTLiensMapper mapper = new TaTLiensMapper();
			TaTLiens entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTLiens refresh(TaTLiens persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTLiens value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTLiens value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTLiensDTO dto, String validationContext) {
		try {
			TaTLiensMapper mapper = new TaTLiensMapper();
			TaTLiens entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTLiensDTO> validator = new BeanValidator<TaTLiensDTO>(TaTLiensDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTLiensDTO dto, String propertyName, String validationContext) {
		try {
			TaTLiensMapper mapper = new TaTLiensMapper();
			TaTLiens entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTLiensDTO> validator = new BeanValidator<TaTLiensDTO>(TaTLiensDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTLiensDTO dto) {
		validateDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTLiensDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTLiens value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTLiens value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

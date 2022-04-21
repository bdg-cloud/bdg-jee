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

import fr.legrain.bdg.model.mapping.mapper.TaLiensMapper;
import fr.legrain.bdg.tiers.service.remote.ITaLiensServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ILiensDAO;
import fr.legrain.tiers.dto.TaLiensDTO;
import fr.legrain.tiers.model.TaLiens;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaLiensService extends AbstractApplicationDAOServer<TaLiens, TaLiensDTO> implements ITaLiensServiceRemote {

	static Logger logger = Logger.getLogger(TaLiensService.class);

	@Inject private ILiensDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLiensService() {
		super(TaLiens.class,TaLiensDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLiens a";
	
	public TaLiens findLiensTiers(String codeTiers, String codeTypeLiens) {
		return dao.findLiensTiers(codeTiers, codeTypeLiens);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLiens transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLiens transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLiens persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLiens()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLiens merge(TaLiens detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLiens merge(TaLiens detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLiens findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLiens findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLiens> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLiensDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLiensDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLiens> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLiensDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLiensDTO entityToDTO(TaLiens entity) {
		TaLiensMapper mapper = new TaLiensMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLiensDTO> listEntityToListDTO(List<TaLiens> entity) {
		List<TaLiensDTO> l = new ArrayList<TaLiensDTO>();

		for (TaLiens TaLiens : entity) {
			l.add(entityToDTO(TaLiens));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLiensDTO> selectAllDTO() {
		System.out.println("List of TaLiensDTO EJB :");
		ArrayList<TaLiensDTO> liste = new ArrayList<TaLiensDTO>();

		List<TaLiens> projects = selectAll();
		for(TaLiens project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLiensDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLiensDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLiensDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLiensDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLiensDTO dto, String validationContext) throws EJBException {
		try {
			TaLiensMapper mapper = new TaLiensMapper();
			TaLiens entity = null;
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
	
	public void persistDTO(TaLiensDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLiensDTO dto, String validationContext) throws CreateException {
		try {
			TaLiensMapper mapper = new TaLiensMapper();
			TaLiens entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLiensDTO dto) throws RemoveException {
		try {
			TaLiensMapper mapper = new TaLiensMapper();
			TaLiens entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLiens refresh(TaLiens persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLiens value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLiens value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLiensDTO dto, String validationContext) {
		try {
			TaLiensMapper mapper = new TaLiensMapper();
			TaLiens entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLiensDTO> validator = new BeanValidator<TaLiensDTO>(TaLiensDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLiensDTO dto, String propertyName, String validationContext) {
		try {
			TaLiensMapper mapper = new TaLiensMapper();
			TaLiens entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLiensDTO> validator = new BeanValidator<TaLiensDTO>(TaLiensDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLiensDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLiensDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLiens value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLiens value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

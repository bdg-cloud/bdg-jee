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

import fr.legrain.bdg.model.mapping.mapper.TaTNoteTiersMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTNoteTiersServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITNoteTiersDAO;
import fr.legrain.tiers.dto.TaTNoteTiersDTO;
import fr.legrain.tiers.model.TaTNoteTiers;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTNoteTiersService extends AbstractApplicationDAOServer<TaTNoteTiers, TaTNoteTiersDTO> implements ITaTNoteTiersServiceRemote {

	static Logger logger = Logger.getLogger(TaTNoteTiersService.class);

	@Inject private ITNoteTiersDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTNoteTiersService() {
		super(TaTNoteTiers.class,TaTNoteTiersDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTNoteTiers a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTNoteTiers transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTNoteTiers transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
		transientInstance.setCodeTNoteTiers(transientInstance.getCodeTNoteTiers().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTNoteTiers persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTNoteTiers()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTNoteTiers merge(TaTNoteTiers detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTNoteTiers merge(TaTNoteTiers detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTNoteTiers(detachedInstance.getCodeTNoteTiers().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTNoteTiers findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTNoteTiers findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTNoteTiers> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTNoteTiersDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTNoteTiersDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTNoteTiers> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTNoteTiersDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTNoteTiersDTO entityToDTO(TaTNoteTiers entity) {
		TaTNoteTiersMapper mapper = new TaTNoteTiersMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTNoteTiersDTO> listEntityToListDTO(List<TaTNoteTiers> entity) {
		List<TaTNoteTiersDTO> l = new ArrayList<TaTNoteTiersDTO>();

		for (TaTNoteTiers TaTNoteTiers : entity) {
			l.add(entityToDTO(TaTNoteTiers));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTNoteTiersDTO> selectAllDTO() {
		System.out.println("List of TaTNoteTiersDTO EJB :");
		ArrayList<TaTNoteTiersDTO> liste = new ArrayList<TaTNoteTiersDTO>();

		List<TaTNoteTiers> projects = selectAll();
		for(TaTNoteTiers project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTNoteTiersDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTNoteTiersDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTNoteTiersDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTNoteTiersDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTNoteTiersDTO dto, String validationContext) throws EJBException {
		try {
			TaTNoteTiersMapper mapper = new TaTNoteTiersMapper();
			TaTNoteTiers entity = null;
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
	
	public void persistDTO(TaTNoteTiersDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTNoteTiersDTO dto, String validationContext) throws CreateException {
		try {
			TaTNoteTiersMapper mapper = new TaTNoteTiersMapper();
			TaTNoteTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTNoteTiersDTO dto) throws RemoveException {
		try {
			TaTNoteTiersMapper mapper = new TaTNoteTiersMapper();
			TaTNoteTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTNoteTiers refresh(TaTNoteTiers persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTNoteTiers value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTNoteTiers value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTNoteTiersDTO dto, String validationContext) {
		try {
			TaTNoteTiersMapper mapper = new TaTNoteTiersMapper();
			TaTNoteTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTNoteTiersDTO> validator = new BeanValidator<TaTNoteTiersDTO>(TaTNoteTiersDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTNoteTiersDTO dto, String propertyName, String validationContext) {
		try {
			TaTNoteTiersMapper mapper = new TaTNoteTiersMapper();
			TaTNoteTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTNoteTiersDTO> validator = new BeanValidator<TaTNoteTiersDTO>(TaTNoteTiersDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTNoteTiersDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTNoteTiersDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTNoteTiers value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTNoteTiers value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

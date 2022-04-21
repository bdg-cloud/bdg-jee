package fr.legrain.tiers.service;

import java.util.ArrayList;
import java.util.List;

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

import fr.legrain.bdg.model.mapping.mapper.TaTTiersMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTTiersServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITTiersDAO;
import fr.legrain.tiers.dto.TaTTiersDTO;
import fr.legrain.tiers.model.TaTTiers;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
//@DeclareRoles("admin")
//@RolesAllowed("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTTiersService extends AbstractApplicationDAOServer<TaTTiers, TaTTiersDTO> implements ITaTTiersServiceRemote {

	static Logger logger = Logger.getLogger(TaTTiersService.class);

	@Inject private ITTiersDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTTiersService() {
		super(TaTTiers.class,TaTTiersDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTTiers a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTTiers transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTTiers transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTTiers(transientInstance.getCodeTTiers().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTTiers persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTTiers()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTTiers merge(TaTTiers detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTTiers merge(TaTTiers detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTTiers(detachedInstance.getCodeTTiers().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTTiers findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTTiers findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTTiers> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTTiersDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTTiersDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTTiers> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTTiersDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTTiersDTO entityToDTO(TaTTiers entity) {
		TaTTiersMapper mapper = new TaTTiersMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTTiersDTO> listEntityToListDTO(List<TaTTiers> entity) {
		List<TaTTiersDTO> l = new ArrayList<TaTTiersDTO>();

		for (TaTTiers TaTTiers : entity) {
			l.add(entityToDTO(TaTTiers));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTTiersDTO> selectAllDTO() {
		System.out.println("List of TaTTiersDTO EJB :");
		ArrayList<TaTTiersDTO> liste = new ArrayList<TaTTiersDTO>();

		List<TaTTiers> projects = selectAll();
		for(TaTTiers project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTTiersDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTTiersDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTTiersDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTTiersDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTTiersDTO dto, String validationContext) throws EJBException {
		try {
			TaTTiersMapper mapper = new TaTTiersMapper();
			TaTTiers entity = null;
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
	
	public void persistDTO(TaTTiersDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTTiersDTO dto, String validationContext) throws CreateException {
		try {
			TaTTiersMapper mapper = new TaTTiersMapper();
			TaTTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTTiersDTO dto) throws RemoveException {
		try {
			TaTTiersMapper mapper = new TaTTiersMapper();
			TaTTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTTiers refresh(TaTTiers persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTTiers value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTTiers value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTTiersDTO dto, String validationContext) {
		try {
			TaTTiersMapper mapper = new TaTTiersMapper();
			TaTTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTiersDTO> validator = new BeanValidator<TaTTiersDTO>(TaTTiersDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTTiersDTO dto, String propertyName, String validationContext) {
		try {
			TaTTiersMapper mapper = new TaTTiersMapper();
			TaTTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTiersDTO> validator = new BeanValidator<TaTTiersDTO>(TaTTiersDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTTiersDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTTiersDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTTiers value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTTiers value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

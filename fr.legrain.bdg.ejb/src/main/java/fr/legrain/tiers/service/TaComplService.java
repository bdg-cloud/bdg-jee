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

import fr.legrain.bdg.model.mapping.mapper.TaComplMapper;
import fr.legrain.bdg.tiers.service.remote.ITaComplServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.IComplDAO;
import fr.legrain.tiers.dto.TaComplDTO;
import fr.legrain.tiers.model.TaCompl;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaComplService extends AbstractApplicationDAOServer<TaCompl, TaComplDTO> implements ITaComplServiceRemote {

	static Logger logger = Logger.getLogger(TaComplService.class);

	@Inject private IComplDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaComplService() {
		super(TaCompl.class,TaComplDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCompl a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCompl transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCompl transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCompl persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCompl()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCompl merge(TaCompl detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCompl merge(TaCompl detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCompl findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCompl findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCompl> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaComplDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaComplDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCompl> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaComplDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaComplDTO entityToDTO(TaCompl entity) {
		TaComplMapper mapper = new TaComplMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaComplDTO> listEntityToListDTO(List<TaCompl> entity) {
		List<TaComplDTO> l = new ArrayList<TaComplDTO>();

		for (TaCompl TaCompl : entity) {
			l.add(entityToDTO(TaCompl));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaComplDTO> selectAllDTO() {
		System.out.println("List of TaComplDTO EJB :");
		ArrayList<TaComplDTO> liste = new ArrayList<TaComplDTO>();

		List<TaCompl> projects = selectAll();
		for(TaCompl project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaComplDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaComplDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaComplDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaComplDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaComplDTO dto, String validationContext) throws EJBException {
		try {
			TaComplMapper mapper = new TaComplMapper();
			TaCompl entity = null;
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
	
	public void persistDTO(TaComplDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaComplDTO dto, String validationContext) throws CreateException {
		try {
			TaComplMapper mapper = new TaComplMapper();
			TaCompl entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaComplDTO dto) throws RemoveException {
		try {
			TaComplMapper mapper = new TaComplMapper();
			TaCompl entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCompl refresh(TaCompl persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCompl value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCompl value, String propertyName, String validationContext) {
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
	public void validateDTO(TaComplDTO dto, String validationContext) {
		try {
			TaComplMapper mapper = new TaComplMapper();
			TaCompl entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaComplDTO> validator = new BeanValidator<TaComplDTO>(TaComplDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaComplDTO dto, String propertyName, String validationContext) {
		try {
			TaComplMapper mapper = new TaComplMapper();
			TaCompl entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaComplDTO> validator = new BeanValidator<TaComplDTO>(TaComplDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaComplDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaComplDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCompl value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCompl value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

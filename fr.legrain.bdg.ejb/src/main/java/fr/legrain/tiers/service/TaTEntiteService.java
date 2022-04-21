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

import fr.legrain.bdg.model.mapping.mapper.TaTEntiteMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTEntiteServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITEntiteDAO;
import fr.legrain.tiers.dto.TaTEntiteDTO;
import fr.legrain.tiers.model.TaTEntite;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTEntiteService extends AbstractApplicationDAOServer<TaTEntite, TaTEntiteDTO> implements ITaTEntiteServiceRemote {

	static Logger logger = Logger.getLogger(TaTEntiteService.class);

	@Inject private ITEntiteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTEntiteService() {
		super(TaTEntite.class,TaTEntiteDTO.class);
	}
	
	public List<TaTEntiteDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTEntite a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTEntite transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTEntite transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
		transientInstance.setCodeTEntite(transientInstance.getCodeTEntite().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTEntite persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdTEntite()));
	}
	
	public TaTEntite merge(TaTEntite detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTEntite merge(TaTEntite detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		detachedInstance.setCodeTEntite(detachedInstance.getCodeTEntite().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTEntite findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTEntite findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTEntite> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTEntiteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTEntiteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTEntite> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTEntiteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTEntiteDTO entityToDTO(TaTEntite entity) {
		TaTEntiteMapper mapper = new TaTEntiteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTEntiteDTO> listEntityToListDTO(List<TaTEntite> entity) {
		List<TaTEntiteDTO> l = new ArrayList<TaTEntiteDTO>();

		for (TaTEntite TaTEntite : entity) {
			l.add(entityToDTO(TaTEntite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTEntiteDTO> selectAllDTO() {
		System.out.println("List of TaTEntiteDTO EJB :");
		ArrayList<TaTEntiteDTO> liste = new ArrayList<TaTEntiteDTO>();

		List<TaTEntite> projects = selectAll();
		for(TaTEntite project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTEntiteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTEntiteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTEntiteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTEntiteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTEntiteDTO dto, String validationContext) throws EJBException {
		try {
			TaTEntiteMapper mapper = new TaTEntiteMapper();
			TaTEntite entity = null;
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
	
	public void persistDTO(TaTEntiteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTEntiteDTO dto, String validationContext) throws CreateException {
		try {
			TaTEntiteMapper mapper = new TaTEntiteMapper();
			TaTEntite entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTEntiteDTO dto) throws RemoveException {
		try {
			TaTEntiteMapper mapper = new TaTEntiteMapper();
			TaTEntite entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTEntite refresh(TaTEntite persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTEntite value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTEntite value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTEntiteDTO dto, String validationContext) {
		try {
			TaTEntiteMapper mapper = new TaTEntiteMapper();
			TaTEntite entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTEntiteDTO> validator = new BeanValidator<TaTEntiteDTO>(TaTEntiteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTEntiteDTO dto, String propertyName, String validationContext) {
		try {
			TaTEntiteMapper mapper = new TaTEntiteMapper();
			TaTEntite entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTEntiteDTO> validator = new BeanValidator<TaTEntiteDTO>(TaTEntiteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTEntiteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTEntiteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTEntite value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTEntite value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

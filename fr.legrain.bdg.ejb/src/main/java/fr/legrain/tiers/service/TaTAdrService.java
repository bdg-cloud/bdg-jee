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

import fr.legrain.bdg.model.mapping.mapper.TaTAdrMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTAdrServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITAdrDAO;
import fr.legrain.tiers.dto.TaTAdrDTO;
import fr.legrain.tiers.model.TaTAdr;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTAdrService extends AbstractApplicationDAOServer<TaTAdr, TaTAdrDTO> implements ITaTAdrServiceRemote {

	static Logger logger = Logger.getLogger(TaTAdrService.class);

	@Inject private ITAdrDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTAdrService() {
		super(TaTAdr.class,TaTAdrDTO.class);
		
		TaTAdr a = getInstanceOfEntity(TaTAdr.class);
		init(a);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTAdr a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTAdr transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTAdr transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
		transientInstance.setCodeTAdr(transientInstance.getCodeTAdr().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTAdr persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdTAdr()));
	}
	
	public TaTAdr merge(TaTAdr detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTAdr merge(TaTAdr detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		detachedInstance.setCodeTAdr(detachedInstance.getCodeTAdr().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTAdr findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTAdr findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTAdr> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTAdrDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTAdrDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTAdr> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTAdrDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTAdrDTO entityToDTO(TaTAdr entity) {
		TaTAdrMapper mapper = new TaTAdrMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTAdrDTO> listEntityToListDTO(List<TaTAdr> entity) {
		List<TaTAdrDTO> l = new ArrayList<TaTAdrDTO>();

		for (TaTAdr TaTAdr : entity) {
			l.add(entityToDTO(TaTAdr));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTAdrDTO> selectAllDTO() {
		System.out.println("List of TaTAdrDTO EJB :");
		ArrayList<TaTAdrDTO> liste = new ArrayList<TaTAdrDTO>();

		List<TaTAdr> projects = selectAll();
		for(TaTAdr project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTAdrDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTAdrDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTAdrDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTAdrDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTAdrDTO dto, String validationContext) throws EJBException {
		try {
			TaTAdrMapper mapper = new TaTAdrMapper();
			TaTAdr entity = null;
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
	
	public void persistDTO(TaTAdrDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTAdrDTO dto, String validationContext) throws CreateException {
		try {
			TaTAdrMapper mapper = new TaTAdrMapper();
			TaTAdr entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTAdrDTO dto) throws RemoveException {
		try {
			TaTAdrMapper mapper = new TaTAdrMapper();
			TaTAdr entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTAdr refresh(TaTAdr persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTAdr value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTAdr value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTAdrDTO dto, String validationContext) {
		try {
			TaTAdrMapper mapper = new TaTAdrMapper();
			TaTAdr entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTAdrDTO> validator = new BeanValidator<TaTAdrDTO>(TaTAdrDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTAdrDTO dto, String propertyName, String validationContext) {
		try {
			TaTAdrMapper mapper = new TaTAdrMapper();
			TaTAdr entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTAdrDTO> validator = new BeanValidator<TaTAdrDTO>(TaTAdrDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTAdrDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTAdrDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTAdr value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTAdr value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

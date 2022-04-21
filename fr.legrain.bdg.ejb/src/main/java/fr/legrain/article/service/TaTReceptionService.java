package fr.legrain.article.service;

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

import fr.legrain.article.dao.ITReceptionDAO;
import fr.legrain.article.dto.TaTReceptionDTO;
import fr.legrain.article.model.TaTReception;
import fr.legrain.bdg.article.service.remote.ITaTReceptionServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTReceptionMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTReceptionService extends AbstractApplicationDAOServer<TaTReception, TaTReceptionDTO> implements ITaTReceptionServiceRemote {

	static Logger logger = Logger.getLogger(TaTReceptionService.class);

	@Inject private ITReceptionDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTReceptionService() {
		super(TaTReception.class,TaTReceptionDTO.class);
	}
	
	public List<TaTReceptionDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTReception a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTReception transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTReception transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTReception(transientInstance.getCodeTReception().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTReception persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdTReception()));
	}
	
	public TaTReception merge(TaTReception detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTReception merge(TaTReception detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		detachedInstance.setCodeTReception(detachedInstance.getCodeTReception().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTReception findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTReception findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTReception> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTReceptionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTReceptionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTReception> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTReceptionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTReceptionDTO entityToDTO(TaTReception entity) {
		TaTReceptionMapper mapper = new TaTReceptionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTReceptionDTO> listEntityToListDTO(List<TaTReception> entity) {
		List<TaTReceptionDTO> l = new ArrayList<TaTReceptionDTO>();

		for (TaTReception TaTReception : entity) {
			l.add(entityToDTO(TaTReception));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTReceptionDTO> selectAllDTO() {
		System.out.println("List of TaTReceptionDTO EJB :");
		ArrayList<TaTReceptionDTO> liste = new ArrayList<TaTReceptionDTO>();

		List<TaTReception> projects = selectAll();
		for(TaTReception project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTReceptionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTReceptionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTReceptionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTReceptionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTReceptionDTO dto, String validationContext) throws EJBException {
		try {
			TaTReceptionMapper mapper = new TaTReceptionMapper();
			TaTReception entity = null;
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
	
	public void persistDTO(TaTReceptionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTReceptionDTO dto, String validationContext) throws CreateException {
		try {
			TaTReceptionMapper mapper = new TaTReceptionMapper();
			TaTReception entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTReceptionDTO dto) throws RemoveException {
		try {
			TaTReceptionMapper mapper = new TaTReceptionMapper();
			TaTReception entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTReception refresh(TaTReception persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTReception value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTReception value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTReceptionDTO dto, String validationContext) {
		try {
			TaTReceptionMapper mapper = new TaTReceptionMapper();
			TaTReception entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTReceptionDTO> validator = new BeanValidator<TaTReceptionDTO>(TaTReceptionDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTReceptionDTO dto, String propertyName, String validationContext) {
		try {
			TaTReceptionMapper mapper = new TaTReceptionMapper();
			TaTReception entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTReceptionDTO> validator = new BeanValidator<TaTReceptionDTO>(TaTReceptionDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTReceptionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTReceptionDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTReception value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTReception value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

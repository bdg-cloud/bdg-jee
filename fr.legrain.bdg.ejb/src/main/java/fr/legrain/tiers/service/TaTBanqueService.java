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

import fr.legrain.bdg.model.mapping.mapper.TaTBanqueMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTBanqueServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITBanqueDAO;
import fr.legrain.tiers.dto.TaTBanqueDTO;
import fr.legrain.tiers.model.TaTBanque;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTBanqueService extends AbstractApplicationDAOServer<TaTBanque, TaTBanqueDTO> implements ITaTBanqueServiceRemote {

	static Logger logger = Logger.getLogger(TaTBanqueService.class);

	@Inject private ITBanqueDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTBanqueService() {
		super(TaTBanque.class,TaTBanqueDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTBanque a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTBanque transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTBanque transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
		transientInstance.setCodeTBanque(transientInstance.getCodeTBanque().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTBanque persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdTBanque()));
	}
	
	public TaTBanque merge(TaTBanque detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTBanque merge(TaTBanque detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTBanque(detachedInstance.getCodeTBanque().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTBanque findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTBanque findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTBanque> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTBanqueDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTBanqueDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTBanque> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTBanqueDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTBanqueDTO entityToDTO(TaTBanque entity) {
		TaTBanqueMapper mapper = new TaTBanqueMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTBanqueDTO> listEntityToListDTO(List<TaTBanque> entity) {
		List<TaTBanqueDTO> l = new ArrayList<TaTBanqueDTO>();

		for (TaTBanque TaTBanque : entity) {
			l.add(entityToDTO(TaTBanque));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTBanqueDTO> selectAllDTO() {
		System.out.println("List of TaTBanqueDTO EJB :");
		ArrayList<TaTBanqueDTO> liste = new ArrayList<TaTBanqueDTO>();

		List<TaTBanque> projects = selectAll();
		for(TaTBanque project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTBanqueDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTBanqueDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTBanqueDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTBanqueDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTBanqueDTO dto, String validationContext) throws EJBException {
		try {
			TaTBanqueMapper mapper = new TaTBanqueMapper();
			TaTBanque entity = null;
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
	
	public void persistDTO(TaTBanqueDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTBanqueDTO dto, String validationContext) throws CreateException {
		try {
			TaTBanqueMapper mapper = new TaTBanqueMapper();
			TaTBanque entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTBanqueDTO dto) throws RemoveException {
		try {
			TaTBanqueMapper mapper = new TaTBanqueMapper();
			TaTBanque entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTBanque refresh(TaTBanque persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTBanque value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTBanque value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTBanqueDTO dto, String validationContext) {
		try {
			TaTBanqueMapper mapper = new TaTBanqueMapper();
			TaTBanque entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTBanqueDTO> validator = new BeanValidator<TaTBanqueDTO>(TaTBanqueDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTBanqueDTO dto, String propertyName, String validationContext) {
		try {
			TaTBanqueMapper mapper = new TaTBanqueMapper();
			TaTBanque entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTBanqueDTO> validator = new BeanValidator<TaTBanqueDTO>(TaTBanqueDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTBanqueDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTBanqueDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTBanque value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTBanque value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

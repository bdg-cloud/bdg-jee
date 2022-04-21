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

import fr.legrain.bdg.model.mapping.mapper.TaTTarifMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTTarifServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITTarifDAO;
import fr.legrain.tiers.dto.TaTTarifDTO;
import fr.legrain.tiers.model.TaTTarif;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTTarifService extends AbstractApplicationDAOServer<TaTTarif, TaTTarifDTO> implements ITaTTarifServiceRemote {

	static Logger logger = Logger.getLogger(TaTTarifService.class);

	@Inject private ITTarifDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTTarifService() {
		super(TaTTarif.class,TaTTarifDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTTarif a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTTarif transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTTarif transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTTarif(transientInstance.getCodeTTarif().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTTarif persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTTarif()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTTarif merge(TaTTarif detachedInstance) {
		 return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTTarif merge(TaTTarif detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTTarif(detachedInstance.getCodeTTarif().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTTarif findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTTarif findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTTarif> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTTarifDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTTarifDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTTarif> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTTarifDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTTarifDTO entityToDTO(TaTTarif entity) {
		TaTTarifMapper mapper = new TaTTarifMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTTarifDTO> listEntityToListDTO(List<TaTTarif> entity) {
		List<TaTTarifDTO> l = new ArrayList<TaTTarifDTO>();

		for (TaTTarif TaTTarif : entity) {
			l.add(entityToDTO(TaTTarif));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTTarifDTO> selectAllDTO() {
		System.out.println("List of TaTTarifDTO EJB :");
		ArrayList<TaTTarifDTO> liste = new ArrayList<TaTTarifDTO>();

		List<TaTTarif> projects = selectAll();
		for(TaTTarif project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTTarifDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTTarifDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTTarifDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTTarifDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTTarifDTO dto, String validationContext) throws EJBException {
		try {
			TaTTarifMapper mapper = new TaTTarifMapper();
			TaTTarif entity = null;
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
	
	public void persistDTO(TaTTarifDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTTarifDTO dto, String validationContext) throws CreateException {
		try {
			TaTTarifMapper mapper = new TaTTarifMapper();
			TaTTarif entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTTarifDTO dto) throws RemoveException {
		try {
			TaTTarifMapper mapper = new TaTTarifMapper();
			TaTTarif entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTTarif refresh(TaTTarif persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTTarif value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTTarif value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTTarifDTO dto, String validationContext) {
		try {
			TaTTarifMapper mapper = new TaTTarifMapper();
			TaTTarif entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTarifDTO> validator = new BeanValidator<TaTTarifDTO>(TaTTarifDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTTarifDTO dto, String propertyName, String validationContext) {
		try {
			TaTTarifMapper mapper = new TaTTarifMapper();
			TaTTarif entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTTarifDTO> validator = new BeanValidator<TaTTarifDTO>(TaTTarifDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTTarifDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTTarifDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTTarif value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTTarif value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

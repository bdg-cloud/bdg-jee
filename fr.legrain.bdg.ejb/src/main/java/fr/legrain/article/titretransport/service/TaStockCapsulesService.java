package fr.legrain.article.titretransport.service;

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

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.article.titretransport.dao.IStockCapsulesDAO;
import fr.legrain.article.titretransport.dto.TaStockCapsulesDTO;
import fr.legrain.article.titretransport.model.TaStockCapsules;
import fr.legrain.bdg.article.titretransport.service.remote.ITaStockCapsulesServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStockCapsulesMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStockCapsulesBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStockCapsulesService extends AbstractApplicationDAOServer<TaStockCapsules, TaStockCapsulesDTO> implements ITaStockCapsulesServiceRemote {

	static Logger logger = Logger.getLogger(TaStockCapsulesService.class);

	@Inject private IStockCapsulesDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStockCapsulesService() {
		super(TaStockCapsules.class,TaStockCapsulesDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStockCapsules a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStockCapsules transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStockCapsules transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStockCapsules persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdStock()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaStockCapsules merge(TaStockCapsules detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStockCapsules merge(TaStockCapsules detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStockCapsules findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStockCapsules findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStockCapsules> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStockCapsulesDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStockCapsulesDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStockCapsules> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStockCapsulesDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStockCapsulesDTO entityToDTO(TaStockCapsules entity) {
//		TaStockCapsulesDTO dto = new TaStockCapsulesDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStockCapsulesMapper mapper = new TaStockCapsulesMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStockCapsulesDTO> listEntityToListDTO(List<TaStockCapsules> entity) {
		List<TaStockCapsulesDTO> l = new ArrayList<TaStockCapsulesDTO>();

		for (TaStockCapsules taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStockCapsulesDTO> selectAllDTO() {
		System.out.println("List of TaStockCapsulesDTO EJB :");
		ArrayList<TaStockCapsulesDTO> liste = new ArrayList<TaStockCapsulesDTO>();

		List<TaStockCapsules> projects = selectAll();
		for(TaStockCapsules project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStockCapsulesDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStockCapsulesDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStockCapsulesDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStockCapsulesDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStockCapsulesDTO dto, String validationContext) throws EJBException {
		try {
			TaStockCapsulesMapper mapper = new TaStockCapsulesMapper();
			TaStockCapsules entity = null;
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
	
	public void persistDTO(TaStockCapsulesDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStockCapsulesDTO dto, String validationContext) throws CreateException {
		try {
			TaStockCapsulesMapper mapper = new TaStockCapsulesMapper();
			TaStockCapsules entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStockCapsulesDTO dto) throws RemoveException {
		try {
			TaStockCapsulesMapper mapper = new TaStockCapsulesMapper();
			TaStockCapsules entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStockCapsules refresh(TaStockCapsules persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStockCapsules value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStockCapsules value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStockCapsulesDTO dto, String validationContext) {
		try {
			TaStockCapsulesMapper mapper = new TaStockCapsulesMapper();
			TaStockCapsules entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStockCapsulesDTO> validator = new BeanValidator<TaStockCapsulesDTO>(TaStockCapsulesDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStockCapsulesDTO dto, String propertyName, String validationContext) {
		try {
			TaStockCapsulesMapper mapper = new TaStockCapsulesMapper();
			TaStockCapsules entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStockCapsulesDTO> validator = new BeanValidator<TaStockCapsulesDTO>(TaStockCapsulesDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStockCapsulesDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStockCapsulesDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStockCapsules value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStockCapsules value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

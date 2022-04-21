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

import fr.legrain.bdg.model.mapping.mapper.TaDocumentTiersMapper;
import fr.legrain.bdg.tiers.service.remote.ITaDocumentTiersServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.IDocumentTiersDAO;
import fr.legrain.tiers.dto.TaDocumentTiersDTO;
import fr.legrain.tiers.model.TaDocumentTiers;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaDocumentTiersService extends AbstractApplicationDAOServer<TaDocumentTiers, TaDocumentTiersDTO> implements ITaDocumentTiersServiceRemote {

	static Logger logger = Logger.getLogger(TaDocumentTiersService.class);

	@Inject private IDocumentTiersDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaDocumentTiersService() {
		super(TaDocumentTiers.class,TaDocumentTiersDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaDocumentTiers a";

	public void RAZCheminVersion(String typeLogiciel) {
		dao.RAZCheminVersion(typeLogiciel);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaDocumentTiers transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaDocumentTiers transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaDocumentTiers persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaDocumentTiers merge(TaDocumentTiers detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergetValidationContext")
	public TaDocumentTiers merge(TaDocumentTiers detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaDocumentTiers findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaDocumentTiers findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaDocumentTiers> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaDocumentTiersDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaDocumentTiersDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaDocumentTiers> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaDocumentTiersDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaDocumentTiersDTO entityToDTO(TaDocumentTiers entity) {
		TaDocumentTiersMapper mapper = new TaDocumentTiersMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaDocumentTiersDTO> listEntityToListDTO(List<TaDocumentTiers> entity) {
		List<TaDocumentTiersDTO> l = new ArrayList<TaDocumentTiersDTO>();

		for (TaDocumentTiers TaDocumentTiers : entity) {
			l.add(entityToDTO(TaDocumentTiers));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaDocumentTiersDTO> selectAllDTO() {
		System.out.println("List of TaDocumentTiersDTO EJB :");
		ArrayList<TaDocumentTiersDTO> liste = new ArrayList<TaDocumentTiersDTO>();

		List<TaDocumentTiers> projects = selectAll();
		for(TaDocumentTiers project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaDocumentTiersDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaDocumentTiersDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaDocumentTiersDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaDocumentTiersDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaDocumentTiersDTO dto, String validationContext) throws EJBException {
		try {
			TaDocumentTiersMapper mapper = new TaDocumentTiersMapper();
			TaDocumentTiers entity = null;
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
	
	public void persistDTO(TaDocumentTiersDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaDocumentTiersDTO dto, String validationContext) throws CreateException {
		try {
			TaDocumentTiersMapper mapper = new TaDocumentTiersMapper();
			TaDocumentTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaDocumentTiersDTO dto) throws RemoveException {
		try {
			TaDocumentTiersMapper mapper = new TaDocumentTiersMapper();
			TaDocumentTiers entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaDocumentTiers refresh(TaDocumentTiers persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaDocumentTiers value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaDocumentTiers value, String propertyName, String validationContext) {
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
	public void validateDTO(TaDocumentTiersDTO dto, String validationContext) {
		try {
			TaDocumentTiersMapper mapper = new TaDocumentTiersMapper();
			TaDocumentTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaDocumentTiersDTO> validator = new BeanValidator<TaDocumentTiersDTO>(TaDocumentTiersDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaDocumentTiersDTO dto, String propertyName, String validationContext) {
		try {
			TaDocumentTiersMapper mapper = new TaDocumentTiersMapper();
			TaDocumentTiers entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaDocumentTiersDTO> validator = new BeanValidator<TaDocumentTiersDTO>(TaDocumentTiersDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaDocumentTiersDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaDocumentTiersDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaDocumentTiers value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaDocumentTiers value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

package fr.legrain.general.service;

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

import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.general.dao.ITaAliasEspaceClientDAO;
import fr.legrain.general.dto.TaAliasEspaceClientDTO;
import fr.legrain.general.mapper.TaAliasEspaceClientMapper;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import fr.legrain.general.model.TaAliasEspaceClient;
import fr.legrain.general.service.remote.ITaAliasEspaceClientServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaAliasEspaceClientService extends AbstractApplicationDAOServer<TaAliasEspaceClient, TaAliasEspaceClientDTO> implements ITaAliasEspaceClientServiceRemote {

	static Logger logger = Logger.getLogger(TaAliasEspaceClientService.class);

	@Inject private ITaAliasEspaceClientDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaAliasEspaceClientService() {
		super(TaAliasEspaceClient.class,TaAliasEspaceClientDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaAliasEspaceClient a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaAliasEspaceClient transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaAliasEspaceClient transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
//		transientInstance.setCodeFichier(transientInstance.getCodeFichier().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaAliasEspaceClient persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaAliasEspaceClient merge(TaAliasEspaceClient detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaAliasEspaceClient merge(TaAliasEspaceClient detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
//		detachedInstance.setCodeFichier(detachedInstance.getCodeFichier().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaAliasEspaceClient findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaAliasEspaceClient findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaAliasEspaceClient> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAliasEspaceClientDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAliasEspaceClientDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaAliasEspaceClient> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAliasEspaceClientDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAliasEspaceClientDTO entityToDTO(TaAliasEspaceClient entity) {
		TaAliasEspaceClientMapper mapper = new TaAliasEspaceClientMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAliasEspaceClientDTO> listEntityToListDTO(List<TaAliasEspaceClient> entity) {
		List<TaAliasEspaceClientDTO> l = new ArrayList<TaAliasEspaceClientDTO>();

		for (TaAliasEspaceClient TaAliasEspaceClient : entity) {
			l.add(entityToDTO(TaAliasEspaceClient));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAliasEspaceClientDTO> selectAllDTO() {
		System.out.println("List of TaAliasEspaceClientDTO EJB :");
		ArrayList<TaAliasEspaceClientDTO> liste = new ArrayList<TaAliasEspaceClientDTO>();

		List<TaAliasEspaceClient> projects = selectAll();
		for(TaAliasEspaceClient project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAliasEspaceClientDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAliasEspaceClientDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAliasEspaceClientDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAliasEspaceClientDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAliasEspaceClientDTO dto, String validationContext) throws EJBException {
		try {
			TaAliasEspaceClientMapper mapper = new TaAliasEspaceClientMapper();
			TaAliasEspaceClient entity = null;
//			if(dto.getId()!=null) {
//				entity = dao.findById(dto.getId());
//				if(dto.getVersionObj()!=entity.getVersionObj()) {
//					throw new OptimisticLockException(entity,
//							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
//				} else {
					 entity = mapper.mapDtoToEntity(dto,entity);
//				}
//			}
			
			//dao.merge(entity);
			dao.detach(entity); //pour passer les controles
			enregistrerMerge(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new CreateException(e.getMessage());
			throw new EJBException(e.getMessage());
		}
	}
	
	public void persistDTO(TaAliasEspaceClientDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAliasEspaceClientDTO dto, String validationContext) throws CreateException {
		try {
			TaAliasEspaceClientMapper mapper = new TaAliasEspaceClientMapper();
			TaAliasEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAliasEspaceClientDTO dto) throws RemoveException {
		try {
			TaAliasEspaceClientMapper mapper = new TaAliasEspaceClientMapper();
			TaAliasEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaAliasEspaceClient refresh(TaAliasEspaceClient persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaAliasEspaceClient value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaAliasEspaceClient value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAliasEspaceClientDTO dto, String validationContext) {
		try {
			TaAliasEspaceClientMapper mapper = new TaAliasEspaceClientMapper();
			TaAliasEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAliasEspaceClientDTO> validator = new BeanValidator<TaAliasEspaceClientDTO>(TaAliasEspaceClientDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAliasEspaceClientDTO dto, String propertyName, String validationContext) {
		try {
			TaAliasEspaceClientMapper mapper = new TaAliasEspaceClientMapper();
			TaAliasEspaceClient entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAliasEspaceClientDTO> validator = new BeanValidator<TaAliasEspaceClientDTO>(TaAliasEspaceClientDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAliasEspaceClientDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAliasEspaceClientDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaAliasEspaceClient value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaAliasEspaceClient value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

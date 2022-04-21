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
import fr.legrain.droits.service.ParamBdg;
import fr.legrain.general.dao.ITaParametreDAO;
import fr.legrain.general.dto.TaParametreDTO;
import fr.legrain.general.mapper.TaParametreMapper;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import fr.legrain.general.model.TaParametre;
import fr.legrain.general.service.remote.ITaParametreServiceRemote;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


@Stateless
//@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaParametreService extends AbstractApplicationDAOServer<TaParametre, TaParametreDTO> implements ITaParametreServiceRemote {

	static Logger logger = Logger.getLogger(TaParametreService.class);

	@Inject private ITaParametreDAO dao;
	@Inject private ParamBdg paramBdg;

	/**
	 * Default constructor. 
	 */
	public TaParametreService() {
		super(TaParametre.class,TaParametreDTO.class);
	}
	
	public void initParamBdg() {
		paramBdg.setTaParametre(findInstance());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaParametre transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaParametre transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
//		transientInstance.setCodeFichier(transientInstance.getCodeFichier().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaParametre persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaParametre merge(TaParametre detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaParametre merge(TaParametre detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
//		detachedInstance.setCodeFichier(detachedInstance.getCodeFichier().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaParametre findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaParametre findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaParametre> selectAll() {
		return dao.selectAll();
	}
	
	public TaParametre findInstance() {
		return dao.findInstance();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaParametreDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaParametreDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaParametre> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaParametreDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaParametreDTO entityToDTO(TaParametre entity) {
		TaParametreMapper mapper = new TaParametreMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaParametreDTO> listEntityToListDTO(List<TaParametre> entity) {
		List<TaParametreDTO> l = new ArrayList<TaParametreDTO>();

		for (TaParametre TaParametre : entity) {
			l.add(entityToDTO(TaParametre));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaParametreDTO> selectAllDTO() {
		System.out.println("List of TaParametreDTO EJB :");
		ArrayList<TaParametreDTO> liste = new ArrayList<TaParametreDTO>();

		List<TaParametre> projects = selectAll();
		for(TaParametre project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaParametreDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaParametreDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaParametreDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaParametreDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaParametreDTO dto, String validationContext) throws EJBException {
		try {
			TaParametreMapper mapper = new TaParametreMapper();
			TaParametre entity = null;
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
	
	public void persistDTO(TaParametreDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaParametreDTO dto, String validationContext) throws CreateException {
		try {
			TaParametreMapper mapper = new TaParametreMapper();
			TaParametre entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaParametreDTO dto) throws RemoveException {
		try {
			TaParametreMapper mapper = new TaParametreMapper();
			TaParametre entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaParametre refresh(TaParametre persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaParametre value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaParametre value, String propertyName, String validationContext) {
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
	public void validateDTO(TaParametreDTO dto, String validationContext) {
		try {
			TaParametreMapper mapper = new TaParametreMapper();
			TaParametre entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParametreDTO> validator = new BeanValidator<TaParametreDTO>(TaParametreDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaParametreDTO dto, String propertyName, String validationContext) {
		try {
			TaParametreMapper mapper = new TaParametreMapper();
			TaParametre entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaParametreDTO> validator = new BeanValidator<TaParametreDTO>(TaParametreDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaParametreDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaParametreDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaParametre value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaParametre value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

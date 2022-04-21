package fr.legrain.dossier.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.article.service.remote.ITaCategorieArticleServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaCategoriePreferenceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaCategoriePreferenceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaCategoriePreferenceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.dossier.dao.ICategoriePreferenceDAO;
import fr.legrain.dossier.dao.IPreferencesDAO;
import fr.legrain.dossier.dto.TaCategoriePreferenceDTO;
import fr.legrain.dossier.model.TaCategoriePreference;
import fr.legrain.dossier.model.TaCategoriePreference;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaCategoriePreferenceService extends AbstractApplicationDAOServer<TaCategoriePreference, TaCategoriePreferenceDTO> implements ITaCategoriePreferenceServiceRemote {

	static Logger logger = Logger.getLogger(TaCategoriePreferenceService.class);

	@Inject private ICategoriePreferenceDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaCategoriePreferenceService() {
		super(TaCategoriePreference.class,TaCategoriePreferenceDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCategoriePreference a";


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCategoriePreference transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
		
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCategoriePreference transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCategoriePreference persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaCategoriePreference merge(TaCategoriePreference detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCategoriePreference merge(TaCategoriePreference detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCategoriePreference findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCategoriePreference findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCategoriePreference> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCategoriePreferenceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCategoriePreferenceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCategoriePreference> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCategoriePreferenceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCategoriePreferenceDTO entityToDTO(TaCategoriePreference entity) {
		TaCategoriePreferenceMapper mapper = new TaCategoriePreferenceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCategoriePreferenceDTO> listEntityToListDTO(List<TaCategoriePreference> entity) {
		List<TaCategoriePreferenceDTO> l = new ArrayList<TaCategoriePreferenceDTO>();

		for (TaCategoriePreference TaCategoriePreference : entity) {
			l.add(entityToDTO(TaCategoriePreference));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCategoriePreferenceDTO> selectAllDTO() {
		System.out.println("List of TaCategoriePreferenceDTO EJB :");
		ArrayList<TaCategoriePreferenceDTO> liste = new ArrayList<TaCategoriePreferenceDTO>();

		List<TaCategoriePreference> projects = selectAll();
		for(TaCategoriePreference project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCategoriePreferenceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCategoriePreferenceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCategoriePreferenceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCategoriePreferenceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCategoriePreferenceDTO dto, String validationContext) throws EJBException {
		try {
			TaCategoriePreferenceMapper mapper = new TaCategoriePreferenceMapper();
			TaCategoriePreference entity = null;
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
	
	public void persistDTO(TaCategoriePreferenceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCategoriePreferenceDTO dto, String validationContext) throws CreateException {
		try {
			TaCategoriePreferenceMapper mapper = new TaCategoriePreferenceMapper();
			TaCategoriePreference entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCategoriePreferenceDTO dto) throws RemoveException {
		try {
			TaCategoriePreferenceMapper mapper = new TaCategoriePreferenceMapper();
			TaCategoriePreference entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCategoriePreference refresh(TaCategoriePreference persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCategoriePreference value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCategoriePreference value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCategoriePreferenceDTO dto, String validationContext) {
		try {
			TaCategoriePreferenceMapper mapper = new TaCategoriePreferenceMapper();
			TaCategoriePreference entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCategoriePreferenceDTO> validator = new BeanValidator<TaCategoriePreferenceDTO>(TaCategoriePreferenceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCategoriePreferenceDTO dto, String propertyName, String validationContext) {
		try {
			TaCategoriePreferenceMapper mapper = new TaCategoriePreferenceMapper();
			TaCategoriePreference entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCategoriePreferenceDTO> validator = new BeanValidator<TaCategoriePreferenceDTO>(TaCategoriePreferenceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCategoriePreferenceDTO dto) {
		validateDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCategoriePreferenceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCategoriePreference value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCategoriePreference value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	

}


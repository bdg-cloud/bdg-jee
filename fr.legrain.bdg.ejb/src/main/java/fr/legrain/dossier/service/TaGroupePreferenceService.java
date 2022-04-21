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
import fr.legrain.bdg.dossier.service.remote.ITaGroupePreferenceServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaGroupePreferenceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaGroupePreferenceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.dossier.dao.IGroupePreferenceDAO;
import fr.legrain.dossier.dao.IPreferencesDAO;
import fr.legrain.dossier.dto.TaGroupePreferenceDTO;
import fr.legrain.dossier.model.TaGroupePreference;
import fr.legrain.dossier.model.TaGroupePreference;
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
public class TaGroupePreferenceService extends AbstractApplicationDAOServer<TaGroupePreference, TaGroupePreferenceDTO> implements ITaGroupePreferenceServiceRemote {

	static Logger logger = Logger.getLogger(TaGroupePreferenceService.class);

	@Inject private IGroupePreferenceDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaGroupePreferenceService() {
		super(TaGroupePreference.class,TaGroupePreferenceDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaGroupePreference a";


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaGroupePreference transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
		
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaGroupePreference transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaGroupePreference persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaGroupePreference merge(TaGroupePreference detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaGroupePreference merge(TaGroupePreference detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaGroupePreference findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaGroupePreference findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaGroupePreference> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaGroupePreferenceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaGroupePreferenceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaGroupePreference> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaGroupePreferenceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaGroupePreferenceDTO entityToDTO(TaGroupePreference entity) {
		TaGroupePreferenceMapper mapper = new TaGroupePreferenceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaGroupePreferenceDTO> listEntityToListDTO(List<TaGroupePreference> entity) {
		List<TaGroupePreferenceDTO> l = new ArrayList<TaGroupePreferenceDTO>();

		for (TaGroupePreference TaGroupePreference : entity) {
			l.add(entityToDTO(TaGroupePreference));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaGroupePreferenceDTO> selectAllDTO() {
		System.out.println("List of TaGroupePreferenceDTO EJB :");
		ArrayList<TaGroupePreferenceDTO> liste = new ArrayList<TaGroupePreferenceDTO>();

		List<TaGroupePreference> projects = selectAll();
		for(TaGroupePreference project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaGroupePreferenceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaGroupePreferenceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaGroupePreferenceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaGroupePreferenceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaGroupePreferenceDTO dto, String validationContext) throws EJBException {
		try {
			TaGroupePreferenceMapper mapper = new TaGroupePreferenceMapper();
			TaGroupePreference entity = null;
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
	
	public void persistDTO(TaGroupePreferenceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaGroupePreferenceDTO dto, String validationContext) throws CreateException {
		try {
			TaGroupePreferenceMapper mapper = new TaGroupePreferenceMapper();
			TaGroupePreference entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaGroupePreferenceDTO dto) throws RemoveException {
		try {
			TaGroupePreferenceMapper mapper = new TaGroupePreferenceMapper();
			TaGroupePreference entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaGroupePreference refresh(TaGroupePreference persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaGroupePreference value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaGroupePreference value, String propertyName, String validationContext) {
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
	public void validateDTO(TaGroupePreferenceDTO dto, String validationContext) {
		try {
			TaGroupePreferenceMapper mapper = new TaGroupePreferenceMapper();
			TaGroupePreference entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaGroupePreferenceDTO> validator = new BeanValidator<TaGroupePreferenceDTO>(TaGroupePreferenceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaGroupePreferenceDTO dto, String propertyName, String validationContext) {
		try {
			TaGroupePreferenceMapper mapper = new TaGroupePreferenceMapper();
			TaGroupePreference entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaGroupePreferenceDTO> validator = new BeanValidator<TaGroupePreferenceDTO>(TaGroupePreferenceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaGroupePreferenceDTO dto) {
		validateDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaGroupePreferenceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaGroupePreference value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaGroupePreference value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	

}


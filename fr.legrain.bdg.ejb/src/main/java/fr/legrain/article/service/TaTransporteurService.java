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
import fr.legrain.article.dao.ITransporteurDAO;
import fr.legrain.article.dto.TaTransporteurDTO;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.article.model.TaTransporteur;
import fr.legrain.bdg.article.service.remote.ITaTransporteurServiceRemote;
import fr.legrain.bdg.article.service.remote.ITaTransporteurServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTransporteurMapper;
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
public class TaTransporteurService extends AbstractApplicationDAOServer<TaTransporteur, TaTransporteurDTO> implements ITaTransporteurServiceRemote {

	static Logger logger = Logger.getLogger(TaTransporteurService.class);

	@Inject private ITransporteurDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTransporteurService() {
		super(TaTransporteur.class,TaTransporteurDTO.class);
	}
	
	public List<TaTransporteurDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTransporteur a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTransporteur transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTransporteur transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
		transientInstance.setCodeTransporteur(transientInstance.getCodeTransporteur().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTransporteur persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdTransporteur()));
	}
	
	public TaTransporteur merge(TaTransporteur detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTransporteur merge(TaTransporteur detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
		detachedInstance.setCodeTransporteur(detachedInstance.getCodeTransporteur().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTransporteur findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTransporteur findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTransporteur> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTransporteurDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTransporteurDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTransporteur> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTransporteurDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTransporteurDTO entityToDTO(TaTransporteur entity) {
		TaTransporteurMapper mapper = new TaTransporteurMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTransporteurDTO> listEntityToListDTO(List<TaTransporteur> entity) {
		List<TaTransporteurDTO> l = new ArrayList<TaTransporteurDTO>();

		for (TaTransporteur TaTransporteur : entity) {
			l.add(entityToDTO(TaTransporteur));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTransporteurDTO> selectAllDTO() {
		System.out.println("List of TaTransporteurDTO EJB :");
		ArrayList<TaTransporteurDTO> liste = new ArrayList<TaTransporteurDTO>();

		List<TaTransporteur> projects = selectAll();
		for(TaTransporteur project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTransporteurDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTransporteurDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTransporteurDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTransporteurDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTransporteurDTO dto, String validationContext) throws EJBException {
		try {
			TaTransporteurMapper mapper = new TaTransporteurMapper();
			TaTransporteur entity = null;
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
	
	public void persistDTO(TaTransporteurDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTransporteurDTO dto, String validationContext) throws CreateException {
		try {
			TaTransporteurMapper mapper = new TaTransporteurMapper();
			TaTransporteur entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTransporteurDTO dto) throws RemoveException {
		try {
			TaTransporteurMapper mapper = new TaTransporteurMapper();
			TaTransporteur entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTransporteur refresh(TaTransporteur persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTransporteur value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTransporteur value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTransporteurDTO dto, String validationContext) {
		try {
			TaTransporteurMapper mapper = new TaTransporteurMapper();
			TaTransporteur entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTransporteurDTO> validator = new BeanValidator<TaTransporteurDTO>(TaTransporteurDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTransporteurDTO dto, String propertyName, String validationContext) {
		try {
			TaTransporteurMapper mapper = new TaTransporteurMapper();
			TaTransporteur entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTransporteurDTO> validator = new BeanValidator<TaTransporteurDTO>(TaTransporteurDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTransporteurDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTransporteurDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTransporteur value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTransporteur value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

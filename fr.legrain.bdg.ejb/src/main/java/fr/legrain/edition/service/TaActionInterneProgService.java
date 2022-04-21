package fr.legrain.edition.service;

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

import fr.legrain.bdg.edition.service.remote.ITaActionInterneProgServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaActionInterneProgMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.edition.dao.IActionInterneProgDAO;
import fr.legrain.edition.dto.TaActionInterneProgDTO;
import fr.legrain.edition.model.TaActionInterneProg;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaActionInterneProgService extends AbstractApplicationDAOServer<TaActionInterneProg, TaActionInterneProgDTO> implements ITaActionInterneProgServiceRemote {

	static Logger logger = Logger.getLogger(TaActionInterneProgService.class);

	@Inject private IActionInterneProgDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaActionInterneProgService() {
		super(TaActionInterneProg.class,TaActionInterneProgDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaActionInterneProg a";
	
//	public List<TaActionInterneProgDTO> findAllLight() {
//		return dao.findAllLight();
//	}
//	
//	public List<TaActionInterneProg> selectAll(int idTiers) {
//		return dao.selectAll(idTiers);
//	}
	
//	public List<TaActionInterneProgDTO> selectAllDTO(int idTiers) {
//		System.out.println("List of TaActionInterneProgDTO EJB :");
//		ArrayList<TaActionInterneProgDTO> liste = new ArrayList<TaActionInterneProgDTO>();
//
//		List<TaActionInterneProg> projects = selectAll(idTiers);
//		for(TaActionInterneProg project : projects) {
//			liste.add(entityToDTO(project));
//		}
//
//		return liste;
//	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaActionInterneProgDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaActionInterneProgDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaActionInterneProg transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaActionInterneProg transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaActionInterneProg persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaActionInterneProg merge(TaActionInterneProg detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaActionInterneProg merge(TaActionInterneProg detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaActionInterneProg findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaActionInterneProg findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaActionInterneProg> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaActionInterneProgDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaActionInterneProgDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaActionInterneProg> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaActionInterneProgDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaActionInterneProgDTO entityToDTO(TaActionInterneProg entity) {
//		TaActionInterneProgDTO dto = new TaActionInterneProgDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaActionInterneProgMapper mapper = new TaActionInterneProgMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaActionInterneProgDTO> listEntityToListDTO(List<TaActionInterneProg> entity) {
		List<TaActionInterneProgDTO> l = new ArrayList<TaActionInterneProgDTO>();

		for (TaActionInterneProg taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaActionInterneProgDTO> selectAllDTO() {
		System.out.println("List of TaActionInterneProgDTO EJB :");
		ArrayList<TaActionInterneProgDTO> liste = new ArrayList<TaActionInterneProgDTO>();

		List<TaActionInterneProg> projects = selectAll();
		for(TaActionInterneProg project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaActionInterneProgDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaActionInterneProgDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaActionInterneProgDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaActionInterneProgDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaActionInterneProgDTO dto, String validationContext) throws EJBException {
		try {
			TaActionInterneProgMapper mapper = new TaActionInterneProgMapper();
			TaActionInterneProg entity = null;
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
	
	public void persistDTO(TaActionInterneProgDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaActionInterneProgDTO dto, String validationContext) throws CreateException {
		try {
			TaActionInterneProgMapper mapper = new TaActionInterneProgMapper();
			TaActionInterneProg entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaActionInterneProgDTO dto) throws RemoveException {
		try {
			TaActionInterneProgMapper mapper = new TaActionInterneProgMapper();
			TaActionInterneProg entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaActionInterneProg refresh(TaActionInterneProg persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaActionInterneProg value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaActionInterneProg value, String propertyName, String validationContext) {
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
	public void validateDTO(TaActionInterneProgDTO dto, String validationContext) {
		try {
			TaActionInterneProgMapper mapper = new TaActionInterneProgMapper();
			TaActionInterneProg entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaActionInterneProgDTO> validator = new BeanValidator<TaActionInterneProgDTO>(TaActionInterneProgDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaActionInterneProgDTO dto, String propertyName, String validationContext) {
		try {
			TaActionInterneProgMapper mapper = new TaActionInterneProgMapper();
			TaActionInterneProg entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaActionInterneProgDTO> validator = new BeanValidator<TaActionInterneProgDTO>(TaActionInterneProgDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaActionInterneProgDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaActionInterneProgDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaActionInterneProg value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaActionInterneProg value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}

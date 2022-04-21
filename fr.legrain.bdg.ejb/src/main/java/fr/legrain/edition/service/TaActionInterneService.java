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

import fr.legrain.bdg.edition.service.remote.ITaActionInterneServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaActionInterneMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.edition.dao.IActionInterneDAO;
import fr.legrain.edition.dto.TaActionInterneDTO;
import fr.legrain.edition.model.TaActionInterne;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaActionInterneService extends AbstractApplicationDAOServer<TaActionInterne, TaActionInterneDTO> implements ITaActionInterneServiceRemote {

	static Logger logger = Logger.getLogger(TaActionInterneService.class);

	@Inject private IActionInterneDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaActionInterneService() {
		super(TaActionInterne.class,TaActionInterneDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaActionInterne a";
	
//	public List<TaActionInterneDTO> findAllLight() {
//		return dao.findAllLight();
//	}
//	
//	public List<TaActionInterne> selectAll(int idTiers) {
//		return dao.selectAll(idTiers);
//	}
	
//	public List<TaActionInterneDTO> selectAllDTO(int idTiers) {
//		System.out.println("List of TaActionInterneDTO EJB :");
//		ArrayList<TaActionInterneDTO> liste = new ArrayList<TaActionInterneDTO>();
//
//		List<TaActionInterne> projects = selectAll(idTiers);
//		for(TaActionInterne project : projects) {
//			liste.add(entityToDTO(project));
//		}
//
//		return liste;
//	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaActionInterneDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaActionInterneDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaActionInterne transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaActionInterne transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaActionInterne persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaActionInterne merge(TaActionInterne detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaActionInterne merge(TaActionInterne detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaActionInterne findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaActionInterne findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaActionInterne> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaActionInterneDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaActionInterneDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaActionInterne> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaActionInterneDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaActionInterneDTO entityToDTO(TaActionInterne entity) {
//		TaActionInterneDTO dto = new TaActionInterneDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaActionInterneMapper mapper = new TaActionInterneMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaActionInterneDTO> listEntityToListDTO(List<TaActionInterne> entity) {
		List<TaActionInterneDTO> l = new ArrayList<TaActionInterneDTO>();

		for (TaActionInterne taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaActionInterneDTO> selectAllDTO() {
		System.out.println("List of TaActionInterneDTO EJB :");
		ArrayList<TaActionInterneDTO> liste = new ArrayList<TaActionInterneDTO>();

		List<TaActionInterne> projects = selectAll();
		for(TaActionInterne project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaActionInterneDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaActionInterneDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaActionInterneDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaActionInterneDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaActionInterneDTO dto, String validationContext) throws EJBException {
		try {
			TaActionInterneMapper mapper = new TaActionInterneMapper();
			TaActionInterne entity = null;
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
	
	public void persistDTO(TaActionInterneDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaActionInterneDTO dto, String validationContext) throws CreateException {
		try {
			TaActionInterneMapper mapper = new TaActionInterneMapper();
			TaActionInterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaActionInterneDTO dto) throws RemoveException {
		try {
			TaActionInterneMapper mapper = new TaActionInterneMapper();
			TaActionInterne entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaActionInterne refresh(TaActionInterne persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaActionInterne value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaActionInterne value, String propertyName, String validationContext) {
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
	public void validateDTO(TaActionInterneDTO dto, String validationContext) {
		try {
			TaActionInterneMapper mapper = new TaActionInterneMapper();
			TaActionInterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaActionInterneDTO> validator = new BeanValidator<TaActionInterneDTO>(TaActionInterneDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaActionInterneDTO dto, String propertyName, String validationContext) {
		try {
			TaActionInterneMapper mapper = new TaActionInterneMapper();
			TaActionInterne entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaActionInterneDTO> validator = new BeanValidator<TaActionInterneDTO>(TaActionInterneDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaActionInterneDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaActionInterneDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaActionInterne value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaActionInterne value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}

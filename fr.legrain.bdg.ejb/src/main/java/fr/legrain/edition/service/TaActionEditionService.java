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

import fr.legrain.bdg.edition.service.remote.ITaActionEditionServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaActionEditionMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.edition.dao.IActionEditionDAO;
import fr.legrain.edition.dto.TaActionEditionDTO;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaActionEditionService extends AbstractApplicationDAOServer<TaActionEdition, TaActionEditionDTO> implements ITaActionEditionServiceRemote {

	static Logger logger = Logger.getLogger(TaActionEditionService.class);

	@Inject private IActionEditionDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaActionEditionService() {
		super(TaActionEdition.class,TaActionEditionDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaActionEdition a";
	
//	public List<TaActionEditionDTO> findAllLight() {
//		return dao.findAllLight();
//	}
//	
//	public List<TaActionEdition> selectAll(int idTiers) {
//		return dao.selectAll(idTiers);
//	}
	
//	public List<TaActionEditionDTO> selectAllDTO(int idTiers) {
//		System.out.println("List of TaActionEditionDTO EJB :");
//		ArrayList<TaActionEditionDTO> liste = new ArrayList<TaActionEditionDTO>();
//
//		List<TaActionEdition> projects = selectAll(idTiers);
//		for(TaActionEdition project : projects) {
//			liste.add(entityToDTO(project));
//		}
//
//		return liste;
//	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	public List<TaActionEditionDTO> selectAllDTOLight() {
//		return dao.selectAllDTOLight();
//	}
//	
//	public List<TaActionEditionDTO> selectAllDTOLight(Date debut, Date fin) {
//		return dao.selectAllDTOLight(debut,fin);
//	}
	
	public void persist(TaActionEdition transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaActionEdition transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaActionEdition persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdActionEdition()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaActionEdition merge(TaActionEdition detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaActionEdition merge(TaActionEdition detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaActionEdition findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaActionEdition findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaActionEdition> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<TaActionEditionDTO> findAllByIdEdtionDTO(int idEdition){
		return dao.findAllByIdEdtionDTO(idEdition);
	}
	public List<TaActionEditionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaActionEditionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaActionEdition> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaActionEditionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaActionEditionDTO entityToDTO(TaActionEdition entity) {
//		TaActionEditionDTO dto = new TaActionEditionDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaActionEditionMapper mapper = new TaActionEditionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaActionEditionDTO> listEntityToListDTO(List<TaActionEdition> entity) {
		List<TaActionEditionDTO> l = new ArrayList<TaActionEditionDTO>();

		for (TaActionEdition taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaActionEditionDTO> selectAllDTO() {
		System.out.println("List of TaActionEditionDTO EJB :");
		ArrayList<TaActionEditionDTO> liste = new ArrayList<TaActionEditionDTO>();

		List<TaActionEdition> projects = selectAll();
		for(TaActionEdition project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaActionEditionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaActionEditionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaActionEditionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaActionEditionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaActionEditionDTO dto, String validationContext) throws EJBException {
		try {
			TaActionEditionMapper mapper = new TaActionEditionMapper();
			TaActionEdition entity = null;
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
	
	public void persistDTO(TaActionEditionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaActionEditionDTO dto, String validationContext) throws CreateException {
		try {
			TaActionEditionMapper mapper = new TaActionEditionMapper();
			TaActionEdition entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaActionEditionDTO dto) throws RemoveException {
		try {
			TaActionEditionMapper mapper = new TaActionEditionMapper();
			TaActionEdition entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaActionEdition refresh(TaActionEdition persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaActionEdition value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaActionEdition value, String propertyName, String validationContext) {
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
	public void validateDTO(TaActionEditionDTO dto, String validationContext) {
		try {
			TaActionEditionMapper mapper = new TaActionEditionMapper();
			TaActionEdition entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaActionEditionDTO> validator = new BeanValidator<TaActionEditionDTO>(TaActionEditionDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaActionEditionDTO dto, String propertyName, String validationContext) {
		try {
			TaActionEditionMapper mapper = new TaActionEditionMapper();
			TaActionEdition entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaActionEditionDTO> validator = new BeanValidator<TaActionEditionDTO>(TaActionEditionDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaActionEditionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaActionEditionDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaActionEdition value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaActionEdition value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
}

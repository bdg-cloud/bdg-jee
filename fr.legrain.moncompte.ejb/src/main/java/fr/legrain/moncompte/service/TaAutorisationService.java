package fr.legrain.moncompte.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.moncompte.service.remote.ITaAutorisationServiceRemote;
import fr.legrain.lib.data.LibConversion;
import fr.legrain.moncompte.dao.IAutorisationDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaAutorisationDTO;
import fr.legrain.moncompte.model.TaAutorisation;
import fr.legrain.moncompte.model.TaSetup;
import fr.legrain.moncompte.model.mapping.mapper.TaAutorisationMapper;


/**
 * Session Bean implementation class TaAutorisationBean
 */
@Stateless
//@DeclareRoles("admin")
@WebService
//@Path("/product")
public class TaAutorisationService extends AbstractApplicationDAOServer<TaAutorisation, TaAutorisationDTO> implements ITaAutorisationServiceRemote {

	static Logger logger = Logger.getLogger(TaAutorisationService.class);

	@Inject private IAutorisationDAO dao;
	
//	@GET
//	@Path("{id}")
//	public TaAutorisation getProductId(@PathParam("id") String id) {
//		System.out.println("TaAutorisationService.getProductId() " +id +" ** "+dao.findById(LibConversion.stringToInteger(id)).getCode());
//		
//		//return Response.status(200).entity("getProductId is called, id : " + id).build();
//		return dao.findById(LibConversion.stringToInteger(id));
//
//	}
//	
	/**
	 * Default constructor. 
	 */
	public TaAutorisationService() {
		super(TaAutorisation.class,TaAutorisationDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaAutorisation a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaAutorisation transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaAutorisation transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaAutorisation persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdAutorisation()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaAutorisation merge(TaAutorisation detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaAutorisation merge(TaAutorisation detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaAutorisation findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaAutorisation findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaAutorisation> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAutorisationDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAutorisationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaAutorisation> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAutorisationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAutorisationDTO entityToDTO(TaAutorisation entity) {
//		TaAutorisationDTO dto = new TaAutorisationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaAutorisationMapper mapper = new TaAutorisationMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAutorisationDTO> listEntityToListDTO(List<TaAutorisation> entity) {
		List<TaAutorisationDTO> l = new ArrayList<TaAutorisationDTO>();

		for (TaAutorisation taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAutorisationDTO> selectAllDTO() {
		System.out.println("List of TaAutorisationDTO EJB :");
		ArrayList<TaAutorisationDTO> liste = new ArrayList<TaAutorisationDTO>();

		List<TaAutorisation> projects = selectAll();
		for(TaAutorisation project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAutorisationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAutorisationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAutorisationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAutorisationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAutorisationDTO dto, String validationContext) throws EJBException {
		try {
			TaAutorisationMapper mapper = new TaAutorisationMapper();
			TaAutorisation entity = null;
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
	
	public void persistDTO(TaAutorisationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAutorisationDTO dto, String validationContext) throws CreateException {
		try {
			TaAutorisationMapper mapper = new TaAutorisationMapper();
			TaAutorisation entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAutorisationDTO dto) throws RemoveException {
		try {
			TaAutorisationMapper mapper = new TaAutorisationMapper();
			TaAutorisation entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaAutorisation refresh(TaAutorisation persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaAutorisation value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaAutorisation value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAutorisationDTO dto, String validationContext) {
		try {
			TaAutorisationMapper mapper = new TaAutorisationMapper();
			TaAutorisation entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAutorisationDTO> validator = new BeanValidator<TaAutorisationDTO>(TaAutorisationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAutorisationDTO dto, String propertyName, String validationContext) {
		try {
			TaAutorisationMapper mapper = new TaAutorisationMapper();
			TaAutorisation entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAutorisationDTO> validator = new BeanValidator<TaAutorisationDTO>(TaAutorisationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAutorisationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAutorisationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaAutorisation value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaAutorisation value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}

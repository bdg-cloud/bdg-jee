package fr.legrain.moncompte.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.moncompte.service.remote.ITaCgvServiceRemote;
import fr.legrain.moncompte.dao.ICgvDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaCgvDTO;
import fr.legrain.moncompte.model.TaCgv;
import fr.legrain.moncompte.model.mapping.mapper.TaCgvMapper;


/**
 * Session Bean implementation class TaCgvBean
 */
@Stateless
@DeclareRoles("admin")
public class TaCgvService extends AbstractApplicationDAOServer<TaCgv, TaCgvDTO> implements ITaCgvServiceRemote {

	static Logger logger = Logger.getLogger(TaCgvService.class);

	@Inject private ICgvDAO dao;


	/**
	 * Default constructor. 
	 */
	public TaCgvService() {
		super(TaCgv.class,TaCgvDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCgv a";
	
	public TaCgv findCgvCourant() {
		return dao.findCgvCourant();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCgv transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCgv transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCgv persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCgv()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCgv merge(TaCgv detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCgv merge(TaCgv detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCgv findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCgv findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCgv> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCgvDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCgvDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCgv> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCgvDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCgvDTO entityToDTO(TaCgv entity) {
//		TaCgvDTO dto = new TaCgvDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCgvMapper mapper = new TaCgvMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCgvDTO> listEntityToListDTO(List<TaCgv> entity) {
		List<TaCgvDTO> l = new ArrayList<TaCgvDTO>();

		for (TaCgv taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCgvDTO> selectAllDTO() {
		System.out.println("List of TaCgvDTO EJB :");
		ArrayList<TaCgvDTO> liste = new ArrayList<TaCgvDTO>();

		List<TaCgv> projects = selectAll();
		for(TaCgv project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCgvDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCgvDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCgvDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCgvDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCgvDTO dto, String validationContext) throws EJBException {
		try {
			TaCgvMapper mapper = new TaCgvMapper();
			TaCgv entity = null;
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
	
	public void persistDTO(TaCgvDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCgvDTO dto, String validationContext) throws CreateException {
		try {
			TaCgvMapper mapper = new TaCgvMapper();
			TaCgv entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCgvDTO dto) throws RemoveException {
		try {
			TaCgvMapper mapper = new TaCgvMapper();
			TaCgv entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCgv refresh(TaCgv persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCgv value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCgv value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCgvDTO dto, String validationContext) {
		try {
			TaCgvMapper mapper = new TaCgvMapper();
			TaCgv entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCgvDTO> validator = new BeanValidator<TaCgvDTO>(TaCgvDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCgvDTO dto, String propertyName, String validationContext) {
		try {
			TaCgvMapper mapper = new TaCgvMapper();
			TaCgv entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCgvDTO> validator = new BeanValidator<TaCgvDTO>(TaCgvDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCgvDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCgvDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCgv value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCgv value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}





}

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

import fr.legrain.bdg.moncompte.service.remote.ITaPrixPersoServiceRemote;
import fr.legrain.moncompte.dao.IPrixPersoDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaPrixPersoDTO;
import fr.legrain.moncompte.model.TaPrixPerso;
import fr.legrain.moncompte.model.mapping.mapper.TaPrixPersoMapper;


/**
 * Session Bean implementation class TaPrixPersoBean
 */
@Stateless
@DeclareRoles("admin")
public class TaPrixPersoService extends AbstractApplicationDAOServer<TaPrixPerso, TaPrixPersoDTO> implements ITaPrixPersoServiceRemote {

	static Logger logger = Logger.getLogger(TaPrixPersoService.class);

	@Inject private IPrixPersoDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaPrixPersoService() {
		super(TaPrixPerso.class,TaPrixPersoDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaPrixPerso a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaPrixPerso transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaPrixPerso transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaPrixPerso persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaPrixPerso merge(TaPrixPerso detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaPrixPerso merge(TaPrixPerso detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaPrixPerso findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaPrixPerso findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaPrixPerso> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPrixPersoDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPrixPersoDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaPrixPerso> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPrixPersoDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPrixPersoDTO entityToDTO(TaPrixPerso entity) {
//		TaPrixPersoDTO dto = new TaPrixPersoDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaPrixPersoMapper mapper = new TaPrixPersoMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaPrixPersoDTO> listEntityToListDTO(List<TaPrixPerso> entity) {
		List<TaPrixPersoDTO> l = new ArrayList<TaPrixPersoDTO>();

		for (TaPrixPerso taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPrixPersoDTO> selectAllDTO() {
		System.out.println("List of TaPrixPersoDTO EJB :");
		ArrayList<TaPrixPersoDTO> liste = new ArrayList<TaPrixPersoDTO>();

		List<TaPrixPerso> projects = selectAll();
		for(TaPrixPerso project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPrixPersoDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPrixPersoDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPrixPersoDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPrixPersoDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPrixPersoDTO dto, String validationContext) throws EJBException {
		try {
			TaPrixPersoMapper mapper = new TaPrixPersoMapper();
			TaPrixPerso entity = null;
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
	
	public void persistDTO(TaPrixPersoDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPrixPersoDTO dto, String validationContext) throws CreateException {
		try {
			TaPrixPersoMapper mapper = new TaPrixPersoMapper();
			TaPrixPerso entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaPrixPersoDTO dto) throws RemoveException {
		try {
			TaPrixPersoMapper mapper = new TaPrixPersoMapper();
			TaPrixPerso entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaPrixPerso refresh(TaPrixPerso persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaPrixPerso value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaPrixPerso value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPrixPersoDTO dto, String validationContext) {
		try {
			TaPrixPersoMapper mapper = new TaPrixPersoMapper();
			TaPrixPerso entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrixPersoDTO> validator = new BeanValidator<TaPrixPersoDTO>(TaPrixPersoDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPrixPersoDTO dto, String propertyName, String validationContext) {
		try {
			TaPrixPersoMapper mapper = new TaPrixPersoMapper();
			TaPrixPerso entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrixPersoDTO> validator = new BeanValidator<TaPrixPersoDTO>(TaPrixPersoDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPrixPersoDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPrixPersoDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaPrixPerso value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaPrixPerso value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}

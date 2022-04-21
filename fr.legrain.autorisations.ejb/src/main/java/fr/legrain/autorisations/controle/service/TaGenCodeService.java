package fr.legrain.autorisations.controle.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.general.service.remote.ITaGenCodeServiceRemote;
import fr.legrain.autorisations.controle.dao.IGenCodeDAO;
import fr.legrain.autorisations.controle.dto.TaGenCodeDTO;
import fr.legrain.autorisations.controle.model.TaGenCode;
import fr.legrain.autorisations.data.AbstractApplicationDAOServer;
import fr.legrain.autorisations.model.mapping.mapper.TaGenCodeMapper;

/**
 * Session Bean implementation class TaGenCodeBean
 */
@Stateless
@DeclareRoles("admin")
public class TaGenCodeService extends AbstractApplicationDAOServer<TaGenCode, TaGenCodeDTO> implements ITaGenCodeServiceRemote {

	static Logger logger = Logger.getLogger(TaGenCodeService.class);

	@Inject private IGenCodeDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaGenCodeService() {
		super(TaGenCode.class,TaGenCodeDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaGenCode a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaGenCode transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
	
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaGenCode transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaGenCode persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaGenCode merge(TaGenCode detachedInstance) {
		return merge(detachedInstance,null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaGenCode merge(TaGenCode detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaGenCode findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaGenCode findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaGenCode> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaGenCodeDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaGenCodeDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaGenCode> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaGenCodeDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaGenCodeDTO entityToDTO(TaGenCode entity) {
//		TaGenCodeDTO dto = new TaGenCodeDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaGenCodeMapper mapper = new TaGenCodeMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaGenCodeDTO> listEntityToListDTO(List<TaGenCode> entity) {
		List<TaGenCodeDTO> l = new ArrayList<TaGenCodeDTO>();

		for (TaGenCode taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaGenCodeDTO> selectAllDTO() {
		System.out.println("List of TaGenCodeDTO EJB :");
		ArrayList<TaGenCodeDTO> liste = new ArrayList<TaGenCodeDTO>();

		List<TaGenCode> projects = selectAll();
		for(TaGenCode project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaGenCodeDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaGenCodeDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaGenCodeDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaGenCodeDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaGenCodeDTO dto, String validationContext) throws EJBException {
		try {
			TaGenCodeMapper mapper = new TaGenCodeMapper();
			TaGenCode entity = null;
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
	
	public void persistDTO(TaGenCodeDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaGenCodeDTO dto, String validationContext) throws CreateException {
		try {
			TaGenCodeMapper mapper = new TaGenCodeMapper();
			TaGenCode entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaGenCodeDTO dto) throws RemoveException {
		try {
			TaGenCodeMapper mapper = new TaGenCodeMapper();
			TaGenCode entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaGenCode refresh(TaGenCode persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEntity(TaGenCode value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaGenCode value, String propertyName, String validationContext) {
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
	public void validateDTO(TaGenCodeDTO dto, String validationContext) {
		try {
			TaGenCodeMapper mapper = new TaGenCodeMapper();
			TaGenCode entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaGenCodeDTO> validator = new BeanValidator<TaGenCodeDTO>(TaGenCodeDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	public void validateDTOProperty(TaGenCodeDTO dto, String propertyName, String validationContext) {
		try {
			TaGenCodeMapper mapper = new TaGenCodeMapper();
			TaGenCode entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaGenCodeDTO> validator = new BeanValidator<TaGenCodeDTO>(TaGenCodeDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}

	@Override
	public void validateDTO(TaGenCodeDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	public void validateDTOProperty(TaGenCodeDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	public void validateEntity(TaGenCode value) {
		validateEntity(value,null);
	}

	@Override
	public void validateEntityProperty(TaGenCode value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

package fr.legrain.autorisations.autorisations.service;

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

import fr.legrain.autorisations.autorisation.dto.TaTypeProduitDTO;
import fr.legrain.autorisations.autorisation.model.TaTypeProduit;
import fr.legrain.autorisations.autorisations.dao.IAutorisationsDAO;
import fr.legrain.autorisations.autorisations.dao.ITypeProduitDAO;
import fr.legrain.autorisations.data.AbstractApplicationDAOServer;
import fr.legrain.autorisations.model.mapping.mapper.TaTypeProduitMapper;
import fr.legrain.bdg.autorisations.service.remote.ITaTypeProduitServiceRemote;

/**
 * Session Bean implementation class TaTypeProduitBean
 */
@Stateless
//@DeclareRoles("admin")
public class TaTypeProduitService extends AbstractApplicationDAOServer<TaTypeProduit, TaTypeProduitDTO> implements ITaTypeProduitServiceRemote {

	static Logger logger = Logger.getLogger(TaTypeProduitService.class);

	@Inject private ITypeProduitDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTypeProduitService() {
		super(TaTypeProduit.class,TaTypeProduitDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTypeProduit a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTypeProduit transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTypeProduit transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTypeProduit persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdType()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTypeProduit merge(TaTypeProduit detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTypeProduit merge(TaTypeProduit detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTypeProduit findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTypeProduit findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTypeProduit> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTypeProduitDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTypeProduitDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTypeProduit> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTypeProduitDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTypeProduitDTO entityToDTO(TaTypeProduit entity) {
//		TaTypeProduitDTO dto = new TaTypeProduitDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTypeProduitMapper mapper = new TaTypeProduitMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTypeProduitDTO> listEntityToListDTO(List<TaTypeProduit> entity) {
		List<TaTypeProduitDTO> l = new ArrayList<TaTypeProduitDTO>();

		for (TaTypeProduit taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTypeProduitDTO> selectAllDTO() {
		System.out.println("List of TaTypeProduitDTO EJB :");
		ArrayList<TaTypeProduitDTO> liste = new ArrayList<TaTypeProduitDTO>();

		List<TaTypeProduit> projects = selectAll();
		for(TaTypeProduit project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTypeProduitDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTypeProduitDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTypeProduitDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTypeProduitDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTypeProduitDTO dto, String validationContext) throws EJBException {
		try {
			TaTypeProduitMapper mapper = new TaTypeProduitMapper();
			TaTypeProduit entity = null;
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
	
	public void persistDTO(TaTypeProduitDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTypeProduitDTO dto, String validationContext) throws CreateException {
		try {
			TaTypeProduitMapper mapper = new TaTypeProduitMapper();
			TaTypeProduit entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTypeProduitDTO dto) throws RemoveException {
		try {
			TaTypeProduitMapper mapper = new TaTypeProduitMapper();
			TaTypeProduit entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTypeProduit refresh(TaTypeProduit persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTypeProduit value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTypeProduit value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTypeProduitDTO dto, String validationContext) {
		try {
			TaTypeProduitMapper mapper = new TaTypeProduitMapper();
			TaTypeProduit entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeProduitDTO> validator = new BeanValidator<TaTypeProduitDTO>(TaTypeProduitDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTypeProduitDTO dto, String propertyName, String validationContext) {
		try {
			TaTypeProduitMapper mapper = new TaTypeProduitMapper();
			TaTypeProduit entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeProduitDTO> validator = new BeanValidator<TaTypeProduitDTO>(TaTypeProduitDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTypeProduitDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTypeProduitDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTypeProduit value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTypeProduit value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

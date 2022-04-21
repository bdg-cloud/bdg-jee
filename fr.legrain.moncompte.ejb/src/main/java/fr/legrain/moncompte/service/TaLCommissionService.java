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

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.moncompte.model.mapping.mapper.TaLCommissionMapper;
import fr.legrain.moncompte.model.mapping.mapper.TaLCommissionMapper;
import fr.legrain.bdg.moncompte.service.remote.ITaLCommissionServiceRemote;
import fr.legrain.bdg.moncompte.service.remote.ITaLPanierServiceRemote;
import fr.legrain.moncompte.dao.ILCommissionDAO;
import fr.legrain.moncompte.dao.ILPanierDAO;
import fr.legrain.moncompte.dao.IPanierDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaLCommissionDTO;
import fr.legrain.moncompte.dto.TaLCommissionDTO;
import fr.legrain.moncompte.model.TaLigneCommission;
import fr.legrain.moncompte.model.TaLigneCommission;


/**
 * Session Bean implementation class TaLigneCommissionBean
 */
@Stateless
//@DeclareRoles("admin")
public class TaLCommissionService extends AbstractApplicationDAOServer<TaLigneCommission, TaLCommissionDTO> implements ITaLCommissionServiceRemote {

	static Logger logger = Logger.getLogger(TaLCommissionService.class);

	@Inject private ILCommissionDAO dao;
	

	/**
	 * Default constructor. 
	 */
	public TaLCommissionService() {
		super(TaLigneCommission.class,TaLCommissionDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLigneCommission a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLigneCommission transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLigneCommission transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLigneCommission persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLigneCommission merge(TaLigneCommission detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLigneCommission merge(TaLigneCommission detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLigneCommission findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLigneCommission findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLigneCommission> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLCommissionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLCommissionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLigneCommission> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLCommissionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLCommissionDTO entityToDTO(TaLigneCommission entity) {
//		TaLCommissionDTO dto = new TaLCommissionDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLCommissionMapper mapper = new TaLCommissionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLCommissionDTO> listEntityToListDTO(List<TaLigneCommission> entity) {
		List<TaLCommissionDTO> l = new ArrayList<TaLCommissionDTO>();

		for (TaLigneCommission taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLCommissionDTO> selectAllDTO() {
		System.out.println("List of TaLCommissionDTO EJB :");
		ArrayList<TaLCommissionDTO> liste = new ArrayList<TaLCommissionDTO>();

		List<TaLigneCommission> projects = selectAll();
		for(TaLigneCommission project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLCommissionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLCommissionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLCommissionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLCommissionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLCommissionDTO dto, String validationContext) throws EJBException {
		try {
			TaLCommissionMapper mapper = new TaLCommissionMapper();
			TaLigneCommission entity = null;
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
	
	public void persistDTO(TaLCommissionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLCommissionDTO dto, String validationContext) throws CreateException {
		try {
			TaLCommissionMapper mapper = new TaLCommissionMapper();
			TaLigneCommission entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLCommissionDTO dto) throws RemoveException {
		try {
			TaLCommissionMapper mapper = new TaLCommissionMapper();
			TaLigneCommission entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLigneCommission refresh(TaLigneCommission persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLigneCommission value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLigneCommission value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLCommissionDTO dto, String validationContext) {
		try {
			TaLCommissionMapper mapper = new TaLCommissionMapper();
			TaLigneCommission entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLCommissionDTO> validator = new BeanValidator<TaLCommissionDTO>(TaLCommissionDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLCommissionDTO dto, String propertyName, String validationContext) {
		try {
			TaLCommissionMapper mapper = new TaLCommissionMapper();
			TaLigneCommission entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLCommissionDTO> validator = new BeanValidator<TaLCommissionDTO>(TaLCommissionDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLCommissionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLCommissionDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLigneCommission value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLigneCommission value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}

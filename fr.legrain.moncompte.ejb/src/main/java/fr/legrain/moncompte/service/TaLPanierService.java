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

import fr.legrain.moncompte.model.mapping.mapper.TaLPanierMapper;
import fr.legrain.bdg.moncompte.service.remote.ITaLPanierServiceRemote;
import fr.legrain.moncompte.dao.ILPanierDAO;
import fr.legrain.moncompte.dao.IPanierDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaLPanierDTO;
import fr.legrain.moncompte.model.TaLignePanier;


/**
 * Session Bean implementation class TaLignePanierBean
 */
@Stateless
//@DeclareRoles("admin")
public class TaLPanierService extends AbstractApplicationDAOServer<TaLignePanier, TaLPanierDTO> implements ITaLPanierServiceRemote {

	static Logger logger = Logger.getLogger(TaLPanierService.class);

	@Inject private ILPanierDAO dao;
	

	/**
	 * Default constructor. 
	 */
	public TaLPanierService() {
		super(TaLignePanier.class,TaLPanierDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLignePanier a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLignePanier transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLignePanier transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLignePanier persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdLignePanier()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaLignePanier merge(TaLignePanier detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLignePanier merge(TaLignePanier detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLignePanier findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLignePanier findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLignePanier> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLPanierDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLPanierDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLignePanier> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLPanierDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLPanierDTO entityToDTO(TaLignePanier entity) {
//		TaLPanierDTO dto = new TaLPanierDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLPanierMapper mapper = new TaLPanierMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLPanierDTO> listEntityToListDTO(List<TaLignePanier> entity) {
		List<TaLPanierDTO> l = new ArrayList<TaLPanierDTO>();

		for (TaLignePanier taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLPanierDTO> selectAllDTO() {
		System.out.println("List of TaLPanierDTO EJB :");
		ArrayList<TaLPanierDTO> liste = new ArrayList<TaLPanierDTO>();

		List<TaLignePanier> projects = selectAll();
		for(TaLignePanier project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLPanierDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLPanierDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLPanierDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLPanierDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLPanierDTO dto, String validationContext) throws EJBException {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLignePanier entity = null;
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
	
	public void persistDTO(TaLPanierDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLPanierDTO dto, String validationContext) throws CreateException {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLignePanier entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLPanierDTO dto) throws RemoveException {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLignePanier entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLignePanier refresh(TaLignePanier persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLignePanier value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLignePanier value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLPanierDTO dto, String validationContext) {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLignePanier entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLPanierDTO> validator = new BeanValidator<TaLPanierDTO>(TaLPanierDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLPanierDTO dto, String propertyName, String validationContext) {
		try {
			TaLPanierMapper mapper = new TaLPanierMapper();
			TaLignePanier entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLPanierDTO> validator = new BeanValidator<TaLPanierDTO>(TaLPanierDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLPanierDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLPanierDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLignePanier value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLignePanier value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}

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

import fr.legrain.moncompte.model.mapping.mapper.TaTypePaiementMapper;
import fr.legrain.bdg.moncompte.service.remote.ITaTypePaiementServiceRemote;
import fr.legrain.moncompte.dao.ITypePaiementDAO;
import fr.legrain.moncompte.dao.ITypeProduitDAO;
import fr.legrain.moncompte.data.AbstractApplicationDAOServer;
import fr.legrain.moncompte.dto.TaTypePaiementDTO;
import fr.legrain.moncompte.model.TaTypePaiement;


/**
 * Session Bean implementation class TaTypePaiementBean
 */
@Stateless
//@DeclareRoles("admin")
public class TaTypePaiementService extends AbstractApplicationDAOServer<TaTypePaiement, TaTypePaiementDTO> implements ITaTypePaiementServiceRemote {

	static Logger logger = Logger.getLogger(TaTypePaiementService.class);

	@Inject private ITypePaiementDAO dao;
	

	/**
	 * Default constructor. 
	 */
	public TaTypePaiementService() {
		super(TaTypePaiement.class,TaTypePaiementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTypePaiement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTypePaiement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTypePaiement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTypePaiement persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTypePaiement()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTypePaiement merge(TaTypePaiement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTypePaiement merge(TaTypePaiement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTypePaiement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTypePaiement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTypePaiement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTypePaiementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTypePaiementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTypePaiement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTypePaiementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTypePaiementDTO entityToDTO(TaTypePaiement entity) {
//		TaTypePaiementDTO dto = new TaTypePaiementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTypePaiementMapper mapper = new TaTypePaiementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTypePaiementDTO> listEntityToListDTO(List<TaTypePaiement> entity) {
		List<TaTypePaiementDTO> l = new ArrayList<TaTypePaiementDTO>();

		for (TaTypePaiement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTypePaiementDTO> selectAllDTO() {
		System.out.println("List of TaTypePaiementDTO EJB :");
		ArrayList<TaTypePaiementDTO> liste = new ArrayList<TaTypePaiementDTO>();

		List<TaTypePaiement> projects = selectAll();
		for(TaTypePaiement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTypePaiementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTypePaiementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTypePaiementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTypePaiementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTypePaiementDTO dto, String validationContext) throws EJBException {
		try {
			TaTypePaiementMapper mapper = new TaTypePaiementMapper();
			TaTypePaiement entity = null;
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
	
	public void persistDTO(TaTypePaiementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTypePaiementDTO dto, String validationContext) throws CreateException {
		try {
			TaTypePaiementMapper mapper = new TaTypePaiementMapper();
			TaTypePaiement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTypePaiementDTO dto) throws RemoveException {
		try {
			TaTypePaiementMapper mapper = new TaTypePaiementMapper();
			TaTypePaiement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTypePaiement refresh(TaTypePaiement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTypePaiement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTypePaiement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTypePaiementDTO dto, String validationContext) {
		try {
			TaTypePaiementMapper mapper = new TaTypePaiementMapper();
			TaTypePaiement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypePaiementDTO> validator = new BeanValidator<TaTypePaiementDTO>(TaTypePaiementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTypePaiementDTO dto, String propertyName, String validationContext) {
		try {
			TaTypePaiementMapper mapper = new TaTypePaiementMapper();
			TaTypePaiement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypePaiementDTO> validator = new BeanValidator<TaTypePaiementDTO>(TaTypePaiementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTypePaiementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTypePaiementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTypePaiement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTypePaiement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}

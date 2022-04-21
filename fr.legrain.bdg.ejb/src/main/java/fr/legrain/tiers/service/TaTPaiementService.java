package fr.legrain.tiers.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaTPaiementMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTPaiementServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaTPaiementDTO;
import fr.legrain.document.model.TaTPaiement;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITPaiementDAO;

/**
 * Session Bean implementation class TaTPaiementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTPaiementService extends AbstractApplicationDAOServer<TaTPaiement, TaTPaiementDTO> implements ITaTPaiementServiceRemote {

	static Logger logger = Logger.getLogger(TaTPaiementService.class);

	@Inject private ITPaiementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTPaiementService() {
		super(TaTPaiement.class,TaTPaiementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTPaiement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTPaiement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTPaiement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTPaiement(transientInstance.getCodeTPaiement().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTPaiement persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTPaiement()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTPaiement merge(TaTPaiement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTPaiement merge(TaTPaiement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTPaiement(detachedInstance.getCodeTPaiement().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTPaiement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTPaiement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTPaiement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTPaiementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTPaiementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTPaiement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTPaiementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTPaiementDTO entityToDTO(TaTPaiement entity) {
//		TaTPaiementDTO dto = new TaTPaiementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTPaiementMapper mapper = new TaTPaiementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTPaiementDTO> listEntityToListDTO(List<TaTPaiement> entity) {
		List<TaTPaiementDTO> l = new ArrayList<TaTPaiementDTO>();

		for (TaTPaiement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTPaiementDTO> selectAllDTO() {
		System.out.println("List of TaTPaiementDTO EJB :");
		ArrayList<TaTPaiementDTO> liste = new ArrayList<TaTPaiementDTO>();

		List<TaTPaiement> projects = selectAll();
		for(TaTPaiement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTPaiementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTPaiementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTPaiementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTPaiementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTPaiementDTO dto, String validationContext) throws EJBException {
		try {
			TaTPaiementMapper mapper = new TaTPaiementMapper();
			TaTPaiement entity = null;
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
	
	public void persistDTO(TaTPaiementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTPaiementDTO dto, String validationContext) throws CreateException {
		try {
			TaTPaiementMapper mapper = new TaTPaiementMapper();
			TaTPaiement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTPaiementDTO dto) throws RemoveException {
		try {
			TaTPaiementMapper mapper = new TaTPaiementMapper();
			TaTPaiement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTPaiement refresh(TaTPaiement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTPaiement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTPaiement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTPaiementDTO dto, String validationContext) {
		try {
			TaTPaiementMapper mapper = new TaTPaiementMapper();
			TaTPaiement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTPaiementDTO> validator = new BeanValidator<TaTPaiementDTO>(TaTPaiementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTPaiementDTO dto, String propertyName, String validationContext) {
		try {
			TaTPaiementMapper mapper = new TaTPaiementMapper();
			TaTPaiement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTPaiementDTO> validator = new BeanValidator<TaTPaiementDTO>(TaTPaiementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTPaiementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTPaiementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTPaiement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTPaiement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

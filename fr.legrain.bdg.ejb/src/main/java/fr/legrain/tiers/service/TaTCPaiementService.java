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
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.model.mapping.mapper.TaTCPaiementMapper;
import fr.legrain.bdg.tiers.service.remote.ITaTCPaiementServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ITCPaiementDAO;
import fr.legrain.tiers.dto.TaTCPaiementDTO;
import fr.legrain.tiers.model.TaTCPaiement;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaTCPaiementService extends AbstractApplicationDAOServer<TaTCPaiement, TaTCPaiementDTO> implements ITaTCPaiementServiceRemote {

	static Logger logger = Logger.getLogger(TaTCPaiementService.class);

	@Inject private ITCPaiementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTCPaiementService() {
		super(TaTCPaiement.class,TaTCPaiementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTCPaiement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTCPaiement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTCPaiement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);
transientInstance.setCodeTCPaiement(transientInstance.getCodeTCPaiement().toUpperCase());
		dao.persist(transientInstance);
	}

	public void remove(TaTCPaiement persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTCPaiement()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTCPaiement merge(TaTCPaiement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTCPaiement merge(TaTCPaiement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);
detachedInstance.setCodeTCPaiement(detachedInstance.getCodeTCPaiement().toUpperCase());
		return dao.merge(detachedInstance);
	}

	public TaTCPaiement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTCPaiement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTCPaiement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTCPaiementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTCPaiementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTCPaiement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTCPaiementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTCPaiementDTO entityToDTO(TaTCPaiement entity) {
		TaTCPaiementMapper mapper = new TaTCPaiementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTCPaiementDTO> listEntityToListDTO(List<TaTCPaiement> entity) {
		List<TaTCPaiementDTO> l = new ArrayList<TaTCPaiementDTO>();

		for (TaTCPaiement TaTCPaiement : entity) {
			l.add(entityToDTO(TaTCPaiement));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTCPaiementDTO> selectAllDTO() {
		System.out.println("List of TaTCPaiementDTO EJB :");
		ArrayList<TaTCPaiementDTO> liste = new ArrayList<TaTCPaiementDTO>();

		List<TaTCPaiement> projects = selectAll();
		for(TaTCPaiement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTCPaiementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTCPaiementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTCPaiementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTCPaiementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTCPaiementDTO dto, String validationContext) throws EJBException {
		try {
			TaTCPaiementMapper mapper = new TaTCPaiementMapper();
			TaTCPaiement entity = null;
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
	
	public void persistDTO(TaTCPaiementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTCPaiementDTO dto, String validationContext) throws CreateException {
		try {
			TaTCPaiementMapper mapper = new TaTCPaiementMapper();
			TaTCPaiement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTCPaiementDTO dto) throws RemoveException {
		try {
			TaTCPaiementMapper mapper = new TaTCPaiementMapper();
			TaTCPaiement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTCPaiement refresh(TaTCPaiement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTCPaiement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTCPaiement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTCPaiementDTO dto, String validationContext) {
		try {
			TaTCPaiementMapper mapper = new TaTCPaiementMapper();
			TaTCPaiement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTCPaiementDTO> validator = new BeanValidator<TaTCPaiementDTO>(TaTCPaiementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTCPaiementDTO dto, String propertyName, String validationContext) {
		try {
			TaTCPaiementMapper mapper = new TaTCPaiementMapper();
			TaTCPaiement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTCPaiementDTO> validator = new BeanValidator<TaTCPaiementDTO>(TaTCPaiementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTCPaiementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTCPaiementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTCPaiement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTCPaiement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

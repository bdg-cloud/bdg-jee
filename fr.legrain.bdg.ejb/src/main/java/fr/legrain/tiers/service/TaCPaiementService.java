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

import fr.legrain.bdg.model.mapping.mapper.TaCPaiementMapper;
import fr.legrain.bdg.tiers.service.remote.ITaCPaiementServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.dao.ICPaiementDAO;
import fr.legrain.tiers.dto.TaCPaiementDTO;
import fr.legrain.tiers.model.TaCPaiement;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaCPaiementService extends AbstractApplicationDAOServer<TaCPaiement, TaCPaiementDTO> implements ITaCPaiementServiceRemote {

	static Logger logger = Logger.getLogger(TaCPaiementService.class);

	@Inject private ICPaiementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaCPaiementService() {
		super(TaCPaiement.class,TaCPaiementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCPaiement a";
	
	public List<TaCPaiementDTO> findAllCPaiementTiers() {
		return dao.findAllCPaiementTiers();
	}
	
	public List<TaCPaiementDTO> findAllCPaiementDoc() {
		return dao.findAllCPaiementDoc();
	}
	
	public List<TaCPaiement> rechercheParType(String codeType) {
		return dao.rechercheParType(codeType);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCPaiement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCPaiement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCPaiement persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCPaiement()));
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public TaCPaiement merge(TaCPaiement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCPaiement merge(TaCPaiement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCPaiement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCPaiement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCPaiement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCPaiementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCPaiementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCPaiement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCPaiementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCPaiementDTO entityToDTO(TaCPaiement entity) {
		TaCPaiementMapper mapper = new TaCPaiementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCPaiementDTO> listEntityToListDTO(List<TaCPaiement> entity) {
		List<TaCPaiementDTO> l = new ArrayList<TaCPaiementDTO>();

		for (TaCPaiement TaCPaiement : entity) {
			l.add(entityToDTO(TaCPaiement));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCPaiementDTO> selectAllDTO() {
		System.out.println("List of TaCPaiementDTO EJB :");
		ArrayList<TaCPaiementDTO> liste = new ArrayList<TaCPaiementDTO>();

		List<TaCPaiement> projects = selectAll();
		for(TaCPaiement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCPaiementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCPaiementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCPaiementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCPaiementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCPaiementDTO dto, String validationContext) throws EJBException {
		try {
			TaCPaiementMapper mapper = new TaCPaiementMapper();
			TaCPaiement entity = null;
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
	
	public void persistDTO(TaCPaiementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCPaiementDTO dto, String validationContext) throws CreateException {
		try {
			TaCPaiementMapper mapper = new TaCPaiementMapper();
			TaCPaiement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCPaiementDTO dto) throws RemoveException {
		try {
			TaCPaiementMapper mapper = new TaCPaiementMapper();
			TaCPaiement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCPaiement refresh(TaCPaiement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCPaiement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCPaiement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCPaiementDTO dto, String validationContext) {
		try {
			TaCPaiementMapper mapper = new TaCPaiementMapper();
			TaCPaiement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCPaiementDTO> validator = new BeanValidator<TaCPaiementDTO>(TaCPaiementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCPaiementDTO dto, String propertyName, String validationContext) {
		try {
			TaCPaiementMapper mapper = new TaCPaiementMapper();
			TaCPaiement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCPaiementDTO> validator = new BeanValidator<TaCPaiementDTO>(TaCPaiementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCPaiementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCPaiementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCPaiement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCPaiement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

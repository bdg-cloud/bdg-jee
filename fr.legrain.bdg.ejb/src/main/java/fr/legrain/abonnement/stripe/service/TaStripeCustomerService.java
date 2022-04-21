package fr.legrain.abonnement.stripe.service;

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

import fr.legrain.abonnement.stripe.dao.IStripeCustomerDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeCustomerDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCustomer;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeCustomerServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeCustomerMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaTiers;


/**
 * Session Bean implementation class TaStripeCustomerBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeCustomerService extends AbstractApplicationDAOServer<TaStripeCustomer, TaStripeCustomerDTO> implements ITaStripeCustomerServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeCustomerService.class);

	@Inject private IStripeCustomerDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeCustomerService() {
		super(TaStripeCustomer.class,TaStripeCustomerDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeCustomer a";
	
	public List<TaStripeCustomerDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeCustomerDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public TaStripeCustomer rechercherCustomer(TaTiers tiers) {
		return dao.rechercherCustomer(tiers);
	}
	public TaStripeCustomer rechercherCustomer(String idExterne){
		return dao.rechercherCustomer(idExterne);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeCustomer transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeCustomer transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeCustomer persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeCustomer()));
	}
	
	public TaStripeCustomer merge(TaStripeCustomer detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeCustomer merge(TaStripeCustomer detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeCustomer findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeCustomer findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeCustomer> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeCustomerDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeCustomerDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeCustomer> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeCustomerDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeCustomerDTO entityToDTO(TaStripeCustomer entity) {
//		TaStripeCustomerDTO dto = new TaStripeCustomerDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeCustomerMapper mapper = new TaStripeCustomerMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeCustomerDTO> listEntityToListDTO(List<TaStripeCustomer> entity) {
		List<TaStripeCustomerDTO> l = new ArrayList<TaStripeCustomerDTO>();

		for (TaStripeCustomer taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeCustomerDTO> selectAllDTO() {
		System.out.println("List of TaStripeCustomerDTO EJB :");
		ArrayList<TaStripeCustomerDTO> liste = new ArrayList<TaStripeCustomerDTO>();

		List<TaStripeCustomer> projects = selectAll();
		for(TaStripeCustomer project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeCustomerDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeCustomerDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeCustomerDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeCustomerDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeCustomerDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeCustomerMapper mapper = new TaStripeCustomerMapper();
			TaStripeCustomer entity = null;
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
	
	public void persistDTO(TaStripeCustomerDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeCustomerDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeCustomerMapper mapper = new TaStripeCustomerMapper();
			TaStripeCustomer entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeCustomerDTO dto) throws RemoveException {
		try {
			TaStripeCustomerMapper mapper = new TaStripeCustomerMapper();
			TaStripeCustomer entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeCustomer refresh(TaStripeCustomer persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeCustomer value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeCustomer value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeCustomerDTO dto, String validationContext) {
		try {
			TaStripeCustomerMapper mapper = new TaStripeCustomerMapper();
			TaStripeCustomer entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeCustomerDTO> validator = new BeanValidator<TaStripeCustomerDTO>(TaStripeCustomerDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeCustomerDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeCustomerMapper mapper = new TaStripeCustomerMapper();
			TaStripeCustomer entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeCustomerDTO> validator = new BeanValidator<TaStripeCustomerDTO>(TaStripeCustomerDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeCustomerDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeCustomerDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeCustomer value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeCustomer value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

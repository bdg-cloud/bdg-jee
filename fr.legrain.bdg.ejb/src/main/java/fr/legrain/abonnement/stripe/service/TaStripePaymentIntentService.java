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

import fr.legrain.abonnement.stripe.dao.IStripePaymentIntentDAO;
import fr.legrain.abonnement.stripe.dto.TaStripePaymentIntentDTO;
import fr.legrain.abonnement.stripe.model.TaStripePaymentIntent;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaymentIntentServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripePaymentIntentMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.SWTDocument;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripePaymentIntentBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripePaymentIntentService extends AbstractApplicationDAOServer<TaStripePaymentIntent, TaStripePaymentIntentDTO> implements ITaStripePaymentIntentServiceRemote {

	static Logger logger = Logger.getLogger(TaStripePaymentIntentService.class);

	@Inject private IStripePaymentIntentDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripePaymentIntentService() {
		super(TaStripePaymentIntent.class,TaStripePaymentIntentDTO.class);
	}
	
	public TaStripePaymentIntent findPaiementNonCapture(SWTDocument doc) {
		return dao.findPaiementNonCapture(doc);
	}
	
	public List<TaStripePaymentIntentDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripePaymentIntentDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaStripePaymentIntentDTO> rechercherPaymentIntentCustomerDTO(String idExterneCustomer) {
		return dao.rechercherPaymentIntentCustomerDTO(idExterneCustomer);
	}
	public List<TaStripePaymentIntent> rechercherPaymentIntentCustomer(String idExterneCustomer) {
		return dao.rechercherPaymentIntentCustomer(idExterneCustomer);
	}
	
	public List<TaStripePaymentIntentDTO> rechercherPaymentIntentDTO() {
		return dao.rechercherPaymentIntentDTO();
	}
	public List<TaStripePaymentIntent> rechercherPaymentIntent() {
		return dao.rechercherPaymentIntent();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripePaymentIntent transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripePaymentIntent transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripePaymentIntent persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripePaymentIntent()));
	}
	
	public TaStripePaymentIntent merge(TaStripePaymentIntent detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripePaymentIntent merge(TaStripePaymentIntent detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripePaymentIntent findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripePaymentIntent findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripePaymentIntent> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripePaymentIntentDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripePaymentIntentDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripePaymentIntent> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripePaymentIntentDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripePaymentIntentDTO entityToDTO(TaStripePaymentIntent entity) {
//		TaStripePaymentIntentDTO dto = new TaStripePaymentIntentDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripePaymentIntentMapper mapper = new TaStripePaymentIntentMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripePaymentIntentDTO> listEntityToListDTO(List<TaStripePaymentIntent> entity) {
		List<TaStripePaymentIntentDTO> l = new ArrayList<TaStripePaymentIntentDTO>();

		for (TaStripePaymentIntent taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripePaymentIntentDTO> selectAllDTO() {
		System.out.println("List of TaStripePaymentIntentDTO EJB :");
		ArrayList<TaStripePaymentIntentDTO> liste = new ArrayList<TaStripePaymentIntentDTO>();

		List<TaStripePaymentIntent> projects = selectAll();
		for(TaStripePaymentIntent project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripePaymentIntentDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripePaymentIntentDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripePaymentIntentDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripePaymentIntentDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripePaymentIntentDTO dto, String validationContext) throws EJBException {
		try {
			TaStripePaymentIntentMapper mapper = new TaStripePaymentIntentMapper();
			TaStripePaymentIntent entity = null;
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
	
	public void persistDTO(TaStripePaymentIntentDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripePaymentIntentDTO dto, String validationContext) throws CreateException {
		try {
			TaStripePaymentIntentMapper mapper = new TaStripePaymentIntentMapper();
			TaStripePaymentIntent entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripePaymentIntentDTO dto) throws RemoveException {
		try {
			TaStripePaymentIntentMapper mapper = new TaStripePaymentIntentMapper();
			TaStripePaymentIntent entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripePaymentIntent refresh(TaStripePaymentIntent persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripePaymentIntent value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripePaymentIntent value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripePaymentIntentDTO dto, String validationContext) {
		try {
			TaStripePaymentIntentMapper mapper = new TaStripePaymentIntentMapper();
			TaStripePaymentIntent entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripePaymentIntentDTO> validator = new BeanValidator<TaStripePaymentIntentDTO>(TaStripePaymentIntentDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripePaymentIntentDTO dto, String propertyName, String validationContext) {
		try {
			TaStripePaymentIntentMapper mapper = new TaStripePaymentIntentMapper();
			TaStripePaymentIntent entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripePaymentIntentDTO> validator = new BeanValidator<TaStripePaymentIntentDTO>(TaStripePaymentIntentDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripePaymentIntentDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripePaymentIntentDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripePaymentIntent value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripePaymentIntent value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

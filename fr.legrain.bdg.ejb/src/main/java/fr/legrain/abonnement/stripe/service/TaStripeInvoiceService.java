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

import fr.legrain.abonnement.stripe.dao.IStripeInvoiceDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeInvoiceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeInvoice;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeInvoiceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeInvoiceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripeInvoiceBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeInvoiceService extends AbstractApplicationDAOServer<TaStripeInvoice, TaStripeInvoiceDTO> implements ITaStripeInvoiceServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeInvoiceService.class);

	@Inject private IStripeInvoiceDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeInvoiceService() {
		super(TaStripeInvoice.class,TaStripeInvoiceDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeInvoice a";
	
	public List<TaStripeInvoiceDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeInvoiceDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForSubscriptionDTO(String subscriptionStripeID, Integer limite) {
		return dao.rechercheInvoiceStripeForSubscriptionDTO(subscriptionStripeID,limite);
	}
	public List<TaStripeInvoice> rechercheInvoiceStripeForSubscription(String subscriptionStripeID, Integer limite) {
		return dao.rechercheInvoiceStripeForSubscription(subscriptionStripeID,limite);
	}
	
	public List<TaStripeInvoiceDTO> rechercheInvoiceStripeForCustomerDTO(String customerStripeID, Integer limite) {
		return dao.rechercheInvoiceStripeForCustomerDTO(customerStripeID,limite);
	}
	public List<TaStripeInvoice> rechercheInvoiceStripeForCustomer(String customerStripeID, Integer limite) {
		return dao.rechercheInvoiceStripeForCustomer(customerStripeID,limite);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeInvoice transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeInvoice transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeInvoice persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeInvoice()));
	}
	
	public TaStripeInvoice merge(TaStripeInvoice detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeInvoice merge(TaStripeInvoice detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeInvoice findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeInvoice findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeInvoice> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeInvoiceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeInvoiceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeInvoice> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeInvoiceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeInvoiceDTO entityToDTO(TaStripeInvoice entity) {
//		TaStripeInvoiceDTO dto = new TaStripeInvoiceDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeInvoiceMapper mapper = new TaStripeInvoiceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeInvoiceDTO> listEntityToListDTO(List<TaStripeInvoice> entity) {
		List<TaStripeInvoiceDTO> l = new ArrayList<TaStripeInvoiceDTO>();

		for (TaStripeInvoice taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeInvoiceDTO> selectAllDTO() {
		System.out.println("List of TaStripeInvoiceDTO EJB :");
		ArrayList<TaStripeInvoiceDTO> liste = new ArrayList<TaStripeInvoiceDTO>();

		List<TaStripeInvoice> projects = selectAll();
		for(TaStripeInvoice project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeInvoiceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeInvoiceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeInvoiceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeInvoiceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeInvoiceDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeInvoiceMapper mapper = new TaStripeInvoiceMapper();
			TaStripeInvoice entity = null;
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
	
	public void persistDTO(TaStripeInvoiceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeInvoiceDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeInvoiceMapper mapper = new TaStripeInvoiceMapper();
			TaStripeInvoice entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeInvoiceDTO dto) throws RemoveException {
		try {
			TaStripeInvoiceMapper mapper = new TaStripeInvoiceMapper();
			TaStripeInvoice entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeInvoice refresh(TaStripeInvoice persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeInvoice value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeInvoice value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeInvoiceDTO dto, String validationContext) {
		try {
			TaStripeInvoiceMapper mapper = new TaStripeInvoiceMapper();
			TaStripeInvoice entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeInvoiceDTO> validator = new BeanValidator<TaStripeInvoiceDTO>(TaStripeInvoiceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeInvoiceDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeInvoiceMapper mapper = new TaStripeInvoiceMapper();
			TaStripeInvoice entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeInvoiceDTO> validator = new BeanValidator<TaStripeInvoiceDTO>(TaStripeInvoiceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeInvoiceDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeInvoiceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeInvoice value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeInvoice value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

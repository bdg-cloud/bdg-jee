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

import fr.legrain.abonnement.stripe.dao.IStripeChargeDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeChargeDTO;
import fr.legrain.abonnement.stripe.model.TaStripeCharge;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeChargeServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeChargeMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripeChargeBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeChargeService extends AbstractApplicationDAOServer<TaStripeCharge, TaStripeChargeDTO> implements ITaStripeChargeServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeChargeService.class);

	@Inject private IStripeChargeDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeChargeService() {
		super(TaStripeCharge.class,TaStripeChargeDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeCharge a";
	
	public List<TaStripeChargeDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeChargeDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public List<TaStripeChargeDTO> rechercherChargeCustomerDTO(String idExterneCustomer) {
		return dao.rechercherChargeCustomerDTO(idExterneCustomer);
	}
	public List<TaStripeCharge> rechercherChargeCustomer(String idExterneCustomer) {
		return dao.rechercherChargeCustomer(idExterneCustomer);
	}
	
	public List<TaStripeChargeDTO> rechercherChargeDTO() {
		return dao.rechercherChargeDTO();
	}
	public List<TaStripeCharge> rechercherCharge() {
		return dao.rechercherCharge();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeCharge transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeCharge transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeCharge persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeCharge()));
	}
	
	public TaStripeCharge merge(TaStripeCharge detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeCharge merge(TaStripeCharge detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeCharge findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeCharge findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeCharge> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeChargeDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeChargeDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeCharge> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeChargeDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeChargeDTO entityToDTO(TaStripeCharge entity) {
//		TaStripeChargeDTO dto = new TaStripeChargeDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeChargeMapper mapper = new TaStripeChargeMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeChargeDTO> listEntityToListDTO(List<TaStripeCharge> entity) {
		List<TaStripeChargeDTO> l = new ArrayList<TaStripeChargeDTO>();

		for (TaStripeCharge taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeChargeDTO> selectAllDTO() {
		System.out.println("List of TaStripeChargeDTO EJB :");
		ArrayList<TaStripeChargeDTO> liste = new ArrayList<TaStripeChargeDTO>();

		List<TaStripeCharge> projects = selectAll();
		for(TaStripeCharge project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeChargeDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeChargeDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeChargeDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeChargeDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeChargeDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeChargeMapper mapper = new TaStripeChargeMapper();
			TaStripeCharge entity = null;
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
	
	public void persistDTO(TaStripeChargeDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeChargeDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeChargeMapper mapper = new TaStripeChargeMapper();
			TaStripeCharge entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeChargeDTO dto) throws RemoveException {
		try {
			TaStripeChargeMapper mapper = new TaStripeChargeMapper();
			TaStripeCharge entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeCharge refresh(TaStripeCharge persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeCharge value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeCharge value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeChargeDTO dto, String validationContext) {
		try {
			TaStripeChargeMapper mapper = new TaStripeChargeMapper();
			TaStripeCharge entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeChargeDTO> validator = new BeanValidator<TaStripeChargeDTO>(TaStripeChargeDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeChargeDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeChargeMapper mapper = new TaStripeChargeMapper();
			TaStripeCharge entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeChargeDTO> validator = new BeanValidator<TaStripeChargeDTO>(TaStripeChargeDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeChargeDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeChargeDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeCharge value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeCharge value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

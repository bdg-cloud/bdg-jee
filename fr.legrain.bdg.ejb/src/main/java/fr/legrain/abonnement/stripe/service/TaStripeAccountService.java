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

import fr.legrain.abonnement.stripe.dao.IStripeAccountDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeAccount;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeAccountServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeAccountMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripeAccountBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeAccountService extends AbstractApplicationDAOServer<TaStripeAccount, TaStripeAccountDTO> implements ITaStripeAccountServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeAccountService.class);

	@Inject private IStripeAccountDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeAccountService() {
		super(TaStripeAccount.class,TaStripeAccountDTO.class);
	}
	
	public TaStripeAccount findInstance() {
		return dao.findInstance();
	}
	
	public List<TaStripeAccountDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeAccountDTO> findAllLight() {
		return dao.findAllLight();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeAccount transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeAccount transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeAccount persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeAccount()));
	}
	
	public TaStripeAccount merge(TaStripeAccount detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeAccount merge(TaStripeAccount detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeAccount findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeAccount findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeAccount> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeAccountDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeAccountDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeAccount> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeAccountDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeAccountDTO entityToDTO(TaStripeAccount entity) {
//		TaStripeAccountDTO dto = new TaStripeAccountDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeAccountMapper mapper = new TaStripeAccountMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeAccountDTO> listEntityToListDTO(List<TaStripeAccount> entity) {
		List<TaStripeAccountDTO> l = new ArrayList<TaStripeAccountDTO>();

		for (TaStripeAccount taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeAccountDTO> selectAllDTO() {
		System.out.println("List of TaStripeAccountDTO EJB :");
		ArrayList<TaStripeAccountDTO> liste = new ArrayList<TaStripeAccountDTO>();

		List<TaStripeAccount> projects = selectAll();
		for(TaStripeAccount project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeAccountDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeAccountDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeAccountDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeAccountDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeAccountDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeAccountMapper mapper = new TaStripeAccountMapper();
			TaStripeAccount entity = null;
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
	
	public void persistDTO(TaStripeAccountDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeAccountDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeAccountMapper mapper = new TaStripeAccountMapper();
			TaStripeAccount entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeAccountDTO dto) throws RemoveException {
		try {
			TaStripeAccountMapper mapper = new TaStripeAccountMapper();
			TaStripeAccount entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeAccount refresh(TaStripeAccount persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeAccount value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeAccount value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeAccountDTO dto, String validationContext) {
		try {
			TaStripeAccountMapper mapper = new TaStripeAccountMapper();
			TaStripeAccount entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeAccountDTO> validator = new BeanValidator<TaStripeAccountDTO>(TaStripeAccountDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeAccountDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeAccountMapper mapper = new TaStripeAccountMapper();
			TaStripeAccount entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeAccountDTO> validator = new BeanValidator<TaStripeAccountDTO>(TaStripeAccountDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeAccountDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeAccountDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeAccount value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeAccount value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

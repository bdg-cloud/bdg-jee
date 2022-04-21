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

import fr.legrain.abonnement.stripe.dao.IStripeBankAccountDAO;
import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeBankAccountDTO;
import fr.legrain.abonnement.stripe.model.TaStripeBankAccount;
import fr.legrain.abonnement.stripe.model.TaStripeBankAccount;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeBankAccountServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeBankAccountMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaStripeBankAccountBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeBankAccountService extends AbstractApplicationDAOServer<TaStripeBankAccount, TaStripeBankAccountDTO> implements ITaStripeBankAccountServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeBankAccountService.class);

	@Inject private IStripeBankAccountDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeBankAccountService() {
		super(TaStripeBankAccount.class,TaStripeBankAccountDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeBankAccount a";
	
	public List<TaStripeBankAccountDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeBankAccountDTO> findAllLight() {
		return dao.findAllLight();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeBankAccount transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeBankAccount transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeBankAccount persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeBankAccount()));
	}
	
	public TaStripeBankAccount merge(TaStripeBankAccount detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeBankAccount merge(TaStripeBankAccount detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeBankAccount findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeBankAccount findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeBankAccount> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeBankAccountDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeBankAccountDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeBankAccount> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeBankAccountDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeBankAccountDTO entityToDTO(TaStripeBankAccount entity) {
//		TaStripeBankAccountDTO dto = new TaStripeBankAccountDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeBankAccountMapper mapper = new TaStripeBankAccountMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeBankAccountDTO> listEntityToListDTO(List<TaStripeBankAccount> entity) {
		List<TaStripeBankAccountDTO> l = new ArrayList<TaStripeBankAccountDTO>();

		for (TaStripeBankAccount taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeBankAccountDTO> selectAllDTO() {
		System.out.println("List of TaStripeBankAccountDTO EJB :");
		ArrayList<TaStripeBankAccountDTO> liste = new ArrayList<TaStripeBankAccountDTO>();

		List<TaStripeBankAccount> projects = selectAll();
		for(TaStripeBankAccount project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeBankAccountDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeBankAccountDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeBankAccountDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeBankAccountDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeBankAccountDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeBankAccountMapper mapper = new TaStripeBankAccountMapper();
			TaStripeBankAccount entity = null;
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
	
	public void persistDTO(TaStripeBankAccountDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeBankAccountDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeBankAccountMapper mapper = new TaStripeBankAccountMapper();
			TaStripeBankAccount entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeBankAccountDTO dto) throws RemoveException {
		try {
			TaStripeBankAccountMapper mapper = new TaStripeBankAccountMapper();
			TaStripeBankAccount entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeBankAccount refresh(TaStripeBankAccount persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeBankAccount value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeBankAccount value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeBankAccountDTO dto, String validationContext) {
		try {
			TaStripeBankAccountMapper mapper = new TaStripeBankAccountMapper();
			TaStripeBankAccount entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeBankAccountDTO> validator = new BeanValidator<TaStripeBankAccountDTO>(TaStripeBankAccountDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeBankAccountDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeBankAccountMapper mapper = new TaStripeBankAccountMapper();
			TaStripeBankAccount entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeBankAccountDTO> validator = new BeanValidator<TaStripeBankAccountDTO>(TaStripeBankAccountDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeBankAccountDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeBankAccountDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeBankAccount value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeBankAccount value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

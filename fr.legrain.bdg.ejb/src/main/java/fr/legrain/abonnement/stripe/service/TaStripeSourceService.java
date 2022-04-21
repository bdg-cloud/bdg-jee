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

import fr.legrain.abonnement.stripe.dao.IStripeProductDAO;
import fr.legrain.abonnement.stripe.dao.IStripeSourceDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeSource;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeSourceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeSourceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;


/**
 * Session Bean implementation class TaStripeSourceBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeSourceService extends AbstractApplicationDAOServer<TaStripeSource, TaStripeSourceDTO> implements ITaStripeSourceServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeSourceService.class);

	@Inject private IStripeSourceDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeSourceService() {
		super(TaStripeSource.class,TaStripeSourceDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeSource a";
	
	public List<TaStripeSourceDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeSourceDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public TaStripeSource rechercherSource(TaCompteBanque compteBanque) {
		return dao.rechercherSource(compteBanque);
	}
	public TaStripeSource rechercherSource(TaCarteBancaire carteBancaire) {
		return dao.rechercherSource(carteBancaire);
	}
	public List<TaStripeSource> rechercherSource(TaTiers tiers) {
		return dao.rechercherSource(tiers);
	}
	public List<TaStripeSource> rechercherSourceCustomer(String idExterneCustomer) {
		return dao.rechercherSourceCustomer(idExterneCustomer);
	}
	public TaStripeSource rechercherSource(String idExterneSource) {
		return dao.rechercherSource(idExterneSource);
	}
	
	public List<TaStripeSourceDTO> rechercherSourceCustomerDTO(String idExterneCustomer) {
		return dao.rechercherSourceCustomerDTO(idExterneCustomer);
	}
	
	public TaStripeSource rechercherSourceManuelle(String codeStripeTSource) {
		return dao.rechercherSourceManuelle(codeStripeTSource);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeSource transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeSource transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeSource persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeSource()));
	}
	
	public TaStripeSource merge(TaStripeSource detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeSource merge(TaStripeSource detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeSource findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeSource findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeSource> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeSourceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeSourceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeSource> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeSourceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeSourceDTO entityToDTO(TaStripeSource entity) {
//		TaStripeSourceDTO dto = new TaStripeSourceDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeSourceMapper mapper = new TaStripeSourceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeSourceDTO> listEntityToListDTO(List<TaStripeSource> entity) {
		List<TaStripeSourceDTO> l = new ArrayList<TaStripeSourceDTO>();

		for (TaStripeSource taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeSourceDTO> selectAllDTO() {
		System.out.println("List of TaStripeSourceDTO EJB :");
		ArrayList<TaStripeSourceDTO> liste = new ArrayList<TaStripeSourceDTO>();

		List<TaStripeSource> projects = selectAll();
		for(TaStripeSource project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeSourceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeSourceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeSourceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeSourceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeSourceDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeSourceMapper mapper = new TaStripeSourceMapper();
			TaStripeSource entity = null;
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
	
	public void persistDTO(TaStripeSourceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeSourceDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeSourceMapper mapper = new TaStripeSourceMapper();
			TaStripeSource entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeSourceDTO dto) throws RemoveException {
		try {
			TaStripeSourceMapper mapper = new TaStripeSourceMapper();
			TaStripeSource entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeSource refresh(TaStripeSource persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeSource value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeSource value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeSourceDTO dto, String validationContext) {
		try {
			TaStripeSourceMapper mapper = new TaStripeSourceMapper();
			TaStripeSource entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeSourceDTO> validator = new BeanValidator<TaStripeSourceDTO>(TaStripeSourceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeSourceDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeSourceMapper mapper = new TaStripeSourceMapper();
			TaStripeSource entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeSourceDTO> validator = new BeanValidator<TaStripeSourceDTO>(TaStripeSourceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeSourceDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeSourceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeSource value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeSource value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

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

import fr.legrain.abonnement.stripe.dao.IStripeTSourceDAO;
import fr.legrain.abonnement.stripe.dto.TaStripeTSourceDTO;
import fr.legrain.abonnement.stripe.model.TaStripeTSource;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripeTSourceServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaStripeTSourceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tiers.model.TaCarteBancaire;
import fr.legrain.tiers.model.TaCompteBanque;
import fr.legrain.tiers.model.TaTiers;


/**
 * Session Bean implementation class TaStripeTSourceBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaStripeTSourceService extends AbstractApplicationDAOServer<TaStripeTSource, TaStripeTSourceDTO> implements ITaStripeTSourceServiceRemote {

	static Logger logger = Logger.getLogger(TaStripeTSourceService.class);

	@Inject private IStripeTSourceDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaStripeTSourceService() {
		super(TaStripeTSource.class,TaStripeTSourceDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaStripeTSource a";
	
	public List<TaStripeTSourceDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}
	
	public List<TaStripeTSourceDTO> findAllLight() {
		return dao.findAllLight();
	}
	
	public TaStripeTSource rechercherSource(TaCompteBanque compteBanque) {
		return dao.rechercherSource(compteBanque);
	}
	public TaStripeTSource rechercherSource(TaCarteBancaire carteBancaire) {
		return dao.rechercherSource(carteBancaire);
	}
	public List<TaStripeTSource> rechercherSource(TaTiers tiers) {
		return dao.rechercherSource(tiers);
	}
	public List<TaStripeTSource> rechercherSourceCustomer(String idExterneCustomer) {
		return dao.rechercherSourceCustomer(idExterneCustomer);
	}
	public TaStripeTSource rechercherSource(String idExterneSource) {
		return dao.rechercherSource(idExterneSource);
	}
	
	public List<TaStripeTSourceDTO> rechercherSourceCustomerDTO(String idExterneCustomer) {
		return dao.rechercherSourceCustomerDTO(idExterneCustomer);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaStripeTSource transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaStripeTSource transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaStripeTSource persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdStripeTSource()));
	}
	
	public TaStripeTSource merge(TaStripeTSource detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaStripeTSource merge(TaStripeTSource detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaStripeTSource findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaStripeTSource findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaStripeTSource> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaStripeTSourceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaStripeTSourceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaStripeTSource> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaStripeTSourceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaStripeTSourceDTO entityToDTO(TaStripeTSource entity) {
//		TaStripeTSourceDTO dto = new TaStripeTSourceDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaStripeTSourceMapper mapper = new TaStripeTSourceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaStripeTSourceDTO> listEntityToListDTO(List<TaStripeTSource> entity) {
		List<TaStripeTSourceDTO> l = new ArrayList<TaStripeTSourceDTO>();

		for (TaStripeTSource taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaStripeTSourceDTO> selectAllDTO() {
		System.out.println("List of TaStripeTSourceDTO EJB :");
		ArrayList<TaStripeTSourceDTO> liste = new ArrayList<TaStripeTSourceDTO>();

		List<TaStripeTSource> projects = selectAll();
		for(TaStripeTSource project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaStripeTSourceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaStripeTSourceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaStripeTSourceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaStripeTSourceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaStripeTSourceDTO dto, String validationContext) throws EJBException {
		try {
			TaStripeTSourceMapper mapper = new TaStripeTSourceMapper();
			TaStripeTSource entity = null;
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
	
	public void persistDTO(TaStripeTSourceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaStripeTSourceDTO dto, String validationContext) throws CreateException {
		try {
			TaStripeTSourceMapper mapper = new TaStripeTSourceMapper();
			TaStripeTSource entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaStripeTSourceDTO dto) throws RemoveException {
		try {
			TaStripeTSourceMapper mapper = new TaStripeTSourceMapper();
			TaStripeTSource entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaStripeTSource refresh(TaStripeTSource persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaStripeTSource value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaStripeTSource value, String propertyName, String validationContext) {
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
	public void validateDTO(TaStripeTSourceDTO dto, String validationContext) {
		try {
			TaStripeTSourceMapper mapper = new TaStripeTSourceMapper();
			TaStripeTSource entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeTSourceDTO> validator = new BeanValidator<TaStripeTSourceDTO>(TaStripeTSourceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaStripeTSourceDTO dto, String propertyName, String validationContext) {
		try {
			TaStripeTSourceMapper mapper = new TaStripeTSourceMapper();
			TaStripeTSource entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaStripeTSourceDTO> validator = new BeanValidator<TaStripeTSourceDTO>(TaStripeTSourceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaStripeTSourceDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaStripeTSourceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaStripeTSource value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaStripeTSource value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

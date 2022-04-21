package fr.legrain.caisse.service;

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

import fr.legrain.article.dao.ITvaDAO;
import fr.legrain.bdg.caisse.service.remote.ITaDepotRetraitCaisseServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaDepotRetraitCaisseMapper;
import fr.legrain.caisse.dao.IDepotRetraitCaisseDAO;
import fr.legrain.caisse.dto.TaDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaDepotRetraitCaisse;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaDepotRetraitCaisseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaDepotRetraitCaisseService extends AbstractApplicationDAOServer<TaDepotRetraitCaisse, TaDepotRetraitCaisseDTO> implements ITaDepotRetraitCaisseServiceRemote {

	static Logger logger = Logger.getLogger(TaDepotRetraitCaisseService.class);

	@Inject private IDepotRetraitCaisseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaDepotRetraitCaisseService() {
		super(TaDepotRetraitCaisse.class,TaDepotRetraitCaisseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaDepotRetraitCaisse a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaDepotRetraitCaisse transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaDepotRetraitCaisse transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaDepotRetraitCaisse persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdDepotRetraitCaisse()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaDepotRetraitCaisse merge(TaDepotRetraitCaisse detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaDepotRetraitCaisse merge(TaDepotRetraitCaisse detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaDepotRetraitCaisse findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaDepotRetraitCaisse findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaDepotRetraitCaisse> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaDepotRetraitCaisseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaDepotRetraitCaisseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaDepotRetraitCaisse> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaDepotRetraitCaisseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaDepotRetraitCaisseDTO entityToDTO(TaDepotRetraitCaisse entity) {
//		TaDepotRetraitCaisseDTO dto = new TaDepotRetraitCaisseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaDepotRetraitCaisseMapper mapper = new TaDepotRetraitCaisseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaDepotRetraitCaisseDTO> listEntityToListDTO(List<TaDepotRetraitCaisse> entity) {
		List<TaDepotRetraitCaisseDTO> l = new ArrayList<TaDepotRetraitCaisseDTO>();

		for (TaDepotRetraitCaisse taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaDepotRetraitCaisseDTO> selectAllDTO() {
		System.out.println("List of TaDepotRetraitCaisseDTO EJB :");
		ArrayList<TaDepotRetraitCaisseDTO> liste = new ArrayList<TaDepotRetraitCaisseDTO>();

		List<TaDepotRetraitCaisse> projects = selectAll();
		for(TaDepotRetraitCaisse project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaDepotRetraitCaisseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaDepotRetraitCaisseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaDepotRetraitCaisseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaDepotRetraitCaisseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaDepotRetraitCaisseDTO dto, String validationContext) throws EJBException {
		try {
			TaDepotRetraitCaisseMapper mapper = new TaDepotRetraitCaisseMapper();
			TaDepotRetraitCaisse entity = null;
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
	
	public void persistDTO(TaDepotRetraitCaisseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaDepotRetraitCaisseDTO dto, String validationContext) throws CreateException {
		try {
			TaDepotRetraitCaisseMapper mapper = new TaDepotRetraitCaisseMapper();
			TaDepotRetraitCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaDepotRetraitCaisseDTO dto) throws RemoveException {
		try {
			TaDepotRetraitCaisseMapper mapper = new TaDepotRetraitCaisseMapper();
			TaDepotRetraitCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaDepotRetraitCaisse refresh(TaDepotRetraitCaisse persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaDepotRetraitCaisse value, String validationContext) /*throws ExceptLgr*/ {
		
//		TransactionSynchronizationRegistry reg;
		try {
//			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
//			if(reg!=null && reg.getResource("tenant")!=null ) {
//				System.out.println("Pas de validation ==> client lourd");
//			} else {
	
				if(validationContext==null) validationContext="";
				validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
				//dao.validate(value); //validation automatique via la JSR bean validation
//			}
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaDepotRetraitCaisse value, String propertyName, String validationContext) {
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
	public void validateDTO(TaDepotRetraitCaisseDTO dto, String validationContext) {
		try {
			TaDepotRetraitCaisseMapper mapper = new TaDepotRetraitCaisseMapper();
			TaDepotRetraitCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaDepotRetraitCaisseDTO> validator = new BeanValidator<TaDepotRetraitCaisseDTO>(TaDepotRetraitCaisseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaDepotRetraitCaisseDTO dto, String propertyName, String validationContext) {
		try {
			TaDepotRetraitCaisseMapper mapper = new TaDepotRetraitCaisseMapper();
			TaDepotRetraitCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaDepotRetraitCaisseDTO> validator = new BeanValidator<TaDepotRetraitCaisseDTO>(TaDepotRetraitCaisseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaDepotRetraitCaisseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaDepotRetraitCaisseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaDepotRetraitCaisse value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaDepotRetraitCaisse value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public List<TaDepotRetraitCaisseDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}

}

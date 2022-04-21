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
import fr.legrain.bdg.caisse.service.remote.ITaTDepotRetraitCaisseServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTDepotRetraitCaisseMapper;
import fr.legrain.caisse.dao.ITDepotRetraitCaisseDAO;
import fr.legrain.caisse.dto.TaTDepotRetraitCaisseDTO;
import fr.legrain.caisse.model.TaTDepotRetraitCaisse;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTDepotRetraitCaisseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTDepotRetraitCaisseService extends AbstractApplicationDAOServer<TaTDepotRetraitCaisse, TaTDepotRetraitCaisseDTO> implements ITaTDepotRetraitCaisseServiceRemote {

	static Logger logger = Logger.getLogger(TaTDepotRetraitCaisseService.class);

	@Inject private ITDepotRetraitCaisseDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTDepotRetraitCaisseService() {
		super(TaTDepotRetraitCaisse.class,TaTDepotRetraitCaisseDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTDepotRetraitCaisse a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTDepotRetraitCaisse transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTDepotRetraitCaisse transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTDepotRetraitCaisse persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTDepotRetraitCaisse()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTDepotRetraitCaisse merge(TaTDepotRetraitCaisse detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTDepotRetraitCaisse merge(TaTDepotRetraitCaisse detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTDepotRetraitCaisse findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTDepotRetraitCaisse findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTDepotRetraitCaisse> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTDepotRetraitCaisseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTDepotRetraitCaisseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTDepotRetraitCaisse> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTDepotRetraitCaisseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTDepotRetraitCaisseDTO entityToDTO(TaTDepotRetraitCaisse entity) {
//		TaTDepotRetraitCaisseDTO dto = new TaTDepotRetraitCaisseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTDepotRetraitCaisseMapper mapper = new TaTDepotRetraitCaisseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTDepotRetraitCaisseDTO> listEntityToListDTO(List<TaTDepotRetraitCaisse> entity) {
		List<TaTDepotRetraitCaisseDTO> l = new ArrayList<TaTDepotRetraitCaisseDTO>();

		for (TaTDepotRetraitCaisse taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTDepotRetraitCaisseDTO> selectAllDTO() {
		System.out.println("List of TaTDepotRetraitCaisseDTO EJB :");
		ArrayList<TaTDepotRetraitCaisseDTO> liste = new ArrayList<TaTDepotRetraitCaisseDTO>();

		List<TaTDepotRetraitCaisse> projects = selectAll();
		for(TaTDepotRetraitCaisse project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTDepotRetraitCaisseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTDepotRetraitCaisseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTDepotRetraitCaisseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTDepotRetraitCaisseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTDepotRetraitCaisseDTO dto, String validationContext) throws EJBException {
		try {
			TaTDepotRetraitCaisseMapper mapper = new TaTDepotRetraitCaisseMapper();
			TaTDepotRetraitCaisse entity = null;
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
	
	public void persistDTO(TaTDepotRetraitCaisseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTDepotRetraitCaisseDTO dto, String validationContext) throws CreateException {
		try {
			TaTDepotRetraitCaisseMapper mapper = new TaTDepotRetraitCaisseMapper();
			TaTDepotRetraitCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTDepotRetraitCaisseDTO dto) throws RemoveException {
		try {
			TaTDepotRetraitCaisseMapper mapper = new TaTDepotRetraitCaisseMapper();
			TaTDepotRetraitCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTDepotRetraitCaisse refresh(TaTDepotRetraitCaisse persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTDepotRetraitCaisse value, String validationContext) /*throws ExceptLgr*/ {
		
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
	public void validateEntityProperty(TaTDepotRetraitCaisse value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTDepotRetraitCaisseDTO dto, String validationContext) {
		try {
			TaTDepotRetraitCaisseMapper mapper = new TaTDepotRetraitCaisseMapper();
			TaTDepotRetraitCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTDepotRetraitCaisseDTO> validator = new BeanValidator<TaTDepotRetraitCaisseDTO>(TaTDepotRetraitCaisseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTDepotRetraitCaisseDTO dto, String propertyName, String validationContext) {
		try {
			TaTDepotRetraitCaisseMapper mapper = new TaTDepotRetraitCaisseMapper();
			TaTDepotRetraitCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTDepotRetraitCaisseDTO> validator = new BeanValidator<TaTDepotRetraitCaisseDTO>(TaTDepotRetraitCaisseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTDepotRetraitCaisseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTDepotRetraitCaisseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTDepotRetraitCaisse value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTDepotRetraitCaisse value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public List<TaTDepotRetraitCaisseDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}

}

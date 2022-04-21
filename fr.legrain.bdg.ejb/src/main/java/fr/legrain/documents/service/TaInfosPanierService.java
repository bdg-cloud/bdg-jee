package fr.legrain.documents.service;

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

import fr.legrain.bdg.documents.service.remote.ITaInfosPanierServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaPanierDTO;
import fr.legrain.document.model.TaInfosPanier;
import fr.legrain.documents.dao.IInfosPanierDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosBoncdeBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosPanierService extends AbstractApplicationDAOServer<TaInfosPanier, TaPanierDTO> implements ITaInfosPanierServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosPanierService.class);

	@Inject private IInfosPanierDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosPanierService() {
		super(TaInfosPanier.class,TaPanierDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosPanier a";
	
	public TaInfosPanier findByCodeBoncde(String code) {
		return dao.findByCodePanier(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosPanier transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosPanier transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosPanier persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosPanier merge(TaInfosPanier detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosPanier merge(TaInfosPanier detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosPanier findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosPanier findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosPanier> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPanierDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPanierDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosPanier> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPanierDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPanierDTO entityToDTO(TaInfosPanier entity) {
//		TaPanierDTO dto = new TaPanierDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
//		TaInfosPanierMapper mapper = new TaInfosPanierMapper();
		return null;
	}

	public List<TaPanierDTO> listEntityToListDTO(List<TaInfosPanier> entity) {
		List<TaPanierDTO> l = new ArrayList<TaPanierDTO>();

		for (TaInfosPanier taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPanierDTO> selectAllDTO() {
		System.out.println("List of TaPanierDTO EJB :");
		ArrayList<TaPanierDTO> liste = new ArrayList<TaPanierDTO>();

		List<TaInfosPanier> projects = selectAll();
		for(TaInfosPanier project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPanierDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPanierDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPanierDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPanierDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPanierDTO dto, String validationContext) throws EJBException {
//		try {
//			TaInfosPanierMapper mapper = new TaInfosPanierMapper();
//			TaInfosPanier entity = null;
//			if(dto.getId()!=null) {
//				entity = dao.findById(dto.getId());
//				if(dto.getVersionObj()!=entity.getVersionObj()) {
//					throw new OptimisticLockException(entity,
//							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getId()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
//				} else {
//					 entity = mapper.mapDtoToEntity(dto,entity);
//				}
//			}
//			
//			//dao.merge(entity);
//			dao.detach(entity); //pour passer les controles
//			enregistrerMerge(entity, validationContext);
//		} catch (Exception e) {
//			e.printStackTrace();
//			//throw new CreateException(e.getMessage());
//			throw new EJBException(e.getMessage());
//		}
	}
	
	public void persistDTO(TaPanierDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPanierDTO dto, String validationContext) throws CreateException {
//		try {
//			TaInfosPanierMapper mapper = new TaInfosPanierMapper();
//			TaInfosPanier entity = mapper.mapDtoToEntity(dto,null);
//			//dao.persist(entity);
//			enregistrerPersist(entity, validationContext);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new CreateException(e.getMessage());
//		}
	}

	@Override
	public void removeDTO(TaPanierDTO dto) throws RemoveException {
//		try {
//			TaInfosPanierMapper mapper = new TaInfosPanierMapper();
//			TaInfosPanier entity = mapper.mapDtoToEntity(dto,null);
//			//dao.remove(entity);
//			supprimer(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RemoveException(e.getMessage());
//		}
	}

	@Override
	protected TaInfosPanier refresh(TaInfosPanier persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosPanier value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosPanier value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPanierDTO dto, String validationContext) {
//		try {
//			TaInfosPanierMapper mapper = new TaInfosPanierMapper();
//			TaInfosPanier entity = mapper.mapDtoToEntity(dto,null);
//			validateEntity(entity,validationContext);
//			
//			//validation automatique via la JSR bean validation
////			BeanValidator<TaPanierDTO> validator = new BeanValidator<TaPanierDTO>(TaPanierDTO.class);
////			validator.validate(dto);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPanierDTO dto, String propertyName, String validationContext) {
//		try {
//			TaInfosPanierMapper mapper = new TaInfosPanierMapper();
//			TaInfosPanier entity = mapper.mapDtoToEntity(dto,null);
//			validateEntityProperty(entity,propertyName,validationContext);
//			
//			//validation automatique via la JSR bean validation
////			BeanValidator<TaPanierDTO> validator = new BeanValidator<TaPanierDTO>(TaPanierDTO.class);
////			validator.validateField(dto,propertyName);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new EJBException(e.getMessage());
//		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPanierDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPanierDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosPanier value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosPanier value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

	@Override
	public TaInfosPanier findByCodePanier(String code) {
		// TODO Auto-generated method stub
		return dao.findByCodePanier(code);
	}

}

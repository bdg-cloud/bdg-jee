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

import fr.legrain.bdg.documents.service.remote.ITaInfosAbonnementServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosAbonnementMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaAbonnementDTO;
import fr.legrain.document.model.TaInfosAbonnement;
import fr.legrain.documents.dao.IInfosAbonnementDAO;
import fr.legrain.documents.dao.IInfosDevisDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosAbonnementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosAbonnementService extends AbstractApplicationDAOServer<TaInfosAbonnement, TaAbonnementDTO> implements ITaInfosAbonnementServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosAbonnementService.class);

	@Inject private IInfosAbonnementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosAbonnementService() {
		super(TaInfosAbonnement.class,TaAbonnementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosAbonnement a";
	
	public TaInfosAbonnement findByCodeDevis(String code) {
		return dao.findByCodeDevis(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosAbonnement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosAbonnement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosAbonnement persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosAbonnement merge(TaInfosAbonnement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosAbonnement merge(TaInfosAbonnement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosAbonnement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosAbonnement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosAbonnement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAbonnementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAbonnementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosAbonnement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAbonnementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAbonnementDTO entityToDTO(TaInfosAbonnement entity) {
//		TaAbonnementDTO dto = new TaAbonnementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosAbonnementMapper mapper = new TaInfosAbonnementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAbonnementDTO> listEntityToListDTO(List<TaInfosAbonnement> entity) {
		List<TaAbonnementDTO> l = new ArrayList<TaAbonnementDTO>();

		for (TaInfosAbonnement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAbonnementDTO> selectAllDTO() {
		System.out.println("List of TaAbonnementDTO EJB :");
		ArrayList<TaAbonnementDTO> liste = new ArrayList<TaAbonnementDTO>();

		List<TaInfosAbonnement> projects = selectAll();
		for(TaInfosAbonnement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAbonnementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAbonnementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAbonnementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAbonnementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAbonnementDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosAbonnementMapper mapper = new TaInfosAbonnementMapper();
			TaInfosAbonnement entity = null;
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
	
	public void persistDTO(TaAbonnementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAbonnementDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosAbonnementMapper mapper = new TaInfosAbonnementMapper();
			TaInfosAbonnement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAbonnementDTO dto) throws RemoveException {
		try {
			TaInfosAbonnementMapper mapper = new TaInfosAbonnementMapper();
			TaInfosAbonnement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosAbonnement refresh(TaInfosAbonnement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosAbonnement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosAbonnement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAbonnementDTO dto, String validationContext) {
		try {
			TaInfosAbonnementMapper mapper = new TaInfosAbonnementMapper();
			TaInfosAbonnement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAbonnementDTO> validator = new BeanValidator<TaAbonnementDTO>(TaAbonnementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAbonnementDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosAbonnementMapper mapper = new TaInfosAbonnementMapper();
			TaInfosAbonnement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAbonnementDTO> validator = new BeanValidator<TaAbonnementDTO>(TaAbonnementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAbonnementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAbonnementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosAbonnement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosAbonnement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

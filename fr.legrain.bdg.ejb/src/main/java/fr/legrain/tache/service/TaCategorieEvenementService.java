package fr.legrain.tache.service;

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

import fr.legrain.bdg.model.mapping.mapper.TaCategorieEvenementMapper;
import fr.legrain.bdg.tache.service.remote.ITaCategorieEvenementServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tache.dao.ICategorieEvenementDAO;
import fr.legrain.tache.dto.TaCategorieEvenementDTO;
import fr.legrain.tache.model.TaCategorieEvenement;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaCategorieEvenementService extends AbstractApplicationDAOServer<TaCategorieEvenement, TaCategorieEvenementDTO> implements ITaCategorieEvenementServiceRemote {

	static Logger logger = Logger.getLogger(TaCategorieEvenementService.class);

	@Inject private ICategorieEvenementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaCategorieEvenementService() {
		super(TaCategorieEvenement.class,TaCategorieEvenementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaCategorieEvenement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCategorieEvenement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCategorieEvenement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCategorieEvenement persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdCategorieEvenement()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaCategorieEvenement merge(TaCategorieEvenement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCategorieEvenement merge(TaCategorieEvenement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCategorieEvenement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCategorieEvenement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCategorieEvenement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCategorieEvenementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCategorieEvenementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCategorieEvenement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCategorieEvenementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCategorieEvenementDTO entityToDTO(TaCategorieEvenement entity) {
//		TaCategorieEvenementDTO dto = new TaCategorieEvenementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCategorieEvenementMapper mapper = new TaCategorieEvenementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCategorieEvenementDTO> listEntityToListDTO(List<TaCategorieEvenement> entity) {
		List<TaCategorieEvenementDTO> l = new ArrayList<TaCategorieEvenementDTO>();

		for (TaCategorieEvenement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCategorieEvenementDTO> selectAllDTO() {
		System.out.println("List of TaCategorieEvenementDTO EJB :");
		ArrayList<TaCategorieEvenementDTO> liste = new ArrayList<TaCategorieEvenementDTO>();

		List<TaCategorieEvenement> projects = selectAll();
		for(TaCategorieEvenement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCategorieEvenementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCategorieEvenementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCategorieEvenementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCategorieEvenementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCategorieEvenementDTO dto, String validationContext) throws EJBException {
		try {
			TaCategorieEvenementMapper mapper = new TaCategorieEvenementMapper();
			TaCategorieEvenement entity = null;
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
	
	public void persistDTO(TaCategorieEvenementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCategorieEvenementDTO dto, String validationContext) throws CreateException {
		try {
			TaCategorieEvenementMapper mapper = new TaCategorieEvenementMapper();
			TaCategorieEvenement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCategorieEvenementDTO dto) throws RemoveException {
		try {
			TaCategorieEvenementMapper mapper = new TaCategorieEvenementMapper();
			TaCategorieEvenement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCategorieEvenement refresh(TaCategorieEvenement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCategorieEvenement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCategorieEvenement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCategorieEvenementDTO dto, String validationContext) {
		try {
			TaCategorieEvenementMapper mapper = new TaCategorieEvenementMapper();
			TaCategorieEvenement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCategorieEvenementDTO> validator = new BeanValidator<TaCategorieEvenementDTO>(TaCategorieEvenementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCategorieEvenementDTO dto, String propertyName, String validationContext) {
		try {
			TaCategorieEvenementMapper mapper = new TaCategorieEvenementMapper();
			TaCategorieEvenement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCategorieEvenementDTO> validator = new BeanValidator<TaCategorieEvenementDTO>(TaCategorieEvenementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCategorieEvenementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCategorieEvenementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCategorieEvenement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCategorieEvenement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

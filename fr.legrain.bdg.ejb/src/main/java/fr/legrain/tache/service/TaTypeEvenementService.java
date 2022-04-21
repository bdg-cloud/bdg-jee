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

import fr.legrain.bdg.model.mapping.mapper.TaTypeEvenementMapper;
import fr.legrain.bdg.tache.service.remote.ITaTypeEvenementServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tache.dao.ITypeEvenementDAO;
import fr.legrain.tache.dto.TaTypeEvenementDTO;
import fr.legrain.tache.model.TaTypeEvenement;


@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTypeEvenementService extends AbstractApplicationDAOServer<TaTypeEvenement, TaTypeEvenementDTO> implements ITaTypeEvenementServiceRemote {

	static Logger logger = Logger.getLogger(TaTypeEvenementService.class);

	@Inject private ITypeEvenementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTypeEvenementService() {
		super(TaTypeEvenement.class,TaTypeEvenementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTypeEvenement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTypeEvenement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTypeEvenement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTypeEvenement persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdTypeEvenement()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaTypeEvenement merge(TaTypeEvenement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTypeEvenement merge(TaTypeEvenement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTypeEvenement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTypeEvenement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTypeEvenement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTypeEvenementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTypeEvenementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTypeEvenement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTypeEvenementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTypeEvenementDTO entityToDTO(TaTypeEvenement entity) {
//		TaTypeEvenementDTO dto = new TaTypeEvenementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTypeEvenementMapper mapper = new TaTypeEvenementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTypeEvenementDTO> listEntityToListDTO(List<TaTypeEvenement> entity) {
		List<TaTypeEvenementDTO> l = new ArrayList<TaTypeEvenementDTO>();

		for (TaTypeEvenement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTypeEvenementDTO> selectAllDTO() {
		System.out.println("List of TaTypeEvenementDTO EJB :");
		ArrayList<TaTypeEvenementDTO> liste = new ArrayList<TaTypeEvenementDTO>();

		List<TaTypeEvenement> projects = selectAll();
		for(TaTypeEvenement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTypeEvenementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTypeEvenementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTypeEvenementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTypeEvenementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTypeEvenementDTO dto, String validationContext) throws EJBException {
		try {
			TaTypeEvenementMapper mapper = new TaTypeEvenementMapper();
			TaTypeEvenement entity = null;
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
	
	public void persistDTO(TaTypeEvenementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTypeEvenementDTO dto, String validationContext) throws CreateException {
		try {
			TaTypeEvenementMapper mapper = new TaTypeEvenementMapper();
			TaTypeEvenement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTypeEvenementDTO dto) throws RemoveException {
		try {
			TaTypeEvenementMapper mapper = new TaTypeEvenementMapper();
			TaTypeEvenement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTypeEvenement refresh(TaTypeEvenement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTypeEvenement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTypeEvenement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTypeEvenementDTO dto, String validationContext) {
		try {
			TaTypeEvenementMapper mapper = new TaTypeEvenementMapper();
			TaTypeEvenement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeEvenementDTO> validator = new BeanValidator<TaTypeEvenementDTO>(TaTypeEvenementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTypeEvenementDTO dto, String propertyName, String validationContext) {
		try {
			TaTypeEvenementMapper mapper = new TaTypeEvenementMapper();
			TaTypeEvenement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTypeEvenementDTO> validator = new BeanValidator<TaTypeEvenementDTO>(TaTypeEvenementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTypeEvenementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTypeEvenementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTypeEvenement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTypeEvenement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

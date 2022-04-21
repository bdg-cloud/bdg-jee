package fr.legrain.controle.service;

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

import fr.legrain.bdg.general.service.remote.ITaVerrouModificationServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaVerrouModificationMapper;
import fr.legrain.controle.dao.IVerrouModificationDAO;
import fr.legrain.controle.dto.TaVerrouModificationDTO;
import fr.legrain.controle.model.TaVerrouModification;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;

/**
 * Session Bean implementation class TaVerrouModificationBean
 */
@Stateless
//@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaVerrouModificationService extends AbstractApplicationDAOServer<TaVerrouModification, TaVerrouModificationDTO> implements ITaVerrouModificationServiceRemote {

	static Logger logger = Logger.getLogger(TaVerrouModificationService.class);

	@Inject private IVerrouModificationDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaVerrouModificationService() {
		super(TaVerrouModification.class,TaVerrouModificationDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaVerrouModification a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void persist(TaVerrouModification transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
	
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaVerrouModification transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaVerrouModification persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaVerrouModification merge(TaVerrouModification detachedInstance) {
		return merge(detachedInstance,null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaVerrouModification merge(TaVerrouModification detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaVerrouModification findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaVerrouModification findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaVerrouModification> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaVerrouModificationDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaVerrouModificationDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaVerrouModification> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaVerrouModificationDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaVerrouModificationDTO entityToDTO(TaVerrouModification entity) {
//		TaVerrouModificationDTO dto = new TaVerrouModificationDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaVerrouModificationMapper mapper = new TaVerrouModificationMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaVerrouModificationDTO> listEntityToListDTO(List<TaVerrouModification> entity) {
		List<TaVerrouModificationDTO> l = new ArrayList<TaVerrouModificationDTO>();

		for (TaVerrouModification taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaVerrouModificationDTO> selectAllDTO() {
		System.out.println("List of TaVerrouModificationDTO EJB :");
		ArrayList<TaVerrouModificationDTO> liste = new ArrayList<TaVerrouModificationDTO>();

		List<TaVerrouModification> projects = selectAll();
		for(TaVerrouModification project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaVerrouModificationDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaVerrouModificationDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaVerrouModificationDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaVerrouModificationDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaVerrouModificationDTO dto, String validationContext) throws EJBException {
		try {
			TaVerrouModificationMapper mapper = new TaVerrouModificationMapper();
			TaVerrouModification entity = null;
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
	
	public void persistDTO(TaVerrouModificationDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaVerrouModificationDTO dto, String validationContext) throws CreateException {
		try {
			TaVerrouModificationMapper mapper = new TaVerrouModificationMapper();
			TaVerrouModification entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaVerrouModificationDTO dto) throws RemoveException {
		try {
			TaVerrouModificationMapper mapper = new TaVerrouModificationMapper();
			TaVerrouModification entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaVerrouModification refresh(TaVerrouModification persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateEntity(TaVerrouModification value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaVerrouModification value, String propertyName, String validationContext) {
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
	public void validateDTO(TaVerrouModificationDTO dto, String validationContext) {
		try {
			TaVerrouModificationMapper mapper = new TaVerrouModificationMapper();
			TaVerrouModification entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaVerrouModificationDTO> validator = new BeanValidator<TaVerrouModificationDTO>(TaVerrouModificationDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	public void validateDTOProperty(TaVerrouModificationDTO dto, String propertyName, String validationContext) {
		try {
			TaVerrouModificationMapper mapper = new TaVerrouModificationMapper();
			TaVerrouModification entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaVerrouModificationDTO> validator = new BeanValidator<TaVerrouModificationDTO>(TaVerrouModificationDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}

	@Override
	public void validateDTO(TaVerrouModificationDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	public void validateDTOProperty(TaVerrouModificationDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	public void validateEntity(TaVerrouModification value) {
		validateEntity(value,null);
	}

	@Override
	public void validateEntityProperty(TaVerrouModification value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

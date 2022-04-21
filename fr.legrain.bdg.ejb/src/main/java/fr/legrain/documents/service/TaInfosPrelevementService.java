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

import fr.legrain.bdg.documents.service.remote.ITaInfosPrelevementServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosPrelevementMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaPrelevementDTO;
import fr.legrain.document.model.TaInfosPrelevement;
import fr.legrain.documents.dao.IInfosPrelevementDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosPrelevementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosPrelevementService extends AbstractApplicationDAOServer<TaInfosPrelevement, TaPrelevementDTO> implements ITaInfosPrelevementServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosPrelevementService.class);

	@Inject private IInfosPrelevementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosPrelevementService() {
		super(TaInfosPrelevement.class,TaPrelevementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosPrelevement a";
	
	public TaInfosPrelevement findByCodePrelevement(String code) {
		return dao.findByCodePrelevement(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosPrelevement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosPrelevement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosPrelevement persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosPrelevement merge(TaInfosPrelevement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosPrelevement merge(TaInfosPrelevement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosPrelevement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosPrelevement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosPrelevement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPrelevementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPrelevementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosPrelevement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPrelevementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPrelevementDTO entityToDTO(TaInfosPrelevement entity) {
//		TaPrelevementDTO dto = new TaPrelevementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosPrelevementMapper mapper = new TaInfosPrelevementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaPrelevementDTO> listEntityToListDTO(List<TaInfosPrelevement> entity) {
		List<TaPrelevementDTO> l = new ArrayList<TaPrelevementDTO>();

		for (TaInfosPrelevement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPrelevementDTO> selectAllDTO() {
		System.out.println("List of TaPrelevementDTO EJB :");
		ArrayList<TaPrelevementDTO> liste = new ArrayList<TaPrelevementDTO>();

		List<TaInfosPrelevement> projects = selectAll();
		for(TaInfosPrelevement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPrelevementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPrelevementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPrelevementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPrelevementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPrelevementDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosPrelevementMapper mapper = new TaInfosPrelevementMapper();
			TaInfosPrelevement entity = null;
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
	
	public void persistDTO(TaPrelevementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPrelevementDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosPrelevementMapper mapper = new TaInfosPrelevementMapper();
			TaInfosPrelevement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaPrelevementDTO dto) throws RemoveException {
		try {
			TaInfosPrelevementMapper mapper = new TaInfosPrelevementMapper();
			TaInfosPrelevement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosPrelevement refresh(TaInfosPrelevement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosPrelevement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosPrelevement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPrelevementDTO dto, String validationContext) {
		try {
			TaInfosPrelevementMapper mapper = new TaInfosPrelevementMapper();
			TaInfosPrelevement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrelevementDTO> validator = new BeanValidator<TaPrelevementDTO>(TaPrelevementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPrelevementDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosPrelevementMapper mapper = new TaInfosPrelevementMapper();
			TaInfosPrelevement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPrelevementDTO> validator = new BeanValidator<TaPrelevementDTO>(TaPrelevementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPrelevementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPrelevementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosPrelevement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosPrelevement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

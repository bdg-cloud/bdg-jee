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

import fr.legrain.bdg.documents.service.remote.ITaLPrelevementServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaLPrelevementMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaLPrelevementDTO;
import fr.legrain.document.model.TaLPrelevement;
import fr.legrain.documents.dao.ILPrelevementDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaLPrelevementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaLPrelevementService extends AbstractApplicationDAOServer<TaLPrelevement, TaLPrelevementDTO> implements ITaLPrelevementServiceRemote {

	static Logger logger = Logger.getLogger(TaLPrelevementService.class);

	@Inject private ILPrelevementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaLPrelevementService() {
		super(TaLPrelevement.class,TaLPrelevementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaLPrelevement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaLPrelevement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaLPrelevement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaLPrelevement persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaLPrelevement merge(TaLPrelevement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaLPrelevement merge(TaLPrelevement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaLPrelevement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaLPrelevement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaLPrelevement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaLPrelevementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaLPrelevementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaLPrelevement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaLPrelevementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaLPrelevementDTO entityToDTO(TaLPrelevement entity) {
//		TaLPrelevementDTO dto = new TaLPrelevementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaLPrelevementMapper mapper = new TaLPrelevementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaLPrelevementDTO> listEntityToListDTO(List<TaLPrelevement> entity) {
		List<TaLPrelevementDTO> l = new ArrayList<TaLPrelevementDTO>();

		for (TaLPrelevement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaLPrelevementDTO> selectAllDTO() {
		System.out.println("List of TaLPrelevementDTO EJB :");
		ArrayList<TaLPrelevementDTO> liste = new ArrayList<TaLPrelevementDTO>();

		List<TaLPrelevement> projects = selectAll();
		for(TaLPrelevement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaLPrelevementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaLPrelevementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaLPrelevementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaLPrelevementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaLPrelevementDTO dto, String validationContext) throws EJBException {
		try {
			TaLPrelevementMapper mapper = new TaLPrelevementMapper();
			TaLPrelevement entity = null;
			if(dto.getIdLDocument()!=null) {
				entity = dao.findById(dto.getIdLDocument());
				if(dto.getVersionObj()!=entity.getVersionObj()) {
					throw new OptimisticLockException(entity,
							"L'objet à été modifié depuis le dernier accés. Client ID : "+dto.getIdLDocument()+" - Client Version objet : "+dto.getVersionObj()+" -Serveur Version Objet : "+entity.getVersionObj());
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
	
	public void persistDTO(TaLPrelevementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaLPrelevementDTO dto, String validationContext) throws CreateException {
		try {
			TaLPrelevementMapper mapper = new TaLPrelevementMapper();
			TaLPrelevement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaLPrelevementDTO dto) throws RemoveException {
		try {
			TaLPrelevementMapper mapper = new TaLPrelevementMapper();
			TaLPrelevement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaLPrelevement refresh(TaLPrelevement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaLPrelevement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaLPrelevement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaLPrelevementDTO dto, String validationContext) {
		try {
			TaLPrelevementMapper mapper = new TaLPrelevementMapper();
			TaLPrelevement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLPrelevementDTO> validator = new BeanValidator<TaLPrelevementDTO>(TaLPrelevementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaLPrelevementDTO dto, String propertyName, String validationContext) {
		try {
			TaLPrelevementMapper mapper = new TaLPrelevementMapper();
			TaLPrelevement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaLPrelevementDTO> validator = new BeanValidator<TaLPrelevementDTO>(TaLPrelevementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaLPrelevementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaLPrelevementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaLPrelevement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaLPrelevement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

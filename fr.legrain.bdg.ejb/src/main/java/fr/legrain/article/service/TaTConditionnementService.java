package fr.legrain.article.service;

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

import fr.legrain.article.dao.ITConditionnementDAO;
import fr.legrain.article.dto.TaTConditionnementDTO;
import fr.legrain.article.model.TaTConditionnement;
import fr.legrain.bdg.article.service.remote.ITaTConditionnementServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaTConditionnementMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTConditionnementBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaTConditionnementService extends AbstractApplicationDAOServer<TaTConditionnement, TaTConditionnementDTO> implements ITaTConditionnementServiceRemote {

	static Logger logger = Logger.getLogger(TaTConditionnementService.class);

	@Inject private ITConditionnementDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTConditionnementService() {
		super(TaTConditionnement.class,TaTConditionnementDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTConditionnement a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTConditionnement transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTConditionnement transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTConditionnement persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaTConditionnement merge(TaTConditionnement detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTConditionnement merge(TaTConditionnement detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTConditionnement findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTConditionnement findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTConditionnement> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaTConditionnementDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaTConditionnementDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTConditionnement> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaTConditionnementDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaTConditionnementDTO entityToDTO(TaTConditionnement entity) {
//		TaTConditionnementDTO dto = new TaTConditionnementDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaTConditionnementMapper mapper = new TaTConditionnementMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaTConditionnementDTO> listEntityToListDTO(List<TaTConditionnement> entity) {
		List<TaTConditionnementDTO> l = new ArrayList<TaTConditionnementDTO>();

		for (TaTConditionnement taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaTConditionnementDTO> selectAllDTO() {
		System.out.println("List of TaTConditionnementDTO EJB :");
		ArrayList<TaTConditionnementDTO> liste = new ArrayList<TaTConditionnementDTO>();

		List<TaTConditionnement> projects = selectAll();
		for(TaTConditionnement project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaTConditionnementDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaTConditionnementDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaTConditionnementDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaTConditionnementDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaTConditionnementDTO dto, String validationContext) throws EJBException {
		try {
			TaTConditionnementMapper mapper = new TaTConditionnementMapper();
			TaTConditionnement entity = null;
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
	
	public void persistDTO(TaTConditionnementDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaTConditionnementDTO dto, String validationContext) throws CreateException {
		try {
			TaTConditionnementMapper mapper = new TaTConditionnementMapper();
			TaTConditionnement entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaTConditionnementDTO dto) throws RemoveException {
		try {
			TaTConditionnementMapper mapper = new TaTConditionnementMapper();
			TaTConditionnement entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTConditionnement refresh(TaTConditionnement persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTConditionnement value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTConditionnement value, String propertyName, String validationContext) {
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
	public void validateDTO(TaTConditionnementDTO dto, String validationContext) {
		try {
			TaTConditionnementMapper mapper = new TaTConditionnementMapper();
			TaTConditionnement entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTConditionnementDTO> validator = new BeanValidator<TaTConditionnementDTO>(TaTConditionnementDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaTConditionnementDTO dto, String propertyName, String validationContext) {
		try {
			TaTConditionnementMapper mapper = new TaTConditionnementMapper();
			TaTConditionnement entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaTConditionnementDTO> validator = new BeanValidator<TaTConditionnementDTO>(TaTConditionnementDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaTConditionnementDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaTConditionnementDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTConditionnement value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTConditionnement value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

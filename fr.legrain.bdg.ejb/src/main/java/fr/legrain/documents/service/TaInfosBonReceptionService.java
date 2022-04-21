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

import fr.legrain.bdg.documents.service.remote.ITaInfosBonReceptionServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosBonReceptionMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaBonReceptionDTO;
import fr.legrain.document.model.TaInfosBonReception;
import fr.legrain.documents.dao.IInfosBonReceptionDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosBonReceptionBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosBonReceptionService extends AbstractApplicationDAOServer<TaInfosBonReception, TaBonReceptionDTO> implements ITaInfosBonReceptionServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosBonReceptionService.class);

	@Inject private IInfosBonReceptionDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosBonReceptionService() {
		super(TaInfosBonReception.class,TaBonReceptionDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosBonReception a";
	
	public TaInfosBonReception findByCodeBonReception(String code) {
		return dao.findByCodeBonReception(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosBonReception transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosBonReception transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosBonReception persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosBonReception merge(TaInfosBonReception detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosBonReception merge(TaInfosBonReception detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosBonReception findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosBonReception findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosBonReception> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaBonReceptionDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaBonReceptionDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosBonReception> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaBonReceptionDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaBonReceptionDTO entityToDTO(TaInfosBonReception entity) {
//		TaBonReceptionDTO dto = new TaBonReceptionDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosBonReceptionMapper mapper = new TaInfosBonReceptionMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaBonReceptionDTO> listEntityToListDTO(List<TaInfosBonReception> entity) {
		List<TaBonReceptionDTO> l = new ArrayList<TaBonReceptionDTO>();

		for (TaInfosBonReception taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaBonReceptionDTO> selectAllDTO() {
		System.out.println("List of TaBonReceptionDTO EJB :");
		ArrayList<TaBonReceptionDTO> liste = new ArrayList<TaBonReceptionDTO>();

		List<TaInfosBonReception> projects = selectAll();
		for(TaInfosBonReception project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaBonReceptionDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaBonReceptionDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaBonReceptionDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaBonReceptionDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaBonReceptionDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosBonReceptionMapper mapper = new TaInfosBonReceptionMapper();
			TaInfosBonReception entity = null;
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
	
	public void persistDTO(TaBonReceptionDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaBonReceptionDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosBonReceptionMapper mapper = new TaInfosBonReceptionMapper();
			TaInfosBonReception entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaBonReceptionDTO dto) throws RemoveException {
		try {
			TaInfosBonReceptionMapper mapper = new TaInfosBonReceptionMapper();
			TaInfosBonReception entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosBonReception refresh(TaInfosBonReception persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosBonReception value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosBonReception value, String propertyName, String validationContext) {
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
	public void validateDTO(TaBonReceptionDTO dto, String validationContext) {
		try {
			TaInfosBonReceptionMapper mapper = new TaInfosBonReceptionMapper();
			TaInfosBonReception entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBonReceptionDTO> validator = new BeanValidator<TaBonReceptionDTO>(TaBonReceptionDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaBonReceptionDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosBonReceptionMapper mapper = new TaInfosBonReceptionMapper();
			TaInfosBonReception entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBonReceptionDTO> validator = new BeanValidator<TaBonReceptionDTO>(TaBonReceptionDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaBonReceptionDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaBonReceptionDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosBonReception value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosBonReception value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

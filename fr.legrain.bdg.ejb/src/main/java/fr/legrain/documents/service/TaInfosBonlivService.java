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

import fr.legrain.bdg.documents.service.remote.ITaInfosBonlivServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosBonlivMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.dto.TaBonlivDTO;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.document.model.TaInfosBonliv;
import fr.legrain.documents.dao.IInfosBonlivDAO;
import fr.legrain.documents.dao.IInfosFactureDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosBonlivBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosBonlivService extends AbstractApplicationDAOServer<TaInfosBonliv, TaBonlivDTO> implements ITaInfosBonlivServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosBonlivService.class);

	@Inject private IInfosBonlivDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosBonlivService() {
		super(TaInfosBonliv.class,TaBonlivDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosBonliv a";
	
	public TaInfosBonliv findByCodeBonliv(String code) {
		return dao.findByCodeBonliv(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosBonliv transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosBonliv transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosBonliv persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosBonliv merge(TaInfosBonliv detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosBonliv merge(TaInfosBonliv detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosBonliv findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosBonliv findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosBonliv> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaBonlivDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaBonlivDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosBonliv> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaBonlivDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaBonlivDTO entityToDTO(TaInfosBonliv entity) {
//		TaBonlivDTO dto = new TaBonlivDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosBonlivMapper mapper = new TaInfosBonlivMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaBonlivDTO> listEntityToListDTO(List<TaInfosBonliv> entity) {
		List<TaBonlivDTO> l = new ArrayList<TaBonlivDTO>();

		for (TaInfosBonliv taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaBonlivDTO> selectAllDTO() {
		System.out.println("List of TaBonlivDTO EJB :");
		ArrayList<TaBonlivDTO> liste = new ArrayList<TaBonlivDTO>();

		List<TaInfosBonliv> projects = selectAll();
		for(TaInfosBonliv project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaBonlivDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaBonlivDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaBonlivDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaBonlivDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaBonlivDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosBonlivMapper mapper = new TaInfosBonlivMapper();
			TaInfosBonliv entity = null;
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
	
	public void persistDTO(TaBonlivDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaBonlivDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosBonlivMapper mapper = new TaInfosBonlivMapper();
			TaInfosBonliv entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaBonlivDTO dto) throws RemoveException {
		try {
			TaInfosBonlivMapper mapper = new TaInfosBonlivMapper();
			TaInfosBonliv entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosBonliv refresh(TaInfosBonliv persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosBonliv value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosBonliv value, String propertyName, String validationContext) {
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
	public void validateDTO(TaBonlivDTO dto, String validationContext) {
		try {
			TaInfosBonlivMapper mapper = new TaInfosBonlivMapper();
			TaInfosBonliv entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBonlivDTO> validator = new BeanValidator<TaBonlivDTO>(TaBonlivDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaBonlivDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosBonlivMapper mapper = new TaInfosBonlivMapper();
			TaInfosBonliv entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaBonlivDTO> validator = new BeanValidator<TaBonlivDTO>(TaBonlivDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaBonlivDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaBonlivDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosBonliv value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosBonliv value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

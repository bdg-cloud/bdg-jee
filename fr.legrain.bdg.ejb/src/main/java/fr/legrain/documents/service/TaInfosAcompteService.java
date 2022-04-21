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

import fr.legrain.bdg.documents.service.remote.ITaInfosAcompteServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaInfosAcompteServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaInfosAcompteMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.dto.TaAcompteDTO;
import fr.legrain.document.model.TaInfosAcompte;
import fr.legrain.document.model.TaInfosAcompte;
import fr.legrain.documents.dao.IInfosAcompteDAO;
import fr.legrain.documents.dao.IInfosDevisDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaInfosAcompteBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaInfosAcompteService extends AbstractApplicationDAOServer<TaInfosAcompte, TaAcompteDTO> implements ITaInfosAcompteServiceRemote {

	static Logger logger = Logger.getLogger(TaInfosAcompteService.class);

	@Inject private IInfosAcompteDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaInfosAcompteService() {
		super(TaInfosAcompte.class,TaAcompteDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaInfosAcompte a";
	
	public TaInfosAcompte findByCodeAcompte(String code) {
		return dao.findByCodeAcompte(code);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaInfosAcompte transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaInfosAcompte transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaInfosAcompte persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaInfosAcompte merge(TaInfosAcompte detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaInfosAcompte merge(TaInfosAcompte detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaInfosAcompte findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaInfosAcompte findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaInfosAcompte> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAcompteDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAcompteDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaInfosAcompte> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAcompteDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAcompteDTO entityToDTO(TaInfosAcompte entity) {
//		TaAcompteDTO dto = new TaAcompteDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaInfosAcompteMapper mapper = new TaInfosAcompteMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAcompteDTO> listEntityToListDTO(List<TaInfosAcompte> entity) {
		List<TaAcompteDTO> l = new ArrayList<TaAcompteDTO>();

		for (TaInfosAcompte taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAcompteDTO> selectAllDTO() {
		System.out.println("List of TaAcompteDTO EJB :");
		ArrayList<TaAcompteDTO> liste = new ArrayList<TaAcompteDTO>();

		List<TaInfosAcompte> projects = selectAll();
		for(TaInfosAcompte project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAcompteDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAcompteDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAcompteDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAcompteDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAcompteDTO dto, String validationContext) throws EJBException {
		try {
			TaInfosAcompteMapper mapper = new TaInfosAcompteMapper();
			TaInfosAcompte entity = null;
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
	
	public void persistDTO(TaAcompteDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAcompteDTO dto, String validationContext) throws CreateException {
		try {
			TaInfosAcompteMapper mapper = new TaInfosAcompteMapper();
			TaInfosAcompte entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAcompteDTO dto) throws RemoveException {
		try {
			TaInfosAcompteMapper mapper = new TaInfosAcompteMapper();
			TaInfosAcompte entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaInfosAcompte refresh(TaInfosAcompte persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaInfosAcompte value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaInfosAcompte value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAcompteDTO dto, String validationContext) {
		try {
			TaInfosAcompteMapper mapper = new TaInfosAcompteMapper();
			TaInfosAcompte entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAcompteDTO> validator = new BeanValidator<TaAcompteDTO>(TaAcompteDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAcompteDTO dto, String propertyName, String validationContext) {
		try {
			TaInfosAcompteMapper mapper = new TaInfosAcompteMapper();
			TaInfosAcompte entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAcompteDTO> validator = new BeanValidator<TaAcompteDTO>(TaAcompteDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAcompteDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAcompteDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaInfosAcompte value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaInfosAcompte value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

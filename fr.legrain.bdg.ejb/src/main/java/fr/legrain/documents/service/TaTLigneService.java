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

import fr.legrain.bdg.documents.service.remote.ITaTLigneServiceRemote;
import fr.legrain.bdg.model.ObjectDTO;
import fr.legrain.bdg.model.mapping.mapper.TaTLigneMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.model.TaTLigne;
import fr.legrain.documents.dao.ITLigneDAO;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaTLigneBean
 */
@Stateless
@DeclareRoles("admin")@Interceptors(ServerTenantInterceptor.class)

public class TaTLigneService extends AbstractApplicationDAOServer<TaTLigne, ObjectDTO> implements ITaTLigneServiceRemote {

	static Logger logger = Logger.getLogger(TaTLigneService.class);

	@Inject private ITLigneDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaTLigneService() {
		super(TaTLigne.class,ObjectDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaTLigne a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaTLigne transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaTLigne transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaTLigne persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
	}
	
	public TaTLigne merge(TaTLigne detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaTLigne merge(TaTLigne detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaTLigne findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaTLigne findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaTLigne> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<ObjectDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<ObjectDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaTLigne> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<ObjectDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public ObjectDTO entityToDTO(TaTLigne entity) {
//		ObjectDTO dto = new ObjectDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		
		TaTLigneMapper mapper = new TaTLigneMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<ObjectDTO> listEntityToListDTO(List<TaTLigne> entity) {
		List<ObjectDTO> l = new ArrayList<ObjectDTO>();

		for (TaTLigne taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<ObjectDTO> selectAllDTO() {
		System.out.println("List of ObjectDTO EJB :");
		ArrayList<ObjectDTO> liste = new ArrayList<ObjectDTO>();

		List<TaTLigne> projects = selectAll();
		for(TaTLigne project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public ObjectDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public ObjectDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(ObjectDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(ObjectDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(ObjectDTO dto, String validationContext) throws EJBException {
		try {
			TaTLigneMapper mapper = new TaTLigneMapper();
			TaTLigne entity = null;
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
	
	public void persistDTO(ObjectDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(ObjectDTO dto, String validationContext) throws CreateException {
		try {
			TaTLigneMapper mapper = new TaTLigneMapper();
			TaTLigne entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(ObjectDTO dto) throws RemoveException {
		try {
			TaTLigneMapper mapper = new TaTLigneMapper();
			TaTLigne entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaTLigne refresh(TaTLigne persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaTLigne value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaTLigne value, String propertyName, String validationContext) {
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
	public void validateDTO(ObjectDTO dto, String validationContext) {
		try {
			TaTLigneMapper mapper = new TaTLigneMapper();
			TaTLigne entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<ObjectDTO> validator = new BeanValidator<ObjectDTO>(ObjectDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(ObjectDTO dto, String propertyName, String validationContext) {
		try {
			TaTLigneMapper mapper = new TaTLigneMapper();
			TaTLigne entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<ObjectDTO> validator = new BeanValidator<ObjectDTO>(ObjectDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(ObjectDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(ObjectDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaTLigne value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaTLigne value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

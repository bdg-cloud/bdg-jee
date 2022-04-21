package fr.legrain.bdg.compteclient.service.droits;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.compteclient.dao.droits.IAuthURLDAO;
import fr.legrain.bdg.compteclient.dto.droits.TaAuthURLDTO;
import fr.legrain.bdg.compteclient.model.droits.TaAuthURL;
import fr.legrain.bdg.compteclient.model.mapping.mapper.TaAuthURLMapper;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaAuthURLServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;


/**
 * Session Bean implementation class TaAuthURLBean
 */
@Stateless
//@Interceptors(ServerTenantInterceptor.class)
public class TaAuthURLService extends AbstractApplicationDAOServer<TaAuthURL, TaAuthURLDTO> implements ITaAuthURLServiceRemote {

	static Logger logger = Logger.getLogger(TaAuthURLService.class);

	@Inject private IAuthURLDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaAuthURLService() {
		super(TaAuthURL.class,TaAuthURLDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaAuthURL a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaAuthURL transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaAuthURL transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaAuthURL persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaAuthURL merge(TaAuthURL detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaAuthURL merge(TaAuthURL detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaAuthURL findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaAuthURL findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaAuthURL> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAuthURLDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAuthURLDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaAuthURL> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAuthURLDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAuthURLDTO entityToDTO(TaAuthURL entity) {
//		TaAuthURLDTO dto = new TaAuthURLDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaAuthURLMapper mapper = new TaAuthURLMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAuthURLDTO> listEntityToListDTO(List<TaAuthURL> entity) {
		List<TaAuthURLDTO> l = new ArrayList<TaAuthURLDTO>();

		for (TaAuthURL taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAuthURLDTO> selectAllDTO() {
		System.out.println("List of TaAuthURLDTO EJB :");
		ArrayList<TaAuthURLDTO> liste = new ArrayList<TaAuthURLDTO>();

		List<TaAuthURL> projects = selectAll();
		for(TaAuthURL project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAuthURLDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAuthURLDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAuthURLDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAuthURLDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAuthURLDTO dto, String validationContext) throws EJBException {
		try {
			TaAuthURLMapper mapper = new TaAuthURLMapper();
			TaAuthURL entity = null;
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
	
	public void persistDTO(TaAuthURLDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAuthURLDTO dto, String validationContext) throws CreateException {
		try {
			TaAuthURLMapper mapper = new TaAuthURLMapper();
			TaAuthURL entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAuthURLDTO dto) throws RemoveException {
		try {
			TaAuthURLMapper mapper = new TaAuthURLMapper();
			TaAuthURL entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaAuthURL refresh(TaAuthURL persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaAuthURL value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaAuthURL value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAuthURLDTO dto, String validationContext) {
		try {
			TaAuthURLMapper mapper = new TaAuthURLMapper();
			TaAuthURL entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAuthURLDTO> validator = new BeanValidator<TaAuthURLDTO>(TaAuthURLDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAuthURLDTO dto, String propertyName, String validationContext) {
		try {
			TaAuthURLMapper mapper = new TaAuthURLMapper();
			TaAuthURL entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAuthURLDTO> validator = new BeanValidator<TaAuthURLDTO>(TaAuthURLDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAuthURLDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAuthURLDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaAuthURL value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaAuthURL value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}

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

import fr.legrain.bdg.compteclient.dao.droits.IUtilisateurDAO;
import fr.legrain.bdg.compteclient.dto.droits.TaUtilisateurDTO;
import fr.legrain.bdg.compteclient.model.droits.TaUtilisateur;
import fr.legrain.bdg.compteclient.model.mapping.mapper.TaUtilisateurMapper;
import fr.legrain.bdg.compteclient.service.interfaces.remote.droits.ITaUtilisateurServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;


/**
 * Session Bean implementation class TaUtilisateurBean
 */
@Stateless
//@Interceptors(ServerTenantInterceptor.class)
public class TaUtilisateurService extends AbstractApplicationDAOServer<TaUtilisateur, TaUtilisateurDTO> implements ITaUtilisateurServiceRemote {

	static Logger logger = Logger.getLogger(TaUtilisateurService.class);

	@Inject private IUtilisateurDAO dao;
	
	@Inject private	SessionInfo sessionInfo;
	
	@Inject private	TenantInfo tenantInfo;

	/**
	 * Default constructor. 
	 */
	public TaUtilisateurService() {
		super(TaUtilisateur.class,TaUtilisateurDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaUtilisateur a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaUtilisateur transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaUtilisateur transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaUtilisateur persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaUtilisateur merge(TaUtilisateur detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaUtilisateur merge(TaUtilisateur detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaUtilisateur findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaUtilisateur findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaUtilisateur> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaUtilisateurDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaUtilisateurDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaUtilisateur> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaUtilisateurDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaUtilisateurDTO entityToDTO(TaUtilisateur entity) {
//		TaUtilisateurDTO dto = new TaUtilisateurDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaUtilisateurMapper mapper = new TaUtilisateurMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaUtilisateurDTO> listEntityToListDTO(List<TaUtilisateur> entity) {
		List<TaUtilisateurDTO> l = new ArrayList<TaUtilisateurDTO>();

		for (TaUtilisateur taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaUtilisateurDTO> selectAllDTO() {
		System.out.println("List of TaUtilisateurDTO EJB :");
		ArrayList<TaUtilisateurDTO> liste = new ArrayList<TaUtilisateurDTO>();

		List<TaUtilisateur> projects = selectAll();
		for(TaUtilisateur project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaUtilisateurDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaUtilisateurDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaUtilisateurDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaUtilisateurDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaUtilisateurDTO dto, String validationContext) throws EJBException {
		try {
			TaUtilisateurMapper mapper = new TaUtilisateurMapper();
			TaUtilisateur entity = null;
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
	
	public void persistDTO(TaUtilisateurDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaUtilisateurDTO dto, String validationContext) throws CreateException {
		try {
			TaUtilisateurMapper mapper = new TaUtilisateurMapper();
			TaUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaUtilisateurDTO dto) throws RemoveException {
		try {
			TaUtilisateurMapper mapper = new TaUtilisateurMapper();
			TaUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaUtilisateur refresh(TaUtilisateur persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaUtilisateur value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaUtilisateur value, String propertyName, String validationContext) {
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
	public void validateDTO(TaUtilisateurDTO dto, String validationContext) {
		try {
			TaUtilisateurMapper mapper = new TaUtilisateurMapper();
			TaUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaUtilisateurDTO> validator = new BeanValidator<TaUtilisateurDTO>(TaUtilisateurDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaUtilisateurDTO dto, String propertyName, String validationContext) {
		try {
			TaUtilisateurMapper mapper = new TaUtilisateurMapper();
			TaUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaUtilisateurDTO> validator = new BeanValidator<TaUtilisateurDTO>(TaUtilisateurDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaUtilisateurDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaUtilisateurDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaUtilisateur value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaUtilisateur value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

	public TaUtilisateur getSessionInfo() {
		if(sessionInfo!=null && sessionInfo.getUtilisateur()!=null)
			return sessionInfo.getUtilisateur();
		else
			return null;
	}

	public String getTenantId() {
		return tenantInfo.getTenantId();
	}
	
	public TaUtilisateur ctrlSaisieEmail(String email) {
		return dao.ctrlSaisieEmail(email);
	}
}

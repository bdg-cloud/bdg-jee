package fr.legrain.droits.service;

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

import fr.legrain.bdg.droits.service.remote.ITaRRoleUtilisateurServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaRRoleUtilisateurMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.dao.IRRoleUtilisateurDAO;
import fr.legrain.droits.dto.TaRRoleUtilisateurDTO;
import fr.legrain.droits.model.TaRRoleUtilisateur;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaRRoleUtilisateurBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaRRoleUtilisateurService extends AbstractApplicationDAOServer<TaRRoleUtilisateur, TaRRoleUtilisateurDTO> implements ITaRRoleUtilisateurServiceRemote {

	static Logger logger = Logger.getLogger(TaRRoleUtilisateurService.class);

	@Inject private IRRoleUtilisateurDAO dao;

	/**
	 * Default constructor. 
	 */
	public TaRRoleUtilisateurService() {
		super(TaRRoleUtilisateur.class,TaRRoleUtilisateurDTO.class);
	}
	
	//	private String defaultJPQLQuery = "select a from TaRRoleUtilisateur a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaRRoleUtilisateur transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaRRoleUtilisateur transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaRRoleUtilisateur persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaRRoleUtilisateur merge(TaRRoleUtilisateur detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaRRoleUtilisateur merge(TaRRoleUtilisateur detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaRRoleUtilisateur findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaRRoleUtilisateur findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaRRoleUtilisateur> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaRRoleUtilisateurDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaRRoleUtilisateurDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaRRoleUtilisateur> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaRRoleUtilisateurDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaRRoleUtilisateurDTO entityToDTO(TaRRoleUtilisateur entity) {
//		TaRRoleUtilisateurDTO dto = new TaRRoleUtilisateurDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaRRoleUtilisateurMapper mapper = new TaRRoleUtilisateurMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaRRoleUtilisateurDTO> listEntityToListDTO(List<TaRRoleUtilisateur> entity) {
		List<TaRRoleUtilisateurDTO> l = new ArrayList<TaRRoleUtilisateurDTO>();

		for (TaRRoleUtilisateur taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaRRoleUtilisateurDTO> selectAllDTO() {
		System.out.println("List of TaRRoleUtilisateurDTO EJB :");
		ArrayList<TaRRoleUtilisateurDTO> liste = new ArrayList<TaRRoleUtilisateurDTO>();

		List<TaRRoleUtilisateur> projects = selectAll();
		for(TaRRoleUtilisateur project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaRRoleUtilisateurDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaRRoleUtilisateurDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaRRoleUtilisateurDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaRRoleUtilisateurDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaRRoleUtilisateurDTO dto, String validationContext) throws EJBException {
		try {
			TaRRoleUtilisateurMapper mapper = new TaRRoleUtilisateurMapper();
			TaRRoleUtilisateur entity = null;
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
	
	public void persistDTO(TaRRoleUtilisateurDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaRRoleUtilisateurDTO dto, String validationContext) throws CreateException {
		try {
			TaRRoleUtilisateurMapper mapper = new TaRRoleUtilisateurMapper();
			TaRRoleUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaRRoleUtilisateurDTO dto) throws RemoveException {
		try {
			TaRRoleUtilisateurMapper mapper = new TaRRoleUtilisateurMapper();
			TaRRoleUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaRRoleUtilisateur refresh(TaRRoleUtilisateur persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaRRoleUtilisateur value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaRRoleUtilisateur value, String propertyName, String validationContext) {
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
	public void validateDTO(TaRRoleUtilisateurDTO dto, String validationContext) {
		try {
			TaRRoleUtilisateurMapper mapper = new TaRRoleUtilisateurMapper();
			TaRRoleUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRRoleUtilisateurDTO> validator = new BeanValidator<TaRRoleUtilisateurDTO>(TaRRoleUtilisateurDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaRRoleUtilisateurDTO dto, String propertyName, String validationContext) {
		try {
			TaRRoleUtilisateurMapper mapper = new TaRRoleUtilisateurMapper();
			TaRRoleUtilisateur entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaRRoleUtilisateurDTO> validator = new BeanValidator<TaRRoleUtilisateurDTO>(TaRRoleUtilisateurDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaRRoleUtilisateurDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaRRoleUtilisateurDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaRRoleUtilisateur value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaRRoleUtilisateur value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

}

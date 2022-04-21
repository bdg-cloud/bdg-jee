package fr.legrain.droits.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurWebServiceServiceRemote;
import fr.legrain.bdg.login.service.remote.ITaUtilisateurLoginServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaUtilisateurWebServiceMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.droits.dao.IUtilisateurDAO;
import fr.legrain.droits.dao.IUtilisateurWebServiceDAO;
import fr.legrain.droits.dto.TaUtilisateurWebServiceDTO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.model.TaUtilisateurWebService;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.login.dao.IUtilisateurLoginDAO;
import fr.legrain.login.model.TaUtilisateurLogin;
import fr.legrain.login.service.TaUtilisateurLoginService;


/**
 * Session Bean implementation class TaUtilisateurWebServiceBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaUtilisateurWebServiceService extends AbstractApplicationDAOServer<TaUtilisateurWebService, TaUtilisateurWebServiceDTO> implements ITaUtilisateurWebServiceServiceRemote {

	static Logger logger = Logger.getLogger(TaUtilisateurWebServiceService.class);

	@Inject private IUtilisateurWebServiceDAO dao;
	@EJB private ITaUtilisateurServiceRemote daoUtilisateur;
	@EJB private ITaUtilisateurLoginServiceRemote daoUtilisateurLoginDb;
	
	@Inject private	SessionInfo sessionInfo;
	
	@Inject private	TenantInfo tenantInfo;

	/**
	 * Default constructor. 
	 */
	public TaUtilisateurWebServiceService() {
		super(TaUtilisateurWebService.class,TaUtilisateurWebServiceDTO.class);
	}
	
	public TaUtilisateurWebService login(String login, String password) throws EJBException {
		return dao.login(login, password);
	}
	
	public TaUtilisateurWebServiceDTO loginDTO(String login, String password) {
		return dao.loginDTO(login, password);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaUtilisateurWebService transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaUtilisateurWebService transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaUtilisateurWebService persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getId()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaUtilisateurWebService merge(TaUtilisateurWebService detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaUtilisateurWebService merge(TaUtilisateurWebService detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaUtilisateurWebService findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaUtilisateurWebService findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaUtilisateurWebService> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaUtilisateurWebServiceDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaUtilisateurWebServiceDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaUtilisateurWebService> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaUtilisateurWebServiceDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaUtilisateurWebServiceDTO entityToDTO(TaUtilisateurWebService entity) {
//		TaUtilisateurWebServiceDTO dto = new TaUtilisateurWebServiceDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaUtilisateurWebServiceMapper mapper = new TaUtilisateurWebServiceMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaUtilisateurWebServiceDTO> listEntityToListDTO(List<TaUtilisateurWebService> entity) {
		List<TaUtilisateurWebServiceDTO> l = new ArrayList<TaUtilisateurWebServiceDTO>();

		for (TaUtilisateurWebService taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaUtilisateurWebServiceDTO> selectAllDTO() {
		System.out.println("List of TaUtilisateurWebServiceDTO EJB :");
		ArrayList<TaUtilisateurWebServiceDTO> liste = new ArrayList<TaUtilisateurWebServiceDTO>();

		List<TaUtilisateurWebService> projects = selectAll();
		for(TaUtilisateurWebService project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaUtilisateurWebServiceDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaUtilisateurWebServiceDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaUtilisateurWebServiceDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaUtilisateurWebServiceDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaUtilisateurWebServiceDTO dto, String validationContext) throws EJBException {
		try {
			TaUtilisateurWebServiceMapper mapper = new TaUtilisateurWebServiceMapper();
			TaUtilisateurWebService entity = null;
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
	
	public void persistDTO(TaUtilisateurWebServiceDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaUtilisateurWebServiceDTO dto, String validationContext) throws CreateException {
		try {
			TaUtilisateurWebServiceMapper mapper = new TaUtilisateurWebServiceMapper();
			TaUtilisateurWebService entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaUtilisateurWebServiceDTO dto) throws RemoveException {
		try {
			TaUtilisateurWebServiceMapper mapper = new TaUtilisateurWebServiceMapper();
			TaUtilisateurWebService entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaUtilisateurWebService refresh(TaUtilisateurWebService persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaUtilisateurWebService value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaUtilisateurWebService value, String propertyName, String validationContext) {
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
	public void validateDTO(TaUtilisateurWebServiceDTO dto, String validationContext) {
		try {
			TaUtilisateurWebServiceMapper mapper = new TaUtilisateurWebServiceMapper();
			TaUtilisateurWebService entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaUtilisateurWebServiceDTO> validator = new BeanValidator<TaUtilisateurWebServiceDTO>(TaUtilisateurWebServiceDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaUtilisateurWebServiceDTO dto, String propertyName, String validationContext) {
		try {
			TaUtilisateurWebServiceMapper mapper = new TaUtilisateurWebServiceMapper();
			TaUtilisateurWebService entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaUtilisateurWebServiceDTO> validator = new BeanValidator<TaUtilisateurWebServiceDTO>(TaUtilisateurWebServiceDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaUtilisateurWebServiceDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaUtilisateurWebServiceDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaUtilisateurWebService value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaUtilisateurWebService value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
	}

	@Override
	public List<TaUtilisateurWebServiceDTO> findByCodeLight(String code) {
		return dao.findByCodeLight(code);
	}

	@Override
	public List<TaUtilisateurWebServiceDTO> findByNomLight(String nom) {
		return dao.findByNomLight(nom);
	}

	@Override
	public void synchroniseCompteUtilisateurDossierEtWebService() {
		List<TaUtilisateur> listeUtilisateurDossier = daoUtilisateur.selectAll(true);
		
		TaUtilisateurWebService uws = null;
		String username_logindb = null;
		TaUtilisateurLogin taUtilisateurLogin = null;
		
		for (TaUtilisateur taUtilisateur : listeUtilisateurDossier) {
			taUtilisateurLogin = null;
			username_logindb = null;
			username_logindb = taUtilisateur.getUsername()+"_"+tenantInfo.getTenantId();
			try {
				taUtilisateurLogin = daoUtilisateurLoginDb.findByCode(username_logindb);
			} catch (FinderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(taUtilisateurLogin!=null) {
				if(taUtilisateur.getPasswd()==null || taUtilisateur.getPasswd().trim().equals("")) {
					//copie du hash du mot de passe dans la table du dossier s'il n'y est pas deja, permet d'avoir une copie du hash en cas de problème sur login_db
					taUtilisateur.setPasswd(taUtilisateurLogin.getPasswd());
				}
				if(taUtilisateur.getTaUtilisateurWebService()!=null) {
					//mise a jour de l'utilisateur ws a partir de l'utilisateur dossier
					taUtilisateur.getTaUtilisateurWebService().setPasswd(taUtilisateurLogin.getPasswd());
					taUtilisateur.getTaUtilisateurWebService().setSysteme(taUtilisateur.getSysteme());
					taUtilisateur.getTaUtilisateurWebService().setActif(taUtilisateur.getActif());
				} else {
					//pas d'utilisateur ws pour cet utilisateur dossier, il faut en créer un
					uws = new TaUtilisateurWebService();
					uws.setLogin(taUtilisateur.getUsername());
					uws.setPasswd(taUtilisateurLogin.getPasswd());
					uws.setActif(taUtilisateur.getActif());
					uws.setSysteme(taUtilisateur.getSysteme());
					uws.setEmail(taUtilisateur.getEmail());
					uws.setAutorisations(taUtilisateur.getAutorisations());
	//				uws.setDescription(taUtilisateur.getNom());
					taUtilisateur.setTaUtilisateurWebService(uws);
				}
				daoUtilisateur.merge(taUtilisateur);
			}
			
		}
		
	}

}

package fr.legrain.dossier.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

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

import fr.legrain.autorisation.xml.ListeModules;
import fr.legrain.autorisation.xml.Module;
import fr.legrain.bdg.dossier.service.remote.ITaAutorisationsDossierServiceRemote;
import fr.legrain.bdg.lib.server.osgi.BdgProperties;
import fr.legrain.bdg.model.mapping.mapper.TaAutorisationsDossierMapper;
import fr.legrain.bdg.servicewebexterne.service.remote.ITaServiceWebExterneServiceRemote;
import fr.legrain.bdg.synchronisation.dossier.service.remote.ISynchronisationDossierServiceRemote;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.dossier.dao.IAutorisationsDossierDAO;
import fr.legrain.dossier.dto.TaAutorisationsDossierDTO;
import fr.legrain.dossier.model.TaAutorisationsDossier;
import fr.legrain.droits.model.IModulesProgramme;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.servicewebexterne.model.TaServiceWebExterne;

/**
 * Session Bean implementation class TaAutorisationsDossierBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaAutorisationsDossierService extends AbstractApplicationDAOServer<TaAutorisationsDossier, TaAutorisationsDossierDTO> implements ITaAutorisationsDossierServiceRemote {

	static Logger logger = Logger.getLogger(TaAutorisationsDossierService.class);

	@Inject private IAutorisationsDossierDAO dao;
	@EJB private ITaServiceWebExterneServiceRemote taServiceWebExterneService;
	@Inject private	SessionInfo sessionInfo;
	
	private String loginlgr;

	/**
	 * Default constructor. 
	 */
	public TaAutorisationsDossierService() {
		super(TaAutorisationsDossier.class,TaAutorisationsDossierDTO.class);
		
		BdgProperties p = new BdgProperties();
		loginlgr = p.getProperty(BdgProperties.KEY_LOGIN_LGR);

	}
	
	//	private String defaultJPQLQuery = "select a from TaAutorisationsDossier a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public TaAutorisationsDossier findInstance() {
		return dao.findInstance();
	}

	/**
	 * La liste des services web externe est disponible dans chaque dossier avec un propriété actif/inactif.
	 * Il est donc pour l'instant possible de mettre a jour cette propriété dans chaque dossier en fonction des modules autoriser
	 * TODO Mais il serait préférable de mettre cette liste de tous les services web externe disponible dans une base programme.
	 * Dans ce cas, il faudra changer ce fonctionnement car il y aura un lien entre cette table dossier et la table programme sous un autre forme
	 * peut etre un principe similaire au catalogue d'édition avec un catalogue de service web externe activable ?
	 */
	public void majDroitModuleDossierServiceWebExterne() {
		List<TaServiceWebExterne> listeServiceExistant = taServiceWebExterneService.selectAll();
		for (TaServiceWebExterne taServiceWebExterne : listeServiceExistant) {
			boolean actif = false;
			actif = autoriseMenu(taServiceWebExterne.getIdModuleBdgAutorisation(),null); 
			//utilisateur null donc même en loginlgr, les services ne seront pas disponibles s'il sne sont pas autorisés sur ce dossier
			taServiceWebExterne.setActif(actif);
			taServiceWebExterneService.merge(taServiceWebExterne);
		}
	}
	
	public List<String> listeAutorisationDossier(){
		List<String> listeAutorisationDossier = new LinkedList<String>();

		//Autorisations dossier
		TaAutorisationsDossier autorisationsDossier = findInstance();
		ListeModules listeModuleDossier = new ListeModules();
		listeModuleDossier = listeModuleDossier.recupModulesXml(autorisationsDossier.getFichier());
		for(Module m : listeModuleDossier.module) {
			//listeAutorisation.add(m.nomModule);
			listeAutorisationDossier.add(m.id);
		}
		
		return listeAutorisationDossier;
	}
	
	/*
	 * Cumul des autorisations du dossier, des roles de l'utilisateur et de l'utilisateur lui même
	 */
	public List<String> listeAutorisationUtilisateur(TaUtilisateur taUtilisateur) {
		List<String> listeAutorisation = null;
		if(listeAutorisation==null) {
			listeAutorisation=new LinkedList<String>();
			//Autorisations dossier
			List<String> listeAutorisationDossier = listeAutorisationDossier();
			
			//Autorisations roles
			
			//Autorisations utilisateur
			if(taUtilisateur!=null) {
				if(taUtilisateur.getAdminDossier()!=null && taUtilisateur.getAdminDossier()) {
					//L'utilisateur connecté est l'administrateur du dossier, il peu accéder à tous les modules du dossier
					listeAutorisation.addAll(listeAutorisationDossier);
				} else {
					//L'utilisateur connecté n'est pas l'administrateur du dossier, il faut consulté ses droits spécifique
					ListeModules listeModuleUtilisateur = new ListeModules();
					if(taUtilisateur.getAutorisations()!=null) {
						listeModuleUtilisateur = listeModuleUtilisateur.recupModulesXml(taUtilisateur.getAutorisations());
						for(Module m : listeModuleUtilisateur.module) {
							if(listeAutorisationDossier.contains(m.id)) {
								//le module est accessible pour ce dossier et l'utilisateur est autorisé à y accéder
								listeAutorisation.add(m.id);
							} else {
								//les droits de l'utilisateur sont supérieur à ceux du dossier ==> problème
							}
						}
					} else {
						//pas de droits spécifique pour cet utilisateur
					}
				}
			} else {
				//si on ne peut pas authentifier l'utilisateur, seul les autorisation du dossier sont actives
				listeAutorisation.addAll(listeAutorisationDossier);
			}
			
		}
		return listeAutorisation;
	}
	
	public Boolean autoriseMenu(String idMenu, TaUtilisateur taUtilisateur) {
		Boolean retour=false;
		List<String> listeAutorisation = null;
		if(listeAutorisation==null){
			listeAutorisation = listeAutorisationUtilisateur(taUtilisateur);
		}
		if(listeAutorisation!=null){
			retour= listeAutorisation.contains(idMenu);
		}
//		if(u.hasRole("admin")) {
//			retour = true;
//		}
		//if(taUtilisateur!=null && taUtilisateur.getUsername().equals("xxxxxx")) {
		if(taUtilisateur!=null && taUtilisateur.getUsername().equals(loginlgr)) {
			//utilisateur "legrain" créer sur tous les dossier, 
			retour = true;
		}

		return retour;
	}
	
	public boolean isDevLgr() {
		return isDevLgr(sessionInfo.getUtilisateur().getUsername());
	}
	
	public boolean isDevLgr(String username) {
		boolean isDev = false;
		if(username!=null && !username.equals("")) {
			if(username.equals(loginlgr) 
					) {
				isDev = true;
			}
		}
		return isDev;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaAutorisationsDossier transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaAutorisationsDossier transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaAutorisationsDossier persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdAutorisation()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaAutorisationsDossier merge(TaAutorisationsDossier detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaAutorisationsDossier merge(TaAutorisationsDossier detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaAutorisationsDossier findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaAutorisationsDossier findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaAutorisationsDossier> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaAutorisationsDossierDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaAutorisationsDossierDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaAutorisationsDossier> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaAutorisationsDossierDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaAutorisationsDossierDTO entityToDTO(TaAutorisationsDossier entity) {
//		TaAutorisationsDossierDossierDTO dto = new TaAutorisationsDossierDossierDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaAutorisationsDossierMapper mapper = new TaAutorisationsDossierMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaAutorisationsDossierDTO> listEntityToListDTO(List<TaAutorisationsDossier> entity) {
		List<TaAutorisationsDossierDTO> l = new ArrayList<TaAutorisationsDossierDTO>();

		for (TaAutorisationsDossier taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaAutorisationsDossierDTO> selectAllDTO() {
		System.out.println("List of TaAutorisationsDossierDossierDTO EJB :");
		ArrayList<TaAutorisationsDossierDTO> liste = new ArrayList<TaAutorisationsDossierDTO>();

		List<TaAutorisationsDossier> projects = selectAll();
		for(TaAutorisationsDossier project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaAutorisationsDossierDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaAutorisationsDossierDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaAutorisationsDossierDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaAutorisationsDossierDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaAutorisationsDossierDTO dto, String validationContext) throws EJBException {
		try {
			TaAutorisationsDossierMapper mapper = new TaAutorisationsDossierMapper();
			TaAutorisationsDossier entity = null;
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
	
	public void persistDTO(TaAutorisationsDossierDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaAutorisationsDossierDTO dto, String validationContext) throws CreateException {
		try {
			TaAutorisationsDossierMapper mapper = new TaAutorisationsDossierMapper();
			TaAutorisationsDossier entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaAutorisationsDossierDTO dto) throws RemoveException {
		try {
			TaAutorisationsDossierMapper mapper = new TaAutorisationsDossierMapper();
			TaAutorisationsDossier entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaAutorisationsDossier refresh(TaAutorisationsDossier persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaAutorisationsDossier value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaAutorisationsDossier value, String propertyName, String validationContext) {
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
	public void validateDTO(TaAutorisationsDossierDTO dto, String validationContext) {
		try {
			TaAutorisationsDossierMapper mapper = new TaAutorisationsDossierMapper();
			TaAutorisationsDossier entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAutorisationsDossierDossierDTO> validator = new BeanValidator<TaAutorisationsDossierDossierDTO>(TaAutorisationsDossierDossierDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaAutorisationsDossierDTO dto, String propertyName, String validationContext) {
		try {
			TaAutorisationsDossierMapper mapper = new TaAutorisationsDossierMapper();
			TaAutorisationsDossier entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaAutorisationsDossierDossierDTO> validator = new BeanValidator<TaAutorisationsDossierDossierDTO>(TaAutorisationsDossierDossierDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaAutorisationsDossierDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaAutorisationsDossierDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaAutorisationsDossier value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaAutorisationsDossier value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}



}

package fr.legrain.dossier.service;

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
import javax.jws.WebService;

import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.bdg.dossier.service.remote.ITaPreferencesServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaPreferencesMapper;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.dossier.dao.IPreferencesDAO;
import fr.legrain.dossier.dto.TaPreferencesDTO;
import fr.legrain.dossier.model.TaPreferences;
//import javax.ejb.Remote;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.lib.data.LibConversion;


@Stateless
@DeclareRoles("admin")
@WebService
@Interceptors(ServerTenantInterceptor.class)
public class TaPreferencesService extends AbstractApplicationDAOServer<TaPreferences, TaPreferencesDTO> implements ITaPreferencesServiceRemote {

	static Logger logger = Logger.getLogger(TaPreferencesService.class);

	@Inject private IPreferencesDAO dao;
	@Inject PreferencesDossier preferencesDossier;
	
	


	
	/**
	 * Default constructor. 
	 */
	public TaPreferencesService() {
		super(TaPreferences.class,TaPreferencesDTO.class);
	}
	
	public List<TaPreferences> findByCategorie(int idCategorie) {
		return dao.findByCategorie(idCategorie);
	}


	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaPreferences transientInstance) throws CreateException {
		persist(transientInstance, null);
	}
		
	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaPreferences transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaPreferences persistentInstance) throws RemoveException {
		dao.remove(persistentInstance);
		razPrefGeneral();
	}
	
	public TaPreferences merge(TaPreferences detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaPreferences merge(TaPreferences detachedInstance, String validationContext) {
		TaPreferences pref=null;
		validateEntity(detachedInstance, validationContext);
		pref =  dao.merge(detachedInstance);
		razPrefGeneral();
		return pref;
	}

	public TaPreferences findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaPreferences findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaPreferences> selectAll() {
		return dao.selectAll();
	}

	public List<TaPreferences> findByGroupe(String ecran) {
		return dao.findByGroupe(ecran);
	}
	
	public TaPreferences findByGoupeAndCle(String groupe,String cle) {
		return dao.findByGoupeAndCle(groupe,cle);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaPreferencesDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaPreferencesDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaPreferences> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaPreferencesDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaPreferencesDTO entityToDTO(TaPreferences entity) {
		TaPreferencesMapper mapper = new TaPreferencesMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaPreferencesDTO> listEntityToListDTO(List<TaPreferences> entity) {
		List<TaPreferencesDTO> l = new ArrayList<TaPreferencesDTO>();

		for (TaPreferences TaPreferences : entity) {
			l.add(entityToDTO(TaPreferences));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaPreferencesDTO> selectAllDTO() {
		System.out.println("List of TaPreferencesDTO EJB :");
		ArrayList<TaPreferencesDTO> liste = new ArrayList<TaPreferencesDTO>();

		List<TaPreferences> projects = selectAll();
		for(TaPreferences project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaPreferencesDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaPreferencesDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaPreferencesDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaPreferencesDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaPreferencesDTO dto, String validationContext) throws EJBException {
		try {
			TaPreferencesMapper mapper = new TaPreferencesMapper();
			TaPreferences entity = null;
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
	
	public void persistDTO(TaPreferencesDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaPreferencesDTO dto, String validationContext) throws CreateException {
		try {
			TaPreferencesMapper mapper = new TaPreferencesMapper();
			TaPreferences entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaPreferencesDTO dto) throws RemoveException {
		try {
			TaPreferencesMapper mapper = new TaPreferencesMapper();
			TaPreferences entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaPreferences refresh(TaPreferences persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaPreferences value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaPreferences value, String propertyName, String validationContext) {
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
	public void validateDTO(TaPreferencesDTO dto, String validationContext) {
		try {
			TaPreferencesMapper mapper = new TaPreferencesMapper();
			TaPreferences entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPreferencesDTO> validator = new BeanValidator<TaPreferencesDTO>(TaPreferencesDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaPreferencesDTO dto, String propertyName, String validationContext) {
		try {
			TaPreferencesMapper mapper = new TaPreferencesMapper();
			TaPreferences entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaPreferencesDTO> validator = new BeanValidator<TaPreferencesDTO>(TaPreferencesDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaPreferencesDTO dto) {
		validateDTO(dto,null);
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaPreferencesDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaPreferences value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaPreferences value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}


	public void  mergePreferenceInteger(String groupe,String cle,Integer valeur){
		TaPreferences pref= dao.findByGoupeAndCle(groupe,cle);
		if(pref!=null){
			pref=new TaPreferences();
			pref.setGroupe(groupe);
			pref.setCle(cle);
		}
			pref.setValeur(LibConversion.integerToString(valeur));
			dao.merge(pref);
	}
	
	public void  mergePreferenceBoolean(String groupe,String cle,Boolean valeur){
		TaPreferences pref= dao.findByGoupeAndCle(groupe,cle);
		if(pref==null){
			pref=new TaPreferences();
			pref.setGroupe(groupe);
			pref.setCle(cle);
		}
		if(valeur==null)valeur=false;
		pref.setValeur(LibConversion.booleanToStringLettre(valeur));
		dao.merge(pref);		
	}
	
	public void  mergePreferenceString(String groupe,String cle,String valeur){
		TaPreferences pref= dao.findByGoupeAndCle(groupe,cle);
		if(pref!=null){
			pref=new TaPreferences();
			pref.setGroupe(groupe);
			pref.setCle(cle);
		}
		pref.setValeur(valeur);
		dao.merge(pref);		
	}
	
	public String retourPreferenceString(String groupe,String cle){
		TaPreferences pref= dao.findByGoupeAndCle(groupe,cle);
		if(pref!=null)return pref.getValeur();
		return null;
	}
	
	public Boolean retourPreferenceBoolean(String groupe,String cle){
		TaPreferences pref= dao.findByGoupeAndCle(groupe,cle);
		if(pref!=null)return  LibConversion.StringToBoolean(pref.getValeur());
		return null;
	}
	
	public Integer retourPreferenceInteger(String groupe,String cle){
		TaPreferences pref= dao.findByGoupeAndCle(groupe,cle);
		if(pref!=null)return LibConversion.stringToInteger(pref.getValeur());
		return null;
	}

	
	@Override
	public void razPrefGeneral() {
		preferencesDossier.getMapPreferences().clear();
	}

	@Override
	public void chargePrefGeneral() {
//		nbMaxChargeListeArticle();
//		nbMaxChargeListeTiers();
//		grosDossier();
//		grosFichierArticle();
//		grosFichierTiers();
	}


		@Override
		public Integer nbMaxChargeListeArticle(){
			TaPreferences pref = preferencesDossier.getMapPreferences().get(ITaPreferencesServiceRemote.NB_CHARGE_MAX_LISTE_ARTICLE);
			if(pref!=null && pref.getValeur() != null)  return LibConversion.stringToInteger(pref.getValeur());			
				pref = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_OPTIMISATION, ITaPreferencesServiceRemote.NB_CHARGE_MAX_LISTE_ARTICLE);
				if(pref!=null && pref.getValeur() != null) {
					preferencesDossier.getMapPreferences().put(ITaPreferencesServiceRemote.NB_CHARGE_MAX_LISTE_ARTICLE, pref);
					return LibConversion.stringToInteger(pref.getValeur());
				}
				return preferencesDossier.nbMaxChargeListeArticle;			
		}
	
		
		@Override
		public Integer nbMaxChargeListeTiers(){
			TaPreferences pref = preferencesDossier.getMapPreferences().get(ITaPreferencesServiceRemote.NB_CHARGE_MAX_LISTE_TIERS);
			if(pref!=null && pref.getValeur() != null)  return LibConversion.stringToInteger(pref.getValeur());			
				pref = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_OPTIMISATION, ITaPreferencesServiceRemote.NB_CHARGE_MAX_LISTE_TIERS);
				if(pref!=null && pref.getValeur() != null) {
					preferencesDossier.getMapPreferences().put(ITaPreferencesServiceRemote.NB_CHARGE_MAX_LISTE_ARTICLE, pref);
					return LibConversion.stringToInteger(pref.getValeur());
				}
				return preferencesDossier.nbMaxChargeListeTiers;			
		}
		
		
		@Override
		public Boolean grosDossier(){
			TaPreferences pref = preferencesDossier.getMapPreferences().get(ITaPreferencesServiceRemote.GROS_DOSSIER);
			if(pref!=null && pref.getValeur() != null)  return LibConversion.StringToBoolean(pref.getValeur());			
				pref = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_OPTIMISATION, ITaPreferencesServiceRemote.GROS_DOSSIER);
				if(pref!=null && pref.getValeur() != null) {
					preferencesDossier.getMapPreferences().put(ITaPreferencesServiceRemote.NB_CHARGE_MAX_LISTE_ARTICLE, pref);
					return LibConversion.StringToBoolean(pref.getValeur());
				}
				return preferencesDossier.grosDossier;			
		}
		
		
		@Override
		public Boolean grosFichierArticle(){
			TaPreferences pref = preferencesDossier.getMapPreferences().get(ITaPreferencesServiceRemote.GROS_FICHIER_ARTICLE);
			if(pref!=null && pref.getValeur() != null)  return LibConversion.StringToBoolean(pref.getValeur());			
				pref = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_OPTIMISATION, ITaPreferencesServiceRemote.GROS_FICHIER_ARTICLE);
				if(pref!=null && pref.getValeur() != null) {
					preferencesDossier.getMapPreferences().put(ITaPreferencesServiceRemote.GROS_DOSSIER, pref);
					return LibConversion.StringToBoolean(pref.getValeur());
				}
				return preferencesDossier.grosFichierArticle;			
		}
		
		@Override
		public Boolean grosFichierTiers(){
			TaPreferences pref = preferencesDossier.getMapPreferences().get(ITaPreferencesServiceRemote.GROS_FICHIER_TIERS);
			if(pref!=null && pref.getValeur() != null)  return LibConversion.StringToBoolean(pref.getValeur());			
				pref = taPreferencesService.findByGoupeAndCle(ITaPreferencesServiceRemote.GROUPE_PREFERENCES_OPTIMISATION, ITaPreferencesServiceRemote.GROS_FICHIER_TIERS);
				if(pref!=null && pref.getValeur() != null) {
					preferencesDossier.getMapPreferences().put(ITaPreferencesServiceRemote.GROS_FICHIER_TIERS, pref);
					return LibConversion.StringToBoolean(pref.getValeur());
				}
				return preferencesDossier.grosFichierTiers;			
		}
	
}


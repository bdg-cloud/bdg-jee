package fr.legrain.cron.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.RemoveException;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.transaction.TransactionSynchronizationRegistry;

import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.hibernate.OptimisticLockException;

import fr.legrain.abonnement.stripe.service.MultitenantProxyTimerAbonnement;
import fr.legrain.abonnement.stripe.service.TimerCreerAvisEcheanceEtPaiementPrevuService;
//import fr.legrain.abonnement.stripe.service.TimerCreerPayerPaiementPrevuService;
import fr.legrain.abonnement.stripe.service.TimerDeclenchePaiementPrevuService;
import fr.legrain.bdg.abonnement.stripe.service.remote.ITaStripePaiementPrevuServiceRemote;
import fr.legrain.bdg.cron.service.remote.ITaCronServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaCronMapper;
import fr.legrain.cron.dao.ICronDAO;
import fr.legrain.cron.model.TaCron;
import fr.legrain.cron.model.dto.TaCronDTO;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.general.dao.IDatabaseDAO;
import fr.legrain.hibernate.multitenant.SchemaResolver;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;
import fr.legrain.tache.model.TaNotification;


/**
 * Session Bean implementation class TaCronBean
 */
@SuppressWarnings("deprecation")
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaCronService extends AbstractApplicationDAOServer<TaCron, TaCronDTO> implements ITaCronServiceRemote {

	static Logger logger = Logger.getLogger(TaCronService.class);

	@Inject private ICronDAO dao;
	
	@Inject private Event<TaNotification> events;
	
//	@EJB private TimerCreerPayerPaiementPrevuService timerCreerPayerPaiementPrevuService;
	@EJB private TimerDeclenchePaiementPrevuService timerDeclenchePaiementPrevuService;
	@EJB private TimerCreerAvisEcheanceEtPaiementPrevuService timerCreerAvisEcheanceEtPaiementPrevuService;
		
	private TransactionSynchronizationRegistry reg;
	private static final String TENANT_POUR_TRANSACTION_SCHEDULE = "demo"; //le dossier 'demo' existe toujours
	
	@EJB private ITaStripePaiementPrevuServiceRemote taStripePaiementPrevuService;
	
	@EJB private MultitenantProxyTimerAbonnement multitenantProxyTimerEvenement;
	
	private Map<String,TaCron> bibliothequeCron;
	
//	private String defaultJPQLQuery = "select a from TaCron a";

	//@EJB private DatabaseService s;
	@Inject private IDatabaseDAO daoDb;
	@Resource TimerService timerService;

	/**
	 * Default constructor. 
	 */
	public TaCronService() {
		super(TaCron.class,TaCronDTO.class);
		
	}
	
	public void initBibliothequeCron() {
		bibliothequeCron = new HashMap<>();
		
		/*
		INSERT INTO ta_cron (systeme, visible, actif, est_unique, schema_tenant, code, libelle, description, type_cron, identifiant_unique, 
second, minute, hour, day_of_month, month, day_of_week, year, version_obj) VALUES ( 
true, DEFAULT, true, true, null, 'CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT', 
null, 'creation des avis d''échéances et programmation des paiements', null, null, 
'0', '0', '2', null, '*', null, '*', 0);
		 */
		TaCron taCron = new TaCron();
		taCron.setSysteme(true);
		taCron.setVisible(true);
		taCron.setActif(true);
		taCron.setEstUnique(true);
//		taCron.setSchemaTenant(null);
		taCron.setCode(TaCron.CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT);
		taCron.setLibelle("");
		taCron.setDescription("Création des avis d'échéances et programmation des paiements");
		taCron.setTypeCron("");
		taCron.setIdentifiantUnique("");
		taCron.setSecond("0");
		taCron.setMinute("0");
		taCron.setHour("2");
		taCron.setDayOfMonth(null);
		taCron.setMonth("*");
		taCron.setDayOfWeek(null);
		taCron.setYear("*");
		bibliothequeCron.put(TaCron.CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT, taCron);
		
		TaCron taCronPaiement = new TaCron();
		taCronPaiement.setSysteme(true);
		taCronPaiement.setVisible(true);
		taCronPaiement.setActif(true);
		taCronPaiement.setEstUnique(true);
//		taCronPaiement.setSchemaTenant(null);
		taCronPaiement.setCode(TaCron.CRON_SYS_DECLENCHE_PAIEMENT_STRIPE);
		taCronPaiement.setLibelle("");
		taCronPaiement.setDescription("Déclenchement des paiements en attente");
		taCronPaiement.setTypeCron("");
		taCronPaiement.setIdentifiantUnique("");
		taCronPaiement.setSecond("0");
		taCronPaiement.setMinute("0");
		taCronPaiement.setHour("2");
		taCronPaiement.setDayOfMonth(null);
		taCronPaiement.setMonth("*");
		taCronPaiement.setDayOfWeek(null);
		taCronPaiement.setYear("*");
		bibliothequeCron.put(TaCron.CRON_SYS_DECLENCHE_PAIEMENT_STRIPE, taCronPaiement);
	}
	
	public void miseAJourCronSystemeDuDossier() {
		initBibliothequeCron();
		SchemaResolver sr = new SchemaResolver();
		String schemaTenant = sr.resolveCurrentTenantIdentifier();
		
		for (String codeCron : bibliothequeCron.keySet()) {
			//L'affectation du tenant avec la variable postgresql TG_TABLE_SCHEMA via le trigger before insert semble ne pas fonctionner dans tous les cas
			bibliothequeCron.get(codeCron).setSchemaTenant(schemaTenant); 
			
			TaCron taCron = dao.findByCode(codeCron);
			if(taCron == null) {
				//insérer
				taCron = dao.merge(bibliothequeCron.get(codeCron));
				activerCronSysteme(codeCron,null);
			} else {
				//mettre à jour ?
				
			}
		}
	}
	
	
	public void activerCronSysteme(String codeCron, Object param) {
		TaCron taCron = dao.findByCode(codeCron);
		TimerHandle h = null;
		if(taCron != null) {
			if(taCron.getTimerHandle()!=null) { //si un timer existe deja, le supprimer et le re creer
				h = (TimerHandle) SerializationUtils.deserialize(taCron.getTimerHandle());
				try {
					if(h!=null) {
						Timer t = h.getTimer();
						t.cancel();
					}
				} catch (NoSuchObjectLocalException ex) {
					System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
				}
			}
			taCron.setActif(true);
			//if(taCron.getTypeCron().equals(TimerCreerAvisEcheanceEtPaiementPrevuService.class.getName())) {
			if(taCron.getCode().equals(TaCron.CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT)) {
				h = timerCreerAvisEcheanceEtPaiementPrevuService.creerTimer(taCron, param);
			//} else if(taCron.getTypeCron().equals(TimerDeclenchePaiementPrevuService.class.getName())) {
			} else if(taCron.getCode().equals(TaCron.CRON_SYS_DECLENCHE_PAIEMENT_STRIPE)) {
				h = timerDeclenchePaiementPrevuService.creerTimer(taCron, param);
			}
			
			taCron.setTimerHandle(SerializationUtils.serialize((Serializable) h));
			taCron = dao.merge(taCron);
		}
	}
	
	public void desactiverCronSysteme(String codeCron) {
		TaCron taCron = dao.findByCode(codeCron);
		TimerHandle h = null;
		if(taCron != null) {
			if(taCron.getTimerHandle()!=null) { 
				h = (TimerHandle) SerializationUtils.deserialize(taCron.getTimerHandle());
				try {
					Timer t = h.getTimer();
					t.cancel();
					taCron.setTimerHandle(null);
				} catch (NoSuchObjectLocalException ex) {
					System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
				}
			}
			taCron.setActif(false);
			taCron = dao.merge(taCron);
		}
	}
	
	public void creerCron(TaCron taCron, Object param) {
		TimerHandle h = null;
		if(taCron.getTypeCron().equals(TimerCreerAvisEcheanceEtPaiementPrevuService.class.getName())) {
			h = timerCreerAvisEcheanceEtPaiementPrevuService.creerTimer(taCron, param);
		} else if(taCron.getTypeCron().equals(TimerDeclenchePaiementPrevuService.class.getName())) {
			h = timerDeclenchePaiementPrevuService.creerTimer(taCron, param);
		}
		
		taCron.setTimerHandle(SerializationUtils.serialize((Serializable) h));
		//TODO re/enregistrer l'objet TaCron qui possède maintenant le timerHandler
	}
	
	public void supprimerCron(TaCron taCron) {
		if(taCron.getTimerHandle()!=null) {
			TimerHandle h = (TimerHandle) SerializationUtils.deserialize(taCron.getTimerHandle());
			try {
				Timer t = h.getTimer();
				t.cancel();
			} catch (NoSuchObjectLocalException ex) {
				System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
			}
		}
	}
	
	public void modifierCron(TaCron taCron) {
		if(taCron.getTimerHandle()!=null) {
			//un timer existe deja pour cette notification, on l'annule et on le recréer
			TimerHandle h = (TimerHandle) SerializationUtils.deserialize(taCron.getTimerHandle());
			try {
				Timer t = h.getTimer();
				t.cancel();
			} catch (NoSuchObjectLocalException ex) {
				System.out.println("Le timer pour cette notification n'existe pas/plus. Il a été annulé ou dejà executé.");
			}
		}
	}
	
	/**
	 * Cette méthode vérifie si le CRON responsable de la génération des prochaines échéances, de la suspension et suppression des échéances,
	 *  et suspension ou annulation des lignes d'abo est bien passé il y a moins de 24 heures
	 */
	public boolean verifPassageCRONGenerationEcheance() {
		boolean cronValide = true;
		try {
			TaCron cron = findByCode(TaCron.CRON_SYS_CREATION_AVIS_ECHEANCE_ET_PROGRAMMATION_PAIEMENT);
			if(cron != null && cron.getDateDerniereExecution() != null) {
				Date now = new Date();
				Date dernierPassage = cron.getDateDerniereExecution();
				LocalDateTime localDateNow = new Date(now.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				LocalDateTime localDateTimeDernierPassage = new Date(dernierPassage.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				//on calcule le nombre d'heure écoulées depuis le dernier passage CRON
				long heures = ChronoUnit.HOURS.between(localDateTimeDernierPassage, localDateNow);
				
				//si il est plus grand que 24 heures, c'est que le dernier passage CRON ne s'est pas fait
				if(heures > 24) {
					cronValide = false;
				}
				
			}
			
			
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cronValide;
		
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaCron transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaCron transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaCron persistentInstance) throws RemoveException {
		dao.remove(dao.findById(persistentInstance.getIdCron()));
	}
	
	public TaCron merge(TaCron detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaCron merge(TaCron detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaCron findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaCron findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaCron> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaCronDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaCronDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaCron> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaCronDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaCronDTO entityToDTO(TaCron entity) {
//		TaCronDTO dto = new TaCronDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaCronMapper mapper = new TaCronMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaCronDTO> listEntityToListDTO(List<TaCron> entity) {
		List<TaCronDTO> l = new ArrayList<TaCronDTO>();

		for (TaCron taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaCronDTO> selectAllDTO() {
		System.out.println("List of TaCronDTO EJB :");
		ArrayList<TaCronDTO> liste = new ArrayList<TaCronDTO>();

		List<TaCron> projects = selectAll();
		for(TaCron project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaCronDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaCronDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaCronDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaCronDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaCronDTO dto, String validationContext) throws EJBException {
		try {
			TaCronMapper mapper = new TaCronMapper();
			TaCron entity = null;
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
	
	public void persistDTO(TaCronDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaCronDTO dto, String validationContext) throws CreateException {
		try {
			TaCronMapper mapper = new TaCronMapper();
			TaCron entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaCronDTO dto) throws RemoveException {
		try {
			TaCronMapper mapper = new TaCronMapper();
			TaCron entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaCron refresh(TaCron persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaCron value, String validationContext) /*throws ExceptLgr*/ {
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
	public void validateEntityProperty(TaCron value, String propertyName, String validationContext) {
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
	public void validateDTO(TaCronDTO dto, String validationContext) {
		try {
			TaCronMapper mapper = new TaCronMapper();
			TaCron entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCronDTO> validator = new BeanValidator<TaCronDTO>(TaCronDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaCronDTO dto, String propertyName, String validationContext) {
		try {
			TaCronMapper mapper = new TaCronMapper();
			TaCron entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaCronDTO> validator = new BeanValidator<TaCronDTO>(TaCronDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaCronDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaCronDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaCron value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaCron value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}

}

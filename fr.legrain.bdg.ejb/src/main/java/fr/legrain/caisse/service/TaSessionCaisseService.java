package fr.legrain.caisse.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
//import org.testng.Assert.ThrowingRunnable;

import fr.legrain.bdg.caisse.service.remote.ITaDepotRetraitCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaFondDeCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaSessionCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaTDepotRetraitCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaTFondDeCaisseServiceRemote;
import fr.legrain.bdg.caisse.service.remote.ITaTLSessionCaisseServiceRemote;
import fr.legrain.bdg.controle.service.remote.ITaGenCodeExServiceRemote;
import fr.legrain.bdg.documents.service.remote.ITaTicketDeCaisseServiceRemote;
import fr.legrain.bdg.dossier.service.remote.ITaInfoEntrepriseServiceRemote;
import fr.legrain.bdg.droits.service.remote.ITaUtilisateurServiceRemote;
import fr.legrain.bdg.model.mapping.mapper.TaSessionCaisseMapper;
import fr.legrain.caisse.dao.IDepotRetraitCaisseDAO;
import fr.legrain.caisse.dao.IFondDeCaisseDAO;
import fr.legrain.caisse.dao.ISessionCaisseDAO;
import fr.legrain.caisse.dto.TaSessionCaisseDTO;
import fr.legrain.caisse.model.TaDepotRetraitCaisse;
import fr.legrain.caisse.model.TaFondDeCaisse;
import fr.legrain.caisse.model.TaLSessionCaisse;
import fr.legrain.caisse.model.TaSessionCaisse;
import fr.legrain.caisse.model.TaTDepotRetraitCaisse;
import fr.legrain.caisse.model.TaTFondDeCaisse;
import fr.legrain.caisse.model.TaTLSessionCaisse;
import fr.legrain.data.AbstractApplicationDAOServer;
import fr.legrain.document.dto.TaTicketDeCaisseDTO;
import fr.legrain.document.model.TaTicketDeCaisse;
import fr.legrain.documents.dao.ITicketDeCaisseDAO;
import fr.legrain.droits.model.TaUtilisateur;
import fr.legrain.droits.service.SessionInfo;
import fr.legrain.hibernate.multitenant.ServerTenantInterceptor;


/**
 * Session Bean implementation class TaSessionCaisseBean
 */
@Stateless
@DeclareRoles("admin")
@Interceptors(ServerTenantInterceptor.class)
public class TaSessionCaisseService extends AbstractApplicationDAOServer<TaSessionCaisse, TaSessionCaisseDTO> implements ITaSessionCaisseServiceRemote {

	static Logger logger = Logger.getLogger(TaSessionCaisseService.class);

	@Inject private ISessionCaisseDAO dao;
	
	@Inject private ITicketDeCaisseDAO daoTicketDeCaisse;
	@Inject private IDepotRetraitCaisseDAO daoDepotRetraitCaisse;
	@Inject private IFondDeCaisseDAO daoFondDeCaisse;
	
	@Inject private	SessionInfo sessionInfo;
	@EJB private ITaGenCodeExServiceRemote gencode;
	@EJB private ITaInfoEntrepriseServiceRemote taInfoEntrepriseService;
	
	@EJB private ITaTLSessionCaisseServiceRemote taTLSessionCaisseService;
	@EJB private ITaTicketDeCaisseServiceRemote taTicketDeCaisseService;
	
	@EJB private ITaTFondDeCaisseServiceRemote taTFondDeCaisseService;
	@EJB private ITaFondDeCaisseServiceRemote taFondDeCaisseService;
	@EJB private ITaTDepotRetraitCaisseServiceRemote taTDepotRetraitCaisseService;
	@EJB private ITaDepotRetraitCaisseServiceRemote taDepotRetraitCaisseService;
	
	@EJB private ITaUtilisateurServiceRemote taUtilisateurService;

	/**
	 * Default constructor. 
	 */
	public TaSessionCaisseService() {
		super(TaSessionCaisse.class,TaSessionCaisseDTO.class);
	}
	
	public TaSessionCaisse calculMontantSessionCaisse(TaSessionCaisse session) {
		return calculMontantSessionCaisse(session,true);
	}
	
	public TaSessionCaisse calculMontantSessionCaisse(TaSessionCaisse session, boolean update) {
		List<TaTLSessionCaisse> listeTypeLigne = taTLSessionCaisseService.selectAll();
		List<TaTicketDeCaisseDTO> listeTicketSession = taTicketDeCaisseService.findAllDTOPeriode(session.getDateDebutSession(), session.getDateFinSession());
		
		for (TaTicketDeCaisseDTO ticket : listeTicketSession) {
			//Totaux généraux
		
			//Totaux par type de ligne
			for (TaTLSessionCaisse taTLSessionCaisse : listeTypeLigne) {
				boolean trouve = false;
				for (TaLSessionCaisse ligne : session.getLignes()) {
					if(taTLSessionCaisse.getCodeTLSessionCaisse().equals(ligne.getTaTLSessionCaisse().getCodeTLSessionCaisse())) {
						trouve = true;
						if(taTLSessionCaisse.getCodeTLSessionCaisse().equals(TaTLSessionCaisse.TAUX_TVA)) {
							
						} else if(taTLSessionCaisse.getCodeTLSessionCaisse().equals(TaTLSessionCaisse.TYPE_PAIEMENT)) {
							
						} else if(taTLSessionCaisse.getCodeTLSessionCaisse().equals(TaTLSessionCaisse.ARTICLE)) {
						} else if(taTLSessionCaisse.getCodeTLSessionCaisse().equals(TaTLSessionCaisse.FAMILLE_ARTICLE)) {
						} else if(taTLSessionCaisse.getCodeTLSessionCaisse().equals(TaTLSessionCaisse.VENDEUR)) {
							
						}
					}
					if(!trouve) {
						//créer la ligne de ce type pour ce ticket avec un montant à 0
					}
				}
			}
		
		}
		throw new EJBException("a finir");
		//return null; //TODO a finir
	}
	
	public TaSessionCaisse findSessionCaisseActive(int idUtilisateur, String numeroCaisse) {
		return dao.findSessionCaisseActive(idUtilisateur, numeroCaisse);
	}
	
	public TaSessionCaisse demarrerSessionCaisse() {
		return demarrerSessionCaisse(null);
	}
	
	public TaSessionCaisse demarrerSessionCaisse(TaFondDeCaisse fondDeCaisse) {
		try {
			TaSessionCaisse s = findSessionCaisseActive(sessionInfo.getUtilisateur().getId(),null);
			if(s==null) {
				Date d = new Date();
				//il n'y a pas de session deja en cours, on peut en commencer une
				s = new TaSessionCaisse();
				TaUtilisateur u = taUtilisateurService.findById(sessionInfo.getUtilisateur().getId());
				
				//gencode.libereVerrouTout(new ArrayList<String>(SessionListener.getSessions().keySet()));			
//				if(s.getCodeSessionCaisse()!=null) {
//					annuleCode(s.getCodeSessionCaisse());
//				}	
				s.setCodeSessionCaisse(genereCode(null));
				s.setTaUtilisateur(u);
				
				s.setDateSession(d);
				s.setDateDebutSession(d);
				s.setAutomatique(false);
				
				if(fondDeCaisse!=null) {
					fondDeCaisse.setTaSessionCaisse(s);
					fondDeCaisse.setTaUtilisateur(u);
					fondDeCaisse.setDate(d);
					fondDeCaisse.setTaTFondDeCaisse(taTFondDeCaisseService.findByCode(TaTFondDeCaisse.OUVERTURE));
					s.setTaFondDeCaisseOuverture(fondDeCaisse);
				}
				
				s = merge(s);
			} else {
				//une session est deja en cours
			}
			return s;
			//déclanche fond de caisse ouverture
			//Création des différents TaLSessionCaisse à 0 ?
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaSessionCaisse cloturerSessionCaisse() {
		return cloturerSessionCaisse(null);
	}
	
	public TaSessionCaisse cloturerSessionCaisse(TaFondDeCaisse fondDeCaisse) {
		//Ticket Z
		try {
			TaSessionCaisse s = findSessionCaisseActive(sessionInfo.getUtilisateur().getId(),null);
			if(s!=null) {
				//il y a bien une session en cours
				
				TaUtilisateur u = taUtilisateurService.findById(sessionInfo.getUtilisateur().getId());
				Date d = new Date();
				
				s.setDateFinSession(new Date());
				s.setVerrouillageTicketZ(true);
				
				if(fondDeCaisse!=null) {
					fondDeCaisse.setTaSessionCaisse(s);
					fondDeCaisse.setTaUtilisateur(s.getTaUtilisateur());
					fondDeCaisse.setDate(d);
					fondDeCaisse.setTaTFondDeCaisse(taTFondDeCaisseService.findByCode(TaTFondDeCaisse.CLOTURE));
					s.setTaFondDeCaisseCloture(fondDeCaisse);
				}
				
				//calcul des totaux
				
				//verrouillage de tous les ticket de la période + depot-retrait + fond de caisse
				List<TaTicketDeCaisse> listeTicketDeCaisseSession = daoTicketDeCaisse.findBySessionCaisseCourante();
				for (TaTicketDeCaisse taTicketDeCaisse : listeTicketDeCaisseSession) {
					taTicketDeCaisse.setTaSessionCaisse(s);
					taTicketDeCaisseService.merge(taTicketDeCaisse);
				}
				List<TaDepotRetraitCaisse> listeDepotRetraitSession = daoDepotRetraitCaisse.findBySessionCaisseCourante();
				for (TaDepotRetraitCaisse taDepotRetraitCaisse : listeDepotRetraitSession) {
					taDepotRetraitCaisse.setTaSessionCaisse(s);
					taDepotRetraitCaisseService.merge(taDepotRetraitCaisse);
				}
				List<TaFondDeCaisse> listeFondDeCaisseSession = daoFondDeCaisse.findBySessionCaisseCourante();
				for (TaFondDeCaisse taFondDeCaisse : listeFondDeCaisseSession) {
					taFondDeCaisse.setTaSessionCaisse(s);
					taFondDeCaisseService.merge(taFondDeCaisse);
				}
				
				
				
				s = merge(s);
			} else {
				//pas de session en cours
			}
			//Sur le TaSessionCaisse courant mettre la date de cloture/verouillage
			//Faire tout les cumuls définitif généraux et cumul des TaLSessionCaisse
			//déclanche fond de caisse cloture
			return s;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TaSessionCaisse deposerEnCaisse(TaDepotRetraitCaisse depotCaisse) {
		try {
			TaSessionCaisse s = findSessionCaisseActive(sessionInfo.getUtilisateur().getId(),null);
			if(s!=null) {
				if(depotCaisse!=null) {
					//depotCaisse.setTaSessionCaisse(s);
					depotCaisse.setTaUtilisateur(s.getTaUtilisateur());
					depotCaisse.setDate(new Date());
					depotCaisse.setTaTDepotRetraitCaisse(taTDepotRetraitCaisseService.findByCode(TaTDepotRetraitCaisse.DEPOT));
					taDepotRetraitCaisseService.merge(depotCaisse);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public TaSessionCaisse retirerCaisse(TaDepotRetraitCaisse retraitCaisse) {
		try {
			TaSessionCaisse s = findSessionCaisseActive(sessionInfo.getUtilisateur().getId(),null);
			if(s!=null) {
				if(retraitCaisse!=null) {
					//retraitCaisse.setTaSessionCaisse(s);
					retraitCaisse.setTaUtilisateur(s.getTaUtilisateur());
					retraitCaisse.setDate(new Date());
					retraitCaisse.setTaTDepotRetraitCaisse(taTDepotRetraitCaisseService.findByCode(TaTDepotRetraitCaisse.RETRAIT));
					taDepotRetraitCaisseService.merge(retraitCaisse);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public TaSessionCaisse ecartCaisse(TaFondDeCaisse ecartCaisse) {
		try {
			TaSessionCaisse s = findSessionCaisseActive(sessionInfo.getUtilisateur().getId(),null);
			if(s!=null) {
				if(ecartCaisse!=null) {
					ecartCaisse.setTaSessionCaisse(s);
					ecartCaisse.setTaUtilisateur(s.getTaUtilisateur());
					ecartCaisse.setDate(new Date());
					ecartCaisse.setTaTFondDeCaisse(taTFondDeCaisseService.findByCode(TaTFondDeCaisse.ECART));
					taFondDeCaisseService.merge(ecartCaisse);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String genereCode( Map<String, String> params) {
		//return dao.genereCode();
		try {
			return gencode.genereCodeJPA(TaSessionCaisse.class.getSimpleName(),params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "NOUVEAU CODE";
	}
	
	public void annuleCode(String code) {
		try {
			
			gencode.annulerCodeGenere(gencode.findByCode(TaSessionCaisse.class.getSimpleName()),code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void verrouilleCode(String code) {
		try {
			gencode.rentreCodeGenere(gencode.findByCode(TaSessionCaisse.class.getSimpleName()),code, sessionInfo.getSessionID());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//	private String defaultJPQLQuery = "select a from TaSessionCaisse a";

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										ENTITY
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void persist(TaSessionCaisse transientInstance) throws CreateException {
		persist(transientInstance, null);
	}

	//@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@WebMethod(operationName = "persistValidationContext")
	public void persist(TaSessionCaisse transientInstance, String validationContext) throws CreateException {

		validateEntity(transientInstance, validationContext);

		dao.persist(transientInstance);
	}

	public void remove(TaSessionCaisse persistentInstance) throws RemoveException {
		try {
			dao.remove(findById(persistentInstance.getIdSessionCaisse()));
		} catch (FinderException e) {
			logger.error("", e);
		}
	}
	
	public TaSessionCaisse merge(TaSessionCaisse detachedInstance) {
		return merge(detachedInstance, null);
	}

	@Override
	@WebMethod(operationName = "mergeValidationContext")
	public TaSessionCaisse merge(TaSessionCaisse detachedInstance, String validationContext) {
		validateEntity(detachedInstance, validationContext);

		return dao.merge(detachedInstance);
	}

	public TaSessionCaisse findById(int id) throws FinderException {
		return dao.findById(id);
	}

	public TaSessionCaisse findByCode(String code) throws FinderException {
		return dao.findByCode(code);
	}

//	@RolesAllowed("admin")
	public List<TaSessionCaisse> selectAll() {
		return dao.selectAll();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 										DTO
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<TaSessionCaisseDTO> findWithNamedQueryDTO(String queryName) throws FinderException {
		return null;
	}

	@Override
	public List<TaSessionCaisseDTO> findWithJPQLQueryDTO(String JPQLquery) throws FinderException {
		List<TaSessionCaisse> entityList = dao.findWithJPQLQuery(JPQLquery);
		List<TaSessionCaisseDTO> l = null;
		if(entityList!=null) {
			l = listEntityToListDTO(entityList);
		}
		return l;
	}

	public TaSessionCaisseDTO entityToDTO(TaSessionCaisse entity) {
//		TaSessionCaisseDTO dto = new TaSessionCaisseDTO();
//		dto.setId(entity.getIdTCivilite());
//		dto.setCodeTCivilite(entity.getCodeTCivilite());
//		return dto;
		TaSessionCaisseMapper mapper = new TaSessionCaisseMapper();
		return mapper.mapEntityToDto(entity, null);
	}

	public List<TaSessionCaisseDTO> listEntityToListDTO(List<TaSessionCaisse> entity) {
		List<TaSessionCaisseDTO> l = new ArrayList<TaSessionCaisseDTO>();

		for (TaSessionCaisse taTCivilite : entity) {
			l.add(entityToDTO(taTCivilite));
		}

		return l;
	}

//	@RolesAllowed("admin")
	public List<TaSessionCaisseDTO> selectAllDTO() {
		System.out.println("List of TaSessionCaisseDTO EJB :");
		ArrayList<TaSessionCaisseDTO> liste = new ArrayList<TaSessionCaisseDTO>();

		List<TaSessionCaisse> projects = selectAll();
		for(TaSessionCaisse project : projects) {
			liste.add(entityToDTO(project));
		}

		return liste;
	}

	public TaSessionCaisseDTO findByIdDTO(int id) throws FinderException {
		return entityToDTO(findById(id));
	}

	public TaSessionCaisseDTO findByCodeDTO(String code) throws FinderException {
		return entityToDTO(findByCode(code));
	}

	@Override
	public void error(TaSessionCaisseDTO dto) {
		throw new EJBException("Test erreur EJB");
	}

	@Override
	public int selectCount() {
		return dao.selectAll().size();
		//return 0;
	}
	
	public void mergeDTO(TaSessionCaisseDTO dto) throws EJBException {
		mergeDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "mergeDTOValidationContext")
	public void mergeDTO(TaSessionCaisseDTO dto, String validationContext) throws EJBException {
		try {
			TaSessionCaisseMapper mapper = new TaSessionCaisseMapper();
			TaSessionCaisse entity = null;
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
	
	public void persistDTO(TaSessionCaisseDTO dto) throws CreateException {
		persistDTO(dto, null);
	}

	@Override
	@WebMethod(operationName = "persistDTOValidationContext")
	public void persistDTO(TaSessionCaisseDTO dto, String validationContext) throws CreateException {
		try {
			TaSessionCaisseMapper mapper = new TaSessionCaisseMapper();
			TaSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.persist(entity);
			enregistrerPersist(entity, validationContext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}

	@Override
	public void removeDTO(TaSessionCaisseDTO dto) throws RemoveException {
		try {
			TaSessionCaisseMapper mapper = new TaSessionCaisseMapper();
			TaSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			//dao.remove(entity);
			supprimer(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException(e.getMessage());
		}
	}

	@Override
	protected TaSessionCaisse refresh(TaSessionCaisse persistentInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod(operationName = "validateEntityValidationContext")
	public void validateEntity(TaSessionCaisse value, String validationContext) /*throws ExceptLgr*/ {
		
//		TransactionSynchronizationRegistry reg;
		try {
//			reg = (TransactionSynchronizationRegistry) new InitialContext().lookup("java:comp/TransactionSynchronizationRegistry");
//			if(reg!=null && reg.getResource("tenant")!=null ) {
//				System.out.println("Pas de validation ==> client lourd");
//			} else {
	
				if(validationContext==null) validationContext="";
				validateAll(value,validationContext,false); //ancienne validation, extraction de l'annotation et appel
				//dao.validate(value); //validation automatique via la JSR bean validation
//			}
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateEntityPropertyValidationContext")
	public void validateEntityProperty(TaSessionCaisse value, String propertyName, String validationContext) {
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
	public void validateDTO(TaSessionCaisseDTO dto, String validationContext) {
		try {
			TaSessionCaisseMapper mapper = new TaSessionCaisseMapper();
			TaSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntity(entity,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaSessionCaisseDTO> validator = new BeanValidator<TaSessionCaisseDTO>(TaSessionCaisseDTO.class);
//			validator.validate(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}
	}

	@Override
	@WebMethod(operationName = "validateDTOPropertyValidationContext")
	public void validateDTOProperty(TaSessionCaisseDTO dto, String propertyName, String validationContext) {
		try {
			TaSessionCaisseMapper mapper = new TaSessionCaisseMapper();
			TaSessionCaisse entity = mapper.mapDtoToEntity(dto,null);
			validateEntityProperty(entity,propertyName,validationContext);
			
			//validation automatique via la JSR bean validation
//			BeanValidator<TaSessionCaisseDTO> validator = new BeanValidator<TaSessionCaisseDTO>(TaSessionCaisseDTO.class);
//			validator.validateField(dto,propertyName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EJBException(e.getMessage());
		}

	}
	
	@Override
	@WebMethod(operationName = "validateDTO")
	public void validateDTO(TaSessionCaisseDTO dto) {
		validateDTO(dto,null);
		
	}

	@Override
	@WebMethod(operationName = "validateDTOProperty")
	public void validateDTOProperty(TaSessionCaisseDTO dto, String propertyName) {
		validateDTOProperty(dto,propertyName,null);
		
	}

	@Override
	@WebMethod(operationName = "validateEntity")
	public void validateEntity(TaSessionCaisse value) {
		validateEntity(value,null);
	}

	@Override
	@WebMethod(operationName = "validateEntityProperty")
	public void validateEntityProperty(TaSessionCaisse value, String propertyName) {
		validateEntityProperty(value,propertyName,null);
		
	}
	
	public List<TaSessionCaisseDTO> findByCodeLight(String code){
		return dao.findByCodeLight(code);
	}

}

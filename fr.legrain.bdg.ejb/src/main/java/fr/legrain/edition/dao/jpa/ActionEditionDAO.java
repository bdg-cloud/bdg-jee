package fr.legrain.edition.dao.jpa;


import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.edition.dao.IActionEditionDAO;
import fr.legrain.edition.dao.ITEditionDAO;
import fr.legrain.edition.dto.TaActionEditionDTO;
import fr.legrain.edition.model.TaActionEdition;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.edition.model.TaActionEdition;

import fr.legrain.validator.BeanValidator;

public class ActionEditionDAO implements IActionEditionDAO {

	static Logger logger = Logger.getLogger(ActionEditionDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaActionEdition a";
	
	public ActionEditionDAO(){
	}

//	public TaActionEdition refresh(TaActionEdition detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaActionEdition.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaActionEdition transientInstance) {
		logger.debug("persisting TaActionEdition instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("persist failed", re);
			throw re2;
		}
	}

	@Override
	public void remove(TaActionEdition persistentInstance) {
		logger.debug("removing TaActionEdition instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("remove failed", re);
			throw re2;
		}
	}

	@Override
	public TaActionEdition merge(TaActionEdition detachedInstance) {
		logger.debug("merging TaActionEdition instance");
		try {
			TaActionEdition result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaActionEdition findById(int id) {
		logger.debug("getting TaActionEdition instance with id: " + id);
		try {
			TaActionEdition instance = entityManager.find(TaActionEdition.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaActionEdition> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaActionEdition");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaActionEdition> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
//	private Set<TaContactMessageEmail> destinataires;
//	private Set<TaContactMessageEmail> cc;
//	private Set<TaContactMessageEmail> bcc;
//	
//	private TaActionEdition TaActionEdition;
//	
//	private TaActionEdition TaActionEditionCc;
//	private TaActionEdition TaActionEditionBcc;
	
//	public List<TaActionEdition> selectAll(int idTiers) {
//	
//	}
	
	@Override
	public List<TaActionEdition> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaActionEdition> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaActionEdition> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaActionEdition> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaActionEdition value) throws Exception {
		BeanValidator<TaActionEdition> validator = new BeanValidator<TaActionEdition>(TaActionEdition.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaActionEdition value, String propertyName) throws Exception {
		BeanValidator<TaActionEdition> validator = new BeanValidator<TaActionEdition>(TaActionEdition.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaActionEdition transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaActionEdition findByCode(String code) {
		logger.debug("getting TaActionEdition instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaActionEdition a where a.codeAction='"+code+"'");
				TaActionEdition instance = (TaActionEdition)query.getSingleResult();
				logger.debug("get successful");
				return instance;
			}
			return null;
		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
			return null;
		}
	}
	
	public List<TaActionEditionDTO> findAllByIdEdtionDTO(int idEdition){
		logger.debug("getting TaActionEditionDTO instance");
		try {
			Query query = null;
			
			query = entityManager.createNamedQuery(TaActionEdition.QN.FIND_ALL_BY_ID_EDITION_DTO);
			query.setParameter("idEdition", idEdition);

			List<TaActionEditionDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
			


		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	
//	public List<TaActionEditionDTO> findAllLight() {
//		logger.debug("getting TaActionEditionDTO instance");
//		try {
//			Query query = null;
//			String jpql = "";
//			
////			String jpql = "select new fr.legrain.email.dto.TaActionEditionDTO("
////					+ " m.idEmail,  u.id,  m.subject,  m.adresseExpediteur,"
////					+ " m.bodyPlain,  m.bodyHtml,  m.dateCreation,  m.dateEnvoi,  m.suivi,  m.spam,"
////					+ " m.lu,  m.accuseReceptionLu,  m.recu,  m.smtp,  m.nomExpediteur,  m.messageID,"
////					+ " m.infosService, "
////					+ " (select new fr.legrain.email.dto.TaContactMessageEmailDTO(contact.id, contact.adresseEmail, tc.idTiers, tc.codeTiers, tc.nomTiers,"
////						+" tc. prenomTiers) from TaContactMessageEmail contact left join contact.taTiers tc join conctact.TaActionEdition md where md.idEmail = m.idEmail),"
////					+ "null,"//cc TaContactMessageEmailDTO
////					+ "null,"//bcc TaContactMessageEmailDTO
////					+ "null,"//etiquette TaEtiquetteEmailDTO
////					+ "null"//pj TaPieceJointeEmailDTO
////					+ " ) "
////					+ " from TaActionEdition m left join m.taUtilisateur u";
//			
//			query = entityManager.createQuery(jpql);
//			
//
//			List<TaActionEditionDTO> l = query.getResultList();
//			logger.debug("get successful");
//			return l;
//			
//
//
//		} catch (RuntimeException re) {
//			logger.error("get failed", re);
//			throw re;
//		}
//	}

}

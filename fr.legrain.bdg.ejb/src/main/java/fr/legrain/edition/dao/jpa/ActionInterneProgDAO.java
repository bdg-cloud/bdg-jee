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
import fr.legrain.edition.dao.IActionInterneDAO;
import fr.legrain.edition.dao.IActionInterneProgDAO;
import fr.legrain.edition.dao.ITEditionDAO;
import fr.legrain.edition.dto.TaActionInterneProgDTO;
import fr.legrain.edition.model.TaActionInterneProg;
import fr.legrain.edition.model.TaTEditionCatalogue;
import fr.legrain.validator.BeanValidator;

public class ActionInterneProgDAO implements IActionInterneProgDAO {

	static Logger logger = Logger.getLogger(ActionInterneProgDAO.class);
	
	@PersistenceContext(unitName = "bdg_prog")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaActionInterneProg a";
	
	public ActionInterneProgDAO(){
	}

//	public TaActionInterneProg refresh(TaActionInterneProg detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaActionInterneProg.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaActionInterneProg transientInstance) {
		logger.debug("persisting TaActionInterneProg instance");
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
	public void remove(TaActionInterneProg persistentInstance) {
		logger.debug("removing TaActionInterneProg instance");
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
	public TaActionInterneProg merge(TaActionInterneProg detachedInstance) {
		logger.debug("merging TaActionInterneProg instance");
		try {
			TaActionInterneProg result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaActionInterneProg findById(int id) {
		logger.debug("getting TaActionInterneProg instance with id: " + id);
		try {
			TaActionInterneProg instance = entityManager.find(TaActionInterneProg.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaActionInterneProg> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaActionInterneProg");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaActionInterneProg> l = ejbQuery.getResultList();
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
//	private TaActionInterneProg TaActionInterneProg;
//	
//	private TaActionInterneProg TaActionInterneProgCc;
//	private TaActionInterneProg TaActionInterneProgBcc;
	
	public List<TaActionInterneProg> selectAll(int idTiers) {
		// TODO Auto-generated method stub
				logger.debug("selectAll TaActionInterneProg");
				try {
					Query ejbQuery = entityManager.createQuery(""
//							+ "select d from TaContactMessageEmail a "
//							+ " left join a.TaActionInterneProg d "
////							+ " left join a.TaActionInterneProgCc cc "
////							+ " left join a.TaActionInterneProgBcc bcc "
//							+ " join a.taTiers t "
//							+" where t.idTiers = "+idTiers);
					
							+ "select m"
					+ " from TaActionInterneProg m"
					+ " where m.idEmail in"
					+ " ("
					+ "  select d.idEmail"
					+ "  from TaContactMessageEmail a"
					+ "       join a.TaActionInterneProg d join a.taTiers t"
					+ "       where t.idTiers = "+idTiers+""
					+ " )"
					+ "      or m.idEmail in"
					+ " ("
					+ "		select d2.idEmail"
					+ "		  from TaContactMessageEmail a2"
					+ "		       join a2.TaActionInterneProgCc d2 join a2.taTiers t2"
					+ "		       where t2.idTiers = "+idTiers+""
					+ " )"
				    + "  or m.idEmail in"
					+ "	("
					+ "			select d3.idEmail"
					+ "			  from TaContactMessageEmail a3"
					+ "			       join a3.TaActionInterneProgBcc d3 join a3.taTiers t3"
					+ "			       where t3.idTiers = "+idTiers+""
					+ "	)"
					 );
					
					
					
					List<TaActionInterneProg> l = ejbQuery.getResultList();
					logger.debug("selectAll successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("selectAll failed", re);
					throw re;
				}
	}
	
	@Override
	public List<TaActionInterneProg> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaActionInterneProg> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaActionInterneProg> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaActionInterneProg> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaActionInterneProg value) throws Exception {
		BeanValidator<TaActionInterneProg> validator = new BeanValidator<TaActionInterneProg>(TaActionInterneProg.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaActionInterneProg value, String propertyName) throws Exception {
		BeanValidator<TaActionInterneProg> validator = new BeanValidator<TaActionInterneProg>(TaActionInterneProg.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaActionInterneProg transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaActionInterneProg findByCode(String code) {
		logger.debug("getting TaActionInterneProg instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaActionInterneProg a where a.idAction='"+code+"'");
				TaActionInterneProg instance = (TaActionInterneProg)query.getSingleResult();
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
	
	public List<TaActionInterneProgDTO> findAllLight() {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			//query = entityManager.createNamedQuery(TaActionInterneProg.QN.FIND_ALL_LIGHT);
			
			String jpql = "select new fr.legrain.email.dto.TaActionInterneProgDTO("
					+ " m.idEmail,  u.id,  m.subject,  m.adresseExpediteur,"
					+ " m.bodyPlain,  m.bodyHtml,  m.dateCreation,  m.dateEnvoi,  m.suivi,  m.spam,"
					+ " m.lu,  m.accuseReceptionLu,  m.recu,  m.smtp,  m.nomExpediteur,  m.messageID,"
					+ " m.infosService, "
					+ " (select new fr.legrain.email.dto.TaContactMessageEmailDTO(contact.id, contact.adresseEmail, tc.idTiers, tc.codeTiers, tc.nomTiers,"
						+" tc. prenomTiers) from TaContactMessageEmail contact left join contact.taTiers tc join conctact.TaActionInterneProg md where md.idEmail = m.idEmail),"
					+ "null,"//cc TaContactMessageEmailDTO
					+ "null,"//bcc TaContactMessageEmailDTO
					+ "null,"//etiquette TaEtiquetteEmailDTO
					+ "null"//pj TaPieceJointeEmailDTO
					+ " ) "
					+ " from TaActionInterneProg m left join m.taUtilisateur u";
			
			query = entityManager.createQuery(jpql);
			

			List<TaActionInterneProgDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
			
//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			CriteriaQuery<TaActionInterneProg> q = cb.createQuery(TaActionInterneProg.class);
//			  Root<TaActionInterneProg> c = q.from(TaActionInterneProg.class);
//			  q.select(cb.construct(TaActionInterneProg.class,
//			      c.get("name"), c.get("department").get("name")));

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}

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
import fr.legrain.edition.dao.ITEditionDAO;
import fr.legrain.edition.dto.TaActionInterneDTO;
import fr.legrain.edition.model.TaActionInterne;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.edition.model.TaActionInterne;

import fr.legrain.validator.BeanValidator;

public class ActionInterneDAO implements IActionInterneDAO {

	static Logger logger = Logger.getLogger(ActionInterneDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaActionInterne a";
	
	public ActionInterneDAO(){
	}

//	public TaActionInterne refresh(TaActionInterne detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaActionInterne.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaActionInterne transientInstance) {
		logger.debug("persisting TaActionInterne instance");
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
	public void remove(TaActionInterne persistentInstance) {
		logger.debug("removing TaActionInterne instance");
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
	public TaActionInterne merge(TaActionInterne detachedInstance) {
		logger.debug("merging TaActionInterne instance");
		try {
			TaActionInterne result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaActionInterne findById(int id) {
		logger.debug("getting TaActionInterne instance with id: " + id);
		try {
			TaActionInterne instance = entityManager.find(TaActionInterne.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaActionInterne> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaActionInterne");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaActionInterne> l = ejbQuery.getResultList();
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
//	private TaActionInterne TaActionInterne;
//	
//	private TaActionInterne TaActionInterneCc;
//	private TaActionInterne TaActionInterneBcc;
	
	public List<TaActionInterne> selectAll(int idTiers) {
		// TODO Auto-generated method stub
				logger.debug("selectAll TaActionInterne");
				try {
					Query ejbQuery = entityManager.createQuery(""
//							+ "select d from TaContactMessageEmail a "
//							+ " left join a.TaActionInterne d "
////							+ " left join a.TaActionInterneCc cc "
////							+ " left join a.TaActionInterneBcc bcc "
//							+ " join a.taTiers t "
//							+" where t.idTiers = "+idTiers);
					
							+ "select m"
					+ " from TaActionInterne m"
					+ " where m.idEmail in"
					+ " ("
					+ "  select d.idEmail"
					+ "  from TaContactMessageEmail a"
					+ "       join a.TaActionInterne d join a.taTiers t"
					+ "       where t.idTiers = "+idTiers+""
					+ " )"
					+ "      or m.idEmail in"
					+ " ("
					+ "		select d2.idEmail"
					+ "		  from TaContactMessageEmail a2"
					+ "		       join a2.TaActionInterneCc d2 join a2.taTiers t2"
					+ "		       where t2.idTiers = "+idTiers+""
					+ " )"
				    + "  or m.idEmail in"
					+ "	("
					+ "			select d3.idEmail"
					+ "			  from TaContactMessageEmail a3"
					+ "			       join a3.TaActionInterneBcc d3 join a3.taTiers t3"
					+ "			       where t3.idTiers = "+idTiers+""
					+ "	)"
					 );
					
					
					
					List<TaActionInterne> l = ejbQuery.getResultList();
					logger.debug("selectAll successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("selectAll failed", re);
					throw re;
				}
	}
	
	@Override
	public List<TaActionInterne> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaActionInterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaActionInterne> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaActionInterne> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaActionInterne value) throws Exception {
		BeanValidator<TaActionInterne> validator = new BeanValidator<TaActionInterne>(TaActionInterne.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaActionInterne value, String propertyName) throws Exception {
		BeanValidator<TaActionInterne> validator = new BeanValidator<TaActionInterne>(TaActionInterne.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaActionInterne transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaActionInterne findByCode(String code) {
		logger.debug("getting TaActionInterne instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaActionInterne a where a.idAction='"+code+"'");
				TaActionInterne instance = (TaActionInterne)query.getSingleResult();
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
	
	public List<TaActionInterneDTO> findAllLight() {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			//query = entityManager.createNamedQuery(TaActionInterne.QN.FIND_ALL_LIGHT);
			
			String jpql = "select new fr.legrain.email.dto.TaActionInterneDTO("
					+ " m.idEmail,  u.id,  m.subject,  m.adresseExpediteur,"
					+ " m.bodyPlain,  m.bodyHtml,  m.dateCreation,  m.dateEnvoi,  m.suivi,  m.spam,"
					+ " m.lu,  m.accuseReceptionLu,  m.recu,  m.smtp,  m.nomExpediteur,  m.messageID,"
					+ " m.infosService, "
					+ " (select new fr.legrain.email.dto.TaContactMessageEmailDTO(contact.id, contact.adresseEmail, tc.idTiers, tc.codeTiers, tc.nomTiers,"
						+" tc. prenomTiers) from TaContactMessageEmail contact left join contact.taTiers tc join conctact.TaActionInterne md where md.idEmail = m.idEmail),"
					+ "null,"//cc TaContactMessageEmailDTO
					+ "null,"//bcc TaContactMessageEmailDTO
					+ "null,"//etiquette TaEtiquetteEmailDTO
					+ "null"//pj TaPieceJointeEmailDTO
					+ " ) "
					+ " from TaActionInterne m left join m.taUtilisateur u";
			
			query = entityManager.createQuery(jpql);
			

			List<TaActionInterneDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
			
//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			CriteriaQuery<TaActionInterne> q = cb.createQuery(TaActionInterne.class);
//			  Root<TaActionInterne> c = q.from(TaActionInterne.class);
//			  q.select(cb.construct(TaActionInterne.class,
//			      c.get("name"), c.get("department").get("name")));

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}

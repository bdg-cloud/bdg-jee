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
import fr.legrain.edition.dao.ITEditionDAO;
import fr.legrain.edition.dto.TaTEditionDTO;
import fr.legrain.edition.model.TaActionInterne;
import fr.legrain.edition.model.TaTEdition;

import fr.legrain.validator.BeanValidator;

public class TEditionDAO implements ITEditionDAO {

	static Logger logger = Logger.getLogger(TEditionDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTEdition a";
	
	public TEditionDAO(){
	}

//	public TaTEdition refresh(TaTEdition detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTEdition.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaTEdition transientInstance) {
		logger.debug("persisting TaTEdition instance");
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
	public void remove(TaTEdition persistentInstance) {
		logger.debug("removing TaTEdition instance");
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
	public TaTEdition merge(TaTEdition detachedInstance) {
		logger.debug("merging TaTEdition instance");
		try {
			TaTEdition result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaTEdition findById(int id) {
		logger.debug("getting TaTEdition instance with id: " + id);
		try {
			TaTEdition instance = entityManager.find(TaTEdition.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaTEdition> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTEdition");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTEdition> l = ejbQuery.getResultList();
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
//	private TaTEdition TaTEdition;
//	
//	private TaTEdition TaTEditionCc;
//	private TaTEdition TaTEditionBcc;
	
	public List<TaTEdition> selectAll(int idTiers) {
		// TODO Auto-generated method stub
				logger.debug("selectAll TaTEdition");
				try {
					Query ejbQuery = entityManager.createQuery(""
//							+ "select d from TaContactMessageEmail a "
//							+ " left join a.TaTEdition d "
////							+ " left join a.TaTEditionCc cc "
////							+ " left join a.TaTEditionBcc bcc "
//							+ " join a.taTiers t "
//							+" where t.idTiers = "+idTiers);
					
							+ "select m"
					+ " from TaTEdition m"
					+ " where m.idEmail in"
					+ " ("
					+ "  select d.idEmail"
					+ "  from TaContactMessageEmail a"
					+ "       join a.TaTEdition d join a.taTiers t"
					+ "       where t.idTiers = "+idTiers+""
					+ " )"
					+ "      or m.idEmail in"
					+ " ("
					+ "		select d2.idEmail"
					+ "		  from TaContactMessageEmail a2"
					+ "		       join a2.TaTEditionCc d2 join a2.taTiers t2"
					+ "		       where t2.idTiers = "+idTiers+""
					+ " )"
				    + "  or m.idEmail in"
					+ "	("
					+ "			select d3.idEmail"
					+ "			  from TaContactMessageEmail a3"
					+ "			       join a3.TaTEditionBcc d3 join a3.taTiers t3"
					+ "			       where t3.idTiers = "+idTiers+""
					+ "	)"
					 );
					
					
					
					List<TaTEdition> l = ejbQuery.getResultList();
					logger.debug("selectAll successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("selectAll failed", re);
					throw re;
				}
	}
	
	@Override
	public List<TaTEdition> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTEdition> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTEdition> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTEdition> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTEdition value) throws Exception {
		BeanValidator<TaTEdition> validator = new BeanValidator<TaTEdition>(TaTEdition.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTEdition value, String propertyName) throws Exception {
		BeanValidator<TaTEdition> validator = new BeanValidator<TaTEdition>(TaTEdition.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTEdition transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaTEdition findByCode(String code) {
		logger.debug("getting TaTEdition instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaTEdition a where a.codeTEdition='"+code+"'");
				TaTEdition instance = (TaTEdition)query.getSingleResult();
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
	
	public List<TaTEditionDTO> findAllLight() {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			//query = entityManager.createNamedQuery(TaTEdition.QN.FIND_ALL_LIGHT);
			
			String jpql = "select new fr.legrain.email.dto.TaTEditionDTO("
					+ " m.idEmail,  u.id,  m.subject,  m.adresseExpediteur,"
					+ " m.bodyPlain,  m.bodyHtml,  m.dateCreation,  m.dateEnvoi,  m.suivi,  m.spam,"
					+ " m.lu,  m.accuseReceptionLu,  m.recu,  m.smtp,  m.nomExpediteur,  m.messageID,"
					+ " m.infosService, "
					+ " (select new fr.legrain.email.dto.TaContactMessageEmailDTO(contact.id, contact.adresseEmail, tc.idTiers, tc.codeTiers, tc.nomTiers,"
						+" tc. prenomTiers) from TaContactMessageEmail contact left join contact.taTiers tc join conctact.TaTEdition md where md.idEmail = m.idEmail),"
					+ "null,"//cc TaContactMessageEmailDTO
					+ "null,"//bcc TaContactMessageEmailDTO
					+ "null,"//etiquette TaEtiquetteEmailDTO
					+ "null"//pj TaPieceJointeEmailDTO
					+ " ) "
					+ " from TaTEdition m left join m.taUtilisateur u";
			
			query = entityManager.createQuery(jpql);
			

			List<TaTEditionDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
			
//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			CriteriaQuery<TaTEdition> q = cb.createQuery(TaTEdition.class);
//			  Root<TaTEdition> c = q.from(TaTEdition.class);
//			  q.select(cb.construct(TaTEdition.class,
//			      c.get("name"), c.get("department").get("name")));

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}

package fr.legrain.sms.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.sms.dao.IMessageSMSDAO;
import fr.legrain.sms.dto.TaMessageSMSDTO;
import fr.legrain.sms.model.TaMessageSMS;
import fr.legrain.validator.BeanValidator;

public class MessageSMSDAO implements IMessageSMSDAO {

	static Logger logger = Logger.getLogger(MessageSMSDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaMessageSMS a";
	
	public MessageSMSDAO(){
	}

//	public TaMessageSMS refresh(TaMessageSMS detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaMessageSMS.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaMessageSMS transientInstance) {
		logger.debug("persisting TaMessageSMS instance");
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
	public void remove(TaMessageSMS persistentInstance) {
		logger.debug("removing TaMessageSMS instance");
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
	public TaMessageSMS merge(TaMessageSMS detachedInstance) {
		logger.debug("merging TaMessageSMS instance");
		try {
			TaMessageSMS result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaMessageSMS findById(int id) {
		logger.debug("getting TaMessageSMS instance with id: " + id);
		try {
			TaMessageSMS instance = entityManager.find(TaMessageSMS.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaMessageSMS> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaMessageSMS");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaMessageSMS> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
//	private Set<TaContactMessageSMS> destinataires;
//	private Set<TaContactMessageSMS> cc;
//	private Set<TaContactMessageSMS> bcc;
//	
//	private TaMessageSMS TaMessageSMS;
//	
//	private TaMessageSMS TaMessageSMSCc;
//	private TaMessageSMS TaMessageSMSBcc;
	
	public List<TaMessageSMS> selectAll(int idTiers) {
		// TODO Auto-generated method stub
				logger.debug("selectAll TaMessageSMS");
				try {
					Query ejbQuery = entityManager.createQuery(""
//							+ "select d from TaContactMessageSMS a "
//							+ " left join a.TaMessageSMS d "
////							+ " left join a.TaMessageSMSCc cc "
////							+ " left join a.TaMessageSMSBcc bcc "
//							+ " join a.taTiers t "
//							+" where t.idTiers = "+idTiers);
					
							+ "select m"
					+ " from TaMessageSMS m"
					+ " where m.idSms in"
					+ " ("
					+ "  select d.idSms"
					+ "  from TaContactMessageSMS a"
					+ "       join a.taMessageSms d join a.taTiers t"
					+ "       where t.idTiers = "+idTiers+""
					+ " )"
//					+ "      or m.idSms in"
//					+ " ("
//					+ "		select d2.idSms"
//					+ "		  from TaContactMessageSMS a2"
//					+ "		       join a2.TaMessageSMSCc d2 join a2.taTiers t2"
//					+ "		       where t2.idTiers = "+idTiers+""
//					+ " )"
//				    + "  or m.idSms in"
//					+ "	("
//					+ "			select d3.idSms"
//					+ "			  from TaContactMessageSMS a3"
//					+ "			       join a3.TaMessageSMSBcc d3 join a3.taTiers t3"
//					+ "			       where t3.idTiers = "+idTiers+""
//					+ "	)"
					 );
					
					
					
					List<TaMessageSMS> l = ejbQuery.getResultList();
					logger.debug("selectAll successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("selectAll failed", re);
					throw re;
				}
	}
	
	@Override
	public List<TaMessageSMS> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaMessageSMS> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaMessageSMS> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaMessageSMS> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaMessageSMS value) throws Exception {
		BeanValidator<TaMessageSMS> validator = new BeanValidator<TaMessageSMS>(TaMessageSMS.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaMessageSMS value, String propertyName) throws Exception {
		BeanValidator<TaMessageSMS> validator = new BeanValidator<TaMessageSMS>(TaMessageSMS.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaMessageSMS transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaMessageSMS findByCode(String code) {
		return null;
	}
	
	public List<TaMessageSMSDTO> findAllLight() {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			//query = entityManager.createNamedQuery(TaMessageSMS.QN.FIND_ALL_LIGHT);
			
			String jpql = "select new fr.legrain.email.dto.TaMessageSMSDTO("
					+ " m.idSms,  u.id,  m.subject,  m.adresseExpediteur,"
					+ " m.bodyPlain,  m.bodyHtml,  m.dateCreation,  m.dateEnvoi,  m.suivi,  m.spam,"
					+ " m.lu,  m.accuseReceptionLu,  m.recu,  m.stop,  m.nomExpediteur,  m.messageID,"
					+ " m.infosService, "
					+ " (select new fr.legrain.email.dto.TaContactMessageSMSDTO(contact.id, contact.adresseEmail, tc.idTiers, tc.codeTiers, tc.nomTiers,"
						+" tc. prenomTiers) from TaContactMessageSMS contact left join contact.taTiers tc join conctact.TaMessageSMS md where md.idSms = m.idSms),"
					+ "null,"//cc TaContactMessageSMSDTO
					+ "null,"//bcc TaContactMessageSMSDTO
					+ "null,"//etiquette TaEtiquetteEmailDTO
					+ "null"//pj TaPieceJointeEmailDTO
					+ " ) "
					+ " from TaMessageSMS m left join m.taUtilisateur u";
			
			query = entityManager.createQuery(jpql);
			

			List<TaMessageSMSDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
			
//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			CriteriaQuery<TaMessageSMS> q = cb.createQuery(TaMessageSMS.class);
//			  Root<TaMessageSMS> c = q.from(TaMessageSMS.class);
//			  q.select(cb.construct(TaMessageSMS.class,
//			      c.get("name"), c.get("department").get("name")));

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}

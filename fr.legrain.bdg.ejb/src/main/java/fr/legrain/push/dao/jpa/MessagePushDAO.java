package fr.legrain.push.dao.jpa;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.bdg.general.service.remote.ThrowableExceptionLgr;
import fr.legrain.push.dao.IMessagePushDAO;
import fr.legrain.push.dto.TaMessagePushDTO;
import fr.legrain.push.model.TaMessagePush;
import fr.legrain.validator.BeanValidator;

public class MessagePushDAO implements IMessagePushDAO {

	static Logger logger = Logger.getLogger(MessagePushDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaMessagePush a";
	
	public MessagePushDAO(){
	}

//	public TaMessagePush refresh(TaMessagePush detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaMessagePush.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaMessagePush transientInstance) {
		logger.debug("persisting TaMessagePush instance");
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
	public void remove(TaMessagePush persistentInstance) {
		logger.debug("removing TaMessagePush instance");
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
	public TaMessagePush merge(TaMessagePush detachedInstance) {
		logger.debug("merging TaMessagePush instance");
		try {
			TaMessagePush result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaMessagePush findById(int id) {
		logger.debug("getting TaMessagePush instance with id: " + id);
		try {
			TaMessagePush instance = entityManager.find(TaMessagePush.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaMessagePush> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaMessagePush");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaMessagePush> l = ejbQuery.getResultList();
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
//	private TaMessagePush TaMessagePush;
//	
//	private TaMessagePush TaMessagePushCc;
//	private TaMessagePush TaMessagePushBcc;
	
	public List<TaMessagePush> selectAll(int idTiers) {
		// TODO Auto-generated method stub
				logger.debug("selectAll TaMessagePush");
				try {
					Query ejbQuery = entityManager.createQuery(""
//							+ "select d from TaContactMessageSMS a "
//							+ " left join a.TaMessagePush d "
////							+ " left join a.TaMessagePushCc cc "
////							+ " left join a.TaMessagePushBcc bcc "
//							+ " join a.taTiers t "
//							+" where t.idTiers = "+idTiers);
					
							+ "select m"
					+ " from TaMessagePush m"
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
//					+ "		       join a2.TaMessagePushCc d2 join a2.taTiers t2"
//					+ "		       where t2.idTiers = "+idTiers+""
//					+ " )"
//				    + "  or m.idSms in"
//					+ "	("
//					+ "			select d3.idSms"
//					+ "			  from TaContactMessageSMS a3"
//					+ "			       join a3.TaMessagePushBcc d3 join a3.taTiers t3"
//					+ "			       where t3.idTiers = "+idTiers+""
//					+ "	)"
					 );
					
					
					
					List<TaMessagePush> l = ejbQuery.getResultList();
					logger.debug("selectAll successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("selectAll failed", re);
					throw re;
				}
	}
	
	@Override
	public List<TaMessagePush> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaMessagePush> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaMessagePush> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaMessagePush> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaMessagePush value) throws Exception {
		BeanValidator<TaMessagePush> validator = new BeanValidator<TaMessagePush>(TaMessagePush.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaMessagePush value, String propertyName) throws Exception {
		BeanValidator<TaMessagePush> validator = new BeanValidator<TaMessagePush>(TaMessagePush.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaMessagePush transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaMessagePush findByCode(String code) {
		return null;
	}
	
	public List<TaMessagePushDTO> findAllLight() {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			//query = entityManager.createNamedQuery(TaMessagePush.QN.FIND_ALL_LIGHT);
			
			String jpql = "select new fr.legrain.push.dto.TaMessagePushDTO("
					+ " m.idSms,  u.id,  m.subject,  m.adresseExpediteur,"
					+ " m.bodyPlain,  m.bodyHtml,  m.dateCreation,  m.dateEnvoi,  m.suivi,  m.spam,"
					+ " m.lu,  m.accuseReceptionLu,  m.recu,  m.stop,  m.nomExpediteur,  m.messageID,"
					+ " m.infosService, "
					+ " (select new fr.legrain.push.dto.TaContactMessagePushDTO(contact.id, contact.adresseEmail, tc.idTiers, tc.codeTiers, tc.nomTiers,"
						+" tc. prenomTiers) from TaContactMessagePush contact left join contact.taTiers tc join conctact.TaMessagePush md where md.idSms = m.idSms),"
					+ "null,"//cc TaContactMessageSMSDTO
					+ "null,"//bcc TaContactMessageSMSDTO
					+ "null,"//etiquette TaEtiquetteEmailDTO
					+ "null"//pj TaPieceJointeEmailDTO
					+ " ) "
					+ " from TaMessagePush m left join m.taUtilisateur u";
			
			query = entityManager.createQuery(jpql);
			

			List<TaMessagePushDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
			
//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			CriteriaQuery<TaMessagePush> q = cb.createQuery(TaMessagePush.class);
//			  Root<TaMessagePush> c = q.from(TaMessagePush.class);
//			  q.select(cb.construct(TaMessagePush.class,
//			      c.get("name"), c.get("department").get("name")));

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}

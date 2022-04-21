package fr.legrain.email.dao.jpa;


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
import fr.legrain.email.dao.IMessageEmailDAO;
import fr.legrain.email.dto.TaContactMessageEmailDTO;
import fr.legrain.email.dto.TaEtiquetteEmailDTO;
import fr.legrain.email.dto.TaMessageEmailDTO;
import fr.legrain.email.dto.TaPieceJointeEmailDTO;
import fr.legrain.email.model.TaContactMessageEmail;
import fr.legrain.email.model.TaMessageEmail;
import fr.legrain.tiers.dto.TaTiersDTO;
import fr.legrain.tiers.model.TaTiers;
import fr.legrain.validator.BeanValidator;

public class MessageEmailDAO implements IMessageEmailDAO {

	static Logger logger = Logger.getLogger(MessageEmailDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaMessageEmail a";
	
	public MessageEmailDAO(){
	}

//	public TaMessageEmail refresh(TaMessageEmail detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaMessageEmail.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaMessageEmail transientInstance) {
		logger.debug("persisting TaMessageEmail instance");
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
	public void remove(TaMessageEmail persistentInstance) {
		logger.debug("removing TaMessageEmail instance");
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
	public TaMessageEmail merge(TaMessageEmail detachedInstance) {
		logger.debug("merging TaMessageEmail instance");
		try {
			TaMessageEmail result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaMessageEmail findById(int id) {
		logger.debug("getting TaMessageEmail instance with id: " + id);
		try {
			TaMessageEmail instance = entityManager.find(TaMessageEmail.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaMessageEmail> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaMessageEmail");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaMessageEmail> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
		
	}
	
	public List<TaMessageEmailDTO> findAllLight() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaMessageEmail");
		try {
			
			String sql = " SELECT m.id_email,subject,adresse_expediteur,body_plain,body_html,date_creation,date_envoi,suivi,spam,lu,accuse_reception_lu,recu,smtp," + 
					"array_to_string(array_agg(c.adresseemail\\:\\:varchar(50) ORDER BY c.adresseemail ASC), ',') as liste_adresses," + 
					"(select count(*) from ta_piece_jointe_email pj where pj.id_email=m.id_email) as nb_pj," + 
					"array_to_string(array_agg(et.libelle\\:\\:varchar(255) ORDER BY et.libelle ASC), ',') as liste_etiquette" + 
					" FROM  ta_message_email m" + 
					" LEFT JOIN ta_contact_message_email c ON c.id_email=m.id_email" + 
					" LEFT JOIN ta_r_etiquette_email re ON re.id_email=m.id_email" + 
					" LEFT JOIN ta_etiquette_email et ON et.id_etiquette_email=re.id_etiquette_email" + 
					" group by m.id_email"+
					""
					;
/*
 SELECT m.id_email,subject,adresse_expediteur,body_plain,body_html,date_creation,date_creation,suivi,spam,lu,accuse_reception_lu,recu,smtp,
array_to_string(array_agg(c.adresseemail::varchar(50) ORDER BY c.adresseemail ASC), ','),
(select count(*) from ta_piece_jointe_email pj where pj.id_email=m.id_email),
array_to_string(array_agg(et.libelle::varchar(255) ORDER BY et.libelle ASC), ',')
FROM  ta_message_email m
LEFT JOIN ta_contact_message_email c ON c.id_email=m.id_email
LEFT JOIN ta_r_etiquette_email re ON re.id_email=m.id_email
LEFT JOIN ta_etiquette_email et ON et.id_etiquette_email=re.id_etiquette_email
group by m.id_email
 */
					
//			String jpql = "";
//
//			jpql = "select "
//					+ "m.subject,m.adresseExpediteur,m.bodyPlain,m.bodyHtml,m.dateCreation,m.dateEnvoi,m.suivi,m.spam,m.lu,m.accuseReceptionLu,m.recu,m.smtp,"
//					+ "select d.adresseEmail "
//					+ "from TaMessageEmail m join m.destinataires d";
			
			
//			Query ejbQuery = entityManager.createQuery(jpql);
//			System.out.println("/*/*/*/*/*/*/* Debut SQL " + new Date());
			Query ejbQuery = entityManager.createNativeQuery(sql,"EmailResult");
//			System.out.println("/*/*/*/*/*/*/* Fin SQL " + new Date());
			List<TaMessageEmailDTO> l = ejbQuery.getResultList();
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
//	private TaMessageEmail taMessageEmail;
//	
//	private TaMessageEmail taMessageEmailCc;
//	private TaMessageEmail taMessageEmailBcc;
	
	public List<TaMessageEmail> selectAll(int idTiers) {
		// TODO Auto-generated method stub
				logger.debug("selectAll TaMessageEmail");
				try {
					Query ejbQuery = entityManager.createQuery(""
//							+ "select d from TaContactMessageEmail a "
//							+ " left join a.taMessageEmail d "
////							+ " left join a.taMessageEmailCc cc "
////							+ " left join a.taMessageEmailBcc bcc "
//							+ " join a.taTiers t "
//							+" where t.idTiers = "+idTiers);
					
							+ "select m"
					+ " from TaMessageEmail m"
					+ " where m.idEmail in"
					+ " ("
					+ "  select d.idEmail"
					+ "  from TaContactMessageEmail a"
					+ "       join a.taMessageEmail d join a.taTiers t"
					+ "       where t.idTiers = "+idTiers+""
					+ " )"
					+ "      or m.idEmail in"
					+ " ("
					+ "		select d2.idEmail"
					+ "		  from TaContactMessageEmail a2"
					+ "		       join a2.taMessageEmailCc d2 join a2.taTiers t2"
					+ "		       where t2.idTiers = "+idTiers+""
					+ " )"
				    + "  or m.idEmail in"
					+ "	("
					+ "			select d3.idEmail"
					+ "			  from TaContactMessageEmail a3"
					+ "			       join a3.taMessageEmailBcc d3 join a3.taTiers t3"
					+ "			       where t3.idTiers = "+idTiers+""
					+ "	)"
					 );
					
					
					
					List<TaMessageEmail> l = ejbQuery.getResultList();
					logger.debug("selectAll successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("selectAll failed", re);
					throw re;
				}
	}
	
	public List<TaMessageEmailDTO> findAllLight(int idTiers) {
		// TODO Auto-generated method stub
				logger.debug("selectAll TaMessageEmail");
				try {
//					Query ejbQuery = entityManager.createQuery(""
////							+ "select d from TaContactMessageEmail a "
////							+ " left join a.taMessageEmail d "
//////							+ " left join a.taMessageEmailCc cc "
//////							+ " left join a.taMessageEmailBcc bcc "
////							+ " join a.taTiers t "
////							+" where t.idTiers = "+idTiers);
//					
//							+ "select m"
//					+ " from TaMessageEmail m"
//					+ " where m.idEmail in"
//					+ " ("
//					+ "  select d.idEmail"
//					+ "  from TaContactMessageEmail a"
//					+ "       join a.taMessageEmail d join a.taTiers t"
//					+ "       where t.idTiers = "+idTiers+""
//					+ " )"
//					+ "      or m.idEmail in"
//					+ " ("
//					+ "		select d2.idEmail"
//					+ "		  from TaContactMessageEmail a2"
//					+ "		       join a2.taMessageEmailCc d2 join a2.taTiers t2"
//					+ "		       where t2.idTiers = "+idTiers+""
//					+ " )"
//				    + "  or m.idEmail in"
//					+ "	("
//					+ "			select d3.idEmail"
//					+ "			  from TaContactMessageEmail a3"
//					+ "			       join a3.taMessageEmailBcc d3 join a3.taTiers t3"
//					+ "			       where t3.idTiers = "+idTiers+""
//					+ "	)"
//					 );
					String sql = " SELECT m.id_email,subject,adresse_expediteur,body_plain,body_html,date_creation,date_envoi,suivi,spam,lu,accuse_reception_lu,recu,smtp," + 
							"array_to_string(array_agg(c.adresseemail\\:\\:varchar(50) ORDER BY c.adresseemail ASC), ',') as liste_adresses," + 
							"(select count(*) from ta_piece_jointe_email pj where pj.id_email=m.id_email) as nb_pj," + 
							"array_to_string(array_agg(et.libelle\\:\\:varchar(255) ORDER BY et.libelle ASC), ',') as liste_etiquette" + 
							" FROM  ta_message_email m" + 
							" LEFT JOIN ta_contact_message_email c ON c.id_email=m.id_email" + 
							" LEFT JOIN ta_r_etiquette_email re ON re.id_email=m.id_email" + 
							" LEFT JOIN ta_etiquette_email et ON et.id_etiquette_email=re.id_etiquette_email" + 
							" where c.id_tiers="+idTiers+
							" group by m.id_email"+
							""
							;
					Query ejbQuery = entityManager.createNativeQuery(sql,"EmailResult");
					
					
					List<TaMessageEmailDTO> l = ejbQuery.getResultList();
					logger.debug("selectAll successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("selectAll failed", re);
					throw re;
				}
	}
	
//	public List<TaMessageEmailDTO> findAllLight() {
//	logger.debug("getting TaTiersDTO instance");
//	try {
//		Query query = null;
//
//		//query = entityManager.createNamedQuery(TaMessageEmail.QN.FIND_ALL_LIGHT);
//		
//		String jpql = "select new fr.legrain.email.dto.TaMessageEmailDTO("
//				+ " m.idEmail,  u.id,  m.subject,  m.adresseExpediteur,"
//				+ " m.bodyPlain,  m.bodyHtml,  m.dateCreation,  m.dateEnvoi,  m.suivi,  m.spam,"
//				+ " m.lu,  m.accuseReceptionLu,  m.recu,  m.smtp,  m.nomExpediteur,  m.messageID,"
//				+ " m.infosService, "
//				+ " (select new fr.legrain.email.dto.TaContactMessageEmailDTO(contact.id, contact.adresseEmail, tc.idTiers, tc.codeTiers, tc.nomTiers,"
//					+" tc. prenomTiers) from TaContactMessageEmail contact left join contact.taTiers tc join conctact.taMessageEmail md where md.idEmail = m.idEmail),"
//				+ "null,"//cc TaContactMessageEmailDTO
//				+ "null,"//bcc TaContactMessageEmailDTO
//				+ "null,"//etiquette TaEtiquetteEmailDTO
//				+ "null"//pj TaPieceJointeEmailDTO
//				+ " ) "
//				+ " from TaMessageEmail m left join m.taUtilisateur u";
//		
//		query = entityManager.createQuery(jpql);
//		
//
//		List<TaMessageEmailDTO> l = query.getResultList();
//		logger.debug("get successful");
//		return l;
//		
////		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
////		CriteriaQuery<TaMessageEmail> q = cb.createQuery(TaMessageEmail.class);
////		  Root<TaMessageEmail> c = q.from(TaMessageEmail.class);
////		  q.select(cb.construct(TaMessageEmail.class,
////		      c.get("name"), c.get("department").get("name")));
//
//	} catch (RuntimeException re) {
//		logger.error("get failed", re);
//		throw re;
//	}
//}
	
	@Override
	public List<TaMessageEmail> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaMessageEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaMessageEmail> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaMessageEmail> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaMessageEmail value) throws Exception {
		BeanValidator<TaMessageEmail> validator = new BeanValidator<TaMessageEmail>(TaMessageEmail.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaMessageEmail value, String propertyName) throws Exception {
		BeanValidator<TaMessageEmail> validator = new BeanValidator<TaMessageEmail>(TaMessageEmail.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaMessageEmail transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaMessageEmail findByCode(String code) {
		return null;
	}
	


}

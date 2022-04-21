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
import fr.legrain.edition.dao.ITEditionCatalogueDAO;
import fr.legrain.edition.dao.ITEditionDAO;
import fr.legrain.edition.dto.TaTEditionCatalogueDTO;
import fr.legrain.edition.model.TaTEdition;
import fr.legrain.edition.model.TaTEditionCatalogue;
import fr.legrain.validator.BeanValidator;

public class TEditionCatalogueDAO implements ITEditionCatalogueDAO {

	static Logger logger = Logger.getLogger(TEditionCatalogueDAO.class);
	
	@PersistenceContext(unitName = "bdg_prog")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaTEditionCatalogue a";
	
	public TEditionCatalogueDAO(){
	}

//	public TaTEditionCatalogue refresh(TaTEditionCatalogue detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaTEditionCatalogue.class, detachedInstance.getIdAdresse());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	@Override
	public void persist(TaTEditionCatalogue transientInstance) {
		logger.debug("persisting TaTEditionCatalogue instance");
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
	public void remove(TaTEditionCatalogue persistentInstance) {
		logger.debug("removing TaTEditionCatalogue instance");
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
	public TaTEditionCatalogue merge(TaTEditionCatalogue detachedInstance) {
		logger.debug("merging TaTEditionCatalogue instance");
		try {
			TaTEditionCatalogue result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			RuntimeException re2=ThrowableExceptionLgr.renvoieCauseRuntimeException(re);
			logger.error("merge failed", re);
			throw re2;
		}
	}


	@Override
	public TaTEditionCatalogue findById(int id) {
		logger.debug("getting TaTEditionCatalogue instance with id: " + id);
		try {
			TaTEditionCatalogue instance = entityManager.find(TaTEditionCatalogue.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

	@Override
	public List<TaTEditionCatalogue> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaTEditionCatalogue");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaTEditionCatalogue> l = ejbQuery.getResultList();
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
//	private TaTEditionCatalogue TaTEditionCatalogue;
//	
//	private TaTEditionCatalogue TaTEditionCatalogueCc;
//	private TaTEditionCatalogue TaTEditionCatalogueBcc;
	
	public List<TaTEditionCatalogue> selectAll(int idTiers) {
		// TODO Auto-generated method stub
				logger.debug("selectAll TaTEditionCatalogue");
				try {
					Query ejbQuery = entityManager.createQuery(""
//							+ "select d from TaContactMessageEmail a "
//							+ " left join a.TaTEditionCatalogue d "
////							+ " left join a.TaTEditionCatalogueCc cc "
////							+ " left join a.TaTEditionCatalogueBcc bcc "
//							+ " join a.taTiers t "
//							+" where t.idTiers = "+idTiers);
					
							+ "select m"
					+ " from TaTEditionCatalogue m"
					+ " where m.idEmail in"
					+ " ("
					+ "  select d.idEmail"
					+ "  from TaContactMessageEmail a"
					+ "       join a.TaTEditionCatalogue d join a.taTiers t"
					+ "       where t.idTiers = "+idTiers+""
					+ " )"
					+ "      or m.idEmail in"
					+ " ("
					+ "		select d2.idEmail"
					+ "		  from TaContactMessageEmail a2"
					+ "		       join a2.TaTEditionCatalogueCc d2 join a2.taTiers t2"
					+ "		       where t2.idTiers = "+idTiers+""
					+ " )"
				    + "  or m.idEmail in"
					+ "	("
					+ "			select d3.idEmail"
					+ "			  from TaContactMessageEmail a3"
					+ "			       join a3.TaTEditionCatalogueBcc d3 join a3.taTiers t3"
					+ "			       where t3.idTiers = "+idTiers+""
					+ "	)"
					 );
					
					
					
					List<TaTEditionCatalogue> l = ejbQuery.getResultList();
					logger.debug("selectAll successful");
					return l;
				} catch (RuntimeException re) {
					logger.error("selectAll failed", re);
					throw re;
				}
	}
	
	@Override
	public List<TaTEditionCatalogue> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaTEditionCatalogue> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaTEditionCatalogue> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaTEditionCatalogue> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaTEditionCatalogue value) throws Exception {
		BeanValidator<TaTEditionCatalogue> validator = new BeanValidator<TaTEditionCatalogue>(TaTEditionCatalogue.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaTEditionCatalogue value, String propertyName) throws Exception {
		BeanValidator<TaTEditionCatalogue> validator = new BeanValidator<TaTEditionCatalogue>(TaTEditionCatalogue.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaTEditionCatalogue transientInstance) {
		entityManager.detach(transientInstance);
	}

	@Override
	public TaTEditionCatalogue findByCode(String code) {
		logger.debug("getting TaTEditionCatalogue instance with code: " + code);
		try {
			if(!code.equals("")){
				Query query = entityManager.createQuery("select a from TaTEditionCatalogue a where a.codeTEdition='"+code+"'");
				TaTEditionCatalogue instance = (TaTEditionCatalogue)query.getSingleResult();
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
	
	public List<TaTEditionCatalogueDTO> findAllLight() {
		logger.debug("getting TaTiersDTO instance");
		try {
			Query query = null;

			//query = entityManager.createNamedQuery(TaTEditionCatalogue.QN.FIND_ALL_LIGHT);
			
			String jpql = "select new fr.legrain.email.dto.TaTEditionCatalogueDTO("
					+ " m.idEmail,  u.id,  m.subject,  m.adresseExpediteur,"
					+ " m.bodyPlain,  m.bodyHtml,  m.dateCreation,  m.dateEnvoi,  m.suivi,  m.spam,"
					+ " m.lu,  m.accuseReceptionLu,  m.recu,  m.smtp,  m.nomExpediteur,  m.messageID,"
					+ " m.infosService, "
					+ " (select new fr.legrain.email.dto.TaContactMessageEmailDTO(contact.id, contact.adresseEmail, tc.idTiers, tc.codeTiers, tc.nomTiers,"
						+" tc. prenomTiers) from TaContactMessageEmail contact left join contact.taTiers tc join conctact.TaTEditionCatalogue md where md.idEmail = m.idEmail),"
					+ "null,"//cc TaContactMessageEmailDTO
					+ "null,"//bcc TaContactMessageEmailDTO
					+ "null,"//etiquette TaEtiquetteEmailDTO
					+ "null"//pj TaPieceJointeEmailDTO
					+ " ) "
					+ " from TaTEditionCatalogue m left join m.taUtilisateur u";
			
			query = entityManager.createQuery(jpql);
			

			List<TaTEditionCatalogueDTO> l = query.getResultList();
			logger.debug("get successful");
			return l;
			
//			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//			CriteriaQuery<TaTEditionCatalogue> q = cb.createQuery(TaTEditionCatalogue.class);
//			  Root<TaTEditionCatalogue> c = q.from(TaTEditionCatalogue.class);
//			  q.select(cb.construct(TaTEditionCatalogue.class,
//			      c.get("name"), c.get("department").get("name")));

		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

}

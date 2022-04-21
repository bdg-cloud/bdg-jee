package fr.legrain.supportAbonnement.dao.jpa;

// Generated Mar 25, 2009 10:06:50 AM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.abonnement.model.TaAbonnementOld;
import fr.legrain.document.model.TaFacture;
import fr.legrain.supportAbonnement.model.TaSupportAbon;
import fr.legrain.supportAbonnements.dao.IAbonnementDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaCPaiement.
 * @see fr.legrain.tiers.dao.test.TaCPaiement
 * @author Hibernate Tools
 */
public class TaAbonnementDAO implements IAbonnementDAO{

	static Logger logger = Logger.getLogger(TaAbonnementDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaAbonnement a";
	
	public TaAbonnementDAO() {
//		this(null);
	}
	
//	public TaAbonnementDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
//		initChampId(TaAbonnement.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaAbonnement());
//	}
	
	public void persist(TaAbonnementOld transientInstance) {
		logger.debug("persisting TaAbonnement instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}
	
//	public TaAbonnement refresh(TaAbonnement detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
//		    session.evict(detachedInstance);
//		    detachedInstance = entityManager.find(TaAbonnement.class, detachedInstance.getIdAbonnement());
//		    return detachedInstance;
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}

	public void remove(TaAbonnementOld persistentInstance) {
		logger.debug("removing TaAbonnement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaAbonnementOld merge(TaAbonnementOld detachedInstance) {
		logger.debug("merging TaAbonnement instance");
		try {
			TaAbonnementOld result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaAbonnementOld findById(int id) {
		logger.debug("getting TaAbonnement instance with id: " + id);
		try {
			TaAbonnementOld instance = entityManager.find(TaAbonnementOld.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaAbonnementOld findByCode(String code) {
		logger.debug("getting TaAbonnement instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaAbonnement f where f.codeAbonnement='"+code+"'");
			TaAbonnementOld instance = (TaAbonnementOld)query.getSingleResult();
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}

//	@Override
	public List<TaAbonnementOld> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaAbonnement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAbonnementOld> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaAbonnementOld> findAbonnementBetweenDateDebDateFin(Date date, Date datefin , String codeTiers) {
		// TODO Auto-generated method stub
		List<TaAbonnementOld>listeFinale=new LinkedList<TaAbonnementOld>();
		List<TaAbonnementOld> l =null;
		logger.debug("findAbonnementBetweenDateDebDateFin");
		try {
			if(codeTiers == null || codeTiers.equals(""))codeTiers="%";
			Query ejbQuery =null;
			ejbQuery = entityManager.createQuery("select a from TaAbonnement a join a.taTiers t join a.taSupportAbon ts where a.dateFin in(" +
                    " select max(a3.dateFin) from TaAbonnement a3 where a3.taSupportAbon=a.taSupportAbon  ) and a.dateFin between :date1  and :datefin1" +
                    " and t.codeTiers like :codeTiers and not exists(select e from TaLEcheance e join e.taAbonnement ab where ab.idAbonnement=a.idAbonnement)  " +
                    " and not exists(select a2 from TaAbonnement a2 where a2.taSupportAbon=a.taSupportAbon and  a2.dateFin between :date  and :datefin and  a2.dateFin>a.dateFin)" +
    				" and ts.inactif<>1 " );
			ejbQuery.setParameter("date1", date,TemporalType.DATE);
			ejbQuery.setParameter("datefin1", datefin,TemporalType.DATE);
			ejbQuery.setParameter("codeTiers", codeTiers);
			ejbQuery.setParameter("date", date,TemporalType.DATE);
			ejbQuery.setParameter("datefin", datefin,TemporalType.DATE);
			 l = ejbQuery.getResultList();

			
			logger.debug("findAbonnementBetweenDateDebDateFin successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAbonnementBetweenDateDebDateFin failed", re);
			throw re;
		}
	}
	
	public TaAbonnementOld selectDernierAbonnement(Integer idSupport) {
		try {
			TaAbonnementOld instance =null;
			List<TaAbonnementOld> resultat=null;
			if(idSupport!=0){
			Query query = entityManager.createQuery("select f from TaAbonnement f " +
					" join f.taSupportAbon s where s.idSupportAbon="+idSupport+" and f.taTiers=s.taTiers " +
							" order by f.dateDebut desc rows 1");
			
			resultat= query.getResultList();
			if(resultat!=null && resultat.size()>0)instance=resultat.get(0);
			logger.debug("get successful");
			return instance;
			}
			return null;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	
	public List<TaAbonnementOld> selectAbonnementFacture(TaFacture taFacture) {
		// TODO Auto-generated method stub
		List<TaAbonnementOld> l=null;
		logger.debug("selectAbonnementFacture");
		try {
			if(taFacture!=null && taFacture.getIdDocument()!=0){
			Query query = entityManager.createQuery("select f from TaAbonnement f " +
					" join f.taLDocument l join l.taDocument d where d.idDocument="+taFacture.getIdDocument() );
			l = query.getResultList();
			}
			logger.debug("selectAbonnementFacture successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAbonnementFacture failed", re);
			throw re;
		}
	}
	
	public List<TaAbonnementOld> selectAbonnementSupport(TaSupportAbon taSupportAbon) {
		// TODO Auto-generated method stub
		List<TaAbonnementOld> l=null;
		logger.debug("selectAbonnementSupport");
		try {
			if(taSupportAbon!=null && taSupportAbon.getIdSupportAbon()!=0){
			Query query = entityManager.createQuery("select f from TaAbonnement f " +
					" join f.taSupportAbon s where s.idSupportAbon="+taSupportAbon.getIdSupportAbon() );
			l = query.getResultList();
			}
			logger.debug("selectAbonnementSupport successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAbonnementSupport failed", re);
			throw re;
		}
	}

	@Override
	public List<TaAbonnementOld> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaAbonnementOld> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaAbonnementOld> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaAbonnementOld> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaAbonnementOld value) throws Exception {
		BeanValidator<TaAbonnementOld> validator = new BeanValidator<TaAbonnementOld>(TaAbonnementOld.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaAbonnementOld value, String propertyName)
			throws Exception {
		BeanValidator<TaAbonnementOld> validator = new BeanValidator<TaAbonnementOld>(TaAbonnementOld.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaAbonnementOld transientInstance) {
		// TODO Auto-generated method stub
		entityManager.detach(transientInstance);
	}

}

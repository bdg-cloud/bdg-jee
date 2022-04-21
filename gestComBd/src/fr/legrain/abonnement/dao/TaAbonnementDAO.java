package fr.legrain.abonnement.dao;

// Generated Mar 25, 2009 10:06:50 AM by Hibernate Tools 3.2.0.CR1

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;

import fr.legrain.SupportAbon.dao.TaSupportAbon;
import fr.legrain.documents.dao.TaFacture;
import fr.legrain.gestCom.Appli.Const;

/**
 * Home object for domain model class TaCPaiement.
 * @see fr.legrain.tiers.dao.test.TaCPaiement
 * @author Hibernate Tools
 */
public class TaAbonnementDAO /*extends AbstractApplicationDAO<TaAbonnement>*/{

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
	
	public void persist(TaAbonnement transientInstance) {
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

	public void remove(TaAbonnement persistentInstance) {
		logger.debug("removing TaAbonnement instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaAbonnement merge(TaAbonnement detachedInstance) {
		logger.debug("merging TaAbonnement instance");
		try {
			TaAbonnement result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaAbonnement findById(int id) {
		logger.debug("getting TaAbonnement instance with id: " + id);
		try {
			TaAbonnement instance = entityManager.find(TaAbonnement.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	public TaAbonnement findByCode(String code) {
		logger.debug("getting TaAbonnement instance with code: " + code);
		try {
			if(!code.equals("")){
			Query query = entityManager.createQuery("select f from TaAbonnement f where f.codeAbonnement='"+code+"'");
			TaAbonnement instance = (TaAbonnement)query.getSingleResult();
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
	public List<TaAbonnement> selectAll() {
		// TODO Auto-generated method stub
		logger.debug("selectAll TaAbonnement");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaAbonnement> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	public List<TaAbonnement> findAbonnementBetweenDateDebDateFin(Date date, Date datefin , String codeTiers) {
		// TODO Auto-generated method stub
		List<TaAbonnement>listeFinale=new LinkedList<TaAbonnement>();
		List<TaAbonnement> l =null;
		logger.debug("findAbonnementBetweenDateDebDateFin");
		try {
			if(codeTiers == null || codeTiers.equals(""))codeTiers="%";
			Query ejbQuery =null;
			ejbQuery = entityManager.createQuery("select a from TaAbonnement a join a.taTiers t join a.taSupportAbon ts where a.dateFin in(" +
                    " select max(a3.dateFin) from TaAbonnement a3 where a3.taSupportAbon=a.taSupportAbon  ) and a.dateFin between ?  and ?" +
                    " and t.codeTiers like ? and not exists(select e from TaLEcheance e join e.taAbonnement ab where ab.idAbonnement=a.idAbonnement)  " +
                    " and not exists(select a2 from TaAbonnement a2 where a2.taSupportAbon=a.taSupportAbon and  a2.dateFin between ?  and ? and  a2.dateFin>a.dateFin)" +
    				" and ts.inactif<>1 " );
			ejbQuery.setParameter(1, date,TemporalType.DATE);
			ejbQuery.setParameter(2, datefin,TemporalType.DATE);
			ejbQuery.setParameter(3, codeTiers);
			ejbQuery.setParameter(4, date,TemporalType.DATE);
			ejbQuery.setParameter(5, datefin,TemporalType.DATE);
			 l = ejbQuery.getResultList();

			
			logger.debug("findAbonnementBetweenDateDebDateFin successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("findAbonnementBetweenDateDebDateFin failed", re);
			throw re;
		}
	}
	
	public TaAbonnement selectDernierAbonnement(Integer idSupport) {
		try {
			TaAbonnement instance =null;
			List<TaAbonnement> resultat=null;
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
	
	
	public List<TaAbonnement> selectAbonnementFacture(TaFacture taFacture) {
		// TODO Auto-generated method stub
		List<TaAbonnement> l=null;
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
	
	public List<TaAbonnement> selectAbonnementSupport(TaSupportAbon taSupportAbon) {
		// TODO Auto-generated method stub
		List<TaAbonnement> l=null;
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

}

package fr.legrain.documents.dao.jpa;

// Generated Apr 7, 2009 3:27:24 PM by Hibernate Tools 3.2.0.CR1

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import fr.legrain.conformite.model.TaResultatConformite;
import fr.legrain.document.model.TaLBonReception;
import fr.legrain.documents.dao.ILBonReceptionDAO;
import fr.legrain.validator.BeanValidator;

/**
 * Home object for domain model class TaLBonReception.
 * @see fr.legrain.documents.dao.TaLBonReception
 * @author Hibernate Tools
 */
public class TaLBonReceptionDAO /*extends AbstractApplicationDAO<TaLBonReception>*/ implements ILBonReceptionDAO{

//	private static final Log log = LogFactory.getLog(TaLBonReceptionDAO.class);
	static Logger logger = Logger.getLogger(TaLBonReceptionDAO.class);
	
	@PersistenceContext(unitName = "bdg")
	private EntityManager entityManager;
	
	private String defaultJPQLQuery = "select a from TaLBonReception a";
	
	public TaLBonReceptionDAO(){
//		this(null);
	}
	
//	public TaLBonReceptionDAO(EntityManager em){
//		if(em!=null) {
//			setEm(em);
//		}
////		champIdTable=ctrlGeneraux.getID_TABLE(TaLBonReception.class.getSimpleName());
//		initChampId(TaLBonReception.class);
//		setJPQLQuery(defaultJPQLQuery);
//		setListeChampMaj(Const.C_FICHIER_INI_CHAMPMAJ);
//		initNomTable(new TaLBonReception());
//	}
	
//	public TaLBonReception refresh(TaLBonReception detachedInstance) {
//		logger.debug("refresh instance");
//		try {
//			detachedInstance.setLegrain(false);
//        	entityManager.refresh(detachedInstance);
////			org.hibernate.Session session = (org.hibernate.Session) entityManager.getDelegate();
////		    session.evict(detachedInstance);
////		    detachedInstance = entityManager.find(TaLBonReception.class, detachedInstance.getIdLDocument());
//			detachedInstance.setLegrain(true);
//		    return detachedInstance;
//			
//		} catch (RuntimeException re) {
//			logger.error("refresh failed", re);
//			throw re;
//		}
//	}
	
	public void persist(TaLBonReception transientInstance) {
		logger.debug("persisting TaLBonReception instance");
		try {
			entityManager.persist(transientInstance);
			logger.debug("persist successful");
		} catch (RuntimeException re) {
			logger.error("persist failed", re);
			throw re;
		}
	}

	public void remove(TaLBonReception persistentInstance) {
		logger.debug("removing TaLBonReception instance");
		try {
			entityManager.remove(persistentInstance);
			logger.debug("remove successful");
		} catch (RuntimeException re) {
			logger.error("remove failed", re);
			throw re;
		}
	}

	public TaLBonReception merge(TaLBonReception detachedInstance) {
		logger.debug("merging TaLBonReception instance");
		try {
			TaLBonReception result = entityManager.merge(detachedInstance);
			logger.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			logger.error("merge failed", re);
			throw re;
		}
	}

	public TaLBonReception findById(int id) {
		logger.debug("getting TaLBonReception instance with id: " + id);
		try {
			TaLBonReception instance = entityManager.find(TaLBonReception.class, id);
			logger.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			logger.error("get failed", re);
			throw re;
		}
	}
	
	/* (non-Javadoc)
	 * @see fr.legrain.articles.dao.ILgrDAO#selectAll()
	 */
	public List<TaLBonReception> selectAll() {
		logger.debug("selectAll TaLBonReception");
		try {
			Query ejbQuery = entityManager.createQuery(defaultJPQLQuery);
			List<TaLBonReception> l = ejbQuery.getResultList();
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}

	
	public List<TaLBonReception> findLigneAvecResultatConformites(int idBr) {
		logger.debug("selectAll TaLBonReception");
		try {
			String query="select lbr from TaLBonReception lbr join lbr.taLot l left join l.taResultatConformites where lbr.taDocument.idDocument ="+idBr;
			Query ejbQuery = entityManager.createQuery(query);
			List<TaLBonReception> l = ejbQuery.getResultList();
			for (TaLBonReception taLBonReception : l) {
				for (TaResultatConformite ligne : taLBonReception.getTaLot().getTaResultatConformites()) {
//					ligne.getTaResultatCorrections();
				}
			}
			logger.debug("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			logger.error("selectAll failed", re);
			throw re;
		}
	}
	
	@Override
	public TaLBonReception findByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaLBonReception> findWithNamedQuery(String queryName) {
		try {
			Query ejbQuery = entityManager.createNamedQuery(queryName);
			List<TaLBonReception> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public List<TaLBonReception> findWithJPQLQuery(String JPQLquery) {
		try {
			Query ejbQuery = entityManager.createQuery(JPQLquery);
			List<TaLBonReception> l = ejbQuery.getResultList();
			System.out.println("selectAll successful");
			return l;
		} catch (RuntimeException re) {
			System.out.println("selectAll failed");
			re.printStackTrace();
			throw re;
		}
	}

	@Override
	public boolean validate(TaLBonReception value) throws Exception {
		BeanValidator<TaLBonReception> validator = new BeanValidator<TaLBonReception>(TaLBonReception.class);
		return validator.validate(value);
	}

	@Override
	public boolean validateField(TaLBonReception value, String propertyName)
			throws Exception {
		BeanValidator<TaLBonReception> validator = new BeanValidator<TaLBonReception>(TaLBonReception.class);
		return validator.validateField(value,propertyName);
	}

	@Override
	public void detach(TaLBonReception transientInstance) {
		entityManager.detach(transientInstance);
	}
}
